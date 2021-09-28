package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothPan;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes.dex */
public class PanProfile implements LocalBluetoothProfile {
    private final HashMap<BluetoothDevice, Integer> mDeviceRoleMap = new HashMap<>();
    private boolean mIsProfileReady;
    private BluetoothPan mService;

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean accessProfileEnabled() {
        return true;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302331;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getProfileId() {
        return 5;
    }

    public String toString() {
        return "PAN";
    }

    /* loaded from: classes.dex */
    private final class PanServiceListener implements BluetoothProfile.ServiceListener {
        private PanServiceListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            PanProfile.this.mService = (BluetoothPan) bluetoothProfile;
            PanProfile.this.mIsProfileReady = true;
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            PanProfile.this.mIsProfileReady = false;
        }
    }

    /* access modifiers changed from: package-private */
    public PanProfile(Context context) {
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new PanServiceListener(), 5);
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothPan bluetoothPan = this.mService;
        if (bluetoothPan == null) {
            return 0;
        }
        return bluetoothPan.getConnectionState(bluetoothDevice);
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothPan bluetoothPan = this.mService;
        if (bluetoothPan == null) {
            return false;
        }
        if (!z) {
            return bluetoothPan.setConnectionPolicy(bluetoothDevice, 0);
        }
        List<BluetoothDevice> connectedDevices = bluetoothPan.getConnectedDevices();
        if (connectedDevices != null) {
            for (BluetoothDevice bluetoothDevice2 : connectedDevices) {
                this.mService.setConnectionPolicy(bluetoothDevice2, 0);
            }
        }
        return this.mService.setConnectionPolicy(bluetoothDevice, 100);
    }

    /* access modifiers changed from: package-private */
    public void setLocalRole(BluetoothDevice bluetoothDevice, int i) {
        this.mDeviceRoleMap.put(bluetoothDevice, Integer.valueOf(i));
    }

    /* access modifiers changed from: package-private */
    public boolean isLocalRoleNap(BluetoothDevice bluetoothDevice) {
        if (!this.mDeviceRoleMap.containsKey(bluetoothDevice) || this.mDeviceRoleMap.get(bluetoothDevice).intValue() != 1) {
            return false;
        }
        return true;
    }

    protected void finalize() {
        Log.d("PanProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(5, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("PanProfile", "Error cleaning up PAN proxy", th);
            }
        }
    }
}
