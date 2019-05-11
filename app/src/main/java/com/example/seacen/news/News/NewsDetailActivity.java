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

import com.example.seacen.news.Comment.CommentActivity;
import com.example.seacen.news.R;

public class NewsDetailActivity extends AppCompatActivity {

    WebView webView;
    View commentIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_activity);

        webView = findViewById(R.id.news_detail_activity_wb);
        setupNavigation();
        setupWebView();
        webView.loadUrl("http://www.baidu.com");

        commentIv = findViewById(R.id.news_detail_activity_comment_iv);
        commentIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetailActivity.this, CommentActivity.class);
                transitionTo(intent);
            }
        });
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
