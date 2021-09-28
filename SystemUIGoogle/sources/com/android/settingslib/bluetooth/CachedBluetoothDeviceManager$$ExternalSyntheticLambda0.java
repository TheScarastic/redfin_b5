package com.android.settingslib.bluetooth;

import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class CachedBluetoothDeviceManager$$ExternalSyntheticLambda0 implements Predicate {
    public static final /* synthetic */ CachedBluetoothDeviceManager$$ExternalSyntheticLambda0 INSTANCE = new CachedBluetoothDeviceManager$$ExternalSyntheticLambda0();

    private /* synthetic */ CachedBluetoothDeviceManager$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return CachedBluetoothDeviceManager.lambda$clearNonBondedDevices$0((CachedBluetoothDevice) obj);
    }
}
