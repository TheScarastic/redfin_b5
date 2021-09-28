package com.android.wm.shell.common;

import android.view.InsetsSourceControl;
import android.view.InsetsState;
import com.android.wm.shell.common.DisplayImeController;
/* loaded from: classes2.dex */
public final /* synthetic */ class DisplayImeController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ DisplayImeController.PerDisplay.DisplayWindowInsetsControllerImpl f$0;
    public final /* synthetic */ InsetsState f$1;
    public final /* synthetic */ InsetsSourceControl[] f$2;

    public /* synthetic */ DisplayImeController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda3(DisplayImeController.PerDisplay.DisplayWindowInsetsControllerImpl displayWindowInsetsControllerImpl, InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
        this.f$0 = displayWindowInsetsControllerImpl;
        this.f$1 = insetsState;
        this.f$2 = insetsSourceControlArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$insetsControlChanged$2(this.f$1, this.f$2);
    }
}
