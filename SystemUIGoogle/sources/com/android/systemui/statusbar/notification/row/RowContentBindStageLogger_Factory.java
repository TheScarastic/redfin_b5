package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class RowContentBindStageLogger_Factory implements Factory<RowContentBindStageLogger> {
    private final Provider<LogBuffer> bufferProvider;

    public RowContentBindStageLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    @Override // javax.inject.Provider
    public RowContentBindStageLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static RowContentBindStageLogger_Factory create(Provider<LogBuffer> provider) {
        return new RowContentBindStageLogger_Factory(provider);
    }

    public static RowContentBindStageLogger newInstance(LogBuffer logBuffer) {
        return new RowContentBindStageLogger(logBuffer);
    }
}
