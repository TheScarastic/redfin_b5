package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationClickerLogger_Factory implements Factory<NotificationClickerLogger> {
    private final Provider<LogBuffer> bufferProvider;

    public NotificationClickerLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    @Override // javax.inject.Provider
    public NotificationClickerLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static NotificationClickerLogger_Factory create(Provider<LogBuffer> provider) {
        return new NotificationClickerLogger_Factory(provider);
    }

    public static NotificationClickerLogger newInstance(LogBuffer logBuffer) {
        return new NotificationClickerLogger(logBuffer);
    }
}
