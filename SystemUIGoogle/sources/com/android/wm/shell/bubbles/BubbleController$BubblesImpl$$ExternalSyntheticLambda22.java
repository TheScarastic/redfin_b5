package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda22 implements Consumer {
    public final /* synthetic */ Executor f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda22(Executor executor, Consumer consumer) {
        this.f$0 = executor;
        this.f$1 = consumer;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        BubbleController.BubblesImpl.lambda$removeSuppressedSummaryIfNecessary$1(this.f$0, this.f$1, (String) obj);
    }
}
