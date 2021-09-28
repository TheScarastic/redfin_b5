package com.google.android.systemui.columbus.gates;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class VrMode_Factory implements Factory<VrMode> {
    private final Provider<Context> contextProvider;

    public VrMode_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public VrMode get() {
        return newInstance(this.contextProvider.get());
    }

    public static VrMode_Factory create(Provider<Context> provider) {
        return new VrMode_Factory(provider);
    }

    public static VrMode newInstance(Context context) {
        return new VrMode(context);
    }
}
