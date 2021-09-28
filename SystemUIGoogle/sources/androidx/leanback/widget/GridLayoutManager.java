package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.collection.CircularIntArray;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.Grid;
import androidx.leanback.widget.ItemAlignmentFacet;
import androidx.leanback.widget.WindowAlignment;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class GridLayoutManager extends RecyclerView.LayoutManager {
    private static final Rect sTempRect = new Rect();
    static int[] sTwoInts = new int[2];
    final BaseGridView mBaseGridView;
    GridLinearSmoothScroller mCurrentSmoothScroller;
    int[] mDisappearingPositions;
    private int mExtraLayoutSpace;
    int mExtraLayoutSpaceInPreLayout;
    private FacetProviderAdapter mFacetProviderAdapter;
    private int mFixedRowSizeSecondary;
    Grid mGrid;
    private int mHorizontalSpacing;
    private int mMaxSizeSecondary;
    int mNumRows;
    PendingMoveSmoothScroller mPendingMoveSmoothScroller;
    int mPositionDeltaInPreLayout;
    private int mPrimaryScrollExtra;
    RecyclerView.Recycler mRecycler;
    private int[] mRowSizeSecondary;
    private int mRowSizeSecondaryRequested;
    private int mSaveContextLevel;
    int mScrollOffsetSecondary;
    private int mSizePrimary;
    private int mSpacingPrimary;
    private int mSpacingSecondary;
    RecyclerView.State mState;
    private int mVerticalSpacing;
    float mSmoothScrollSpeedFactor = 1.0f;
    int mMaxPendingMoves = 10;
    int mOrientation = 0;
    private OrientationHelper mOrientationHelper = OrientationHelper.createHorizontalHelper(this);
    final SparseIntArray mPositionToRowInPostLayout = new SparseIntArray();
    int mFlag = 221696;
    private OnChildSelectedListener mChildSelectedListener = null;
    private ArrayList<OnChildViewHolderSelectedListener> mChildViewHolderSelectedListeners = null;
    ArrayList<BaseGridView.OnLayoutCompletedListener> mOnLayoutCompletedListeners = null;
    OnChildLaidOutListener mChildLaidOutListener = null;
    int mFocusPosition = -1;
    int mSubFocusPosition = 0;
    private int mFocusPositionOffset = 0;
    private int mGravity = 8388659;
    private int mNumRowsRequested = 1;
    private int mFocusScrollStrategy = 0;
    final WindowAlignment mWindowAlignment = new WindowAlignment();
    private final ItemAlignment mItemAlignment = new ItemAlignment();
    private int[] mMeasuredDimension = new int[2];
    final ViewsStateBundle mChildrenStates = new ViewsStateBundle();
    private final Runnable mRequestLayoutRunnable = new Runnable() { // from class: androidx.leanback.widget.GridLayoutManager.1
        @Override // java.lang.Runnable
        public void run() {
            GridLayoutManager.this.requestLayout();
        }
    };
    private Grid.Provider mGridProvider = new Grid.Provider() { // from class: androidx.leanback.widget.GridLayoutManager.2
        @Override // androidx.leanback.widget.Grid.Provider
        public int getMinIndex() {
            return GridLayoutManager.this.mPositionDeltaInPreLayout;
        }

        @Override // androidx.leanback.widget.Grid.Provider
        public int getCount() {
            return GridLayoutManager.this.mState.getItemCount() + GridLayoutManager.this.mPositionDeltaInPreLayout;
        }

        @Override // androidx.leanback.widget.Grid.Provider
        public int createItem(int i, boolean z, Object[] objArr, boolean z2) {
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            View viewForPosition = gridLayoutManager.getViewForPosition(i - gridLayoutManager.mPositionDeltaInPreLayout);
            if (!((LayoutParams) viewForPosition.getLayoutParams()).isItemRemoved()) {
                if (z2) {
                    if (z) {
                        GridLayoutManager.this.addDisappearingView(viewForPosition);
                    } else {
                        GridLayoutManager.this.addDisappearingView(viewForPosition, 0);
                    }
                } else if (z) {
                    GridLayoutManager.this.addView(viewForPosition);
                } else {
                    GridLayoutManager.this.addView(viewForPosition, 0);
                }
                int i2 = GridLayoutManager.this.mChildVisibility;
                if (i2 != -1) {
                    viewForPosition.setVisibility(i2);
                }
                PendingMoveSmoothScroller pendingMoveSmoothScroller = GridLayoutManager.this.mPendingMoveSmoothScroller;
                if (pendingMoveSmoothScroller != null) {
                    pendingMoveSmoothScroller.consumePendingMovesBeforeLayout();
                }
                int subPositionByView = GridLayoutManager.this.getSubPositionByView(viewForPosition, viewForPosition.findFocus());
                GridLayoutManager gridLayoutManager2 = GridLayoutManager.this;
                int i3 = gridLayoutManager2.mFlag;
                if ((i3 & 3) != 1) {
                    if (i == gridLayoutManager2.mFocusPosition && subPositionByView == gridLayoutManager2.mSubFocusPosition && gridLayoutManager2.mPendingMoveSmoothScroller == null) {
                        gridLayoutManager2.dispatchChildSelected();
                    }
                } else if ((i3 & 4) == 0) {
                    if ((i3 & 16) == 0 && i == gridLayoutManager2.mFocusPosition && subPositionByView == gridLayoutManager2.mSubFocusPosition) {
                        gridLayoutManager2.dispatchChildSelected();
                    } else if ((i3 & 16) != 0 && i >= gridLayoutManager2.mFocusPosition && viewForPosition.hasFocusable()) {
                        GridLayoutManager gridLayoutManager3 = GridLayoutManager.this;
                        gridLayoutManager3.mFocusPosition = i;
                        gridLayoutManager3.mSubFocusPosition = subPositionByView;
                        gridLayoutManager3.mFlag &= -17;
                        gridLayoutManager3.dispatchChildSelected();
                    }
                }
                GridLayoutManager.this.measureChild(viewForPosition);
            }
            objArr[0] = viewForPosition;
            GridLayoutManager gridLayoutManager4 = GridLayoutManager.this;
            if (gridLayoutManager4.mOrientation == 0) {
                return gridLayoutManager4.getDecoratedMeasuredWidthWithMargin(viewForPosition);
            }
            return gridLayoutManager4.getDecoratedMeasuredHeightWithMargin(viewForPosition);
        }

        @Override // androidx.leanback.widget.Grid.Provider
        public void addItem(Object obj, int i, int i2, int i3, int i4) {
            int i5;
            int i6;
            long j;
            PendingMoveSmoothScroller pendingMoveSmoothScroller;
            View view = (View) obj;
            if (i4 == Integer.MIN_VALUE || i4 == Integer.MAX_VALUE) {
                if (!GridLayoutManager.this.mGrid.isReversedFlow()) {
                    i4 = GridLayoutManager.this.mWindowAlignment.mainAxis().getPaddingMin();
                } else {
                    i4 = GridLayoutManager.this.mWindowAlignment.mainAxis().getSize() - GridLayoutManager.this.mWindowAlignment.mainAxis().getPaddingMax();
                }
            }
            if (!GridLayoutManager.this.mGrid.isReversedFlow()) {
                i5 = i2 + i4;
                i6 = i4;
            } else {
                i6 = i4 - i2;
                i5 = i4;
            }
            int rowStartSecondary = GridLayoutManager.this.getRowStartSecondary(i3) + GridLayoutManager.this.mWindowAlignment.secondAxis().getPaddingMin();
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            int i7 = rowStartSecondary - gridLayoutManager.mScrollOffsetSecondary;
            gridLayoutManager.mChildrenStates.loadView(view, i);
            GridLayoutManager.this.layoutChild(i3, view, i6, i5, i7);
            if (!GridLayoutManager.this.mState.isPreLayout()) {
                GridLayoutManager.this.updateScrollLimits();
            }
            GridLayoutManager gridLayoutManager2 = GridLayoutManager.this;
            if (!((gridLayoutManager2.mFlag & 3) == 1 || (pendingMoveSmoothScroller = gridLayoutManager2.mPendingMoveSmoothScroller) == null)) {
                pendingMoveSmoothScroller.consumePendingMovesAfterLayout();
            }
            GridLayoutManager gridLayoutManager3 = GridLayoutManager.this;
            if (gridLayoutManager3.mChildLaidOutListener != null) {
                RecyclerView.ViewHolder childViewHolder = gridLayoutManager3.mBaseGridView.getChildViewHolder(view);
                GridLayoutManager gridLayoutManager4 = GridLayoutManager.this;
                OnChildLaidOutListener onChildLaidOutListener = gridLayoutManager4.mChildLaidOutListener;
                BaseGridView baseGridView = gridLayoutManager4.mBaseGridView;
                if (childViewHolder == null) {
                    j = -1;
                } else {
                    j = childViewHolder.getItemId();
                }
                onChildLaidOutListener.onChildLaidOut(baseGridView, view, i, j);
            }
        }

        @Override // androidx.leanback.widget.Grid.Provider
        public void removeItem(int i) {
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            View findViewByPosition = gridLayoutManager.findViewByPosition(i - gridLayoutManager.mPositionDeltaInPreLayout);
            GridLayoutManager gridLayoutManager2 = GridLayoutManager.this;
            if ((gridLayoutManager2.mFlag & 3) == 1) {
                gridLayoutManager2.detachAndScrapView(findViewByPosition, gridLayoutManager2.mRecycler);
            } else {
                gridLayoutManager2.removeAndRecycleView(findViewByPosition, gridLayoutManager2.mRecycler);
            }
        }

        @Override // androidx.leanback.widget.Grid.Provider
        public int getEdge(int i) {
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            View findViewByPosition = gridLayoutManager.findViewByPosition(i - gridLayoutManager.mPositionDeltaInPreLayout);
            GridLayoutManager gridLayoutManager2 = GridLayoutManager.this;
            return (gridLayoutManager2.mFlag & 262144) != 0 ? gridLayoutManager2.getViewMax(findViewByPosition) : gridLayoutManager2.getViewMin(findViewByPosition);
        }

        @Override // androidx.leanback.widget.Grid.Provider
        public int getSize(int i) {
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            return gridLayoutManager.getViewPrimarySize(gridLayoutManager.findViewByPosition(i - gridLayoutManager.mPositionDeltaInPreLayout));
        }
    };
    int mChildVisibility = -1;

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean requestChildRectangleOnScreen(RecyclerView recyclerView, View view, Rect rect, boolean z) {
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class LayoutParams extends RecyclerView.LayoutParams {
        private int[] mAlignMultiple;
        private int mAlignX;
        private int mAlignY;
        private ItemAlignmentFacet mAlignmentFacet;
        int mBottomInset;
        int mLeftInset;
        int mRightInset;
        int mTopInset;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(RecyclerView.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((RecyclerView.LayoutParams) layoutParams);
        }

        int getAlignX() {
            return this.mAlignX;
        }

        int getAlignY() {
            return this.mAlignY;
        }

        int getOpticalLeft(View view) {
            return view.getLeft() + this.mLeftInset;
        }

        int getOpticalTop(View view) {
            return view.getTop() + this.mTopInset;
        }

        int getOpticalRight(View view) {
            return view.getRight() - this.mRightInset;
        }

        /* access modifiers changed from: package-private */
        public int getOpticalWidth(View view) {
            return (view.getWidth() - this.mLeftInset) - this.mRightInset;
        }

        /* access modifiers changed from: package-private */
        public int getOpticalHeight(View view) {
            return (view.getHeight() - this.mTopInset) - this.mBottomInset;
        }

        /* access modifiers changed from: package-private */
        public int getOpticalLeftInset() {
            return this.mLeftInset;
        }

        /* access modifiers changed from: package-private */
        public int getOpticalRightInset() {
            return this.mRightInset;
        }

        /* access modifiers changed from: package-private */
        public int getOpticalTopInset() {
            return this.mTopInset;
        }

        void setAlignX(int i) {
            this.mAlignX = i;
        }

        void setAlignY(int i) {
            this.mAlignY = i;
        }

        void setItemAlignmentFacet(ItemAlignmentFacet itemAlignmentFacet) {
            this.mAlignmentFacet = itemAlignmentFacet;
        }

        ItemAlignmentFacet getItemAlignmentFacet() {
            return this.mAlignmentFacet;
        }

        void calculateItemAlignments(int i, View view) {
            ItemAlignmentFacet.ItemAlignmentDef[] alignmentDefs = this.mAlignmentFacet.getAlignmentDefs();
            int[] iArr = this.mAlignMultiple;
            if (iArr == null || iArr.length != alignmentDefs.length) {
                this.mAlignMultiple = new int[alignmentDefs.length];
            }
            for (int i2 = 0; i2 < alignmentDefs.length; i2++) {
                this.mAlignMultiple[i2] = ItemAlignmentFacetHelper.getAlignmentPosition(view, alignmentDefs[i2], i);
            }
            if (i == 0) {
                this.mAlignX = this.mAlignMultiple[0];
            } else {
                this.mAlignY = this.mAlignMultiple[0];
            }
        }

        int[] getAlignMultiple() {
            return this.mAlignMultiple;
        }

        void setOpticalInsets(int i, int i2, int i3, int i4) {
            this.mLeftInset = i;
            this.mTopInset = i2;
            this.mRightInset = i3;
            this.mBottomInset = i4;
        }
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public abstract class GridLinearSmoothScroller extends LinearSmoothScroller {
        boolean mSkipOnStopInternal;

        GridLinearSmoothScroller() {
            super(GridLayoutManager.this.mBaseGridView.getContext());
        }

        /* access modifiers changed from: protected */
        @Override // androidx.recyclerview.widget.LinearSmoothScroller, androidx.recyclerview.widget.RecyclerView.SmoothScroller
        public void onStop() {
            super.onStop();
            if (!this.mSkipOnStopInternal) {
                onStopInternal();
            }
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            if (gridLayoutManager.mCurrentSmoothScroller == this) {
                gridLayoutManager.mCurrentSmoothScroller = null;
            }
            if (gridLayoutManager.mPendingMoveSmoothScroller == this) {
                gridLayoutManager.mPendingMoveSmoothScroller = null;
            }
        }

        protected void onStopInternal() {
            View findViewByPosition = findViewByPosition(getTargetPosition());
            if (findViewByPosition != null) {
                if (GridLayoutManager.this.mFocusPosition != getTargetPosition()) {
                    GridLayoutManager.this.mFocusPosition = getTargetPosition();
                }
                if (GridLayoutManager.this.hasFocus()) {
                    GridLayoutManager.this.mFlag |= 32;
                    findViewByPosition.requestFocus();
                    GridLayoutManager.this.mFlag &= -33;
                }
                GridLayoutManager.this.dispatchChildSelected();
                GridLayoutManager.this.dispatchChildSelectedAndPositioned();
            } else if (getTargetPosition() >= 0) {
                GridLayoutManager.this.scrollToSelection(getTargetPosition(), 0, false, 0);
            }
        }

        /* access modifiers changed from: protected */
        @Override // androidx.recyclerview.widget.LinearSmoothScroller
        public float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return super.calculateSpeedPerPixel(displayMetrics) * GridLayoutManager.this.mSmoothScrollSpeedFactor;
        }

        /* access modifiers changed from: protected */
        @Override // androidx.recyclerview.widget.LinearSmoothScroller
        public int calculateTimeForScrolling(int i) {
            int calculateTimeForScrolling = super.calculateTimeForScrolling(i);
            if (GridLayoutManager.this.mWindowAlignment.mainAxis().getSize() <= 0) {
                return calculateTimeForScrolling;
            }
            float size = (30.0f / ((float) GridLayoutManager.this.mWindowAlignment.mainAxis().getSize())) * ((float) i);
            return ((float) calculateTimeForScrolling) < size ? (int) size : calculateTimeForScrolling;
        }

        @Override // androidx.recyclerview.widget.LinearSmoothScroller, androidx.recyclerview.widget.RecyclerView.SmoothScroller
        protected void onTargetFound(View view, RecyclerView.State state, RecyclerView.SmoothScroller.Action action) {
            int i;
            int i2;
            if (GridLayoutManager.this.getScrollPosition(view, null, GridLayoutManager.sTwoInts)) {
                if (GridLayoutManager.this.mOrientation == 0) {
                    int[] iArr = GridLayoutManager.sTwoInts;
                    i = iArr[0];
                    i2 = iArr[1];
                } else {
                    int[] iArr2 = GridLayoutManager.sTwoInts;
                    int i3 = iArr2[1];
                    i2 = iArr2[0];
                    i = i3;
                }
                action.update(i, i2, calculateTimeForDeceleration((int) Math.sqrt((double) ((i * i) + (i2 * i2)))), this.mDecelerateInterpolator);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class PendingMoveSmoothScroller extends GridLinearSmoothScroller {
        private int mPendingMoves;
        private final boolean mStaggeredGrid;

        PendingMoveSmoothScroller(int i, boolean z) {
            super();
            this.mPendingMoves = i;
            this.mStaggeredGrid = z;
            setTargetPosition(-2);
        }

        void increasePendingMoves() {
            int i = this.mPendingMoves;
            if (i < GridLayoutManager.this.mMaxPendingMoves) {
                this.mPendingMoves = i + 1;
            }
        }

        void decreasePendingMoves() {
            int i = this.mPendingMoves;
            if (i > (-GridLayoutManager.this.mMaxPendingMoves)) {
                this.mPendingMoves = i - 1;
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:14:0x001f  */
        /* JADX WARNING: Removed duplicated region for block: B:28:0x0054  */
        /* JADX WARNING: Removed duplicated region for block: B:33:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void consumePendingMovesBeforeLayout() {
            /*
                r4 = this;
                boolean r0 = r4.mStaggeredGrid
                if (r0 != 0) goto L_0x006f
                int r0 = r4.mPendingMoves
                if (r0 != 0) goto L_0x0009
                goto L_0x006f
            L_0x0009:
                r1 = 0
                if (r0 <= 0) goto L_0x0014
                androidx.leanback.widget.GridLayoutManager r0 = androidx.leanback.widget.GridLayoutManager.this
                int r2 = r0.mFocusPosition
                int r0 = r0.mNumRows
            L_0x0012:
                int r2 = r2 + r0
                goto L_0x001b
            L_0x0014:
                androidx.leanback.widget.GridLayoutManager r0 = androidx.leanback.widget.GridLayoutManager.this
                int r2 = r0.mFocusPosition
                int r0 = r0.mNumRows
            L_0x001a:
                int r2 = r2 - r0
            L_0x001b:
                int r0 = r4.mPendingMoves
                if (r0 == 0) goto L_0x0052
                android.view.View r0 = r4.findViewByPosition(r2)
                if (r0 != 0) goto L_0x0026
                goto L_0x0052
            L_0x0026:
                androidx.leanback.widget.GridLayoutManager r3 = androidx.leanback.widget.GridLayoutManager.this
                boolean r3 = r3.canScrollTo(r0)
                if (r3 != 0) goto L_0x002f
                goto L_0x0044
            L_0x002f:
                androidx.leanback.widget.GridLayoutManager r1 = androidx.leanback.widget.GridLayoutManager.this
                r1.mFocusPosition = r2
                r3 = 0
                r1.mSubFocusPosition = r3
                int r1 = r4.mPendingMoves
                if (r1 <= 0) goto L_0x003f
                int r1 = r1 + -1
                r4.mPendingMoves = r1
                goto L_0x0043
            L_0x003f:
                int r1 = r1 + 1
                r4.mPendingMoves = r1
            L_0x0043:
                r1 = r0
            L_0x0044:
                int r0 = r4.mPendingMoves
                if (r0 <= 0) goto L_0x004d
                androidx.leanback.widget.GridLayoutManager r0 = androidx.leanback.widget.GridLayoutManager.this
                int r0 = r0.mNumRows
                goto L_0x0012
            L_0x004d:
                androidx.leanback.widget.GridLayoutManager r0 = androidx.leanback.widget.GridLayoutManager.this
                int r0 = r0.mNumRows
                goto L_0x001a
            L_0x0052:
                if (r1 == 0) goto L_0x006f
                androidx.leanback.widget.GridLayoutManager r0 = androidx.leanback.widget.GridLayoutManager.this
                boolean r0 = r0.hasFocus()
                if (r0 == 0) goto L_0x006f
                androidx.leanback.widget.GridLayoutManager r0 = androidx.leanback.widget.GridLayoutManager.this
                int r2 = r0.mFlag
                r2 = r2 | 32
                r0.mFlag = r2
                r1.requestFocus()
                androidx.leanback.widget.GridLayoutManager r4 = androidx.leanback.widget.GridLayoutManager.this
                int r0 = r4.mFlag
                r0 = r0 & -33
                r4.mFlag = r0
            L_0x006f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.PendingMoveSmoothScroller.consumePendingMovesBeforeLayout():void");
        }

        void consumePendingMovesAfterLayout() {
            int i;
            if (this.mStaggeredGrid && (i = this.mPendingMoves) != 0) {
                this.mPendingMoves = GridLayoutManager.this.processSelectionMoves(true, i);
            }
            int i2 = this.mPendingMoves;
            if (i2 == 0 || ((i2 > 0 && GridLayoutManager.this.hasCreatedLastItem()) || (this.mPendingMoves < 0 && GridLayoutManager.this.hasCreatedFirstItem()))) {
                setTargetPosition(GridLayoutManager.this.mFocusPosition);
                stop();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller
        public PointF computeScrollVectorForPosition(int i) {
            int i2 = this.mPendingMoves;
            if (i2 == 0) {
                return null;
            }
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            int i3 = ((gridLayoutManager.mFlag & 262144) == 0 ? i2 >= 0 : i2 <= 0) ? 1 : -1;
            if (gridLayoutManager.mOrientation == 0) {
                return new PointF((float) i3, 0.0f);
            }
            return new PointF(0.0f, (float) i3);
        }

        @Override // androidx.leanback.widget.GridLayoutManager.GridLinearSmoothScroller
        protected void onStopInternal() {
            super.onStopInternal();
            this.mPendingMoves = 0;
            View findViewByPosition = findViewByPosition(getTargetPosition());
            if (findViewByPosition != null) {
                GridLayoutManager.this.scrollToView(findViewByPosition, true);
            }
        }
    }

    String getTag() {
        return "GridLayoutManager:" + this.mBaseGridView.getId();
    }

    public GridLayoutManager(BaseGridView baseGridView) {
        this.mBaseGridView = baseGridView;
        setItemPrefetchEnabled(false);
    }

    public void setOrientation(int i) {
        if (i == 0 || i == 1) {
            this.mOrientation = i;
            this.mOrientationHelper = OrientationHelper.createOrientationHelper(this, i);
            this.mWindowAlignment.setOrientation(i);
            this.mItemAlignment.setOrientation(i);
            this.mFlag |= 256;
        }
    }

    public void onRtlPropertiesChanged(int i) {
        int i2;
        boolean z = false;
        if (this.mOrientation == 0) {
            if (i == 1) {
                i2 = 262144;
            }
            i2 = 0;
        } else {
            if (i == 1) {
                i2 = 524288;
            }
            i2 = 0;
        }
        int i3 = this.mFlag;
        if ((786432 & i3) != i2) {
            int i4 = i2 | (i3 & -786433);
            this.mFlag = i4;
            this.mFlag = i4 | 256;
            WindowAlignment.Axis axis = this.mWindowAlignment.horizontal;
            if (i == 1) {
                z = true;
            }
            axis.setReversedFlow(z);
        }
    }

    public void setWindowAlignment(int i) {
        this.mWindowAlignment.mainAxis().setWindowAlignment(i);
    }

    public void setFocusOutAllowed(boolean z, boolean z2) {
        int i = 0;
        int i2 = (z ? 2048 : 0) | (this.mFlag & -6145);
        if (z2) {
            i = 4096;
        }
        this.mFlag = i2 | i;
    }

    public void setFocusOutSideAllowed(boolean z, boolean z2) {
        int i = 0;
        int i2 = (z ? 8192 : 0) | (this.mFlag & -24577);
        if (z2) {
            i = 16384;
        }
        this.mFlag = i2 | i;
    }

    public void setNumRows(int i) {
        if (i >= 0) {
            this.mNumRowsRequested = i;
            return;
        }
        throw new IllegalArgumentException();
    }

    public void setRowHeight(int i) {
        if (i >= 0 || i == -2) {
            this.mRowSizeSecondaryRequested = i;
            return;
        }
        throw new IllegalArgumentException("Invalid row height: " + i);
    }

    public void setVerticalSpacing(int i) {
        if (this.mOrientation == 1) {
            this.mVerticalSpacing = i;
            this.mSpacingPrimary = i;
            return;
        }
        this.mVerticalSpacing = i;
        this.mSpacingSecondary = i;
    }

    public void setHorizontalSpacing(int i) {
        if (this.mOrientation == 0) {
            this.mHorizontalSpacing = i;
            this.mSpacingPrimary = i;
            return;
        }
        this.mHorizontalSpacing = i;
        this.mSpacingSecondary = i;
    }

    public int getVerticalSpacing() {
        return this.mVerticalSpacing;
    }

    public void setGravity(int i) {
        this.mGravity = i;
    }

    protected boolean hasDoneFirstLayout() {
        return this.mGrid != null;
    }

    public void setOnChildViewHolderSelectedListener(OnChildViewHolderSelectedListener onChildViewHolderSelectedListener) {
        if (onChildViewHolderSelectedListener == null) {
            this.mChildViewHolderSelectedListeners = null;
            return;
        }
        ArrayList<OnChildViewHolderSelectedListener> arrayList = this.mChildViewHolderSelectedListeners;
        if (arrayList == null) {
            this.mChildViewHolderSelectedListeners = new ArrayList<>();
        } else {
            arrayList.clear();
        }
        this.mChildViewHolderSelectedListeners.add(onChildViewHolderSelectedListener);
    }

    boolean hasOnChildViewHolderSelectedListener() {
        ArrayList<OnChildViewHolderSelectedListener> arrayList = this.mChildViewHolderSelectedListeners;
        return arrayList != null && arrayList.size() > 0;
    }

    void fireOnChildViewHolderSelected(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int i, int i2) {
        ArrayList<OnChildViewHolderSelectedListener> arrayList = this.mChildViewHolderSelectedListeners;
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                this.mChildViewHolderSelectedListeners.get(size).onChildViewHolderSelected(recyclerView, viewHolder, i, i2);
            }
        }
    }

    void fireOnChildViewHolderSelectedAndPositioned(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int i, int i2) {
        ArrayList<OnChildViewHolderSelectedListener> arrayList = this.mChildViewHolderSelectedListeners;
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                this.mChildViewHolderSelectedListeners.get(size).onChildViewHolderSelectedAndPositioned(recyclerView, viewHolder, i, i2);
            }
        }
    }

    private int getAdapterPositionByView(View view) {
        LayoutParams layoutParams;
        if (view == null || (layoutParams = (LayoutParams) view.getLayoutParams()) == null || layoutParams.isItemRemoved()) {
            return -1;
        }
        return layoutParams.getAbsoluteAdapterPosition();
    }

    int getSubPositionByView(View view, View view2) {
        ItemAlignmentFacet itemAlignmentFacet;
        if (!(view == null || view2 == null || (itemAlignmentFacet = ((LayoutParams) view.getLayoutParams()).getItemAlignmentFacet()) == null)) {
            ItemAlignmentFacet.ItemAlignmentDef[] alignmentDefs = itemAlignmentFacet.getAlignmentDefs();
            if (alignmentDefs.length > 1) {
                while (view2 != view) {
                    int id = view2.getId();
                    if (id != -1) {
                        for (int i = 1; i < alignmentDefs.length; i++) {
                            if (alignmentDefs[i].getItemAlignmentFocusViewId() == id) {
                                return i;
                            }
                        }
                        continue;
                    }
                    view2 = (View) view2.getParent();
                }
            }
        }
        return 0;
    }

    private int getAdapterPositionByIndex(int i) {
        return getAdapterPositionByView(getChildAt(i));
    }

    void dispatchChildSelected() {
        long j;
        if (this.mChildSelectedListener != null || hasOnChildViewHolderSelectedListener()) {
            int i = this.mFocusPosition;
            View findViewByPosition = i == -1 ? null : findViewByPosition(i);
            if (findViewByPosition != null) {
                RecyclerView.ViewHolder childViewHolder = this.mBaseGridView.getChildViewHolder(findViewByPosition);
                OnChildSelectedListener onChildSelectedListener = this.mChildSelectedListener;
                if (onChildSelectedListener != null) {
                    BaseGridView baseGridView = this.mBaseGridView;
                    int i2 = this.mFocusPosition;
                    if (childViewHolder == null) {
                        j = -1;
                    } else {
                        j = childViewHolder.getItemId();
                    }
                    onChildSelectedListener.onChildSelected(baseGridView, findViewByPosition, i2, j);
                }
                fireOnChildViewHolderSelected(this.mBaseGridView, childViewHolder, this.mFocusPosition, this.mSubFocusPosition);
            } else {
                OnChildSelectedListener onChildSelectedListener2 = this.mChildSelectedListener;
                if (onChildSelectedListener2 != null) {
                    onChildSelectedListener2.onChildSelected(this.mBaseGridView, null, -1, -1);
                }
                fireOnChildViewHolderSelected(this.mBaseGridView, null, -1, 0);
            }
            if (!((this.mFlag & 3) == 1 || this.mBaseGridView.isLayoutRequested())) {
                int childCount = getChildCount();
                for (int i3 = 0; i3 < childCount; i3++) {
                    if (getChildAt(i3).isLayoutRequested()) {
                        forceRequestLayout();
                        return;
                    }
                }
            }
        }
    }

    void dispatchChildSelectedAndPositioned() {
        if (hasOnChildViewHolderSelectedListener()) {
            int i = this.mFocusPosition;
            View findViewByPosition = i == -1 ? null : findViewByPosition(i);
            if (findViewByPosition != null) {
                fireOnChildViewHolderSelectedAndPositioned(this.mBaseGridView, this.mBaseGridView.getChildViewHolder(findViewByPosition), this.mFocusPosition, this.mSubFocusPosition);
                return;
            }
            OnChildSelectedListener onChildSelectedListener = this.mChildSelectedListener;
            if (onChildSelectedListener != null) {
                onChildSelectedListener.onChildSelected(this.mBaseGridView, null, -1, -1);
            }
            fireOnChildViewHolderSelectedAndPositioned(this.mBaseGridView, null, -1, 0);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean canScrollHorizontally() {
        return this.mOrientation == 0 || this.mNumRows > 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean canScrollVertically() {
        return this.mOrientation == 1 || this.mNumRows > 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public RecyclerView.LayoutParams generateLayoutParams(Context context, AttributeSet attributeSet) {
        return new LayoutParams(context, attributeSet);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) layoutParams);
        }
        if (layoutParams instanceof RecyclerView.LayoutParams) {
            return new LayoutParams((RecyclerView.LayoutParams) layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    protected View getViewForPosition(int i) {
        View viewForPosition = this.mRecycler.getViewForPosition(i);
        ((LayoutParams) viewForPosition.getLayoutParams()).setItemAlignmentFacet((ItemAlignmentFacet) getFacet(this.mBaseGridView.getChildViewHolder(viewForPosition), ItemAlignmentFacet.class));
        return viewForPosition;
    }

    /* access modifiers changed from: package-private */
    public final int getOpticalLeft(View view) {
        return ((LayoutParams) view.getLayoutParams()).getOpticalLeft(view);
    }

    /* access modifiers changed from: package-private */
    public final int getOpticalRight(View view) {
        return ((LayoutParams) view.getLayoutParams()).getOpticalRight(view);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getDecoratedLeft(View view) {
        return super.getDecoratedLeft(view) + ((LayoutParams) view.getLayoutParams()).mLeftInset;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getDecoratedTop(View view) {
        return super.getDecoratedTop(view) + ((LayoutParams) view.getLayoutParams()).mTopInset;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getDecoratedRight(View view) {
        return super.getDecoratedRight(view) - ((LayoutParams) view.getLayoutParams()).mRightInset;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getDecoratedBottom(View view) {
        return super.getDecoratedBottom(view) - ((LayoutParams) view.getLayoutParams()).mBottomInset;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void getDecoratedBoundsWithMargins(View view, Rect rect) {
        super.getDecoratedBoundsWithMargins(view, rect);
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        rect.left += layoutParams.mLeftInset;
        rect.top += layoutParams.mTopInset;
        rect.right -= layoutParams.mRightInset;
        rect.bottom -= layoutParams.mBottomInset;
    }

    int getViewMin(View view) {
        return this.mOrientationHelper.getDecoratedStart(view);
    }

    int getViewMax(View view) {
        return this.mOrientationHelper.getDecoratedEnd(view);
    }

    int getViewPrimarySize(View view) {
        Rect rect = sTempRect;
        getDecoratedBoundsWithMargins(view, rect);
        return this.mOrientation == 0 ? rect.width() : rect.height();
    }

    private int getViewCenter(View view) {
        return this.mOrientation == 0 ? getViewCenterX(view) : getViewCenterY(view);
    }

    private int getViewCenterSecondary(View view) {
        return this.mOrientation == 0 ? getViewCenterY(view) : getViewCenterX(view);
    }

    private int getViewCenterX(View view) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        return layoutParams.getOpticalLeft(view) + layoutParams.getAlignX();
    }

    private int getViewCenterY(View view) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        return layoutParams.getOpticalTop(view) + layoutParams.getAlignY();
    }

    private void saveContext(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int i = this.mSaveContextLevel;
        if (i == 0) {
            this.mRecycler = recycler;
            this.mState = state;
            this.mPositionDeltaInPreLayout = 0;
            this.mExtraLayoutSpaceInPreLayout = 0;
        }
        this.mSaveContextLevel = i + 1;
    }

    private void leaveContext() {
        int i = this.mSaveContextLevel - 1;
        this.mSaveContextLevel = i;
        if (i == 0) {
            this.mRecycler = null;
            this.mState = null;
            this.mPositionDeltaInPreLayout = 0;
            this.mExtraLayoutSpaceInPreLayout = 0;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0074, code lost:
        if (((r5.mFlag & 262144) != 0) != r5.mGrid.isReversedFlow()) goto L_0x0076;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean layoutInit() {
        /*
            r5 = this;
            androidx.recyclerview.widget.RecyclerView$State r0 = r5.mState
            int r0 = r0.getItemCount()
            r1 = -1
            r2 = 1
            r3 = 0
            if (r0 != 0) goto L_0x0010
            r5.mFocusPosition = r1
            r5.mSubFocusPosition = r3
            goto L_0x0022
        L_0x0010:
            int r4 = r5.mFocusPosition
            if (r4 < r0) goto L_0x001a
            int r0 = r0 - r2
            r5.mFocusPosition = r0
            r5.mSubFocusPosition = r3
            goto L_0x0022
        L_0x001a:
            if (r4 != r1) goto L_0x0022
            if (r0 <= 0) goto L_0x0022
            r5.mFocusPosition = r3
            r5.mSubFocusPosition = r3
        L_0x0022:
            androidx.recyclerview.widget.RecyclerView$State r0 = r5.mState
            boolean r0 = r0.didStructureChange()
            if (r0 != 0) goto L_0x0052
            androidx.leanback.widget.Grid r0 = r5.mGrid
            if (r0 == 0) goto L_0x0052
            int r0 = r0.getFirstVisibleIndex()
            if (r0 < 0) goto L_0x0052
            int r0 = r5.mFlag
            r0 = r0 & 256(0x100, float:3.59E-43)
            if (r0 != 0) goto L_0x0052
            androidx.leanback.widget.Grid r0 = r5.mGrid
            int r0 = r0.getNumRows()
            int r1 = r5.mNumRows
            if (r0 != r1) goto L_0x0052
            r5.updateScrollController()
            r5.updateSecondaryScrollLimits()
            androidx.leanback.widget.Grid r0 = r5.mGrid
            int r5 = r5.mSpacingPrimary
            r0.setSpacing(r5)
            return r2
        L_0x0052:
            int r0 = r5.mFlag
            r0 = r0 & -257(0xfffffffffffffeff, float:NaN)
            r5.mFlag = r0
            androidx.leanback.widget.Grid r0 = r5.mGrid
            r1 = 262144(0x40000, float:3.67342E-40)
            if (r0 == 0) goto L_0x0076
            int r4 = r5.mNumRows
            int r0 = r0.getNumRows()
            if (r4 != r0) goto L_0x0076
            int r0 = r5.mFlag
            r0 = r0 & r1
            if (r0 == 0) goto L_0x006d
            r0 = r2
            goto L_0x006e
        L_0x006d:
            r0 = r3
        L_0x006e:
            androidx.leanback.widget.Grid r4 = r5.mGrid
            boolean r4 = r4.isReversedFlow()
            if (r0 == r4) goto L_0x008f
        L_0x0076:
            int r0 = r5.mNumRows
            androidx.leanback.widget.Grid r0 = androidx.leanback.widget.Grid.createGrid(r0)
            r5.mGrid = r0
            androidx.leanback.widget.Grid$Provider r4 = r5.mGridProvider
            r0.setProvider(r4)
            androidx.leanback.widget.Grid r0 = r5.mGrid
            int r4 = r5.mFlag
            r1 = r1 & r4
            if (r1 == 0) goto L_0x008b
            goto L_0x008c
        L_0x008b:
            r2 = r3
        L_0x008c:
            r0.setReversedFlow(r2)
        L_0x008f:
            r5.initScrollController()
            r5.updateSecondaryScrollLimits()
            androidx.leanback.widget.Grid r0 = r5.mGrid
            int r1 = r5.mSpacingPrimary
            r0.setSpacing(r1)
            androidx.recyclerview.widget.RecyclerView$Recycler r0 = r5.mRecycler
            r5.detachAndScrapAttachedViews(r0)
            androidx.leanback.widget.Grid r0 = r5.mGrid
            r0.resetVisibleIndex()
            androidx.leanback.widget.WindowAlignment r0 = r5.mWindowAlignment
            androidx.leanback.widget.WindowAlignment$Axis r0 = r0.mainAxis()
            r0.invalidateScrollMin()
            androidx.leanback.widget.WindowAlignment r5 = r5.mWindowAlignment
            androidx.leanback.widget.WindowAlignment$Axis r5 = r5.mainAxis()
            r5.invalidateScrollMax()
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.layoutInit():boolean");
    }

    private int getRowSizeSecondary(int i) {
        int i2 = this.mFixedRowSizeSecondary;
        if (i2 != 0) {
            return i2;
        }
        int[] iArr = this.mRowSizeSecondary;
        if (iArr == null) {
            return 0;
        }
        return iArr[i];
    }

    int getRowStartSecondary(int i) {
        int i2 = 0;
        if ((this.mFlag & 524288) != 0) {
            for (int i3 = this.mNumRows - 1; i3 > i; i3--) {
                i2 += getRowSizeSecondary(i3) + this.mSpacingSecondary;
            }
            return i2;
        }
        int i4 = 0;
        while (i2 < i) {
            i4 += getRowSizeSecondary(i2) + this.mSpacingSecondary;
            i2++;
        }
        return i4;
    }

    private int getSizeSecondary() {
        int i = (this.mFlag & 524288) != 0 ? 0 : this.mNumRows - 1;
        return getRowStartSecondary(i) + getRowSizeSecondary(i);
    }

    int getDecoratedMeasuredWidthWithMargin(View view) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        return getDecoratedMeasuredWidth(view) + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
    }

    int getDecoratedMeasuredHeightWithMargin(View view) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
    }

    private void measureScrapChild(int i, int i2, int i3, int[] iArr) {
        View viewForPosition = this.mRecycler.getViewForPosition(i);
        if (viewForPosition != null) {
            LayoutParams layoutParams = (LayoutParams) viewForPosition.getLayoutParams();
            Rect rect = sTempRect;
            calculateItemDecorationsForChild(viewForPosition, rect);
            viewForPosition.measure(ViewGroup.getChildMeasureSpec(i2, getPaddingLeft() + getPaddingRight() + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + rect.left + rect.right, ((ViewGroup.MarginLayoutParams) layoutParams).width), ViewGroup.getChildMeasureSpec(i3, getPaddingTop() + getPaddingBottom() + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + rect.top + rect.bottom, ((ViewGroup.MarginLayoutParams) layoutParams).height));
            iArr[0] = getDecoratedMeasuredWidthWithMargin(viewForPosition);
            iArr[1] = getDecoratedMeasuredHeightWithMargin(viewForPosition);
            this.mRecycler.recycleView(viewForPosition);
        }
    }

    private boolean processRowSizeSecondary(boolean z) {
        CircularIntArray circularIntArray;
        int i;
        int i2;
        if (this.mFixedRowSizeSecondary != 0 || this.mRowSizeSecondary == null) {
            return false;
        }
        Grid grid = this.mGrid;
        CircularIntArray[] itemPositionsInRows = grid == null ? null : grid.getItemPositionsInRows();
        boolean z2 = false;
        int i3 = -1;
        for (int i4 = 0; i4 < this.mNumRows; i4++) {
            if (itemPositionsInRows == null) {
                circularIntArray = null;
            } else {
                circularIntArray = itemPositionsInRows[i4];
            }
            if (circularIntArray == null) {
                i = 0;
            } else {
                i = circularIntArray.size();
            }
            int i5 = -1;
            for (int i6 = 0; i6 < i; i6 += 2) {
                int i7 = circularIntArray.get(i6 + 1);
                for (int i8 = circularIntArray.get(i6); i8 <= i7; i8++) {
                    View findViewByPosition = findViewByPosition(i8 - this.mPositionDeltaInPreLayout);
                    if (findViewByPosition != null) {
                        if (z) {
                            measureChild(findViewByPosition);
                        }
                        if (this.mOrientation == 0) {
                            i2 = getDecoratedMeasuredHeightWithMargin(findViewByPosition);
                        } else {
                            i2 = getDecoratedMeasuredWidthWithMargin(findViewByPosition);
                        }
                        if (i2 > i5) {
                            i5 = i2;
                        }
                    }
                }
            }
            int itemCount = this.mState.getItemCount();
            if (!this.mBaseGridView.hasFixedSize() && z && i5 < 0 && itemCount > 0) {
                if (i3 < 0) {
                    int i9 = this.mFocusPosition;
                    if (i9 < 0) {
                        i9 = 0;
                    } else if (i9 >= itemCount) {
                        i9 = itemCount - 1;
                    }
                    if (getChildCount() > 0) {
                        int layoutPosition = this.mBaseGridView.getChildViewHolder(getChildAt(0)).getLayoutPosition();
                        int layoutPosition2 = this.mBaseGridView.getChildViewHolder(getChildAt(getChildCount() - 1)).getLayoutPosition();
                        if (i9 >= layoutPosition && i9 <= layoutPosition2) {
                            i9 = i9 - layoutPosition <= layoutPosition2 - i9 ? layoutPosition - 1 : layoutPosition2 + 1;
                            if (i9 < 0 && layoutPosition2 < itemCount - 1) {
                                i9 = layoutPosition2 + 1;
                            } else if (i9 >= itemCount && layoutPosition > 0) {
                                i9 = layoutPosition - 1;
                            }
                        }
                    }
                    if (i9 >= 0 && i9 < itemCount) {
                        measureScrapChild(i9, View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0), this.mMeasuredDimension);
                        if (this.mOrientation == 0) {
                            i3 = this.mMeasuredDimension[1];
                        } else {
                            i3 = this.mMeasuredDimension[0];
                        }
                    }
                }
                if (i3 >= 0) {
                    i5 = i3;
                }
            }
            if (i5 < 0) {
                i5 = 0;
            }
            int[] iArr = this.mRowSizeSecondary;
            if (iArr[i4] != i5) {
                iArr[i4] = i5;
                z2 = true;
            }
        }
        return z2;
    }

    private void updateRowSecondarySizeRefresh() {
        int i = this.mFlag & -1025;
        int i2 = 0;
        if (processRowSizeSecondary(false)) {
            i2 = 1024;
        }
        int i3 = i | i2;
        this.mFlag = i3;
        if ((i3 & 1024) != 0) {
            forceRequestLayout();
        }
    }

    private void forceRequestLayout() {
        ViewCompat.postOnAnimation(this.mBaseGridView, this.mRequestLayoutRunnable);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        saveContext(recycler, state);
        if (this.mOrientation == 0) {
            i6 = View.MeasureSpec.getSize(i);
            i5 = View.MeasureSpec.getSize(i2);
            i4 = View.MeasureSpec.getMode(i2);
            i3 = getPaddingTop();
            i7 = getPaddingBottom();
        } else {
            i5 = View.MeasureSpec.getSize(i);
            i6 = View.MeasureSpec.getSize(i2);
            i4 = View.MeasureSpec.getMode(i);
            i3 = getPaddingLeft();
            i7 = getPaddingRight();
        }
        int i8 = i3 + i7;
        this.mMaxSizeSecondary = i5;
        int i9 = this.mRowSizeSecondaryRequested;
        if (i9 == -2) {
            int i10 = this.mNumRowsRequested;
            if (i10 == 0) {
                i10 = 1;
            }
            this.mNumRows = i10;
            this.mFixedRowSizeSecondary = 0;
            int[] iArr = this.mRowSizeSecondary;
            if (iArr == null || iArr.length != i10) {
                this.mRowSizeSecondary = new int[i10];
            }
            if (this.mState.isPreLayout()) {
                updatePositionDeltaInPreLayout();
            }
            processRowSizeSecondary(true);
            if (i4 == Integer.MIN_VALUE) {
                i5 = Math.min(getSizeSecondary() + i8, this.mMaxSizeSecondary);
            } else if (i4 == 0) {
                i5 = getSizeSecondary() + i8;
            } else if (i4 == 1073741824) {
                i5 = this.mMaxSizeSecondary;
            } else {
                throw new IllegalStateException("wrong spec");
            }
        } else {
            if (i4 != Integer.MIN_VALUE) {
                if (i4 == 0) {
                    if (i9 == 0) {
                        i9 = i5 - i8;
                    }
                    this.mFixedRowSizeSecondary = i9;
                    int i11 = this.mNumRowsRequested;
                    if (i11 == 0) {
                        i11 = 1;
                    }
                    this.mNumRows = i11;
                    i5 = (i9 * i11) + (this.mSpacingSecondary * (i11 - 1)) + i8;
                } else if (i4 != 1073741824) {
                    throw new IllegalStateException("wrong spec");
                }
            }
            int i12 = this.mNumRowsRequested;
            if (i12 == 0 && i9 == 0) {
                this.mNumRows = 1;
                this.mFixedRowSizeSecondary = i5 - i8;
            } else if (i12 == 0) {
                this.mFixedRowSizeSecondary = i9;
                int i13 = this.mSpacingSecondary;
                this.mNumRows = (i5 + i13) / (i9 + i13);
            } else if (i9 == 0) {
                this.mNumRows = i12;
                this.mFixedRowSizeSecondary = ((i5 - i8) - (this.mSpacingSecondary * (i12 - 1))) / i12;
            } else {
                this.mNumRows = i12;
                this.mFixedRowSizeSecondary = i9;
            }
            if (i4 == Integer.MIN_VALUE) {
                int i14 = this.mFixedRowSizeSecondary;
                int i15 = this.mNumRows;
                int i16 = (i14 * i15) + (this.mSpacingSecondary * (i15 - 1)) + i8;
                if (i16 < i5) {
                    i5 = i16;
                }
            }
        }
        if (this.mOrientation == 0) {
            setMeasuredDimension(i6, i5);
        } else {
            setMeasuredDimension(i5, i6);
        }
        leaveContext();
    }

    void measureChild(View view) {
        int i;
        int i2;
        int i3;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        Rect rect = sTempRect;
        calculateItemDecorationsForChild(view, rect);
        int i4 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + rect.left + rect.right;
        int i5 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + rect.top + rect.bottom;
        if (this.mRowSizeSecondaryRequested == -2) {
            i = View.MeasureSpec.makeMeasureSpec(0, 0);
        } else {
            i = View.MeasureSpec.makeMeasureSpec(this.mFixedRowSizeSecondary, 1073741824);
        }
        if (this.mOrientation == 0) {
            i2 = ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(0, 0), i4, ((ViewGroup.MarginLayoutParams) layoutParams).width);
            i3 = ViewGroup.getChildMeasureSpec(i, i5, ((ViewGroup.MarginLayoutParams) layoutParams).height);
        } else {
            int childMeasureSpec = ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(0, 0), i5, ((ViewGroup.MarginLayoutParams) layoutParams).height);
            i2 = ViewGroup.getChildMeasureSpec(i, i4, ((ViewGroup.MarginLayoutParams) layoutParams).width);
            i3 = childMeasureSpec;
        }
        view.measure(i2, i3);
    }

    <E> E getFacet(RecyclerView.ViewHolder viewHolder, Class<? extends E> cls) {
        FacetProviderAdapter facetProviderAdapter;
        FacetProvider facetProvider;
        E e = viewHolder instanceof FacetProvider ? (E) ((FacetProvider) viewHolder).getFacet(cls) : null;
        return (e != null || (facetProviderAdapter = this.mFacetProviderAdapter) == null || (facetProvider = facetProviderAdapter.getFacetProvider(viewHolder.getItemViewType())) == null) ? e : (E) facetProvider.getFacet(cls);
    }

    void layoutChild(int i, View view, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        if (this.mOrientation == 0) {
            i5 = getDecoratedMeasuredHeightWithMargin(view);
        } else {
            i5 = getDecoratedMeasuredWidthWithMargin(view);
        }
        int i8 = this.mFixedRowSizeSecondary;
        if (i8 > 0) {
            i5 = Math.min(i5, i8);
        }
        int i9 = this.mGravity;
        int i10 = i9 & 112;
        int absoluteGravity = (this.mFlag & 786432) != 0 ? Gravity.getAbsoluteGravity(i9 & 8388615, 1) : i9 & 7;
        int i11 = this.mOrientation;
        if (!((i11 == 0 && i10 == 48) || (i11 == 1 && absoluteGravity == 3))) {
            if ((i11 == 0 && i10 == 80) || (i11 == 1 && absoluteGravity == 5)) {
                i7 = getRowSizeSecondary(i) - i5;
            } else if ((i11 == 0 && i10 == 16) || (i11 == 1 && absoluteGravity == 1)) {
                i7 = (getRowSizeSecondary(i) - i5) / 2;
            }
            i4 += i7;
        }
        if (this.mOrientation == 0) {
            i6 = i5 + i4;
        } else {
            i3 = i5 + i4;
            i4 = i2;
            i2 = i4;
            i6 = i3;
        }
        layoutDecoratedWithMargins(view, i2, i4, i3, i6);
        Rect rect = sTempRect;
        super.getDecoratedBoundsWithMargins(view, rect);
        ((LayoutParams) view.getLayoutParams()).setOpticalInsets(i2 - rect.left, i4 - rect.top, rect.right - i3, rect.bottom - i6);
        updateChildAlignments(view);
    }

    private void updateChildAlignments(View view) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (layoutParams.getItemAlignmentFacet() == null) {
            layoutParams.setAlignX(this.mItemAlignment.horizontal.getAlignmentPosition(view));
            layoutParams.setAlignY(this.mItemAlignment.vertical.getAlignmentPosition(view));
            return;
        }
        layoutParams.calculateItemAlignments(this.mOrientation, view);
        if (this.mOrientation == 0) {
            layoutParams.setAlignY(this.mItemAlignment.vertical.getAlignmentPosition(view));
        } else {
            layoutParams.setAlignX(this.mItemAlignment.horizontal.getAlignmentPosition(view));
        }
    }

    private void removeInvisibleViewsAtEnd() {
        int i;
        int i2 = this.mFlag;
        if ((65600 & i2) == 65536) {
            Grid grid = this.mGrid;
            int i3 = this.mFocusPosition;
            if ((i2 & 262144) != 0) {
                i = -this.mExtraLayoutSpace;
            } else {
                i = this.mExtraLayoutSpace + this.mSizePrimary;
            }
            grid.removeInvisibleItemsAtEnd(i3, i);
        }
    }

    private void removeInvisibleViewsAtFront() {
        int i = this.mFlag;
        if ((65600 & i) == 65536) {
            this.mGrid.removeInvisibleItemsAtFront(this.mFocusPosition, (i & 262144) != 0 ? this.mSizePrimary + this.mExtraLayoutSpace : -this.mExtraLayoutSpace);
        }
    }

    private boolean appendOneColumnVisibleItems() {
        return this.mGrid.appendOneColumnVisibleItems();
    }

    int getSlideOutDistance() {
        int i;
        int i2;
        int right;
        if (this.mOrientation == 1) {
            i2 = -getHeight();
            if (getChildCount() <= 0 || (i = getChildAt(0).getTop()) >= 0) {
                return i2;
            }
        } else if ((this.mFlag & 262144) != 0) {
            int width = getWidth();
            if (getChildCount() <= 0 || (right = getChildAt(0).getRight()) <= width) {
                return width;
            }
            return right;
        } else {
            i2 = -getWidth();
            if (getChildCount() <= 0 || (i = getChildAt(0).getLeft()) >= 0) {
                return i2;
            }
        }
        return i2 + i;
    }

    /* access modifiers changed from: package-private */
    public boolean isSlidingChildViews() {
        return (this.mFlag & 64) != 0;
    }

    private boolean prependOneColumnVisibleItems() {
        return this.mGrid.prependOneColumnVisibleItems();
    }

    private void appendVisibleItems() {
        int i;
        Grid grid = this.mGrid;
        if ((this.mFlag & 262144) != 0) {
            i = (-this.mExtraLayoutSpace) - this.mExtraLayoutSpaceInPreLayout;
        } else {
            i = this.mSizePrimary + this.mExtraLayoutSpace + this.mExtraLayoutSpaceInPreLayout;
        }
        grid.appendVisibleItems(i);
    }

    private void prependVisibleItems() {
        int i;
        Grid grid = this.mGrid;
        if ((this.mFlag & 262144) != 0) {
            i = this.mSizePrimary + this.mExtraLayoutSpace + this.mExtraLayoutSpaceInPreLayout;
        } else {
            i = (-this.mExtraLayoutSpace) - this.mExtraLayoutSpaceInPreLayout;
        }
        grid.prependVisibleItems(i);
    }

    private void fastRelayout() {
        Grid.Location location;
        int i;
        int childCount = getChildCount();
        int firstVisibleIndex = this.mGrid.getFirstVisibleIndex();
        this.mFlag &= -9;
        boolean z = false;
        int i2 = 0;
        while (i2 < childCount) {
            View childAt = getChildAt(i2);
            if (firstVisibleIndex == getAdapterPositionByView(childAt) && (location = this.mGrid.getLocation(firstVisibleIndex)) != null) {
                int rowStartSecondary = (getRowStartSecondary(location.row) + this.mWindowAlignment.secondAxis().getPaddingMin()) - this.mScrollOffsetSecondary;
                int viewMin = getViewMin(childAt);
                int viewPrimarySize = getViewPrimarySize(childAt);
                if (((LayoutParams) childAt.getLayoutParams()).viewNeedsUpdate()) {
                    this.mFlag |= 8;
                    detachAndScrapView(childAt, this.mRecycler);
                    childAt = getViewForPosition(firstVisibleIndex);
                    addView(childAt, i2);
                }
                measureChild(childAt);
                if (this.mOrientation == 0) {
                    i = getDecoratedMeasuredWidthWithMargin(childAt);
                } else {
                    i = getDecoratedMeasuredHeightWithMargin(childAt);
                }
                layoutChild(location.row, childAt, viewMin, viewMin + i, rowStartSecondary);
                if (viewPrimarySize == i) {
                    i2++;
                    firstVisibleIndex++;
                }
            }
            z = true;
        }
        if (z) {
            int lastVisibleIndex = this.mGrid.getLastVisibleIndex();
            for (int i3 = childCount - 1; i3 >= i2; i3--) {
                detachAndScrapView(getChildAt(i3), this.mRecycler);
            }
            this.mGrid.invalidateItemsAfter(firstVisibleIndex);
            if ((this.mFlag & 65536) != 0) {
                appendVisibleItems();
                int i4 = this.mFocusPosition;
                if (i4 >= 0 && i4 <= lastVisibleIndex) {
                    while (this.mGrid.getLastVisibleIndex() < this.mFocusPosition) {
                        this.mGrid.appendOneColumnVisibleItems();
                    }
                }
            } else {
                while (this.mGrid.appendOneColumnVisibleItems() && this.mGrid.getLastVisibleIndex() < lastVisibleIndex) {
                }
            }
        }
        updateScrollLimits();
        updateSecondaryScrollLimits();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void removeAndRecycleAllViews(RecyclerView.Recycler recycler) {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            removeAndRecycleViewAt(childCount, recycler);
        }
    }

    private void focusToViewInLayout(boolean z, boolean z2, int i, int i2) {
        View findViewByPosition = findViewByPosition(this.mFocusPosition);
        if (findViewByPosition != null && z2) {
            scrollToView(findViewByPosition, false, i, i2);
        }
        if (findViewByPosition != null && z && !findViewByPosition.hasFocus()) {
            findViewByPosition.requestFocus();
        } else if (!z && !this.mBaseGridView.hasFocus()) {
            if (findViewByPosition == null || !findViewByPosition.hasFocusable()) {
                int childCount = getChildCount();
                int i3 = 0;
                while (true) {
                    if (i3 < childCount) {
                        findViewByPosition = getChildAt(i3);
                        if (findViewByPosition != null && findViewByPosition.hasFocusable()) {
                            this.mBaseGridView.focusableViewAvailable(findViewByPosition);
                            break;
                        }
                        i3++;
                    } else {
                        break;
                    }
                }
            } else {
                this.mBaseGridView.focusableViewAvailable(findViewByPosition);
            }
            if (z2 && findViewByPosition != null && findViewByPosition.hasFocus()) {
                scrollToView(findViewByPosition, false, i, i2);
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutCompleted(RecyclerView.State state) {
        ArrayList<BaseGridView.OnLayoutCompletedListener> arrayList = this.mOnLayoutCompletedListeners;
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                this.mOnLayoutCompletedListeners.get(size).onLayoutCompleted(state);
            }
        }
    }

    void updatePositionToRowMapInPostLayout() {
        Grid.Location location;
        this.mPositionToRowInPostLayout.clear();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            int oldPosition = this.mBaseGridView.getChildViewHolder(getChildAt(i)).getOldPosition();
            if (oldPosition >= 0 && (location = this.mGrid.getLocation(oldPosition)) != null) {
                this.mPositionToRowInPostLayout.put(oldPosition, location.row);
            }
        }
    }

    void fillScrapViewsInPostLayout() {
        int i;
        List<RecyclerView.ViewHolder> scrapList = this.mRecycler.getScrapList();
        int size = scrapList.size();
        if (size != 0) {
            int[] iArr = this.mDisappearingPositions;
            if (iArr == null || size > iArr.length) {
                if (iArr == null) {
                    i = 16;
                } else {
                    i = iArr.length;
                }
                while (i < size) {
                    i <<= 1;
                }
                this.mDisappearingPositions = new int[i];
            }
            int i2 = 0;
            for (int i3 = 0; i3 < size; i3++) {
                int absoluteAdapterPosition = scrapList.get(i3).getAbsoluteAdapterPosition();
                if (absoluteAdapterPosition >= 0) {
                    i2++;
                    this.mDisappearingPositions[i2] = absoluteAdapterPosition;
                }
            }
            if (i2 > 0) {
                Arrays.sort(this.mDisappearingPositions, 0, i2);
                this.mGrid.fillDisappearingItems(this.mDisappearingPositions, i2, this.mPositionToRowInPostLayout);
            }
            this.mPositionToRowInPostLayout.clear();
        }
    }

    void updatePositionDeltaInPreLayout() {
        if (getChildCount() > 0) {
            this.mPositionDeltaInPreLayout = this.mGrid.getFirstVisibleIndex() - ((LayoutParams) getChildAt(0).getLayoutParams()).getViewLayoutPosition();
        } else {
            this.mPositionDeltaInPreLayout = 0;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        if (this.mNumRows == 0 || state.getItemCount() < 0) {
            return;
        }
        if ((this.mFlag & 64) == 0 || getChildCount() <= 0) {
            int i6 = this.mFlag;
            if ((i6 & 512) == 0) {
                discardLayoutInfo();
                removeAndRecycleAllViews(recycler);
                return;
            }
            boolean z = true;
            this.mFlag = (i6 & -4) | 1;
            saveContext(recycler, state);
            int i7 = Integer.MIN_VALUE;
            int i8 = 0;
            if (state.isPreLayout()) {
                updatePositionDeltaInPreLayout();
                int childCount = getChildCount();
                if (this.mGrid != null && childCount > 0) {
                    int i9 = Integer.MAX_VALUE;
                    int oldPosition = this.mBaseGridView.getChildViewHolder(getChildAt(0)).getOldPosition();
                    int oldPosition2 = this.mBaseGridView.getChildViewHolder(getChildAt(childCount - 1)).getOldPosition();
                    while (i8 < childCount) {
                        View childAt = getChildAt(i8);
                        LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                        int childAdapterPosition = this.mBaseGridView.getChildAdapterPosition(childAt);
                        if (layoutParams.isItemChanged() || layoutParams.isItemRemoved() || childAt.isLayoutRequested() || ((!childAt.hasFocus() && this.mFocusPosition == layoutParams.getAbsoluteAdapterPosition()) || ((childAt.hasFocus() && this.mFocusPosition != layoutParams.getAbsoluteAdapterPosition()) || childAdapterPosition < oldPosition || childAdapterPosition > oldPosition2))) {
                            i9 = Math.min(i9, getViewMin(childAt));
                            i7 = Math.max(i7, getViewMax(childAt));
                        }
                        i8++;
                    }
                    if (i7 > i9) {
                        this.mExtraLayoutSpaceInPreLayout = i7 - i9;
                    }
                    appendVisibleItems();
                    prependVisibleItems();
                }
                this.mFlag &= -4;
                leaveContext();
                return;
            }
            if (state.willRunPredictiveAnimations()) {
                updatePositionToRowMapInPostLayout();
            }
            if (isSmoothScrolling() || this.mFocusScrollStrategy != 0) {
                z = false;
            }
            int i10 = this.mFocusPosition;
            if (!(i10 == -1 || (i5 = this.mFocusPositionOffset) == Integer.MIN_VALUE)) {
                this.mFocusPosition = i10 + i5;
                this.mSubFocusPosition = 0;
            }
            this.mFocusPositionOffset = 0;
            View findViewByPosition = findViewByPosition(this.mFocusPosition);
            int i11 = this.mFocusPosition;
            int i12 = this.mSubFocusPosition;
            boolean hasFocus = this.mBaseGridView.hasFocus();
            Grid grid = this.mGrid;
            int firstVisibleIndex = grid != null ? grid.getFirstVisibleIndex() : -1;
            Grid grid2 = this.mGrid;
            int lastVisibleIndex = grid2 != null ? grid2.getLastVisibleIndex() : -1;
            if (this.mOrientation == 0) {
                i2 = state.getRemainingScrollHorizontal();
                i = state.getRemainingScrollVertical();
            } else {
                i = state.getRemainingScrollHorizontal();
                i2 = state.getRemainingScrollVertical();
            }
            if (layoutInit()) {
                this.mFlag |= 4;
                this.mGrid.setStart(this.mFocusPosition);
                fastRelayout();
            } else {
                int i13 = this.mFlag & -5;
                this.mFlag = i13;
                int i14 = i13 & -17;
                if (z) {
                    i8 = 16;
                }
                this.mFlag = i8 | i14;
                if (z && (firstVisibleIndex < 0 || (i4 = this.mFocusPosition) > lastVisibleIndex || i4 < firstVisibleIndex)) {
                    firstVisibleIndex = this.mFocusPosition;
                    lastVisibleIndex = firstVisibleIndex;
                }
                this.mGrid.setStart(firstVisibleIndex);
                if (lastVisibleIndex != -1) {
                    while (appendOneColumnVisibleItems() && findViewByPosition(lastVisibleIndex) == null) {
                    }
                }
            }
            while (true) {
                updateScrollLimits();
                int firstVisibleIndex2 = this.mGrid.getFirstVisibleIndex();
                int lastVisibleIndex2 = this.mGrid.getLastVisibleIndex();
                focusToViewInLayout(hasFocus, z, -i2, -i);
                appendVisibleItems();
                prependVisibleItems();
                if (this.mGrid.getFirstVisibleIndex() == firstVisibleIndex2 && this.mGrid.getLastVisibleIndex() == lastVisibleIndex2) {
                    break;
                }
            }
            removeInvisibleViewsAtFront();
            removeInvisibleViewsAtEnd();
            if (state.willRunPredictiveAnimations()) {
                fillScrapViewsInPostLayout();
            }
            int i15 = this.mFlag;
            if ((i15 & 1024) != 0) {
                this.mFlag = i15 & -1025;
            } else {
                updateRowSecondarySizeRefresh();
            }
            if ((this.mFlag & 4) != 0 && ((i3 = this.mFocusPosition) != i11 || this.mSubFocusPosition != i12 || findViewByPosition(i3) != findViewByPosition || (this.mFlag & 8) != 0)) {
                dispatchChildSelected();
            } else if ((this.mFlag & 20) == 16) {
                dispatchChildSelected();
            }
            dispatchChildSelectedAndPositioned();
            if ((this.mFlag & 64) != 0) {
                scrollDirectionPrimary(getSlideOutDistance());
            }
            this.mFlag &= -4;
            leaveContext();
            return;
        }
        this.mFlag |= 128;
    }

    private void offsetChildrenSecondary(int i) {
        int childCount = getChildCount();
        int i2 = 0;
        if (this.mOrientation == 0) {
            while (i2 < childCount) {
                getChildAt(i2).offsetTopAndBottom(i);
                i2++;
            }
            return;
        }
        while (i2 < childCount) {
            getChildAt(i2).offsetLeftAndRight(i);
            i2++;
        }
    }

    private void offsetChildrenPrimary(int i) {
        int childCount = getChildCount();
        int i2 = 0;
        if (this.mOrientation == 1) {
            while (i2 < childCount) {
                getChildAt(i2).offsetTopAndBottom(i);
                i2++;
            }
            return;
        }
        while (i2 < childCount) {
            getChildAt(i2).offsetLeftAndRight(i);
            i2++;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollHorizontallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int i2;
        if ((this.mFlag & 512) == 0 || !hasDoneFirstLayout()) {
            return 0;
        }
        saveContext(recycler, state);
        this.mFlag = (this.mFlag & -4) | 2;
        if (this.mOrientation == 0) {
            i2 = scrollDirectionPrimary(i);
        } else {
            i2 = scrollDirectionSecondary(i);
        }
        leaveContext();
        this.mFlag &= -4;
        return i2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int i2;
        if ((this.mFlag & 512) == 0 || !hasDoneFirstLayout()) {
            return 0;
        }
        this.mFlag = (this.mFlag & -4) | 2;
        saveContext(recycler, state);
        if (this.mOrientation == 1) {
            i2 = scrollDirectionPrimary(i);
        } else {
            i2 = scrollDirectionSecondary(i);
        }
        leaveContext();
        this.mFlag &= -4;
        return i2;
    }

    private int scrollDirectionPrimary(int i) {
        int i2;
        int i3 = this.mFlag;
        boolean z = true;
        if ((i3 & 64) == 0 && (i3 & 3) != 1 && (i <= 0 ? !(i >= 0 || this.mWindowAlignment.mainAxis().isMinUnknown() || i >= (i2 = this.mWindowAlignment.mainAxis().getMinScroll())) : !(this.mWindowAlignment.mainAxis().isMaxUnknown() || i <= (i2 = this.mWindowAlignment.mainAxis().getMaxScroll())))) {
            i = i2;
        }
        if (i == 0) {
            return 0;
        }
        offsetChildrenPrimary(-i);
        if ((this.mFlag & 3) == 1) {
            updateScrollLimits();
            return i;
        }
        int childCount = getChildCount();
        if ((this.mFlag & 262144) == 0 ? i >= 0 : i <= 0) {
            appendVisibleItems();
        } else {
            prependVisibleItems();
        }
        boolean z2 = getChildCount() > childCount;
        int childCount2 = getChildCount();
        if ((262144 & this.mFlag) == 0 ? i >= 0 : i <= 0) {
            removeInvisibleViewsAtFront();
        } else {
            removeInvisibleViewsAtEnd();
        }
        if (getChildCount() >= childCount2) {
            z = false;
        }
        if (z2 || z) {
            updateRowSecondarySizeRefresh();
        }
        this.mBaseGridView.invalidate();
        updateScrollLimits();
        return i;
    }

    private int scrollDirectionSecondary(int i) {
        if (i == 0) {
            return 0;
        }
        offsetChildrenSecondary(-i);
        this.mScrollOffsetSecondary += i;
        updateSecondaryScrollLimits();
        this.mBaseGridView.invalidate();
        return i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void collectAdjacentPrefetchPositions(int i, int i2, RecyclerView.State state, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int i3;
        try {
            saveContext(null, state);
            if (this.mOrientation != 0) {
                i = i2;
            }
            if (!(getChildCount() == 0 || i == 0)) {
                if (i < 0) {
                    i3 = -this.mExtraLayoutSpace;
                } else {
                    i3 = this.mSizePrimary + this.mExtraLayoutSpace;
                }
                this.mGrid.collectAdjacentPrefetchPositions(i3, i, layoutPrefetchRegistry);
            }
        } finally {
            leaveContext();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void collectInitialPrefetchPositions(int i, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int i2 = this.mBaseGridView.mInitialPrefetchItemCount;
        if (i != 0 && i2 != 0) {
            int max = Math.max(0, Math.min(this.mFocusPosition - ((i2 - 1) / 2), i - i2));
            int i3 = max;
            while (i3 < i && i3 < max + i2) {
                layoutPrefetchRegistry.addPosition(i3, 0);
                i3++;
            }
        }
    }

    void updateScrollLimits() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        if (this.mState.getItemCount() != 0) {
            if ((this.mFlag & 262144) == 0) {
                i4 = this.mGrid.getLastVisibleIndex();
                i3 = this.mState.getItemCount() - 1;
                i = this.mGrid.getFirstVisibleIndex();
                i2 = 0;
            } else {
                i4 = this.mGrid.getFirstVisibleIndex();
                int lastVisibleIndex = this.mGrid.getLastVisibleIndex();
                i2 = this.mState.getItemCount() - 1;
                i = lastVisibleIndex;
                i3 = 0;
            }
            if (i4 >= 0 && i >= 0) {
                boolean z = i4 == i3;
                boolean z2 = i == i2;
                if (z || !this.mWindowAlignment.mainAxis().isMaxUnknown() || z2 || !this.mWindowAlignment.mainAxis().isMinUnknown()) {
                    int i7 = Integer.MAX_VALUE;
                    if (z) {
                        i7 = this.mGrid.findRowMax(true, sTwoInts);
                        View findViewByPosition = findViewByPosition(sTwoInts[1]);
                        i5 = getViewCenter(findViewByPosition);
                        int[] alignMultiple = ((LayoutParams) findViewByPosition.getLayoutParams()).getAlignMultiple();
                        if (alignMultiple != null && alignMultiple.length > 0) {
                            i5 += alignMultiple[alignMultiple.length - 1] - alignMultiple[0];
                        }
                    } else {
                        i5 = Integer.MAX_VALUE;
                    }
                    int i8 = Integer.MIN_VALUE;
                    if (z2) {
                        i8 = this.mGrid.findRowMin(false, sTwoInts);
                        i6 = getViewCenter(findViewByPosition(sTwoInts[1]));
                    } else {
                        i6 = Integer.MIN_VALUE;
                    }
                    this.mWindowAlignment.mainAxis().updateMinMax(i8, i7, i6, i5);
                }
            }
        }
    }

    private void updateSecondaryScrollLimits() {
        WindowAlignment.Axis secondAxis = this.mWindowAlignment.secondAxis();
        int paddingMin = secondAxis.getPaddingMin() - this.mScrollOffsetSecondary;
        int sizeSecondary = getSizeSecondary() + paddingMin;
        secondAxis.updateMinMax(paddingMin, sizeSecondary, paddingMin, sizeSecondary);
    }

    private void initScrollController() {
        this.mWindowAlignment.reset();
        this.mWindowAlignment.horizontal.setSize(getWidth());
        this.mWindowAlignment.vertical.setSize(getHeight());
        this.mWindowAlignment.horizontal.setPadding(getPaddingLeft(), getPaddingRight());
        this.mWindowAlignment.vertical.setPadding(getPaddingTop(), getPaddingBottom());
        this.mSizePrimary = this.mWindowAlignment.mainAxis().getSize();
        this.mScrollOffsetSecondary = 0;
    }

    private void updateScrollController() {
        this.mWindowAlignment.horizontal.setSize(getWidth());
        this.mWindowAlignment.vertical.setSize(getHeight());
        this.mWindowAlignment.horizontal.setPadding(getPaddingLeft(), getPaddingRight());
        this.mWindowAlignment.vertical.setPadding(getPaddingTop(), getPaddingBottom());
        this.mSizePrimary = this.mWindowAlignment.mainAxis().getSize();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void scrollToPosition(int i) {
        setSelection(i, 0, false, 0);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
        setSelection(i, 0, true, 0);
    }

    public void setSelection(int i, int i2) {
        setSelection(i, 0, false, i2);
    }

    public void setSelectionSmooth(int i) {
        setSelection(i, 0, true, 0);
    }

    public void setSelectionWithSub(int i, int i2, int i3) {
        setSelection(i, i2, false, i3);
    }

    public int getSelection() {
        return this.mFocusPosition;
    }

    public void setSelection(int i, int i2, boolean z, int i3) {
        if ((this.mFocusPosition != i && i != -1) || i2 != this.mSubFocusPosition || i3 != this.mPrimaryScrollExtra) {
            scrollToSelection(i, i2, z, i3);
        }
    }

    void scrollToSelection(int i, int i2, boolean z, int i3) {
        this.mPrimaryScrollExtra = i3;
        View findViewByPosition = findViewByPosition(i);
        boolean z2 = !isSmoothScrolling();
        if (!z2 || this.mBaseGridView.isLayoutRequested() || findViewByPosition == null || getAdapterPositionByView(findViewByPosition) != i) {
            int i4 = this.mFlag;
            if ((i4 & 512) == 0 || (i4 & 64) != 0) {
                this.mFocusPosition = i;
                this.mSubFocusPosition = i2;
                this.mFocusPositionOffset = Integer.MIN_VALUE;
            } else if (!z || this.mBaseGridView.isLayoutRequested()) {
                if (!z2) {
                    skipSmoothScrollerOnStopInternal();
                    this.mBaseGridView.stopScroll();
                }
                if (this.mBaseGridView.isLayoutRequested() || findViewByPosition == null || getAdapterPositionByView(findViewByPosition) != i) {
                    this.mFocusPosition = i;
                    this.mSubFocusPosition = i2;
                    this.mFocusPositionOffset = Integer.MIN_VALUE;
                    this.mFlag |= 256;
                    requestLayout();
                    return;
                }
                this.mFlag |= 32;
                scrollToView(findViewByPosition, z);
                this.mFlag &= -33;
            } else {
                this.mFocusPosition = i;
                this.mSubFocusPosition = i2;
                this.mFocusPositionOffset = Integer.MIN_VALUE;
                if (!hasDoneFirstLayout()) {
                    Log.w(getTag(), "setSelectionSmooth should not be called before first layout pass");
                    return;
                }
                int startPositionSmoothScroller = startPositionSmoothScroller(i);
                if (startPositionSmoothScroller != this.mFocusPosition) {
                    this.mFocusPosition = startPositionSmoothScroller;
                    this.mSubFocusPosition = 0;
                }
            }
        } else {
            this.mFlag |= 32;
            scrollToView(findViewByPosition, z);
            this.mFlag &= -33;
        }
    }

    int startPositionSmoothScroller(int i) {
        AnonymousClass4 r0 = new GridLinearSmoothScroller() { // from class: androidx.leanback.widget.GridLayoutManager.4
            @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller
            public PointF computeScrollVectorForPosition(int i2) {
                if (getChildCount() == 0) {
                    return null;
                }
                GridLayoutManager gridLayoutManager = GridLayoutManager.this;
                boolean z = false;
                int position = gridLayoutManager.getPosition(gridLayoutManager.getChildAt(0));
                GridLayoutManager gridLayoutManager2 = GridLayoutManager.this;
                int i3 = 1;
                if ((gridLayoutManager2.mFlag & 262144) == 0 ? i2 < position : i2 > position) {
                    z = true;
                }
                if (z) {
                    i3 = -1;
                }
                if (gridLayoutManager2.mOrientation == 0) {
                    return new PointF((float) i3, 0.0f);
                }
                return new PointF(0.0f, (float) i3);
            }
        };
        r0.setTargetPosition(i);
        startSmoothScroll(r0);
        return r0.getTargetPosition();
    }

    void skipSmoothScrollerOnStopInternal() {
        GridLinearSmoothScroller gridLinearSmoothScroller = this.mCurrentSmoothScroller;
        if (gridLinearSmoothScroller != null) {
            gridLinearSmoothScroller.mSkipOnStopInternal = true;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void startSmoothScroll(RecyclerView.SmoothScroller smoothScroller) {
        skipSmoothScrollerOnStopInternal();
        super.startSmoothScroll(smoothScroller);
        if (!smoothScroller.isRunning() || !(smoothScroller instanceof GridLinearSmoothScroller)) {
            this.mCurrentSmoothScroller = null;
            this.mPendingMoveSmoothScroller = null;
            return;
        }
        GridLinearSmoothScroller gridLinearSmoothScroller = (GridLinearSmoothScroller) smoothScroller;
        this.mCurrentSmoothScroller = gridLinearSmoothScroller;
        if (gridLinearSmoothScroller instanceof PendingMoveSmoothScroller) {
            this.mPendingMoveSmoothScroller = (PendingMoveSmoothScroller) gridLinearSmoothScroller;
        } else {
            this.mPendingMoveSmoothScroller = null;
        }
    }

    void processPendingMovement(boolean z) {
        if (z) {
            if (hasCreatedLastItem()) {
                return;
            }
        } else if (hasCreatedFirstItem()) {
            return;
        }
        PendingMoveSmoothScroller pendingMoveSmoothScroller = this.mPendingMoveSmoothScroller;
        if (pendingMoveSmoothScroller == null) {
            boolean z2 = true;
            int i = z ? 1 : -1;
            if (this.mNumRows <= 1) {
                z2 = false;
            }
            PendingMoveSmoothScroller pendingMoveSmoothScroller2 = new PendingMoveSmoothScroller(i, z2);
            this.mFocusPositionOffset = 0;
            startSmoothScroll(pendingMoveSmoothScroller2);
        } else if (z) {
            pendingMoveSmoothScroller.increasePendingMoves();
        } else {
            pendingMoveSmoothScroller.decreasePendingMoves();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsAdded(RecyclerView recyclerView, int i, int i2) {
        Grid grid;
        int i3;
        if (!(this.mFocusPosition == -1 || (grid = this.mGrid) == null || grid.getFirstVisibleIndex() < 0 || (i3 = this.mFocusPositionOffset) == Integer.MIN_VALUE || i > this.mFocusPosition + i3)) {
            this.mFocusPositionOffset = i3 + i2;
        }
        this.mChildrenStates.clear();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsChanged(RecyclerView recyclerView) {
        this.mFocusPositionOffset = 0;
        this.mChildrenStates.clear();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsRemoved(RecyclerView recyclerView, int i, int i2) {
        Grid grid;
        int i3;
        int i4;
        int i5;
        if (!(this.mFocusPosition == -1 || (grid = this.mGrid) == null || grid.getFirstVisibleIndex() < 0 || (i3 = this.mFocusPositionOffset) == Integer.MIN_VALUE || i > (i5 = (i4 = this.mFocusPosition) + i3))) {
            if (i + i2 > i5) {
                int i6 = i3 + (i - i5);
                this.mFocusPositionOffset = i6;
                this.mFocusPosition = i4 + i6;
                this.mFocusPositionOffset = Integer.MIN_VALUE;
            } else {
                this.mFocusPositionOffset = i3 - i2;
            }
        }
        this.mChildrenStates.clear();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsMoved(RecyclerView recyclerView, int i, int i2, int i3) {
        int i4;
        int i5 = this.mFocusPosition;
        if (!(i5 == -1 || (i4 = this.mFocusPositionOffset) == Integer.MIN_VALUE)) {
            int i6 = i5 + i4;
            if (i <= i6 && i6 < i + i3) {
                this.mFocusPositionOffset = i4 + (i2 - i);
            } else if (i < i6 && i2 > i6 - i3) {
                this.mFocusPositionOffset = i4 - i3;
            } else if (i > i6 && i2 < i6) {
                this.mFocusPositionOffset = i4 + i3;
            }
        }
        this.mChildrenStates.clear();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsUpdated(RecyclerView recyclerView, int i, int i2) {
        int i3 = i2 + i;
        while (i < i3) {
            this.mChildrenStates.remove(i);
            i++;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean onRequestChildFocus(RecyclerView recyclerView, View view, View view2) {
        if ((this.mFlag & 32768) == 0 && getAdapterPositionByView(view) != -1 && (this.mFlag & 35) == 0) {
            scrollToView(view, view2, true);
        }
        return true;
    }

    private int getPrimaryAlignedScrollDistance(View view) {
        return this.mWindowAlignment.mainAxis().getScroll(getViewCenter(view));
    }

    private int getAdjustedPrimaryAlignedScrollDistance(int i, View view, View view2) {
        int subPositionByView = getSubPositionByView(view, view2);
        if (subPositionByView == 0) {
            return i;
        }
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        return i + (layoutParams.getAlignMultiple()[subPositionByView] - layoutParams.getAlignMultiple()[0]);
    }

    private int getSecondaryScrollDistance(View view) {
        return this.mWindowAlignment.secondAxis().getScroll(getViewCenterSecondary(view));
    }

    void scrollToView(View view, boolean z) {
        scrollToView(view, view == null ? null : view.findFocus(), z);
    }

    void scrollToView(View view, boolean z, int i, int i2) {
        scrollToView(view, view == null ? null : view.findFocus(), z, i, i2);
    }

    private void scrollToView(View view, View view2, boolean z) {
        scrollToView(view, view2, z, 0, 0);
    }

    private void scrollToView(View view, View view2, boolean z, int i, int i2) {
        if ((this.mFlag & 64) == 0) {
            int adapterPositionByView = getAdapterPositionByView(view);
            int subPositionByView = getSubPositionByView(view, view2);
            if (!(adapterPositionByView == this.mFocusPosition && subPositionByView == this.mSubFocusPosition)) {
                this.mFocusPosition = adapterPositionByView;
                this.mSubFocusPosition = subPositionByView;
                this.mFocusPositionOffset = 0;
                if ((this.mFlag & 3) != 1) {
                    dispatchChildSelected();
                }
                if (this.mBaseGridView.isChildrenDrawingOrderEnabledInternal()) {
                    this.mBaseGridView.invalidate();
                }
            }
            if (view != null) {
                if (!view.hasFocus() && this.mBaseGridView.hasFocus()) {
                    view.requestFocus();
                }
                if ((this.mFlag & 131072) == 0 && z) {
                    return;
                }
                if (getScrollPosition(view, view2, sTwoInts) || i != 0 || i2 != 0) {
                    int[] iArr = sTwoInts;
                    scrollGrid(iArr[0] + i, iArr[1] + i2, z);
                }
            }
        }
    }

    boolean getScrollPosition(View view, View view2, int[] iArr) {
        int i = this.mFocusScrollStrategy;
        if (i == 1 || i == 2) {
            return getNoneAlignedPosition(view, iArr);
        }
        return getAlignedPosition(view, view2, iArr);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x009f, code lost:
        if (r2 != null) goto L_0x00a5;
     */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00ba  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean getNoneAlignedPosition(android.view.View r13, int[] r14) {
        /*
            r12 = this;
            int r0 = r12.getAdapterPositionByView(r13)
            int r1 = r12.getViewMin(r13)
            int r2 = r12.getViewMax(r13)
            androidx.leanback.widget.WindowAlignment r3 = r12.mWindowAlignment
            androidx.leanback.widget.WindowAlignment$Axis r3 = r3.mainAxis()
            int r3 = r3.getPaddingMin()
            androidx.leanback.widget.WindowAlignment r4 = r12.mWindowAlignment
            androidx.leanback.widget.WindowAlignment$Axis r4 = r4.mainAxis()
            int r4 = r4.getClientSize()
            androidx.leanback.widget.Grid r5 = r12.mGrid
            int r5 = r5.getRowIndex(r0)
            r6 = 1
            r7 = 0
            r8 = 2
            r9 = 0
            if (r1 >= r3) goto L_0x006f
            int r1 = r12.mFocusScrollStrategy
            if (r1 != r8) goto L_0x006c
            r1 = r13
        L_0x0031:
            boolean r10 = r12.prependOneColumnVisibleItems()
            if (r10 == 0) goto L_0x0069
            androidx.leanback.widget.Grid r1 = r12.mGrid
            int r10 = r1.getFirstVisibleIndex()
            androidx.collection.CircularIntArray[] r1 = r1.getItemPositionsInRows(r10, r0)
            r1 = r1[r5]
            int r10 = r1.get(r7)
            android.view.View r10 = r12.findViewByPosition(r10)
            int r11 = r12.getViewMin(r10)
            int r11 = r2 - r11
            if (r11 <= r4) goto L_0x0067
            int r0 = r1.size()
            if (r0 <= r8) goto L_0x0064
            int r0 = r1.get(r8)
            android.view.View r0 = r12.findViewByPosition(r0)
            r2 = r9
            r9 = r0
            goto L_0x00a5
        L_0x0064:
            r2 = r9
            r9 = r10
            goto L_0x00a5
        L_0x0067:
            r1 = r10
            goto L_0x0031
        L_0x0069:
            r2 = r9
            r9 = r1
            goto L_0x00a5
        L_0x006c:
            r2 = r9
        L_0x006d:
            r9 = r13
            goto L_0x00a5
        L_0x006f:
            int r10 = r4 + r3
            if (r2 <= r10) goto L_0x00a4
            int r2 = r12.mFocusScrollStrategy
            if (r2 != r8) goto L_0x00a2
        L_0x0077:
            androidx.leanback.widget.Grid r2 = r12.mGrid
            int r8 = r2.getLastVisibleIndex()
            androidx.collection.CircularIntArray[] r2 = r2.getItemPositionsInRows(r0, r8)
            r2 = r2[r5]
            int r8 = r2.size()
            int r8 = r8 - r6
            int r2 = r2.get(r8)
            android.view.View r2 = r12.findViewByPosition(r2)
            int r8 = r12.getViewMax(r2)
            int r8 = r8 - r1
            if (r8 <= r4) goto L_0x0099
            r2 = r9
            goto L_0x009f
        L_0x0099:
            boolean r8 = r12.appendOneColumnVisibleItems()
            if (r8 != 0) goto L_0x0077
        L_0x009f:
            if (r2 == 0) goto L_0x006d
            goto L_0x00a5
        L_0x00a2:
            r2 = r13
            goto L_0x00a5
        L_0x00a4:
            r2 = r9
        L_0x00a5:
            if (r9 == 0) goto L_0x00ad
            int r0 = r12.getViewMin(r9)
        L_0x00ab:
            int r0 = r0 - r3
            goto L_0x00b6
        L_0x00ad:
            if (r2 == 0) goto L_0x00b5
            int r0 = r12.getViewMax(r2)
            int r3 = r3 + r4
            goto L_0x00ab
        L_0x00b5:
            r0 = r7
        L_0x00b6:
            if (r9 == 0) goto L_0x00ba
            r13 = r9
            goto L_0x00bd
        L_0x00ba:
            if (r2 == 0) goto L_0x00bd
            r13 = r2
        L_0x00bd:
            int r12 = r12.getSecondaryScrollDistance(r13)
            if (r0 != 0) goto L_0x00c7
            if (r12 == 0) goto L_0x00c6
            goto L_0x00c7
        L_0x00c6:
            return r7
        L_0x00c7:
            r14[r7] = r0
            r14[r6] = r12
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.getNoneAlignedPosition(android.view.View, int[]):boolean");
    }

    private boolean getAlignedPosition(View view, View view2, int[] iArr) {
        int primaryAlignedScrollDistance = getPrimaryAlignedScrollDistance(view);
        if (view2 != null) {
            primaryAlignedScrollDistance = getAdjustedPrimaryAlignedScrollDistance(primaryAlignedScrollDistance, view, view2);
        }
        int secondaryScrollDistance = getSecondaryScrollDistance(view);
        int i = primaryAlignedScrollDistance + this.mPrimaryScrollExtra;
        if (i == 0 && secondaryScrollDistance == 0) {
            iArr[0] = 0;
            iArr[1] = 0;
            return false;
        }
        iArr[0] = i;
        iArr[1] = secondaryScrollDistance;
        return true;
    }

    private void scrollGrid(int i, int i2, boolean z) {
        if ((this.mFlag & 3) == 1) {
            scrollDirectionPrimary(i);
            scrollDirectionSecondary(i2);
            return;
        }
        if (this.mOrientation != 0) {
            i2 = i;
            i = i2;
        }
        if (z) {
            this.mBaseGridView.smoothScrollBy(i, i2);
            return;
        }
        this.mBaseGridView.scrollBy(i, i2);
        dispatchChildSelectedAndPositioned();
    }

    public boolean isScrollEnabled() {
        return (this.mFlag & 131072) != 0;
    }

    private int findImmediateChildIndex(View view) {
        BaseGridView baseGridView;
        View findContainingItemView;
        if (view == null || (baseGridView = this.mBaseGridView) == null || view == baseGridView || (findContainingItemView = findContainingItemView(view)) == null) {
            return -1;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (getChildAt(i) == findContainingItemView) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public void onFocusChanged(boolean z, int i, Rect rect) {
        if (z) {
            int i2 = this.mFocusPosition;
            while (true) {
                View findViewByPosition = findViewByPosition(i2);
                if (findViewByPosition != null) {
                    if (findViewByPosition.getVisibility() != 0 || !findViewByPosition.hasFocusable()) {
                        i2++;
                    } else {
                        findViewByPosition.requestFocus();
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:72:0x00ca A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x00cb  */
    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.view.View onInterceptFocusSearch(android.view.View r8, int r9) {
        /*
        // Method dump skipped, instructions count: 222
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.onInterceptFocusSearch(android.view.View, int):android.view.View");
    }

    /* JADX WARNING: Removed duplicated region for block: B:53:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00b5  */
    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onAddFocusables(androidx.recyclerview.widget.RecyclerView r18, java.util.ArrayList<android.view.View> r19, int r20, int r21) {
        /*
        // Method dump skipped, instructions count: 391
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.onAddFocusables(androidx.recyclerview.widget.RecyclerView, java.util.ArrayList, int, int):boolean");
    }

    boolean hasCreatedLastItem() {
        int itemCount = getItemCount();
        if (itemCount == 0 || this.mBaseGridView.findViewHolderForAdapterPosition(itemCount - 1) != null) {
            return true;
        }
        return false;
    }

    boolean hasCreatedFirstItem() {
        if (getItemCount() == 0 || this.mBaseGridView.findViewHolderForAdapterPosition(0) != null) {
            return true;
        }
        return false;
    }

    boolean isItemFullyVisible(int i) {
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.mBaseGridView.findViewHolderForAdapterPosition(i);
        if (findViewHolderForAdapterPosition != null && findViewHolderForAdapterPosition.itemView.getLeft() >= 0 && findViewHolderForAdapterPosition.itemView.getRight() <= this.mBaseGridView.getWidth() && findViewHolderForAdapterPosition.itemView.getTop() >= 0 && findViewHolderForAdapterPosition.itemView.getBottom() <= this.mBaseGridView.getHeight()) {
            return true;
        }
        return false;
    }

    boolean canScrollTo(View view) {
        return view.getVisibility() == 0 && (!hasFocus() || view.hasFocusable());
    }

    /* access modifiers changed from: package-private */
    public boolean gridOnRequestFocusInDescendants(RecyclerView recyclerView, int i, Rect rect) {
        int i2 = this.mFocusScrollStrategy;
        if (i2 == 1 || i2 == 2) {
            return gridOnRequestFocusInDescendantsUnaligned(i, rect);
        }
        return gridOnRequestFocusInDescendantsAligned(i, rect);
    }

    private boolean gridOnRequestFocusInDescendantsAligned(int i, Rect rect) {
        View findViewByPosition = findViewByPosition(this.mFocusPosition);
        if (findViewByPosition != null) {
            return findViewByPosition.requestFocus(i, rect);
        }
        return false;
    }

    private boolean gridOnRequestFocusInDescendantsUnaligned(int i, Rect rect) {
        int i2;
        int i3;
        int childCount = getChildCount();
        int i4 = -1;
        if ((i & 2) != 0) {
            i4 = childCount;
            i3 = 0;
            i2 = 1;
        } else {
            i3 = childCount - 1;
            i2 = -1;
        }
        int paddingMin = this.mWindowAlignment.mainAxis().getPaddingMin();
        int clientSize = this.mWindowAlignment.mainAxis().getClientSize() + paddingMin;
        while (i3 != i4) {
            View childAt = getChildAt(i3);
            if (childAt.getVisibility() == 0 && getViewMin(childAt) >= paddingMin && getViewMax(childAt) <= clientSize && childAt.requestFocus(i, rect)) {
                return true;
            }
            i3 += i2;
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0035, code lost:
        if (r10 != 130) goto L_0x0046;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003d, code lost:
        if ((r9.mFlag & 524288) == 0) goto L_0x001b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0043, code lost:
        if ((r9.mFlag & 524288) == 0) goto L_0x0023;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        return 3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0018, code lost:
        if (r10 != 130) goto L_0x0046;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getMovement(int r10) {
        /*
            r9 = this;
            int r0 = r9.mOrientation
            r1 = 130(0x82, float:1.82E-43)
            r2 = 66
            r3 = 33
            r4 = 0
            r5 = 3
            r6 = 2
            r7 = 17
            r8 = 1
            if (r0 != 0) goto L_0x002b
            r0 = 262144(0x40000, float:3.67342E-40)
            if (r10 == r7) goto L_0x0025
            if (r10 == r3) goto L_0x0023
            if (r10 == r2) goto L_0x001d
            if (r10 == r1) goto L_0x001b
            goto L_0x0046
        L_0x001b:
            r4 = r5
            goto L_0x0047
        L_0x001d:
            int r9 = r9.mFlag
            r9 = r9 & r0
            if (r9 != 0) goto L_0x0047
            goto L_0x0038
        L_0x0023:
            r4 = r6
            goto L_0x0047
        L_0x0025:
            int r9 = r9.mFlag
            r9 = r9 & r0
            if (r9 != 0) goto L_0x0038
            goto L_0x0047
        L_0x002b:
            if (r0 != r8) goto L_0x0046
            r0 = 524288(0x80000, float:7.34684E-40)
            if (r10 == r7) goto L_0x0040
            if (r10 == r3) goto L_0x0047
            if (r10 == r2) goto L_0x003a
            if (r10 == r1) goto L_0x0038
            goto L_0x0046
        L_0x0038:
            r4 = r8
            goto L_0x0047
        L_0x003a:
            int r9 = r9.mFlag
            r9 = r9 & r0
            if (r9 != 0) goto L_0x0023
            goto L_0x001b
        L_0x0040:
            int r9 = r9.mFlag
            r9 = r9 & r0
            if (r9 != 0) goto L_0x001b
            goto L_0x0023
        L_0x0046:
            r4 = r7
        L_0x0047:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.getMovement(int):int");
    }

    /* access modifiers changed from: package-private */
    public int getChildDrawingOrder(RecyclerView recyclerView, int i, int i2) {
        View findViewByPosition = findViewByPosition(this.mFocusPosition);
        if (findViewByPosition == null) {
            return i2;
        }
        int indexOfChild = recyclerView.indexOfChild(findViewByPosition);
        if (i2 < indexOfChild) {
            return i2;
        }
        return i2 < i + -1 ? ((indexOfChild + i) - 1) - i2 : indexOfChild;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onAdapterChanged(RecyclerView.Adapter adapter, RecyclerView.Adapter adapter2) {
        if (adapter != null) {
            discardLayoutInfo();
            this.mFocusPosition = -1;
            this.mFocusPositionOffset = 0;
            this.mChildrenStates.clear();
        }
        if (adapter2 instanceof FacetProviderAdapter) {
            this.mFacetProviderAdapter = (FacetProviderAdapter) adapter2;
        } else {
            this.mFacetProviderAdapter = null;
        }
        super.onAdapterChanged(adapter, adapter2);
    }

    private void discardLayoutInfo() {
        this.mGrid = null;
        this.mRowSizeSecondary = null;
        this.mFlag &= -1025;
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: classes.dex */
    public static final class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: androidx.leanback.widget.GridLayoutManager.SavedState.1
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        Bundle childStates;
        int index;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.index);
            parcel.writeBundle(this.childStates);
        }

        SavedState(Parcel parcel) {
            this.childStates = Bundle.EMPTY;
            this.index = parcel.readInt();
            this.childStates = parcel.readBundle(GridLayoutManager.class.getClassLoader());
        }

        SavedState() {
            this.childStates = Bundle.EMPTY;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState();
        savedState.index = getSelection();
        Bundle saveAsBundle = this.mChildrenStates.saveAsBundle();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            int adapterPositionByView = getAdapterPositionByView(childAt);
            if (adapterPositionByView != -1) {
                saveAsBundle = this.mChildrenStates.saveOnScreenView(saveAsBundle, childAt, adapterPositionByView);
            }
        }
        savedState.childStates = saveAsBundle;
        return savedState;
    }

    /* access modifiers changed from: package-private */
    public void onChildRecycled(RecyclerView.ViewHolder viewHolder) {
        int absoluteAdapterPosition = viewHolder.getAbsoluteAdapterPosition();
        if (absoluteAdapterPosition != -1) {
            this.mChildrenStates.saveOffscreenView(viewHolder.itemView, absoluteAdapterPosition);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            SavedState savedState = (SavedState) parcelable;
            this.mFocusPosition = savedState.index;
            this.mFocusPositionOffset = 0;
            this.mChildrenStates.loadFromBundle(savedState.childStates);
            this.mFlag |= 256;
            requestLayout();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getRowCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Grid grid;
        if (this.mOrientation != 0 || (grid = this.mGrid) == null) {
            return super.getRowCountForAccessibility(recycler, state);
        }
        return grid.getNumRows();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getColumnCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Grid grid;
        if (this.mOrientation != 1 || (grid = this.mGrid) == null) {
            return super.getColumnCountForAccessibility(recycler, state);
        }
        return grid.getNumRows();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler recycler, RecyclerView.State state, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (this.mGrid != null && (layoutParams instanceof LayoutParams)) {
            int absoluteAdapterPosition = ((LayoutParams) layoutParams).getAbsoluteAdapterPosition();
            int rowIndex = absoluteAdapterPosition >= 0 ? this.mGrid.getRowIndex(absoluteAdapterPosition) : -1;
            if (rowIndex >= 0) {
                int numRows = absoluteAdapterPosition / this.mGrid.getNumRows();
                if (this.mOrientation == 0) {
                    accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(rowIndex, 1, numRows, 1, false, false));
                } else {
                    accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(numRows, 1, rowIndex, 1, false, false));
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002c, code lost:
        if (r6 != false) goto L_0x004c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0037, code lost:
        if (r6 != false) goto L_0x0042;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004a, code lost:
        if (r8 == androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_DOWN.getId()) goto L_0x004c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x004c, code lost:
        r8 = 4096;
     */
    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean performAccessibilityAction(androidx.recyclerview.widget.RecyclerView.Recycler r6, androidx.recyclerview.widget.RecyclerView.State r7, int r8, android.os.Bundle r9) {
        /*
            r5 = this;
            boolean r9 = r5.isScrollEnabled()
            r0 = 1
            if (r9 != 0) goto L_0x0008
            return r0
        L_0x0008:
            r5.saveContext(r6, r7)
            int r6 = r5.mFlag
            r9 = 262144(0x40000, float:3.67342E-40)
            r6 = r6 & r9
            r9 = 0
            if (r6 == 0) goto L_0x0015
            r6 = r0
            goto L_0x0016
        L_0x0015:
            r6 = r9
        L_0x0016:
            int r1 = android.os.Build.VERSION.SDK_INT
            r2 = 23
            r3 = 4096(0x1000, float:5.74E-42)
            r4 = 8192(0x2000, float:1.14794E-41)
            if (r1 < r2) goto L_0x004d
            int r1 = r5.mOrientation
            if (r1 != 0) goto L_0x003a
            androidx.core.view.accessibility.AccessibilityNodeInfoCompat$AccessibilityActionCompat r1 = androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_LEFT
            int r1 = r1.getId()
            if (r8 != r1) goto L_0x002f
            if (r6 == 0) goto L_0x0042
            goto L_0x004c
        L_0x002f:
            androidx.core.view.accessibility.AccessibilityNodeInfoCompat$AccessibilityActionCompat r1 = androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_RIGHT
            int r1 = r1.getId()
            if (r8 != r1) goto L_0x004d
            if (r6 == 0) goto L_0x004c
            goto L_0x0042
        L_0x003a:
            androidx.core.view.accessibility.AccessibilityNodeInfoCompat$AccessibilityActionCompat r6 = androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_UP
            int r6 = r6.getId()
            if (r8 != r6) goto L_0x0044
        L_0x0042:
            r8 = r4
            goto L_0x004d
        L_0x0044:
            androidx.core.view.accessibility.AccessibilityNodeInfoCompat$AccessibilityActionCompat r6 = androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_DOWN
            int r6 = r6.getId()
            if (r8 != r6) goto L_0x004d
        L_0x004c:
            r8 = r3
        L_0x004d:
            int r6 = r5.mFocusPosition
            if (r6 != 0) goto L_0x0055
            if (r8 != r4) goto L_0x0055
            r1 = r0
            goto L_0x0056
        L_0x0055:
            r1 = r9
        L_0x0056:
            int r7 = r7.getItemCount()
            int r7 = r7 - r0
            if (r6 != r7) goto L_0x0061
            if (r8 != r3) goto L_0x0061
            r6 = r0
            goto L_0x0062
        L_0x0061:
            r6 = r9
        L_0x0062:
            if (r1 != 0) goto L_0x007b
            if (r6 == 0) goto L_0x0067
            goto L_0x007b
        L_0x0067:
            if (r8 == r3) goto L_0x0074
            if (r8 == r4) goto L_0x006c
            goto L_0x007e
        L_0x006c:
            r5.processPendingMovement(r9)
            r6 = -1
            r5.processSelectionMoves(r9, r6)
            goto L_0x007e
        L_0x0074:
            r5.processPendingMovement(r0)
            r5.processSelectionMoves(r9, r0)
            goto L_0x007e
        L_0x007b:
            r5.sendTypeViewScrolledAccessibilityEvent()
        L_0x007e:
            r5.leaveContext()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.performAccessibilityAction(androidx.recyclerview.widget.RecyclerView$Recycler, androidx.recyclerview.widget.RecyclerView$State, int, android.os.Bundle):boolean");
    }

    private void sendTypeViewScrolledAccessibilityEvent() {
        AccessibilityEvent obtain = AccessibilityEvent.obtain(4096);
        this.mBaseGridView.onInitializeAccessibilityEvent(obtain);
        BaseGridView baseGridView = this.mBaseGridView;
        baseGridView.requestSendAccessibilityEvent(baseGridView, obtain);
    }

    int processSelectionMoves(boolean z, int i) {
        Grid grid = this.mGrid;
        if (grid == null) {
            return i;
        }
        int i2 = this.mFocusPosition;
        int rowIndex = i2 != -1 ? grid.getRowIndex(i2) : -1;
        View view = null;
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount && i != 0; i3++) {
            int i4 = i > 0 ? i3 : (childCount - 1) - i3;
            View childAt = getChildAt(i4);
            if (canScrollTo(childAt)) {
                int adapterPositionByIndex = getAdapterPositionByIndex(i4);
                int rowIndex2 = this.mGrid.getRowIndex(adapterPositionByIndex);
                if (rowIndex == -1) {
                    i2 = adapterPositionByIndex;
                    view = childAt;
                    rowIndex = rowIndex2;
                } else if (rowIndex2 == rowIndex && ((i > 0 && adapterPositionByIndex > i2) || (i < 0 && adapterPositionByIndex < i2))) {
                    i = i > 0 ? i - 1 : i + 1;
                    i2 = adapterPositionByIndex;
                    view = childAt;
                }
            }
        }
        if (view != null) {
            if (z) {
                if (hasFocus()) {
                    this.mFlag |= 32;
                    view.requestFocus();
                    this.mFlag &= -33;
                }
                this.mFocusPosition = i2;
                this.mSubFocusPosition = 0;
            } else {
                scrollToView(view, true);
            }
        }
        return i;
    }

    private void addA11yActionMovingBackward(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, boolean z) {
        AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat;
        if (Build.VERSION.SDK_INT < 23) {
            accessibilityNodeInfoCompat.addAction(8192);
        } else if (this.mOrientation == 0) {
            if (z) {
                accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_RIGHT;
            } else {
                accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_LEFT;
            }
            accessibilityNodeInfoCompat.addAction(accessibilityActionCompat);
        } else {
            accessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_UP);
        }
        accessibilityNodeInfoCompat.setScrollable(true);
    }

    private void addA11yActionMovingForward(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, boolean z) {
        AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat;
        if (Build.VERSION.SDK_INT < 23) {
            accessibilityNodeInfoCompat.addAction(4096);
        } else if (this.mOrientation == 0) {
            if (z) {
                accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_LEFT;
            } else {
                accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_RIGHT;
            }
            accessibilityNodeInfoCompat.addAction(accessibilityActionCompat);
        } else {
            accessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_DOWN);
        }
        accessibilityNodeInfoCompat.setScrollable(true);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler recycler, RecyclerView.State state, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        saveContext(recycler, state);
        int itemCount = state.getItemCount();
        int i = this.mFlag;
        boolean z = (262144 & i) != 0;
        if ((i & 2048) == 0 || (itemCount > 1 && !isItemFullyVisible(0))) {
            addA11yActionMovingBackward(accessibilityNodeInfoCompat, z);
        }
        if ((this.mFlag & 4096) == 0 || (itemCount > 1 && !isItemFullyVisible(itemCount - 1))) {
            addA11yActionMovingForward(accessibilityNodeInfoCompat, z);
        }
        accessibilityNodeInfoCompat.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(getRowCountForAccessibility(recycler, state), getColumnCountForAccessibility(recycler, state), isLayoutHierarchical(recycler, state), getSelectionModeForAccessibility(recycler, state)));
        leaveContext();
    }
}
