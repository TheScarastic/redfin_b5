package com.google.android.systemui.assist.uihints;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ColorChangeHandler_Factory implements Factory<ColorChangeHandler> {
    private final Provider<Context> contextProvider;

    public ColorChangeHandler_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public ColorChangeHandler get() {
        return newInstance(this.contextProvider.get());
    }

    public static ColorChangeHandler_Factory create(Provider<Context> provider) {
        return new ColorChangeHandler_Factory(provider);
    }

    public static ColorChangeHandler newInstance(Context context) {
        return new ColorChangeHandler(context);
    }
}
