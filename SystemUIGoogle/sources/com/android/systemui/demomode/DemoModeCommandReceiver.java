package com.android.systemui.demomode;

import android.os.Bundle;
/* loaded from: classes.dex */
public interface DemoModeCommandReceiver {
    void dispatchDemoCommand(String str, Bundle bundle);

    default void onDemoModeFinished() {
    }

    default void onDemoModeStarted() {
    }
}
