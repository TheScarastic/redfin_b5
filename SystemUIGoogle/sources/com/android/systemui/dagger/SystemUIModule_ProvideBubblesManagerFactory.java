package com.android.systemui.dagger;

import android.app.INotificationManager;
import android.content.Context;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.model.SysUiState;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SystemUIModule_ProvideBubblesManagerFactory implements Factory<Optional<BubblesManager>> {
    private final Provider<Optional<Bubbles>> bubblesOptionalProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<NotificationEntryManager> entryManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<NotificationGroupManagerLegacy> groupManagerProvider;
    private final Provider<NotificationInterruptStateProvider> interruptionStateProvider;
    private final Provider<NotifPipeline> notifPipelineProvider;
    private final Provider<NotificationLockscreenUserManager> notifUserManagerProvider;
    private final Provider<INotificationManager> notificationManagerProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    private final Provider<ShadeController> shadeControllerProvider;
    private final Provider<IStatusBarService> statusBarServiceProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<SysUiState> sysUiStateProvider;
    private final Provider<Executor> sysuiMainExecutorProvider;
    private final Provider<ZenModeController> zenModeControllerProvider;

    public SystemUIModule_ProvideBubblesManagerFactory(Provider<Context> provider, Provider<Optional<Bubbles>> provider2, Provider<NotificationShadeWindowController> provider3, Provider<StatusBarStateController> provider4, Provider<ShadeController> provider5, Provider<ConfigurationController> provider6, Provider<IStatusBarService> provider7, Provider<INotificationManager> provider8, Provider<NotificationInterruptStateProvider> provider9, Provider<ZenModeController> provider10, Provider<NotificationLockscreenUserManager> provider11, Provider<NotificationGroupManagerLegacy> provider12, Provider<NotificationEntryManager> provider13, Provider<NotifPipeline> provider14, Provider<SysUiState> provider15, Provider<FeatureFlags> provider16, Provider<DumpManager> provider17, Provider<Executor> provider18) {
        this.contextProvider = provider;
        this.bubblesOptionalProvider = provider2;
        this.notificationShadeWindowControllerProvider = provider3;
        this.statusBarStateControllerProvider = provider4;
        this.shadeControllerProvider = provider5;
        this.configurationControllerProvider = provider6;
        this.statusBarServiceProvider = provider7;
        this.notificationManagerProvider = provider8;
        this.interruptionStateProvider = provider9;
        this.zenModeControllerProvider = provider10;
        this.notifUserManagerProvider = provider11;
        this.groupManagerProvider = provider12;
        this.entryManagerProvider = provider13;
        this.notifPipelineProvider = provider14;
        this.sysUiStateProvider = provider15;
        this.featureFlagsProvider = provider16;
        this.dumpManagerProvider = provider17;
        this.sysuiMainExecutorProvider = provider18;
    }

    @Override // javax.inject.Provider
    public Optional<BubblesManager> get() {
        return provideBubblesManager(this.contextProvider.get(), this.bubblesOptionalProvider.get(), this.notificationShadeWindowControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.shadeControllerProvider.get(), this.configurationControllerProvider.get(), this.statusBarServiceProvider.get(), this.notificationManagerProvider.get(), this.interruptionStateProvider.get(), this.zenModeControllerProvider.get(), this.notifUserManagerProvider.get(), this.groupManagerProvider.get(), this.entryManagerProvider.get(), this.notifPipelineProvider.get(), this.sysUiStateProvider.get(), this.featureFlagsProvider.get(), this.dumpManagerProvider.get(), this.sysuiMainExecutorProvider.get());
    }

    public static SystemUIModule_ProvideBubblesManagerFactory create(Provider<Context> provider, Provider<Optional<Bubbles>> provider2, Provider<NotificationShadeWindowController> provider3, Provider<StatusBarStateController> provider4, Provider<ShadeController> provider5, Provider<ConfigurationController> provider6, Provider<IStatusBarService> provider7, Provider<INotificationManager> provider8, Provider<NotificationInterruptStateProvider> provider9, Provider<ZenModeController> provider10, Provider<NotificationLockscreenUserManager> provider11, Provider<NotificationGroupManagerLegacy> provider12, Provider<NotificationEntryManager> provider13, Provider<NotifPipeline> provider14, Provider<SysUiState> provider15, Provider<FeatureFlags> provider16, Provider<DumpManager> provider17, Provider<Executor> provider18) {
        return new SystemUIModule_ProvideBubblesManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18);
    }

    public static Optional<BubblesManager> provideBubblesManager(Context context, Optional<Bubbles> optional, NotificationShadeWindowController notificationShadeWindowController, StatusBarStateController statusBarStateController, ShadeController shadeController, ConfigurationController configurationController, IStatusBarService iStatusBarService, INotificationManager iNotificationManager, NotificationInterruptStateProvider notificationInterruptStateProvider, ZenModeController zenModeController, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, SysUiState sysUiState, FeatureFlags featureFlags, DumpManager dumpManager, Executor executor) {
        return (Optional) Preconditions.checkNotNullFromProvides(SystemUIModule.provideBubblesManager(context, optional, notificationShadeWindowController, statusBarStateController, shadeController, configurationController, iStatusBarService, iNotificationManager, notificationInterruptStateProvider, zenModeController, notificationLockscreenUserManager, notificationGroupManagerLegacy, notificationEntryManager, notifPipeline, sysUiState, featureFlags, dumpManager, executor));
    }
}
