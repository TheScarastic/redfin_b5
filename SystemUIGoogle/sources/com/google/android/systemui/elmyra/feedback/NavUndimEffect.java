package com.google.android.systemui.elmyra.feedback;

import com.android.systemui.Dependency;
import com.android.systemui.navigationbar.NavigationBarController;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
/* loaded from: classes2.dex */
public class NavUndimEffect implements FeedbackEffect {
    private final NavigationBarController mNavBarController = (NavigationBarController) Dependency.get(NavigationBarController.class);

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onProgress(float f, int i) {
        this.mNavBarController.touchAutoDim(0);
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onRelease() {
        this.mNavBarController.touchAutoDim(0);
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        this.mNavBarController.touchAutoDim(0);
    }
}
