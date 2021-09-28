package com.android.keyguard;

import android.animation.ValueAnimator;
import android.view.WindowInsetsAnimationController;
import com.android.keyguard.KeyguardPasswordView;
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardPasswordView$1$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ WindowInsetsAnimationController f$0;
    public final /* synthetic */ ValueAnimator f$1;

    public /* synthetic */ KeyguardPasswordView$1$$ExternalSyntheticLambda0(WindowInsetsAnimationController windowInsetsAnimationController, ValueAnimator valueAnimator) {
        this.f$0 = windowInsetsAnimationController;
        this.f$1 = valueAnimator;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        KeyguardPasswordView.AnonymousClass1.m11$r8$lambda$2hxqAG6eMJPz803KTO261_JPSI(this.f$0, this.f$1, valueAnimator);
    }
}
