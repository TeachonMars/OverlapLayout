package com.teachonmars.modules.widget.overlapLayout;

import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;

import static android.support.v4.view.ViewCompat.getLayoutDirection;
import static android.view.View.GONE;
import static com.teachonmars.modules.widget.overlapLayout.OverlapLayout.HORIZONTAL;

class ChildLayoutDelegate {
    private final Rect          childPosition;
    private final Rect          availableSpace;
    private final OverlapLayout parent;
    private       int           layoutDirection;
    private       int           parentHorizontalGravity;
    private       int           parentVerticalGravity;

    public ChildLayoutDelegate(OverlapLayout overlapLayout) {
        parent = overlapLayout;
        childPosition = new Rect();
        availableSpace = new Rect();
    }

    public void layout(int l, int t, int r, int b) {
        initBeforeLayout(l, t, r, b);
        if (parent.orientation == HORIZONTAL) {
            layoutHorizontal();
        } else {
            layoutVertical();
        }
    }

    private void layoutHorizontal() {
        int nbLayouted = 0;
//        Rect marginRect = new Rect();
        for (int pos = 0, count = parent.getChildCount(); pos < count && nbLayouted < parent.getMaxChildShowCount(); pos++) {
            View child = parent.getChildAt(pos);
            if (child.getVisibility() != GONE) {
//                retrieveChildMargins(child, marginRect);
                alignVerticalChild(child);
                if (nbLayouted == 0) {
                    horizontalInitChildPosition(child);
                } else {
                    int childWidth = child.getMeasuredWidth();
                    childPosition.right += childWidth * parent.overlapFactor;
                    childPosition.left = childPosition.right - childWidth;
                }
                child.layout(childPosition.left, childPosition.top, childPosition.right, childPosition.bottom);
                nbLayouted++;
            }
        }
    }

//    private void retrieveChildMargins(View child, Rect out) {
//        OverlapLayout.LayoutParams lp = ((OverlapLayout.LayoutParams) child.getLayoutParams());
//        out.set(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin);
//    }

    private void alignVerticalChild(View child) {
//        int gravity= child.getLayoutParams().gravity;
//        if(gravity<0){
//              gravity= parentVerticalGravity;
//        }
        switch (parentVerticalGravity) {
            case Gravity.TOP:
                childPosition.top = availableSpace.top;
                childPosition.bottom = availableSpace.top + child.getMeasuredHeight();
                break;
            case Gravity.BOTTOM:
                childPosition.top = availableSpace.bottom - child.getMeasuredHeight();
                childPosition.bottom = availableSpace.bottom;
                break;
            case Gravity.CENTER_VERTICAL:
            case Gravity.CENTER:
            default:
                float childHalfHeight = child.getMeasuredHeight() / 2f;
                float parentVerticalCenter = availableSpace.exactCenterY();
                childPosition.top = (int) (parentVerticalCenter - childHalfHeight);
                childPosition.bottom = (int) (parentVerticalCenter + childHalfHeight);
                break;
        }
    }

    private void horizontalInitChildPosition(View child) {
        switch (parentHorizontalGravity) {
            case Gravity.RIGHT:
                childPosition.left = availableSpace.right - parent.allChildRect.width();
                childPosition.right = childPosition.left + child.getMeasuredWidth();
                break;
            case Gravity.CENTER:
            case Gravity.CENTER_HORIZONTAL:
                //TODO, fallback to LEFT
            case Gravity.LEFT:
            default:
                childPosition.left = availableSpace.left;
                childPosition.right = childPosition.left + child.getMeasuredWidth();
                break;
        }
    }

    private void layoutVertical() {
        int nbLayouted = 0;
        for (int pos = 0, count = parent.getChildCount(); pos < count && nbLayouted < parent.getMaxChildShowCount(); pos++) {
            View child = parent.getChildAt(pos);
            if (child.getVisibility() != GONE) {
                alignHorizontalChild(child);
                if (nbLayouted == 0) {
                    verticalInitChildPosition(child);
                } else {
                    int childHeight = child.getMeasuredHeight();
                    childPosition.bottom += childHeight * parent.overlapFactor;
                    childPosition.top = childPosition.bottom - childHeight;
                }
                child.layout(childPosition.left, childPosition.top, childPosition.right, childPosition.bottom);
                nbLayouted++;
            }
        }
    }

    private void verticalInitChildPosition(View child) {
        switch (parentVerticalGravity) {
            case Gravity.BOTTOM:
                childPosition.top = availableSpace.bottom - parent.allChildRect.height();
                childPosition.bottom = childPosition.top + child.getMeasuredHeight();
                break;
            case Gravity.CENTER:
            case Gravity.CENTER_VERTICAL:
                //TODO, fallback to TOP
            case Gravity.TOP:
            default:
                childPosition.top = availableSpace.top;
                childPosition.bottom = availableSpace.top + child.getMeasuredHeight();
                break;
        }
    }

    private void alignHorizontalChild(View child) {
//        int gravity= Gravity.getAbsoluteGravity(child.getLayoutParams().gravity, layoutDirection);
//        if(gravity<0){
//              gravity= parentVerticalGravity;
//        }
        switch (parentHorizontalGravity) {
            case Gravity.LEFT:
                childPosition.left = availableSpace.left;
                childPosition.right = availableSpace.left + child.getMeasuredWidth();
                break;
            case Gravity.RIGHT:
                childPosition.left = availableSpace.right - child.getMeasuredWidth();
                childPosition.right = availableSpace.right;
                break;
            case Gravity.CENTER_HORIZONTAL:
            case Gravity.CENTER:
            default:
                float childHalfWidth = child.getMeasuredWidth() / 2f;
                float parentHorizontalCenter = availableSpace.exactCenterX();
                childPosition.left = (int) (parentHorizontalCenter - childHalfWidth);
                childPosition.right = (int) (parentHorizontalCenter + childHalfWidth);
                break;
        }
    }

    private void initBeforeLayout(int l, int t, int r, int b) {
        layoutDirection = getLayoutDirection(parent);
        availableSpace.set(parent.getPaddingLeft(), parent.getPaddingTop(), r - l - parent.getPaddingRight(), b - t - parent.getPaddingBottom());

        parentHorizontalGravity = Gravity.getAbsoluteGravity(parent.getGravity() & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK, layoutDirection);
        parentVerticalGravity = parent.getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
    }
}
