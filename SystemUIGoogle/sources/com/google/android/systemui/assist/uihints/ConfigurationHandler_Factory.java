package com.google.android.systemui.assist.uihints;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ConfigurationHandler_Factory implements Factory<ConfigurationHandler> {
    private final Provider<Context> contextProvider;

    public ConfigurationHandler_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public ConfigurationHandler get() {
        return newInstance(this.contextProvider.get());
    }

    public static ConfigurationHandler_Factory create(Provider<Context> provider) {
        return new ConfigurationHandler_Factory(provider);
    }

    public static ConfigurationHandler newInstance(Context context) {
        return new ConfigurationHandler(context);
    }
}
