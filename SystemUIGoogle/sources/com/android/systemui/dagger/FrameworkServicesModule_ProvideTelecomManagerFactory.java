package com.android.systemui.dagger;

import android.content.Context;
import android.telecom.TelecomManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideTelecomManagerFactory implements Factory<TelecomManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideTelecomManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public TelecomManager get() {
        return provideTelecomManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideTelecomManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideTelecomManagerFactory(provider);
    }

    public static TelecomManager provideTelecomManager(Context context) {
        return FrameworkServicesModule.provideTelecomManager(context);
    }
}
