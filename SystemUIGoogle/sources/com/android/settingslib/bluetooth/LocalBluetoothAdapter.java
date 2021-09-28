package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import java.util.Set;
@Deprecated
/* loaded from: classes.dex */
public class LocalBluetoothAdapter {
    private static LocalBluetoothAdapter sInstance;
    private final BluetoothAdapter mAdapter;
    private LocalBluetoothProfileManager mProfileManager;
    private int mState = Integer.MIN_VALUE;

    private LocalBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.mAdapter = bluetoothAdapter;
    }

    /* access modifiers changed from: package-private */
    public void setProfileManager(LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mProfileManager = localBluetoothProfileManager;
    }

    /* access modifiers changed from: package-private */
    public static synchronized LocalBluetoothAdapter getInstance() {
        LocalBluetoothAdapter localBluetoothAdapter;
        BluetoothAdapter defaultAdapter;
        synchronized (LocalBluetoothAdapter.class) {
            if (sInstance == null && (defaultAdapter = BluetoothAdapter.getDefaultAdapter()) != null) {
                sInstance = new LocalBluetoothAdapter(defaultAdapter);
            }
            localBluetoothAdapter = sInstance;
        }
        return localBluetoothAdapter;
    }

    public boolean enable() {
        return this.mAdapter.enable();
    }

    public Set<BluetoothDevice> getBondedDevices() {
        return this.mAdapter.getBondedDevices();
    }

    public BluetoothLeScanner getBluetoothLeScanner() {
        return this.mAdapter.getBluetoothLeScanner();
    }

    public int getState() {
        return this.mAdapter.getState();
    }

    public int getConnectionState() {
        return this.mAdapter.getConnectionState();
    }

    public synchronized int getBluetoothState() {
        syncBluetoothState();
        return this.mState;
    }

    /* access modifiers changed from: package-private */
    public void setBluetoothStateInt(int i) {
        LocalBluetoothProfileManager localBluetoothProfileManager;
        synchronized (this) {
            if (this.mState != i) {
                this.mState = i;
                if (i == 12 && (localBluetoothProfileManager = this.mProfileManager) != null) {
                    localBluetoothProfileManager.setBluetoothStateOn();
                }
            }
        }
    }

    boolean syncBluetoothState() {
        if (this.mAdapter.getState() == this.mState) {
            return false;
        }
        setBluetoothStateInt(this.mAdapter.getState());
        return true;
    }

    public boolean setBluetoothEnabled(boolean z) {
        boolean z2;
        if (z) {
            z2 = this.mAdapter.enable();
        } else {
            z2 = this.mAdapter.disable();
        }
        if (z2) {
            setBluetoothStateInt(z ? 11 : 13);
        } else {
            syncBluetoothState();
        }
        return z2;
    }
}
