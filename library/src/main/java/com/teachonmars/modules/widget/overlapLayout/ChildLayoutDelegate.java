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

    private void initBeforeLayout(int l, int t, int r, int b) {
        availableSpace.set(parent.getPaddingLeft(), parent.getPaddingTop(), r - l - parent.getPaddingRight(), b - t - parent.getPaddingBottom());
        parentHorizontalGravity = Gravity.getAbsoluteGravity(parent.getGravity() & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK, getLayoutDirection(parent));
        parentVerticalGravity = parent.getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
    }

    private void layoutHorizontal() {
        int nbLayouted = 0;
        Rect marginRect = new Rect();
        for (int pos = 0, count = parent.getChildCount(); pos < count && nbLayouted < parent.getMaxChildShowCount(); pos++) {
            View child = parent.getChildAt(pos);
            if (child.getVisibility() != GONE) {
                retrieveChildMargins(child, marginRect);
                alignVerticalChild(child, marginRect);
                if (nbLayouted == 0) {
                    horizontalInitChildPosition(child, marginRect);
                } else {
                    horizontalPushChildPosition(child, marginRect);
                }
                layoutChild(child, childPosition, marginRect);
                nbLayouted++;
            }
        }
    }

    private void alignVerticalChild(View child, Rect marginRect) {
        switch (parentVerticalGravity) {
            case Gravity.TOP:
                childPosition.top = availableSpace.top;
                childPosition.bottom = childPosition.top + child.getMeasuredHeight() + marginRect.top + marginRect.bottom;
                break;
            case Gravity.BOTTOM:
                childPosition.bottom = availableSpace.bottom;
                childPosition.top = childPosition.bottom - (child.getMeasuredHeight() + marginRect.top + marginRect.bottom);
                break;
            case Gravity.CENTER_VERTICAL:
            case Gravity.CENTER:
            default:
                float childHalfHeight = (child.getMeasuredHeight() + marginRect.top + marginRect.bottom) / 2f;
                float parentVerticalCenter = availableSpace.exactCenterY();
                childPosition.top = (int) (parentVerticalCenter - childHalfHeight);
                childPosition.bottom = (int) (parentVerticalCenter + childHalfHeight);
                break;
        }

    }

    private void horizontalInitChildPosition(View child, Rect marginRect) {
        switch (parentHorizontalGravity) {
            case Gravity.RIGHT:
                childPosition.left = availableSpace.right - parent.allChildRect.width() + marginRect.left;
                break;
            case Gravity.CENTER:
            case Gravity.CENTER_HORIZONTAL:
                childPosition.left = (int) (availableSpace.left + (availableSpace.width() - parent.allChildRect.width()) / 2f);
                break;
            case Gravity.LEFT:
            default:
                childPosition.left = availableSpace.left;
                break;
        }
        childPosition.right = childPosition.left + child.getMeasuredWidth() + marginRect.left + marginRect.right;
    }

    private void horizontalPushChildPosition(View child, Rect marginRect) {
        int childWidth = child.getMeasuredWidth();
        childPosition.left = (int) (childPosition.right - parent.overlapFactor * childWidth);
        childPosition.right = childPosition.left + childWidth + marginRect.left + marginRect.right;
    }

    private void layoutVertical() {
        int nbLayouted = 0;
        Rect marginRect = new Rect();
        for (int pos = 0, count = parent.getChildCount(); pos < count && nbLayouted < parent.getMaxChildShowCount(); pos++) {
            View child = parent.getChildAt(pos);
            if (child.getVisibility() != GONE) {
                retrieveChildMargins(child, marginRect);
                alignHorizontalChild(child, marginRect);
                if (nbLayouted == 0) {
                    verticalInitChildPosition(child, marginRect);
                } else {
                    verticalPushChildPosition(child, marginRect);
                }
                layoutChild(child, childPosition, marginRect);
                nbLayouted++;
            }
        }
    }

    private void alignHorizontalChild(View child, Rect marginRect) {
        switch (parentHorizontalGravity) {
            case Gravity.LEFT:
                childPosition.left = availableSpace.left;
                childPosition.right = childPosition.left + child.getMeasuredWidth() + marginRect.left + marginRect.right;
                break;
            case Gravity.RIGHT:
                childPosition.right = availableSpace.right;
                childPosition.left = childPosition.right - (child.getMeasuredWidth() + marginRect.left + marginRect.right);
                break;
            case Gravity.CENTER_HORIZONTAL:
            case Gravity.CENTER:
            default:
                float childHalfWidth = (child.getMeasuredWidth() + marginRect.left + marginRect.right) / 2f;
                float parentHorizontalCenter = availableSpace.exactCenterX();
                childPosition.left = (int) (parentHorizontalCenter - childHalfWidth);
                childPosition.right = (int) (parentHorizontalCenter + childHalfWidth);
                break;
        }
    }

    private void verticalInitChildPosition(View child, Rect marginRect) {
        switch (parentVerticalGravity) {
            case Gravity.BOTTOM:
                childPosition.top = availableSpace.bottom - parent.allChildRect.height();
                break;
            case Gravity.CENTER:
            case Gravity.CENTER_VERTICAL:
                childPosition.top = (int) (availableSpace.top + (availableSpace.height() - parent.allChildRect.height()) / 2f);
                break;
            case Gravity.TOP:
            default:
                childPosition.top = availableSpace.top;
                break;
        }
        childPosition.bottom = childPosition.top + child.getMeasuredHeight() + marginRect.top + marginRect.bottom;
    }

    private void verticalPushChildPosition(View child, Rect marginRect) {
        int childHeight = child.getMeasuredHeight();
        childPosition.top = (int) (childPosition.bottom - (childHeight * parent.overlapFactor));
        childPosition.bottom = childPosition.top + childHeight + marginRect.top + marginRect.bottom;
    }

    private void retrieveChildMargins(View child, Rect out) {
        OverlapLayout.LayoutParams lp = ((OverlapLayout.LayoutParams) child.getLayoutParams());
        out.set(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin);
    }

    private void layoutChild(View child, Rect childPosition, Rect marginRect) {
        child.layout(childPosition.left + marginRect.left, childPosition.top + marginRect.top, childPosition.right - marginRect.right, childPosition.bottom - marginRect.bottom);
    }
}
