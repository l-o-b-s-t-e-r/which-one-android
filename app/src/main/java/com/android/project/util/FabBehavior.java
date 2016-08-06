package com.android.project.util;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.project.R;

/**
 * Created by macos on 05.08.16.
 */
public class FabBehavior extends FloatingActionButton.Behavior implements Animation.AnimationListener {

    private FloatingActionButton mFAB;
    private Animation mAnimationHide;
    private Animation mAnimationShow;

    public FabBehavior(Context context, AttributeSet attrs) {
        super();

        mAnimationHide = AnimationUtils.loadAnimation(context, R.anim.fab_hide_scrolling);
        mAnimationShow = AnimationUtils.loadAnimation(context, R.anim.fab_show_scrolling);

        mAnimationHide.setAnimationListener(this);
        mAnimationShow.setAnimationListener(this);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        mFAB = child;

        if (child.getVisibility() == View.VISIBLE && dyConsumed > 0) {
            child.startAnimation(mAnimationHide);
        } else if (child.getVisibility() == View.GONE && dyConsumed < 0) {
            child.startAnimation(mAnimationShow);
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (mFAB.getVisibility() == View.VISIBLE) {
            mFAB.setVisibility(View.GONE);
        } else {
            mFAB.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
