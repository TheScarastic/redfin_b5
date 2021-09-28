package com.android.systemui.statusbar.notification.row;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class RowContentBindStage_Factory implements Factory<RowContentBindStage> {
    private final Provider<NotificationRowContentBinder> binderProvider;
    private final Provider<NotifInflationErrorManager> errorManagerProvider;
    private final Provider<RowContentBindStageLogger> loggerProvider;

    public RowContentBindStage_Factory(Provider<NotificationRowContentBinder> provider, Provider<NotifInflationErrorManager> provider2, Provider<RowContentBindStageLogger> provider3) {
        this.binderProvider = provider;
        this.errorManagerProvider = provider2;
        this.loggerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public RowContentBindStage get() {
        return newInstance(this.binderProvider.get(), this.errorManagerProvider.get(), this.loggerProvider.get());
    }

    public static RowContentBindStage_Factory create(Provider<NotificationRowContentBinder> provider, Provider<NotifInflationErrorManager> provider2, Provider<RowContentBindStageLogger> provider3) {
        return new RowContentBindStage_Factory(provider, provider2, provider3);
    }

    public static RowContentBindStage newInstance(NotificationRowContentBinder notificationRowContentBinder, NotifInflationErrorManager notifInflationErrorManager, RowContentBindStageLogger rowContentBindStageLogger) {
        return new RowContentBindStage(notificationRowContentBinder, notifInflationErrorManager, rowContentBindStageLogger);
    }
}
