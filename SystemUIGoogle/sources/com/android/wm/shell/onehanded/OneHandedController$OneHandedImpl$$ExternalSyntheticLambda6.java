package com.android.wm.shell.onehanded;

import com.android.wm.shell.onehanded.OneHandedController;
/* loaded from: classes2.dex */
public final /* synthetic */ class OneHandedController$OneHandedImpl$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ OneHandedController.OneHandedImpl f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ OneHandedController$OneHandedImpl$$ExternalSyntheticLambda6(OneHandedController.OneHandedImpl oneHandedImpl, boolean z) {
        this.f$0 = oneHandedImpl;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onKeyguardVisibilityChanged$8(this.f$1);
    }
}
