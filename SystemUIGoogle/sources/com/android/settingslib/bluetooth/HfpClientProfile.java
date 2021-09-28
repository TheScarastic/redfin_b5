package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadsetClient;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import java.util.List;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class HfpClientProfile implements LocalBluetoothProfile {
    static final ParcelUuid[] SRC_UUIDS = {BluetoothUuid.HSP_AG, BluetoothUuid.HFP_AG};
    private final CachedBluetoothDeviceManager mDeviceManager;
    private boolean mIsProfileReady;
    private final LocalBluetoothProfileManager mProfileManager;
    private BluetoothHeadsetClient mService;

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean accessProfileEnabled() {
        return true;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302327;
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getProfileId() {
        return 16;
    }

    public String toString() {
        return "HEADSET_CLIENT";
    }

    /* loaded from: classes.dex */
    private final class HfpClientServiceListener implements BluetoothProfile.ServiceListener {
        private HfpClientServiceListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            HfpClientProfile.this.mService = (BluetoothHeadsetClient) bluetoothProfile;
            List connectedDevices = HfpClientProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = HfpClientProfile.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    Log.w("HfpClientProfile", "HfpClient profile found new device: " + bluetoothDevice);
                    findDevice = HfpClientProfile.this.mDeviceManager.addDevice(bluetoothDevice);
                }
                findDevice.onProfileStateChanged(HfpClientProfile.this, 2);
                findDevice.refresh();
            }
            HfpClientProfile.this.mIsProfileReady = true;
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            HfpClientProfile.this.mIsProfileReady = false;
        }
    }

    /* access modifiers changed from: package-private */
    public HfpClientProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, new HfpClientServiceListener(), 16);
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothHeadsetClient bluetoothHeadsetClient = this.mService;
        if (bluetoothHeadsetClient == null) {
            return 0;
        }
        return bluetoothHeadsetClient.getConnectionState(bluetoothDevice);
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfile
    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothHeadsetClient bluetoothHeadsetClient = this.mService;
        if (bluetoothHeadsetClient == null) {
            return false;
        }
        if (!z) {
            return bluetoothHeadsetClient.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothHeadsetClient.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    protected void finalize() {
        Log.d("HfpClientProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(16, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("HfpClientProfile", "Error cleaning up HfpClient proxy", th);
            }
        }
    }
}
