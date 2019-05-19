package com.example.seacen.news.News;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.seacen.news.Account.Account;
import com.example.seacen.news.Account.LoginActivity;
import com.example.seacen.news.Account.UserManagerActivity;
import com.example.seacen.news.R;
import com.example.seacen.news.Utils.Network.SCNetworkHandler;
import com.example.seacen.news.Utils.Network.SCNetworkMethod;
import com.example.seacen.news.Utils.Network.SCNetworkPort;
import com.example.seacen.news.Utils.Network.SCNetworkTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsActivity extends AppCompatActivity {
    static String TAG = "NewsActivity";

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private NewsContentFragmentAdapter adapter;
    private List<NewsClassifyModel> classifyModels;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        ButterKnife.bind(this);

        getSupportActionBar().hide();
        adapter = new NewsContentFragmentAdapter(getSupportFragmentManager());

        initClassify();
        loadClassify();
        adapter.setList(classifyModels);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initClassify() {
        // 要闻 就是 首页
        classifyModels = new ArrayList<>();
        NewsClassifyModel home = new NewsClassifyModel();
        home.setId(0);
        home.setName("要闻");
        classifyModels.add(home);
    }

    private void loadClassify() {
        // TODO: - 根据后台获取分类然后本地化数据
        SCNetworkTool.shared().normalRequest(SCNetworkPort.AllClassify, SCNetworkMethod.GET, null, new SCNetworkHandler() {
            @Override
            public void successHandle(JSONObject jsonObject) {
                JSONArray array = jsonObject.getJSONArray("data");
                List<NewsClassifyModel> data =  array.toJavaList(NewsClassifyModel.class);
                for (NewsClassifyModel model: data) {
                    classifyModels.add(model);
                }
                adapter.setList(classifyModels);
            }

            @Override
            public void errorHandle(Exception error) {
                Toast toast = Toast.makeText(NewsActivity.this,"分类加载错误", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @OnClick(R.id.news_activity_user_iv)
    void userBtnClick() {
        if (Account.shared().isLogin()) {
            // 进入用户管理界面
            Intent intent = new Intent(NewsActivity.this, UserManagerActivity.class);
            startActivity(intent);
        } else {
            // 进入登录注册界面
            Intent intent = new Intent(NewsActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}

