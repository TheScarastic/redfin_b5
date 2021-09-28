package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationEntryManagerLogger_Factory implements Factory<NotificationEntryManagerLogger> {
    private final Provider<LogBuffer> bufferProvider;

    public NotificationEntryManagerLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    @Override // javax.inject.Provider
    public NotificationEntryManagerLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static NotificationEntryManagerLogger_Factory create(Provider<LogBuffer> provider) {
        return new NotificationEntryManagerLogger_Factory(provider);
    }

    public static NotificationEntryManagerLogger newInstance(LogBuffer logBuffer) {
        return new NotificationEntryManagerLogger(logBuffer);
    }
}
