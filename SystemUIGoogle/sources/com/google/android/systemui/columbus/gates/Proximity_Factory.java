package com.google.android.systemui.columbus.gates;

import android.content.Context;
import com.android.systemui.util.sensors.ProximitySensor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class Proximity_Factory implements Factory<Proximity> {
    private final Provider<Context> contextProvider;
    private final Provider<ProximitySensor> proximitySensorProvider;

    public Proximity_Factory(Provider<Context> provider, Provider<ProximitySensor> provider2) {
        this.contextProvider = provider;
        this.proximitySensorProvider = provider2;
    }

    @Override // javax.inject.Provider
    public Proximity get() {
        return newInstance(this.contextProvider.get(), this.proximitySensorProvider.get());
    }

    public static Proximity_Factory create(Provider<Context> provider, Provider<ProximitySensor> provider2) {
        return new Proximity_Factory(provider, provider2);
    }

    public static Proximity newInstance(Context context, ProximitySensor proximitySensor) {
        return new Proximity(context, proximitySensor);
    }
}
