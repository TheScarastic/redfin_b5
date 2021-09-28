package com.android.systemui.biometrics;

import android.view.Surface;
/* loaded from: classes.dex */
public interface UdfpsHbmProvider {
    void disableHbm(Runnable runnable);

    void enableHbm(int i, Surface surface, Runnable runnable);
}
