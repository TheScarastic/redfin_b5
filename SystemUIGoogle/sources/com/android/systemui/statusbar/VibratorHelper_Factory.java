package com.android.systemui.statusbar;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class VibratorHelper_Factory implements Factory<VibratorHelper> {
    private final Provider<Context> contextProvider;

    public VibratorHelper_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public VibratorHelper get() {
        return newInstance(this.contextProvider.get());
    }

    public static VibratorHelper_Factory create(Provider<Context> provider) {
        return new VibratorHelper_Factory(provider);
    }

    public static VibratorHelper newInstance(Context context) {
        return new VibratorHelper(context);
    }
}
