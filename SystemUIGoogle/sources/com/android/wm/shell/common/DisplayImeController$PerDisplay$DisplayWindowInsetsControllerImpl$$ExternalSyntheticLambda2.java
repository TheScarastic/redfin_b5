package com.android.wm.shell.common;

import android.view.InsetsState;
import com.android.wm.shell.common.DisplayImeController;
/* loaded from: classes2.dex */
public final /* synthetic */ class DisplayImeController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ DisplayImeController.PerDisplay.DisplayWindowInsetsControllerImpl f$0;
    public final /* synthetic */ InsetsState f$1;

    public /* synthetic */ DisplayImeController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda2(DisplayImeController.PerDisplay.DisplayWindowInsetsControllerImpl displayWindowInsetsControllerImpl, InsetsState insetsState) {
        this.f$0 = displayWindowInsetsControllerImpl;
        this.f$1 = insetsState;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$insetsChanged$1(this.f$1);
    }
}
