package com.google.android.systemui.columbus;

import android.app.IActivityManager;
import android.os.Handler;
import com.google.android.systemui.columbus.ColumbusContentObserver;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ColumbusContentObserver_Factory_Factory implements Factory<ColumbusContentObserver.Factory> {
    private final Provider<IActivityManager> activityManagerServiceProvider;
    private final Provider<ContentResolverWrapper> contentResolverProvider;
    private final Provider<Handler> handlerProvider;

    public ColumbusContentObserver_Factory_Factory(Provider<ContentResolverWrapper> provider, Provider<IActivityManager> provider2, Provider<Handler> provider3) {
        this.contentResolverProvider = provider;
        this.activityManagerServiceProvider = provider2;
        this.handlerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public ColumbusContentObserver.Factory get() {
        return newInstance(this.contentResolverProvider.get(), this.activityManagerServiceProvider.get(), this.handlerProvider.get());
    }

    public static ColumbusContentObserver_Factory_Factory create(Provider<ContentResolverWrapper> provider, Provider<IActivityManager> provider2, Provider<Handler> provider3) {
        return new ColumbusContentObserver_Factory_Factory(provider, provider2, provider3);
    }

    public static ColumbusContentObserver.Factory newInstance(ContentResolverWrapper contentResolverWrapper, IActivityManager iActivityManager, Handler handler) {
        return new ColumbusContentObserver.Factory(contentResolverWrapper, iActivityManager, handler);
    }
}
