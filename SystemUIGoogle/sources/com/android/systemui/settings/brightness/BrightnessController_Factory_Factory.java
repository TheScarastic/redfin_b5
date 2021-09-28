package com.android.systemui.settings.brightness;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.brightness.BrightnessController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BrightnessController_Factory_Factory implements Factory<BrightnessController.Factory> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;

    public BrightnessController_Factory_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
    }

    @Override // javax.inject.Provider
    public BrightnessController.Factory get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get());
    }

    public static BrightnessController_Factory_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2) {
        return new BrightnessController_Factory_Factory(provider, provider2);
    }

    public static BrightnessController.Factory newInstance(Context context, BroadcastDispatcher broadcastDispatcher) {
        return new BrightnessController.Factory(context, broadcastDispatcher);
    }
}
