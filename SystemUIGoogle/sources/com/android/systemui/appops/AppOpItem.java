package com.android.systemui.appops;
/* loaded from: classes.dex */
public class AppOpItem {
    private int mCode;
    private boolean mIsDisabled;
    private String mPackageName;
    private StringBuilder mState;
    private long mTimeStartedElapsed;
    private int mUid;

    public AppOpItem(int i, int i2, String str, long j) {
        this.mCode = i;
        this.mUid = i2;
        this.mPackageName = str;
        this.mTimeStartedElapsed = j;
        StringBuilder sb = new StringBuilder();
        sb.append("AppOpItem(");
        sb.append("Op code=");
        sb.append(i);
        sb.append(", ");
        sb.append("UID=");
        sb.append(i2);
        sb.append(", ");
        sb.append("Package name=");
        sb.append(str);
        sb.append(", ");
        sb.append("Paused=");
        this.mState = sb;
    }

    public int getCode() {
        return this.mCode;
    }

    public int getUid() {
        return this.mUid;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public long getTimeStartedElapsed() {
        return this.mTimeStartedElapsed;
    }

    public void setDisabled(boolean z) {
        this.mIsDisabled = z;
    }

    public boolean isDisabled() {
        return this.mIsDisabled;
    }

    public String toString() {
        StringBuilder sb = this.mState;
        sb.append(this.mIsDisabled);
        sb.append(")");
        return sb.toString();
    }
}
