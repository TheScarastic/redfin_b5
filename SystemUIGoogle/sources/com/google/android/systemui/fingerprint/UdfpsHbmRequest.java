package com.google.android.systemui.fingerprint;

import android.view.Surface;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class UdfpsHbmRequest {
    public final Args args;
    public boolean beganEnablingHbm;
    public boolean finishedEnablingHbm;

    /* access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class Args {
        public final int displayId;
        public final int hbmType;
        public final Runnable onHbmEnabled;
        public final Surface surface;

        Args(int i, int i2, Surface surface, Runnable runnable) {
            this.displayId = i;
            this.hbmType = i2;
            this.surface = surface;
            this.onHbmEnabled = runnable;
        }
    }

    /* access modifiers changed from: package-private */
    public UdfpsHbmRequest(int i, int i2, Surface surface, Runnable runnable) {
        this.args = new Args(i, i2, surface, runnable);
    }
}
