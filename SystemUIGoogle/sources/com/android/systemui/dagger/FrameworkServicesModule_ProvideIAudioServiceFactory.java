package com.android.systemui.dagger;

import android.media.IAudioService;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIAudioServiceFactory implements Factory<IAudioService> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideIAudioServiceFactory INSTANCE = new FrameworkServicesModule_ProvideIAudioServiceFactory();
    }

    @Override // javax.inject.Provider
    public IAudioService get() {
        return provideIAudioService();
    }

    public static FrameworkServicesModule_ProvideIAudioServiceFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IAudioService provideIAudioService() {
        return (IAudioService) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIAudioService());
    }
}
