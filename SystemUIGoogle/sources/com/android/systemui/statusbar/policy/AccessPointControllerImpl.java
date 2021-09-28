package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.SimpleClock;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.IndentingPrintWriter;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wifitrackerlib.WifiPickerTracker;
import java.io.PrintWriter;
import java.time.Clock;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class AccessPointControllerImpl implements NetworkController.AccessPointController, WifiPickerTracker.WifiPickerTrackerCallback, LifecycleOwner {
    private static final boolean DEBUG = Log.isLoggable("AccessPointController", 3);
    private static final int[] ICONS = WifiIcons.WIFI_FULL_ICONS;
    private int mCurrentUser;
    private final Executor mMainExecutor;
    private final UserManager mUserManager;
    private WifiPickerTracker mWifiPickerTracker;
    private WifiPickerTrackerFactory mWifiPickerTrackerFactory;
    private final ArrayList<NetworkController.AccessPointController.AccessPointCallback> mCallbacks = new ArrayList<>();
    private final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);
    private final WifiEntry.ConnectCallback mConnectCallback = new WifiEntry.ConnectCallback() { // from class: com.android.systemui.statusbar.policy.AccessPointControllerImpl.1
        @Override // com.android.wifitrackerlib.WifiEntry.ConnectCallback
        public void onConnectResult(int i) {
            if (i == 0) {
                if (AccessPointControllerImpl.DEBUG) {
                    Log.d("AccessPointController", "connect success");
                }
            } else if (AccessPointControllerImpl.DEBUG) {
                Log.d("AccessPointController", "connect failure reason=" + i);
            }
        }
    };

    @Override // com.android.wifitrackerlib.WifiPickerTracker.WifiPickerTrackerCallback
    public void onNumSavedNetworksChanged() {
    }

    @Override // com.android.wifitrackerlib.WifiPickerTracker.WifiPickerTrackerCallback
    public void onNumSavedSubscriptionsChanged() {
    }

    public AccessPointControllerImpl(UserManager userManager, UserTracker userTracker, Executor executor, WifiPickerTrackerFactory wifiPickerTrackerFactory) {
        this.mUserManager = userManager;
        this.mCurrentUser = userTracker.getUserId();
        this.mMainExecutor = executor;
        this.mWifiPickerTrackerFactory = wifiPickerTrackerFactory;
        executor.execute(new Runnable() { // from class: com.android.systemui.statusbar.policy.AccessPointControllerImpl$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                AccessPointControllerImpl.m369$r8$lambda$ZAZTO33Klhov4t_FQiJoseTjuY(AccessPointControllerImpl.this);
            }
        });
    }

    public /* synthetic */ void lambda$new$0() {
        this.mLifecycle.setCurrentState(Lifecycle.State.CREATED);
    }

    public void init() {
        if (this.mWifiPickerTracker == null) {
            this.mWifiPickerTracker = this.mWifiPickerTrackerFactory.create(getLifecycle(), this);
        }
    }

    @Override // androidx.lifecycle.LifecycleOwner
    public Lifecycle getLifecycle() {
        return this.mLifecycle;
    }

    public /* synthetic */ void lambda$finalize$1() {
        this.mLifecycle.setCurrentState(Lifecycle.State.DESTROYED);
    }

    protected void finalize() throws Throwable {
        this.mMainExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.policy.AccessPointControllerImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AccessPointControllerImpl.$r8$lambda$1pXx6Z46jSTBzwuYGeove6EfkYQ(AccessPointControllerImpl.this);
            }
        });
        super.finalize();
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.AccessPointController
    public boolean canConfigWifi() {
        return !this.mUserManager.hasUserRestriction("no_config_wifi", new UserHandle(this.mCurrentUser));
    }

    public void onUserSwitched(int i) {
        this.mCurrentUser = i;
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.AccessPointController
    public void addAccessPointCallback(NetworkController.AccessPointController.AccessPointCallback accessPointCallback) {
        if (accessPointCallback != null && !this.mCallbacks.contains(accessPointCallback)) {
            if (DEBUG) {
                Log.d("AccessPointController", "addCallback " + accessPointCallback);
            }
            this.mCallbacks.add(accessPointCallback);
            if (this.mCallbacks.size() == 1) {
                this.mMainExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.policy.AccessPointControllerImpl$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        AccessPointControllerImpl.$r8$lambda$FVL1JSZDi40ROyotdZsRI9eHi34(AccessPointControllerImpl.this);
                    }
                });
            }
        }
    }

    public /* synthetic */ void lambda$addAccessPointCallback$2() {
        this.mLifecycle.setCurrentState(Lifecycle.State.STARTED);
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.AccessPointController
    public void removeAccessPointCallback(NetworkController.AccessPointController.AccessPointCallback accessPointCallback) {
        if (accessPointCallback != null) {
            if (DEBUG) {
                Log.d("AccessPointController", "removeCallback " + accessPointCallback);
            }
            this.mCallbacks.remove(accessPointCallback);
            if (this.mCallbacks.isEmpty()) {
                this.mMainExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.policy.AccessPointControllerImpl$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        AccessPointControllerImpl.$r8$lambda$kzVTdfDqvMyeeTWs8tuVFKB3BKo(AccessPointControllerImpl.this);
                    }
                });
            }
        }
    }

    public /* synthetic */ void lambda$removeAccessPointCallback$3() {
        this.mLifecycle.setCurrentState(Lifecycle.State.CREATED);
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.AccessPointController
    public void scanForAccessPoints() {
        WifiPickerTracker wifiPickerTracker = this.mWifiPickerTracker;
        if (wifiPickerTracker == null) {
            fireAcccessPointsCallback(Collections.emptyList());
            return;
        }
        List<WifiEntry> wifiEntries = wifiPickerTracker.getWifiEntries();
        WifiEntry connectedWifiEntry = this.mWifiPickerTracker.getConnectedWifiEntry();
        if (connectedWifiEntry != null) {
            wifiEntries.add(0, connectedWifiEntry);
        }
        fireAcccessPointsCallback(wifiEntries);
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.AccessPointController
    public int getIcon(WifiEntry wifiEntry) {
        return ICONS[Math.max(0, wifiEntry.getLevel())];
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.AccessPointController
    public boolean connect(WifiEntry wifiEntry) {
        if (wifiEntry == null) {
            return false;
        }
        if (DEBUG) {
            if (wifiEntry.getWifiConfiguration() != null) {
                Log.d("AccessPointController", "connect networkId=" + wifiEntry.getWifiConfiguration().networkId);
            } else {
                Log.d("AccessPointController", "connect to unsaved network " + wifiEntry.getTitle());
            }
        }
        if (wifiEntry.isSaved()) {
            wifiEntry.connect(this.mConnectCallback);
        } else if (wifiEntry.getSecurity() != 0) {
            Intent intent = new Intent("android.settings.WIFI_SETTINGS");
            intent.putExtra("wifi_start_connect_ssid", wifiEntry.getSsid());
            intent.addFlags(268435456);
            fireSettingsIntentCallback(intent);
            return true;
        } else {
            wifiEntry.connect(this.mConnectCallback);
        }
        return false;
    }

    private void fireSettingsIntentCallback(Intent intent) {
        Iterator<NetworkController.AccessPointController.AccessPointCallback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onSettingsActivityTriggered(intent);
        }
    }

    private void fireAcccessPointsCallback(List<WifiEntry> list) {
        Iterator<NetworkController.AccessPointController.AccessPointCallback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onAccessPointsChanged(list);
        }
    }

    public void dump(PrintWriter printWriter) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.println("AccessPointControllerImpl:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("Callbacks: " + Arrays.toString(this.mCallbacks.toArray()));
        indentingPrintWriter.println("WifiPickerTracker: " + this.mWifiPickerTracker.toString());
        if (this.mWifiPickerTracker != null && !this.mCallbacks.isEmpty()) {
            indentingPrintWriter.println("Connected: " + this.mWifiPickerTracker.getConnectedWifiEntry());
            indentingPrintWriter.println("Other wifi entries: " + Arrays.toString(this.mWifiPickerTracker.getWifiEntries().toArray()));
        } else if (this.mWifiPickerTracker != null) {
            indentingPrintWriter.println("WifiPickerTracker not started, cannot get reliable entries");
        }
        indentingPrintWriter.decreaseIndent();
    }

    @Override // com.android.wifitrackerlib.BaseWifiTracker.BaseWifiTrackerCallback
    public void onWifiStateChanged() {
        scanForAccessPoints();
    }

    @Override // com.android.wifitrackerlib.WifiPickerTracker.WifiPickerTrackerCallback
    public void onWifiEntriesChanged() {
        scanForAccessPoints();
    }

    /* loaded from: classes.dex */
    public static class WifiPickerTrackerFactory {
        private final Clock mClock = new SimpleClock(ZoneOffset.UTC) { // from class: com.android.systemui.statusbar.policy.AccessPointControllerImpl.WifiPickerTrackerFactory.1
            public long millis() {
                return SystemClock.elapsedRealtime();
            }
        };
        private final ConnectivityManager mConnectivityManager;
        private final Context mContext;
        private final Handler mMainHandler;
        private final NetworkScoreManager mNetworkScoreManager;
        private final WifiManager mWifiManager;
        private final Handler mWorkerHandler;

        /* JADX WARN: Type inference failed for: r0v0, types: [java.time.Clock, com.android.systemui.statusbar.policy.AccessPointControllerImpl$WifiPickerTrackerFactory$1] */
        public WifiPickerTrackerFactory(Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, NetworkScoreManager networkScoreManager, Handler handler, Handler handler2) {
            this.mContext = context;
            this.mWifiManager = wifiManager;
            this.mConnectivityManager = connectivityManager;
            this.mNetworkScoreManager = networkScoreManager;
            this.mMainHandler = handler;
            this.mWorkerHandler = handler2;
        }

        public WifiPickerTracker create(Lifecycle lifecycle, WifiPickerTracker.WifiPickerTrackerCallback wifiPickerTrackerCallback) {
            WifiManager wifiManager = this.mWifiManager;
            if (wifiManager == null) {
                return null;
            }
            return new WifiPickerTracker(lifecycle, this.mContext, wifiManager, this.mConnectivityManager, this.mNetworkScoreManager, this.mMainHandler, this.mWorkerHandler, this.mClock, 15000, 10000, wifiPickerTrackerCallback);
        }
    }
}
