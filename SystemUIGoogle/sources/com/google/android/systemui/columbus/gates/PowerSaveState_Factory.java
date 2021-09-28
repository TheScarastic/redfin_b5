package com.google.android.systemui.columbus.gates;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class PowerSaveState_Factory implements Factory<PowerSaveState> {
    private final Provider<Context> contextProvider;

    public PowerSaveState_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public PowerSaveState get() {
        return newInstance(this.contextProvider.get());
    }

    public static PowerSaveState_Factory create(Provider<Context> provider) {
        return new PowerSaveState_Factory(provider);
    }

    public static PowerSaveState newInstance(Context context) {
        return new PowerSaveState(context);
    }
}
