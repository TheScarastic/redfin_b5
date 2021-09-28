package com.android.systemui.screenshot;

import android.os.UserManager;
import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TakeScreenshotService_Factory implements Factory<TakeScreenshotService> {
    private final Provider<ScreenshotNotificationsController> notificationsControllerProvider;
    private final Provider<ScreenshotController> screenshotControllerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<UserManager> userManagerProvider;

    public TakeScreenshotService_Factory(Provider<ScreenshotController> provider, Provider<UserManager> provider2, Provider<UiEventLogger> provider3, Provider<ScreenshotNotificationsController> provider4) {
        this.screenshotControllerProvider = provider;
        this.userManagerProvider = provider2;
        this.uiEventLoggerProvider = provider3;
        this.notificationsControllerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public TakeScreenshotService get() {
        return newInstance(this.screenshotControllerProvider.get(), this.userManagerProvider.get(), this.uiEventLoggerProvider.get(), this.notificationsControllerProvider.get());
    }

    public static TakeScreenshotService_Factory create(Provider<ScreenshotController> provider, Provider<UserManager> provider2, Provider<UiEventLogger> provider3, Provider<ScreenshotNotificationsController> provider4) {
        return new TakeScreenshotService_Factory(provider, provider2, provider3, provider4);
    }

    public static TakeScreenshotService newInstance(ScreenshotController screenshotController, UserManager userManager, UiEventLogger uiEventLogger, ScreenshotNotificationsController screenshotNotificationsController) {
        return new TakeScreenshotService(screenshotController, userManager, uiEventLogger, screenshotNotificationsController);
    }
}
