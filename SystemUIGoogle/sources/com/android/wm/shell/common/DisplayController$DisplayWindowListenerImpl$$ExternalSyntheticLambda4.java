package com.android.wm.shell.common;

import android.content.res.Configuration;
import com.android.wm.shell.common.DisplayController;
/* loaded from: classes2.dex */
public final /* synthetic */ class DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ DisplayController.DisplayWindowListenerImpl f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ Configuration f$2;

    public /* synthetic */ DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda4(DisplayController.DisplayWindowListenerImpl displayWindowListenerImpl, int i, Configuration configuration) {
        this.f$0 = displayWindowListenerImpl;
        this.f$1 = i;
        this.f$2 = configuration;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onDisplayConfigurationChanged$1(this.f$1, this.f$2);
    }
}
