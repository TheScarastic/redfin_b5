package com.android.systemui.util.animation;

import android.view.ViewTreeObserver;
/* compiled from: TransitionLayout.kt */
/* loaded from: classes2.dex */
public final class TransitionLayout$preDrawApplicator$1 implements ViewTreeObserver.OnPreDrawListener {
    final /* synthetic */ TransitionLayout this$0;

    /* access modifiers changed from: package-private */
    public TransitionLayout$preDrawApplicator$1(TransitionLayout transitionLayout) {
        this.this$0 = transitionLayout;
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public boolean onPreDraw() {
        this.this$0.updateScheduled = false;
        this.this$0.getViewTreeObserver().removeOnPreDrawListener(this);
        this.this$0.isPreDrawApplicatorRegistered = false;
        this.this$0.applyCurrentState();
        return true;
    }
}
