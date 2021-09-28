package com.google.android.systemui.adaptivecharging;

import android.content.Context;
import android.os.IHwBinder;
import android.os.RemoteException;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.util.Log;
import java.util.NoSuchElementException;
import vendor.google.google_battery.V1_0.IGoogleBattery;
import vendor.google.google_battery.V1_1.IGoogleBattery;
/* loaded from: classes2.dex */
public class AdaptiveChargingManager {
    private static final boolean DEBUG = Log.isLoggable("AdaptiveChargingManager", 3);
    Context mContext;

    /* loaded from: classes2.dex */
    public interface AdaptiveChargingStatusReceiver {
        void onDestroyInterface();

        void onReceiveStatus(String str, int i);
    }

    public AdaptiveChargingManager(Context context) {
        this.mContext = context;
    }

    private static IGoogleBattery initHalInterface(IHwBinder.DeathRecipient deathRecipient) {
        if (DEBUG) {
            Log.d("AdaptiveChargingManager", "initHalInterface");
        }
        try {
            IGoogleBattery service = IGoogleBattery.getService();
            if (!(service == null || deathRecipient == null)) {
                service.linkToDeath(deathRecipient, 0);
            }
            return service;
        } catch (RemoteException | NoSuchElementException e) {
            Log.e("AdaptiveChargingManager", "failed to get Google Battery HAL: ", e);
            return null;
        }
    }

    /* access modifiers changed from: private */
    public void destroyHalInterface(IGoogleBattery iGoogleBattery, IHwBinder.DeathRecipient deathRecipient) {
        if (DEBUG) {
            Log.d("AdaptiveChargingManager", "destroyHalInterface");
        }
        if (deathRecipient != null) {
            try {
                iGoogleBattery.unlinkToDeath(deathRecipient);
            } catch (RemoteException e) {
                Log.e("AdaptiveChargingManager", "unlinkToDeath failed: ", e);
            }
        }
    }

    boolean hasAdaptiveChargingFeature() {
        return this.mContext.getPackageManager().hasSystemFeature("com.google.android.feature.ADAPTIVE_CHARGING");
    }

    public boolean isAvailable() {
        if (!hasAdaptiveChargingFeature() || !DeviceConfig.getBoolean("adaptive_charging", "adaptive_charging_enabled", true)) {
            return false;
        }
        return true;
    }

    public boolean isEnabled() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "adaptive_charging_enabled", 1) == 1;
    }

    public static boolean isStageActive(String str) {
        return "Active".equals(str);
    }

    public static boolean isStageEnabled(String str) {
        return "Enabled".equals(str);
    }

    public static boolean isStageActiveOrEnabled(String str) {
        return isStageActive(str) || isStageEnabled(str);
    }

    public void queryStatus(final AdaptiveChargingStatusReceiver adaptiveChargingStatusReceiver) {
        final AnonymousClass1 r0 = new IHwBinder.DeathRecipient() { // from class: com.google.android.systemui.adaptivecharging.AdaptiveChargingManager.1
            public void serviceDied(long j) {
                if (AdaptiveChargingManager.DEBUG) {
                    Log.d("AdaptiveChargingManager", "serviceDied");
                }
                adaptiveChargingStatusReceiver.onDestroyInterface();
            }
        };
        final IGoogleBattery initHalInterface = initHalInterface(r0);
        if (initHalInterface == null) {
            adaptiveChargingStatusReceiver.onDestroyInterface();
            return;
        }
        try {
            initHalInterface.getChargingStageAndDeadline(new IGoogleBattery.getChargingStageAndDeadlineCallback() { // from class: com.google.android.systemui.adaptivecharging.AdaptiveChargingManager.2
                @Override // vendor.google.google_battery.V1_0.IGoogleBattery.getChargingStageAndDeadlineCallback
                public void onValues(byte b, String str, int i) {
                    if (AdaptiveChargingManager.DEBUG) {
                        Log.d("AdaptiveChargingManager", "getChargingStageDeadlineCallback result: " + ((int) b) + ", stage: \"" + str + "\", seconds: " + i);
                    }
                    if (b == 0) {
                        adaptiveChargingStatusReceiver.onReceiveStatus(str, i);
                    }
                    AdaptiveChargingManager.this.destroyHalInterface(initHalInterface, r0);
                    adaptiveChargingStatusReceiver.onDestroyInterface();
                }
            });
        } catch (RemoteException e) {
            Log.e("AdaptiveChargingManager", "Failed to get Adaptive Chaging status: ", e);
            destroyHalInterface(initHalInterface, r0);
            adaptiveChargingStatusReceiver.onDestroyInterface();
        }
    }
}
