package com.android.systemui.shared.system;

import android.app.KeyguardManager;
import android.content.Context;
/* loaded from: classes.dex */
public class KeyguardManagerCompat {
    private final KeyguardManager mKeyguardManager;

    public KeyguardManagerCompat(Context context) {
        this.mKeyguardManager = (KeyguardManager) context.getSystemService("keyguard");
    }

    public boolean isDeviceLocked(int i) {
        return this.mKeyguardManager.isDeviceLocked(i);
    }
}
