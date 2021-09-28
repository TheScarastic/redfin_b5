package com.android.systemui.statusbar;

import android.view.animation.Interpolator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.LightRevealEffect;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LightRevealScrim.kt */
/* loaded from: classes.dex */
public final class LiftReveal implements LightRevealEffect {
    public static final LiftReveal INSTANCE = new LiftReveal();
    private static final Interpolator INTERPOLATOR = Interpolators.FAST_OUT_SLOW_IN_REVERSE;

    private LiftReveal() {
    }

    @Override // com.android.systemui.statusbar.LightRevealEffect
    public void setRevealAmountOnScrim(float f, LightRevealScrim lightRevealScrim) {
        Intrinsics.checkNotNullParameter(lightRevealScrim, "scrim");
        float interpolation = INTERPOLATOR.getInterpolation(f);
        LightRevealEffect.Companion companion = LightRevealEffect.Companion;
        float percentPastThreshold = companion.getPercentPastThreshold(interpolation, 0.35f);
        lightRevealScrim.setRevealGradientEndColorAlpha(1.0f - companion.getPercentPastThreshold(f, 0.85f));
        lightRevealScrim.setRevealGradientBounds((((float) lightRevealScrim.getWidth()) * 0.25f) + (((float) (-lightRevealScrim.getWidth())) * percentPastThreshold), (((float) lightRevealScrim.getHeight()) * 1.1f) - (((float) lightRevealScrim.getHeight()) * interpolation), (((float) lightRevealScrim.getWidth()) * 0.75f) + (((float) lightRevealScrim.getWidth()) * percentPastThreshold), (((float) lightRevealScrim.getHeight()) * 1.2f) + (((float) lightRevealScrim.getHeight()) * interpolation));
    }
}
