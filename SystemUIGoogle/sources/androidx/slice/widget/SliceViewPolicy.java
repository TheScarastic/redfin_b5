package androidx.slice.widget;
/* loaded from: classes.dex */
public class SliceViewPolicy {
    private PolicyChangeListener mListener;
    private int mMaxHeight = 0;
    private int mMaxSmallHeight = 0;
    private boolean mScrollable = true;
    private int mMode = 2;

    /* loaded from: classes.dex */
    public interface PolicyChangeListener {
        void onMaxHeightChanged(int i);

        void onMaxSmallChanged(int i);
    }

    public void setListener(PolicyChangeListener policyChangeListener) {
        this.mListener = policyChangeListener;
    }

    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    public int getMaxSmallHeight() {
        return this.mMaxSmallHeight;
    }

    public boolean isScrollable() {
        return this.mScrollable;
    }

    public int getMode() {
        return this.mMode;
    }

    public void setMaxHeight(int i) {
        if (i != this.mMaxHeight) {
            this.mMaxHeight = i;
            PolicyChangeListener policyChangeListener = this.mListener;
            if (policyChangeListener != null) {
                policyChangeListener.onMaxHeightChanged(i);
            }
        }
    }

    public void setMaxSmallHeight(int i) {
        if (this.mMaxSmallHeight != i) {
            this.mMaxSmallHeight = i;
            PolicyChangeListener policyChangeListener = this.mListener;
            if (policyChangeListener != null) {
                policyChangeListener.onMaxSmallChanged(i);
            }
        }
    }
}
