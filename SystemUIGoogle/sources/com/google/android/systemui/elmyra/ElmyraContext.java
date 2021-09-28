package com.google.android.systemui.elmyra;

import android.content.Context;
/* loaded from: classes2.dex */
public final class ElmyraContext {
    private Context mContext;

    public ElmyraContext(Context context) {
        this.mContext = context;
    }

    public boolean isAvailable() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.sensor.assist");
    }
}
