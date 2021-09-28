package androidx.recyclerview.widget;

import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public class LayoutState {
    public int mAvailable;
    public int mCurrentPosition;
    public boolean mInfinite;
    public int mItemDirection;
    public int mLayoutDirection;
    public boolean mStopInFocusable;
    public boolean mRecycle = true;
    public int mStartLine = 0;
    public int mEndLine = 0;

    public String toString() {
        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("LayoutState{mAvailable=");
        m.append(this.mAvailable);
        m.append(", mCurrentPosition=");
        m.append(this.mCurrentPosition);
        m.append(", mItemDirection=");
        m.append(this.mItemDirection);
        m.append(", mLayoutDirection=");
        m.append(this.mLayoutDirection);
        m.append(", mStartLine=");
        m.append(this.mStartLine);
        m.append(", mEndLine=");
        m.append(this.mEndLine);
        m.append('}');
        return m.toString();
    }
}
