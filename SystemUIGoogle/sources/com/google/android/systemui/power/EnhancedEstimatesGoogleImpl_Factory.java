package com.google.android.systemui.power;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class EnhancedEstimatesGoogleImpl_Factory implements Factory<EnhancedEstimatesGoogleImpl> {
    private final Provider<Context> contextProvider;

    public EnhancedEstimatesGoogleImpl_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public EnhancedEstimatesGoogleImpl get() {
        return newInstance(this.contextProvider.get());
    }

    public static EnhancedEstimatesGoogleImpl_Factory create(Provider<Context> provider) {
        return new EnhancedEstimatesGoogleImpl_Factory(provider);
    }

    public static EnhancedEstimatesGoogleImpl newInstance(Context context) {
        return new EnhancedEstimatesGoogleImpl(context);
    }
}
