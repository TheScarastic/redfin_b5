package com.google.android.systemui.columbus;

import android.content.Context;
import com.google.android.systemui.columbus.ColumbusContentObserver;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ColumbusSettings_Factory implements Factory<ColumbusSettings> {
    private final Provider<ColumbusContentObserver.Factory> contentObserverFactoryProvider;
    private final Provider<Context> contextProvider;

    public ColumbusSettings_Factory(Provider<Context> provider, Provider<ColumbusContentObserver.Factory> provider2) {
        this.contextProvider = provider;
        this.contentObserverFactoryProvider = provider2;
    }

    @Override // javax.inject.Provider
    public ColumbusSettings get() {
        return newInstance(this.contextProvider.get(), this.contentObserverFactoryProvider.get());
    }

    public static ColumbusSettings_Factory create(Provider<Context> provider, Provider<ColumbusContentObserver.Factory> provider2) {
        return new ColumbusSettings_Factory(provider, provider2);
    }

    public static ColumbusSettings newInstance(Context context, ColumbusContentObserver.Factory factory) {
        return new ColumbusSettings(context, factory);
    }
}
