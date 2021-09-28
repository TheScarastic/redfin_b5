package com.android.systemui.statusbar.phone.dagger;

import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarPhoneDependenciesModule_ProvideNotificationGroupAlertTransferHelperFactory implements Factory<NotificationGroupAlertTransferHelper> {
    private final Provider<RowContentBindStage> bindStageProvider;

    public StatusBarPhoneDependenciesModule_ProvideNotificationGroupAlertTransferHelperFactory(Provider<RowContentBindStage> provider) {
        this.bindStageProvider = provider;
    }

    @Override // javax.inject.Provider
    public NotificationGroupAlertTransferHelper get() {
        return provideNotificationGroupAlertTransferHelper(this.bindStageProvider.get());
    }

    public static StatusBarPhoneDependenciesModule_ProvideNotificationGroupAlertTransferHelperFactory create(Provider<RowContentBindStage> provider) {
        return new StatusBarPhoneDependenciesModule_ProvideNotificationGroupAlertTransferHelperFactory(provider);
    }

    public static NotificationGroupAlertTransferHelper provideNotificationGroupAlertTransferHelper(RowContentBindStage rowContentBindStage) {
        return (NotificationGroupAlertTransferHelper) Preconditions.checkNotNullFromProvides(StatusBarPhoneDependenciesModule.provideNotificationGroupAlertTransferHelper(rowContentBindStage));
    }
}
