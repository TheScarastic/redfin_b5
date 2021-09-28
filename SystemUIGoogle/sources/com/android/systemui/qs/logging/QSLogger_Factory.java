package com.android.systemui.qs.logging;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSLogger_Factory implements Factory<QSLogger> {
    private final Provider<LogBuffer> bufferProvider;

    public QSLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    @Override // javax.inject.Provider
    public QSLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static QSLogger_Factory create(Provider<LogBuffer> provider) {
        return new QSLogger_Factory(provider);
    }

    public static QSLogger newInstance(LogBuffer logBuffer) {
        return new QSLogger(logBuffer);
    }
}
