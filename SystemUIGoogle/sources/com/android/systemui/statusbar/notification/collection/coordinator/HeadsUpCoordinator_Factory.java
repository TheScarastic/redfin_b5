package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class HeadsUpCoordinator_Factory implements Factory<HeadsUpCoordinator> {
    private final Provider<HeadsUpManager> headsUpManagerProvider;
    private final Provider<HeadsUpViewBinder> headsUpViewBinderProvider;
    private final Provider<NodeController> incomingHeaderControllerProvider;
    private final Provider<NotificationInterruptStateProvider> notificationInterruptStateProvider;
    private final Provider<NotificationRemoteInputManager> remoteInputManagerProvider;

    public HeadsUpCoordinator_Factory(Provider<HeadsUpManager> provider, Provider<HeadsUpViewBinder> provider2, Provider<NotificationInterruptStateProvider> provider3, Provider<NotificationRemoteInputManager> provider4, Provider<NodeController> provider5) {
        this.headsUpManagerProvider = provider;
        this.headsUpViewBinderProvider = provider2;
        this.notificationInterruptStateProvider = provider3;
        this.remoteInputManagerProvider = provider4;
        this.incomingHeaderControllerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public HeadsUpCoordinator get() {
        return newInstance(this.headsUpManagerProvider.get(), this.headsUpViewBinderProvider.get(), this.notificationInterruptStateProvider.get(), this.remoteInputManagerProvider.get(), this.incomingHeaderControllerProvider.get());
    }

    public static HeadsUpCoordinator_Factory create(Provider<HeadsUpManager> provider, Provider<HeadsUpViewBinder> provider2, Provider<NotificationInterruptStateProvider> provider3, Provider<NotificationRemoteInputManager> provider4, Provider<NodeController> provider5) {
        return new HeadsUpCoordinator_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static HeadsUpCoordinator newInstance(HeadsUpManager headsUpManager, HeadsUpViewBinder headsUpViewBinder, NotificationInterruptStateProvider notificationInterruptStateProvider, NotificationRemoteInputManager notificationRemoteInputManager, NodeController nodeController) {
        return new HeadsUpCoordinator(headsUpManager, headsUpViewBinder, notificationInterruptStateProvider, notificationRemoteInputManager, nodeController);
    }
}
