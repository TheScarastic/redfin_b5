package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.content.Context;
import android.net.TetheringManager;
import android.net.wifi.WifiClient;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.UserManager;
import android.util.Log;
import com.android.internal.util.ConcurrentUtils;
import com.android.systemui.statusbar.policy.HotspotController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class HotspotControllerImpl implements HotspotController, WifiManager.SoftApCallback {
    private static final boolean DEBUG = Log.isLoggable("HotspotController", 3);
    private final Context mContext;
    private int mHotspotState;
    private final Handler mMainHandler;
    private volatile int mNumConnectedDevices;
    private final TetheringManager mTetheringManager;
    private boolean mWaitingForTerminalState;
    private final WifiManager mWifiManager;
    private final ArrayList<HotspotController.Callback> mCallbacks = new ArrayList<>();
    private volatile boolean mIsTetheringSupported = true;
    private volatile boolean mHasTetherableWifiRegexs = true;
    private TetheringManager.TetheringEventCallback mTetheringCallback = new TetheringManager.TetheringEventCallback() { // from class: com.android.systemui.statusbar.policy.HotspotControllerImpl.1
        public void onTetheringSupported(boolean z) {
            if (HotspotControllerImpl.this.mIsTetheringSupported != z) {
                HotspotControllerImpl.this.mIsTetheringSupported = z;
                HotspotControllerImpl.this.fireHotspotAvailabilityChanged();
            }
        }

        public void onTetherableInterfaceRegexpsChanged(TetheringManager.TetheringInterfaceRegexps tetheringInterfaceRegexps) {
            boolean z = tetheringInterfaceRegexps.getTetherableWifiRegexs().size() != 0;
            if (HotspotControllerImpl.this.mHasTetherableWifiRegexs != z) {
                HotspotControllerImpl.this.mHasTetherableWifiRegexs = z;
                HotspotControllerImpl.this.fireHotspotAvailabilityChanged();
            }
        }
    };

    private static String stateToString(int i) {
        switch (i) {
            case 10:
                return "DISABLING";
            case 11:
                return "DISABLED";
            case 12:
                return "ENABLING";
            case 13:
                return "ENABLED";
            case 14:
                return "FAILED";
            default:
                return null;
        }
    }

    public HotspotControllerImpl(Context context, Handler handler, Handler handler2) {
        this.mContext = context;
        TetheringManager tetheringManager = (TetheringManager) context.getSystemService(TetheringManager.class);
        this.mTetheringManager = tetheringManager;
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        this.mMainHandler = handler;
        tetheringManager.registerTetheringEventCallback(new HandlerExecutor(handler2), this.mTetheringCallback);
    }

    @Override // com.android.systemui.statusbar.policy.HotspotController
    public boolean isHotspotSupported() {
        return this.mIsTetheringSupported && this.mHasTetherableWifiRegexs && UserManager.get(this.mContext).isUserAdmin(ActivityManager.getCurrentUser());
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("HotspotController state:");
        printWriter.print("  available=");
        printWriter.println(isHotspotSupported());
        printWriter.print("  mHotspotState=");
        printWriter.println(stateToString(this.mHotspotState));
        printWriter.print("  mNumConnectedDevices=");
        printWriter.println(this.mNumConnectedDevices);
        printWriter.print("  mWaitingForTerminalState=");
        printWriter.println(this.mWaitingForTerminalState);
    }

    public void addCallback(HotspotController.Callback callback) {
        synchronized (this.mCallbacks) {
            if (callback != null) {
                if (!this.mCallbacks.contains(callback)) {
                    if (DEBUG) {
                        Log.d("HotspotController", "addCallback " + callback);
                    }
                    this.mCallbacks.add(callback);
                    if (this.mWifiManager != null) {
                        if (this.mCallbacks.size() == 1) {
                            this.mWifiManager.registerSoftApCallback(new HandlerExecutor(this.mMainHandler), this);
                        } else {
                            this.mMainHandler.post(new Runnable(callback) { // from class: com.android.systemui.statusbar.policy.HotspotControllerImpl$$ExternalSyntheticLambda0
                                public final /* synthetic */ HotspotController.Callback f$1;

                                {
                                    this.f$1 = r2;
                                }

                                @Override // java.lang.Runnable
                                public final void run() {
                                    HotspotControllerImpl.$r8$lambda$Ny3gGR9GHGZZhdytnRgmX054cro(HotspotControllerImpl.this, this.f$1);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    public /* synthetic */ void lambda$addCallback$0(HotspotController.Callback callback) {
        callback.onHotspotChanged(isHotspotEnabled(), this.mNumConnectedDevices);
    }

    public void removeCallback(HotspotController.Callback callback) {
        WifiManager wifiManager;
        if (callback != null) {
            if (DEBUG) {
                Log.d("HotspotController", "removeCallback " + callback);
            }
            synchronized (this.mCallbacks) {
                this.mCallbacks.remove(callback);
                if (this.mCallbacks.isEmpty() && (wifiManager = this.mWifiManager) != null) {
                    wifiManager.unregisterSoftApCallback(this);
                }
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.HotspotController
    public boolean isHotspotEnabled() {
        return this.mHotspotState == 13;
    }

    @Override // com.android.systemui.statusbar.policy.HotspotController
    public boolean isHotspotTransient() {
        return this.mWaitingForTerminalState || this.mHotspotState == 12;
    }

    @Override // com.android.systemui.statusbar.policy.HotspotController
    public void setHotspotEnabled(boolean z) {
        if (this.mWaitingForTerminalState) {
            if (DEBUG) {
                Log.d("HotspotController", "Ignoring setHotspotEnabled; waiting for terminal state.");
            }
        } else if (z) {
            this.mWaitingForTerminalState = true;
            if (DEBUG) {
                Log.d("HotspotController", "Starting tethering");
            }
            this.mTetheringManager.startTethering(new TetheringManager.TetheringRequest.Builder(0).setShouldShowEntitlementUi(false).build(), ConcurrentUtils.DIRECT_EXECUTOR, new TetheringManager.StartTetheringCallback() { // from class: com.android.systemui.statusbar.policy.HotspotControllerImpl.2
                public void onTetheringFailed(int i) {
                    if (HotspotControllerImpl.DEBUG) {
                        Log.d("HotspotController", "onTetheringFailed");
                    }
                    HotspotControllerImpl.this.maybeResetSoftApState();
                    HotspotControllerImpl.this.fireHotspotChangedCallback();
                }
            });
        } else {
            this.mTetheringManager.stopTethering(0);
        }
    }

    @Override // com.android.systemui.statusbar.policy.HotspotController
    public int getNumConnectedDevices() {
        return this.mNumConnectedDevices;
    }

    public void fireHotspotChangedCallback() {
        ArrayList<HotspotController.Callback> arrayList;
        synchronized (this.mCallbacks) {
            arrayList = new ArrayList(this.mCallbacks);
        }
        for (HotspotController.Callback callback : arrayList) {
            callback.onHotspotChanged(isHotspotEnabled(), this.mNumConnectedDevices);
        }
    }

    public void fireHotspotAvailabilityChanged() {
        ArrayList<HotspotController.Callback> arrayList;
        synchronized (this.mCallbacks) {
            arrayList = new ArrayList(this.mCallbacks);
        }
        for (HotspotController.Callback callback : arrayList) {
            callback.onHotspotAvailabilityChanged(isHotspotSupported());
        }
    }

    public void onStateChanged(int i, int i2) {
        this.mHotspotState = i;
        maybeResetSoftApState();
        if (!isHotspotEnabled()) {
            this.mNumConnectedDevices = 0;
        }
        fireHotspotChangedCallback();
    }

    public void maybeResetSoftApState() {
        if (this.mWaitingForTerminalState) {
            int i = this.mHotspotState;
            if (!(i == 11 || i == 13)) {
                if (i == 14) {
                    this.mTetheringManager.stopTethering(0);
                } else {
                    return;
                }
            }
            this.mWaitingForTerminalState = false;
        }
    }

    public void onConnectedClientsChanged(List<WifiClient> list) {
        this.mNumConnectedDevices = list.size();
        fireHotspotChangedCallback();
    }
}
