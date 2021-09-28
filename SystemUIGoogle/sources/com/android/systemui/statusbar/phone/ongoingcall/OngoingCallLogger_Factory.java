package com.android.systemui.statusbar.phone.ongoingcall;

import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class OngoingCallLogger_Factory implements Factory<OngoingCallLogger> {
    private final Provider<UiEventLogger> loggerProvider;

    public OngoingCallLogger_Factory(Provider<UiEventLogger> provider) {
        this.loggerProvider = provider;
    }

    @Override // javax.inject.Provider
    public OngoingCallLogger get() {
        return newInstance(this.loggerProvider.get());
    }

    public static OngoingCallLogger_Factory create(Provider<UiEventLogger> provider) {
        return new OngoingCallLogger_Factory(provider);
    }

    public static OngoingCallLogger newInstance(UiEventLogger uiEventLogger) {
        return new OngoingCallLogger(uiEventLogger);
    }
}
