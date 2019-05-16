package com.example.seacen.news.Utils.Network;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// 172.20.10.6

/**
 * 网络工具类
 */
public class SCNetworkTool {
    static int timeout = 5000;
    static String TAG = "SCNetworkTool";

    /**
     * 单例的静态内部类
     */
    private  static  class SingletonHolder {
        private static final  SCNetworkTool INSTANCE = new SCNetworkTool();
    }
    private SCNetworkTool () {
        // 设置线程策略
        setPolicy();
    }
    public  static final SCNetworkTool shared() {
        return SingletonHolder.INSTANCE;
    }

    public void normalEequest(SCNetworkPort port, SCNetworkMethod method, Map<String, String> params, SCNetworkHandler handler) {
        okCoreRequeest(port.path(), method, params, handler);
    }

    /**
     * 核心请求方法
     * @param urlstr
     * @param method
     * @param params
     * @param handler
     */
    public void okCoreRequeest(String urlstr, SCNetworkMethod method, Map<String, String> params, final SCNetworkHandler handler) {
        OkHttpClient okHttpClient = genericClient();
        Request request = null;
        try {
            URL url = new URL(urlstr);
            switch (method) {
                case GET:
                    url = jointGetURL(url, params);
                    request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();
                    break;
                case POST:
                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
                    String requestBody = getRequestData(params, "utf-8").toString();
                    request = new Request.Builder()
                            .url(url)
                            .post(RequestBody.create(mediaType, requestBody))
                            .build();
                    break;
            }
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: ");
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //已在主线程中，可以更新UI
                            handler.errorHandle(e);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
//                    Log.d(TAG, "onResponse: " + response.body().string());
                    String body = response.body().string();
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //已在主线程中，可以更新UI
                            handler.successHandle(body);
                        }
                    });
                }
            });
        }  catch (IOException e) {
            e.printStackTrace();
            handler.errorHandle(e);
            return;
        }

    }

    /**
     * 用于提供一个统一添加请求头的位置
     * @return OkHttpClient
     */
    private static OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("device", "Andriod")
                                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .build();
                        return chain.proceed(request);
                    }

                })
                .build();

        return httpClient;
    }

    /**
     * 拼接GET连接
     * @param url 原连接
     * @param params 参数字典
     * @return 拼接好的URL
     * @throws MalformedURLException
     */
    private URL jointGetURL(URL url, Map<String, String> params) throws MalformedURLException {
        if (params.isEmpty())
            return url;
        StringBuffer buffer = new StringBuffer(url.toString());
        boolean isFirst = true;
        buffer.append("?");
        for (Map.Entry<String, String> entry: params.entrySet()) {
            buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        //删除最后的一个"&"
        buffer.deleteCharAt(buffer.length() - 1);
        URL res = new URL(buffer.toString());
        return  res;
    }

    private URL jointGetURL(String urlstr, Map<String, String> params) throws MalformedURLException {
        URL url = new URL(urlstr);
        return jointGetURL(url, params);
    }

    /**
     * 获取请求数据
     * @param params 参数字典
     * @param encode 编码格式
     * @return StringBuffer
     */
    public StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            //删除最后的一个"&"
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    void setPolicy() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects() //探测SQLite数据库操作
                .penaltyLog() //打印logcat
                .penaltyDeath()
                .build());

    }
}