package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda9 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ Bubble f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda9(BubbleController.BubblesImpl bubblesImpl, Bubble bubble) {
        this.f$0 = bubblesImpl;
        this.f$1 = bubble;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$expandStackAndSelectBubble$6(this.f$1);
    }
}
