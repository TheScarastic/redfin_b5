package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.media.AudioManager;
import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ManageMedia_Factory implements Factory<ManageMedia> {
    private final Provider<AudioManager> audioManagerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public ManageMedia_Factory(Provider<Context> provider, Provider<AudioManager> provider2, Provider<UiEventLogger> provider3) {
        this.contextProvider = provider;
        this.audioManagerProvider = provider2;
        this.uiEventLoggerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public ManageMedia get() {
        return newInstance(this.contextProvider.get(), this.audioManagerProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static ManageMedia_Factory create(Provider<Context> provider, Provider<AudioManager> provider2, Provider<UiEventLogger> provider3) {
        return new ManageMedia_Factory(provider, provider2, provider3);
    }

    public static ManageMedia newInstance(Context context, AudioManager audioManager, UiEventLogger uiEventLogger) {
        return new ManageMedia(context, audioManager, uiEventLogger);
    }
}
