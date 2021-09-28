package com.android.systemui.demomode.dagger;

import android.content.Context;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.settings.GlobalSettings;
/* loaded from: classes.dex */
public abstract class DemoModeModule {
    /* access modifiers changed from: package-private */
    public static DemoModeController provideDemoModeController(Context context, DumpManager dumpManager, GlobalSettings globalSettings) {
        DemoModeController demoModeController = new DemoModeController(context, dumpManager, globalSettings);
        demoModeController.initialize();
        return demoModeController;
    }
}
