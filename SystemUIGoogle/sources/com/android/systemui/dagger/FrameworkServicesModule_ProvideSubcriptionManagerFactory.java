package com.android.systemui.dagger;

import android.content.Context;
import android.telephony.SubscriptionManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideSubcriptionManagerFactory implements Factory<SubscriptionManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideSubcriptionManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public SubscriptionManager get() {
        return provideSubcriptionManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideSubcriptionManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideSubcriptionManagerFactory(provider);
    }

    public static SubscriptionManager provideSubcriptionManager(Context context) {
        return (SubscriptionManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideSubcriptionManager(context));
    }
}
