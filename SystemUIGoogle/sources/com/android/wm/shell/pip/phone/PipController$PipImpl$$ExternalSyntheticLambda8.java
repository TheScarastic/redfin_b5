package com.android.wm.shell.pip.phone;

import com.android.wm.shell.pip.phone.PipController;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class PipController$PipImpl$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ PipController.PipImpl f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ PipController$PipImpl$$ExternalSyntheticLambda8(PipController.PipImpl pipImpl, Consumer consumer) {
        this.f$0 = pipImpl;
        this.f$1 = consumer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setPipExclusionBoundsChangeListener$9(this.f$1);
    }
}
