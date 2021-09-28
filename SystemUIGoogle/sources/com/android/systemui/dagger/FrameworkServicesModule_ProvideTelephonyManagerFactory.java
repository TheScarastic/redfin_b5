package com.android.systemui.dagger;

import android.content.Context;
import android.telephony.TelephonyManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideTelephonyManagerFactory implements Factory<TelephonyManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideTelephonyManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public TelephonyManager get() {
        return provideTelephonyManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideTelephonyManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideTelephonyManagerFactory(provider);
    }

    public static TelephonyManager provideTelephonyManager(Context context) {
        return (TelephonyManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideTelephonyManager(context));
    }
}
