package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.FeatureFlags;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotifCoordinators_Factory implements Factory<NotifCoordinators> {
    private final Provider<AppOpsCoordinator> appOpsCoordinatorProvider;
    private final Provider<BubbleCoordinator> bubbleCoordinatorProvider;
    private final Provider<ConversationCoordinator> conversationCoordinatorProvider;
    private final Provider<DeviceProvisionedCoordinator> deviceProvisionedCoordinatorProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<HeadsUpCoordinator> headsUpCoordinatorProvider;
    private final Provider<HideNotifsForOtherUsersCoordinator> hideNotifsForOtherUsersCoordinatorProvider;
    private final Provider<KeyguardCoordinator> keyguardCoordinatorProvider;
    private final Provider<MediaCoordinator> mediaCoordinatorProvider;
    private final Provider<PreparationCoordinator> preparationCoordinatorProvider;
    private final Provider<RankingCoordinator> rankingCoordinatorProvider;
    private final Provider<SmartspaceDedupingCoordinator> smartspaceDedupingCoordinatorProvider;
    private final Provider<VisualStabilityCoordinator> visualStabilityCoordinatorProvider;

    public NotifCoordinators_Factory(Provider<DumpManager> provider, Provider<FeatureFlags> provider2, Provider<HideNotifsForOtherUsersCoordinator> provider3, Provider<KeyguardCoordinator> provider4, Provider<RankingCoordinator> provider5, Provider<AppOpsCoordinator> provider6, Provider<DeviceProvisionedCoordinator> provider7, Provider<BubbleCoordinator> provider8, Provider<HeadsUpCoordinator> provider9, Provider<ConversationCoordinator> provider10, Provider<PreparationCoordinator> provider11, Provider<MediaCoordinator> provider12, Provider<SmartspaceDedupingCoordinator> provider13, Provider<VisualStabilityCoordinator> provider14) {
        this.dumpManagerProvider = provider;
        this.featureFlagsProvider = provider2;
        this.hideNotifsForOtherUsersCoordinatorProvider = provider3;
        this.keyguardCoordinatorProvider = provider4;
        this.rankingCoordinatorProvider = provider5;
        this.appOpsCoordinatorProvider = provider6;
        this.deviceProvisionedCoordinatorProvider = provider7;
        this.bubbleCoordinatorProvider = provider8;
        this.headsUpCoordinatorProvider = provider9;
        this.conversationCoordinatorProvider = provider10;
        this.preparationCoordinatorProvider = provider11;
        this.mediaCoordinatorProvider = provider12;
        this.smartspaceDedupingCoordinatorProvider = provider13;
        this.visualStabilityCoordinatorProvider = provider14;
    }

    @Override // javax.inject.Provider
    public NotifCoordinators get() {
        return newInstance(this.dumpManagerProvider.get(), this.featureFlagsProvider.get(), this.hideNotifsForOtherUsersCoordinatorProvider.get(), this.keyguardCoordinatorProvider.get(), this.rankingCoordinatorProvider.get(), this.appOpsCoordinatorProvider.get(), this.deviceProvisionedCoordinatorProvider.get(), this.bubbleCoordinatorProvider.get(), this.headsUpCoordinatorProvider.get(), this.conversationCoordinatorProvider.get(), this.preparationCoordinatorProvider.get(), this.mediaCoordinatorProvider.get(), this.smartspaceDedupingCoordinatorProvider.get(), this.visualStabilityCoordinatorProvider.get());
    }

    public static NotifCoordinators_Factory create(Provider<DumpManager> provider, Provider<FeatureFlags> provider2, Provider<HideNotifsForOtherUsersCoordinator> provider3, Provider<KeyguardCoordinator> provider4, Provider<RankingCoordinator> provider5, Provider<AppOpsCoordinator> provider6, Provider<DeviceProvisionedCoordinator> provider7, Provider<BubbleCoordinator> provider8, Provider<HeadsUpCoordinator> provider9, Provider<ConversationCoordinator> provider10, Provider<PreparationCoordinator> provider11, Provider<MediaCoordinator> provider12, Provider<SmartspaceDedupingCoordinator> provider13, Provider<VisualStabilityCoordinator> provider14) {
        return new NotifCoordinators_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    public static NotifCoordinators newInstance(DumpManager dumpManager, FeatureFlags featureFlags, HideNotifsForOtherUsersCoordinator hideNotifsForOtherUsersCoordinator, KeyguardCoordinator keyguardCoordinator, RankingCoordinator rankingCoordinator, AppOpsCoordinator appOpsCoordinator, DeviceProvisionedCoordinator deviceProvisionedCoordinator, BubbleCoordinator bubbleCoordinator, HeadsUpCoordinator headsUpCoordinator, ConversationCoordinator conversationCoordinator, PreparationCoordinator preparationCoordinator, MediaCoordinator mediaCoordinator, SmartspaceDedupingCoordinator smartspaceDedupingCoordinator, VisualStabilityCoordinator visualStabilityCoordinator) {
        return new NotifCoordinators(dumpManager, featureFlags, hideNotifsForOtherUsersCoordinator, keyguardCoordinator, rankingCoordinator, appOpsCoordinator, deviceProvisionedCoordinator, bubbleCoordinator, headsUpCoordinator, conversationCoordinator, preparationCoordinator, mediaCoordinator, smartspaceDedupingCoordinator, visualStabilityCoordinator);
    }
}
