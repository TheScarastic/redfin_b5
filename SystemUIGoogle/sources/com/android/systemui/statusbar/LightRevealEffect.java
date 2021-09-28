package com.android.systemui.statusbar;

import kotlin.ranges.RangesKt___RangesKt;
/* compiled from: LightRevealScrim.kt */
/* loaded from: classes.dex */
public interface LightRevealEffect {
    public static final Companion Companion = Companion.$$INSTANCE;

    void setRevealAmountOnScrim(float f, LightRevealScrim lightRevealScrim);

    /* compiled from: LightRevealScrim.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();

        private Companion() {
        }

        public final float getPercentPastThreshold(float f, float f2) {
            return RangesKt___RangesKt.coerceAtLeast(f - f2, 0.0f) * (1.0f / (1.0f - f2));
        }
    }
}
