package com.example.seacen.news.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.seacen.news.News.NewsActivity;
import com.example.seacen.news.R;
import com.example.seacen.news.Utils.Network.*;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    static String TAG = "LoginActivity";

    @BindView(R.id.login_activity_username_Et)
    EditText userEt;
    @BindView(R.id.login_activity_password_Et)
    EditText passwordEt;
    @BindView(R.id.login_activity_login_btn)
    Button loginBtn;
    @BindView(R.id.login_activity_register_btn)
    Button registerBtn;
    @BindView(R.id.login_activity_trip_btn)
    Button tripBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        setupNavigationBar();
    }

    @OnClick(R.id.login_activity_register_btn)
    void registerClick() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                intent.setAction("com.seacen.register");
        startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
    }

    @OnClick(R.id.login_activity_login_btn)
    void loginClick() {
        // TODO: -  登录操作
        Map<String, Object> params = new HashMap<>();
        params.put("name", userEt.getText().toString());
        params.put("passwd", passwordEt.getText().toString());
        SCNetworkTool.shared().normalEequest(SCNetworkPort.Login, SCNetworkMethod.POST, params, new SCNetworkHandler() {
            @Override
            public void successHandle(String bodyStr) {
                Log.i(TAG, bodyStr);
                TextView textView = findViewById(R.id.login_activity_test_tv);
                textView.setText(bodyStr);
            }

            @Override
            public void errorHandle(Exception error) {
                Log.i(TAG, error.toString());
                TextView textView = findViewById(R.id.login_activity_test_tv);
                textView.setText(error.toString());
            }
        });
    }

    @OnClick(R.id.login_activity_trip_btn)
    void tripClick() {
        // TODO: - 游客登录操作
        // 直接跳转新闻界面测试
        Intent intent = new Intent(LoginActivity.this, NewsActivity.class);
        startActivity(intent);
    }

    void setupNavigationBar() {
        ActionBar navigationBar = getSupportActionBar();
        navigationBar.setTitle("登录");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
