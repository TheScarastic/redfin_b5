package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.statusbar.notification.collection.inflation.NotifInflater;
import com.android.systemui.statusbar.notification.collection.render.NotifViewBarn;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PreparationCoordinator_Factory implements Factory<PreparationCoordinator> {
    private final Provider<NotifInflationErrorManager> errorManagerProvider;
    private final Provider<PreparationCoordinatorLogger> loggerProvider;
    private final Provider<NotifInflater> notifInflaterProvider;
    private final Provider<IStatusBarService> serviceProvider;
    private final Provider<NotifViewBarn> viewBarnProvider;

    public PreparationCoordinator_Factory(Provider<PreparationCoordinatorLogger> provider, Provider<NotifInflater> provider2, Provider<NotifInflationErrorManager> provider3, Provider<NotifViewBarn> provider4, Provider<IStatusBarService> provider5) {
        this.loggerProvider = provider;
        this.notifInflaterProvider = provider2;
        this.errorManagerProvider = provider3;
        this.viewBarnProvider = provider4;
        this.serviceProvider = provider5;
    }

    @Override // javax.inject.Provider
    public PreparationCoordinator get() {
        return newInstance(this.loggerProvider.get(), this.notifInflaterProvider.get(), this.errorManagerProvider.get(), this.viewBarnProvider.get(), this.serviceProvider.get());
    }

    public static PreparationCoordinator_Factory create(Provider<PreparationCoordinatorLogger> provider, Provider<NotifInflater> provider2, Provider<NotifInflationErrorManager> provider3, Provider<NotifViewBarn> provider4, Provider<IStatusBarService> provider5) {
        return new PreparationCoordinator_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static PreparationCoordinator newInstance(PreparationCoordinatorLogger preparationCoordinatorLogger, NotifInflater notifInflater, NotifInflationErrorManager notifInflationErrorManager, NotifViewBarn notifViewBarn, IStatusBarService iStatusBarService) {
        return new PreparationCoordinator(preparationCoordinatorLogger, notifInflater, notifInflationErrorManager, notifViewBarn, iStatusBarService);
    }
}
