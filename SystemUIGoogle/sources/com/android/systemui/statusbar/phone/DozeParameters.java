package com.android.systemui.statusbar.phone;

import android.content.res.Resources;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.util.MathUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.R$bool;
import com.android.systemui.R$integer;
import com.android.systemui.doze.AlwaysOnDisplayPolicy;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.tuner.TunerService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public class DozeParameters implements TunerService.Tunable, com.android.systemui.plugins.statusbar.DozeParameters, Dumpable {
    private final AlwaysOnDisplayPolicy mAlwaysOnPolicy;
    private final AmbientDisplayConfiguration mAmbientDisplayConfiguration;
    private final BatteryController mBatteryController;
    private final Set<Callback> mCallbacks = new HashSet();
    private boolean mControlScreenOffAnimation;
    private boolean mDozeAlwaysOn;
    private final FeatureFlags mFeatureFlags;
    private final PowerManager mPowerManager;
    private final Resources mResources;
    private final UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    public static final boolean FORCE_NO_BLANKING = SystemProperties.getBoolean("debug.force_no_blanking", false);
    public static final boolean FORCE_BLANKING = SystemProperties.getBoolean("debug.force_blanking", false);

    /* loaded from: classes.dex */
    interface Callback {
        void onAlwaysOnChange();
    }

    /* access modifiers changed from: protected */
    public DozeParameters(Resources resources, AmbientDisplayConfiguration ambientDisplayConfiguration, AlwaysOnDisplayPolicy alwaysOnDisplayPolicy, PowerManager powerManager, BatteryController batteryController, TunerService tunerService, DumpManager dumpManager, FeatureFlags featureFlags, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        this.mResources = resources;
        this.mAmbientDisplayConfiguration = ambientDisplayConfiguration;
        this.mAlwaysOnPolicy = alwaysOnDisplayPolicy;
        this.mBatteryController = batteryController;
        dumpManager.registerDumpable("DozeParameters", this);
        boolean z = !getDisplayNeedsBlanking();
        this.mControlScreenOffAnimation = z;
        this.mPowerManager = powerManager;
        powerManager.setDozeAfterScreenOff(!z);
        this.mFeatureFlags = featureFlags;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
        tunerService.addTunable(this, "doze_always_on", "accessibility_display_inversion_enabled");
    }

    public boolean getDisplayStateSupported() {
        return getBoolean("doze.display.supported", R$bool.doze_display_state_supported);
    }

    public boolean getDozeSuspendDisplayStateSupported() {
        return this.mResources.getBoolean(R$bool.doze_suspend_display_state_supported);
    }

    public int getPulseDuration() {
        return getPulseInDuration() + getPulseVisibleDuration() + getPulseOutDuration();
    }

    public float getScreenBrightnessDoze() {
        return ((float) this.mResources.getInteger(17694902)) / 255.0f;
    }

    public int getPulseInDuration() {
        return getInt("doze.pulse.duration.in", R$integer.doze_pulse_duration_in);
    }

    public int getPulseVisibleDuration() {
        return getInt("doze.pulse.duration.visible", R$integer.doze_pulse_duration_visible);
    }

    public int getPulseOutDuration() {
        return getInt("doze.pulse.duration.out", R$integer.doze_pulse_duration_out);
    }

    public boolean getPulseOnSigMotion() {
        return getBoolean("doze.pulse.sigmotion", R$bool.doze_pulse_on_significant_motion);
    }

    public boolean getVibrateOnSigMotion() {
        return SystemProperties.getBoolean("doze.vibrate.sigmotion", false);
    }

    public boolean getVibrateOnPickup() {
        return SystemProperties.getBoolean("doze.vibrate.pickup", false);
    }

    public boolean getProxCheckBeforePulse() {
        return getBoolean("doze.pulse.proxcheck", R$bool.doze_proximity_check_before_pulse);
    }

    public boolean getSelectivelyRegisterSensorsUsingProx() {
        return getBoolean("doze.prox.selectively_register", R$bool.doze_selectively_register_prox);
    }

    public int getPickupVibrationThreshold() {
        return getInt("doze.pickup.vibration.threshold", R$integer.doze_pickup_vibration_threshold);
    }

    public long getWallpaperAodDuration() {
        if (shouldControlScreenOff()) {
            return 2500;
        }
        return this.mAlwaysOnPolicy.wallpaperVisibilityDuration;
    }

    public long getWallpaperFadeOutDuration() {
        return this.mAlwaysOnPolicy.wallpaperFadeOutDuration;
    }

    public boolean getAlwaysOn() {
        return this.mDozeAlwaysOn && !this.mBatteryController.isAodPowerSave();
    }

    public boolean isQuickPickupEnabled() {
        return this.mAmbientDisplayConfiguration.quickPickupSensorEnabled(-2);
    }

    public boolean getDisplayNeedsBlanking() {
        return FORCE_BLANKING || (!FORCE_NO_BLANKING && this.mResources.getBoolean(17891505));
    }

    @Override // com.android.systemui.plugins.statusbar.DozeParameters
    public boolean shouldControlScreenOff() {
        return this.mControlScreenOffAnimation;
    }

    public void setControlScreenOffAnimation(boolean z) {
        if (this.mControlScreenOffAnimation != z) {
            this.mControlScreenOffAnimation = z;
            this.mPowerManager.setDozeAfterScreenOff(!z);
        }
    }

    public boolean shouldControlUnlockedScreenOff() {
        return this.mUnlockedScreenOffAnimationController.shouldPlayUnlockedScreenOffAnimation();
    }

    public boolean canControlUnlockedScreenOff() {
        return getAlwaysOn() && this.mFeatureFlags.useNewLockscreenAnimations() && !getDisplayNeedsBlanking();
    }

    private boolean getBoolean(String str, int i) {
        return SystemProperties.getBoolean(str, this.mResources.getBoolean(i));
    }

    private int getInt(String str, int i) {
        return MathUtils.constrain(SystemProperties.getInt(str, this.mResources.getInteger(i)), 0, 60000);
    }

    public int getPulseVisibleDurationExtended() {
        return getPulseVisibleDuration() * 2;
    }

    public boolean doubleTapReportsTouchCoordinates() {
        return this.mResources.getBoolean(R$bool.doze_double_tap_reports_touch_coordinates);
    }

    public boolean singleTapUsesProx() {
        return this.mResources.getBoolean(R$bool.doze_single_tap_uses_prox);
    }

    public boolean longPressUsesProx() {
        return this.mResources.getBoolean(R$bool.doze_long_press_uses_prox);
    }

    public void addCallback(Callback callback) {
        this.mCallbacks.add(callback);
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public void onTuningChanged(String str, String str2) {
        this.mDozeAlwaysOn = this.mAmbientDisplayConfiguration.alwaysOnEnabled(-2);
        for (Callback callback : this.mCallbacks) {
            callback.onAlwaysOnChange();
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.print("getDisplayStateSupported(): ");
        printWriter.println(getDisplayStateSupported());
        printWriter.print("getPulseDuration(): ");
        printWriter.println(getPulseDuration());
        printWriter.print("getPulseInDuration(): ");
        printWriter.println(getPulseInDuration());
        printWriter.print("getPulseInVisibleDuration(): ");
        printWriter.println(getPulseVisibleDuration());
        printWriter.print("getPulseOutDuration(): ");
        printWriter.println(getPulseOutDuration());
        printWriter.print("getPulseOnSigMotion(): ");
        printWriter.println(getPulseOnSigMotion());
        printWriter.print("getVibrateOnSigMotion(): ");
        printWriter.println(getVibrateOnSigMotion());
        printWriter.print("getVibrateOnPickup(): ");
        printWriter.println(getVibrateOnPickup());
        printWriter.print("getProxCheckBeforePulse(): ");
        printWriter.println(getProxCheckBeforePulse());
        printWriter.print("getPickupVibrationThreshold(): ");
        printWriter.println(getPickupVibrationThreshold());
        printWriter.print("getSelectivelyRegisterSensorsUsingProx(): ");
        printWriter.println(getSelectivelyRegisterSensorsUsingProx());
    }
}
