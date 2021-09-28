package com.android.wm.shell.bubbles;

import java.util.function.IntConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda21 implements Runnable {
    public final /* synthetic */ IntConsumer f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda21(IntConsumer intConsumer, int i) {
        this.f$0 = intConsumer;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.accept(this.f$1);
    }
}
