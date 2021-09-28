package com.android.systemui.toast;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ToastLogger_Factory implements Factory<ToastLogger> {
    private final Provider<LogBuffer> bufferProvider;

    public ToastLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    @Override // javax.inject.Provider
    public ToastLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static ToastLogger_Factory create(Provider<LogBuffer> provider) {
        return new ToastLogger_Factory(provider);
    }

    public static ToastLogger newInstance(LogBuffer logBuffer) {
        return new ToastLogger(logBuffer);
    }
}
