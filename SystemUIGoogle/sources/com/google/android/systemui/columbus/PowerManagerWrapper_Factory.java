package com.google.android.systemui.columbus;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class PowerManagerWrapper_Factory implements Factory<PowerManagerWrapper> {
    private final Provider<Context> contextProvider;

    public PowerManagerWrapper_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public PowerManagerWrapper get() {
        return newInstance(this.contextProvider.get());
    }

    public static PowerManagerWrapper_Factory create(Provider<Context> provider) {
        return new PowerManagerWrapper_Factory(provider);
    }

    public static PowerManagerWrapper newInstance(Context context) {
        return new PowerManagerWrapper(context);
    }
}
