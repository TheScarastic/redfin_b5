package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda12 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ BubbleEntry f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda12(BubbleController.BubblesImpl bubblesImpl, BubbleEntry bubbleEntry) {
        this.f$0 = bubblesImpl;
        this.f$1 = bubbleEntry;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$expandStackAndSelectBubble$5(this.f$1);
    }
}
