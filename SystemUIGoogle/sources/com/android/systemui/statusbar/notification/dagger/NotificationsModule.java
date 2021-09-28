package com.android.systemui.statusbar.notification.dagger;

import android.app.INotificationManager;
import android.content.Context;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutManager;
import android.os.Handler;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.R$bool;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.ForegroundServiceDismissalFeatureController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinder;
import com.android.systemui.statusbar.notification.collection.inflation.OnUserInteractionCallbackImpl;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.OnUserInteractionCallbackImplLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManagerImpl;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManagerImpl;
import com.android.systemui.statusbar.notification.init.NotificationsController;
import com.android.systemui.statusbar.notification.init.NotificationsControllerImpl;
import com.android.systemui.statusbar.notification.init.NotificationsControllerStub;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.logging.NotificationPanelLogger;
import com.android.systemui.statusbar.notification.logging.NotificationPanelLoggerImpl;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialogController;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.leak.LeakDetector;
import com.android.systemui.wmshell.BubblesManager;
import dagger.Lazy;
import java.util.Optional;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public interface NotificationsModule {
    static NotificationEntryManager provideNotificationEntryManager(NotificationEntryManagerLogger notificationEntryManagerLogger, NotificationGroupManagerLegacy notificationGroupManagerLegacy, FeatureFlags featureFlags, Lazy<NotificationRowBinder> lazy, Lazy<NotificationRemoteInputManager> lazy2, LeakDetector leakDetector, ForegroundServiceDismissalFeatureController foregroundServiceDismissalFeatureController, IStatusBarService iStatusBarService) {
        return new NotificationEntryManager(notificationEntryManagerLogger, notificationGroupManagerLegacy, featureFlags, lazy, lazy2, leakDetector, foregroundServiceDismissalFeatureController, iStatusBarService);
    }

    static NotificationGutsManager provideNotificationGutsManager(Context context, Lazy<StatusBar> lazy, Handler handler, Handler handler2, AccessibilityManager accessibilityManager, HighPriorityProvider highPriorityProvider, INotificationManager iNotificationManager, NotificationEntryManager notificationEntryManager, PeopleSpaceWidgetManager peopleSpaceWidgetManager, LauncherApps launcherApps, ShortcutManager shortcutManager, ChannelEditorDialogController channelEditorDialogController, UserContextProvider userContextProvider, AssistantFeedbackController assistantFeedbackController, Optional<BubblesManager> optional, UiEventLogger uiEventLogger, OnUserInteractionCallback onUserInteractionCallback, ShadeController shadeController) {
        return new NotificationGutsManager(context, lazy, handler, handler2, accessibilityManager, highPriorityProvider, iNotificationManager, notificationEntryManager, peopleSpaceWidgetManager, launcherApps, shortcutManager, channelEditorDialogController, userContextProvider, assistantFeedbackController, optional, uiEventLogger, onUserInteractionCallback, shadeController);
    }

    static VisualStabilityManager provideVisualStabilityManager(FeatureFlags featureFlags, NotificationEntryManager notificationEntryManager, Handler handler, StatusBarStateController statusBarStateController, WakefulnessLifecycle wakefulnessLifecycle) {
        return new VisualStabilityManager(notificationEntryManager, handler, statusBarStateController, wakefulnessLifecycle);
    }

    static NotificationLogger provideNotificationLogger(NotificationListener notificationListener, Executor executor, NotificationEntryManager notificationEntryManager, StatusBarStateController statusBarStateController, NotificationLogger.ExpansionStateLogger expansionStateLogger, NotificationPanelLogger notificationPanelLogger) {
        return new NotificationLogger(notificationListener, executor, notificationEntryManager, statusBarStateController, expansionStateLogger, notificationPanelLogger);
    }

    static NotificationPanelLogger provideNotificationPanelLogger() {
        return new NotificationPanelLoggerImpl();
    }

    static GroupMembershipManager provideGroupMembershipManager(FeatureFlags featureFlags, Lazy<NotificationGroupManagerLegacy> lazy) {
        if (featureFlags.isNewNotifPipelineRenderingEnabled()) {
            return new GroupMembershipManagerImpl();
        }
        return lazy.get();
    }

    static GroupExpansionManager provideGroupExpansionManager(FeatureFlags featureFlags, Lazy<GroupMembershipManager> lazy, Lazy<NotificationGroupManagerLegacy> lazy2) {
        if (featureFlags.isNewNotifPipelineRenderingEnabled()) {
            return new GroupExpansionManagerImpl(lazy.get());
        }
        return lazy2.get();
    }

    static NotificationsController provideNotificationsController(Context context, Lazy<NotificationsControllerImpl> lazy, Lazy<NotificationsControllerStub> lazy2) {
        if (context.getResources().getBoolean(R$bool.config_renderNotifications)) {
            return lazy.get();
        }
        return lazy2.get();
    }

    static CommonNotifCollection provideCommonNotifCollection(FeatureFlags featureFlags, Lazy<NotifPipeline> lazy, NotificationEntryManager notificationEntryManager) {
        return featureFlags.isNewNotifPipelineRenderingEnabled() ? lazy.get() : notificationEntryManager;
    }

    static OnUserInteractionCallback provideOnUserInteractionCallback(FeatureFlags featureFlags, HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController, Lazy<NotifPipeline> lazy, Lazy<NotifCollection> lazy2, Lazy<VisualStabilityCoordinator> lazy3, NotificationEntryManager notificationEntryManager, VisualStabilityManager visualStabilityManager, Lazy<GroupMembershipManager> lazy4) {
        if (featureFlags.isNewNotifPipelineRenderingEnabled()) {
            return new OnUserInteractionCallbackImpl(lazy.get(), lazy2.get(), headsUpManager, statusBarStateController, lazy3.get(), lazy4.get());
        }
        return new OnUserInteractionCallbackImplLegacy(notificationEntryManager, headsUpManager, statusBarStateController, visualStabilityManager, lazy4.get());
    }
}
