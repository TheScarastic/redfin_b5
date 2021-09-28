package com.google.android.systemui.reversecharging;

import android.content.Context;
import android.os.Bundle;
import android.os.IHwBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import vendor.google.wireless_charger.V1_0.IWirelessCharger;
import vendor.google.wireless_charger.V1_2.IWirelessCharger;
import vendor.google.wireless_charger.V1_2.IWirelessChargerRtxStatusCallback;
import vendor.google.wireless_charger.V1_2.RtxStatusInfo;
/* loaded from: classes2.dex */
public class ReverseWirelessCharger extends IWirelessChargerRtxStatusCallback.Stub implements IHwBinder.DeathRecipient {
    private static final boolean DEBUG = Log.isLoggable("ReverseWirelessCharger", 3);
    private Context mContext;
    private IWirelessCharger mWirelessCharger;
    private final ArrayList<IsDockPresentCallback> mIsDockPresentCallbacks = new ArrayList<>();
    private final ArrayList<RtxInformationCallback> mRtxInformationCallbacks = new ArrayList<>();
    private final ArrayList<RtxStatusCallback> mRtxStatusCallbacks = new ArrayList<>();
    private final Object mLock = new Object();
    private final LocalIsDockPresentCallback mLocalIsDockPresentCallback = new LocalIsDockPresentCallback();
    private final LocalRtxInformationCallback mLocalRtxInformationCallback = new LocalRtxInformationCallback();

    /* loaded from: classes2.dex */
    public interface IsDockPresentCallback {
        void onIsDockPresentChanged(boolean z, byte b, byte b2, boolean z2, int i);
    }

    /* loaded from: classes2.dex */
    public interface RtxInformationCallback {
        void onRtxInformationChanged(RtxStatusInfo rtxStatusInfo);
    }

    /* loaded from: classes2.dex */
    public interface RtxStatusCallback {
        void onRtxStatusChanged(RtxStatusInfo rtxStatusInfo);
    }

    public ReverseWirelessCharger(Context context) {
        this.mContext = context;
    }

    /* access modifiers changed from: private */
    public static Bundle buildDockPresentBundle(boolean z, byte b, byte b2, boolean z2, int i) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("key_dock_present_docked", z);
        bundle.putByte("key_dock_present_type", b);
        bundle.putByte("key_dock_present_orientation", b2);
        bundle.putBoolean("key_dock_present_get_info", z2);
        bundle.putInt("key_dock_present_id", i);
        return bundle;
    }

    /* access modifiers changed from: private */
    public static Bundle buildReverseStatusBundle(RtxStatusInfo rtxStatusInfo) {
        Bundle bundle = new Bundle();
        bundle.putInt("key_rtx_mode", rtxStatusInfo.mode);
        bundle.putInt("key_accessory_type", rtxStatusInfo.acctype);
        bundle.putBoolean("key_rtx_connection", rtxStatusInfo.chg_s);
        bundle.putInt("key_rtx_iout", rtxStatusInfo.iout);
        bundle.putInt("key_rtx_vout", rtxStatusInfo.vout);
        bundle.putInt("key_rtx_level", rtxStatusInfo.level);
        bundle.putInt("key_reason_type", rtxStatusInfo.reason);
        return bundle;
    }

    public void serviceDied(long j) {
        Log.i("ReverseWirelessCharger", "serviceDied");
        this.mWirelessCharger = null;
    }

    private void initHALInterface() {
        if (this.mWirelessCharger == null) {
            try {
                IWirelessCharger service = IWirelessCharger.getService();
                this.mWirelessCharger = service;
                service.linkToDeath(this, 0);
                this.mWirelessCharger.registerRtxCallback(this);
            } catch (Exception e) {
                Log.i("ReverseWirelessCharger", "no wireless charger hal found: " + e.getMessage(), e);
                this.mWirelessCharger = null;
            }
        }
    }

    public boolean isRtxSupported() {
        initHALInterface();
        IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                return iWirelessCharger.isRtxSupported();
            } catch (Exception e) {
                Log.i("ReverseWirelessCharger", "isRtxSupported fail: ", e);
            }
        }
        return false;
    }

    public void addIsDockPresentCallback(IsDockPresentCallback isDockPresentCallback) {
        synchronized (this.mLock) {
            this.mIsDockPresentCallbacks.add(isDockPresentCallback);
        }
    }

    public void addIsDockPresentChangeListener(IsDockPresentChangeListener isDockPresentChangeListener) {
        addIsDockPresentCallback(isDockPresentChangeListener);
    }

    /* access modifiers changed from: private */
    public void dispatchIsDockPresentCallbacks(boolean z, byte b, byte b2, boolean z2, int i) {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mIsDockPresentCallbacks);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((IsDockPresentCallback) it.next()).onIsDockPresentChanged(z, b, b2, z2, i);
        }
    }

    public void getRtxInformation() {
        initHALInterface();
        IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.getRtxInformation(this.mLocalRtxInformationCallback);
            } catch (Exception e) {
                Log.i("ReverseWirelessCharger", "getRtxInformation fail: ", e);
            }
        }
    }

    public void addRtxInformationCallback(RtxInformationCallback rtxInformationCallback) {
        synchronized (this.mLock) {
            this.mRtxInformationCallbacks.add(rtxInformationCallback);
        }
    }

    public void addReverseChargingInformationChangeListener(ReverseChargingInformationChangeListener reverseChargingInformationChangeListener) {
        addRtxInformationCallback(reverseChargingInformationChangeListener);
    }

    /* access modifiers changed from: private */
    public void dispatchRtxInformationCallbacks(RtxStatusInfo rtxStatusInfo) {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mRtxInformationCallbacks);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((RtxInformationCallback) it.next()).onRtxInformationChanged(rtxStatusInfo);
        }
    }

    public void setRtxMode(boolean z) {
        initHALInterface();
        IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.setRtxMode(z);
            } catch (Exception e) {
                Log.i("ReverseWirelessCharger", "setRtxMode fail: ", e);
            }
        }
    }

    public void addRtxStatusCallback(RtxStatusCallback rtxStatusCallback) {
        synchronized (this.mLock) {
            this.mRtxStatusCallbacks.add(rtxStatusCallback);
        }
    }

    public void addReverseChargingChangeListener(ReverseChargingChangeListener reverseChargingChangeListener) {
        addRtxStatusCallback(reverseChargingChangeListener);
    }

    private void dispatchRtxStatusCallbacks(RtxStatusInfo rtxStatusInfo) {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mRtxStatusCallbacks);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((RtxStatusCallback) it.next()).onRtxStatusChanged(rtxStatusInfo);
        }
    }

    @Override // vendor.google.wireless_charger.V1_2.IWirelessChargerRtxStatusCallback
    public void rtxStatusInfoChanged(RtxStatusInfo rtxStatusInfo) throws RemoteException {
        dispatchRtxStatusCallbacks(rtxStatusInfo);
    }

    /* loaded from: classes2.dex */
    public interface IsDockPresentChangeListener extends IsDockPresentCallback {
        void onDockPresentChanged(Bundle bundle);

        @Override // com.google.android.systemui.reversecharging.ReverseWirelessCharger.IsDockPresentCallback
        default void onIsDockPresentChanged(boolean z, byte b, byte b2, boolean z2, int i) {
            if (ReverseWirelessCharger.DEBUG) {
                Log.d("ReverseWirelessCharger", "onIsDockPresentChanged(): docked=" + (z ? 1 : 0) + " type=" + ((int) b) + " orient=" + ((int) b2) + " isGetI=" + (z2 ? 1 : 0) + " id=" + i);
            }
            onDockPresentChanged(ReverseWirelessCharger.buildDockPresentBundle(z, b, b2, z2, i));
        }
    }

    /* loaded from: classes2.dex */
    public interface ReverseChargingInformationChangeListener extends RtxInformationCallback {
        void onReverseInformationChanged(Bundle bundle);

        @Override // com.google.android.systemui.reversecharging.ReverseWirelessCharger.RtxInformationCallback
        default void onRtxInformationChanged(RtxStatusInfo rtxStatusInfo) {
            if (ReverseWirelessCharger.DEBUG) {
                Log.d("ReverseWirelessCharger", "onRtxInformationChanged() RtxStatusInfo : " + rtxStatusInfo.toString());
            }
            onReverseInformationChanged(ReverseWirelessCharger.buildReverseStatusBundle(rtxStatusInfo));
        }
    }

    /* loaded from: classes2.dex */
    public interface ReverseChargingChangeListener extends RtxStatusCallback {
        void onReverseStatusChanged(Bundle bundle);

        @Override // com.google.android.systemui.reversecharging.ReverseWirelessCharger.RtxStatusCallback
        default void onRtxStatusChanged(RtxStatusInfo rtxStatusInfo) {
            if (ReverseWirelessCharger.DEBUG) {
                Log.d("ReverseWirelessCharger", "onRtxStatusChanged() RtxStatusInfo : " + rtxStatusInfo.toString());
            }
            onReverseStatusChanged(ReverseWirelessCharger.buildReverseStatusBundle(rtxStatusInfo));
        }
    }

    /* loaded from: classes2.dex */
    class LocalIsDockPresentCallback implements IWirelessCharger.isDockPresentCallback {
        LocalIsDockPresentCallback() {
        }

        @Override // vendor.google.wireless_charger.V1_0.IWirelessCharger.isDockPresentCallback
        public void onValues(boolean z, byte b, byte b2, boolean z2, int i) {
            if (ReverseWirelessCharger.DEBUG) {
                Log.d("ReverseWirelessCharger", "LocalIsDockPresentCallback::onValues(): docked=" + (z ? 1 : 0) + " type=" + ((int) b) + " orient=" + ((int) b2) + " isGetI=" + (z2 ? 1 : 0) + " id=" + i);
            }
            ReverseWirelessCharger.this.dispatchIsDockPresentCallbacks(z, b, b2, z2, i);
        }
    }

    /* loaded from: classes2.dex */
    class LocalRtxInformationCallback implements IWirelessCharger.getRtxInformationCallback {
        LocalRtxInformationCallback() {
        }

        @Override // vendor.google.wireless_charger.V1_2.IWirelessCharger.getRtxInformationCallback
        public void onValues(byte b, RtxStatusInfo rtxStatusInfo) {
            ReverseWirelessCharger.this.dispatchRtxInformationCallbacks(rtxStatusInfo);
        }
    }
}
