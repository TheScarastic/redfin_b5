package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.os.UserManager;
import com.android.settingslib.Utils;
import com.android.systemui.BootCompleteCache;
import com.android.systemui.appops.AppOpItem;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.LocationController;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class LocationControllerImpl extends BroadcastReceiver implements LocationController, AppOpsController.Callback {
    private final AppOpsController mAppOpsController;
    private boolean mAreActiveLocationRequests;
    private final BootCompleteCache mBootCompleteCache;
    private final Context mContext;
    private final H mHandler;
    private final UserTracker mUserTracker;

    public LocationControllerImpl(Context context, AppOpsController appOpsController, Looper looper, Handler handler, BroadcastDispatcher broadcastDispatcher, BootCompleteCache bootCompleteCache, UserTracker userTracker) {
        this.mContext = context;
        this.mAppOpsController = appOpsController;
        this.mBootCompleteCache = bootCompleteCache;
        H h = new H(looper);
        this.mHandler = h;
        this.mUserTracker = userTracker;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.location.MODE_CHANGED");
        broadcastDispatcher.registerReceiverWithHandler(this, intentFilter, h, UserHandle.ALL);
        appOpsController.addCallback(new int[]{42}, this);
        handler.post(new Runnable() { // from class: com.android.systemui.statusbar.policy.LocationControllerImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                LocationControllerImpl.$r8$lambda$iAeac363VcvzueYKaxUiR4eMaA0(LocationControllerImpl.this);
            }
        });
    }

    public void addCallback(LocationController.LocationChangeCallback locationChangeCallback) {
        this.mHandler.obtainMessage(3, locationChangeCallback).sendToTarget();
        this.mHandler.sendEmptyMessage(1);
    }

    public void removeCallback(LocationController.LocationChangeCallback locationChangeCallback) {
        this.mHandler.obtainMessage(4, locationChangeCallback).sendToTarget();
    }

    @Override // com.android.systemui.statusbar.policy.LocationController
    public boolean setLocationEnabled(boolean z) {
        int userId = this.mUserTracker.getUserId();
        if (isUserLocationRestricted(userId)) {
            return false;
        }
        Utils.updateLocationEnabled(this.mContext, z, userId, 2);
        return true;
    }

    @Override // com.android.systemui.statusbar.policy.LocationController
    public boolean isLocationEnabled() {
        return this.mBootCompleteCache.isBootComplete() && ((LocationManager) this.mContext.getSystemService("location")).isLocationEnabledForUser(this.mUserTracker.getUserHandle());
    }

    @Override // com.android.systemui.statusbar.policy.LocationController
    public boolean isLocationActive() {
        return this.mAreActiveLocationRequests;
    }

    private boolean isUserLocationRestricted(int i) {
        return ((UserManager) this.mContext.getSystemService("user")).hasUserRestriction("no_share_location", UserHandle.of(i));
    }

    protected boolean areActiveHighPowerLocationRequests() {
        List<AppOpItem> activeAppOps = this.mAppOpsController.getActiveAppOps();
        int size = activeAppOps.size();
        for (int i = 0; i < size; i++) {
            if (activeAppOps.get(i).getCode() == 42) {
                return true;
            }
        }
        return false;
    }

    public void updateActiveLocationRequests() {
        boolean z = this.mAreActiveLocationRequests;
        boolean areActiveHighPowerLocationRequests = areActiveHighPowerLocationRequests();
        this.mAreActiveLocationRequests = areActiveHighPowerLocationRequests;
        if (areActiveHighPowerLocationRequests != z) {
            this.mHandler.sendEmptyMessage(2);
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("android.location.MODE_CHANGED".equals(intent.getAction())) {
            this.mHandler.locationSettingsChanged();
        }
    }

    @Override // com.android.systemui.appops.AppOpsController.Callback
    public void onActiveStateChanged(int i, int i2, String str, boolean z) {
        updateActiveLocationRequests();
    }

    /* loaded from: classes.dex */
    public final class H extends Handler {
        private ArrayList<LocationController.LocationChangeCallback> mSettingsChangeCallbacks = new ArrayList<>();

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        H(Looper looper) {
            super(looper);
            LocationControllerImpl.this = r1;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                locationSettingsChanged();
            } else if (i == 2) {
                locationActiveChanged();
            } else if (i == 3) {
                this.mSettingsChangeCallbacks.add((LocationController.LocationChangeCallback) message.obj);
            } else if (i == 4) {
                this.mSettingsChangeCallbacks.remove((LocationController.LocationChangeCallback) message.obj);
            }
        }

        private void locationActiveChanged() {
            com.android.systemui.util.Utils.safeForeach(this.mSettingsChangeCallbacks, new LocationControllerImpl$H$$ExternalSyntheticLambda0(this));
        }

        public /* synthetic */ void lambda$locationActiveChanged$0(LocationController.LocationChangeCallback locationChangeCallback) {
            locationChangeCallback.onLocationActiveChanged(LocationControllerImpl.this.mAreActiveLocationRequests);
        }

        public void locationSettingsChanged() {
            com.android.systemui.util.Utils.safeForeach(this.mSettingsChangeCallbacks, new LocationControllerImpl$H$$ExternalSyntheticLambda1(LocationControllerImpl.this.isLocationEnabled()));
        }
    }
}
