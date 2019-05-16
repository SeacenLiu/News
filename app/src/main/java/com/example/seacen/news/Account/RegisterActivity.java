package com.example.seacen.news.Account;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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


public class RegisterActivity extends AppCompatActivity {

    static String TAG = "RegisterActivity";

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
                Map<String, String> params = new HashMap<>();
                params.put("name", userEt.getText().toString());
                params.put("passwd", passwordEt.getText().toString());
                SCNetworkTool.shared().normalEequest(SCNetworkPort.Register, SCNetworkMethod.POST, params, new SCNetworkHandler() {
                    @Override
                    public void successHandle(String bodyStr) {
                        Toast toast = Toast.makeText(RegisterActivity.this, bodyStr, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    @Override
                    public void errorHandle(Exception error) {
                        Toast toast = Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
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
