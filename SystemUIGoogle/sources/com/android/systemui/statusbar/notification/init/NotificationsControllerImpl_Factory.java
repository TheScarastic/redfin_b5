package com.android.systemui.statusbar.notification.init;

import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager;
import com.android.systemui.statusbar.notification.NotificationClicker;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationRankingManager;
import com.android.systemui.statusbar.notification.collection.TargetSdkResolver;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.init.NotifPipelineInitializer;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.interruption.HeadsUpController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineInitializer;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationsControllerImpl_Factory implements Factory<NotificationsControllerImpl> {
    private final Provider<AnimatedImageNotificationManager> animatedImageNotificationManagerProvider;
    private final Provider<NotificationClicker.Builder> clickerBuilderProvider;
    private final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    private final Provider<NotificationEntryManager> entryManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<NotificationGroupAlertTransferHelper> groupAlertTransferHelperProvider;
    private final Provider<NotificationGroupManagerLegacy> groupManagerLegacyProvider;
    private final Provider<HeadsUpController> headsUpControllerProvider;
    private final Provider<HeadsUpManager> headsUpManagerProvider;
    private final Provider<HeadsUpViewBinder> headsUpViewBinderProvider;
    private final Provider<NotificationRankingManager> legacyRankerProvider;
    private final Provider<NotifPipelineInitializer> newNotifPipelineProvider;
    private final Provider<NotifBindPipelineInitializer> notifBindPipelineInitializerProvider;
    private final Provider<NotifPipeline> notifPipelineProvider;
    private final Provider<NotificationListener> notificationListenerProvider;
    private final Provider<NotificationRowBinderImpl> notificationRowBinderProvider;
    private final Provider<PeopleSpaceWidgetManager> peopleSpaceWidgetManagerProvider;
    private final Provider<RemoteInputUriController> remoteInputUriControllerProvider;
    private final Provider<TargetSdkResolver> targetSdkResolverProvider;

    public NotificationsControllerImpl_Factory(Provider<FeatureFlags> provider, Provider<NotificationListener> provider2, Provider<NotificationEntryManager> provider3, Provider<NotificationRankingManager> provider4, Provider<NotifPipeline> provider5, Provider<TargetSdkResolver> provider6, Provider<NotifPipelineInitializer> provider7, Provider<NotifBindPipelineInitializer> provider8, Provider<DeviceProvisionedController> provider9, Provider<NotificationRowBinderImpl> provider10, Provider<RemoteInputUriController> provider11, Provider<NotificationGroupManagerLegacy> provider12, Provider<NotificationGroupAlertTransferHelper> provider13, Provider<HeadsUpManager> provider14, Provider<HeadsUpController> provider15, Provider<HeadsUpViewBinder> provider16, Provider<NotificationClicker.Builder> provider17, Provider<AnimatedImageNotificationManager> provider18, Provider<PeopleSpaceWidgetManager> provider19) {
        this.featureFlagsProvider = provider;
        this.notificationListenerProvider = provider2;
        this.entryManagerProvider = provider3;
        this.legacyRankerProvider = provider4;
        this.notifPipelineProvider = provider5;
        this.targetSdkResolverProvider = provider6;
        this.newNotifPipelineProvider = provider7;
        this.notifBindPipelineInitializerProvider = provider8;
        this.deviceProvisionedControllerProvider = provider9;
        this.notificationRowBinderProvider = provider10;
        this.remoteInputUriControllerProvider = provider11;
        this.groupManagerLegacyProvider = provider12;
        this.groupAlertTransferHelperProvider = provider13;
        this.headsUpManagerProvider = provider14;
        this.headsUpControllerProvider = provider15;
        this.headsUpViewBinderProvider = provider16;
        this.clickerBuilderProvider = provider17;
        this.animatedImageNotificationManagerProvider = provider18;
        this.peopleSpaceWidgetManagerProvider = provider19;
    }

    @Override // javax.inject.Provider
    public NotificationsControllerImpl get() {
        return newInstance(this.featureFlagsProvider.get(), this.notificationListenerProvider.get(), this.entryManagerProvider.get(), this.legacyRankerProvider.get(), DoubleCheck.lazy(this.notifPipelineProvider), this.targetSdkResolverProvider.get(), DoubleCheck.lazy(this.newNotifPipelineProvider), this.notifBindPipelineInitializerProvider.get(), this.deviceProvisionedControllerProvider.get(), this.notificationRowBinderProvider.get(), this.remoteInputUriControllerProvider.get(), DoubleCheck.lazy(this.groupManagerLegacyProvider), this.groupAlertTransferHelperProvider.get(), this.headsUpManagerProvider.get(), this.headsUpControllerProvider.get(), this.headsUpViewBinderProvider.get(), this.clickerBuilderProvider.get(), this.animatedImageNotificationManagerProvider.get(), this.peopleSpaceWidgetManagerProvider.get());
    }

    public static NotificationsControllerImpl_Factory create(Provider<FeatureFlags> provider, Provider<NotificationListener> provider2, Provider<NotificationEntryManager> provider3, Provider<NotificationRankingManager> provider4, Provider<NotifPipeline> provider5, Provider<TargetSdkResolver> provider6, Provider<NotifPipelineInitializer> provider7, Provider<NotifBindPipelineInitializer> provider8, Provider<DeviceProvisionedController> provider9, Provider<NotificationRowBinderImpl> provider10, Provider<RemoteInputUriController> provider11, Provider<NotificationGroupManagerLegacy> provider12, Provider<NotificationGroupAlertTransferHelper> provider13, Provider<HeadsUpManager> provider14, Provider<HeadsUpController> provider15, Provider<HeadsUpViewBinder> provider16, Provider<NotificationClicker.Builder> provider17, Provider<AnimatedImageNotificationManager> provider18, Provider<PeopleSpaceWidgetManager> provider19) {
        return new NotificationsControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19);
    }

    public static NotificationsControllerImpl newInstance(FeatureFlags featureFlags, NotificationListener notificationListener, NotificationEntryManager notificationEntryManager, NotificationRankingManager notificationRankingManager, Lazy<NotifPipeline> lazy, TargetSdkResolver targetSdkResolver, Lazy<NotifPipelineInitializer> lazy2, NotifBindPipelineInitializer notifBindPipelineInitializer, DeviceProvisionedController deviceProvisionedController, NotificationRowBinderImpl notificationRowBinderImpl, RemoteInputUriController remoteInputUriController, Lazy<NotificationGroupManagerLegacy> lazy3, NotificationGroupAlertTransferHelper notificationGroupAlertTransferHelper, HeadsUpManager headsUpManager, HeadsUpController headsUpController, HeadsUpViewBinder headsUpViewBinder, NotificationClicker.Builder builder, AnimatedImageNotificationManager animatedImageNotificationManager, PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        return new NotificationsControllerImpl(featureFlags, notificationListener, notificationEntryManager, notificationRankingManager, lazy, targetSdkResolver, lazy2, notifBindPipelineInitializer, deviceProvisionedController, notificationRowBinderImpl, remoteInputUriController, lazy3, notificationGroupAlertTransferHelper, headsUpManager, headsUpController, headsUpViewBinder, builder, animatedImageNotificationManager, peopleSpaceWidgetManager);
    }
}
