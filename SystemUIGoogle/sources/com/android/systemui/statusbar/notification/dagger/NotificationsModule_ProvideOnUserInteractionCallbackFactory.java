package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationsModule_ProvideOnUserInteractionCallbackFactory implements Factory<OnUserInteractionCallback> {
    private final Provider<NotificationEntryManager> entryManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<GroupMembershipManager> groupMembershipManagerLazyProvider;
    private final Provider<HeadsUpManager> headsUpManagerProvider;
    private final Provider<NotifCollection> notifCollectionProvider;
    private final Provider<NotifPipeline> pipelineProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<VisualStabilityCoordinator> visualStabilityCoordinatorProvider;
    private final Provider<VisualStabilityManager> visualStabilityManagerProvider;

    public NotificationsModule_ProvideOnUserInteractionCallbackFactory(Provider<FeatureFlags> provider, Provider<HeadsUpManager> provider2, Provider<StatusBarStateController> provider3, Provider<NotifPipeline> provider4, Provider<NotifCollection> provider5, Provider<VisualStabilityCoordinator> provider6, Provider<NotificationEntryManager> provider7, Provider<VisualStabilityManager> provider8, Provider<GroupMembershipManager> provider9) {
        this.featureFlagsProvider = provider;
        this.headsUpManagerProvider = provider2;
        this.statusBarStateControllerProvider = provider3;
        this.pipelineProvider = provider4;
        this.notifCollectionProvider = provider5;
        this.visualStabilityCoordinatorProvider = provider6;
        this.entryManagerProvider = provider7;
        this.visualStabilityManagerProvider = provider8;
        this.groupMembershipManagerLazyProvider = provider9;
    }

    @Override // javax.inject.Provider
    public OnUserInteractionCallback get() {
        return provideOnUserInteractionCallback(this.featureFlagsProvider.get(), this.headsUpManagerProvider.get(), this.statusBarStateControllerProvider.get(), DoubleCheck.lazy(this.pipelineProvider), DoubleCheck.lazy(this.notifCollectionProvider), DoubleCheck.lazy(this.visualStabilityCoordinatorProvider), this.entryManagerProvider.get(), this.visualStabilityManagerProvider.get(), DoubleCheck.lazy(this.groupMembershipManagerLazyProvider));
    }

    public static NotificationsModule_ProvideOnUserInteractionCallbackFactory create(Provider<FeatureFlags> provider, Provider<HeadsUpManager> provider2, Provider<StatusBarStateController> provider3, Provider<NotifPipeline> provider4, Provider<NotifCollection> provider5, Provider<VisualStabilityCoordinator> provider6, Provider<NotificationEntryManager> provider7, Provider<VisualStabilityManager> provider8, Provider<GroupMembershipManager> provider9) {
        return new NotificationsModule_ProvideOnUserInteractionCallbackFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static OnUserInteractionCallback provideOnUserInteractionCallback(FeatureFlags featureFlags, HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController, Lazy<NotifPipeline> lazy, Lazy<NotifCollection> lazy2, Lazy<VisualStabilityCoordinator> lazy3, NotificationEntryManager notificationEntryManager, VisualStabilityManager visualStabilityManager, Lazy<GroupMembershipManager> lazy4) {
        return (OnUserInteractionCallback) Preconditions.checkNotNullFromProvides(NotificationsModule.provideOnUserInteractionCallback(featureFlags, headsUpManager, statusBarStateController, lazy, lazy2, lazy3, notificationEntryManager, visualStabilityManager, lazy4));
    }
}
