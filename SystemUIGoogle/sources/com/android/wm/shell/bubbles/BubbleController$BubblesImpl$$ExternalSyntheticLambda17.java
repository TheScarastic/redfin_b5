package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda17 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ Consumer f$1;
    public final /* synthetic */ Executor f$2;
    public final /* synthetic */ String f$3;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda17(BubbleController.BubblesImpl bubblesImpl, Consumer consumer, Executor executor, String str) {
        this.f$0 = bubblesImpl;
        this.f$1 = consumer;
        this.f$2 = executor;
        this.f$3 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$removeSuppressedSummaryIfNecessary$2(this.f$1, this.f$2, this.f$3);
    }
}
