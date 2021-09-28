package com.android.wm.shell.common;

import com.android.wm.shell.common.DisplayImeController;
/* loaded from: classes2.dex */
public final /* synthetic */ class DisplayImeController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ DisplayImeController.PerDisplay.DisplayWindowInsetsControllerImpl f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ DisplayImeController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda4(DisplayImeController.PerDisplay.DisplayWindowInsetsControllerImpl displayWindowInsetsControllerImpl, String str) {
        this.f$0 = displayWindowInsetsControllerImpl;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$topFocusedWindowChanged$0(this.f$1);
    }
}
