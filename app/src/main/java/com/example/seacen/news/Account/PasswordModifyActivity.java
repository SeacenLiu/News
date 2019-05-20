package com.example.seacen.news.Account;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.seacen.news.R;
import com.example.seacen.news.Utils.Network.SCNetworkHandler;
import com.example.seacen.news.Utils.Network.SCNetworkMethod;
import com.example.seacen.news.Utils.Network.SCNetworkPort;
import com.example.seacen.news.Utils.Network.SCNetworkTool;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordModifyActivity extends AppCompatActivity {

    @BindView(R.id.password_activity_origin_Et)
    EditText originEt;
    @BindView(R.id.password_activity_password_Et)
    EditText passwordEt;
    @BindView(R.id.password_activity_passwordAg_Et)
    EditText passwordAgEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_modify_activity);
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
        Map<String, Object> param = new HashMap<>();
        param.put("pripasswd", origin);
        param.put("newpasswd", passwd);
        SCNetworkTool.shared().normalRequest(SCNetworkPort.UpdatePassword, SCNetworkMethod.PUT, param, new SCNetworkHandler() {
            @Override
            public void successHandle(JSONObject jsonObject) {
                int code = jsonObject.getInteger("status");
                String msg = jsonObject.getString("msg");
                if (code == 200) {
                    Toast.makeText(PasswordModifyActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                    PasswordModifyActivity.this.finish();
                } else {
                    Toast.makeText(PasswordModifyActivity.this,  msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorHandle(Exception error) {
                Toast.makeText(PasswordModifyActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
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
