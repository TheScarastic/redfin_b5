package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.os.Handler;
import android.view.WindowManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ShortcutBarController_Factory implements Factory<ShortcutBarController> {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<FpsController> fpsControllerProvider;
    private final Provider<ScreenRecordController> screenRecordControllerProvider;
    private final Provider<Optional<TaskSurfaceHelper>> screenshotControllerProvider;
    private final Provider<Handler> screenshotHandlerProvider;
    private final Provider<ToastController> toastProvider;
    private final Provider<GameDashboardUiEventLogger> uiEventLoggerProvider;
    private final Provider<WindowManager> windowManagerProvider;

    public ShortcutBarController_Factory(Provider<Context> provider, Provider<WindowManager> provider2, Provider<FpsController> provider3, Provider<ConfigurationController> provider4, Provider<Handler> provider5, Provider<ScreenRecordController> provider6, Provider<Optional<TaskSurfaceHelper>> provider7, Provider<GameDashboardUiEventLogger> provider8, Provider<ToastController> provider9) {
        this.contextProvider = provider;
        this.windowManagerProvider = provider2;
        this.fpsControllerProvider = provider3;
        this.configurationControllerProvider = provider4;
        this.screenshotHandlerProvider = provider5;
        this.screenRecordControllerProvider = provider6;
        this.screenshotControllerProvider = provider7;
        this.uiEventLoggerProvider = provider8;
        this.toastProvider = provider9;
    }

    @Override // javax.inject.Provider
    public ShortcutBarController get() {
        return newInstance(this.contextProvider.get(), this.windowManagerProvider.get(), this.fpsControllerProvider.get(), this.configurationControllerProvider.get(), this.screenshotHandlerProvider.get(), this.screenRecordControllerProvider.get(), this.screenshotControllerProvider.get(), this.uiEventLoggerProvider.get(), this.toastProvider.get());
    }

    public static ShortcutBarController_Factory create(Provider<Context> provider, Provider<WindowManager> provider2, Provider<FpsController> provider3, Provider<ConfigurationController> provider4, Provider<Handler> provider5, Provider<ScreenRecordController> provider6, Provider<Optional<TaskSurfaceHelper>> provider7, Provider<GameDashboardUiEventLogger> provider8, Provider<ToastController> provider9) {
        return new ShortcutBarController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static ShortcutBarController newInstance(Context context, WindowManager windowManager, FpsController fpsController, ConfigurationController configurationController, Handler handler, ScreenRecordController screenRecordController, Optional<TaskSurfaceHelper> optional, GameDashboardUiEventLogger gameDashboardUiEventLogger, ToastController toastController) {
        return new ShortcutBarController(context, windowManager, fpsController, configurationController, handler, screenRecordController, optional, gameDashboardUiEventLogger, toastController);
    }
}
