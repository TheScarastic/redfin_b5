package com.google.android.systemui.assist.uihints;

import android.content.Context;
import android.view.ViewGroup;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class GlowController_Factory implements Factory<GlowController> {
    private final Provider<Context> contextProvider;
    private final Provider<ViewGroup> parentProvider;
    private final Provider<TouchInsideHandler> touchInsideHandlerProvider;

    public GlowController_Factory(Provider<Context> provider, Provider<ViewGroup> provider2, Provider<TouchInsideHandler> provider3) {
        this.contextProvider = provider;
        this.parentProvider = provider2;
        this.touchInsideHandlerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public GlowController get() {
        return newInstance(this.contextProvider.get(), this.parentProvider.get(), this.touchInsideHandlerProvider.get());
    }

    public static GlowController_Factory create(Provider<Context> provider, Provider<ViewGroup> provider2, Provider<TouchInsideHandler> provider3) {
        return new GlowController_Factory(provider, provider2, provider3);
    }

    public static GlowController newInstance(Context context, ViewGroup viewGroup, TouchInsideHandler touchInsideHandler) {
        return new GlowController(context, viewGroup, touchInsideHandler);
    }
}
