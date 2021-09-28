package com.google.android.systemui.columbus.actions;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SettingsAction_Factory implements Factory<SettingsAction> {
    private final Provider<Context> contextProvider;
    private final Provider<StatusBar> statusBarProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public SettingsAction_Factory(Provider<Context> provider, Provider<StatusBar> provider2, Provider<UiEventLogger> provider3) {
        this.contextProvider = provider;
        this.statusBarProvider = provider2;
        this.uiEventLoggerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public SettingsAction get() {
        return newInstance(this.contextProvider.get(), this.statusBarProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static SettingsAction_Factory create(Provider<Context> provider, Provider<StatusBar> provider2, Provider<UiEventLogger> provider3) {
        return new SettingsAction_Factory(provider, provider2, provider3);
    }

    public static SettingsAction newInstance(Context context, StatusBar statusBar, UiEventLogger uiEventLogger) {
        return new SettingsAction(context, statusBar, uiEventLogger);
    }
}
