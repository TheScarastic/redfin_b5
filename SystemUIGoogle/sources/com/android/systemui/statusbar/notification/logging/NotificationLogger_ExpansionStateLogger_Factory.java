package com.android.systemui.statusbar.notification.logging;

import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationLogger_ExpansionStateLogger_Factory implements Factory<NotificationLogger.ExpansionStateLogger> {
    private final Provider<Executor> uiBgExecutorProvider;

    public NotificationLogger_ExpansionStateLogger_Factory(Provider<Executor> provider) {
        this.uiBgExecutorProvider = provider;
    }

    @Override // javax.inject.Provider
    public NotificationLogger.ExpansionStateLogger get() {
        return newInstance(this.uiBgExecutorProvider.get());
    }

    public static NotificationLogger_ExpansionStateLogger_Factory create(Provider<Executor> provider) {
        return new NotificationLogger_ExpansionStateLogger_Factory(provider);
    }

    public static NotificationLogger.ExpansionStateLogger newInstance(Executor executor) {
        return new NotificationLogger.ExpansionStateLogger(executor);
    }
}
