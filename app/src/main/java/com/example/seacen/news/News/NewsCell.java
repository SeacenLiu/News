package com.example.seacen.news.News;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seacen.news.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsCell extends LinearLayout {

    @BindView(R.id.cell_news_cover_iv)
    public ImageView coverImg;
    @BindView(R.id.cell_news_title_tv)
    public TextView titleTv;
    @BindView(R.id.cell_news_detail_tv)
    public TextView detailTv;
    @BindView(R.id.cell_news_source_tv)
    public TextView sourceTv;
    @BindView(R.id.cell_news_time_tv)
    public TextView timeTv;
    @BindView(R.id.cell_news_read_tv)
    public TextView readTv;
    @BindView(R.id.cell_news_comment_tv)
    public TextView commentTv;

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
        View view = LayoutInflater.from(context).inflate(R.layout.cell_news, this);
        ButterKnife.bind(this, view);
    }

}
