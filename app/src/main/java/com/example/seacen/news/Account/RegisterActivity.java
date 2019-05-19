package com.example.seacen.news.Account;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


public class RegisterActivity extends AppCompatActivity {

    static String TAG = "RegisterActivity";

    @BindView(R.id.register_activity_username_Et)
    EditText userEt;
    @BindView(R.id.register_activity_password_Et)
    EditText passwordEt;
    @BindView(R.id.register_activity_passwordAg_Et)
    EditText passwordAgEt;
    @BindView(R.id.register_activity_register_btn)
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ButterKnife.bind(this);
        setupNavigationBar();
    }

    @OnClick(R.id.register_activity_register_btn)
    void registerClick() {
        // 判断是否合法
        if (!passwordEt.getText().toString().equals(passwordAgEt.getText().toString())) {
            Toast toast = Toast.makeText(RegisterActivity.this, "两次密码不同", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // TODO: - 注册操作
        Map<String, Object> params = new HashMap<>();
        params.put("name", userEt.getText().toString());
        params.put("passwd", passwordEt.getText().toString());
        SCNetworkTool.shared().normalRequest(SCNetworkPort.Register, SCNetworkMethod.POST, params, new SCNetworkHandler() {
            @Override
            public void successHandle(com.alibaba.fastjson.JSONObject jsonObject) {
                Toast toast = Toast.makeText(RegisterActivity.this, jsonObject.toJSONString(), Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void errorHandle(Exception error) {
                Toast toast = Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    void setupNavigationBar() {
        ActionBar navigationBar = getSupportActionBar();
        navigationBar.setTitle("注册");
        navigationBar.setDisplayHomeAsUpEnabled(true);
    }

    // 有这个方法才可以跳回前一个界面
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
