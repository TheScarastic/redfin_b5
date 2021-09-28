package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.Bubbles;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda15 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ Bubbles.SysuiProxy f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda15(BubbleController.BubblesImpl bubblesImpl, Bubbles.SysuiProxy sysuiProxy) {
        this.f$0 = bubblesImpl;
        this.f$1 = sysuiProxy;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setSysuiProxy$12(this.f$1);
    }
}
