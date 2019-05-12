package com.example.seacen.news.Comment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.seacen.news.R;

public class CommentCell extends LinearLayout {
    /**
     * 在java代码里new的时候会用到
     * @param context
     */
    public CommentCell(Context context) {
        super(context);
        setupUI(context);
    }

    /**
     * 在xml布局文件中使用时自动调用
     * @param context
     */
    public CommentCell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupUI(context);
    }

    /**
     * 不会自动调用，如果有默认style时，在第二个构造函数中调用
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CommentCell(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupUI(context);
    }

    private void setupUI(Context context) {
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.cell_comment, this);


    }

}
