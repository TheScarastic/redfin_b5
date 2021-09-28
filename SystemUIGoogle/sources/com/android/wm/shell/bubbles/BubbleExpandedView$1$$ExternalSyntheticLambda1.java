package com.android.wm.shell.bubbles;

import android.app.ActivityOptions;
import android.graphics.Rect;
import com.android.wm.shell.bubbles.BubbleExpandedView;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleExpandedView$1$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ BubbleExpandedView.AnonymousClass1 f$0;
    public final /* synthetic */ ActivityOptions f$1;
    public final /* synthetic */ Rect f$2;

    public /* synthetic */ BubbleExpandedView$1$$ExternalSyntheticLambda1(BubbleExpandedView.AnonymousClass1 r1, ActivityOptions activityOptions, Rect rect) {
        this.f$0 = r1;
        this.f$1 = activityOptions;
        this.f$2 = rect;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onInitialized$0(this.f$1, this.f$2);
    }
}
