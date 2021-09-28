package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class RankingCoordinator_Factory implements Factory<RankingCoordinator> {
    private final Provider<NodeController> alertingHeaderControllerProvider;
    private final Provider<HighPriorityProvider> highPriorityProvider;
    private final Provider<NodeController> silentHeaderControllerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public RankingCoordinator_Factory(Provider<StatusBarStateController> provider, Provider<HighPriorityProvider> provider2, Provider<NodeController> provider3, Provider<NodeController> provider4) {
        this.statusBarStateControllerProvider = provider;
        this.highPriorityProvider = provider2;
        this.alertingHeaderControllerProvider = provider3;
        this.silentHeaderControllerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public RankingCoordinator get() {
        return newInstance(this.statusBarStateControllerProvider.get(), this.highPriorityProvider.get(), this.alertingHeaderControllerProvider.get(), this.silentHeaderControllerProvider.get());
    }

    public static RankingCoordinator_Factory create(Provider<StatusBarStateController> provider, Provider<HighPriorityProvider> provider2, Provider<NodeController> provider3, Provider<NodeController> provider4) {
        return new RankingCoordinator_Factory(provider, provider2, provider3, provider4);
    }

    public static RankingCoordinator newInstance(StatusBarStateController statusBarStateController, HighPriorityProvider highPriorityProvider, NodeController nodeController, NodeController nodeController2) {
        return new RankingCoordinator(statusBarStateController, highPriorityProvider, nodeController, nodeController2);
    }
}
