package com.android.wm.shell.common;

import com.android.wm.shell.common.DisplayImeController;
/* loaded from: classes2.dex */
public final /* synthetic */ class DisplayImeController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ DisplayImeController.PerDisplay.DisplayWindowInsetsControllerImpl f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ DisplayImeController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda1(DisplayImeController.PerDisplay.DisplayWindowInsetsControllerImpl displayWindowInsetsControllerImpl, int i, boolean z) {
        this.f$0 = displayWindowInsetsControllerImpl;
        this.f$1 = i;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$showInsets$3(this.f$1, this.f$2);
    }
}
