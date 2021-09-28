package com.google.android.systemui.columbus;

import android.content.Context;
import android.os.PowerManager;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PowerManagerWrapper.kt */
/* loaded from: classes2.dex */
public class PowerManagerWrapper {
    private final PowerManager powerManager;

    public PowerManagerWrapper(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.powerManager = (PowerManager) context.getSystemService("power");
    }

    /* compiled from: PowerManagerWrapper.kt */
    /* loaded from: classes2.dex */
    public static class WakeLockWrapper {
        private final PowerManager.WakeLock wakeLock;

        public WakeLockWrapper(PowerManager.WakeLock wakeLock) {
            this.wakeLock = wakeLock;
        }

        public void acquire(long j) {
            PowerManager.WakeLock wakeLock = this.wakeLock;
            if (wakeLock != null) {
                wakeLock.acquire(j);
            }
        }
    }

    public WakeLockWrapper newWakeLock(int i, String str) {
        Intrinsics.checkNotNullParameter(str, "tag");
        PowerManager powerManager = this.powerManager;
        return new WakeLockWrapper(powerManager == null ? null : powerManager.newWakeLock(i, str));
    }

    public Boolean isInteractive() {
        PowerManager powerManager = this.powerManager;
        if (powerManager == null) {
            return null;
        }
        return Boolean.valueOf(powerManager.isInteractive());
    }
}
