package com.android.project.util;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.android.project.R;


public class ImageBehavior extends CoordinatorLayout.Behavior<ImageView> {

    private Toolbar toolbar;
    private float appBarHeight, toolBarHeight;
    private float deltaAppBar, deltaAppBarPercent;
    private float childStartY, childStartX;

    public ImageBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        if (toolbar==null && dependency instanceof AppBarLayout){
            toolbar = (Toolbar) dependency.findViewById(R.id.toolbar);
        }
        if (toolbar!=null && toolBarHeight == 0.0f){
                toolBarHeight = toolbar.getHeight();
                childStartY = child.getY();
                childStartX = child.getX();
        }

        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {

        deltaAppBar = appBarHeight-dependency.getY();
        deltaAppBarPercent = deltaAppBar/(dependency.getHeight()-toolBarHeight-child.getHeight());

        child.setY(child.getY() - (childStartY-toolBarHeight)*deltaAppBarPercent);
        child.setX(child.getX() + (dependency.getWidth()-childStartX)*deltaAppBarPercent);

        appBarHeight = dependency.getY();

        return true;
    }
}
