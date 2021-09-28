package com.android.systemui.statusbar.phone;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Handler;
import android.service.dreams.IDreamManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.wmshell.BubblesManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarNotificationActivityStarter_Builder_Factory implements Factory<StatusBarNotificationActivityStarter.Builder> {
    private final Provider<ActivityIntentHelper> activityIntentHelperProvider;
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<AssistManager> assistManagerLazyProvider;
    private final Provider<Optional<BubblesManager>> bubblesManagerProvider;
    private final Provider<NotificationClickNotifier> clickNotifierProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<IDreamManager> dreamManagerProvider;
    private final Provider<NotificationEntryManager> entryManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<GroupMembershipManager> groupMembershipManagerProvider;
    private final Provider<HeadsUpManagerPhone> headsUpManagerProvider;
    private final Provider<KeyguardManager> keyguardManagerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<LockPatternUtils> lockPatternUtilsProvider;
    private final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    private final Provider<StatusBarNotificationActivityStarterLogger> loggerProvider;
    private final Provider<Handler> mainThreadHandlerProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<NotifPipeline> notifPipelineProvider;
    private final Provider<NotificationInterruptStateProvider> notificationInterruptStateProvider;
    private final Provider<OnUserInteractionCallback> onUserInteractionCallbackProvider;
    private final Provider<StatusBarRemoteInputCallback> remoteInputCallbackProvider;
    private final Provider<NotificationRemoteInputManager> remoteInputManagerProvider;
    private final Provider<ShadeController> shadeControllerProvider;
    private final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<Executor> uiBgExecutorProvider;

    public StatusBarNotificationActivityStarter_Builder_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<Handler> provider3, Provider<Executor> provider4, Provider<NotificationEntryManager> provider5, Provider<NotifPipeline> provider6, Provider<HeadsUpManagerPhone> provider7, Provider<ActivityStarter> provider8, Provider<NotificationClickNotifier> provider9, Provider<StatusBarStateController> provider10, Provider<StatusBarKeyguardViewManager> provider11, Provider<KeyguardManager> provider12, Provider<IDreamManager> provider13, Provider<Optional<BubblesManager>> provider14, Provider<AssistManager> provider15, Provider<NotificationRemoteInputManager> provider16, Provider<GroupMembershipManager> provider17, Provider<NotificationLockscreenUserManager> provider18, Provider<ShadeController> provider19, Provider<KeyguardStateController> provider20, Provider<NotificationInterruptStateProvider> provider21, Provider<LockPatternUtils> provider22, Provider<StatusBarRemoteInputCallback> provider23, Provider<ActivityIntentHelper> provider24, Provider<FeatureFlags> provider25, Provider<MetricsLogger> provider26, Provider<StatusBarNotificationActivityStarterLogger> provider27, Provider<OnUserInteractionCallback> provider28) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.mainThreadHandlerProvider = provider3;
        this.uiBgExecutorProvider = provider4;
        this.entryManagerProvider = provider5;
        this.notifPipelineProvider = provider6;
        this.headsUpManagerProvider = provider7;
        this.activityStarterProvider = provider8;
        this.clickNotifierProvider = provider9;
        this.statusBarStateControllerProvider = provider10;
        this.statusBarKeyguardViewManagerProvider = provider11;
        this.keyguardManagerProvider = provider12;
        this.dreamManagerProvider = provider13;
        this.bubblesManagerProvider = provider14;
        this.assistManagerLazyProvider = provider15;
        this.remoteInputManagerProvider = provider16;
        this.groupMembershipManagerProvider = provider17;
        this.lockscreenUserManagerProvider = provider18;
        this.shadeControllerProvider = provider19;
        this.keyguardStateControllerProvider = provider20;
        this.notificationInterruptStateProvider = provider21;
        this.lockPatternUtilsProvider = provider22;
        this.remoteInputCallbackProvider = provider23;
        this.activityIntentHelperProvider = provider24;
        this.featureFlagsProvider = provider25;
        this.metricsLoggerProvider = provider26;
        this.loggerProvider = provider27;
        this.onUserInteractionCallbackProvider = provider28;
    }

    @Override // javax.inject.Provider
    public StatusBarNotificationActivityStarter.Builder get() {
        return newInstance(this.contextProvider.get(), this.commandQueueProvider.get(), this.mainThreadHandlerProvider.get(), this.uiBgExecutorProvider.get(), this.entryManagerProvider.get(), this.notifPipelineProvider.get(), this.headsUpManagerProvider.get(), this.activityStarterProvider.get(), this.clickNotifierProvider.get(), this.statusBarStateControllerProvider.get(), this.statusBarKeyguardViewManagerProvider.get(), this.keyguardManagerProvider.get(), this.dreamManagerProvider.get(), this.bubblesManagerProvider.get(), DoubleCheck.lazy(this.assistManagerLazyProvider), this.remoteInputManagerProvider.get(), this.groupMembershipManagerProvider.get(), this.lockscreenUserManagerProvider.get(), this.shadeControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.notificationInterruptStateProvider.get(), this.lockPatternUtilsProvider.get(), this.remoteInputCallbackProvider.get(), this.activityIntentHelperProvider.get(), this.featureFlagsProvider.get(), this.metricsLoggerProvider.get(), this.loggerProvider.get(), this.onUserInteractionCallbackProvider.get());
    }

    public static StatusBarNotificationActivityStarter_Builder_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<Handler> provider3, Provider<Executor> provider4, Provider<NotificationEntryManager> provider5, Provider<NotifPipeline> provider6, Provider<HeadsUpManagerPhone> provider7, Provider<ActivityStarter> provider8, Provider<NotificationClickNotifier> provider9, Provider<StatusBarStateController> provider10, Provider<StatusBarKeyguardViewManager> provider11, Provider<KeyguardManager> provider12, Provider<IDreamManager> provider13, Provider<Optional<BubblesManager>> provider14, Provider<AssistManager> provider15, Provider<NotificationRemoteInputManager> provider16, Provider<GroupMembershipManager> provider17, Provider<NotificationLockscreenUserManager> provider18, Provider<ShadeController> provider19, Provider<KeyguardStateController> provider20, Provider<NotificationInterruptStateProvider> provider21, Provider<LockPatternUtils> provider22, Provider<StatusBarRemoteInputCallback> provider23, Provider<ActivityIntentHelper> provider24, Provider<FeatureFlags> provider25, Provider<MetricsLogger> provider26, Provider<StatusBarNotificationActivityStarterLogger> provider27, Provider<OnUserInteractionCallback> provider28) {
        return new StatusBarNotificationActivityStarter_Builder_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28);
    }

    public static StatusBarNotificationActivityStarter.Builder newInstance(Context context, CommandQueue commandQueue, Handler handler, Executor executor, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, HeadsUpManagerPhone headsUpManagerPhone, ActivityStarter activityStarter, NotificationClickNotifier notificationClickNotifier, StatusBarStateController statusBarStateController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, KeyguardManager keyguardManager, IDreamManager iDreamManager, Optional<BubblesManager> optional, Lazy<AssistManager> lazy, NotificationRemoteInputManager notificationRemoteInputManager, GroupMembershipManager groupMembershipManager, NotificationLockscreenUserManager notificationLockscreenUserManager, ShadeController shadeController, KeyguardStateController keyguardStateController, NotificationInterruptStateProvider notificationInterruptStateProvider, LockPatternUtils lockPatternUtils, StatusBarRemoteInputCallback statusBarRemoteInputCallback, ActivityIntentHelper activityIntentHelper, FeatureFlags featureFlags, MetricsLogger metricsLogger, StatusBarNotificationActivityStarterLogger statusBarNotificationActivityStarterLogger, OnUserInteractionCallback onUserInteractionCallback) {
        return new StatusBarNotificationActivityStarter.Builder(context, commandQueue, handler, executor, notificationEntryManager, notifPipeline, headsUpManagerPhone, activityStarter, notificationClickNotifier, statusBarStateController, statusBarKeyguardViewManager, keyguardManager, iDreamManager, optional, lazy, notificationRemoteInputManager, groupMembershipManager, notificationLockscreenUserManager, shadeController, keyguardStateController, notificationInterruptStateProvider, lockPatternUtils, statusBarRemoteInputCallback, activityIntentHelper, featureFlags, metricsLogger, statusBarNotificationActivityStarterLogger, onUserInteractionCallback);
    }
}
