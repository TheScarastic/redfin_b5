package com.android.wm.shell.common;

import android.view.IDisplayWindowRotationCallback;
import com.android.wm.shell.common.DisplayChangeController;
/* loaded from: classes2.dex */
public final /* synthetic */ class DisplayChangeController$DisplayWindowRotationControllerImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DisplayChangeController.DisplayWindowRotationControllerImpl f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ IDisplayWindowRotationCallback f$4;

    public /* synthetic */ DisplayChangeController$DisplayWindowRotationControllerImpl$$ExternalSyntheticLambda0(DisplayChangeController.DisplayWindowRotationControllerImpl displayWindowRotationControllerImpl, int i, int i2, int i3, IDisplayWindowRotationCallback iDisplayWindowRotationCallback) {
        this.f$0 = displayWindowRotationControllerImpl;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = i3;
        this.f$4 = iDisplayWindowRotationCallback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onRotateDisplay$0(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
