package com.android.wm.shell.onehanded;

import com.android.wm.shell.onehanded.OneHandedController;
/* loaded from: classes2.dex */
public final /* synthetic */ class OneHandedController$OneHandedImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ OneHandedController.OneHandedImpl f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ OneHandedController$OneHandedImpl$$ExternalSyntheticLambda1(OneHandedController.OneHandedImpl oneHandedImpl, int i) {
        this.f$0 = oneHandedImpl;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$stopOneHanded$2(this.f$1);
    }
}
