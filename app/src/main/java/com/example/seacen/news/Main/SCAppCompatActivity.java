package com.example.seacen.news.Main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SCAppCompatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        setupActivity();
    }

    /**
     * 获取 LayoutResID 用于初始化界面
     * @return LayoutResID
     */
    protected int getLayoutResID() {
        return 0;
    }

    /**
     * 统一设置属性方法
     */
    protected void setupActivity() {

    }
}
