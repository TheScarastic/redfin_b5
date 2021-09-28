package com.android.systemui.media;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ResumeMediaBrowserFactory_Factory implements Factory<ResumeMediaBrowserFactory> {
    private final Provider<MediaBrowserFactory> browserFactoryProvider;
    private final Provider<Context> contextProvider;

    public ResumeMediaBrowserFactory_Factory(Provider<Context> provider, Provider<MediaBrowserFactory> provider2) {
        this.contextProvider = provider;
        this.browserFactoryProvider = provider2;
    }

    @Override // javax.inject.Provider
    public ResumeMediaBrowserFactory get() {
        return newInstance(this.contextProvider.get(), this.browserFactoryProvider.get());
    }

    public static ResumeMediaBrowserFactory_Factory create(Provider<Context> provider, Provider<MediaBrowserFactory> provider2) {
        return new ResumeMediaBrowserFactory_Factory(provider, provider2);
    }

    public static ResumeMediaBrowserFactory newInstance(Context context, MediaBrowserFactory mediaBrowserFactory) {
        return new ResumeMediaBrowserFactory(context, mediaBrowserFactory);
    }
}
