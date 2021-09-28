package com.android.wm.shell.onehanded;

import android.content.res.Configuration;
import com.android.wm.shell.onehanded.OneHandedController;
/* loaded from: classes2.dex */
public final /* synthetic */ class OneHandedController$OneHandedImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ OneHandedController.OneHandedImpl f$0;
    public final /* synthetic */ Configuration f$1;

    public /* synthetic */ OneHandedController$OneHandedImpl$$ExternalSyntheticLambda3(OneHandedController.OneHandedImpl oneHandedImpl, Configuration configuration) {
        this.f$0 = oneHandedImpl;
        this.f$1 = configuration;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onConfigChanged$6(this.f$1);
    }
}
