package com.google.android.systemui.assist;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class OpaEnabledSettings_Factory implements Factory<OpaEnabledSettings> {
    private final Provider<Context> contextProvider;

    public OpaEnabledSettings_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public OpaEnabledSettings get() {
        return newInstance(this.contextProvider.get());
    }

    public static OpaEnabledSettings_Factory create(Provider<Context> provider) {
        return new OpaEnabledSettings_Factory(provider);
    }

    public static OpaEnabledSettings newInstance(Context context) {
        return new OpaEnabledSettings(context);
    }
}
