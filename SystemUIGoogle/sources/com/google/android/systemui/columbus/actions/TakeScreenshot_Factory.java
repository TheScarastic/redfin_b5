package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.os.Handler;
import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TakeScreenshot_Factory implements Factory<TakeScreenshot> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public TakeScreenshot_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<UiEventLogger> provider3) {
        this.contextProvider = provider;
        this.handlerProvider = provider2;
        this.uiEventLoggerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public TakeScreenshot get() {
        return newInstance(this.contextProvider.get(), this.handlerProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static TakeScreenshot_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<UiEventLogger> provider3) {
        return new TakeScreenshot_Factory(provider, provider2, provider3);
    }

    public static TakeScreenshot newInstance(Context context, Handler handler, UiEventLogger uiEventLogger) {
        return new TakeScreenshot(context, handler, uiEventLogger);
    }
}
