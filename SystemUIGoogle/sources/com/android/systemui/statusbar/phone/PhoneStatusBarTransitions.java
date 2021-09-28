package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
/* loaded from: classes.dex */
public final class PhoneStatusBarTransitions extends BarTransitions {
    private View mBattery;
    private Animator mCurrentAnimation;
    private final float mIconAlphaWhenOpaque;
    private View mLeftSide;
    private View mStatusIcons;

    private boolean isOpaque(int i) {
        return (i == 1 || i == 2 || i == 0 || i == 6) ? false : true;
    }

    public PhoneStatusBarTransitions(PhoneStatusBarView phoneStatusBarView, View view) {
        super(view, R$drawable.status_background);
        this.mIconAlphaWhenOpaque = phoneStatusBarView.getContext().getResources().getFraction(R$dimen.status_bar_icon_drawing_alpha, 1, 1);
        this.mLeftSide = phoneStatusBarView.findViewById(R$id.status_bar_left_side);
        this.mStatusIcons = phoneStatusBarView.findViewById(R$id.statusIcons);
        this.mBattery = phoneStatusBarView.findViewById(R$id.battery);
        applyModeBackground(-1, getMode(), false);
        applyMode(getMode(), false);
    }

    public ObjectAnimator animateTransitionTo(View view, float f) {
        return ObjectAnimator.ofFloat(view, "alpha", view.getAlpha(), f);
    }

    private float getNonBatteryClockAlphaFor(int i) {
        if (isLightsOut(i)) {
            return 0.0f;
        }
        if (!isOpaque(i)) {
            return 1.0f;
        }
        return this.mIconAlphaWhenOpaque;
    }

    private float getBatteryClockAlpha(int i) {
        if (isLightsOut(i)) {
            return 0.5f;
        }
        return getNonBatteryClockAlphaFor(i);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.BarTransitions
    public void onTransition(int i, int i2, boolean z) {
        super.onTransition(i, i2, z);
        applyMode(i2, z);
    }

    private void applyMode(int i, boolean z) {
        if (this.mLeftSide != null) {
            float nonBatteryClockAlphaFor = getNonBatteryClockAlphaFor(i);
            float batteryClockAlpha = getBatteryClockAlpha(i);
            Animator animator = this.mCurrentAnimation;
            if (animator != null) {
                animator.cancel();
            }
            if (z) {
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animateTransitionTo(this.mLeftSide, nonBatteryClockAlphaFor), animateTransitionTo(this.mStatusIcons, nonBatteryClockAlphaFor), animateTransitionTo(this.mBattery, batteryClockAlpha));
                if (isLightsOut(i)) {
                    animatorSet.setDuration(1500L);
                }
                animatorSet.start();
                this.mCurrentAnimation = animatorSet;
                return;
            }
            this.mLeftSide.setAlpha(nonBatteryClockAlphaFor);
            this.mStatusIcons.setAlpha(nonBatteryClockAlphaFor);
            this.mBattery.setAlpha(batteryClockAlpha);
        }
    }
}
