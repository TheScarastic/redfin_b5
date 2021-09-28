package com.android.systemui.statusbar.phone;

import android.view.ViewGroup;
import com.android.systemui.animation.ActivityLaunchAnimator;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StatusBarLaunchAnimatorController.kt */
/* loaded from: classes.dex */
public final class StatusBarLaunchAnimatorController implements ActivityLaunchAnimator.Controller {
    private final ActivityLaunchAnimator.Controller delegate;
    private final boolean isLaunchForActivity;
    private final StatusBar statusBar;

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public ActivityLaunchAnimator.State createAnimatorState() {
        return this.delegate.createAnimatorState();
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public ViewGroup getLaunchContainer() {
        return this.delegate.getLaunchContainer();
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void setLaunchContainer(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "<set-?>");
        this.delegate.setLaunchContainer(viewGroup);
    }

    public StatusBarLaunchAnimatorController(ActivityLaunchAnimator.Controller controller, StatusBar statusBar, boolean z) {
        Intrinsics.checkNotNullParameter(controller, "delegate");
        Intrinsics.checkNotNullParameter(statusBar, "statusBar");
        this.delegate = controller;
        this.statusBar = statusBar;
        this.isLaunchForActivity = z;
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onIntentStarted(boolean z) {
        this.delegate.onIntentStarted(z);
        if (!z) {
            this.statusBar.collapsePanelOnMainThread();
        }
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationStart(boolean z) {
        this.delegate.onLaunchAnimationStart(z);
        this.statusBar.getNotificationPanelViewController().setIsLaunchAnimationRunning(true);
        if (!z) {
            this.statusBar.collapsePanelWithDuration(500);
        }
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationEnd(boolean z) {
        this.delegate.onLaunchAnimationEnd(z);
        this.statusBar.getNotificationPanelViewController().setIsLaunchAnimationRunning(false);
        this.statusBar.onLaunchAnimationEnd(z);
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationProgress(ActivityLaunchAnimator.State state, float f, float f2) {
        Intrinsics.checkNotNullParameter(state, "state");
        this.delegate.onLaunchAnimationProgress(state, f, f2);
        this.statusBar.getNotificationPanelViewController().applyLaunchAnimationProgress(f2);
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationCancelled() {
        this.delegate.onLaunchAnimationCancelled();
        this.statusBar.onLaunchAnimationCancelled(this.isLaunchForActivity);
    }
}
