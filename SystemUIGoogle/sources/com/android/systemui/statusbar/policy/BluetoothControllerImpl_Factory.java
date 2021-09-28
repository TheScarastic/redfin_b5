package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.os.Looper;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BluetoothControllerImpl_Factory implements Factory<BluetoothControllerImpl> {
    private final Provider<Looper> bgLooperProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<LocalBluetoothManager> localBluetoothManagerProvider;
    private final Provider<Looper> mainLooperProvider;

    public BluetoothControllerImpl_Factory(Provider<Context> provider, Provider<DumpManager> provider2, Provider<Looper> provider3, Provider<Looper> provider4, Provider<LocalBluetoothManager> provider5) {
        this.contextProvider = provider;
        this.dumpManagerProvider = provider2;
        this.bgLooperProvider = provider3;
        this.mainLooperProvider = provider4;
        this.localBluetoothManagerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public BluetoothControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.dumpManagerProvider.get(), this.bgLooperProvider.get(), this.mainLooperProvider.get(), this.localBluetoothManagerProvider.get());
    }

    public static BluetoothControllerImpl_Factory create(Provider<Context> provider, Provider<DumpManager> provider2, Provider<Looper> provider3, Provider<Looper> provider4, Provider<LocalBluetoothManager> provider5) {
        return new BluetoothControllerImpl_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static BluetoothControllerImpl newInstance(Context context, DumpManager dumpManager, Looper looper, Looper looper2, LocalBluetoothManager localBluetoothManager) {
        return new BluetoothControllerImpl(context, dumpManager, looper, looper2, localBluetoothManager);
    }
}
