package com.google.android.systemui.columbus.feedback;

import android.os.Vibrator;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class HapticClick_Factory implements Factory<HapticClick> {
    private final Provider<Vibrator> vibratorProvider;

    public HapticClick_Factory(Provider<Vibrator> provider) {
        this.vibratorProvider = provider;
    }

    @Override // javax.inject.Provider
    public HapticClick get() {
        return newInstance(DoubleCheck.lazy(this.vibratorProvider));
    }

    public static HapticClick_Factory create(Provider<Vibrator> provider) {
        return new HapticClick_Factory(provider);
    }

    public static HapticClick newInstance(Lazy<Vibrator> lazy) {
        return new HapticClick(lazy);
    }
}
