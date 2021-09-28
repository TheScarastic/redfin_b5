package com.android.systemui.controls.management;

import android.animation.Animator;
import android.view.View;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ControlsAnimations.kt */
/* loaded from: classes.dex */
final class ControlsAnimations$enterWindowTransition$1 extends Lambda implements Function1<View, Animator> {
    public static final ControlsAnimations$enterWindowTransition$1 INSTANCE = new ControlsAnimations$enterWindowTransition$1();

    ControlsAnimations$enterWindowTransition$1() {
        super(1);
    }

    public final Animator invoke(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        return ControlsAnimations.INSTANCE.enterAnimation(view);
    }
}
