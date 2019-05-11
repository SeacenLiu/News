package com.example.seacen.news.Tools;

import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 网络请求处理
 */
interface SCNetworkHandler {
    void successHandle(String bodyStr);
    void errorHandle(Exception error);
}

/**
 * 网络请求方法
 */
enum SCNetworkMethod {
    GET("GET"), POST("POST");
    private String methodName;
    SCNetworkMethod(String name) {
        this.methodName = name;
    }
    public String getName() {
        return methodName;
    }
}

/**
 * 网络工具类
 */
public class SCNetworkTool {
    static int timeout = 5000;

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

    private void setCommonHeader(HttpURLConnection connection) {
        connection.setRequestProperty("device", "Andriod");
    }

    /**
     * 网络请求核心方法
     * @param urlStr 请求连接的字符串
     * @param method 请求方法
     * @param params 参数字典
     * @param handler 回调
     */
    public void coreRequest(String urlStr, SCNetworkMethod method, Map<String, String> params, SCNetworkHandler handler) {
        try {
            URL url = new URL(urlStr);
            coreRequest(url, method, params, handler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            handler.errorHandle(e);
            return;
        }
    }

    /**
     * 网络请求核心方法
     * @param url 请求URL
     * @param method 请求方法
     * @param params 参数字典
     * @param handler 回调
     */
    public void coreRequest(URL url, SCNetworkMethod method, Map<String, String> params, SCNetworkHandler handler) {
        HttpURLConnection connection = null;
        // 创建连接
        try {
            if (method == SCNetworkMethod.GET) {
                connection = (HttpURLConnection) jointGetURL(url, params).openConnection();
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }

            // 设置请求头
            setCommonHeader(connection);

            // 设置超时时长
            connection.setConnectTimeout(timeout);
            // 设置请求方式
            connection.setRequestMethod(method.getName());
            // 方法特定设置
            switch (method) {
                case GET:
                    break;
                case POST:
                    byte[] data = getRequestData(params, "utf-8").toString().getBytes();
                    //打开输入流，以便从服务器获取数据　
                    connection.setDoInput(true);
                    //打开输出流，以便向服务器提交数据
                    connection.setDoOutput(true);
                    //设置请求体的类型是文本类型
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //设置请求体的长度
                    connection.setRequestProperty("Content-Length", String.valueOf(data.length));
                    //获得输出流，向服务器写入数据
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(data);
                    break;
            }
            // 获取相应
            int responseCode = connection.getResponseCode();
            BufferedReader buffer = null;
            StringBuilder responseBuffer = new StringBuilder();
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                    buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    for (String s = buffer.readLine(); s != null; s = buffer.readLine()) {
                        responseBuffer.append(s);
                    }
                    // 请求成功回调
                    handler.successHandle(responseBuffer.toString());
                    buffer.close();
                    break;
                default:

                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            handler.errorHandle(e);
            return;
        }
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
