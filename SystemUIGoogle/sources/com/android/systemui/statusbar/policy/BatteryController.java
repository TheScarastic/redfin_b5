package com.android.systemui.statusbar.policy;

import com.android.systemui.Dumpable;
import com.android.systemui.demomode.DemoMode;
/* loaded from: classes.dex */
public interface BatteryController extends DemoMode, Dumpable, CallbackController<BatteryStateChangeCallback> {

    /* loaded from: classes.dex */
    public interface BatteryStateChangeCallback {
        default void onBatteryLevelChanged(int i, boolean z, boolean z2) {
        }

        default void onBatteryUnknownStateChanged(boolean z) {
        }

        default void onExtremeBatterySaverChanged(boolean z) {
        }

        default void onPowerSaveChanged(boolean z) {
        }

        default void onReverseChanged(boolean z, int i, String str) {
        }

        default void onWirelessChargingChanged(boolean z) {
        }
    }

    /* loaded from: classes.dex */
    public interface EstimateFetchCompletion {
        void onBatteryRemainingEstimateRetrieved(String str);
    }

    default void getEstimatedTimeRemainingString(EstimateFetchCompletion estimateFetchCompletion) {
    }

    default void init() {
    }

    boolean isAodPowerSave();

    boolean isPluggedIn();

    default boolean isPluggedInWireless() {
        return false;
    }

    boolean isPowerSave();

    default boolean isReverseOn() {
        return false;
    }

    default boolean isReverseSupported() {
        return false;
    }

    default boolean isWirelessCharging() {
        return false;
    }

    void setPowerSaveMode(boolean z);

    default void setReverseState(boolean z) {
    }
}
