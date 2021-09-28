package com.android.systemui.statusbar.notification.stack;

import android.content.res.Resources;
import android.view.LayoutInflater;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.ForegroundServiceDismissalFeatureController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationStackScrollLayoutController_Factory implements Factory<NotificationStackScrollLayoutController> {
    private final Provider<Boolean> allowLongPressProvider;
    private final Provider<SysuiColorExtractor> colorExtractorProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<DynamicPrivacyController> dynamicPrivacyControllerProvider;
    private final Provider<FalsingCollector> falsingCollectorProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<ForegroundServiceDismissalFeatureController> fgFeatureControllerProvider;
    private final Provider<ForegroundServiceSectionController> fgServicesSectionControllerProvider;
    private final Provider<GroupExpansionManager> groupManagerProvider;
    private final Provider<HeadsUpManagerPhone> headsUpManagerProvider;
    private final Provider<IStatusBarService> iStatusBarServiceProvider;
    private final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    private final Provider<KeyguardMediaController> keyguardMediaControllerProvider;
    private final Provider<LayoutInflater> layoutInflaterProvider;
    private final Provider<NotificationGroupManagerLegacy> legacyGroupManagerProvider;
    private final Provider<LockscreenShadeTransitionController> lockscreenShadeTransitionControllerProvider;
    private final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<NotifCollection> notifCollectionProvider;
    private final Provider<NotifPipeline> notifPipelineProvider;
    private final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    private final Provider<NotificationGutsManager> notificationGutsManagerProvider;
    private final Provider<NotificationRoundnessManager> notificationRoundnessManagerProvider;
    private final Provider<NotificationSwipeHelper.Builder> notificationSwipeHelperBuilderProvider;
    private final Provider<NotificationRemoteInputManager> remoteInputManagerProvider;
    private final Provider<Resources> resourcesProvider;
    private final Provider<ScrimController> scrimControllerProvider;
    private final Provider<ShadeController> shadeControllerProvider;
    private final Provider<SectionHeaderController> silentHeaderControllerProvider;
    private final Provider<StatusBar> statusBarProvider;
    private final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    private final Provider<TunerService> tunerServiceProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<VisualStabilityManager> visualStabilityManagerProvider;
    private final Provider<ZenModeController> zenModeControllerProvider;

    public NotificationStackScrollLayoutController_Factory(Provider<Boolean> provider, Provider<NotificationGutsManager> provider2, Provider<HeadsUpManagerPhone> provider3, Provider<NotificationRoundnessManager> provider4, Provider<TunerService> provider5, Provider<DynamicPrivacyController> provider6, Provider<ConfigurationController> provider7, Provider<SysuiStatusBarStateController> provider8, Provider<KeyguardMediaController> provider9, Provider<KeyguardBypassController> provider10, Provider<ZenModeController> provider11, Provider<SysuiColorExtractor> provider12, Provider<NotificationLockscreenUserManager> provider13, Provider<MetricsLogger> provider14, Provider<FalsingCollector> provider15, Provider<FalsingManager> provider16, Provider<Resources> provider17, Provider<NotificationSwipeHelper.Builder> provider18, Provider<StatusBar> provider19, Provider<ScrimController> provider20, Provider<NotificationGroupManagerLegacy> provider21, Provider<GroupExpansionManager> provider22, Provider<SectionHeaderController> provider23, Provider<FeatureFlags> provider24, Provider<NotifPipeline> provider25, Provider<NotifCollection> provider26, Provider<NotificationEntryManager> provider27, Provider<LockscreenShadeTransitionController> provider28, Provider<IStatusBarService> provider29, Provider<UiEventLogger> provider30, Provider<ForegroundServiceDismissalFeatureController> provider31, Provider<ForegroundServiceSectionController> provider32, Provider<LayoutInflater> provider33, Provider<NotificationRemoteInputManager> provider34, Provider<VisualStabilityManager> provider35, Provider<ShadeController> provider36) {
        this.allowLongPressProvider = provider;
        this.notificationGutsManagerProvider = provider2;
        this.headsUpManagerProvider = provider3;
        this.notificationRoundnessManagerProvider = provider4;
        this.tunerServiceProvider = provider5;
        this.dynamicPrivacyControllerProvider = provider6;
        this.configurationControllerProvider = provider7;
        this.statusBarStateControllerProvider = provider8;
        this.keyguardMediaControllerProvider = provider9;
        this.keyguardBypassControllerProvider = provider10;
        this.zenModeControllerProvider = provider11;
        this.colorExtractorProvider = provider12;
        this.lockscreenUserManagerProvider = provider13;
        this.metricsLoggerProvider = provider14;
        this.falsingCollectorProvider = provider15;
        this.falsingManagerProvider = provider16;
        this.resourcesProvider = provider17;
        this.notificationSwipeHelperBuilderProvider = provider18;
        this.statusBarProvider = provider19;
        this.scrimControllerProvider = provider20;
        this.legacyGroupManagerProvider = provider21;
        this.groupManagerProvider = provider22;
        this.silentHeaderControllerProvider = provider23;
        this.featureFlagsProvider = provider24;
        this.notifPipelineProvider = provider25;
        this.notifCollectionProvider = provider26;
        this.notificationEntryManagerProvider = provider27;
        this.lockscreenShadeTransitionControllerProvider = provider28;
        this.iStatusBarServiceProvider = provider29;
        this.uiEventLoggerProvider = provider30;
        this.fgFeatureControllerProvider = provider31;
        this.fgServicesSectionControllerProvider = provider32;
        this.layoutInflaterProvider = provider33;
        this.remoteInputManagerProvider = provider34;
        this.visualStabilityManagerProvider = provider35;
        this.shadeControllerProvider = provider36;
    }

    @Override // javax.inject.Provider
    public NotificationStackScrollLayoutController get() {
        return newInstance(this.allowLongPressProvider.get().booleanValue(), this.notificationGutsManagerProvider.get(), this.headsUpManagerProvider.get(), this.notificationRoundnessManagerProvider.get(), this.tunerServiceProvider.get(), this.dynamicPrivacyControllerProvider.get(), this.configurationControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.keyguardMediaControllerProvider.get(), this.keyguardBypassControllerProvider.get(), this.zenModeControllerProvider.get(), this.colorExtractorProvider.get(), this.lockscreenUserManagerProvider.get(), this.metricsLoggerProvider.get(), this.falsingCollectorProvider.get(), this.falsingManagerProvider.get(), this.resourcesProvider.get(), this.notificationSwipeHelperBuilderProvider.get(), this.statusBarProvider.get(), this.scrimControllerProvider.get(), this.legacyGroupManagerProvider.get(), this.groupManagerProvider.get(), this.silentHeaderControllerProvider.get(), this.featureFlagsProvider.get(), this.notifPipelineProvider.get(), this.notifCollectionProvider.get(), this.notificationEntryManagerProvider.get(), this.lockscreenShadeTransitionControllerProvider.get(), this.iStatusBarServiceProvider.get(), this.uiEventLoggerProvider.get(), this.fgFeatureControllerProvider.get(), this.fgServicesSectionControllerProvider.get(), this.layoutInflaterProvider.get(), this.remoteInputManagerProvider.get(), this.visualStabilityManagerProvider.get(), this.shadeControllerProvider.get());
    }

    public static NotificationStackScrollLayoutController_Factory create(Provider<Boolean> provider, Provider<NotificationGutsManager> provider2, Provider<HeadsUpManagerPhone> provider3, Provider<NotificationRoundnessManager> provider4, Provider<TunerService> provider5, Provider<DynamicPrivacyController> provider6, Provider<ConfigurationController> provider7, Provider<SysuiStatusBarStateController> provider8, Provider<KeyguardMediaController> provider9, Provider<KeyguardBypassController> provider10, Provider<ZenModeController> provider11, Provider<SysuiColorExtractor> provider12, Provider<NotificationLockscreenUserManager> provider13, Provider<MetricsLogger> provider14, Provider<FalsingCollector> provider15, Provider<FalsingManager> provider16, Provider<Resources> provider17, Provider<NotificationSwipeHelper.Builder> provider18, Provider<StatusBar> provider19, Provider<ScrimController> provider20, Provider<NotificationGroupManagerLegacy> provider21, Provider<GroupExpansionManager> provider22, Provider<SectionHeaderController> provider23, Provider<FeatureFlags> provider24, Provider<NotifPipeline> provider25, Provider<NotifCollection> provider26, Provider<NotificationEntryManager> provider27, Provider<LockscreenShadeTransitionController> provider28, Provider<IStatusBarService> provider29, Provider<UiEventLogger> provider30, Provider<ForegroundServiceDismissalFeatureController> provider31, Provider<ForegroundServiceSectionController> provider32, Provider<LayoutInflater> provider33, Provider<NotificationRemoteInputManager> provider34, Provider<VisualStabilityManager> provider35, Provider<ShadeController> provider36) {
        return new NotificationStackScrollLayoutController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31, provider32, provider33, provider34, provider35, provider36);
    }

    public static NotificationStackScrollLayoutController newInstance(boolean z, NotificationGutsManager notificationGutsManager, HeadsUpManagerPhone headsUpManagerPhone, NotificationRoundnessManager notificationRoundnessManager, TunerService tunerService, DynamicPrivacyController dynamicPrivacyController, ConfigurationController configurationController, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardMediaController keyguardMediaController, KeyguardBypassController keyguardBypassController, ZenModeController zenModeController, SysuiColorExtractor sysuiColorExtractor, NotificationLockscreenUserManager notificationLockscreenUserManager, MetricsLogger metricsLogger, FalsingCollector falsingCollector, FalsingManager falsingManager, Resources resources, Object obj, StatusBar statusBar, ScrimController scrimController, NotificationGroupManagerLegacy notificationGroupManagerLegacy, GroupExpansionManager groupExpansionManager, SectionHeaderController sectionHeaderController, FeatureFlags featureFlags, NotifPipeline notifPipeline, NotifCollection notifCollection, NotificationEntryManager notificationEntryManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, IStatusBarService iStatusBarService, UiEventLogger uiEventLogger, ForegroundServiceDismissalFeatureController foregroundServiceDismissalFeatureController, ForegroundServiceSectionController foregroundServiceSectionController, LayoutInflater layoutInflater, NotificationRemoteInputManager notificationRemoteInputManager, VisualStabilityManager visualStabilityManager, ShadeController shadeController) {
        return new NotificationStackScrollLayoutController(z, notificationGutsManager, headsUpManagerPhone, notificationRoundnessManager, tunerService, dynamicPrivacyController, configurationController, sysuiStatusBarStateController, keyguardMediaController, keyguardBypassController, zenModeController, sysuiColorExtractor, notificationLockscreenUserManager, metricsLogger, falsingCollector, falsingManager, resources, (NotificationSwipeHelper.Builder) obj, statusBar, scrimController, notificationGroupManagerLegacy, groupExpansionManager, sectionHeaderController, featureFlags, notifPipeline, notifCollection, notificationEntryManager, lockscreenShadeTransitionController, iStatusBarService, uiEventLogger, foregroundServiceDismissalFeatureController, foregroundServiceSectionController, layoutInflater, notificationRemoteInputManager, visualStabilityManager, shadeController);
    }
}
