package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleController$2$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ BubbleController.AnonymousClass2 f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ BubbleController$2$$ExternalSyntheticLambda1(BubbleController.AnonymousClass2 r1, int i) {
        this.f$0 = r1;
        this.f$1 = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.lambda$onTaskMovedToFront$1(this.f$1, (Boolean) obj);
    }
}
