package androidx.recyclerview.widget;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
/* loaded from: classes.dex */
public class PagerSnapHelper extends SnapHelper {
    public OrientationHelper mHorizontalHelper;
    public OrientationHelper mVerticalHelper;

    @Override // androidx.recyclerview.widget.SnapHelper
    public int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager layoutManager, View view) {
        int[] iArr = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            iArr[0] = distanceToCenter(view, getHorizontalHelper(layoutManager));
        } else {
            iArr[0] = 0;
        }
        if (layoutManager.canScrollVertically()) {
            iArr[1] = distanceToCenter(view, getVerticalHelper(layoutManager));
        } else {
            iArr[1] = 0;
        }
        return iArr;
    }

    public final int distanceToCenter(View view, OrientationHelper orientationHelper) {
        return ((orientationHelper.getDecoratedMeasurement(view) / 2) + orientationHelper.getDecoratedStart(view)) - ((orientationHelper.getTotalSpace() / 2) + orientationHelper.getStartAfterPadding());
    }

    public final View findCenterView(RecyclerView.LayoutManager layoutManager, OrientationHelper orientationHelper) {
        int childCount = layoutManager.getChildCount();
        View view = null;
        if (childCount == 0) {
            return null;
        }
        int totalSpace = (orientationHelper.getTotalSpace() / 2) + orientationHelper.getStartAfterPadding();
        int i = Integer.MAX_VALUE;
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = layoutManager.getChildAt(i2);
            int abs = Math.abs(((orientationHelper.getDecoratedMeasurement(childAt) / 2) + orientationHelper.getDecoratedStart(childAt)) - totalSpace);
            if (abs < i) {
                view = childAt;
                i = abs;
            }
        }
        return view;
    }

    @Override // androidx.recyclerview.widget.SnapHelper
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return findCenterView(layoutManager, getVerticalHelper(layoutManager));
        }
        if (layoutManager.canScrollHorizontally()) {
            return findCenterView(layoutManager, getHorizontalHelper(layoutManager));
        }
        return null;
    }

    public final OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager layoutManager) {
        OrientationHelper orientationHelper = this.mHorizontalHelper;
        if (orientationHelper == null || orientationHelper.mLayoutManager != layoutManager) {
            this.mHorizontalHelper = new OrientationHelper(layoutManager) { // from class: androidx.recyclerview.widget.OrientationHelper.1
                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getDecoratedEnd(View view) {
                    return this.mLayoutManager.getDecoratedRight(view) + ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) view.getLayoutParams())).rightMargin;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getDecoratedMeasurement(View view) {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                    return this.mLayoutManager.getDecoratedMeasuredWidth(view) + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getDecoratedMeasurementInOther(View view) {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                    return this.mLayoutManager.getDecoratedMeasuredHeight(view) + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getDecoratedStart(View view) {
                    return this.mLayoutManager.getDecoratedLeft(view) - ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) view.getLayoutParams())).leftMargin;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getEnd() {
                    return this.mLayoutManager.mWidth;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getEndAfterPadding() {
                    RecyclerView.LayoutManager layoutManager2 = this.mLayoutManager;
                    return layoutManager2.mWidth - layoutManager2.getPaddingRight();
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getEndPadding() {
                    return this.mLayoutManager.getPaddingRight();
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getMode() {
                    return this.mLayoutManager.mWidthMode;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getModeInOther() {
                    return this.mLayoutManager.mHeightMode;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getStartAfterPadding() {
                    return this.mLayoutManager.getPaddingLeft();
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getTotalSpace() {
                    RecyclerView.LayoutManager layoutManager2 = this.mLayoutManager;
                    return (layoutManager2.mWidth - layoutManager2.getPaddingLeft()) - this.mLayoutManager.getPaddingRight();
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getTransformedEndWithDecoration(View view) {
                    this.mLayoutManager.getTransformedBoundingBox(view, true, this.mTmpRect);
                    return this.mTmpRect.right;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getTransformedStartWithDecoration(View view) {
                    this.mLayoutManager.getTransformedBoundingBox(view, true, this.mTmpRect);
                    return this.mTmpRect.left;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public void offsetChildren(int i2) {
                    this.mLayoutManager.offsetChildrenHorizontal(i2);
                }
            };
        }
        return this.mHorizontalHelper;
    }

    public final OrientationHelper getVerticalHelper(RecyclerView.LayoutManager layoutManager) {
        OrientationHelper orientationHelper = this.mVerticalHelper;
        if (orientationHelper == null || orientationHelper.mLayoutManager != layoutManager) {
            this.mVerticalHelper = new OrientationHelper(layoutManager) { // from class: androidx.recyclerview.widget.OrientationHelper.2
                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getDecoratedEnd(View view) {
                    return this.mLayoutManager.getDecoratedBottom(view) + ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) view.getLayoutParams())).bottomMargin;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getDecoratedMeasurement(View view) {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                    return this.mLayoutManager.getDecoratedMeasuredHeight(view) + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getDecoratedMeasurementInOther(View view) {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                    return this.mLayoutManager.getDecoratedMeasuredWidth(view) + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getDecoratedStart(View view) {
                    return this.mLayoutManager.getDecoratedTop(view) - ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) view.getLayoutParams())).topMargin;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getEnd() {
                    return this.mLayoutManager.mHeight;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getEndAfterPadding() {
                    RecyclerView.LayoutManager layoutManager2 = this.mLayoutManager;
                    return layoutManager2.mHeight - layoutManager2.getPaddingBottom();
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getEndPadding() {
                    return this.mLayoutManager.getPaddingBottom();
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getMode() {
                    return this.mLayoutManager.mHeightMode;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getModeInOther() {
                    return this.mLayoutManager.mWidthMode;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getStartAfterPadding() {
                    return this.mLayoutManager.getPaddingTop();
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getTotalSpace() {
                    RecyclerView.LayoutManager layoutManager2 = this.mLayoutManager;
                    return (layoutManager2.mHeight - layoutManager2.getPaddingTop()) - this.mLayoutManager.getPaddingBottom();
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getTransformedEndWithDecoration(View view) {
                    this.mLayoutManager.getTransformedBoundingBox(view, true, this.mTmpRect);
                    return this.mTmpRect.bottom;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public int getTransformedStartWithDecoration(View view) {
                    this.mLayoutManager.getTransformedBoundingBox(view, true, this.mTmpRect);
                    return this.mTmpRect.top;
                }

                @Override // androidx.recyclerview.widget.OrientationHelper
                public void offsetChildren(int i2) {
                    this.mLayoutManager.offsetChildrenVertical(i2);
                }
            };
        }
        return this.mVerticalHelper;
    }
}
