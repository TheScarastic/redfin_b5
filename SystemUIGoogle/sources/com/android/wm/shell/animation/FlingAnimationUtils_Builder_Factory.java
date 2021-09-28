package com.android.wm.shell.animation;

import android.util.DisplayMetrics;
import com.android.wm.shell.animation.FlingAnimationUtils;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class FlingAnimationUtils_Builder_Factory implements Factory<FlingAnimationUtils.Builder> {
    private final Provider<DisplayMetrics> displayMetricsProvider;

    public FlingAnimationUtils_Builder_Factory(Provider<DisplayMetrics> provider) {
        this.displayMetricsProvider = provider;
    }

    @Override // javax.inject.Provider
    public FlingAnimationUtils.Builder get() {
        return newInstance(this.displayMetricsProvider.get());
    }

    public static FlingAnimationUtils_Builder_Factory create(Provider<DisplayMetrics> provider) {
        return new FlingAnimationUtils_Builder_Factory(provider);
    }

    public static FlingAnimationUtils.Builder newInstance(DisplayMetrics displayMetrics) {
        return new FlingAnimationUtils.Builder(displayMetrics);
    }
}
