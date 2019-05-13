package com.example.seacen.news.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.seacen.news.News.NewsActivity;
import com.example.seacen.news.R;
import com.example.seacen.news.Utils.Network.*;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    static String TAG = "LoginActivity";

    EditText userEt, passwordEt;
    Button loginBtn, registerBtn, tripBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        userEt = findViewById(R.id.login_activity_username_Et);
        passwordEt = findViewById(R.id.login_activity_password_Et);
        loginBtn = findViewById(R.id.login_activity_login_btn);
        registerBtn = findViewById(R.id.login_activity_register_btn);
        tripBtn = findViewById(R.id.login_activity_trip_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                intent.setAction("com.seacen.register");
                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: -  登录操作
                Map<String, String> params = new HashMap<>();
                params.put("logname", userEt.getText().toString());
                params.put("password", passwordEt.getText().toString());
                // Get 测试
//                SCNetworkTool.shared().okCoreRequeest(
//                        "http://172.20.10.6:8080/loginServlet",
//                        SCNetworkMethod.GET,
//                        params,
//                        new SCNetworkHandler() {
//                            @Override
//                            public void successHandle(String bodyStr) {
//                                Log.i(TAG, bodyStr);
//                                TextView tv = findViewById(R.id.login_activity_test_tv);
//                                tv.setText(bodyStr);
//                            }
//
//                            @Override
//                            public void errorHandle(Exception error) {
//                                Log.i(TAG, error.toString());
//                            }
//                        });
                SCNetworkTool.shared().okCoreRequeest(
                        "http://172.20.10.6:8080/loginServlet",
                        SCNetworkMethod.POST,
                        params,
                        new SCNetworkHandler() {
                            @Override
                            public void successHandle(String bodyStr) {
                                Log.i(TAG, bodyStr);
                                TextView tv = findViewById(R.id.login_activity_test_tv);
                                tv.setText(bodyStr);
                            }

                            @Override
                            public void errorHandle(Exception error) {
                                Log.i(TAG, error.toString());
                            }
                        });
            }
        });

        tripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: - 游客登录操作
                // 直接跳转新闻界面测试
                Intent intent = new Intent(LoginActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });

        setupNavigationBar();
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
