package com.android.wm.shell.onehanded;

import com.android.wm.shell.onehanded.OneHandedController;
/* loaded from: classes2.dex */
public final /* synthetic */ class OneHandedController$OneHandedImpl$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ OneHandedController.OneHandedImpl f$0;
    public final /* synthetic */ OneHandedEventCallback f$1;

    public /* synthetic */ OneHandedController$OneHandedImpl$$ExternalSyntheticLambda4(OneHandedController.OneHandedImpl oneHandedImpl, OneHandedEventCallback oneHandedEventCallback) {
        this.f$0 = oneHandedImpl;
        this.f$1 = oneHandedEventCallback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$registerEventCallback$4(this.f$1);
    }
}
