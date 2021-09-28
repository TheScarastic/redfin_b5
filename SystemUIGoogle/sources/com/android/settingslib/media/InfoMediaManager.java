package com.android.settingslib.media;

import android.app.Notification;
import android.content.Context;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import android.media.RoutingSessionInfo;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public class InfoMediaManager extends MediaManager {
    private static final boolean DEBUG = Log.isLoggable("InfoMediaManager", 3);
    private LocalBluetoothManager mBluetoothManager;
    private MediaDevice mCurrentConnectedDevice;
    @VisibleForTesting
    String mPackageName;
    @VisibleForTesting
    MediaRouter2Manager mRouterManager;
    @VisibleForTesting
    final RouterManagerCallback mMediaRouterCallback = new RouterManagerCallback();
    @VisibleForTesting
    final Executor mExecutor = Executors.newSingleThreadExecutor();

    public InfoMediaManager(Context context, String str, Notification notification, LocalBluetoothManager localBluetoothManager) {
        super(context, notification);
        this.mRouterManager = MediaRouter2Manager.getInstance(context);
        this.mBluetoothManager = localBluetoothManager;
        if (!TextUtils.isEmpty(str)) {
            this.mPackageName = str;
        }
    }

    public void startScan() {
        this.mMediaDevices.clear();
        this.mRouterManager.registerCallback(this.mExecutor, this.mMediaRouterCallback);
        this.mRouterManager.startScan();
        refreshDevices();
    }

    public void stopScan() {
        this.mRouterManager.unregisterCallback(this.mMediaRouterCallback);
        this.mRouterManager.stopScan();
    }

    /* access modifiers changed from: package-private */
    public MediaDevice getCurrentConnectedDevice() {
        return this.mCurrentConnectedDevice;
    }

    /* access modifiers changed from: package-private */
    public boolean connectDeviceWithoutPackageName(MediaDevice mediaDevice) {
        List activeSessions = this.mRouterManager.getActiveSessions();
        if (activeSessions.size() <= 0) {
            return false;
        }
        this.mRouterManager.transfer((RoutingSessionInfo) activeSessions.get(0), mediaDevice.mRouteInfo);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean addDeviceToPlayMedia(MediaDevice mediaDevice) {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "addDeviceToPlayMedia() package name is null or empty!");
            return false;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo == null || !routingSessionInfo.getSelectableRoutes().contains(mediaDevice.mRouteInfo.getId())) {
            Log.w("InfoMediaManager", "addDeviceToPlayMedia() Ignoring selecting a non-selectable device : " + mediaDevice.getName());
            return false;
        }
        this.mRouterManager.selectRoute(routingSessionInfo, mediaDevice.mRouteInfo);
        return true;
    }

    private RoutingSessionInfo getRoutingSessionInfo() {
        return getRoutingSessionInfo(this.mPackageName);
    }

    private RoutingSessionInfo getRoutingSessionInfo(String str) {
        List routingSessions = this.mRouterManager.getRoutingSessions(str);
        if (routingSessions == null || routingSessions.isEmpty()) {
            return null;
        }
        return (RoutingSessionInfo) routingSessions.get(routingSessions.size() - 1);
    }

    /* access modifiers changed from: package-private */
    public boolean removeDeviceFromPlayMedia(MediaDevice mediaDevice) {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "removeDeviceFromMedia() package name is null or empty!");
            return false;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo == null || !routingSessionInfo.getSelectedRoutes().contains(mediaDevice.mRouteInfo.getId())) {
            Log.w("InfoMediaManager", "removeDeviceFromMedia() Ignoring deselecting a non-deselectable device : " + mediaDevice.getName());
            return false;
        }
        this.mRouterManager.deselectRoute(routingSessionInfo, mediaDevice.mRouteInfo);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean releaseSession() {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "releaseSession() package name is null or empty!");
            return false;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            this.mRouterManager.releaseSession(routingSessionInfo);
            return true;
        }
        Log.w("InfoMediaManager", "releaseSession() Ignoring release session : " + this.mPackageName);
        return false;
    }

    /* access modifiers changed from: package-private */
    public List<MediaDevice> getSelectableMediaDevice() {
        ArrayList arrayList = new ArrayList();
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "getSelectableMediaDevice() package name is null or empty!");
            return arrayList;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            for (MediaRoute2Info mediaRoute2Info : this.mRouterManager.getSelectableRoutes(routingSessionInfo)) {
                arrayList.add(new InfoMediaDevice(this.mContext, this.mRouterManager, mediaRoute2Info, this.mPackageName));
            }
            return arrayList;
        }
        Log.w("InfoMediaManager", "getSelectableMediaDevice() cannot found selectable MediaDevice from : " + this.mPackageName);
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public List<MediaDevice> getDeselectableMediaDevice() {
        ArrayList arrayList = new ArrayList();
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.d("InfoMediaManager", "getDeselectableMediaDevice() package name is null or empty!");
            return arrayList;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            for (MediaRoute2Info mediaRoute2Info : this.mRouterManager.getDeselectableRoutes(routingSessionInfo)) {
                arrayList.add(new InfoMediaDevice(this.mContext, this.mRouterManager, mediaRoute2Info, this.mPackageName));
                Log.d("InfoMediaManager", ((Object) mediaRoute2Info.getName()) + " is deselectable for " + this.mPackageName);
            }
            return arrayList;
        }
        Log.d("InfoMediaManager", "getDeselectableMediaDevice() cannot found deselectable MediaDevice from : " + this.mPackageName);
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public List<MediaDevice> getSelectedMediaDevice() {
        ArrayList arrayList = new ArrayList();
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "getSelectedMediaDevice() package name is null or empty!");
            return arrayList;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            for (MediaRoute2Info mediaRoute2Info : this.mRouterManager.getSelectedRoutes(routingSessionInfo)) {
                arrayList.add(new InfoMediaDevice(this.mContext, this.mRouterManager, mediaRoute2Info, this.mPackageName));
            }
            return arrayList;
        }
        Log.w("InfoMediaManager", "getSelectedMediaDevice() cannot found selectable MediaDevice from : " + this.mPackageName);
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public void adjustSessionVolume(int i) {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "adjustSessionVolume() package name is null or empty!");
            return;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            Log.d("InfoMediaManager", "adjustSessionVolume() adjust volume : " + i + ", with : " + this.mPackageName);
            this.mRouterManager.setSessionVolume(routingSessionInfo, i);
            return;
        }
        Log.w("InfoMediaManager", "adjustSessionVolume() can't found corresponding RoutingSession with : " + this.mPackageName);
    }

    public int getSessionVolumeMax() {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "getSessionVolumeMax() package name is null or empty!");
            return -1;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            return routingSessionInfo.getVolumeMax();
        }
        Log.w("InfoMediaManager", "getSessionVolumeMax() can't found corresponding RoutingSession with : " + this.mPackageName);
        return -1;
    }

    public int getSessionVolume() {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "getSessionVolume() package name is null or empty!");
            return -1;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            return routingSessionInfo.getVolume();
        }
        Log.w("InfoMediaManager", "getSessionVolume() can't found corresponding RoutingSession with : " + this.mPackageName);
        return -1;
    }

    /* access modifiers changed from: package-private */
    public CharSequence getSessionName() {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "Unable to get session name. The package name is null or empty!");
            return null;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            return routingSessionInfo.getName();
        }
        Log.w("InfoMediaManager", "Unable to get session name for package: " + this.mPackageName);
        return null;
    }

    /* access modifiers changed from: private */
    public void refreshDevices() {
        this.mMediaDevices.clear();
        this.mCurrentConnectedDevice = null;
        if (TextUtils.isEmpty(this.mPackageName)) {
            buildAllRoutes();
        } else {
            buildAvailableRoutes();
        }
        dispatchDeviceListAdded();
    }

    /* access modifiers changed from: private */
    public void buildAllRoutes() {
        for (MediaRoute2Info mediaRoute2Info : this.mRouterManager.getAllRoutes()) {
            if (DEBUG) {
                Log.d("InfoMediaManager", "buildAllRoutes() route : " + ((Object) mediaRoute2Info.getName()) + ", volume : " + mediaRoute2Info.getVolume() + ", type : " + mediaRoute2Info.getType());
            }
            if (mediaRoute2Info.isSystemRoute()) {
                addMediaDevice(mediaRoute2Info);
            }
        }
    }

    /* access modifiers changed from: private */
    public void buildAvailableRoutes() {
        for (MediaRoute2Info mediaRoute2Info : getAvailableRoutes(this.mPackageName)) {
            if (DEBUG) {
                Log.d("InfoMediaManager", "buildAvailableRoutes() route : " + ((Object) mediaRoute2Info.getName()) + ", volume : " + mediaRoute2Info.getVolume() + ", type : " + mediaRoute2Info.getType());
            }
            addMediaDevice(mediaRoute2Info);
        }
    }

    private List<MediaRoute2Info> getAvailableRoutes(String str) {
        ArrayList arrayList = new ArrayList();
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo(str);
        if (routingSessionInfo != null) {
            arrayList.addAll(this.mRouterManager.getSelectedRoutes(routingSessionInfo));
        }
        for (MediaRoute2Info mediaRoute2Info : this.mRouterManager.getTransferableRoutes(str)) {
            boolean z = false;
            Iterator it = arrayList.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (TextUtils.equals(mediaRoute2Info.getId(), ((MediaRoute2Info) it.next()).getId())) {
                        z = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (!z) {
                arrayList.add(mediaRoute2Info);
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x00a6  */
    /* JADX WARNING: Removed duplicated region for block: B:41:? A[RETURN, SYNTHETIC] */
    @com.android.internal.annotations.VisibleForTesting
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void addMediaDevice(android.media.MediaRoute2Info r9) {
        /*
            r8 = this;
            int r0 = r9.getType()
            if (r0 == 0) goto L_0x0079
            r1 = 2000(0x7d0, float:2.803E-42)
            if (r0 == r1) goto L_0x0079
            r1 = 2
            if (r0 == r1) goto L_0x006d
            r1 = 3
            if (r0 == r1) goto L_0x006d
            r1 = 4
            if (r0 == r1) goto L_0x006d
            r1 = 8
            if (r0 == r1) goto L_0x0045
            r1 = 9
            if (r0 == r1) goto L_0x006d
            r1 = 22
            if (r0 == r1) goto L_0x006d
            r1 = 23
            if (r0 == r1) goto L_0x0045
            r1 = 1001(0x3e9, float:1.403E-42)
            if (r0 == r1) goto L_0x0079
            r1 = 1002(0x3ea, float:1.404E-42)
            if (r0 == r1) goto L_0x0079
            switch(r0) {
                case 11: goto L_0x006d;
                case 12: goto L_0x006d;
                case 13: goto L_0x006d;
                default: goto L_0x002e;
            }
        L_0x002e:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r1 = "addMediaDevice() unknown device type : "
            r9.append(r1)
            r9.append(r0)
            java.lang.String r9 = r9.toString()
            java.lang.String r0 = "InfoMediaManager"
            android.util.Log.w(r0, r9)
            goto L_0x006b
        L_0x0045:
            android.bluetooth.BluetoothAdapter r0 = android.bluetooth.BluetoothAdapter.getDefaultAdapter()
            java.lang.String r1 = r9.getAddress()
            android.bluetooth.BluetoothDevice r0 = r0.getRemoteDevice(r1)
            com.android.settingslib.bluetooth.LocalBluetoothManager r1 = r8.mBluetoothManager
            com.android.settingslib.bluetooth.CachedBluetoothDeviceManager r1 = r1.getCachedDeviceManager()
            com.android.settingslib.bluetooth.CachedBluetoothDevice r4 = r1.findDevice(r0)
            if (r4 == 0) goto L_0x006b
            com.android.settingslib.media.BluetoothMediaDevice r0 = new com.android.settingslib.media.BluetoothMediaDevice
            android.content.Context r3 = r8.mContext
            android.media.MediaRouter2Manager r5 = r8.mRouterManager
            java.lang.String r7 = r8.mPackageName
            r2 = r0
            r6 = r9
            r2.<init>(r3, r4, r5, r6, r7)
            goto L_0x00a4
        L_0x006b:
            r0 = 0
            goto L_0x00a4
        L_0x006d:
            com.android.settingslib.media.PhoneMediaDevice r0 = new com.android.settingslib.media.PhoneMediaDevice
            android.content.Context r1 = r8.mContext
            android.media.MediaRouter2Manager r2 = r8.mRouterManager
            java.lang.String r3 = r8.mPackageName
            r0.<init>(r1, r2, r9, r3)
            goto L_0x00a4
        L_0x0079:
            com.android.settingslib.media.InfoMediaDevice r0 = new com.android.settingslib.media.InfoMediaDevice
            android.content.Context r1 = r8.mContext
            android.media.MediaRouter2Manager r2 = r8.mRouterManager
            java.lang.String r3 = r8.mPackageName
            r0.<init>(r1, r2, r9, r3)
            java.lang.String r1 = r8.mPackageName
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 != 0) goto L_0x00a4
            android.media.RoutingSessionInfo r1 = r8.getRoutingSessionInfo()
            java.util.List r1 = r1.getSelectedRoutes()
            java.lang.String r9 = r9.getId()
            boolean r9 = r1.contains(r9)
            if (r9 == 0) goto L_0x00a4
            com.android.settingslib.media.MediaDevice r9 = r8.mCurrentConnectedDevice
            if (r9 != 0) goto L_0x00a4
            r8.mCurrentConnectedDevice = r0
        L_0x00a4:
            if (r0 == 0) goto L_0x00ab
            java.util.List<com.android.settingslib.media.MediaDevice> r8 = r8.mMediaDevices
            r8.add(r0)
        L_0x00ab:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.media.InfoMediaManager.addMediaDevice(android.media.MediaRoute2Info):void");
    }

    /* loaded from: classes.dex */
    class RouterManagerCallback implements MediaRouter2Manager.Callback {
        RouterManagerCallback() {
        }

        public void onRoutesAdded(List<MediaRoute2Info> list) {
            InfoMediaManager.this.refreshDevices();
        }

        public void onPreferredFeaturesChanged(String str, List<String> list) {
            if (TextUtils.equals(InfoMediaManager.this.mPackageName, str)) {
                InfoMediaManager.this.refreshDevices();
            }
        }

        public void onRoutesChanged(List<MediaRoute2Info> list) {
            InfoMediaManager.this.refreshDevices();
        }

        public void onRoutesRemoved(List<MediaRoute2Info> list) {
            InfoMediaManager.this.refreshDevices();
        }

        public void onTransferred(RoutingSessionInfo routingSessionInfo, RoutingSessionInfo routingSessionInfo2) {
            if (InfoMediaManager.DEBUG) {
                Log.d("InfoMediaManager", "onTransferred() oldSession : " + ((Object) routingSessionInfo.getName()) + ", newSession : " + ((Object) routingSessionInfo2.getName()));
            }
            InfoMediaManager.this.mMediaDevices.clear();
            String str = null;
            InfoMediaManager.this.mCurrentConnectedDevice = null;
            if (TextUtils.isEmpty(InfoMediaManager.this.mPackageName)) {
                InfoMediaManager.this.buildAllRoutes();
            } else {
                InfoMediaManager.this.buildAvailableRoutes();
            }
            if (InfoMediaManager.this.mCurrentConnectedDevice != null) {
                str = InfoMediaManager.this.mCurrentConnectedDevice.getId();
            }
            InfoMediaManager.this.dispatchConnectedDeviceChanged(str);
        }

        public void onTransferFailed(RoutingSessionInfo routingSessionInfo, MediaRoute2Info mediaRoute2Info) {
            InfoMediaManager.this.dispatchOnRequestFailed(0);
        }

        public void onRequestFailed(int i) {
            InfoMediaManager.this.dispatchOnRequestFailed(i);
        }

        public void onSessionUpdated(RoutingSessionInfo routingSessionInfo) {
            InfoMediaManager.this.dispatchDataChanged();
        }
    }
}
