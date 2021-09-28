package com.android.systemui.media;

import android.content.Context;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LocalMediaManagerFactory_Factory implements Factory<LocalMediaManagerFactory> {
    private final Provider<Context> contextProvider;
    private final Provider<LocalBluetoothManager> localBluetoothManagerProvider;

    public LocalMediaManagerFactory_Factory(Provider<Context> provider, Provider<LocalBluetoothManager> provider2) {
        this.contextProvider = provider;
        this.localBluetoothManagerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public LocalMediaManagerFactory get() {
        return newInstance(this.contextProvider.get(), this.localBluetoothManagerProvider.get());
    }

    public static LocalMediaManagerFactory_Factory create(Provider<Context> provider, Provider<LocalBluetoothManager> provider2) {
        return new LocalMediaManagerFactory_Factory(provider, provider2);
    }

    public static LocalMediaManagerFactory newInstance(Context context, LocalBluetoothManager localBluetoothManager) {
        return new LocalMediaManagerFactory(context, localBluetoothManager);
    }
}
