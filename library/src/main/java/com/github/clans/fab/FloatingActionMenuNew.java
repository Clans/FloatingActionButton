package com.github.clans.fab;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

public class FloatingActionMenuNew extends ViewGroup {

    private static final int ANIMATION_DURATION = 300;

    private static final float CLOSED_PLUS_ROTATION = 0f;
    private static final float OPENED_PLUS_ROTATION_LEFT = -90f - 45f;
    private static final float OPENED_PLUS_ROTATION_RIGHT = 90f + 45f;

    private static final int LABELS_POSITION_LEFT = 0;
    private static final int LABELS_POSITION_RIGHT = 1;

    private static final int OPEN_UP = 0;
    private static final int OPEN_DOWN = 1;

    private AnimatorSet mOpenAnimatorSet = new AnimatorSet();
    private AnimatorSet mCloseAnimatorSet = new AnimatorSet();
    private Interpolator mOpenInterpolator;
    private Interpolator mCloseInterpolator;
    private AnimatorSet mIconToggleSet;

    private int mButtonSpacing = Util.dpToPx(getContext(), 2f);
    private FloatingActionButtonNew mMenuButton;
    private int mMaxButtonWidth;
    private int mLabelsMargin = Util.dpToPx(getContext(), 0f);
    private int mLabelsVerticalOffset = Util.dpToPx(getContext(), 0f);
    private int mButtonsCount;
    private boolean mMenuOpened;
    private boolean mIsMenuOpening;
    private Handler mUiHandler = new Handler();

    // Lables
    private int mLabelsShowAnimation;
    private int mLabelsHideAnimation;
    private int mLabelsPaddingTop = Util.dpToPx(getContext(), 4f);
    private int mLabelsPaddingRight = Util.dpToPx(getContext(), 8f);
    private int mLabelsPaddingBottom = Util.dpToPx(getContext(), 4f);
    private int mLabelsPaddingLeft = Util.dpToPx(getContext(), 8f);
    private ColorStateList mLabelsTextColor;
    private float mLabelsTextSize;
    private int mLabelsCornerRadius = Util.dpToPx(getContext(), 3f);
    private boolean mLabelsShowShadow;
    private int mLabelsColorNormal;
    private int mLabelsColorPressed;
    private int mLabelsColorRipple;
    private int mLabelsPosition;
    private Context mLabelsContext;
    private int mLabelsStyle;

    private ValueAnimator mShowBackgroundAnimator;
    private ValueAnimator mHideBackgroundAnimator;
    private int mBackgroundColor;

    private int mOpenDirection;

    private String mMenuLabelText;
    private boolean mUsingMenuLabel;

    private int mMenuFabSize;

    private Drawable mIcon;
    private boolean mUsingCustomIcon;
    private ImageView mImageToggle;
    private boolean mIconAnimated = true;

    private boolean mIsAnimated = true;

    private int mAnimationDelayPerItem;

    private OnMenuToggleListener mToggleListener;

    private int mHiddenFabs;

    public interface OnMenuToggleListener {
        void onMenuToggle(boolean opened);
    }

    public FloatingActionMenuNew(Context context) {
        this(context, null);
    }

    public FloatingActionMenuNew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingActionMenuNew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FloatingActionMenuNew(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionMenu, 0, 0);

        mButtonSpacing = attr.getDimensionPixelSize(R.styleable.FloatingActionMenu_menu_buttonSpacing, mButtonSpacing);
        mLabelsMargin = attr.getDimensionPixelSize(R.styleable.FloatingActionMenu_menu_labels_margin, mLabelsMargin);
        mLabelsPosition = attr.getInt(R.styleable.FloatingActionMenu_menu_labels_position, LABELS_POSITION_LEFT);
        mLabelsShowAnimation = attr.getResourceId(R.styleable.FloatingActionMenu_menu_labels_showAnimation,
                mLabelsPosition == LABELS_POSITION_LEFT ? R.anim.fab_slide_in_from_right : R.anim.fab_slide_in_from_left);
        mLabelsHideAnimation = attr.getResourceId(R.styleable.FloatingActionMenu_menu_labels_hideAnimation,
                mLabelsPosition == LABELS_POSITION_LEFT ? R.anim.fab_slide_out_to_right : R.anim.fab_slide_out_to_left);
        mLabelsPaddingTop = attr.getDimensionPixelSize(R.styleable.FloatingActionMenu_menu_labels_paddingTop, mLabelsPaddingTop);
        mLabelsPaddingRight = attr.getDimensionPixelSize(R.styleable.FloatingActionMenu_menu_labels_paddingRight, mLabelsPaddingRight);
        mLabelsPaddingBottom = attr.getDimensionPixelSize(R.styleable.FloatingActionMenu_menu_labels_paddingBottom, mLabelsPaddingBottom);
        mLabelsPaddingLeft = attr.getDimensionPixelSize(R.styleable.FloatingActionMenu_menu_labels_paddingLeft, mLabelsPaddingLeft);
        mLabelsTextColor = attr.getColorStateList(R.styleable.FloatingActionMenu_menu_labels_textColor);
        // set default value if null same as for textview
        if (mLabelsTextColor == null) {
            mLabelsTextColor = ColorStateList.valueOf(Color.WHITE);
        }
        mLabelsTextSize = attr.getDimension(R.styleable.FloatingActionMenu_menu_labels_textSize, getResources().getDimension(R.dimen.labels_text_size));
        mLabelsCornerRadius = attr.getDimensionPixelSize(R.styleable.FloatingActionMenu_menu_labels_cornerRadius, mLabelsCornerRadius);
        mLabelsShowShadow = attr.getBoolean(R.styleable.FloatingActionMenu_menu_labels_showShadow, true);
        mLabelsColorNormal = attr.getColor(R.styleable.FloatingActionMenu_menu_labels_colorNormal, 0xFF333333);
        mLabelsColorPressed = attr.getColor(R.styleable.FloatingActionMenu_menu_labels_colorPressed, 0xFF444444);
        mLabelsColorRipple = attr.getColor(R.styleable.FloatingActionMenu_menu_labels_colorRipple, 0x66FFFFFF);
        mLabelsStyle = attr.getResourceId(R.styleable.FloatingActionMenu_menu_labels_style, 0);
        mLabelsContext = new ContextThemeWrapper(getContext(), mLabelsStyle);

        mAnimationDelayPerItem = attr.getInt(R.styleable.FloatingActionMenu_menu_animationDelayPerItem, 50);

        if (attr.hasValue(R.styleable.FloatingActionMenu_menu_fab_label)) {
            mUsingMenuLabel = true;
            mMenuLabelText = attr.getString(R.styleable.FloatingActionMenu_menu_fab_label);
        }

        mMenuFabSize = attr.getInt(R.styleable.FloatingActionMenu_menu_fab_size, FloatingActionButton.SIZE_NORMAL);

        if (attr.hasValue(R.styleable.FloatingActionMenu_menu_src)) {
            mIcon = attr.getDrawable(R.styleable.FloatingActionMenu_menu_src);
        } else {
            mUsingCustomIcon = true;
            // TODO: think on new name for the attribute 'menu_icon'
            mIcon = attr.getDrawable(R.styleable.FloatingActionMenu_menu_icon);
            if (mIcon == null) {
                mIcon = ContextCompat.getDrawable(getContext(), R.drawable.fab_add);
            }
        }

        initBackgroundDimAnimation();
        createMenuButton();

        attr.recycle();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                requestLayout();
                if (Util.hasJellyBean()) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    private void initBackgroundDimAnimation() {
        final int maxAlpha = Color.alpha(mBackgroundColor);
        final int red = Color.red(mBackgroundColor);
        final int green = Color.green(mBackgroundColor);
        final int blue = Color.blue(mBackgroundColor);

        mShowBackgroundAnimator = ValueAnimator.ofInt(0, maxAlpha);
        mShowBackgroundAnimator.setDuration(ANIMATION_DURATION);
        mShowBackgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer alpha = (Integer) animation.getAnimatedValue();
                setBackgroundColor(Color.argb(alpha, red, green, blue));
            }
        });

        mHideBackgroundAnimator = ValueAnimator.ofInt(maxAlpha, 0);
        mHideBackgroundAnimator.setDuration(ANIMATION_DURATION);
        mHideBackgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer alpha = (Integer) animation.getAnimatedValue();
                setBackgroundColor(Color.argb(alpha, red, green, blue));
            }
        });
    }

    private void createMenuButton() {
        mMenuButton = new FloatingActionButtonNew(getContext());
        mMenuButton.setSize(mMenuFabSize);

        mImageToggle = new ImageView(getContext());
        mImageToggle.setImageDrawable(mIcon);

        if (mUsingCustomIcon) {
            if (Util.hasLollipop()) {
                mMenuButton.setElevation(Util.dpToPx(getContext(), 4f));
                mMenuButton.setStateListAnimator(null);
                mImageToggle.setElevation(mMenuButton.getCompatElevation());
            }
        } else {
            mMenuButton.setImageDrawable(mIcon);
        }

        addView(mMenuButton, super.generateDefaultLayoutParams());
        if (mUsingCustomIcon) {
            addView(mImageToggle);
            createDefaultIconAnimation();
        }
    }

    private void createDefaultIconAnimation() {
        float collapseAngle;
        float expandAngle;

        mOpenInterpolator = new OvershootInterpolator();
        mCloseInterpolator = new AnticipateInterpolator();

        if (mOpenDirection == OPEN_UP) {
            collapseAngle = mLabelsPosition == LABELS_POSITION_LEFT ? OPENED_PLUS_ROTATION_LEFT : OPENED_PLUS_ROTATION_RIGHT;
            expandAngle = mLabelsPosition == LABELS_POSITION_LEFT ? OPENED_PLUS_ROTATION_LEFT : OPENED_PLUS_ROTATION_RIGHT;
        } else {
            collapseAngle = mLabelsPosition == LABELS_POSITION_LEFT ? OPENED_PLUS_ROTATION_RIGHT : OPENED_PLUS_ROTATION_LEFT;
            expandAngle = mLabelsPosition == LABELS_POSITION_LEFT ? OPENED_PLUS_ROTATION_RIGHT : OPENED_PLUS_ROTATION_LEFT;
        }

        ObjectAnimator collapseAnimator = ObjectAnimator.ofFloat(
                mImageToggle,
                "rotation",
                collapseAngle,
                CLOSED_PLUS_ROTATION
        );

        ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(
                mImageToggle,
                "rotation",
                CLOSED_PLUS_ROTATION,
                expandAngle
        );

        mOpenAnimatorSet.play(expandAnimator);
        mCloseAnimatorSet.play(collapseAnimator);

        mOpenAnimatorSet.setInterpolator(mOpenInterpolator);
        mCloseAnimatorSet.setInterpolator(mCloseInterpolator);

        mOpenAnimatorSet.setDuration(ANIMATION_DURATION);
        mCloseAnimatorSet.setDuration(ANIMATION_DURATION);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;
        mMaxButtonWidth = 0;
        int maxLabelWidth = 0;

        measureChildWithMargins(mImageToggle, widthMeasureSpec, 0, heightMeasureSpec, 0);

        for (int i = 0; i < mButtonsCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE || child == mImageToggle) continue;

            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            mMaxButtonWidth = Math.max(mMaxButtonWidth, child.getMeasuredWidth() + child.getPaddingLeft() + child.getPaddingRight());
        }

        for (int i = 0; i < mButtonsCount; i++) {
            int usedWidth = 0;
            View child = getChildAt(i);

            if (child.getVisibility() == GONE || child == mImageToggle) continue;

            usedWidth += child.getMeasuredWidth();
            height += child.getMeasuredHeight() + child.getPaddingTop() + child.getPaddingBottom();

            Label label = (Label) child.getTag(R.id.fab_label);
            if (label != null) {
                int labelOffset = (mMaxButtonWidth - child.getMeasuredWidth()) / (mUsingMenuLabel ? 1 : 2);
                int labelUsedWidth = child.getMeasuredWidth() + label.calculateShadowWidth() + mLabelsMargin + labelOffset;
                measureChildWithMargins(label, widthMeasureSpec, labelUsedWidth, heightMeasureSpec, 0);
                usedWidth += label.getMeasuredWidth();
                maxLabelWidth = Math.max(maxLabelWidth, usedWidth + labelOffset);
            }
        }

        width = Math.max(mMaxButtonWidth, maxLabelWidth + mLabelsMargin) + getPaddingLeft() + getPaddingRight();

        height += mButtonSpacing * (mButtonsCount - 1) + getPaddingTop() + getPaddingBottom();
        height = adjustForOvershoot(height);

        if (getLayoutParams().width == LayoutParams.MATCH_PARENT) {
            width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        }

        if (getLayoutParams().height == LayoutParams.MATCH_PARENT) {
            height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int buttonsHorizontalCenter = mLabelsPosition == LABELS_POSITION_LEFT
                ? r - l - mMaxButtonWidth / 2 - getPaddingRight()
                : mMaxButtonWidth / 2 + getPaddingLeft();
        boolean openUp = mOpenDirection == OPEN_UP;

        int menuButtonTop = openUp
                ? b - t - mMenuButton.getMeasuredHeight() - getPaddingBottom()
                : getPaddingTop();
        int menuButtonLeft = buttonsHorizontalCenter - mMenuButton.getMeasuredWidth() / 2;

        mMenuButton.layout(
                menuButtonLeft,
                menuButtonTop - mMenuButton.getPaddingBottom(),
                menuButtonLeft + mMenuButton.getMeasuredWidth(),
                menuButtonTop + mMenuButton.getMeasuredHeight() - mMenuButton.getPaddingBottom()
        );

        int imageLeft = buttonsHorizontalCenter - mImageToggle.getMeasuredWidth() / 2;
        int imageTop = menuButtonTop + mMenuButton.getMeasuredHeight() / 2
                - mImageToggle.getMeasuredHeight() / 2 - mMenuButton.getPaddingBottom();

        mImageToggle.layout(imageLeft, imageTop, imageLeft + mImageToggle.getMeasuredWidth(),
                imageTop + mImageToggle.getMeasuredHeight());

        int nextY = openUp
                ? menuButtonTop + mMenuButton.getMeasuredHeight() + mButtonSpacing
                : menuButtonTop;

        for (int i = mButtonsCount - 1; i >= 0; i--) {
            View child = getChildAt(i);

            if (child == mImageToggle) continue;

            FloatingActionButtonNew fab = (FloatingActionButtonNew) child;

            if (fab.getVisibility() == GONE) continue;

            int childX = buttonsHorizontalCenter - fab.getMeasuredWidth() / 2;
            int childY = openUp ? nextY - fab.getMeasuredHeight() - fab.getPaddingBottom() - mButtonSpacing : nextY;

            if (fab != mMenuButton) {
                fab.layout(childX, childY, childX + fab.getMeasuredWidth(), childY + fab.getMeasuredHeight());

                if (!mIsMenuOpening) {
                    fab.setVisibility(GONE);
                }
            }

            View label = (View) fab.getTag(R.id.fab_label);
            if (label != null) {
                int labelsOffset = (mUsingMenuLabel ? mMaxButtonWidth / 2 : fab.getMeasuredWidth() / 2) + mLabelsMargin;
                int labelXNearButton = mLabelsPosition == LABELS_POSITION_LEFT
                        ? buttonsHorizontalCenter - labelsOffset
                        : buttonsHorizontalCenter + labelsOffset;

                int labelXAwayFromButton = mLabelsPosition == LABELS_POSITION_LEFT
                        ? labelXNearButton - label.getMeasuredWidth()
                        : labelXNearButton + label.getMeasuredWidth();

                int labelLeft = mLabelsPosition == LABELS_POSITION_LEFT
                        ? labelXAwayFromButton
                        : labelXNearButton;

                int labelRight = mLabelsPosition == LABELS_POSITION_LEFT
                        ? labelXNearButton
                        : labelXAwayFromButton;

                int labelTop = childY - mLabelsVerticalOffset + (fab.getMeasuredHeight()
                        - label.getMeasuredHeight()) / 2;

                label.layout(labelLeft, labelTop, labelRight, labelTop + label.getMeasuredHeight());

                if (!mIsMenuOpening) {
                    label.setVisibility(INVISIBLE);
                }
            }

            nextY = openUp
                    ? childY - mButtonSpacing
                    : childY + child.getMeasuredHeight() + mButtonSpacing;
        }
    }

    private int adjustForOvershoot(int dimension) {
        return (int) (dimension * 0.03 + dimension);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bringChildToFront(mMenuButton);
        bringChildToFront(mImageToggle);
        mButtonsCount = getChildCount();
        createLabels();
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

    private void createLabels() {
        for (int i = 0; i < mButtonsCount; i++) {

            if (getChildAt(i) == mImageToggle) continue;

            final FloatingActionButtonNew fab = (FloatingActionButtonNew) getChildAt(i);

            if (fab.getTag(R.id.fab_label) != null) continue;

//            addLabel(fab);

            if (fab == mMenuButton) {
                mMenuButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggle(mIsAnimated);
                    }
                });
            }
        }
    }

    // TODO: use LabelNew here
    private void addLabel(FloatingActionButtonNew fab) {
        String text = fab.getLabelText();

        if (TextUtils.isEmpty(text)) return;

        final Label label = new Label(mLabelsContext);
        label.setClickable(true);
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
        label.setOnClickListener(fab.getOnClickListener());

        addView(label);
        fab.setTag(R.id.fab_label, label);
    }

    /* =================================================== */
    /* =================   API methods   ================= */
    /* =================================================== */

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
            if (mIconAnimated) {
                if (mIconToggleSet != null) {
                    mIconToggleSet.start();
                } else {
                    mCloseAnimatorSet.cancel();
                    mOpenAnimatorSet.start();
                }
            }

            int delay = 0;
            int counter = 0;
            mIsMenuOpening = true;
            for (int i = getChildCount() - 1; i >= 0; i--) {
                View child = getChildAt(i);
                if (child instanceof FloatingActionButtonNew /*&& child.getVisibility() != GONE*/) {
                    counter++;

                    final FloatingActionButtonNew fab = (FloatingActionButtonNew) child;
                    mUiHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isOpened()) return;

                            if (fab != mMenuButton) {
                                fab.show(animate, null);
                            }

                            Label label = (Label) fab.getTag(R.id.fab_label);
                            if (label != null && label.isHandleVisibilityChanges()) {
                                label.show(animate);
                            }
                        }
                    }, delay);
                    delay += mAnimationDelayPerItem;
                }
            }

            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMenuOpened = true;

                    if (mToggleListener != null) {
                        mToggleListener.onMenuToggle(true);
                    }
                }
            }, ++counter * mAnimationDelayPerItem);
        }
    }

    public void close(final boolean animate) {
        if (isOpened()) {
            if (mIconAnimated) {
                if (mIconToggleSet != null) {
                    mIconToggleSet.start();
                } else {
                    mCloseAnimatorSet.start();
                    mOpenAnimatorSet.cancel();
                }
            }

            int delay = 0;
            mHiddenFabs = 0;
            mIsMenuOpening = false;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof FloatingActionButtonNew /*&& child.getVisibility() != GONE*/) {

                    final FloatingActionButtonNew fab = (FloatingActionButtonNew) child;
                    mUiHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!isOpened()) return;

                            if (fab != mMenuButton) {
                                fab.hide(animate, new FloatingActionButton.OnVisibilityChangedListener() {
                                    @Override
                                    public void onHidden(FloatingActionButton fab) {
                                        if (++mHiddenFabs == getChildCount() - 2) {
                                            mMenuOpened = false;
                                            requestLayout();

                                            if (mToggleListener != null) {
                                                mToggleListener.onMenuToggle(false);
                                            }
                                        }
                                    }
                                });
                            }

                            Label label = (Label) fab.getTag(R.id.fab_label);
                            if (label != null && label.isHandleVisibilityChanges()) {
                                label.hide(animate);
                            }
                        }
                    }, delay);
                    delay += mAnimationDelayPerItem;
                }
            }
        }
    }

    public void setOnMenuButtonClickListener(OnClickListener clickListener) {
        mMenuButton.setOnClickListener(clickListener);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mMenuButton.setOnClickListener(l);
    }

    public void setOnMenuToggleListener(OnMenuToggleListener listener) {
        mToggleListener = listener;
    }
}
