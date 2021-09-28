package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda24 implements Supplier {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ BubbleEntry f$1;
    public final /* synthetic */ List f$2;
    public final /* synthetic */ IntConsumer f$3;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda24(BubbleController.BubblesImpl bubblesImpl, BubbleEntry bubbleEntry, List list, IntConsumer intConsumer) {
        this.f$0 = bubblesImpl;
        this.f$1 = bubbleEntry;
        this.f$2 = list;
        this.f$3 = intConsumer;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        return this.f$0.lambda$handleDismissalInterception$11(this.f$1, this.f$2, this.f$3);
    }
}
