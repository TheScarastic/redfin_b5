package com.android.settingslib.media;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import com.android.settingslib.bluetooth.BluetoothUtils;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
/* loaded from: classes.dex */
public class BluetoothMediaDevice extends MediaDevice {
    private CachedBluetoothDevice mCachedDevice;

    /* access modifiers changed from: package-private */
    public BluetoothMediaDevice(Context context, CachedBluetoothDevice cachedBluetoothDevice, MediaRouter2Manager mediaRouter2Manager, MediaRoute2Info mediaRoute2Info, String str) {
        super(context, mediaRouter2Manager, mediaRoute2Info, str);
        this.mCachedDevice = cachedBluetoothDevice;
        initDeviceRecord();
    }

    @Override // com.android.settingslib.media.MediaDevice
    public String getName() {
        return this.mCachedDevice.getName();
    }

    @Override // com.android.settingslib.media.MediaDevice
    public Drawable getIcon() {
        Drawable drawable = (Drawable) BluetoothUtils.getBtDrawableWithDescription(this.mContext, this.mCachedDevice).first;
        if (!(drawable instanceof BitmapDrawable)) {
            setColorFilter(drawable);
        }
        return BluetoothUtils.buildAdvancedDrawable(this.mContext, drawable);
    }

    @Override // com.android.settingslib.media.MediaDevice
    public Drawable getIconWithoutBackground() {
        return (Drawable) BluetoothUtils.getBtClassDrawableWithDescription(this.mContext, this.mCachedDevice).first;
    }

    @Override // com.android.settingslib.media.MediaDevice
    public String getId() {
        return MediaDeviceUtils.getId(this.mCachedDevice);
    }

    public CachedBluetoothDevice getCachedDevice() {
        return this.mCachedDevice;
    }

    @Override // com.android.settingslib.media.MediaDevice
    protected boolean isCarKitDevice() {
        BluetoothClass bluetoothClass = this.mCachedDevice.getDevice().getBluetoothClass();
        if (bluetoothClass == null) {
            return false;
        }
        int deviceClass = bluetoothClass.getDeviceClass();
        return deviceClass == 1032 || deviceClass == 1056;
    }

    @Override // com.android.settingslib.media.MediaDevice
    public boolean isFastPairDevice() {
        CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevice;
        return cachedBluetoothDevice != null && BluetoothUtils.getBooleanMetaData(cachedBluetoothDevice.getDevice(), 6);
    }

    @Override // com.android.settingslib.media.MediaDevice
    public boolean isConnected() {
        return this.mCachedDevice.getBondState() == 12 && this.mCachedDevice.isConnected();
    }
}
