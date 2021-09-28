package com.android.systemui.statusbar;

import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarStateControllerImpl_Factory implements Factory<StatusBarStateControllerImpl> {
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public StatusBarStateControllerImpl_Factory(Provider<UiEventLogger> provider) {
        this.uiEventLoggerProvider = provider;
    }

    @Override // javax.inject.Provider
    public StatusBarStateControllerImpl get() {
        return newInstance(this.uiEventLoggerProvider.get());
    }

    public static StatusBarStateControllerImpl_Factory create(Provider<UiEventLogger> provider) {
        return new StatusBarStateControllerImpl_Factory(provider);
    }

    public static StatusBarStateControllerImpl newInstance(UiEventLogger uiEventLogger) {
        return new StatusBarStateControllerImpl(uiEventLogger);
    }
}
