package com.android.wm.shell.pip.phone;

import android.content.res.Configuration;
import com.android.wm.shell.pip.phone.PipController;
/* loaded from: classes2.dex */
public final /* synthetic */ class PipController$PipImpl$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ PipController.PipImpl f$0;
    public final /* synthetic */ Configuration f$1;

    public /* synthetic */ PipController$PipImpl$$ExternalSyntheticLambda5(PipController.PipImpl pipImpl, Configuration configuration) {
        this.f$0 = pipImpl;
        this.f$1 = configuration;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onConfigurationChanged$2(this.f$1);
    }
}
