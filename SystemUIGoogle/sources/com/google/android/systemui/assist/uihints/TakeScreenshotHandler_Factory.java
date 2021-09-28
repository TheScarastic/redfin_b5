package com.google.android.systemui.assist.uihints;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TakeScreenshotHandler_Factory implements Factory<TakeScreenshotHandler> {
    private final Provider<Context> contextProvider;

    public TakeScreenshotHandler_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public TakeScreenshotHandler get() {
        return newInstance(this.contextProvider.get());
    }

    public static TakeScreenshotHandler_Factory create(Provider<Context> provider) {
        return new TakeScreenshotHandler_Factory(provider);
    }

    public static TakeScreenshotHandler newInstance(Context context) {
        return new TakeScreenshotHandler(context);
    }
}
