package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaCarouselController_Factory implements Factory<MediaCarouselController> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<DelayableExecutor> executorProvider;
    private final Provider<FalsingCollector> falsingCollectorProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<MediaControlPanel> mediaControlPanelFactoryProvider;
    private final Provider<MediaHostStatesManager> mediaHostStatesManagerProvider;
    private final Provider<MediaDataManager> mediaManagerProvider;
    private final Provider<SystemClock> systemClockProvider;
    private final Provider<VisualStabilityManager> visualStabilityManagerProvider;

    public MediaCarouselController_Factory(Provider<Context> provider, Provider<MediaControlPanel> provider2, Provider<VisualStabilityManager> provider3, Provider<MediaHostStatesManager> provider4, Provider<ActivityStarter> provider5, Provider<SystemClock> provider6, Provider<DelayableExecutor> provider7, Provider<MediaDataManager> provider8, Provider<ConfigurationController> provider9, Provider<FalsingCollector> provider10, Provider<FalsingManager> provider11, Provider<DumpManager> provider12) {
        this.contextProvider = provider;
        this.mediaControlPanelFactoryProvider = provider2;
        this.visualStabilityManagerProvider = provider3;
        this.mediaHostStatesManagerProvider = provider4;
        this.activityStarterProvider = provider5;
        this.systemClockProvider = provider6;
        this.executorProvider = provider7;
        this.mediaManagerProvider = provider8;
        this.configurationControllerProvider = provider9;
        this.falsingCollectorProvider = provider10;
        this.falsingManagerProvider = provider11;
        this.dumpManagerProvider = provider12;
    }

    @Override // javax.inject.Provider
    public MediaCarouselController get() {
        return newInstance(this.contextProvider.get(), this.mediaControlPanelFactoryProvider, this.visualStabilityManagerProvider.get(), this.mediaHostStatesManagerProvider.get(), this.activityStarterProvider.get(), this.systemClockProvider.get(), this.executorProvider.get(), this.mediaManagerProvider.get(), this.configurationControllerProvider.get(), this.falsingCollectorProvider.get(), this.falsingManagerProvider.get(), this.dumpManagerProvider.get());
    }

    public static MediaCarouselController_Factory create(Provider<Context> provider, Provider<MediaControlPanel> provider2, Provider<VisualStabilityManager> provider3, Provider<MediaHostStatesManager> provider4, Provider<ActivityStarter> provider5, Provider<SystemClock> provider6, Provider<DelayableExecutor> provider7, Provider<MediaDataManager> provider8, Provider<ConfigurationController> provider9, Provider<FalsingCollector> provider10, Provider<FalsingManager> provider11, Provider<DumpManager> provider12) {
        return new MediaCarouselController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static MediaCarouselController newInstance(Context context, Provider<MediaControlPanel> provider, VisualStabilityManager visualStabilityManager, MediaHostStatesManager mediaHostStatesManager, ActivityStarter activityStarter, SystemClock systemClock, DelayableExecutor delayableExecutor, MediaDataManager mediaDataManager, ConfigurationController configurationController, FalsingCollector falsingCollector, FalsingManager falsingManager, DumpManager dumpManager) {
        return new MediaCarouselController(context, provider, visualStabilityManager, mediaHostStatesManager, activityStarter, systemClock, delayableExecutor, mediaDataManager, configurationController, falsingCollector, falsingManager, dumpManager);
    }
}
