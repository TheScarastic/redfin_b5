package com.android.systemui.dagger;

import android.content.Context;
import android.hardware.face.FaceManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideFaceManagerFactory implements Factory<FaceManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideFaceManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public FaceManager get() {
        return provideFaceManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideFaceManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideFaceManagerFactory(provider);
    }

    public static FaceManager provideFaceManager(Context context) {
        return FrameworkServicesModule.provideFaceManager(context);
    }
}
