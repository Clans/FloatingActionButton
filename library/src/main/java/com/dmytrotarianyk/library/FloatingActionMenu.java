package com.dmytrotarianyk.library;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

public class FloatingActionMenu extends ViewGroup {

    private static final int ANIMATION_DURATION = 300;
    private static final float CLOSED_PLUS_ROTATION = 0f;
    private static final float OPENED_PLUS_ROTATION = -90f - 45f;

    private AnimatorSet mOpenAnimatorSet = new AnimatorSet().setDuration(ANIMATION_DURATION);
    private AnimatorSet mCloseAnimatorSet = new AnimatorSet().setDuration(ANIMATION_DURATION);

    private int mButtonSpacing = Util.dpToPx(getContext(), 0f);
    private FloatingActionButton mMenuButton;
    private int mMaxButtonWidth;
    private int mLabelsMargin = Util.dpToPx(getContext(), 0f);
    private int mLabelsVerticalOffset = Util.dpToPx(getContext(), 0f);
    private int mButtonsCount;
    private boolean mMenuOpened;
    private Handler mUiHandler = new Handler();
    private int mLabelsShowAnimation;
    private int mLabelsHideAnimation;
    private int mLabelsPaddingTop = Util.dpToPx(getContext(), 4f);
    private int mLabelsPaddingRight = Util.dpToPx(getContext(), 8f);
    private int mLabelsPaddingBottom = Util.dpToPx(getContext(), 4f);
    private int mLabelsPaddingLeft = Util.dpToPx(getContext(), 8f);
    private int mLabelsTextColor;
    private float mLabelsTextSize;
    private int mLabelsCornerRadius;
    private boolean mLabelsShowShadow;
    private int mLabelsColorNormal;
    private int mLabelsColorPressed;
    private int mLabelsColorRipple;
    private int mMenuShadowColor;
    private int mMenuColorNormal;
    private int mMenuColorPressed;
    private int mMenuColorRipple;
    private Drawable mIcon;
    private int mAnimationDelayPerItem;
    private Interpolator mOpenInterpolator;
    private Interpolator mCloseInterpolator;
    private boolean mIsAnimated = true;
    private boolean mLabelsSingleLine;
    private int mLabelsEllipsize;
    private int mLabelsMaxLines;
    private int mMenuFabSize;
    private int mLabelsStyle;

    private OnMenuToggleListener mToggleListener;

    public interface OnMenuToggleListener {
        void onMenuToggle(boolean opened);
    }

    public FloatingActionMenu(Context context) {
        this(context, null);
    }

    public FloatingActionMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingActionMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionMenu, 0, 0);
        mButtonSpacing = attr.getDimensionPixelSize(R.styleable.FloatingActionMenu_menu_buttonSpacing, mButtonSpacing);
        mLabelsMargin = attr.getDimensionPixelSize(R.styleable.FloatingActionMenu_menu_labels_margin, mLabelsMargin);
        mLabelsShowAnimation = attr.getResourceId(R.styleable.FloatingActionMenu_menu_labels_showAnimation, R.anim.fab_slide_in_from_right);
        mLabelsHideAnimation = attr.getResourceId(R.styleable.FloatingActionMenu_menu_labels_hideAnimation, R.anim.fab_slide_out_to_right);
        mLabelsPaddingTop = attr.getDimensionPixelSize(R.styleable.FloatingActionMenu_menu_labels_paddingTop, mLabelsPaddingTop);
        mLabelsPaddingRight = attr.getDimensionPixelSize(R.styleable.FloatingActionMenu_menu_labels_paddingRight, mLabelsPaddingRight);
        mLabelsPaddingBottom = attr.getDimensionPixelSize(R.styleable.FloatingActionMenu_menu_labels_paddingBottom, mLabelsPaddingBottom);
        mLabelsPaddingLeft = attr.getDimensionPixelSize(R.styleable.FloatingActionMenu_menu_labels_paddingLeft, mLabelsPaddingLeft);
        mLabelsTextColor = attr.getColor(R.styleable.FloatingActionMenu_menu_labels_textColor, Color.WHITE);
        mLabelsTextSize = attr.getDimension(R.styleable.FloatingActionMenu_menu_labels_textSize, getResources().getDimension(R.dimen.labels_text_size));
        mLabelsCornerRadius = attr.getDimensionPixelSize(R.styleable.FloatingActionMenu_menu_labels_cornerRadius, 6);
        mLabelsShowShadow = attr.getBoolean(R.styleable.FloatingActionMenu_menu_labels_showShadow, true);
        mLabelsColorNormal = attr.getColor(R.styleable.FloatingActionMenu_menu_labels_colorNormal, 0xFF333333);
        mLabelsColorPressed = attr.getColor(R.styleable.FloatingActionMenu_menu_labels_colorPressed, 0xFF444444);
        mLabelsColorRipple = attr.getColor(R.styleable.FloatingActionMenu_menu_labels_colorRipple, 0x66FFFFFF);
        mMenuShadowColor = attr.getColor(R.styleable.FloatingActionMenu_menu_shadowColor, 0x66000000);
        mMenuColorNormal = attr.getColor(R.styleable.FloatingActionMenu_menu_colorNormal, 0xFFDA4336);
        mMenuColorPressed = attr.getColor(R.styleable.FloatingActionMenu_menu_colorPressed, 0xFFBF3B2F);
        mMenuColorRipple = attr.getColor(R.styleable.FloatingActionMenu_menu_colorRipple, 0x33000000);
        mAnimationDelayPerItem = attr.getInt(R.styleable.FloatingActionMenu_menu_animationDelayPerItem, 50);
        mIcon = attr.getDrawable(R.styleable.FloatingActionMenu_menu_icon);
        if (mIcon == null) {
            mIcon = getResources().getDrawable(R.drawable.fab_add);
        }
        mLabelsSingleLine = attr.getBoolean(R.styleable.FloatingActionMenu_menu_labels_singleLine, false);
        mLabelsEllipsize = attr.getInt(R.styleable.FloatingActionMenu_menu_labels_ellipsize, 0);
        mLabelsMaxLines = attr.getInt(R.styleable.FloatingActionMenu_menu_labels_maxLines, -1);
        mMenuFabSize = attr.getInt(R.styleable.FloatingActionMenu_menu_fab_size, FloatingActionButton.SIZE_NORMAL);
        mLabelsStyle = attr.getResourceId(R.styleable.FloatingActionMenu_menu_labels_style, 0);

        if (attr.hasValue(R.styleable.FloatingActionMenu_menu_labels_padding)) {
            int padding = attr.getDimensionPixelSize(R.styleable.FloatingActionMenu_menu_labels_padding, 0);
            initPadding(padding);
        }
        attr.recycle();

        mOpenInterpolator = new OvershootInterpolator();
        mCloseInterpolator = new AnticipateInterpolator();

        createMenuButton();
    }

    private void initPadding(int padding) {
        mLabelsPaddingTop = padding;
        mLabelsPaddingRight = padding;
        mLabelsPaddingBottom = padding;
        mLabelsPaddingLeft = padding;
    }

    private void createMenuButton() {
        mMenuButton = new FloatingActionButton(getContext()) {

            @Override
            protected Drawable getIconDrawable() {
                RotatingDrawable rotatingDrawable = new RotatingDrawable(mIcon);

                ObjectAnimator collapseAnimator = ObjectAnimator.ofFloat(rotatingDrawable, "rotation", OPENED_PLUS_ROTATION, CLOSED_PLUS_ROTATION);
                ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(rotatingDrawable, "rotation", CLOSED_PLUS_ROTATION, OPENED_PLUS_ROTATION);

                mOpenAnimatorSet.play(expandAnimator);
                mCloseAnimatorSet.play(collapseAnimator);

                mOpenAnimatorSet.setInterpolator(mOpenInterpolator);
                mCloseAnimatorSet.setInterpolator(mCloseInterpolator);

                return rotatingDrawable;
            }
        };

        mMenuButton.setColors(mMenuColorNormal, mMenuColorPressed, mMenuColorRipple);
        mMenuButton.setShadowColor(mMenuShadowColor);
        mMenuButton.setButtonSize(mMenuFabSize);
        mMenuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(mIsAnimated);
            }
        });

        addView(mMenuButton, super.generateDefaultLayoutParams());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;
        mMaxButtonWidth = 0;
        int maxLabelWidth = 0;

        for (int i = 0; i < mButtonsCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE) continue;

            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            mMaxButtonWidth = Math.max(mMaxButtonWidth, child.getMeasuredWidth());
        }

        for (int i = 0; i < mButtonsCount; i++) {
            int usedWidth = 0;
            View child = getChildAt(i);

            if (child.getVisibility() == GONE) continue;

            usedWidth += child.getMeasuredWidth();
            height += child.getMeasuredHeight();

            Label label = (Label) child.getTag(R.id.fab_label);
            if (label != null) {
                int labelOffset = (mMaxButtonWidth - child.getMeasuredWidth()) / 2;
                int labelUsedWidth = child.getMeasuredWidth() + label.calculateShadowWidth() + mLabelsMargin + labelOffset;
                measureChildWithMargins(label, widthMeasureSpec, labelUsedWidth, heightMeasureSpec, 0);
                usedWidth += label.getMeasuredWidth();
                maxLabelWidth = Math.max(maxLabelWidth, usedWidth + labelOffset);
            }
        }

        width = Math.max(mMaxButtonWidth, maxLabelWidth + mLabelsMargin);

        height += mButtonSpacing * (getChildCount() - 1);
        height = adjustForOvershoot(height);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int buttonsHorizontalCenter = r - l - mMaxButtonWidth / 2;
        int menuButtonTop = b - t - mMenuButton.getMeasuredHeight() - getPaddingBottom();
        int menuButtonLeft = buttonsHorizontalCenter - mMenuButton.getMeasuredWidth() / 2;

        mMenuButton.layout(menuButtonLeft, menuButtonTop, menuButtonLeft + mMenuButton.getMeasuredWidth(),
                menuButtonTop + mMenuButton.getMeasuredHeight());

        int nextY = menuButtonTop - mButtonSpacing;

        for (int i = mButtonsCount - 1; i >= 0; i--) {
            FloatingActionButton child = (FloatingActionButton) getChildAt(i);

            if (child == mMenuButton || child.getVisibility() == GONE) continue;

            int childX = buttonsHorizontalCenter - child.getMeasuredWidth() / 2;
            int childY = nextY - child.getMeasuredHeight();
            child.layout(childX, childY, childX + child.getMeasuredWidth(),
                    childY + child.getMeasuredHeight());


            if (!mMenuOpened) {
                child.hide(false);
            }

            View label = (View) child.getTag(R.id.fab_label);
            if (label != null) {
                int labelsOffset = child.getMeasuredWidth() / 2 + mLabelsMargin;
                int labelXNearButton = buttonsHorizontalCenter - labelsOffset;

                int labelXAwayFromButton = labelXNearButton - label.getMeasuredWidth();
                int labelTop = childY - mLabelsVerticalOffset + (child.getMeasuredHeight()
                        - label.getMeasuredHeight()) / 2;

                label.layout(labelXAwayFromButton, labelTop,
                        labelXNearButton, labelTop + label.getMeasuredHeight());

                label.setVisibility(INVISIBLE);
            }

            nextY = childY - mButtonSpacing;
        }
    }

    private int adjustForOvershoot(int dimension) {
        return dimension * 12 / 10;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bringChildToFront(mMenuButton);
        mButtonsCount = getChildCount();
        createLabels();
    }

    private void createLabels() {
        Context context = new ContextThemeWrapper(getContext(), mLabelsStyle);

        for (int i = 0; i < mButtonsCount; i++) {
            final FloatingActionButton fab = (FloatingActionButton) getChildAt(i);
            String text = fab.getLabel();

            if (fab == mMenuButton || TextUtils.isEmpty(text) || fab.getTag(R.id.fab_label) != null) {
                continue;
            }

            final Label label = new Label(context);
            label.setFab(fab);
            label.setShowAnimation(AnimationUtils.loadAnimation(getContext(), mLabelsShowAnimation));
            label.setHideAnimation(AnimationUtils.loadAnimation(getContext(), mLabelsHideAnimation));

            if (mLabelsStyle > 0) {
                label.setTextAppearance(getContext(), mLabelsStyle);
                label.setShowShadow(false);
                label.setUsingStyle(true);
            } else {
                label.setColors(mLabelsColorNormal, mLabelsColorPressed, mLabelsColorRipple);
                label.setShowShadow(mLabelsShowShadow);
                label.setCornerRadius(mLabelsCornerRadius);
                if (mLabelsEllipsize > 0) {
                    setLabelEllipsize(label);
                }
                label.setMaxLines(mLabelsMaxLines);
                label.updateBackground();

                label.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLabelsTextSize);
                label.setTextColor(mLabelsTextColor);

                int left = mLabelsPaddingLeft;
                int top = mLabelsPaddingTop;
                if (mLabelsShowShadow) {
                    left += fab.getShadowRadius() + Math.abs(fab.getShadowXOffset());
                    top += fab.getShadowRadius() + Math.abs(fab.getShadowYOffset());
                }

                label.setPadding(
                        left,
                        top,
                        mLabelsPaddingLeft,
                        mLabelsPaddingTop
                );

                if (mLabelsMaxLines < 0 || mLabelsSingleLine) {
                    label.setSingleLine(mLabelsSingleLine);
                }
            }

            label.setText(text);

            addView(label);
            fab.setTag(R.id.fab_label, label);
        }
    }

    private void setLabelEllipsize(Label label) {
        switch (mLabelsEllipsize) {
            case 1:
                label.setEllipsize(TextUtils.TruncateAt.START);
                break;
            case 2:
                label.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                break;
            case 3:
                label.setEllipsize(TextUtils.TruncateAt.END);
                break;
            case 4:
                label.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                break;
        }
    }

    private static class RotatingDrawable extends LayerDrawable {

        public RotatingDrawable(Drawable drawable) {
            super(new Drawable[]{drawable});
        }

        private float mRotation;

        @SuppressWarnings("UnusedDeclaration")
        public float getRotation() {
            return mRotation;
        }

        @SuppressWarnings("UnusedDeclaration")
        public void setRotation(float rotation) {
            mRotation = rotation;
            invalidateSelf();
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.save();
            canvas.rotate(mRotation, getBounds().centerX(), getBounds().centerY());
            super.draw(canvas);
            canvas.restore();
        }
    }

    @Override
    public MarginLayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected MarginLayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected MarginLayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT,
                MarginLayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    /* ===== API methods ===== */

    public boolean isOpened() {
        return mMenuOpened;
    }

    public void toggle(boolean animate) {
        if (isOpened()) {
            close(animate);
        } else {
            open(animate);
        }
    }

    public void open(final boolean animate) {
        if (!isOpened()) {
            mCloseAnimatorSet.cancel();
            mOpenAnimatorSet.start();
            mMenuOpened = true;
            int delay = 0;
            for (int i = getChildCount() - 1; i >= 0; i--) {
                View child = getChildAt(i);
                if (child instanceof FloatingActionButton
                        && child != mMenuButton && child.getVisibility() != GONE) {

                    final FloatingActionButton fab = (FloatingActionButton) child;
                    mUiHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fab.show(animate);
                            Label label = (Label) fab.getTag(R.id.fab_label);
                            if (label != null) {
                                label.show(animate);
                            }
                        }
                    }, delay);
                    delay += mAnimationDelayPerItem;
                }
            }

            if (mToggleListener != null) {
                mToggleListener.onMenuToggle(true);
            }
        }
    }

    public void close(final boolean animate) {
        if (isOpened()) {
            mCloseAnimatorSet.start();
            mOpenAnimatorSet.cancel();
            mMenuOpened = false;
            int delay = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof FloatingActionButton
                        && child != mMenuButton && child.getVisibility() != GONE) {

                    final FloatingActionButton fab = (FloatingActionButton) child;
                    mUiHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fab.hide(animate);
                            Label label = (Label) fab.getTag(R.id.fab_label);
                            if (label != null) {
                                label.hide(animate);
                            }
                        }
                    }, delay);
                    delay += mAnimationDelayPerItem;
                }
            }

            if (mToggleListener != null) {
                mToggleListener.onMenuToggle(false);
            }
        }
    }

    /**
     * Sets the {@link android.view.animation.Interpolator} for <b>FloatingActionButton's</b> icon animation.
     *
     * @param interpolator the Interpolator to be used in animation
     */
    public void setIconAnimationInterpolator(Interpolator interpolator) {
        mOpenAnimatorSet.setInterpolator(interpolator);
        mCloseAnimatorSet.setInterpolator(interpolator);
    }

    public void setIconAnimationOpenInterpolator(Interpolator openInterpolator) {
        mOpenAnimatorSet.setInterpolator(openInterpolator);
    }

    public void setIconAnimationCloseInterpolator(Interpolator closeInterpolator) {
        mCloseAnimatorSet.setInterpolator(closeInterpolator);
    }

    /**
     * Sets whether open and close actions should be animated
     *
     * @param animated if <b>false</b> - menu items will appear/disappear instantly without any animation
     */
    public void setAnimated(boolean animated) {
        mIsAnimated = animated;
        mOpenAnimatorSet.setDuration(animated ? ANIMATION_DURATION : 0);
        mCloseAnimatorSet.setDuration(animated ? ANIMATION_DURATION : 0);
    }

    public boolean isAnimated() {
        return mIsAnimated;
    }

    public void setAnimationDelayPerItem(int animationDelayPerItem) {
        mAnimationDelayPerItem = animationDelayPerItem;
    }

    public void setOnMenuToggleListener(OnMenuToggleListener listener) {
        mToggleListener = listener;
    }
}
