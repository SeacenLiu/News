package com.example.seacen.news.Account;

import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.example.seacen.news.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordModify extends AppCompatActivity {

    @BindView(R.id.password_activity_origin_Et)
    EditText originEt;
    @BindView(R.id.password_activity_password_Et)
    EditText passwordEt;
    @BindView(R.id.password_activity_passwordAg_Et)
    EditText passwordAgEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_manager_activity);
        ButterKnife.bind(this);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("修改密码");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.password_activity_modify_btn)
    void modifyBtnClick() {
        String origin = originEt.getText().toString();
        String passwd = passwordEt.getText().toString();
        String passwdAg = passwordAgEt.getText().toString();
        if (passwd.isEmpty() || passwdAg.isEmpty() || origin.isEmpty()) {
            Toast.makeText(this, "信息不完整", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!passwd.equals(passwdAg)) {
            Toast.makeText(this, "确认密码失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if (passwd.equals(origin)) {
            Toast.makeText(this, "新密码应与原密码不同", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO: - 修改密码接口对接

    }
}
