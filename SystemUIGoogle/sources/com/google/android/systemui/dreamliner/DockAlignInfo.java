package com.google.android.systemui.dreamliner;
/* loaded from: classes2.dex */
public class DockAlignInfo {
    private final int mAlignPct;
    private final int mAlignState;

    public DockAlignInfo(int i, int i2) {
        this.mAlignState = i;
        this.mAlignPct = i2;
    }

    public int getAlignState() {
        return this.mAlignState;
    }

    public int getAlignPct() {
        return this.mAlignPct;
    }
}
