package com.github.clans.fab;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.design.widget.*;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class FloatingActionButtonNew extends android.support.design.widget.FloatingActionButton {

    static final int ANIM_STATE_NONE = 0;
    static final int ANIM_STATE_HIDING = 1;
    static final int ANIM_STATE_SHOWING = 2;

    private String mLabelText;
    private Animation mShowAnimation;
    private Animation mHideAnimation;

    int mAnimState = ANIM_STATE_NONE;

    public FloatingActionButtonNew(Context context) {
        this(context, null);
    }

    public FloatingActionButtonNew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingActionButtonNew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionButton, defStyleAttr, 0);
//        setUseCompatPadding(true);
        initShowAnimation(attr);
        initHideAnimation(attr);

        setSize(SIZE_MINI);

        attr.recycle();
    }

    private void initShowAnimation(TypedArray attr) {
        int resourceId = attr.getResourceId(R.styleable.FloatingActionButton_fab_showAnimation, R.anim.fab_scale_up);
        mShowAnimation = AnimationUtils.loadAnimation(getContext(), resourceId);
    }

    private void initHideAnimation(TypedArray attr) {
        int resourceId = attr.getResourceId(R.styleable.FloatingActionButton_fab_hideAnimation, R.anim.fab_scale_down);
        mHideAnimation = AnimationUtils.loadAnimation(getContext(), resourceId);
    }

    void playShowAnimation(@Nullable final OnVisibilityChangedListener listener) {
        mHideAnimation.cancel();

        mShowAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimState = ANIM_STATE_NONE;
                if (listener != null) {
                    listener.onShown(FloatingActionButtonNew.this);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(mShowAnimation);
    }

    void playHideAnimation(@Nullable final OnVisibilityChangedListener listener) {
        mShowAnimation.cancel();
        mHideAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimState = ANIM_STATE_NONE;
                setVisibility(View.GONE);
                if (listener != null) {
                    listener.onHidden(FloatingActionButtonNew.this);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(mHideAnimation);
    }

    boolean isOrWillBeHidden() {
        if (getVisibility() == View.VISIBLE) {
            // If we currently visible, return true if we're animating to be hidden
            return mAnimState == ANIM_STATE_HIDING;
        } else {
            // Otherwise if we're not visible, return true if we're not animating to be shown
            return mAnimState != ANIM_STATE_SHOWING;
        }
    }

    boolean isOrWillBeShown() {
        if (getVisibility() != View.VISIBLE) {
            // If we not currently visible, return true if we're animating to be shown
            return mAnimState == ANIM_STATE_SHOWING;
        } else {
            // Otherwise if we're visible, return true if we're not animating to be hidden
            return mAnimState != ANIM_STATE_HIDING;
        }
    }

    public void setLabelText(String text) {
        mLabelText = text;
        TextView labelView = getLabelView();
        if (labelView != null) {
            labelView.setText(text);
        }
    }

    public String getLabelText() {
        return mLabelText;
    }

    Label getLabelView() {
        return (Label) getTag(R.id.fab_label);
    }

    /**
     * Checks whether <b>FloatingActionButton</b> is hidden
     *
     * @return true if <b>FloatingActionButton</b> is hidden, false otherwise
     */
    public boolean isHidden() {
        return getVisibility() == GONE;
    }

    /**
     * Makes the <b>FloatingActionButton</b> to appear and sets its visibility to {@link #VISIBLE}
     *
     * @param animate if true - plays "show animation"
     */
    public void show(boolean animate, @Nullable OnVisibilityChangedListener listener) {
        if (!isOrWillBeShown()) {
            if (animate) {
                mAnimState = ANIM_STATE_SHOWING;
                playShowAnimation(listener);
            } else {
                setVisibility(VISIBLE);
            }
        }
    }

    /**
     * Makes the <b>FloatingActionButton</b> to disappear and sets its visibility to {@link #INVISIBLE}
     *
     * @param animate if true - plays "hide animation"
     */
    public void hide(boolean animate, @Nullable OnVisibilityChangedListener listener) {
        if (!isOrWillBeHidden()) {
            if (animate) {
                mAnimState = ANIM_STATE_HIDING;
                playHideAnimation(listener);
            } else {
                setVisibility(GONE);
            }
        }
    }
}
