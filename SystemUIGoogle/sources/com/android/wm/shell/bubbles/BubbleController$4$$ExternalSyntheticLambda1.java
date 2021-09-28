package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleController$4$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ BubbleController.AnonymousClass4 f$0;
    public final /* synthetic */ Bubble f$1;

    public /* synthetic */ BubbleController$4$$ExternalSyntheticLambda1(BubbleController.AnonymousClass4 r1, Bubble bubble) {
        this.f$0 = r1;
        this.f$1 = bubble;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.lambda$applyUpdate$1(this.f$1, (BubbleEntry) obj);
    }
}
