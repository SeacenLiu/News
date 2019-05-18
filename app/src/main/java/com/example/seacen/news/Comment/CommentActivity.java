package com.example.seacen.news.Comment;

import android.content.Intent;
import android.graphics.Rect;
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
import com.example.seacen.news.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "CommentActivity";

    @BindView(R.id.comment_activity_bottom_send_bar)
    View bottomSendBar;
    @BindView(R.id.comment_activity_content)
    View commentContent;
    @BindView(R.id.comment_activity_body_lv)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);
        ButterKnife.bind(this);
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
//        actionBar.hide();
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

        Transition revealTransition = new ShareElemEnterRevealTransition(commentContent);
        transitionSet.addTransition(revealTransition);
        revealTransition.addTarget(R.id.comment_activity_content);
        revealTransition.setInterpolator(new FastOutSlowInInterpolator());
        revealTransition.setDuration(300);

        Transition changePos = new ChangePosition();
        changePos.setDuration(300);
        changePos.addTarget(R.id.comment_activity_content);
        transitionSet.addTransition(changePos);

        ChangeColor changeColor = new ChangeColor(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.white));
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

        Transition revealTransition = new ShareElemReturnRevealTransition(commentContent);
        revealTransition.addTarget(R.id.comment_activity_content);
        revealTransition.setDuration(300);
        transitionSet.addTransition(revealTransition);

        // x+y-
        Transition changePos = new ShareElemReturnChangePosition(12, 219);
//        Transition changePos = new ShareElemReturnChangePosition(R.dimen.animate_offset_x, R.dimen.animate_offset_y);
        changePos.setDuration(300);
        changePos.addTarget(R.id.comment_activity_content);
        transitionSet.addTransition(changePos);

        ChangeColor changeColor = new ChangeColor(getResources().getColor(R.color.white), getResources().getColor(R.color.colorPrimary));
        changeColor.addTarget(R.id.comment_activity_content);
        changeColor.setDuration(350);
        transitionSet.addTransition(changeColor);

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
