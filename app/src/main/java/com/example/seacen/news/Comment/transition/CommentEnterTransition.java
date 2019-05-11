package com.example.seacen.news.Comment.transition;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;

public class CommentEnterTransition extends Visibility {
    private static final String TAG = "CommentEnterTransition";

    private static final String PROPNAME_BOTTOM_BOX_TRANSITION_Y = "custom_bottom_box_enter_transition:change_transY:transitionY";

    private View mBottomView;
    private Context mContext;

    public CommentEnterTransition(Context context, View bottomView) {
        mBottomView = bottomView;
        mContext = context;
    }


    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        mBottomView.measure(0, 0);
        int transY = mBottomView.getMeasuredHeight();

        // 保存 计算初始值
        transitionValues.values.put(PROPNAME_BOTTOM_BOX_TRANSITION_Y, transY);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);

        // 保存计算结束值
        transitionValues.values.put(PROPNAME_BOTTOM_BOX_TRANSITION_Y, 0);
    }


    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        return super.createAnimator(sceneRoot, startValues, endValues);

    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, final View view,
                             TransitionValues startValues, TransitionValues endValues) {

        if (null == startValues || null == endValues) {
            return null;
        }

        // 这里去除 之前 存储的 初始值 和 结束值, 然后执行东湖
        if (view == mBottomView) {
            int startTransY = (int) startValues.values.get(PROPNAME_BOTTOM_BOX_TRANSITION_Y);
            int endTransY = (int) endValues.values.get(PROPNAME_BOTTOM_BOX_TRANSITION_Y);

            if (startTransY != endTransY) {
                ValueAnimator animator = ValueAnimator.ofInt(startTransY, endTransY);
                // 注意这里不能使用 属性动画, 使用 ValueAnimator 然后在更新 View 的对应属性
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Object value = animation.getAnimatedValue();
                        if (null != value) {
                            view.setTranslationY((Integer) value);
                        }
                    }
                });
                return animator;
            }
        }
        return null;
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, final View view,
                                TransitionValues startValues, TransitionValues endValues) {
        if (null == startValues || null == endValues) {
            return null;
        }

        // 这里执行 返回动画,  这里金 初始值 和技术值 对调了,这样动画, 就就和原来动画想反了
        if (view == mBottomView) {
            int startTransY = (int) endValues.values.get(PROPNAME_BOTTOM_BOX_TRANSITION_Y);
            int endTransY = (int) startValues.values.get(PROPNAME_BOTTOM_BOX_TRANSITION_Y);

            if (startTransY != endTransY) {
                ValueAnimator animator = ValueAnimator.ofInt(startTransY, endTransY);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Object value = animation.getAnimatedValue();
                        if (null != value) {
                            view.setTranslationY((Integer) value);
                        }
                    }
                });
                return animator;
            }
        }
        return null;
    }
}
