package com.android.systemui.screenshot;

import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LongScreenshotActivity_Factory implements Factory<LongScreenshotActivity> {
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<ImageExporter> imageExporterProvider;
    private final Provider<LongScreenshotData> longScreenshotHolderProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public LongScreenshotActivity_Factory(Provider<UiEventLogger> provider, Provider<ImageExporter> provider2, Provider<Executor> provider3, Provider<Executor> provider4, Provider<LongScreenshotData> provider5) {
        this.uiEventLoggerProvider = provider;
        this.imageExporterProvider = provider2;
        this.mainExecutorProvider = provider3;
        this.bgExecutorProvider = provider4;
        this.longScreenshotHolderProvider = provider5;
    }

    @Override // javax.inject.Provider
    public LongScreenshotActivity get() {
        return newInstance(this.uiEventLoggerProvider.get(), this.imageExporterProvider.get(), this.mainExecutorProvider.get(), this.bgExecutorProvider.get(), this.longScreenshotHolderProvider.get());
    }

    public static LongScreenshotActivity_Factory create(Provider<UiEventLogger> provider, Provider<ImageExporter> provider2, Provider<Executor> provider3, Provider<Executor> provider4, Provider<LongScreenshotData> provider5) {
        return new LongScreenshotActivity_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static LongScreenshotActivity newInstance(UiEventLogger uiEventLogger, Object obj, Executor executor, Executor executor2, LongScreenshotData longScreenshotData) {
        return new LongScreenshotActivity(uiEventLogger, (ImageExporter) obj, executor, executor2, longScreenshotData);
    }
}
