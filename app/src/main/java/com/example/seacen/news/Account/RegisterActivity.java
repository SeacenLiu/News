package com.example.seacen.news.Account;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.seacen.news.R;


public class RegisterActivity extends AppCompatActivity {

    EditText userEt, passwordEt, passwordAgEt;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        userEt = findViewById(R.id.register_activity_username_Et);
        passwordEt = findViewById(R.id.register_activity_password_Et);
        passwordAgEt = findViewById(R.id.register_activity_passwordAg_Et);
        registerBtn = findViewById(R.id.register_activity_register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: - 注册操作

            }
        });

        setupNavigationBar();
    }

    void setupNavigationBar() {
        ActionBar navigationBar = getSupportActionBar();
        navigationBar.setTitle("注册");
        navigationBar.setDisplayHomeAsUpEnabled(true);
    }

    // 有这个方法才可以跳回前一个界面
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
