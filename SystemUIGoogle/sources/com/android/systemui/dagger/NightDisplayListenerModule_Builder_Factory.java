package com.android.systemui.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.dagger.NightDisplayListenerModule;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NightDisplayListenerModule_Builder_Factory implements Factory<NightDisplayListenerModule.Builder> {
    private final Provider<Handler> bgHandlerProvider;
    private final Provider<Context> contextProvider;

    public NightDisplayListenerModule_Builder_Factory(Provider<Context> provider, Provider<Handler> provider2) {
        this.contextProvider = provider;
        this.bgHandlerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public NightDisplayListenerModule.Builder get() {
        return newInstance(this.contextProvider.get(), this.bgHandlerProvider.get());
    }

    public static NightDisplayListenerModule_Builder_Factory create(Provider<Context> provider, Provider<Handler> provider2) {
        return new NightDisplayListenerModule_Builder_Factory(provider, provider2);
    }

    public static NightDisplayListenerModule.Builder newInstance(Context context, Handler handler) {
        return new NightDisplayListenerModule.Builder(context, handler);
    }
}
