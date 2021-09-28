package com.android.systemui.dagger;

import android.app.INotificationManager;
import android.content.Context;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.model.SysUiState;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.system.smartspace.SmartspaceTransitionController;
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
import java.util.Optional;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public abstract class SystemUIModule {
    /* access modifiers changed from: package-private */
    public static SysUiState provideSysUiState() {
        return new SysUiState();
    }

    /* access modifiers changed from: package-private */
    public static SmartspaceTransitionController provideSmartspaceTransitionController() {
        return new SmartspaceTransitionController();
    }

    /* access modifiers changed from: package-private */
    public static Optional<BubblesManager> provideBubblesManager(Context context, Optional<Bubbles> optional, NotificationShadeWindowController notificationShadeWindowController, StatusBarStateController statusBarStateController, ShadeController shadeController, ConfigurationController configurationController, IStatusBarService iStatusBarService, INotificationManager iNotificationManager, NotificationInterruptStateProvider notificationInterruptStateProvider, ZenModeController zenModeController, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, SysUiState sysUiState, FeatureFlags featureFlags, DumpManager dumpManager, Executor executor) {
        return Optional.ofNullable(BubblesManager.create(context, optional, notificationShadeWindowController, statusBarStateController, shadeController, configurationController, iStatusBarService, iNotificationManager, notificationInterruptStateProvider, zenModeController, notificationLockscreenUserManager, notificationGroupManagerLegacy, notificationEntryManager, notifPipeline, sysUiState, featureFlags, dumpManager, executor));
    }
}
