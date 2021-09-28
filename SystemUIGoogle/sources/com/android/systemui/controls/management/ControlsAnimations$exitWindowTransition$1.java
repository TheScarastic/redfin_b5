package com.android.systemui.controls.management;

import android.animation.Animator;
import android.view.View;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ControlsAnimations.kt */
/* loaded from: classes.dex */
final class ControlsAnimations$exitWindowTransition$1 extends Lambda implements Function1<View, Animator> {
    public static final ControlsAnimations$exitWindowTransition$1 INSTANCE = new ControlsAnimations$exitWindowTransition$1();

    ControlsAnimations$exitWindowTransition$1() {
        super(1);
    }

    public final Animator invoke(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
        return ControlsAnimations.exitAnimation$default(view, null, 2, null);
    }
}
