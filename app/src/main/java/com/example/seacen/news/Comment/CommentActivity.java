package com.example.seacen.news.Comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.seacen.news.Comment.transition.ChangeColor;
import com.example.seacen.news.Comment.transition.ChangePosition;
import com.example.seacen.news.Comment.transition.CommentEnterTransition;
import com.example.seacen.news.Comment.transition.ShareElemEnterRevealTransition;
import com.example.seacen.news.Comment.transition.ShareElemReturnChangePosition;
import com.example.seacen.news.Comment.transition.ShareElemReturnRevealTransition;
import com.example.seacen.news.News.NewsActivity;
import com.example.seacen.news.News.NewsCell;
import com.example.seacen.news.News.NewsDetailActivity;
import com.example.seacen.news.R;

public class CommentActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "CommentActivity";

    View bottomSendBar;
    View commentContent;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);
        commentContent = findViewById(R.id.comment_activity_content);
        bottomSendBar = findViewById(R.id.comment_activity_bottom_send_bar);
        listView = findViewById(R.id.comment_activity_body_lv);
        listView.setAdapter(new CommentAdapter());
        listView.setOnItemClickListener(this);

        setTransition();
        setupNavigation();
    }

    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        Log.i("indexPath.row", String.valueOf(position));
    }

    private class CommentAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new CommentCell(CommentActivity.this);
            }
            return convertView;
        }
    }

    private void setupNavigation() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("评论区");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setTransition() {
        // 底部输入框的进入动画
        getWindow().setEnterTransition(new CommentEnterTransition(this, bottomSendBar));
        // 入场动画
        getWindow().setSharedElementEnterTransition(buildShareElemEnterSet());
        // 退场动画
        getWindow().setSharedElementReturnTransition(buildShareElemReturnSet());
    }

    /**
     * 进入动画
     * @return
     */
    private TransitionSet buildShareElemEnterSet() {
        TransitionSet transitionSet = new TransitionSet();

        Transition changePos = new ChangePosition();
        changePos.setDuration(300);
        changePos.addTarget(R.id.comment_activity_content);
        transitionSet.addTransition(changePos);

        Transition revealTransition = new ShareElemEnterRevealTransition(commentContent);
        transitionSet.addTransition(revealTransition);
        revealTransition.addTarget(R.id.comment_activity_content);
        revealTransition.setInterpolator(new FastOutSlowInInterpolator());
        revealTransition.setDuration(300);

        ChangeColor changeColor = new ChangeColor(getResources().getColor(R.color.black_85_alpha), getResources().getColor(R.color.white));
        changeColor.addTarget(R.id.comment_activity_content);
        changeColor.setDuration(350);
        transitionSet.addTransition(changeColor);
        transitionSet.setDuration(900);

        return transitionSet;
    }

    /**
     * 返回动画
     * @return
     */
    private TransitionSet buildShareElemReturnSet() {
        TransitionSet transitionSet = new TransitionSet();

        Transition changePos = new ShareElemReturnChangePosition();
        changePos.addTarget(R.id.comment_activity_content);
        transitionSet.addTransition(changePos);

        ChangeColor changeColor = new ChangeColor(getResources().getColor(R.color.white), getResources().getColor(R.color.black_85_alpha));
        changeColor.addTarget(R.id.comment_activity_content);
        transitionSet.addTransition(changeColor);

        Transition revealTransition = new ShareElemReturnRevealTransition(commentContent);
        revealTransition.addTarget(R.id.comment_activity_content);
        transitionSet.addTransition(revealTransition);
        transitionSet.setDuration(900);

        return transitionSet;
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
