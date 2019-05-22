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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.seacen.news.Comment.CommentActivity;
import com.example.seacen.news.R;
import com.example.seacen.news.Utils.Network.SCNetworkHandler;
import com.example.seacen.news.Utils.Network.SCNetworkMethod;
import com.example.seacen.news.Utils.Network.SCNetworkPort;
import com.example.seacen.news.Utils.Network.SCNetworkTool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsDetailActivity extends AppCompatActivity {

    @BindView(R.id.news_detail_activity_wb)
    WebView webView;
    @BindView(R.id.news_detail_floting_Btn)
    View commentIv;
    @BindView(R.id.news_detail_activity_title_tv)
    TextView titleTv;

    NewsModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_activity);
        ButterKnife.bind(this);

        // 获取传来的值
        model = (NewsModel) getIntent().getSerializableExtra("news_model");
        if (model == null) {
            return;
        }

        setupNavigation();
        setupWebView();

        titleTv.setText(model.getTitle());

        // 请求详细
        SCNetworkPort port = SCNetworkPort.NewsDetail;
        port.setSuffix(model.getId());
        SCNetworkTool.shared().normalRequest(port, SCNetworkMethod.GET, null, new SCNetworkHandler() {
            @Override
            public void successHandle(JSONObject jsonObject) {
                Integer code = jsonObject.getInteger("status");
                String msg = jsonObject.getString("msg");
                JSONObject data = jsonObject.getJSONObject("data");
                model = data.toJavaObject(NewsModel.class);
                String htmlString = model.getContent();
                htmlString = addHeadContent(htmlString, "https:");

                StringBuilder sb = new StringBuilder();
                sb.append("<HTML><HEAD><LINK href=\"webview.min.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
//                sb.append("<h1>");
//                sb.append(model.title);
//                sb.append("</h1>");
                sb.append(htmlString);
                sb.append("</body></HTML>");
                webView.loadDataWithBaseURL("file:///android_asset/", sb.toString(), "text/html", "utf-8", null);
            }

            @Override
            public void errorHandle(Exception error) {
                Toast toast = Toast.makeText(NewsDetailActivity.this, error.toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    /**
     * 为 src 添加URL头部
     *
     * @param content
     * @param addHttp
     * @return
     */
    String addHeadContent(String content, String addHttp) {
        String patternStr = "<img\\s*([^>]*)\\s*src=\\\"(.*?)\\\"\\s*([^>]*)>";
        Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        String result = content;
        while (matcher.find()) {
            String src = matcher.group(2);
            String replaceSrc = "";
            replaceSrc = addHttp + src;
            result = result.replaceAll(src, replaceSrc);
        }
        return result;
    }

    //    @OnClick(R.id.news_detail_activity_comment_iv)
    @OnClick(R.id.news_detail_floting_Btn)
    void commentClick() {
        Intent intent = new Intent(NewsDetailActivity.this, CommentActivity.class);
        intent.putExtra("newsid", model.id.intValue());
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
        webView.setWebViewClient(new WebViewClient() {
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
