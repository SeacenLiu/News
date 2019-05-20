package com.example.seacen.news.Account;

import android.accounts.AbstractAccountAuthenticator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seacen.news.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserManagerActivity extends AppCompatActivity {

    @BindView(R.id.user_manager_username)
    TextView userTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_manager_activity);
        ButterKnife.bind(this);

        userTv.setText(Account.shared().user.name);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("用户管理");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.user_manager_change)
    void modifyClick() {
        
    }

    @OnClick(R.id.user_manager_logout)
    void logoutoutClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("真的要退出吗？");
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: - 退出登录接口操作


                Account.shared().logout();
                Toast.makeText(UserManagerActivity.this, "退出登录成功", Toast.LENGTH_SHORT).show();
                UserManagerActivity.this.finish();
            }
        });
        builder.setNegativeButton("取消",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
