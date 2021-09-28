package com.google.android.systemui.columbus.actions;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.recents.Recents;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class LaunchOverview_Factory implements Factory<LaunchOverview> {
    private final Provider<Context> contextProvider;
    private final Provider<Recents> recentsProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public LaunchOverview_Factory(Provider<Context> provider, Provider<Recents> provider2, Provider<UiEventLogger> provider3) {
        this.contextProvider = provider;
        this.recentsProvider = provider2;
        this.uiEventLoggerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public LaunchOverview get() {
        return newInstance(this.contextProvider.get(), this.recentsProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static LaunchOverview_Factory create(Provider<Context> provider, Provider<Recents> provider2, Provider<UiEventLogger> provider3) {
        return new LaunchOverview_Factory(provider, provider2, provider3);
    }

    public static LaunchOverview newInstance(Context context, Recents recents, UiEventLogger uiEventLogger) {
        return new LaunchOverview(context, recents, uiEventLogger);
    }
}
