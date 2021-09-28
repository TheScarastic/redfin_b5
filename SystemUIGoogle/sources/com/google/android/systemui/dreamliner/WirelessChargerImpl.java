package com.google.android.systemui.dreamliner;

import android.os.Bundle;
import android.os.Handler;
import android.os.IHwBinder;
import android.os.Looper;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.google.android.systemui.dreamliner.WirelessCharger;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import vendor.google.wireless_charger.V1_0.DockInfo;
import vendor.google.wireless_charger.V1_0.IWirelessCharger;
import vendor.google.wireless_charger.V1_0.KeyExchangeResponse;
import vendor.google.wireless_charger.V1_1.AlignInfo;
import vendor.google.wireless_charger.V1_1.IWirelessChargerInfoCallback;
import vendor.google.wireless_charger.V1_3.FanDetailedInfo;
import vendor.google.wireless_charger.V1_3.FanInfo;
import vendor.google.wireless_charger.V1_3.IWirelessCharger;
/* loaded from: classes2.dex */
public class WirelessChargerImpl extends WirelessCharger implements IHwBinder.DeathRecipient, IWirelessCharger.isDockPresentCallback {
    private static final boolean DEBUG = Log.isLoggable("Dreamliner-WLC_HAL", 3);
    private static final long MAX_POLLING_TIMEOUT_NS = TimeUnit.SECONDS.toNanos(5);
    private IWirelessCharger.isDockPresentCallback mCallback;
    private long mPollingStartedTimeNs;
    private vendor.google.wireless_charger.V1_3.IWirelessCharger mWirelessCharger;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable mRunnable = new Runnable() { // from class: com.google.android.systemui.dreamliner.WirelessChargerImpl.1
        @Override // java.lang.Runnable
        public void run() {
            WirelessChargerImpl wirelessChargerImpl = WirelessChargerImpl.this;
            wirelessChargerImpl.isDockPresentInternal(wirelessChargerImpl);
        }
    };

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void asyncIsDockPresent(WirelessCharger.IsDockPresentCallback isDockPresentCallback) {
        initHALInterface();
        if (this.mWirelessCharger != null) {
            this.mPollingStartedTimeNs = System.nanoTime();
            this.mCallback = new IsDockPresentCallbackWrapper(isDockPresentCallback);
            this.mHandler.removeCallbacks(this.mRunnable);
            this.mHandler.postDelayed(this.mRunnable, 100);
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void getInformation(WirelessCharger.GetInformationCallback getInformationCallback) {
        initHALInterface();
        vendor.google.wireless_charger.V1_3.IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.getInformation(new GetInformationCallbackWrapper(getInformationCallback));
            } catch (Exception e) {
                Log.i("Dreamliner-WLC_HAL", "getInformation fail: " + e.getMessage());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void keyExchange(byte[] bArr, WirelessCharger.KeyExchangeCallback keyExchangeCallback) {
        initHALInterface();
        if (this.mWirelessCharger != null) {
            try {
                this.mWirelessCharger.keyExchange(convertPrimitiveArrayToArrayList(bArr), new KeyExchangeCallbackWrapper(keyExchangeCallback));
            } catch (Exception e) {
                Log.i("Dreamliner-WLC_HAL", "keyExchange fail: " + e.getMessage());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void challenge(byte b, byte[] bArr, WirelessCharger.ChallengeCallback challengeCallback) {
        initHALInterface();
        if (this.mWirelessCharger != null) {
            try {
                this.mWirelessCharger.challenge(b, convertPrimitiveArrayToArrayList(bArr), new ChallengeCallbackWrapper(challengeCallback));
            } catch (Exception e) {
                Log.i("Dreamliner-WLC_HAL", "challenge fail: " + e.getMessage());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void registerAlignInfo(WirelessCharger.AlignInfoListener alignInfoListener) {
        initHALInterface();
        vendor.google.wireless_charger.V1_3.IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.registerCallback(new WirelessChargerInfoCallback(alignInfoListener));
            } catch (Exception e) {
                Log.i("Dreamliner-WLC_HAL", "register alignInfo callback fail: " + e.getMessage());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void getFanSimpleInformation(byte b, WirelessCharger.GetFanSimpleInformationCallback getFanSimpleInformationCallback) {
        initHALInterface();
        Log.d("Dreamliner-WLC_HAL", "command=3");
        vendor.google.wireless_charger.V1_3.IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.getFan(b, new GetFanSimpleInformationCallbackWrapper(b, getFanSimpleInformationCallback));
            } catch (Exception e) {
                Log.i("Dreamliner-WLC_HAL", "command=3 fail: " + e.getMessage());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void getFanInformation(byte b, WirelessCharger.GetFanInformationCallback getFanInformationCallback) {
        initHALInterface();
        Log.d("Dreamliner-WLC_HAL", "command=0");
        vendor.google.wireless_charger.V1_3.IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.getFanInformation(b, new GetFanInformationCallbackWrapper(b, getFanInformationCallback));
            } catch (Exception e) {
                Log.i("Dreamliner-WLC_HAL", "command=0 fail: " + e.getMessage());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void setFan(byte b, byte b2, int i, WirelessCharger.SetFanCallback setFanCallback) {
        initHALInterface();
        Log.d("Dreamliner-WLC_HAL", "command=1, i=" + ((int) b) + ", m=" + ((int) b2) + ", r=" + i);
        if (this.mWirelessCharger != null) {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                this.mWirelessCharger.setFan(b, b2, (short) i, new SetFanCallbackWrapper(b, setFanCallback));
                if (DEBUG) {
                    Log.d("Dreamliner-WLC_HAL", "command=1 spending time: " + (System.currentTimeMillis() - currentTimeMillis));
                }
            } catch (Exception e) {
                Log.i("Dreamliner-WLC_HAL", "command=1 fail: " + e.getMessage());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    public void getWpcAuthDigests(byte b, WirelessCharger.GetWpcAuthDigestsCallback getWpcAuthDigestsCallback) {
        initHALInterface();
        vendor.google.wireless_charger.V1_3.IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.getWpcAuthDigests(b, new GetWpcAuthDigestsCallbackWrapper(getWpcAuthDigestsCallback));
            } catch (Exception e) {
                Log.i("Dreamliner-WLC_HAL", "get wpc digests fail: " + e.getMessage());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    public void getWpcAuthCertificate(byte b, short s, short s2, WirelessCharger.GetWpcAuthCertificateCallback getWpcAuthCertificateCallback) {
        initHALInterface();
        vendor.google.wireless_charger.V1_3.IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.getWpcAuthCertificate(b, s, s2, new GetWpcAuthCertificateCallbackWrapper(getWpcAuthCertificateCallback));
            } catch (Exception e) {
                Log.i("Dreamliner-WLC_HAL", "get wpc cert fail: " + e.getMessage());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    public void getWpcAuthChallengeResponse(byte b, byte[] bArr, WirelessCharger.GetWpcAuthChallengeResponseCallback getWpcAuthChallengeResponseCallback) {
        initHALInterface();
        vendor.google.wireless_charger.V1_3.IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.getWpcAuthChallengeResponse(b, convertPrimitiveArrayToArrayList(bArr), new GetWpcAuthChallengeResponseCallbackWrapper(getWpcAuthChallengeResponseCallback));
            } catch (Exception e) {
                Log.i("Dreamliner-WLC_HAL", "get wpc challenge response fail: " + e.getMessage());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    public void setFeatures(long j, long j2, WirelessCharger.SetFeaturesCallback setFeaturesCallback) {
        initHALInterface();
        vendor.google.wireless_charger.V1_3.IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                setFeaturesCallback.onCallback(iWirelessCharger.setFeatures(j, j2));
            } catch (Exception e) {
                Log.i("Dreamliner-WLC_HAL", "set features fail: " + e.getMessage());
            }
        }
    }

    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    public void getFeatures(long j, WirelessCharger.GetFeaturesCallback getFeaturesCallback) {
        initHALInterface();
        vendor.google.wireless_charger.V1_3.IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.getFeatures(j, new GetFeaturesCallbackWrapper(getFeaturesCallback));
            } catch (Exception e) {
                Log.i("Dreamliner-WLC_HAL", "get features fail: " + e.getMessage());
            }
        }
    }

    /* access modifiers changed from: package-private */
    @Override // com.google.android.systemui.dreamliner.WirelessCharger
    public int getFanLevel() {
        initHALInterface();
        Log.d("Dreamliner-WLC_HAL", "command=2");
        vendor.google.wireless_charger.V1_3.IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger == null) {
            return -1;
        }
        try {
            return iWirelessCharger.getFanLevel();
        } catch (Exception e) {
            Log.i("Dreamliner-WLC_HAL", "command=2 fail: " + e.getMessage());
            return -1;
        }
    }

    public void serviceDied(long j) {
        Log.i("Dreamliner-WLC_HAL", "serviceDied");
        this.mWirelessCharger = null;
    }

    private ArrayList<Byte> convertPrimitiveArrayToArrayList(byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        ArrayList<Byte> arrayList = new ArrayList<>();
        for (byte b : bArr) {
            arrayList.add(Byte.valueOf(b));
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public void isDockPresentInternal(IWirelessCharger.isDockPresentCallback isdockpresentcallback) {
        initHALInterface();
        vendor.google.wireless_charger.V1_3.IWirelessCharger iWirelessCharger = this.mWirelessCharger;
        if (iWirelessCharger != null) {
            try {
                iWirelessCharger.isDockPresent(isdockpresentcallback);
            } catch (Exception e) {
                Log.i("Dreamliner-WLC_HAL", "isDockPresent fail: " + e.getMessage());
            }
        }
    }

    @Override // vendor.google.wireless_charger.V1_0.IWirelessCharger.isDockPresentCallback
    public void onValues(boolean z, byte b, byte b2, boolean z2, int i) {
        if (System.nanoTime() >= this.mPollingStartedTimeNs + MAX_POLLING_TIMEOUT_NS || i != 0) {
            IWirelessCharger.isDockPresentCallback isdockpresentcallback = this.mCallback;
            if (isdockpresentcallback != null) {
                isdockpresentcallback.onValues(z, b, b2, z2, i);
                this.mCallback = null;
                return;
            }
            return;
        }
        this.mHandler.postDelayed(this.mRunnable, 100);
    }

    private void initHALInterface() {
        if (this.mWirelessCharger == null) {
            try {
                vendor.google.wireless_charger.V1_3.IWirelessCharger service = vendor.google.wireless_charger.V1_3.IWirelessCharger.getService();
                this.mWirelessCharger = service;
                service.linkToDeath(this, 0);
            } catch (Exception e) {
                Log.i("Dreamliner-WLC_HAL", "no wireless charger hal found: " + e.getMessage());
                this.mWirelessCharger = null;
            }
        }
    }

    /* loaded from: classes2.dex */
    final class IsDockPresentCallbackWrapper implements IWirelessCharger.isDockPresentCallback {
        private final WirelessCharger.IsDockPresentCallback mCallback;

        public IsDockPresentCallbackWrapper(WirelessCharger.IsDockPresentCallback isDockPresentCallback) {
            this.mCallback = isDockPresentCallback;
        }

        @Override // vendor.google.wireless_charger.V1_0.IWirelessCharger.isDockPresentCallback
        public void onValues(boolean z, byte b, byte b2, boolean z2, int i) {
            this.mCallback.onCallback(z, b, b2, z2, i);
        }
    }

    /* loaded from: classes2.dex */
    final class GetInformationCallbackWrapper implements IWirelessCharger.getInformationCallback {
        private final WirelessCharger.GetInformationCallback mCallback;

        public GetInformationCallbackWrapper(WirelessCharger.GetInformationCallback getInformationCallback) {
            this.mCallback = getInformationCallback;
        }

        @Override // vendor.google.wireless_charger.V1_0.IWirelessCharger.getInformationCallback
        public void onValues(byte b, DockInfo dockInfo) {
            this.mCallback.onCallback(Byte.valueOf(b).intValue(), convertDockInfo(dockInfo));
        }

        private DockInfo convertDockInfo(DockInfo dockInfo) {
            return new DockInfo(dockInfo.manufacturer, dockInfo.model, dockInfo.serial, Byte.valueOf(dockInfo.type).intValue());
        }
    }

    /* loaded from: classes2.dex */
    final class KeyExchangeCallbackWrapper implements IWirelessCharger.keyExchangeCallback {
        private final WirelessCharger.KeyExchangeCallback mCallback;

        public KeyExchangeCallbackWrapper(WirelessCharger.KeyExchangeCallback keyExchangeCallback) {
            this.mCallback = keyExchangeCallback;
        }

        @Override // vendor.google.wireless_charger.V1_0.IWirelessCharger.keyExchangeCallback
        public void onValues(byte b, KeyExchangeResponse keyExchangeResponse) {
            if (keyExchangeResponse != null) {
                this.mCallback.onCallback(Byte.valueOf(b).intValue(), keyExchangeResponse.dockId, keyExchangeResponse.dockPublicKey);
            } else {
                this.mCallback.onCallback(Byte.valueOf(b).intValue(), (byte) -1, null);
            }
        }
    }

    /* loaded from: classes2.dex */
    final class ChallengeCallbackWrapper implements IWirelessCharger.challengeCallback {
        private final WirelessCharger.ChallengeCallback mCallback;

        public ChallengeCallbackWrapper(WirelessCharger.ChallengeCallback challengeCallback) {
            this.mCallback = challengeCallback;
        }

        @Override // vendor.google.wireless_charger.V1_0.IWirelessCharger.challengeCallback
        public void onValues(byte b, ArrayList<Byte> arrayList) {
            this.mCallback.onCallback(Byte.valueOf(b).intValue(), arrayList);
        }
    }

    /* loaded from: classes2.dex */
    final class WirelessChargerInfoCallback extends IWirelessChargerInfoCallback.Stub {
        private final WirelessCharger.AlignInfoListener mListener;

        public WirelessChargerInfoCallback(WirelessCharger.AlignInfoListener alignInfoListener) {
            this.mListener = alignInfoListener;
        }

        @Override // vendor.google.wireless_charger.V1_1.IWirelessChargerInfoCallback
        public void alignInfoChanged(AlignInfo alignInfo) {
            this.mListener.onAlignInfoChanged(convertAlignInfo(alignInfo));
        }

        private DockAlignInfo convertAlignInfo(AlignInfo alignInfo) {
            return new DockAlignInfo(Byte.valueOf(alignInfo.alignState).intValue(), Byte.valueOf(alignInfo.alignPct).intValue());
        }
    }

    /* loaded from: classes2.dex */
    static final class GetFanSimpleInformationCallbackWrapper implements IWirelessCharger.getFanCallback {
        private final WirelessCharger.GetFanSimpleInformationCallback mCallback;
        private final byte mFanId;

        GetFanSimpleInformationCallbackWrapper(byte b, WirelessCharger.GetFanSimpleInformationCallback getFanSimpleInformationCallback) {
            this.mFanId = b;
            this.mCallback = getFanSimpleInformationCallback;
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger.getFanCallback
        public void onValues(byte b, FanInfo fanInfo) {
            Log.d("Dreamliner-WLC_HAL", "command=3, result=" + Byte.valueOf(b).intValue() + ", i=" + ((int) this.mFanId) + ", m=" + ((int) fanInfo.fanMode) + ", cr=" + ((int) fanInfo.currentRpm));
            this.mCallback.onCallback(Byte.valueOf(b).intValue(), WirelessChargerImpl.convertFanInfo(this.mFanId, fanInfo));
        }
    }

    /* access modifiers changed from: private */
    public static Bundle convertFanInfo(byte b, FanInfo fanInfo) {
        Bundle bundle = new Bundle();
        bundle.putByte("fan_id", b);
        bundle.putByte("fan_mode", fanInfo.fanMode);
        bundle.putInt("fan_current_rpm", fanInfo.currentRpm);
        return bundle;
    }

    /* loaded from: classes2.dex */
    static final class GetFanInformationCallbackWrapper implements IWirelessCharger.getFanInformationCallback {
        private final WirelessCharger.GetFanInformationCallback mCallback;
        private final byte mFanId;

        public GetFanInformationCallbackWrapper(byte b, WirelessCharger.GetFanInformationCallback getFanInformationCallback) {
            this.mFanId = b;
            this.mCallback = getFanInformationCallback;
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger.getFanInformationCallback
        public void onValues(byte b, FanDetailedInfo fanDetailedInfo) {
            Log.d("Dreamliner-WLC_HAL", "command=0, result=" + Byte.valueOf(b).intValue() + ", i=" + ((int) this.mFanId) + ", m=" + ((int) fanDetailedInfo.fanMode) + ", cr=" + ((int) fanDetailedInfo.currentRpm) + ", mir=" + ((int) fanDetailedInfo.minimumRpm) + ", mxr=" + ((int) fanDetailedInfo.maximumRpm) + ", t=" + ((int) fanDetailedInfo.type) + ", c=" + ((int) fanDetailedInfo.count));
            this.mCallback.onCallback(Byte.valueOf(b).intValue(), WirelessChargerImpl.convertFanDetailedInfo(this.mFanId, fanDetailedInfo));
        }
    }

    /* access modifiers changed from: private */
    public static Bundle convertFanDetailedInfo(byte b, FanDetailedInfo fanDetailedInfo) {
        Bundle bundle = new Bundle();
        bundle.putByte("fan_id", b);
        bundle.putByte("fan_mode", fanDetailedInfo.fanMode);
        bundle.putInt("fan_current_rpm", fanDetailedInfo.currentRpm);
        bundle.putInt("fan_min_rpm", fanDetailedInfo.minimumRpm);
        bundle.putInt("fan_max_rpm", fanDetailedInfo.maximumRpm);
        bundle.putByte("fan_type", fanDetailedInfo.type);
        bundle.putByte("fan_count", fanDetailedInfo.count);
        return bundle;
    }

    /* loaded from: classes2.dex */
    static final class SetFanCallbackWrapper implements IWirelessCharger.setFanCallback {
        private final WirelessCharger.SetFanCallback mCallback;
        private final byte mFanId;

        public SetFanCallbackWrapper(byte b, WirelessCharger.SetFanCallback setFanCallback) {
            this.mFanId = b;
            this.mCallback = setFanCallback;
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger.setFanCallback
        public void onValues(byte b, FanInfo fanInfo) {
            Log.d("Dreamliner-WLC_HAL", "command=1, result=" + Byte.valueOf(b).intValue() + ", i=" + ((int) this.mFanId) + ", m=" + ((int) fanInfo.fanMode) + ", cr=" + ((int) fanInfo.currentRpm));
            this.mCallback.onCallback(Byte.valueOf(b).intValue(), WirelessChargerImpl.convertFanInfo(this.mFanId, fanInfo));
        }
    }

    /* loaded from: classes2.dex */
    static final class GetWpcAuthDigestsCallbackWrapper implements IWirelessCharger.getWpcAuthDigestsCallback {
        private final WirelessCharger.GetWpcAuthDigestsCallback mCallback;

        GetWpcAuthDigestsCallbackWrapper(WirelessCharger.GetWpcAuthDigestsCallback getWpcAuthDigestsCallback) {
            this.mCallback = getWpcAuthDigestsCallback;
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger.getWpcAuthDigestsCallback
        public void onValues(byte b, byte b2, byte b3, ArrayList<byte[]> arrayList) {
            this.mCallback.onCallback(Byte.valueOf(b).intValue(), b2, b3, arrayList);
        }
    }

    /* loaded from: classes2.dex */
    static final class GetWpcAuthCertificateCallbackWrapper implements IWirelessCharger.getWpcAuthCertificateCallback {
        private final WirelessCharger.GetWpcAuthCertificateCallback mCallback;

        GetWpcAuthCertificateCallbackWrapper(WirelessCharger.GetWpcAuthCertificateCallback getWpcAuthCertificateCallback) {
            this.mCallback = getWpcAuthCertificateCallback;
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger.getWpcAuthCertificateCallback
        public void onValues(byte b, ArrayList<Byte> arrayList) {
            this.mCallback.onCallback(Byte.valueOf(b).intValue(), arrayList);
        }
    }

    /* loaded from: classes2.dex */
    static final class GetWpcAuthChallengeResponseCallbackWrapper implements IWirelessCharger.getWpcAuthChallengeResponseCallback {
        private final WirelessCharger.GetWpcAuthChallengeResponseCallback mCallback;

        GetWpcAuthChallengeResponseCallbackWrapper(WirelessCharger.GetWpcAuthChallengeResponseCallback getWpcAuthChallengeResponseCallback) {
            this.mCallback = getWpcAuthChallengeResponseCallback;
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger.getWpcAuthChallengeResponseCallback
        public void onValues(byte b, byte b2, byte b3, byte b4, ArrayList<Byte> arrayList, ArrayList<Byte> arrayList2) {
            this.mCallback.onCallback(Byte.valueOf(b).intValue(), b2, b3, b4, arrayList, arrayList2);
        }
    }

    /* loaded from: classes2.dex */
    static final class GetFeaturesCallbackWrapper implements IWirelessCharger.getFeaturesCallback {
        private final WirelessCharger.GetFeaturesCallback mCallback;

        GetFeaturesCallbackWrapper(WirelessCharger.GetFeaturesCallback getFeaturesCallback) {
            this.mCallback = getFeaturesCallback;
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger.getFeaturesCallback
        public void onValues(byte b, long j) {
            this.mCallback.onCallback(Byte.valueOf(b).intValue(), j);
        }
    }
}
