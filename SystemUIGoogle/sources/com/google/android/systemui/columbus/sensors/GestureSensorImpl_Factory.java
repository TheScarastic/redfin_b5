package com.google.android.systemui.columbus.sensors;

import android.content.Context;
import android.os.Handler;
import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class GestureSensorImpl_Factory implements Factory<GestureSensorImpl> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public GestureSensorImpl_Factory(Provider<Context> provider, Provider<UiEventLogger> provider2, Provider<Handler> provider3) {
        this.contextProvider = provider;
        this.uiEventLoggerProvider = provider2;
        this.handlerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public GestureSensorImpl get() {
        return newInstance(this.contextProvider.get(), this.uiEventLoggerProvider.get(), this.handlerProvider.get());
    }

    public static GestureSensorImpl_Factory create(Provider<Context> provider, Provider<UiEventLogger> provider2, Provider<Handler> provider3) {
        return new GestureSensorImpl_Factory(provider, provider2, provider3);
    }

    public static GestureSensorImpl newInstance(Context context, UiEventLogger uiEventLogger, Handler handler) {
        return new GestureSensorImpl(context, uiEventLogger, handler);
    }
}
