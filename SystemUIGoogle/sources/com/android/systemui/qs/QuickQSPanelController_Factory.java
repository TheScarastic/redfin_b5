package com.android.systemui.qs;

import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.statusbar.FeatureFlags;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QuickQSPanelController_Factory implements Factory<QuickQSPanelController> {
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<MediaHost> mediaHostProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<QSCustomizerController> qsCustomizerControllerProvider;
    private final Provider<QSLogger> qsLoggerProvider;
    private final Provider<QSTileHost> qsTileHostProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<Boolean> usingMediaPlayerProvider;
    private final Provider<QuickQSPanel> viewProvider;

    public QuickQSPanelController_Factory(Provider<QuickQSPanel> provider, Provider<QSTileHost> provider2, Provider<QSCustomizerController> provider3, Provider<Boolean> provider4, Provider<MediaHost> provider5, Provider<MetricsLogger> provider6, Provider<UiEventLogger> provider7, Provider<QSLogger> provider8, Provider<DumpManager> provider9, Provider<FeatureFlags> provider10) {
        this.viewProvider = provider;
        this.qsTileHostProvider = provider2;
        this.qsCustomizerControllerProvider = provider3;
        this.usingMediaPlayerProvider = provider4;
        this.mediaHostProvider = provider5;
        this.metricsLoggerProvider = provider6;
        this.uiEventLoggerProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.dumpManagerProvider = provider9;
        this.featureFlagsProvider = provider10;
    }

    @Override // javax.inject.Provider
    public QuickQSPanelController get() {
        return newInstance(this.viewProvider.get(), this.qsTileHostProvider.get(), this.qsCustomizerControllerProvider.get(), this.usingMediaPlayerProvider.get().booleanValue(), this.mediaHostProvider.get(), this.metricsLoggerProvider.get(), this.uiEventLoggerProvider.get(), this.qsLoggerProvider.get(), this.dumpManagerProvider.get(), this.featureFlagsProvider.get());
    }

    public static QuickQSPanelController_Factory create(Provider<QuickQSPanel> provider, Provider<QSTileHost> provider2, Provider<QSCustomizerController> provider3, Provider<Boolean> provider4, Provider<MediaHost> provider5, Provider<MetricsLogger> provider6, Provider<UiEventLogger> provider7, Provider<QSLogger> provider8, Provider<DumpManager> provider9, Provider<FeatureFlags> provider10) {
        return new QuickQSPanelController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static QuickQSPanelController newInstance(QuickQSPanel quickQSPanel, QSTileHost qSTileHost, QSCustomizerController qSCustomizerController, boolean z, MediaHost mediaHost, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, QSLogger qSLogger, DumpManager dumpManager, FeatureFlags featureFlags) {
        return new QuickQSPanelController(quickQSPanel, qSTileHost, qSCustomizerController, z, mediaHost, metricsLogger, uiEventLogger, qSLogger, dumpManager, featureFlags);
    }
}
