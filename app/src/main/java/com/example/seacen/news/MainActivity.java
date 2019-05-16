package com.example.seacen.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.seacen.news.News.NewsModel;
import com.example.seacen.news.Utils.Network.SCJsonResponse;
import com.example.seacen.news.Utils.StreamUtils;

/**
 * 用于测试的 Activity
 */
public class MainActivity extends AppCompatActivity {
    static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testJSON();
    }

    void testJSON() {
        String json = StreamUtils.get(MainActivity.this, R.raw.test_news);
        SCJsonResponse<NewsModel> obj = SCJsonResponse.parseObject(json);
        NewsModel model = obj.getData();
        Log.i(TAG, obj.getMsg());
    }
}
