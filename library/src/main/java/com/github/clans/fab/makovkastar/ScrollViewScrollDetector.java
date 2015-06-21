package com.github.clans.fab.makovkastar;

/*
    based on project https://github.com/makovkastar/FloatingActionButton/

    file -> https://github.com/makovkastar/FloatingActionButton/blob/master/library/src/main/java/com/melnykov/fab/ScrollViewScrollDetector.java
*/

import android.widget.ScrollView;

abstract class ScrollViewScrollDetector implements ObservableScrollView.OnScrollChangedListener {
    private int mLastScrollY;
    private int mScrollThreshold;

    abstract void onScrollUp();

    abstract void onScrollDown();

    @Override
    public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
        boolean isSignificantDelta = Math.abs(t - mLastScrollY) > mScrollThreshold;
        if (isSignificantDelta) {
            if (t > mLastScrollY) {
                onScrollUp();
            } else {
                onScrollDown();
            }
        }
        mLastScrollY = t;
    }

    public void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }
}