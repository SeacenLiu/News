package com.example.seacen.news.News;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.seacen.news.Comment.CommentActivity;
import com.example.seacen.news.R;
import com.example.seacen.news.Utils.Network.SCJsonResponse;
import com.example.seacen.news.Utils.Network.SCNetworkHandler;
import com.example.seacen.news.Utils.Network.SCNetworkMethod;
import com.example.seacen.news.Utils.Network.SCNetworkPort;
import com.example.seacen.news.Utils.Network.SCNetworkTool;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsDetailActivity extends AppCompatActivity {

    @BindView(R.id.news_detail_activity_wb)
    WebView webView;
    @BindView(R.id.news_detail_activity_comment_iv)
    View commentIv;

    NewsModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_activity);
        ButterKnife.bind(this);

        // 获取传来的值
        NewsModel newsModel = (NewsModel) getIntent().getSerializableExtra("news_model");
        if (newsModel == null) {
            return;
        }

        setupNavigation();
        setupWebView();
//        webView.loadUrl("http://www.baidu.com");
        String path = SCNetworkPort.NewsDetail.path();
        path += "/";
        path += String.valueOf(newsModel.getId());
        SCNetworkTool.shared().okCoreRequeest(path, SCNetworkMethod.GET, null, new SCNetworkHandler() {
            @Override
            public void successHandle(String bodyStr) {
                JSONObject response = JSONObject.parseObject(bodyStr);
                Integer code = response.getInteger("status");
                String msg = response.getString("msg");
                JSONObject jsonObject = response.getJSONObject("data");
                NewsModel model = jsonObject.toJavaObject(NewsModel.class);
                String htmlString = model.getContent();
                htmlString = repairContent(htmlString, "https:", 500);

                StringBuilder sb = new StringBuilder();
                sb.append("<HTML><HEAD><LINK href=\"webview.min.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
                sb.append(htmlString);
                sb.append("</body></HTML>");
                webView.loadDataWithBaseURL("file:///android_asset/", sb.toString(), "text/html", "utf-8", null);
            }

            @Override
            public void errorHandle(Exception error) {

            }
        });
    }

    String repairContent(String content,String replaceHttp,int size){
        String patternStr="<img\\s*([^>]*)\\s*src=\\\"(.*?)\\\"\\s*([^>]*)>";
        Pattern pattern = Pattern.compile(patternStr,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        String result = content;
        while(matcher.find()) {
            String src = matcher.group(2);
            String replaceSrc = "";
//            if(src.lastIndexOf(".")>0){
//                replaceSrc = src.substring(0,src.lastIndexOf("."))+"_"+size+src.substring(src.lastIndexOf("."));
//            }
//            if(!src.startsWith("http://")&&!src.startsWith("https://")){
//                replaceSrc = replaceHttp + replaceSrc;
//            }
            replaceSrc = replaceHttp + src;
            result = result.replaceAll(src,replaceSrc);
        }
        return result;
    }

    @OnClick(R.id.news_detail_activity_comment_iv)
    void commentClick() {
        Intent intent = new Intent(NewsDetailActivity.this, CommentActivity.class);
        transitionTo(intent);
    }

    void transitionTo(Intent i) {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, new Pair<>(commentIv, "comment"));
        startActivity(i, transitionActivityOptions.toBundle());
    }

    private void setupNavigation() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("新闻详细");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupWebView() {
        // 防止跳转到浏览器
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        System.out.println(item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
