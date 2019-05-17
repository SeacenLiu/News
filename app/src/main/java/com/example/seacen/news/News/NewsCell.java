package com.example.seacen.news.News;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seacen.news.R;

public class NewsCell extends LinearLayout {

    ImageView coverImg;
    TextView titleTv, detailTv;
    TextView sourceTv, timeTv, readTv, commentTv;

    /**
     * 在java代码里new的时候会用到
     * @param context
     */
    public NewsCell(Context context) {
        super(context);
        setupUI(context);
    }

    /**
     * 在xml布局文件中使用时自动调用
     * @param context
     */
    public NewsCell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupUI(context);
    }

    /**
     * 不会自动调用，如果有默认style时，在第二个构造函数中调用
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public NewsCell(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupUI(context);
    }

    private void setupUI(Context context) {
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.cell_news, this);

        coverImg = findViewById(R.id.cell_news_cover_iv);
        titleTv = findViewById(R.id.cell_news_title_tv);
        detailTv = findViewById(R.id.cell_news_detail_tv);
        sourceTv = findViewById(R.id.cell_news_source_tv);
        timeTv = findViewById(R.id.cell_news_time_tv);
        readTv = findViewById(R.id.cell_news_read_tv);
        commentTv = findViewById(R.id.cell_news_comment_tv);
    }

}
