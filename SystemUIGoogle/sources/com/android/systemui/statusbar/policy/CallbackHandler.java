package com.android.systemui.statusbar.policy;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SubscriptionInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.statusbar.policy.NetworkController;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class CallbackHandler extends Handler implements NetworkController.EmergencyListener, NetworkController.SignalCallback {
    private static final SimpleDateFormat SSDF = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    private int mHistoryIndex;
    private String mLastCallback;
    private final ArrayList<NetworkController.EmergencyListener> mEmergencyListeners = new ArrayList<>();
    private final ArrayList<NetworkController.SignalCallback> mSignalCallbacks = new ArrayList<>();
    private final String[] mHistory = new String[64];

    @VisibleForTesting
    public CallbackHandler(Looper looper) {
        super(looper);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 0:
                Iterator<NetworkController.EmergencyListener> it = this.mEmergencyListeners.iterator();
                while (it.hasNext()) {
                    it.next().setEmergencyCallsOnly(message.arg1 != 0);
                }
                return;
            case 1:
                Iterator<NetworkController.SignalCallback> it2 = this.mSignalCallbacks.iterator();
                while (it2.hasNext()) {
                    it2.next().setSubs((List) message.obj);
                }
                return;
            case 2:
                Iterator<NetworkController.SignalCallback> it3 = this.mSignalCallbacks.iterator();
                while (it3.hasNext()) {
                    it3.next().setNoSims(message.arg1 != 0, message.arg2 != 0);
                }
                return;
            case 3:
                Iterator<NetworkController.SignalCallback> it4 = this.mSignalCallbacks.iterator();
                while (it4.hasNext()) {
                    it4.next().setEthernetIndicators((NetworkController.IconState) message.obj);
                }
                return;
            case 4:
                Iterator<NetworkController.SignalCallback> it5 = this.mSignalCallbacks.iterator();
                while (it5.hasNext()) {
                    it5.next().setIsAirplaneMode((NetworkController.IconState) message.obj);
                }
                return;
            case 5:
                Iterator<NetworkController.SignalCallback> it6 = this.mSignalCallbacks.iterator();
                while (it6.hasNext()) {
                    it6.next().setMobileDataEnabled(message.arg1 != 0);
                }
                return;
            case 6:
                if (message.arg1 != 0) {
                    this.mEmergencyListeners.add((NetworkController.EmergencyListener) message.obj);
                    return;
                } else {
                    this.mEmergencyListeners.remove((NetworkController.EmergencyListener) message.obj);
                    return;
                }
            case 7:
                if (message.arg1 != 0) {
                    this.mSignalCallbacks.add((NetworkController.SignalCallback) message.obj);
                    return;
                } else {
                    this.mSignalCallbacks.remove((NetworkController.SignalCallback) message.obj);
                    return;
                }
            default:
                return;
        }
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setWifiIndicators(NetworkController.WifiIndicators wifiIndicators) {
        recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + wifiIndicators);
        post(new Runnable(wifiIndicators) { // from class: com.android.systemui.statusbar.policy.CallbackHandler$$ExternalSyntheticLambda2
            public final /* synthetic */ NetworkController.WifiIndicators f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                CallbackHandler.$r8$lambda$eo_aw2C3NKZ3xoTafvO2sST_bIs(CallbackHandler.this, this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$setWifiIndicators$0(NetworkController.WifiIndicators wifiIndicators) {
        Iterator<NetworkController.SignalCallback> it = this.mSignalCallbacks.iterator();
        while (it.hasNext()) {
            it.next().setWifiIndicators(wifiIndicators);
        }
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setMobileDataIndicators(NetworkController.MobileDataIndicators mobileDataIndicators) {
        recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + mobileDataIndicators);
        post(new Runnable(mobileDataIndicators) { // from class: com.android.systemui.statusbar.policy.CallbackHandler$$ExternalSyntheticLambda1
            public final /* synthetic */ NetworkController.MobileDataIndicators f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                CallbackHandler.$r8$lambda$d3gIbPKOrXq7BQtUy9vvsIRKylc(CallbackHandler.this, this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$setMobileDataIndicators$1(NetworkController.MobileDataIndicators mobileDataIndicators) {
        Iterator<NetworkController.SignalCallback> it = this.mSignalCallbacks.iterator();
        while (it.hasNext()) {
            it.next().setMobileDataIndicators(mobileDataIndicators);
        }
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setConnectivityStatus(boolean z, boolean z2, boolean z3) {
        String str = "setConnectivityStatus: noDefaultNetwork=" + z + ",noValidatedNetwork=" + z2 + ",noNetworksAvailable=" + z3;
        if (!str.equals(this.mLastCallback)) {
            this.mLastCallback = str;
            recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + str + ",");
        }
        post(new Runnable(z, z2, z3) { // from class: com.android.systemui.statusbar.policy.CallbackHandler$$ExternalSyntheticLambda3
            public final /* synthetic */ boolean f$1;
            public final /* synthetic */ boolean f$2;
            public final /* synthetic */ boolean f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.lang.Runnable
            public final void run() {
                CallbackHandler.$r8$lambda$L3qpss2cWXkqnzcWSkq1TITVmRw(CallbackHandler.this, this.f$1, this.f$2, this.f$3);
            }
        });
    }

    public /* synthetic */ void lambda$setConnectivityStatus$2(boolean z, boolean z2, boolean z3) {
        Iterator<NetworkController.SignalCallback> it = this.mSignalCallbacks.iterator();
        while (it.hasNext()) {
            it.next().setConnectivityStatus(z, z2, z3);
        }
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setCallIndicator(NetworkController.IconState iconState, int i) {
        String str = "setCallIndicator: statusIcon=" + iconState + ",subId=" + i;
        if (!str.equals(this.mLastCallback)) {
            this.mLastCallback = str;
            recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + str + ",");
        }
        post(new Runnable(iconState, i) { // from class: com.android.systemui.statusbar.policy.CallbackHandler$$ExternalSyntheticLambda0
            public final /* synthetic */ NetworkController.IconState f$1;
            public final /* synthetic */ int f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                CallbackHandler.$r8$lambda$xKJ3_ZwtgEBu5y1UhFDHBD2QuK0(CallbackHandler.this, this.f$1, this.f$2);
            }
        });
    }

    public /* synthetic */ void lambda$setCallIndicator$3(NetworkController.IconState iconState, int i) {
        Iterator<NetworkController.SignalCallback> it = this.mSignalCallbacks.iterator();
        while (it.hasNext()) {
            it.next().setCallIndicator(iconState, i);
        }
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setSubs(List<SubscriptionInfo> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("setSubs: ");
        sb.append("subs=");
        sb.append(list == null ? "" : list.toString());
        String sb2 = sb.toString();
        if (!sb2.equals(this.mLastCallback)) {
            this.mLastCallback = sb2;
            recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + sb2 + ",");
        }
        obtainMessage(1, list).sendToTarget();
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setNoSims(boolean z, boolean z2) {
        obtainMessage(2, z ? 1 : 0, z2 ? 1 : 0).sendToTarget();
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setMobileDataEnabled(boolean z) {
        obtainMessage(5, z ? 1 : 0, 0).sendToTarget();
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.EmergencyListener
    public void setEmergencyCallsOnly(boolean z) {
        obtainMessage(0, z ? 1 : 0, 0).sendToTarget();
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setEthernetIndicators(NetworkController.IconState iconState) {
        recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + ",setEthernetIndicators: icon=" + iconState);
        obtainMessage(3, iconState).sendToTarget();
    }

    @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
    public void setIsAirplaneMode(NetworkController.IconState iconState) {
        String str = "setIsAirplaneMode: icon=" + iconState;
        if (!str.equals(this.mLastCallback)) {
            this.mLastCallback = str;
            recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + str + ",");
        }
        obtainMessage(4, iconState).sendToTarget();
    }

    public void setListening(NetworkController.SignalCallback signalCallback, boolean z) {
        obtainMessage(7, z ? 1 : 0, 0, signalCallback).sendToTarget();
    }

    protected void recordLastCallback(String str) {
        String[] strArr = this.mHistory;
        int i = this.mHistoryIndex;
        strArr[i] = str;
        this.mHistoryIndex = (i + 1) % 64;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("  - CallbackHandler -----");
        int i = 0;
        for (int i2 = 0; i2 < 64; i2++) {
            if (this.mHistory[i2] != null) {
                i++;
            }
        }
        int i3 = this.mHistoryIndex + 64;
        while (true) {
            i3--;
            if (i3 >= (this.mHistoryIndex + 64) - i) {
                printWriter.println("  Previous Callback(" + ((this.mHistoryIndex + 64) - i3) + "): " + this.mHistory[i3 & 63]);
            } else {
                return;
            }
        }
    }
}
