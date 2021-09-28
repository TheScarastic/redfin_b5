package com.google.android.systemui.columbus.gates;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class KeyguardProximity_Factory implements Factory<KeyguardProximity> {
    private final Provider<Context> contextProvider;
    private final Provider<KeyguardVisibility> keyguardGateProvider;
    private final Provider<Proximity> proximityProvider;

    public KeyguardProximity_Factory(Provider<Context> provider, Provider<KeyguardVisibility> provider2, Provider<Proximity> provider3) {
        this.contextProvider = provider;
        this.keyguardGateProvider = provider2;
        this.proximityProvider = provider3;
    }

    @Override // javax.inject.Provider
    public KeyguardProximity get() {
        return newInstance(this.contextProvider.get(), this.keyguardGateProvider.get(), this.proximityProvider.get());
    }

    public static KeyguardProximity_Factory create(Provider<Context> provider, Provider<KeyguardVisibility> provider2, Provider<Proximity> provider3) {
        return new KeyguardProximity_Factory(provider, provider2, provider3);
    }

    public static KeyguardProximity newInstance(Context context, KeyguardVisibility keyguardVisibility, Proximity proximity) {
        return new KeyguardProximity(context, keyguardVisibility, proximity);
    }
}
