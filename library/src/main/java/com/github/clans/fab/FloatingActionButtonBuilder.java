package com.github.clans.fab;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

/**
 * The <code>FloatingActionButtonBuilder</code> helps you construct easily a
 * {@link #FloatingActionButton} directly from your activity (no xml resource
 * invocation).<br>
 * <br>
 * Example:
 * <br>
 * <br>
 * <b><code>FloatingActionButtonBuilder builder = new FloatingActionButtonBuilder(this);</code></b><br>
 * <b><code>builder.setSize(56);</code></b><br>
 * <b><code>builder.setColorNormal(colorNormal);</code></b><br>
 * <b><code>builder.setColorPressed(colorPressed);</code></b><br>
 * <b><code>builder.setColorRipple(colorRipple);</code></b><br>
 * <b><code>builder.setDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));</code></b><br>
 * <b><code>builder.setElevation(4);</code></b><br>
 * <b><code>builder.setMargins(0, 0, bottomEndMargins, bottomEndMargins);</code></b><br>
 * <b><code>FloatingActionButton fab = builder.build();</code></b><br>
 * <b><code>fab.setOnClickListener(this);</code></b><br>
 * 
 * @author Erkan Molla
 * @version 1.0.0
 */
public class FloatingActionButtonBuilder {

    private final Activity       mActivity;
    private final DisplayMetrics mDisplayMetrics;
    private LayoutParams         mLayoutParams = null;
    private int                  mGravity      = Gravity.BOTTOM | Gravity.END;
    private Drawable             mDrawable     = null;
    private int                  mColorNormal  = 0;
    private int                  mColorPressed = 0;
    private int                  mColorRipple  = 0;
    private float                mElevation    = 0;

    /**
     * Initialize the builder class with your activity as parameter.
     * 
     * @param context The activity that is calling this constructor
     */
    public FloatingActionButtonBuilder(final Activity context) {
        mActivity = context;
        mDisplayMetrics = context.getResources().getDisplayMetrics();
    }

    /**
     * Sets the size in dp.
     * 
     * @param dp density-independent pixel
     * @return this
     * @see <a
     *      href="http://developer.android.com/guide/practices/screens_support.html">Supporting
     *      Multiple Screens</a>
     */
    public FloatingActionButtonBuilder setSize(final int dp) {
        int px = (int) toPixel(dp);
        mLayoutParams = new FrameLayout.LayoutParams(px, px);
        return this;
    }

    /**
     * Sets the gravity.
     * 
     * @param gravity to be set
     * @return this
     * @see <a
     *      href="http://developer.android.com/reference/android/view/Gravity.html">Gravity</a>
     */
    public FloatingActionButtonBuilder setGravity(final int gravity) {
        mGravity = gravity;
        return this;
    }

    /**
     * Sets the margins in dp.
     * 
     * @param left the left margin size
     * @param top the top margin size
     * @param right the right margin size
     * @param bottom the bottom margin size
     * @return this
     */
    public FloatingActionButtonBuilder setMargins(final int left, final int top, final int right,
            final int bottom) {
        int leftPx = (int) toPixel(left);
        int topPx = (int) toPixel(top);
        int rightPx = (int) toPixel(right);
        int bottomPx = (int) toPixel(bottom);
        mLayoutParams.setMargins(leftPx, topPx, rightPx, bottomPx);
        return this;
    }

    /**
     * Sets the drawable.
     * 
     * @param drawable to be set
     * @return this
     */
    public FloatingActionButtonBuilder setDrawable(final Drawable drawable) {
        mDrawable = drawable;
        return this;
    }

    /**
     * Sets the normal, pressed and ripple colors.
     * 
     * @param colorNormal normal color
     * @param colorPressed pressed color
     * @param colorRipple ripple color
     * @return this
     */
    public FloatingActionButtonBuilder setColors(final int colorNormal, final int colorPressed,
            final int colorRipple) {
        mColorNormal = colorNormal;
        mColorPressed = colorPressed;
        mColorRipple = colorRipple;
        return this;
    }

    /**
     * Sets the normal color.
     * 
     * @param colorNormal normal color
     * @return this
     */
    public FloatingActionButtonBuilder setColorNormal(final int colorNormal) {
        mColorNormal = colorNormal;
        return this;
    }

    /**
     * Sets the pressed color.
     * 
     * @param colorPressed pressed color
     * @return this
     */
    public FloatingActionButtonBuilder setColorPressed(final int colorPressed) {
        mColorPressed = colorPressed;
        return this;
    }

    /**
     * Sets the ripple color.
     * 
     * @param colorRipple ripple color
     * @return this
     */
    public FloatingActionButtonBuilder setColorRipple(final int colorRipple) {
        mColorRipple = colorRipple;
        return this;
    }

    /**
     * Sets the elevation in dp.
     * 
     * @param dp elevation to be applied
     * @return this
     */
    public FloatingActionButtonBuilder setElevation(final int dp) {
        mElevation = toPixel(dp);
        return this;
    }

    /**
     * Builds the <code>FloatingActionButton</code> with all specified
     * parameters.
     * 
     * @return a <code>FloatingActionButton</code> as requested
     */
    public FloatingActionButton build() {
        final FloatingActionButton fab = new FloatingActionButton(mActivity);
        fab.setButtonSize(FloatingActionButton.SIZE_NORMAL);
        fab.setImageDrawable(mDrawable);
        fab.setColors(mColorNormal, mColorPressed, mColorRipple);
        fab.setElevationCompat(mElevation);
        mLayoutParams.gravity = mGravity;
        ViewGroup root = (ViewGroup) mActivity.findViewById(android.R.id.content);
        root.addView(fab, mLayoutParams);
        return fab;
    }

    /**
     * The calculation (dp * scale + 0.5f) is a widely used to convert dp to
     * pixel units based on density scale.
     * 
     * @param dp density-independent pixel
     * @param scale the logical density of the display
     * @return the pixel value
     * @see <a href=
     *      "http://developer.android.com/guide/practices/screens_support.html#dips-pels"
     *      >Converting dp units to pixel units</a>
     */
    public int toPixel(int dp, float scale) {
        return (int) (dp * scale + 0.5f);
    }

    /**
     * Converts the given dp value to pixels.
     * 
     * @param dp density-independent pixel
     * @return pixels of the given dp
     */
    public float toPixel(final int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mDisplayMetrics);
    }
}
