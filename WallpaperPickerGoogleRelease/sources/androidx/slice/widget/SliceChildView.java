package androidx.slice.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceAction;
import androidx.slice.widget.SliceActionView;
import androidx.slice.widget.SliceView;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public abstract class SliceChildView extends FrameLayout {
    public int mInsetBottom;
    public int mInsetEnd;
    public int mInsetStart;
    public int mInsetTop;
    public long mLastUpdated;
    public SliceActionView.SliceActionLoadingListener mLoadingListener;
    public SliceView.OnSliceActionListener mObserver;
    public RowStyle mRowStyle;
    public boolean mShowLastUpdated;
    public SliceStyle mSliceStyle;
    public int mTintColor;
    public SliceViewPolicy mViewPolicy;

    public SliceChildView(Context context) {
        super(context);
        this.mTintColor = -1;
        this.mLastUpdated = -1;
    }

    public Set<SliceItem> getLoadingActions() {
        return null;
    }

    public int getMode() {
        SliceViewPolicy sliceViewPolicy = this.mViewPolicy;
        if (sliceViewPolicy != null) {
            return sliceViewPolicy.mMode;
        }
        return 2;
    }

    public abstract void resetView();

    public void setActionLoading(SliceItem sliceItem) {
    }

    public void setAllowTwoLines(boolean z) {
    }

    public void setInsets(int i, int i2, int i3, int i4) {
        this.mInsetStart = i;
        this.mInsetTop = i2;
        this.mInsetEnd = i3;
        this.mInsetBottom = i4;
    }

    public void setLastUpdated(long j) {
        this.mLastUpdated = j;
    }

    public void setLoadingActions(Set<SliceItem> set) {
    }

    public void setPolicy(SliceViewPolicy sliceViewPolicy) {
        this.mViewPolicy = sliceViewPolicy;
    }

    public void setShowLastUpdated(boolean z) {
        this.mShowLastUpdated = z;
    }

    public void setSliceActionListener(SliceView.OnSliceActionListener onSliceActionListener) {
        this.mObserver = null;
    }

    public void setSliceActions(List<SliceAction> list) {
    }

    public void setSliceContent(ListContent listContent) {
    }

    public void setSliceItem(SliceContent sliceContent, boolean z, int i, int i2, SliceView.OnSliceActionListener onSliceActionListener) {
    }

    public void setStyle(SliceStyle sliceStyle, RowStyle rowStyle) {
        this.mSliceStyle = sliceStyle;
        this.mRowStyle = rowStyle;
    }

    public void setTint(int i) {
        this.mTintColor = i;
    }

    public SliceChildView(Context context, AttributeSet attributeSet) {
        this(context);
    }
}
