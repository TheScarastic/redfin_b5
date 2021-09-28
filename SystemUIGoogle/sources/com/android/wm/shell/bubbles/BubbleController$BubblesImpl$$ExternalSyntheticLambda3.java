package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda3(BubbleController.BubblesImpl bubblesImpl, int i) {
        this.f$0 = bubblesImpl;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onUserChanged$22(this.f$1);
    }
}
