package com.github.clans.fab;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;


public class FloatingActionMenuBehavior extends CoordinatorLayout.Behavior<FloatingActionMenu> {

    private int mToolbarHeight = -1;

    public FloatingActionMenuBehavior() {
        super();
    }

    public FloatingActionMenuBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionMenu child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout
                || dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionMenu fam, View dependency) {
        super.onDependentViewChanged(parent, fam, dependency);

        if (mToolbarHeight == -1) {
            mToolbarHeight = Util.getToolbarHeight(fam.getContext());
        }

        float translationY;
        if (dependency instanceof Snackbar.SnackbarLayout) {
            translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        } else {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fam
                    .getLayoutParams();
            int famBottomMargin = lp.bottomMargin;
            int height;
            if (!fam.isOpened()) {
                height = fam.getChildAt(0).getHeight();
            } else {
                height = fam.getHeight();
            }
            int distanceToScroll = height + famBottomMargin;
            float ratio = (float) dependency.getY() / (float) mToolbarHeight;
            translationY = - distanceToScroll * ratio;
        }
        fam.setTranslationY(translationY);

        return true;
    }

}