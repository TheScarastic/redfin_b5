package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.ForegroundServiceDismissalFeatureController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ForegroundServiceSectionController_Factory implements Factory<ForegroundServiceSectionController> {
    private final Provider<NotificationEntryManager> entryManagerProvider;
    private final Provider<ForegroundServiceDismissalFeatureController> featureControllerProvider;

    public ForegroundServiceSectionController_Factory(Provider<NotificationEntryManager> provider, Provider<ForegroundServiceDismissalFeatureController> provider2) {
        this.entryManagerProvider = provider;
        this.featureControllerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public ForegroundServiceSectionController get() {
        return newInstance(this.entryManagerProvider.get(), this.featureControllerProvider.get());
    }

    public static ForegroundServiceSectionController_Factory create(Provider<NotificationEntryManager> provider, Provider<ForegroundServiceDismissalFeatureController> provider2) {
        return new ForegroundServiceSectionController_Factory(provider, provider2);
    }

    public static ForegroundServiceSectionController newInstance(NotificationEntryManager notificationEntryManager, ForegroundServiceDismissalFeatureController foregroundServiceDismissalFeatureController) {
        return new ForegroundServiceSectionController(notificationEntryManager, foregroundServiceDismissalFeatureController);
    }
}
