package com.android.systemui.screenshot;

import android.app.ActivityManager;
import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ScreenshotController_Factory implements Factory<ScreenshotController> {
    private final Provider<ActivityManager> activityManagerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<ImageExporter> imageExporterProvider;
    private final Provider<LongScreenshotData> longScreenshotHolderProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<ScreenshotNotificationsController> screenshotNotificationsControllerProvider;
    private final Provider<ScreenshotSmartActions> screenshotSmartActionsProvider;
    private final Provider<ScrollCaptureClient> scrollCaptureClientProvider;
    private final Provider<ScrollCaptureController> scrollCaptureControllerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public ScreenshotController_Factory(Provider<Context> provider, Provider<ScreenshotSmartActions> provider2, Provider<ScreenshotNotificationsController> provider3, Provider<ScrollCaptureClient> provider4, Provider<UiEventLogger> provider5, Provider<ImageExporter> provider6, Provider<Executor> provider7, Provider<ScrollCaptureController> provider8, Provider<LongScreenshotData> provider9, Provider<ActivityManager> provider10) {
        this.contextProvider = provider;
        this.screenshotSmartActionsProvider = provider2;
        this.screenshotNotificationsControllerProvider = provider3;
        this.scrollCaptureClientProvider = provider4;
        this.uiEventLoggerProvider = provider5;
        this.imageExporterProvider = provider6;
        this.mainExecutorProvider = provider7;
        this.scrollCaptureControllerProvider = provider8;
        this.longScreenshotHolderProvider = provider9;
        this.activityManagerProvider = provider10;
    }

    @Override // javax.inject.Provider
    public ScreenshotController get() {
        return newInstance(this.contextProvider.get(), this.screenshotSmartActionsProvider.get(), this.screenshotNotificationsControllerProvider.get(), this.scrollCaptureClientProvider.get(), this.uiEventLoggerProvider.get(), this.imageExporterProvider.get(), this.mainExecutorProvider.get(), this.scrollCaptureControllerProvider.get(), this.longScreenshotHolderProvider.get(), this.activityManagerProvider.get());
    }

    public static ScreenshotController_Factory create(Provider<Context> provider, Provider<ScreenshotSmartActions> provider2, Provider<ScreenshotNotificationsController> provider3, Provider<ScrollCaptureClient> provider4, Provider<UiEventLogger> provider5, Provider<ImageExporter> provider6, Provider<Executor> provider7, Provider<ScrollCaptureController> provider8, Provider<LongScreenshotData> provider9, Provider<ActivityManager> provider10) {
        return new ScreenshotController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static ScreenshotController newInstance(Context context, ScreenshotSmartActions screenshotSmartActions, ScreenshotNotificationsController screenshotNotificationsController, ScrollCaptureClient scrollCaptureClient, UiEventLogger uiEventLogger, Object obj, Executor executor, ScrollCaptureController scrollCaptureController, LongScreenshotData longScreenshotData, ActivityManager activityManager) {
        return new ScreenshotController(context, screenshotSmartActions, screenshotNotificationsController, scrollCaptureClient, uiEventLogger, (ImageExporter) obj, executor, scrollCaptureController, longScreenshotData, activityManager);
    }
}
