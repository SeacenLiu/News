package com.example.seacen.news.Comment;

import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.View;

import com.example.seacen.news.Comment.transition.ChangeColor;
import com.example.seacen.news.Comment.transition.ChangePosition;
import com.example.seacen.news.Comment.transition.CommentEnterTransition;
import com.example.seacen.news.Comment.transition.ShareElemEnterRevealTransition;
import com.example.seacen.news.Comment.transition.ShareElemReturnChangePosition;
import com.example.seacen.news.Comment.transition.ShareElemReturnRevealTransition;
import com.example.seacen.news.R;

public class CommentActivity extends AppCompatActivity {
    private static final String TAG = "CommentActivity";

    View bottomSendBar;
    View commentContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);
        commentContent = findViewById(R.id.comment_activity_content);
        bottomSendBar = findViewById(R.id.comment_activity_bottom_send_bar);

        setTransition();
        setupNavigation();
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
}
