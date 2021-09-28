package com.google.android.systemui.assist.uihints;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class OverlayUiHost_Factory implements Factory<OverlayUiHost> {
    private final Provider<Context> contextProvider;
    private final Provider<TouchOutsideHandler> touchOutsideProvider;

    public OverlayUiHost_Factory(Provider<Context> provider, Provider<TouchOutsideHandler> provider2) {
        this.contextProvider = provider;
        this.touchOutsideProvider = provider2;
    }

    @Override // javax.inject.Provider
    public OverlayUiHost get() {
        return newInstance(this.contextProvider.get(), this.touchOutsideProvider.get());
    }

    public static OverlayUiHost_Factory create(Provider<Context> provider, Provider<TouchOutsideHandler> provider2) {
        return new OverlayUiHost_Factory(provider, provider2);
    }

    public static OverlayUiHost newInstance(Context context, Object obj) {
        return new OverlayUiHost(context, (TouchOutsideHandler) obj);
    }
}
