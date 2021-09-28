package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ShadeViewDifferLogger_Factory implements Factory<ShadeViewDifferLogger> {
    private final Provider<LogBuffer> bufferProvider;

    public ShadeViewDifferLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    @Override // javax.inject.Provider
    public ShadeViewDifferLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static ShadeViewDifferLogger_Factory create(Provider<LogBuffer> provider) {
        return new ShadeViewDifferLogger_Factory(provider);
    }

    public static ShadeViewDifferLogger newInstance(LogBuffer logBuffer) {
        return new ShadeViewDifferLogger(logBuffer);
    }
}
