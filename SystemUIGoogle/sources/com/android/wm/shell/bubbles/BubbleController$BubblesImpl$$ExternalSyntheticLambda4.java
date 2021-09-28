package com.android.wm.shell.bubbles;

import android.content.res.Configuration;
import com.android.wm.shell.bubbles.BubbleController;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ Configuration f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda4(BubbleController.BubblesImpl bubblesImpl, Configuration configuration) {
        this.f$0 = bubblesImpl;
        this.f$1 = configuration;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onConfigChanged$24(this.f$1);
    }
}
