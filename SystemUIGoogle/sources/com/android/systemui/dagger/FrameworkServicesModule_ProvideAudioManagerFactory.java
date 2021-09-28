package com.android.systemui.dagger;

import android.content.Context;
import android.media.AudioManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideAudioManagerFactory implements Factory<AudioManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideAudioManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public AudioManager get() {
        return provideAudioManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideAudioManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideAudioManagerFactory(provider);
    }

    public static AudioManager provideAudioManager(Context context) {
        return (AudioManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideAudioManager(context));
    }
}
