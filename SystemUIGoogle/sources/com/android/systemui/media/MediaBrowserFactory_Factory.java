package com.android.systemui.media;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaBrowserFactory_Factory implements Factory<MediaBrowserFactory> {
    private final Provider<Context> contextProvider;

    public MediaBrowserFactory_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public MediaBrowserFactory get() {
        return newInstance(this.contextProvider.get());
    }

    public static MediaBrowserFactory_Factory create(Provider<Context> provider) {
        return new MediaBrowserFactory_Factory(provider);
    }

    public static MediaBrowserFactory newInstance(Context context) {
        return new MediaBrowserFactory(context);
    }
}
