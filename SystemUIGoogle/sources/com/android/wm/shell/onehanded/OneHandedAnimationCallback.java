package com.android.wm.shell.onehanded;

import android.view.SurfaceControl;
import com.android.wm.shell.onehanded.OneHandedAnimationController;
/* loaded from: classes2.dex */
public interface OneHandedAnimationCallback {
    default void onAnimationUpdate(SurfaceControl.Transaction transaction, float f, float f2) {
    }

    default void onOneHandedAnimationCancel(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
    }

    default void onOneHandedAnimationEnd(SurfaceControl.Transaction transaction, OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
    }

    default void onOneHandedAnimationStart(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
    }
}
