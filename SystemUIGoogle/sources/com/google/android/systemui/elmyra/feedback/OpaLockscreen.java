package com.google.android.systemui.elmyra.feedback;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.phone.KeyguardBottomAreaView;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
/* loaded from: classes2.dex */
public class OpaLockscreen implements FeedbackEffect {
    private static final Interpolator LOCK_ICON_HIDE_INTERPOLATOR = new DecelerateInterpolator();
    private static final Interpolator LOCK_ICON_SHOW_INTERPOLATOR = new AccelerateInterpolator();
    private KeyguardBottomAreaView mKeyguardBottomAreaView;
    private final KeyguardStateController mKeyguardStateController;
    private FeedbackEffect mLockscreenOpaLayout;
    private final StatusBar mStatusBar;

    public OpaLockscreen(StatusBar statusBar, KeyguardStateController keyguardStateController) {
        this.mStatusBar = statusBar;
        this.mKeyguardStateController = keyguardStateController;
        refreshLockscreenOpaLayout();
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onProgress(float f, int i) {
        refreshLockscreenOpaLayout();
        FeedbackEffect feedbackEffect = this.mLockscreenOpaLayout;
        if (feedbackEffect != null) {
            feedbackEffect.onProgress(f, i);
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onRelease() {
        refreshLockscreenOpaLayout();
        FeedbackEffect feedbackEffect = this.mLockscreenOpaLayout;
        if (feedbackEffect != null) {
            feedbackEffect.onRelease();
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        refreshLockscreenOpaLayout();
        FeedbackEffect feedbackEffect = this.mLockscreenOpaLayout;
        if (feedbackEffect != null) {
            feedbackEffect.onResolve(detectionProperties);
        }
    }

    private void refreshLockscreenOpaLayout() {
        if (this.mStatusBar.getKeyguardBottomAreaView() == null || !this.mKeyguardStateController.isShowing()) {
            this.mKeyguardBottomAreaView = null;
            this.mLockscreenOpaLayout = null;
            return;
        }
        KeyguardBottomAreaView keyguardBottomAreaView = this.mStatusBar.getKeyguardBottomAreaView();
        if (this.mLockscreenOpaLayout == null || !keyguardBottomAreaView.equals(this.mKeyguardBottomAreaView)) {
            this.mKeyguardBottomAreaView = keyguardBottomAreaView;
            FeedbackEffect feedbackEffect = this.mLockscreenOpaLayout;
            if (feedbackEffect != null) {
                feedbackEffect.onRelease();
            }
            this.mLockscreenOpaLayout = (FeedbackEffect) keyguardBottomAreaView.findViewById(R$id.lockscreen_opa);
        }
    }
}
