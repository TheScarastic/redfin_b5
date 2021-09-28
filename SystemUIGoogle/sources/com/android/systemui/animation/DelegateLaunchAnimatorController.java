package com.android.systemui.animation;

import android.view.ViewGroup;
import com.android.systemui.animation.ActivityLaunchAnimator;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DelegateLaunchAnimatorController.kt */
/* loaded from: classes.dex */
public class DelegateLaunchAnimatorController implements ActivityLaunchAnimator.Controller {
    private final ActivityLaunchAnimator.Controller delegate;

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public ActivityLaunchAnimator.State createAnimatorState() {
        return this.delegate.createAnimatorState();
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public ViewGroup getLaunchContainer() {
        return this.delegate.getLaunchContainer();
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onIntentStarted(boolean z) {
        this.delegate.onIntentStarted(z);
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationCancelled() {
        this.delegate.onLaunchAnimationCancelled();
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationProgress(ActivityLaunchAnimator.State state, float f, float f2) {
        Intrinsics.checkNotNullParameter(state, "state");
        this.delegate.onLaunchAnimationProgress(state, f, f2);
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void setLaunchContainer(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "<set-?>");
        this.delegate.setLaunchContainer(viewGroup);
    }

    public DelegateLaunchAnimatorController(ActivityLaunchAnimator.Controller controller) {
        Intrinsics.checkNotNullParameter(controller, "delegate");
        this.delegate = controller;
    }

    /* access modifiers changed from: protected */
    public final ActivityLaunchAnimator.Controller getDelegate() {
        return this.delegate;
    }
}
