package com.example.seacen.news.Utils.Network;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络工具类
 */
public class SCNetworkTool {
    static int timeout = 5000;
    static String TAG = "SCNetworkTool";

    /**
     * 单例的静态内部类
     */
    private static class SingletonHolder {
        private static final SCNetworkTool INSTANCE = new SCNetworkTool();
    }

    private SCNetworkTool() {
        // 设置线程策略
        setPolicy();
    }

    public static final SCNetworkTool shared() {
        return SingletonHolder.INSTANCE;
    }

    private OkHttpClient client = null;

    /**
     * okhttpClient 配置
     *
     * @return OkHttpClient
     */
    private static OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() { // 拦截器
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Device", "Andriod")
//                                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .addHeader("Content-Type", "application/json; charset=UTF-8")
                                .build();
                        return chain.proceed(request);
                    }

                })
                .cookieJar(new CookieJar() { // 添加 cookie
                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList();
                    }
                })
                .build();

        return httpClient;
    }

    public void loadClassifyNews(String name, SCNetworkHandler handler) {
        if (name.equals("要闻")) {
            okCoreRequest(SCNetworkPort.IndexNews.path(), SCNetworkMethod.GET, null, handler);
            return;
        }
        try {
            String encodeName = URLEncoder.encode(name, "utf-8");
            String urlStr = SCNetworkPort.root + "/" + encodeName;
            okCoreRequest(urlStr, SCNetworkMethod.GET, null, handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 普通请求方法
     * @param port
     * @param method
     * @param params
     * @param handler
     */
    public void normalRequest(SCNetworkPort port, SCNetworkMethod method, Map<String, Object> params, SCNetworkHandler handler) {
        okCoreRequest(port.path(), method, params, handler);
    }

    /**
     * 核心请求方法
     *
     * @param urlstr
     * @param method
     * @param params
     * @param handler
     */
    public void okCoreRequest(String urlstr, SCNetworkMethod method, Map<String, Object> params, final SCNetworkHandler handler) {
        if (client == null) {
            client = genericClient();
        }
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
                    MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
                    String requestBody = JSONObject.toJSON(params).toString();
                    request = new Request.Builder()
                            .url(url)
                            .post(RequestBody.create(mediaType, requestBody))
                            .build();
                    break;
                case DELETE:
                    url = jointGetURL(url, params);
                    request = new Request.Builder()
                            .url(url)
                            .delete()
                            .build();
                    break;
                case PUT:
                    MediaType mediaTypePut = MediaType.parse("application/json; charset=UTF-8");
                    String putBody = JSONObject.toJSON(params).toString();
                    request = new Request.Builder()
                            .url(url)
                            .put(RequestBody.create(mediaTypePut, putBody))
                            .build();
                    break;
            }

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.d(TAG, "onFailure: ");
                    mainThreadCallBack(null, e, handler);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
//                    Log.d(TAG, "onResponse: " + response.body().string());
                    String header = response.header("Content-Type");
                    if (!(header.equals("application/json;charset=UTF-8") || header.equals("application/json;charset=utf-8"))) {
                        // 后台不返回 JSON 视为错误
                        Exception exception = new Exception("后台返回格式错误");
                        mainThreadCallBack(null, exception, handler);
                        return;
                    }
                    String body = response.body().string();
                    // fastJSON 转换
                    JSONObject jsonObject = JSONObject.parseObject(body);
                    // 返回主线程回调
                    mainThreadCallBack(jsonObject, null, handler);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            mainThreadCallBack(null, e, handler);
            return;
        }

    }

    private void mainThreadCallBack(JSONObject jsonObject, Exception exception, SCNetworkHandler handler) {
        // 返回主线程回调
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //已在主线程中，可以更新UI
                if (jsonObject != null) {
                    handler.successHandle(jsonObject);
                } else if (exception != null) {
                    handler.errorHandle(exception);
                }
            }
        });
    }

    /**
     * 拼接GET连接
     *
     * @param url    原连接
     * @param params 参数字典
     * @return 拼接好的URL
     * @throws MalformedURLException
     */
    private URL jointGetURL(URL url, Map<String, Object> params) throws MalformedURLException {
        if (params == null || params.isEmpty())
            return url;
        StringBuffer buffer = new StringBuffer(url.toString());
        boolean isFirst = true;
        buffer.append("?");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        //删除最后的一个"&"
        buffer.deleteCharAt(buffer.length() - 1);
        URL res = new URL(buffer.toString());
        return res;
    }

    private URL jointGetURL(String urlstr, Map<String, Object> params) throws MalformedURLException {
        URL url = new URL(urlstr);
        return jointGetURL(url, params);
    }

    /**
     * 获取请求数据
     *
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
