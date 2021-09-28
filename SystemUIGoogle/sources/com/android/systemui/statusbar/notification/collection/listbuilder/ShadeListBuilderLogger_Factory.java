package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ShadeListBuilderLogger_Factory implements Factory<ShadeListBuilderLogger> {
    private final Provider<LogBuffer> bufferProvider;

    public ShadeListBuilderLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    @Override // javax.inject.Provider
    public ShadeListBuilderLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static ShadeListBuilderLogger_Factory create(Provider<LogBuffer> provider) {
        return new ShadeListBuilderLogger_Factory(provider);
    }

    public static ShadeListBuilderLogger newInstance(LogBuffer logBuffer) {
        return new ShadeListBuilderLogger(logBuffer);
    }
}
