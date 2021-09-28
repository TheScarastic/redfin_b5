package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import com.android.systemui.plugins.ActivityStarter;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class GameMenuActivity_Factory implements Factory<GameMenuActivity> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Context> contextProvider;
    private final Provider<EntryPointController> entryPointControllerProvider;
    private final Provider<GameModeDndController> gameModeDndControllerProvider;
    private final Provider<LayoutInflater> layoutInflaterProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<ShortcutBarController> shortcutBarControllerProvider;
    private final Provider<GameDashboardUiEventLogger> uiEventLoggerProvider;

    public GameMenuActivity_Factory(Provider<Context> provider, Provider<EntryPointController> provider2, Provider<ActivityStarter> provider3, Provider<ShortcutBarController> provider4, Provider<GameModeDndController> provider5, Provider<LayoutInflater> provider6, Provider<Handler> provider7, Provider<GameDashboardUiEventLogger> provider8) {
        this.contextProvider = provider;
        this.entryPointControllerProvider = provider2;
        this.activityStarterProvider = provider3;
        this.shortcutBarControllerProvider = provider4;
        this.gameModeDndControllerProvider = provider5;
        this.layoutInflaterProvider = provider6;
        this.mainHandlerProvider = provider7;
        this.uiEventLoggerProvider = provider8;
    }

    @Override // javax.inject.Provider
    public GameMenuActivity get() {
        return newInstance(this.contextProvider.get(), this.entryPointControllerProvider.get(), this.activityStarterProvider.get(), this.shortcutBarControllerProvider.get(), this.gameModeDndControllerProvider.get(), this.layoutInflaterProvider.get(), this.mainHandlerProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static GameMenuActivity_Factory create(Provider<Context> provider, Provider<EntryPointController> provider2, Provider<ActivityStarter> provider3, Provider<ShortcutBarController> provider4, Provider<GameModeDndController> provider5, Provider<LayoutInflater> provider6, Provider<Handler> provider7, Provider<GameDashboardUiEventLogger> provider8) {
        return new GameMenuActivity_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static GameMenuActivity newInstance(Context context, EntryPointController entryPointController, ActivityStarter activityStarter, ShortcutBarController shortcutBarController, GameModeDndController gameModeDndController, LayoutInflater layoutInflater, Handler handler, GameDashboardUiEventLogger gameDashboardUiEventLogger) {
        return new GameMenuActivity(context, entryPointController, activityStarter, shortcutBarController, gameModeDndController, layoutInflater, handler, gameDashboardUiEventLogger);
    }
}
