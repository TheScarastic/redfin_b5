package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.util.LruCache;
import com.android.internal.util.ArrayUtils;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public class CachedBluetoothDevice implements Comparable<CachedBluetoothDevice> {
    private long mConnectAttempted;
    private final Context mContext;
    BluetoothDevice mDevice;
    LruCache<String, BitmapDrawable> mDrawableCache;
    boolean mJustDiscovered;
    private boolean mLocalNapRoleConnected;
    private final LocalBluetoothProfileManager mProfileManager;
    short mRssi;
    private CachedBluetoothDevice mSubDevice;
    private final Object mProfileLock = new Object();
    private final Collection<LocalBluetoothProfile> mProfiles = new CopyOnWriteArrayList();
    private final Collection<LocalBluetoothProfile> mRemovedProfiles = new CopyOnWriteArrayList();
    private final Collection<Callback> mCallbacks = new CopyOnWriteArrayList();
    private boolean mIsActiveDeviceA2dp = false;
    private boolean mIsActiveDeviceHeadset = false;
    private boolean mIsActiveDeviceHearingAid = false;
    private boolean mIsA2dpProfileConnectedFail = false;
    private boolean mIsHeadsetProfileConnectedFail = false;
    private boolean mIsHearingAidProfileConnectedFail = false;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.android.settingslib.bluetooth.CachedBluetoothDevice.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                CachedBluetoothDevice.this.mIsHeadsetProfileConnectedFail = true;
            } else if (i == 2) {
                CachedBluetoothDevice.this.mIsA2dpProfileConnectedFail = true;
            } else if (i != 21) {
                Log.w("CachedBluetoothDevice", "handleMessage(): unknown message : " + message.what);
            } else {
                CachedBluetoothDevice.this.mIsHearingAidProfileConnectedFail = true;
            }
            Log.w("CachedBluetoothDevice", "Connect to profile : " + message.what + " timeout, show error message !");
            CachedBluetoothDevice.this.refresh();
        }
    };
    private final BluetoothAdapter mLocalAdapter = BluetoothAdapter.getDefaultAdapter();
    private long mHiSyncId = 0;

    /* loaded from: classes.dex */
    public interface Callback {
        void onDeviceAttributesChanged();
    }

    /* access modifiers changed from: package-private */
    public CachedBluetoothDevice(Context context, LocalBluetoothProfileManager localBluetoothProfileManager, BluetoothDevice bluetoothDevice) {
        this.mContext = context;
        this.mProfileManager = localBluetoothProfileManager;
        this.mDevice = bluetoothDevice;
        fillData();
        initDrawableCache();
    }

    private void initDrawableCache() {
        this.mDrawableCache = new LruCache<String, BitmapDrawable>(((int) (Runtime.getRuntime().maxMemory() / 1024)) / 8) { // from class: com.android.settingslib.bluetooth.CachedBluetoothDevice.2
            /* access modifiers changed from: protected */
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return bitmapDrawable.getBitmap().getByteCount() / 1024;
            }
        };
    }

    private String describe(LocalBluetoothProfile localBluetoothProfile) {
        StringBuilder sb = new StringBuilder();
        sb.append("Address:");
        sb.append(this.mDevice);
        if (localBluetoothProfile != null) {
            sb.append(" Profile:");
            sb.append(localBluetoothProfile);
        }
        return sb.toString();
    }

    /* access modifiers changed from: package-private */
    public void onProfileStateChanged(LocalBluetoothProfile localBluetoothProfile, int i) {
        Log.d("CachedBluetoothDevice", "onProfileStateChanged: profile " + localBluetoothProfile + ", device " + this.mDevice.getAlias() + ", newProfileState " + i);
        if (this.mLocalAdapter.getState() == 13) {
            Log.d("CachedBluetoothDevice", " BT Turninig Off...Profile conn state change ignored...");
            return;
        }
        synchronized (this.mProfileLock) {
            if ((localBluetoothProfile instanceof A2dpProfile) || (localBluetoothProfile instanceof HeadsetProfile) || (localBluetoothProfile instanceof HearingAidProfile)) {
                setProfileConnectedStatus(localBluetoothProfile.getProfileId(), false);
                if (i != 0) {
                    if (i == 1) {
                        this.mHandler.sendEmptyMessageDelayed(localBluetoothProfile.getProfileId(), 60000);
                    } else if (i == 2) {
                        this.mHandler.removeMessages(localBluetoothProfile.getProfileId());
                    } else if (i != 3) {
                        Log.w("CachedBluetoothDevice", "onProfileStateChanged(): unknown profile state : " + i);
                    } else if (this.mHandler.hasMessages(localBluetoothProfile.getProfileId())) {
                        this.mHandler.removeMessages(localBluetoothProfile.getProfileId());
                    }
                } else if (this.mHandler.hasMessages(localBluetoothProfile.getProfileId())) {
                    this.mHandler.removeMessages(localBluetoothProfile.getProfileId());
                    setProfileConnectedStatus(localBluetoothProfile.getProfileId(), true);
                }
            }
            if (i == 2) {
                if (localBluetoothProfile instanceof MapProfile) {
                    localBluetoothProfile.setEnabled(this.mDevice, true);
                }
                if (!this.mProfiles.contains(localBluetoothProfile)) {
                    this.mRemovedProfiles.remove(localBluetoothProfile);
                    this.mProfiles.add(localBluetoothProfile);
                    if ((localBluetoothProfile instanceof PanProfile) && ((PanProfile) localBluetoothProfile).isLocalRoleNap(this.mDevice)) {
                        this.mLocalNapRoleConnected = true;
                    }
                }
            } else if ((localBluetoothProfile instanceof MapProfile) && i == 0) {
                localBluetoothProfile.setEnabled(this.mDevice, false);
            } else if (this.mLocalNapRoleConnected && (localBluetoothProfile instanceof PanProfile) && ((PanProfile) localBluetoothProfile).isLocalRoleNap(this.mDevice) && i == 0) {
                Log.d("CachedBluetoothDevice", "Removing PanProfile from device after NAP disconnect");
                this.mProfiles.remove(localBluetoothProfile);
                this.mRemovedProfiles.add(localBluetoothProfile);
                this.mLocalNapRoleConnected = false;
            }
        }
        fetchActiveDevices();
    }

    void setProfileConnectedStatus(int i, boolean z) {
        if (i == 1) {
            this.mIsHeadsetProfileConnectedFail = z;
        } else if (i == 2) {
            this.mIsA2dpProfileConnectedFail = z;
        } else if (i != 21) {
            Log.w("CachedBluetoothDevice", "setProfileConnectedStatus(): unknown profile id : " + i);
        } else {
            this.mIsHearingAidProfileConnectedFail = z;
        }
    }

    public void disconnect() {
        synchronized (this.mProfileLock) {
            this.mLocalAdapter.disconnectAllEnabledProfiles(this.mDevice);
        }
        PbapServerProfile pbapProfile = this.mProfileManager.getPbapProfile();
        if (pbapProfile != null && isConnectedProfile(pbapProfile)) {
            pbapProfile.setEnabled(this.mDevice, false);
        }
    }

    @Deprecated
    public void connect(boolean z) {
        connect();
    }

    public void connect() {
        if (ensurePaired()) {
            this.mConnectAttempted = SystemClock.elapsedRealtime();
            connectAllEnabledProfiles();
        }
    }

    public long getHiSyncId() {
        return this.mHiSyncId;
    }

    public void setHiSyncId(long j) {
        this.mHiSyncId = j;
    }

    public boolean isHearingAidDevice() {
        return this.mHiSyncId != 0;
    }

    private void connectAllEnabledProfiles() {
        synchronized (this.mProfileLock) {
            if (this.mProfiles.isEmpty()) {
                Log.d("CachedBluetoothDevice", "No profiles. Maybe we will connect later for device " + this.mDevice);
                return;
            }
            this.mLocalAdapter.connectAllEnabledProfiles(this.mDevice);
        }
    }

    private boolean ensurePaired() {
        if (getBondState() != 10) {
            return true;
        }
        startPairing();
        return false;
    }

    public boolean startPairing() {
        if (this.mLocalAdapter.isDiscovering()) {
            this.mLocalAdapter.cancelDiscovery();
        }
        return this.mDevice.createBond();
    }

    public void unpair() {
        BluetoothDevice bluetoothDevice;
        int bondState = getBondState();
        if (bondState == 11) {
            this.mDevice.cancelBondProcess();
        }
        if (bondState != 10 && (bluetoothDevice = this.mDevice) != null && bluetoothDevice.removeBond()) {
            releaseLruCache();
            Log.d("CachedBluetoothDevice", "Command sent successfully:REMOVE_BOND " + describe(null));
        }
    }

    public int getProfileConnectionState(LocalBluetoothProfile localBluetoothProfile) {
        if (localBluetoothProfile != null) {
            return localBluetoothProfile.getConnectionStatus(this.mDevice);
        }
        return 0;
    }

    private void fillData() {
        updateProfiles();
        fetchActiveDevices();
        migratePhonebookPermissionChoice();
        migrateMessagePermissionChoice();
        lambda$refresh$0();
    }

    public BluetoothDevice getDevice() {
        return this.mDevice;
    }

    public String getAddress() {
        return this.mDevice.getAddress();
    }

    public String getName() {
        String alias = this.mDevice.getAlias();
        return TextUtils.isEmpty(alias) ? getAddress() : alias;
    }

    /* access modifiers changed from: package-private */
    public void refreshName() {
        Log.d("CachedBluetoothDevice", "Device name: " + getName());
        lambda$refresh$0();
    }

    public int getBatteryLevel() {
        return this.mDevice.getBatteryLevel();
    }

    /* access modifiers changed from: package-private */
    public void refresh() {
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.android.settingslib.bluetooth.CachedBluetoothDevice$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CachedBluetoothDevice.this.lambda$refresh$1();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refresh$1() {
        Uri uriMetaData;
        if (BluetoothUtils.isAdvancedDetailsHeader(this.mDevice) && (uriMetaData = BluetoothUtils.getUriMetaData(getDevice(), 5)) != null && this.mDrawableCache.get(uriMetaData.toString()) == null) {
            this.mDrawableCache.put(uriMetaData.toString(), (BitmapDrawable) BluetoothUtils.getBtDrawableWithDescription(this.mContext, this).first);
        }
        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.android.settingslib.bluetooth.CachedBluetoothDevice$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CachedBluetoothDevice.this.lambda$refresh$0();
            }
        });
    }

    public void setJustDiscovered(boolean z) {
        if (this.mJustDiscovered != z) {
            this.mJustDiscovered = z;
            lambda$refresh$0();
        }
    }

    public int getBondState() {
        return this.mDevice.getBondState();
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0047  */
    /* JADX WARNING: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActiveDeviceChanged(boolean r4, int r5) {
        /*
            r3 = this;
            r0 = 1
            r1 = 0
            if (r5 == r0) goto L_0x003c
            r2 = 2
            if (r5 == r2) goto L_0x0033
            r2 = 21
            if (r5 == r2) goto L_0x002a
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "onActiveDeviceChanged: unknown profile "
            r0.append(r2)
            r0.append(r5)
            java.lang.String r5 = " isActive "
            r0.append(r5)
            r0.append(r4)
            java.lang.String r4 = r0.toString()
            java.lang.String r5 = "CachedBluetoothDevice"
            android.util.Log.w(r5, r4)
            goto L_0x0045
        L_0x002a:
            boolean r5 = r3.mIsActiveDeviceHearingAid
            if (r5 == r4) goto L_0x002f
            goto L_0x0030
        L_0x002f:
            r0 = r1
        L_0x0030:
            r3.mIsActiveDeviceHearingAid = r4
            goto L_0x0044
        L_0x0033:
            boolean r5 = r3.mIsActiveDeviceA2dp
            if (r5 == r4) goto L_0x0038
            goto L_0x0039
        L_0x0038:
            r0 = r1
        L_0x0039:
            r3.mIsActiveDeviceA2dp = r4
            goto L_0x0044
        L_0x003c:
            boolean r5 = r3.mIsActiveDeviceHeadset
            if (r5 == r4) goto L_0x0041
            goto L_0x0042
        L_0x0041:
            r0 = r1
        L_0x0042:
            r3.mIsActiveDeviceHeadset = r4
        L_0x0044:
            r1 = r0
        L_0x0045:
            if (r1 == 0) goto L_0x004a
            r3.lambda$refresh$0()
        L_0x004a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDevice.onActiveDeviceChanged(boolean, int):void");
    }

    /* access modifiers changed from: package-private */
    public void onAudioModeChanged() {
        lambda$refresh$0();
    }

    public boolean isActiveDevice(int i) {
        if (i == 1) {
            return this.mIsActiveDeviceHeadset;
        }
        if (i == 2) {
            return this.mIsActiveDeviceA2dp;
        }
        if (i == 21) {
            return this.mIsActiveDeviceHearingAid;
        }
        Log.w("CachedBluetoothDevice", "getActiveDevice: unknown profile " + i);
        return false;
    }

    /* access modifiers changed from: package-private */
    public void setRssi(short s) {
        if (this.mRssi != s) {
            this.mRssi = s;
            lambda$refresh$0();
        }
    }

    public boolean isConnected() {
        synchronized (this.mProfileLock) {
            for (LocalBluetoothProfile localBluetoothProfile : this.mProfiles) {
                if (getProfileConnectionState(localBluetoothProfile) == 2) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isConnectedProfile(LocalBluetoothProfile localBluetoothProfile) {
        return getProfileConnectionState(localBluetoothProfile) == 2;
    }

    public boolean isBusy() {
        int profileConnectionState;
        synchronized (this.mProfileLock) {
            Iterator<LocalBluetoothProfile> it = this.mProfiles.iterator();
            do {
                boolean z = true;
                if (it.hasNext()) {
                    profileConnectionState = getProfileConnectionState(it.next());
                    if (profileConnectionState == 1) {
                        break;
                    }
                } else {
                    if (getBondState() != 11) {
                        z = false;
                    }
                    return z;
                }
            } while (profileConnectionState != 3);
            return true;
        }
    }

    private boolean updateProfiles() {
        ParcelUuid[] uuids;
        ParcelUuid[] uuids2 = this.mDevice.getUuids();
        if (uuids2 == null || (uuids = this.mLocalAdapter.getUuids()) == null) {
            return false;
        }
        processPhonebookAccess();
        synchronized (this.mProfileLock) {
            this.mProfileManager.updateProfiles(uuids2, uuids, this.mProfiles, this.mRemovedProfiles, this.mLocalNapRoleConnected, this.mDevice);
        }
        Log.d("CachedBluetoothDevice", "updating profiles for " + this.mDevice.getAlias());
        BluetoothClass bluetoothClass = this.mDevice.getBluetoothClass();
        if (bluetoothClass != null) {
            Log.v("CachedBluetoothDevice", "Class: " + bluetoothClass.toString());
        }
        Log.v("CachedBluetoothDevice", "UUID:");
        for (ParcelUuid parcelUuid : uuids2) {
            Log.v("CachedBluetoothDevice", "  " + parcelUuid);
        }
        return true;
    }

    private void fetchActiveDevices() {
        A2dpProfile a2dpProfile = this.mProfileManager.getA2dpProfile();
        if (a2dpProfile != null) {
            this.mIsActiveDeviceA2dp = this.mDevice.equals(a2dpProfile.getActiveDevice());
        }
        HeadsetProfile headsetProfile = this.mProfileManager.getHeadsetProfile();
        if (headsetProfile != null) {
            this.mIsActiveDeviceHeadset = this.mDevice.equals(headsetProfile.getActiveDevice());
        }
        HearingAidProfile hearingAidProfile = this.mProfileManager.getHearingAidProfile();
        if (hearingAidProfile != null) {
            this.mIsActiveDeviceHearingAid = hearingAidProfile.getActiveDevices().contains(this.mDevice);
        }
    }

    /* access modifiers changed from: package-private */
    public void onUuidChanged() {
        long j;
        updateProfiles();
        ParcelUuid[] uuids = this.mDevice.getUuids();
        if (ArrayUtils.contains(uuids, BluetoothUuid.HOGP)) {
            j = 30000;
        } else {
            j = ArrayUtils.contains(uuids, BluetoothUuid.HEARING_AID) ? 15000 : 5000;
        }
        Log.d("CachedBluetoothDevice", "onUuidChanged: Time since last connect=" + (SystemClock.elapsedRealtime() - this.mConnectAttempted));
        if (this.mConnectAttempted + j > SystemClock.elapsedRealtime()) {
            Log.d("CachedBluetoothDevice", "onUuidChanged: triggering connectAllEnabledProfiles");
            connectAllEnabledProfiles();
        }
        lambda$refresh$0();
    }

    /* access modifiers changed from: package-private */
    public void onBondingStateChanged(int i) {
        if (i == 10) {
            synchronized (this.mProfileLock) {
                this.mProfiles.clear();
            }
            this.mDevice.setPhonebookAccessPermission(0);
            this.mDevice.setMessageAccessPermission(0);
            this.mDevice.setSimAccessPermission(0);
        }
        refresh();
        if (i == 12 && this.mDevice.isBondingInitiatedLocally()) {
            connect();
        }
    }

    public BluetoothClass getBtClass() {
        return this.mDevice.getBluetoothClass();
    }

    public List<LocalBluetoothProfile> getProfiles() {
        return new ArrayList(this.mProfiles);
    }

    public List<LocalBluetoothProfile> getConnectableProfiles() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mProfileLock) {
            for (LocalBluetoothProfile localBluetoothProfile : this.mProfiles) {
                if (localBluetoothProfile.accessProfileEnabled()) {
                    arrayList.add(localBluetoothProfile);
                }
            }
        }
        return arrayList;
    }

    public void registerCallback(Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void unregisterCallback(Callback callback) {
        this.mCallbacks.remove(callback);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: dispatchAttributesChanged */
    public void lambda$refresh$0() {
        for (Callback callback : this.mCallbacks) {
            callback.onDeviceAttributesChanged();
        }
    }

    @Override // java.lang.Object
    public String toString() {
        return this.mDevice.toString();
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CachedBluetoothDevice)) {
            return false;
        }
        return this.mDevice.equals(((CachedBluetoothDevice) obj).mDevice);
    }

    @Override // java.lang.Object
    public int hashCode() {
        return this.mDevice.getAddress().hashCode();
    }

    public int compareTo(CachedBluetoothDevice cachedBluetoothDevice) {
        int i = (cachedBluetoothDevice.isConnected() ? 1 : 0) - (isConnected() ? 1 : 0);
        if (i != 0) {
            return i;
        }
        int i2 = 1;
        int i3 = cachedBluetoothDevice.getBondState() == 12 ? 1 : 0;
        if (getBondState() != 12) {
            i2 = 0;
        }
        int i4 = i3 - i2;
        if (i4 != 0) {
            return i4;
        }
        int i5 = (cachedBluetoothDevice.mJustDiscovered ? 1 : 0) - (this.mJustDiscovered ? 1 : 0);
        if (i5 != 0) {
            return i5;
        }
        int i6 = cachedBluetoothDevice.mRssi - this.mRssi;
        if (i6 != 0) {
            return i6;
        }
        return getName().compareTo(cachedBluetoothDevice.getName());
    }

    private void migratePhonebookPermissionChoice() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("bluetooth_phonebook_permission", 0);
        if (sharedPreferences.contains(this.mDevice.getAddress())) {
            if (this.mDevice.getPhonebookAccessPermission() == 0) {
                int i = sharedPreferences.getInt(this.mDevice.getAddress(), 0);
                if (i == 1) {
                    this.mDevice.setPhonebookAccessPermission(1);
                } else if (i == 2) {
                    this.mDevice.setPhonebookAccessPermission(2);
                }
            }
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(this.mDevice.getAddress());
            edit.commit();
        }
    }

    private void migrateMessagePermissionChoice() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("bluetooth_message_permission", 0);
        if (sharedPreferences.contains(this.mDevice.getAddress())) {
            if (this.mDevice.getMessageAccessPermission() == 0) {
                int i = sharedPreferences.getInt(this.mDevice.getAddress(), 0);
                if (i == 1) {
                    this.mDevice.setMessageAccessPermission(1);
                } else if (i == 2) {
                    this.mDevice.setMessageAccessPermission(2);
                }
            }
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(this.mDevice.getAddress());
            edit.commit();
        }
    }

    private void processPhonebookAccess() {
        if (this.mDevice.getBondState() == 12 && BluetoothUuid.containsAnyUuid(this.mDevice.getUuids(), PbapServerProfile.PBAB_CLIENT_UUIDS)) {
            BluetoothClass bluetoothClass = this.mDevice.getBluetoothClass();
            if (this.mDevice.getPhonebookAccessPermission() == 0) {
                if (bluetoothClass != null && (bluetoothClass.getDeviceClass() == 1032 || bluetoothClass.getDeviceClass() == 1028)) {
                    EventLog.writeEvent(1397638484, "138529441", -1, "");
                }
                this.mDevice.setPhonebookAccessPermission(2);
            }
        }
    }

    public int getMaxConnectionState() {
        int i;
        synchronized (this.mProfileLock) {
            i = 0;
            for (LocalBluetoothProfile localBluetoothProfile : getProfiles()) {
                int profileConnectionState = getProfileConnectionState(localBluetoothProfile);
                if (profileConnectionState > i) {
                    i = profileConnectionState;
                }
            }
        }
        return i;
    }

    public CachedBluetoothDevice getSubDevice() {
        return this.mSubDevice;
    }

    public void setSubDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        this.mSubDevice = cachedBluetoothDevice;
    }

    public void switchSubDeviceContent() {
        BluetoothDevice bluetoothDevice = this.mDevice;
        short s = this.mRssi;
        boolean z = this.mJustDiscovered;
        CachedBluetoothDevice cachedBluetoothDevice = this.mSubDevice;
        this.mDevice = cachedBluetoothDevice.mDevice;
        this.mRssi = cachedBluetoothDevice.mRssi;
        this.mJustDiscovered = cachedBluetoothDevice.mJustDiscovered;
        cachedBluetoothDevice.mDevice = bluetoothDevice;
        cachedBluetoothDevice.mRssi = s;
        cachedBluetoothDevice.mJustDiscovered = z;
        fetchActiveDevices();
    }

    void releaseLruCache() {
        this.mDrawableCache.evictAll();
    }
}
