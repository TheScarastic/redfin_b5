package com.android.systemui.statusbar.dagger;

import android.app.IActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.ActionClickLogger;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.MediaArtworkProcessor;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.DynamicChildBindController;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.inflation.LowPriorityInflationHelper;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallLogger;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.Lazy;
import java.util.Optional;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public interface StatusBarDependenciesModule {
    static NotificationRemoteInputManager provideNotificationRemoteInputManager(Context context, NotificationLockscreenUserManager notificationLockscreenUserManager, SmartReplyController smartReplyController, NotificationEntryManager notificationEntryManager, Lazy<StatusBar> lazy, StatusBarStateController statusBarStateController, Handler handler, RemoteInputUriController remoteInputUriController, NotificationClickNotifier notificationClickNotifier, ActionClickLogger actionClickLogger) {
        return new NotificationRemoteInputManager(context, notificationLockscreenUserManager, smartReplyController, notificationEntryManager, lazy, statusBarStateController, handler, remoteInputUriController, notificationClickNotifier, actionClickLogger);
    }

    static NotificationMediaManager provideNotificationMediaManager(Context context, Lazy<StatusBar> lazy, Lazy<NotificationShadeWindowController> lazy2, NotificationEntryManager notificationEntryManager, MediaArtworkProcessor mediaArtworkProcessor, KeyguardBypassController keyguardBypassController, NotifPipeline notifPipeline, NotifCollection notifCollection, FeatureFlags featureFlags, DelayableExecutor delayableExecutor, DeviceConfigProxy deviceConfigProxy, MediaDataManager mediaDataManager) {
        return new NotificationMediaManager(context, lazy, lazy2, notificationEntryManager, mediaArtworkProcessor, keyguardBypassController, notifPipeline, notifCollection, featureFlags, delayableExecutor, deviceConfigProxy, mediaDataManager);
    }

    static NotificationListener provideNotificationListener(Context context, NotificationManager notificationManager, Handler handler) {
        return new NotificationListener(context, notificationManager, handler);
    }

    static SmartReplyController provideSmartReplyController(NotificationEntryManager notificationEntryManager, IStatusBarService iStatusBarService, NotificationClickNotifier notificationClickNotifier) {
        return new SmartReplyController(notificationEntryManager, iStatusBarService, notificationClickNotifier);
    }

    static NotificationViewHierarchyManager provideNotificationViewHierarchyManager(Context context, Handler handler, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, VisualStabilityManager visualStabilityManager, StatusBarStateController statusBarStateController, NotificationEntryManager notificationEntryManager, KeyguardBypassController keyguardBypassController, Optional<Bubbles> optional, DynamicPrivacyController dynamicPrivacyController, ForegroundServiceSectionController foregroundServiceSectionController, DynamicChildBindController dynamicChildBindController, LowPriorityInflationHelper lowPriorityInflationHelper, AssistantFeedbackController assistantFeedbackController) {
        return new NotificationViewHierarchyManager(context, handler, notificationLockscreenUserManager, notificationGroupManagerLegacy, visualStabilityManager, statusBarStateController, notificationEntryManager, keyguardBypassController, optional, dynamicPrivacyController, foregroundServiceSectionController, dynamicChildBindController, lowPriorityInflationHelper, assistantFeedbackController);
    }

    static CommandQueue provideCommandQueue(Context context, ProtoTracer protoTracer, CommandRegistry commandRegistry) {
        return new CommandQueue(context, protoTracer, commandRegistry);
    }

    static OngoingCallController provideOngoingCallController(CommonNotifCollection commonNotifCollection, FeatureFlags featureFlags, SystemClock systemClock, ActivityStarter activityStarter, Executor executor, IActivityManager iActivityManager, OngoingCallLogger ongoingCallLogger) {
        OngoingCallController ongoingCallController = new OngoingCallController(commonNotifCollection, featureFlags, systemClock, activityStarter, executor, iActivityManager, ongoingCallLogger);
        ongoingCallController.init();
        return ongoingCallController;
    }
}
