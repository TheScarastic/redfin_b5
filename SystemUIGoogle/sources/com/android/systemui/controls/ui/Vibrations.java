package com.android.systemui.controls.ui;

import android.os.VibrationEffect;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Vibrations.kt */
/* loaded from: classes.dex */
public final class Vibrations {
    public static final Vibrations INSTANCE;
    private static final VibrationEffect rangeEdgeEffect;
    private static final VibrationEffect rangeMiddleEffect;

    private Vibrations() {
    }

    static {
        Vibrations vibrations = new Vibrations();
        INSTANCE = vibrations;
        rangeEdgeEffect = vibrations.initRangeEdgeEffect();
        rangeMiddleEffect = vibrations.initRangeMiddleEffect();
    }

    public final VibrationEffect getRangeEdgeEffect() {
        return rangeEdgeEffect;
    }

    public final VibrationEffect getRangeMiddleEffect() {
        return rangeMiddleEffect;
    }

    private final VibrationEffect initRangeEdgeEffect() {
        VibrationEffect.Composition startComposition = VibrationEffect.startComposition();
        startComposition.addPrimitive(7, 0.5f);
        VibrationEffect compose = startComposition.compose();
        Intrinsics.checkNotNullExpressionValue(compose, "composition.compose()");
        return compose;
    }

    private final VibrationEffect initRangeMiddleEffect() {
        VibrationEffect.Composition startComposition = VibrationEffect.startComposition();
        startComposition.addPrimitive(7, 0.1f);
        VibrationEffect compose = startComposition.compose();
        Intrinsics.checkNotNullExpressionValue(compose, "composition.compose()");
        return compose;
    }
}
