package com.android.systemui.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideLocalBluetoothControllerFactory implements Factory<LocalBluetoothManager> {
    private final Provider<Handler> bgHandlerProvider;
    private final Provider<Context> contextProvider;

    public DependencyProvider_ProvideLocalBluetoothControllerFactory(Provider<Context> provider, Provider<Handler> provider2) {
        this.contextProvider = provider;
        this.bgHandlerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public LocalBluetoothManager get() {
        return provideLocalBluetoothController(this.contextProvider.get(), this.bgHandlerProvider.get());
    }

    public static DependencyProvider_ProvideLocalBluetoothControllerFactory create(Provider<Context> provider, Provider<Handler> provider2) {
        return new DependencyProvider_ProvideLocalBluetoothControllerFactory(provider, provider2);
    }

    public static LocalBluetoothManager provideLocalBluetoothController(Context context, Handler handler) {
        return DependencyProvider.provideLocalBluetoothController(context, handler);
    }
}
