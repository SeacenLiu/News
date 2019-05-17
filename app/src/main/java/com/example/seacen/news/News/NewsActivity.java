package com.example.seacen.news.News;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.seacen.news.R;
import com.example.seacen.news.Utils.Network.SCNetworkHandler;
import com.example.seacen.news.Utils.Network.SCNetworkMethod;
import com.example.seacen.news.Utils.Network.SCNetworkPort;
import com.example.seacen.news.Utils.Network.SCNetworkTool;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    static String TAG = "NewsActivity";

    TabLayout tabLayout;
    ViewPager viewPager;

    private NewsContentFragmentAdapter adapter;
    private List<String> names;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        adapter = new NewsContentFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        testClassify();
        adapter.setList(names);
    }

    private void loadClassify() {
        // FIXME: - 根据后台获取分类然后本地化数据
    }

    private void testClassify() {
        names = new ArrayList<>();
        names.add("要闻");
        names.add("体育");
        names.add("教育");
        names.add("财经");
        names.add("社会");
        names.add("娱乐");
        names.add("军事");
        names.add("国内");
        names.add("科技");
        names.add("互联网");
        names.add("房产");
        names.add("国际");
        names.add("汽车");
        names.add("游戏");
//        names = new ArrayList<>();
//        names.add("关注");
//        names.add("推荐");
//        names.add("热点");
//        names.add("视频");
//        names.add("小说");
//        names.add("娱乐");
//        names.add("问答");
//        names.add("图片");
//        names.add("科技");
//        names.add("懂车帝");
//        names.add("体育");
//        names.add("财经");
//        names.add("军事");
//        names.add("国际");
//        names.add("健康");
    }
}

