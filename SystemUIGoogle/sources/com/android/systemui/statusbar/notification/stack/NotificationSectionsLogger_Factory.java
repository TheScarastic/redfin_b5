package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationSectionsLogger_Factory implements Factory<NotificationSectionsLogger> {
    private final Provider<LogBuffer> logBufferProvider;

    public NotificationSectionsLogger_Factory(Provider<LogBuffer> provider) {
        this.logBufferProvider = provider;
    }

    @Override // javax.inject.Provider
    public NotificationSectionsLogger get() {
        return newInstance(this.logBufferProvider.get());
    }

    public static NotificationSectionsLogger_Factory create(Provider<LogBuffer> provider) {
        return new NotificationSectionsLogger_Factory(provider);
    }

    public static NotificationSectionsLogger newInstance(LogBuffer logBuffer) {
        return new NotificationSectionsLogger(logBuffer);
    }
}
