package de.fhdw.ergoholics.brainphaser.activities.usersettings;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by funkv on 16.03.2016.
 *
 * Expand LinearLayout to allow animated expansion/collasing in vertical direction and hide self when fully collapsed.
 * Width needs to be match_parent of static.
 */
public class CollapsingLinearLayout extends LinearLayout implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener  {
    private ValueAnimator mAnimator;
    private boolean mReversing = true;
    private OnChangeListener mOnChangeListener;

    public interface OnChangeListener {
        void onExpand();
        void onCollapse();
    }

    public CollapsingLinearLayout(Context context) {
        super(context);
        init();
    }

    public CollapsingLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CollapsingLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Intializes the animator
     */
    private void init() {
        mAnimator = ValueAnimator.ofInt(0,0);
        mAnimator.addUpdateListener(this);
        mAnimator.addListener(this);
        mAnimator.setInterpolator(new FastOutSlowInInterpolator());
    }

    /**
     * Updates the height on interpolation
     * @param animation mAnimator
     */
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int interpolated = (Integer) mAnimator.getAnimatedValue();
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.height = interpolated;
        setLayoutParams(layoutParams);
        requestLayout();
    }

    /**
     * Set the view to visible if expanding
     */
    @Override
    public void onAnimationStart(Animator animation) {
        if (!mReversing) {
            this.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Hide the view if collapsing
     */
    @Override
    public void onAnimationEnd(Animator animator) {
        if (mReversing) {
            this.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }

    /**
     * Updates the animator and sets target so that children fit.
     */
    public void measureAndAdjust() {
        setVisibility(View.VISIBLE);

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        setLayoutParams(layoutParams);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        measure(widthSpec, heightSpec);

        final int widthSpec2 = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), View.MeasureSpec.AT_MOST);
        final int heightSpec2 = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        measure(widthSpec2, heightSpec2);

        mAnimator.setIntValues(0, getMeasuredHeight());
        Log.d("Measured",  "h" + getMeasuredHeight());
    }

    /**
     * Expands the layout
     */
    public void expand() {
        measureAndAdjust();
        if (getVisibility() == View.GONE || mReversing) {
            mAnimator.start();
            if (mOnChangeListener != null) mOnChangeListener.onExpand();
        }
        mReversing = false;
    }

    /**
     * Collapses the layout
     */
    public void collapse() {
        if (!mReversing) {
            mAnimator.reverse();
            if (mOnChangeListener != null) mOnChangeListener.onCollapse();
            mReversing = true;
        }
    }

    /**
     * Returns the animator used
     * @return the animator used internally for expanding/collapsing
     */
    public ValueAnimator getAnimator() {
        return mAnimator;
    }

    /**
     * Can be used to find out whether the layout is currently in collapsing or expanding mode.
     * Does not indicate whether or not it is currently animating.
     * @return true, if in collapsing mode
     */
    public boolean isCollapsing() {
        return mReversing;
    }

    public void setOnChangeListener(OnChangeListener listener) {
        mOnChangeListener = listener;
    }
}
