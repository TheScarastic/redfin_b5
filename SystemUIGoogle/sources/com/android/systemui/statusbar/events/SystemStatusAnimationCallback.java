package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.animation.ValueAnimator;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SystemStatusAnimationScheduler.kt */
/* loaded from: classes.dex */
public interface SystemStatusAnimationCallback {
    default Animator onHidePersistentDot() {
        return null;
    }

    default void onSystemChromeAnimationEnd() {
    }

    default void onSystemChromeAnimationStart() {
    }

    default void onSystemChromeAnimationUpdate(ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(valueAnimator, "animator");
    }

    default Animator onSystemStatusAnimationTransitionToPersistentDot(String str) {
        return null;
    }
}
