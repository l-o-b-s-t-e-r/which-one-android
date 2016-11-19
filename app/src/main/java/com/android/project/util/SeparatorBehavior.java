package com.android.project.util;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.android.project.R;

/**
 * Created by Lobster on 19.11.16.
 */
public class SeparatorBehavior extends CoordinatorLayout.Behavior<View> {

    private AppBarLayout appBar;
    private Toolbar toolbar;
    private int integerPart, marginPX;
    private float appBarOffset, fractionPart, newWidth, targetWidth, deltaAppBar, deltaAppBarInPercent;

    public SeparatorBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        marginPX = (int) context.getResources().getDimension(R.dimen.separator_margin_LR);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            if (appBar == null) {
                appBar = (AppBarLayout) dependency.findViewById(R.id.appbar);
            }

            if (toolbar == null) {
                toolbar = (Toolbar) dependency.findViewById(R.id.toolbar);
            }
        }

        if (appBar != null && appBar.getWidth() != 0 && targetWidth == 0.0f) {
            targetWidth = appBar.getWidth() - 2 * marginPX;
        }

        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.setY(appBar.getHeight() + dependency.getY());

        deltaAppBar = appBarOffset - dependency.getY();
        deltaAppBarInPercent = deltaAppBar / (appBar.getHeight() - toolbar.getHeight());

        newWidth = child.getLayoutParams().width + targetWidth * deltaAppBarInPercent + fractionPart;
        integerPart = (int) newWidth;
        fractionPart = newWidth - integerPart;
        child.getLayoutParams().width = integerPart;

        child.requestLayout();
        appBarOffset = dependency.getY();

        return true;
    }
}
