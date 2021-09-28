package com.android.systemui.navigationbar;

import com.android.systemui.navigationbar.RotationButtonController;
/* loaded from: classes.dex */
public final /* synthetic */ class RotationButtonController$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ RotationButtonController.AnonymousClass1 f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ RotationButtonController$1$$ExternalSyntheticLambda0(RotationButtonController.AnonymousClass1 r1, int i) {
        this.f$0 = r1;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onRotationChanged$0(this.f$1);
    }
}
