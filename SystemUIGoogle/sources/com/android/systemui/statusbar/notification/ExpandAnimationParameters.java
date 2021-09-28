package com.android.systemui.statusbar.notification;

import android.util.MathUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.Interpolators;
/* compiled from: ExpandAnimationParameters.kt */
/* loaded from: classes.dex */
public final class ExpandAnimationParameters extends ActivityLaunchAnimator.State {
    private float linearProgress;
    private int parentStartClipTopAmount;
    private int parentStartRoundedTopClipping;
    private float progress;
    private int startClipTopAmount;
    private float startNotificationTop;
    private int startRoundedTopClipping;
    private float startTranslationZ;

    public ExpandAnimationParameters(int i, int i2, int i3, int i4, float f, float f2) {
        super(i, i2, i3, i4, f, f2);
    }

    @VisibleForTesting
    public ExpandAnimationParameters() {
        this(0, 0, 0, 0, 0.0f, 0.0f);
    }

    public final float getStartTranslationZ() {
        return this.startTranslationZ;
    }

    public final void setStartTranslationZ(float f) {
        this.startTranslationZ = f;
    }

    public final float getStartNotificationTop() {
        return this.startNotificationTop;
    }

    public final void setStartNotificationTop(float f) {
        this.startNotificationTop = f;
    }

    public final int getStartClipTopAmount() {
        return this.startClipTopAmount;
    }

    public final void setStartClipTopAmount(int i) {
        this.startClipTopAmount = i;
    }

    public final int getParentStartClipTopAmount() {
        return this.parentStartClipTopAmount;
    }

    public final void setParentStartClipTopAmount(int i) {
        this.parentStartClipTopAmount = i;
    }

    public final float getProgress() {
        return this.progress;
    }

    public final void setProgress(float f) {
        this.progress = f;
    }

    public final void setLinearProgress(float f) {
        this.linearProgress = f;
    }

    public final int getStartRoundedTopClipping() {
        return this.startRoundedTopClipping;
    }

    public final void setStartRoundedTopClipping(int i) {
        this.startRoundedTopClipping = i;
    }

    public final int getParentStartRoundedTopClipping() {
        return this.parentStartRoundedTopClipping;
    }

    public final void setParentStartRoundedTopClipping(int i) {
        this.parentStartRoundedTopClipping = i;
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.State
    public int getTopChange() {
        int i = this.startClipTopAmount;
        return Math.min(super.getTopChange() - (!((((float) i) > 0.0f ? 1 : (((float) i) == 0.0f ? 0 : -1)) == 0) ? (int) MathUtils.lerp(0.0f, (float) i, Interpolators.FAST_OUT_SLOW_IN.getInterpolation(this.linearProgress)) : 0), 0);
    }

    public final float getProgress(long j, long j2) {
        return ActivityLaunchAnimator.Companion.getProgress(this.linearProgress, j, j2);
    }
}
