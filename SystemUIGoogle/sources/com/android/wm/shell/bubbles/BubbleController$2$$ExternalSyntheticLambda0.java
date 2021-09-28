package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleController$2$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ BubbleController.AnonymousClass2 f$0;
    public final /* synthetic */ Boolean f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ BubbleController$2$$ExternalSyntheticLambda0(BubbleController.AnonymousClass2 r1, Boolean bool, int i) {
        this.f$0 = r1;
        this.f$1 = bool;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onTaskMovedToFront$0(this.f$1, this.f$2);
    }
}
