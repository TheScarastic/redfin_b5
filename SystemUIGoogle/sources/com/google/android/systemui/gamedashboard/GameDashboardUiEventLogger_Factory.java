package com.google.android.systemui.gamedashboard;

import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class GameDashboardUiEventLogger_Factory implements Factory<GameDashboardUiEventLogger> {
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public GameDashboardUiEventLogger_Factory(Provider<UiEventLogger> provider) {
        this.uiEventLoggerProvider = provider;
    }

    @Override // javax.inject.Provider
    public GameDashboardUiEventLogger get() {
        return newInstance(this.uiEventLoggerProvider.get());
    }

    public static GameDashboardUiEventLogger_Factory create(Provider<UiEventLogger> provider) {
        return new GameDashboardUiEventLogger_Factory(provider);
    }

    public static GameDashboardUiEventLogger newInstance(UiEventLogger uiEventLogger) {
        return new GameDashboardUiEventLogger(uiEventLogger);
    }
}
