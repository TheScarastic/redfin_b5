package com.android.keyguard;

import android.view.View;
import android.view.ViewPropertyAnimator;
import androidx.appcompat.R$styleable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
/* loaded from: classes.dex */
public class KeyguardVisibilityHelper {
    private boolean mAnimateYPos;
    private final DozeParameters mDozeParameters;
    private final KeyguardStateController mKeyguardStateController;
    private boolean mKeyguardViewVisibilityAnimating;
    private final UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    private View mView;
    private boolean mLastOccludedState = false;
    private final AnimationProperties mAnimationProperties = new AnimationProperties();
    private final Runnable mAnimateKeyguardStatusViewInvisibleEndRunnable = new Runnable() { // from class: com.android.keyguard.KeyguardVisibilityHelper$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            KeyguardVisibilityHelper.$r8$lambda$_LKGqgB9rtsYChysUNvvAaVh1uo(KeyguardVisibilityHelper.this);
        }
    };
    private final Runnable mAnimateKeyguardStatusViewGoneEndRunnable = new Runnable() { // from class: com.android.keyguard.KeyguardVisibilityHelper$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            KeyguardVisibilityHelper.$r8$lambda$clLsUyjaESrz144F6Tu8QZo1h1Y(KeyguardVisibilityHelper.this);
        }
    };
    private final Runnable mAnimateKeyguardStatusViewVisibleEndRunnable = new Runnable() { // from class: com.android.keyguard.KeyguardVisibilityHelper$$ExternalSyntheticLambda2
        @Override // java.lang.Runnable
        public final void run() {
            KeyguardVisibilityHelper.$r8$lambda$m13VDioBxfHFL5OFUGNyOM1xFSM(KeyguardVisibilityHelper.this);
        }
    };

    public KeyguardVisibilityHelper(View view, KeyguardStateController keyguardStateController, DozeParameters dozeParameters, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, boolean z) {
        this.mView = view;
        this.mKeyguardStateController = keyguardStateController;
        this.mDozeParameters = dozeParameters;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
        this.mAnimateYPos = z;
    }

    public boolean isVisibilityAnimating() {
        return this.mKeyguardViewVisibilityAnimating;
    }

    public void setViewVisibility(int i, boolean z, boolean z2, int i2) {
        this.mView.animate().cancel();
        boolean isOccluded = this.mKeyguardStateController.isOccluded();
        this.mKeyguardViewVisibilityAnimating = false;
        if ((!z && i2 == 1 && i != 1) || z2) {
            this.mKeyguardViewVisibilityAnimating = true;
            this.mView.animate().alpha(0.0f).setStartDelay(0).setDuration(160).setInterpolator(Interpolators.ALPHA_OUT).withEndAction(this.mAnimateKeyguardStatusViewGoneEndRunnable);
            if (z) {
                this.mView.animate().setStartDelay(this.mKeyguardStateController.getKeyguardFadingAwayDelay()).setDuration(this.mKeyguardStateController.getShortenedFadingAwayDuration()).start();
            }
        } else if (i2 == 2 && i == 1) {
            this.mView.setVisibility(0);
            this.mKeyguardViewVisibilityAnimating = true;
            this.mView.setAlpha(0.0f);
            this.mView.animate().alpha(1.0f).setStartDelay(0).setDuration(320).setInterpolator(Interpolators.ALPHA_IN).withEndAction(this.mAnimateKeyguardStatusViewVisibleEndRunnable);
        } else if (i != 1) {
            this.mView.setVisibility(8);
            this.mView.setAlpha(1.0f);
        } else if (z) {
            this.mKeyguardViewVisibilityAnimating = true;
            ViewPropertyAnimator withEndAction = this.mView.animate().alpha(0.0f).setInterpolator(Interpolators.FAST_OUT_LINEAR_IN).withEndAction(this.mAnimateKeyguardStatusViewInvisibleEndRunnable);
            if (this.mAnimateYPos) {
                float y = this.mView.getY() - (((float) this.mView.getHeight()) * 0.05f);
                AnimationProperties animationProperties = this.mAnimationProperties;
                long j = (long) R$styleable.AppCompatTheme_windowMinWidthMinor;
                long j2 = (long) 0;
                animationProperties.setDuration(j).setDelay(j2);
                View view = this.mView;
                AnimatableProperty animatableProperty = AnimatableProperty.Y;
                PropertyAnimator.cancelAnimation(view, animatableProperty);
                PropertyAnimator.setProperty(this.mView, animatableProperty, y, this.mAnimationProperties, true);
                withEndAction.setDuration(j).setStartDelay(j2);
            }
            withEndAction.start();
        } else if (this.mLastOccludedState && !isOccluded) {
            this.mView.setVisibility(0);
            this.mView.setAlpha(0.0f);
            this.mView.animate().setDuration(500).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f).withEndAction(this.mAnimateKeyguardStatusViewVisibleEndRunnable).start();
        } else if (this.mUnlockedScreenOffAnimationController.isScreenOffLightRevealAnimationPlaying()) {
            this.mKeyguardViewVisibilityAnimating = true;
            this.mUnlockedScreenOffAnimationController.animateInKeyguard(this.mView, this.mAnimateKeyguardStatusViewVisibleEndRunnable);
        } else {
            this.mView.setVisibility(0);
            this.mView.setAlpha(1.0f);
        }
        this.mLastOccludedState = isOccluded;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mKeyguardViewVisibilityAnimating = false;
        this.mView.setVisibility(4);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        this.mKeyguardViewVisibilityAnimating = false;
        this.mView.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        this.mKeyguardViewVisibilityAnimating = false;
    }
}
