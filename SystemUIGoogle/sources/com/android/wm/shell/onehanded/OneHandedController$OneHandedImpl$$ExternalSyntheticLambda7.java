package com.android.wm.shell.onehanded;

import com.android.wm.shell.onehanded.OneHandedController;
/* loaded from: classes2.dex */
public final /* synthetic */ class OneHandedController$OneHandedImpl$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ OneHandedController.OneHandedImpl f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ OneHandedController$OneHandedImpl$$ExternalSyntheticLambda7(OneHandedController.OneHandedImpl oneHandedImpl, boolean z, boolean z2) {
        this.f$0 = oneHandedImpl;
        this.f$1 = z;
        this.f$2 = z2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setLockedDisabled$3(this.f$1, this.f$2);
    }
}
