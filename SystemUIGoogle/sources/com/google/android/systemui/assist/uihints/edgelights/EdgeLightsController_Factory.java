package com.google.android.systemui.assist.uihints.edgelights;

import android.content.Context;
import android.view.ViewGroup;
import com.android.systemui.assist.AssistLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class EdgeLightsController_Factory implements Factory<EdgeLightsController> {
    private final Provider<AssistLogger> assistLoggerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<ViewGroup> parentProvider;

    public EdgeLightsController_Factory(Provider<Context> provider, Provider<ViewGroup> provider2, Provider<AssistLogger> provider3) {
        this.contextProvider = provider;
        this.parentProvider = provider2;
        this.assistLoggerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public EdgeLightsController get() {
        return newInstance(this.contextProvider.get(), this.parentProvider.get(), this.assistLoggerProvider.get());
    }

    public static EdgeLightsController_Factory create(Provider<Context> provider, Provider<ViewGroup> provider2, Provider<AssistLogger> provider3) {
        return new EdgeLightsController_Factory(provider, provider2, provider3);
    }

    public static EdgeLightsController newInstance(Context context, ViewGroup viewGroup, AssistLogger assistLogger) {
        return new EdgeLightsController(context, viewGroup, assistLogger);
    }
}
