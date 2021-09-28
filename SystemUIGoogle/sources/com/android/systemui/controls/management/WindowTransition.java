package com.android.systemui.controls.management;

import android.animation.Animator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsAnimations.kt */
/* loaded from: classes.dex */
public final class WindowTransition extends Transition {
    private final Function1<View, Animator> animator;

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: kotlin.jvm.functions.Function1<? super android.view.View, ? extends android.animation.Animator> */
    /* JADX WARN: Multi-variable type inference failed */
    public WindowTransition(Function1<? super View, ? extends Animator> function1) {
        Intrinsics.checkNotNullParameter(function1, "animator");
        this.animator = function1;
    }

    @Override // android.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        Intrinsics.checkNotNullParameter(transitionValues, "tv");
        Map map = transitionValues.values;
        Intrinsics.checkNotNullExpressionValue(map, "tv.values");
        map.put("item", Float.valueOf(0.0f));
    }

    @Override // android.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        Intrinsics.checkNotNullParameter(transitionValues, "tv");
        Map map = transitionValues.values;
        Intrinsics.checkNotNullExpressionValue(map, "tv.values");
        map.put("item", Float.valueOf(1.0f));
    }

    @Override // android.transition.Transition
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        Intrinsics.checkNotNullParameter(viewGroup, "sceneRoot");
        Function1<View, Animator> function1 = this.animator;
        Intrinsics.checkNotNull(transitionValues);
        View view = transitionValues.view;
        Intrinsics.checkNotNullExpressionValue(view, "!!.view");
        return function1.invoke(view);
    }
}
