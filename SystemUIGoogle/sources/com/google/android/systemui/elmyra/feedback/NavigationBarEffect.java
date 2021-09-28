package com.google.android.systemui.elmyra.feedback;

import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public abstract class NavigationBarEffect implements FeedbackEffect, NavigationModeController.ModeChangedListener {
    private final List<FeedbackEffect> mFeedbackEffects = new ArrayList();
    private int mNavMode;
    private final StatusBar mStatusBar;

    protected abstract List<FeedbackEffect> findFeedbackEffects(NavigationBarView navigationBarView);

    protected abstract boolean isActiveFeedbackEffect(FeedbackEffect feedbackEffect);

    protected abstract boolean validateFeedbackEffects(List<FeedbackEffect> list);

    public NavigationBarEffect(StatusBar statusBar, NavigationModeController navigationModeController) {
        this.mStatusBar = statusBar;
        this.mNavMode = navigationModeController.addListener(this);
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onProgress(float f, int i) {
        refreshFeedbackEffects();
        for (int i2 = 0; i2 < this.mFeedbackEffects.size(); i2++) {
            FeedbackEffect feedbackEffect = this.mFeedbackEffects.get(i2);
            if (isActiveFeedbackEffect(feedbackEffect)) {
                feedbackEffect.onProgress(f, i);
            }
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onRelease() {
        refreshFeedbackEffects();
        for (int i = 0; i < this.mFeedbackEffects.size(); i++) {
            this.mFeedbackEffects.get(i).onRelease();
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        refreshFeedbackEffects();
        for (int i = 0; i < this.mFeedbackEffects.size(); i++) {
            this.mFeedbackEffects.get(i).onResolve(detectionProperties);
        }
    }

    /* access modifiers changed from: protected */
    public void reset() {
        this.mFeedbackEffects.clear();
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public void onNavigationModeChanged(int i) {
        this.mNavMode = i;
        refreshFeedbackEffects();
    }

    private void refreshFeedbackEffects() {
        NavigationBarView navigationBarView = this.mStatusBar.getNavigationBarView();
        if (navigationBarView == null || !isAllowed()) {
            reset();
            return;
        }
        if (!validateFeedbackEffects(this.mFeedbackEffects)) {
            this.mFeedbackEffects.clear();
        }
        if (this.mFeedbackEffects.isEmpty()) {
            this.mFeedbackEffects.addAll(findFeedbackEffects(navigationBarView));
        }
    }

    private boolean isAllowed() {
        return !QuickStepContract.isGesturalMode(this.mNavMode);
    }
}
