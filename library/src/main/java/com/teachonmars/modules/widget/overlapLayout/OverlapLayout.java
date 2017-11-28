package com.teachonmars.modules.widget.overlapLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class OverlapLayout extends ViewGroup {
    int   orientation;
    float overlapFactor;
    public Rect allChildRect = new Rect();
    private ChildLayoutDelegate childLayoutDelegate;
    private int                 maxChildShowCount;
    private int                 gravity;
    private int                 renderOrder;

    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL   = 1;

    @IntDef({BOTTOM_TO_TOP, TOP_TO_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RenderOrder {
    }

    public static final int BOTTOM_TO_TOP = 0;
    public static final int TOP_TO_BOTTOM = 1;

    public OverlapLayout(Context context) {
        this(context, null);
    }

    public OverlapLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.overlapLayoutStyle);
    }

    public OverlapLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.OverlapLayoutDefault);
    }

    public OverlapLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OverlapLayout, defStyleAttr, defStyleRes);
            initConfig(a);
            a.recycle();
        }
        init();
    }

    private void initConfig(TypedArray a) {
        maxChildShowCount = a.getInteger(R.styleable.OverlapLayout_maxChildShowCount, 0);
        gravity = a.getInt(R.styleable.OverlapLayout_android_gravity, Gravity.END | Gravity.CENTER_VERTICAL);
        orientation = a.getInt(R.styleable.OverlapLayout_android_orientation, HORIZONTAL);
        overlapFactor = a.getFraction(R.styleable.OverlapLayout_overlapFactor, 1, 1, 1);
        renderOrder = a.getInt(R.styleable.OverlapLayout_renderOrder, TOP_TO_BOTTOM);
    }

    private void init() {
        childLayoutDelegate = new ChildLayoutDelegate(this);
        if (renderOrder == TOP_TO_BOTTOM) {
            setChildrenDrawingOrderEnabled(true);
        }
    }

    public int getMaxChildShowCount() {
        return maxChildShowCount;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        childLayoutDelegate.layout(l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int horizontalPadding = ViewCompat.getPaddingStart(this) + ViewCompat.getPaddingEnd(this);
        int verticalPadding = getPaddingTop() + getPaddingBottom();
        int nbMeasured = 0;
        allChildRect.setEmpty();
        for (int pos = 0, count = getChildCount(); pos < count && nbMeasured < maxChildShowCount; pos++) {
            View child = getChildAt(pos);
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, allChildRect.width(), heightMeasureSpec, allChildRect.height());
                updateAllChildBounds(child.getMeasuredWidth(), child.getMeasuredHeight());
                nbMeasured++;
            }
        }
        setMeasuredDimension(
                resolveSize(Math.max(allChildRect.width(), getSuggestedMinimumWidth()) + horizontalPadding, widthMeasureSpec),
                resolveSize(Math.max(allChildRect.height(), getSuggestedMinimumHeight()) + verticalPadding, heightMeasureSpec));
    }

    private void updateAllChildBounds(int childWidth, int childHeight) {
        if (orientation == LinearLayoutCompat.HORIZONTAL) {
            allChildRect.set(0, 0, allChildRect.isEmpty() ? childWidth : (int) (allChildRect.width() + (overlapFactor * childWidth)), childHeight);
        } else {
            allChildRect.set(0, 0, childWidth, allChildRect.isEmpty() ? childHeight : (int) (allChildRect.height() + (overlapFactor * childHeight)));
        }
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (renderOrder == BOTTOM_TO_TOP) {
            return super.getChildDrawingOrder(childCount, i);
        } else {
            return childCount - i - 1;
        }
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams lp) {
        return lp instanceof OverlapLayout.LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new OverlapLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new OverlapLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof OverlapLayout.LayoutParams
                || lp instanceof MarginLayoutParams) {
            return new OverlapLayout.LayoutParams((MarginLayoutParams) lp);
        } else {
            return new OverlapLayout.LayoutParams(lp);
        }
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
