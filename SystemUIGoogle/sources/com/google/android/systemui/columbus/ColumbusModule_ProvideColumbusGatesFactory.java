package com.google.android.systemui.columbus;

import com.google.android.systemui.columbus.gates.CameraVisibility;
import com.google.android.systemui.columbus.gates.ChargingState;
import com.google.android.systemui.columbus.gates.FlagEnabled;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.gates.KeyguardProximity;
import com.google.android.systemui.columbus.gates.PowerSaveState;
import com.google.android.systemui.columbus.gates.PowerState;
import com.google.android.systemui.columbus.gates.ScreenTouch;
import com.google.android.systemui.columbus.gates.SetupWizard;
import com.google.android.systemui.columbus.gates.SystemKeyPress;
import com.google.android.systemui.columbus.gates.TelephonyActivity;
import com.google.android.systemui.columbus.gates.UsbState;
import com.google.android.systemui.columbus.gates.VrMode;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ColumbusModule_ProvideColumbusGatesFactory implements Factory<Set<Gate>> {
    private final Provider<CameraVisibility> cameraVisibilityProvider;
    private final Provider<ChargingState> chargingStateProvider;
    private final Provider<FlagEnabled> flagEnabledProvider;
    private final Provider<KeyguardProximity> keyguardProximityProvider;
    private final Provider<PowerSaveState> powerSaveStateProvider;
    private final Provider<PowerState> powerStateProvider;
    private final Provider<ScreenTouch> screenTouchProvider;
    private final Provider<SetupWizard> setupWizardProvider;
    private final Provider<SystemKeyPress> systemKeyPressProvider;
    private final Provider<TelephonyActivity> telephonyActivityProvider;
    private final Provider<UsbState> usbStateProvider;
    private final Provider<VrMode> vrModeProvider;

    public ColumbusModule_ProvideColumbusGatesFactory(Provider<FlagEnabled> provider, Provider<ChargingState> provider2, Provider<UsbState> provider3, Provider<KeyguardProximity> provider4, Provider<SetupWizard> provider5, Provider<SystemKeyPress> provider6, Provider<TelephonyActivity> provider7, Provider<VrMode> provider8, Provider<CameraVisibility> provider9, Provider<PowerSaveState> provider10, Provider<PowerState> provider11, Provider<ScreenTouch> provider12) {
        this.flagEnabledProvider = provider;
        this.chargingStateProvider = provider2;
        this.usbStateProvider = provider3;
        this.keyguardProximityProvider = provider4;
        this.setupWizardProvider = provider5;
        this.systemKeyPressProvider = provider6;
        this.telephonyActivityProvider = provider7;
        this.vrModeProvider = provider8;
        this.cameraVisibilityProvider = provider9;
        this.powerSaveStateProvider = provider10;
        this.powerStateProvider = provider11;
        this.screenTouchProvider = provider12;
    }

    @Override // javax.inject.Provider
    public Set<Gate> get() {
        return provideColumbusGates(this.flagEnabledProvider.get(), this.chargingStateProvider.get(), this.usbStateProvider.get(), this.keyguardProximityProvider.get(), this.setupWizardProvider.get(), this.systemKeyPressProvider.get(), this.telephonyActivityProvider.get(), this.vrModeProvider.get(), this.cameraVisibilityProvider.get(), this.powerSaveStateProvider.get(), this.powerStateProvider.get(), this.screenTouchProvider.get());
    }

    public static ColumbusModule_ProvideColumbusGatesFactory create(Provider<FlagEnabled> provider, Provider<ChargingState> provider2, Provider<UsbState> provider3, Provider<KeyguardProximity> provider4, Provider<SetupWizard> provider5, Provider<SystemKeyPress> provider6, Provider<TelephonyActivity> provider7, Provider<VrMode> provider8, Provider<CameraVisibility> provider9, Provider<PowerSaveState> provider10, Provider<PowerState> provider11, Provider<ScreenTouch> provider12) {
        return new ColumbusModule_ProvideColumbusGatesFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static Set<Gate> provideColumbusGates(FlagEnabled flagEnabled, ChargingState chargingState, UsbState usbState, KeyguardProximity keyguardProximity, SetupWizard setupWizard, SystemKeyPress systemKeyPress, TelephonyActivity telephonyActivity, VrMode vrMode, CameraVisibility cameraVisibility, PowerSaveState powerSaveState, PowerState powerState, ScreenTouch screenTouch) {
        return (Set) Preconditions.checkNotNullFromProvides(ColumbusModule.provideColumbusGates(flagEnabled, chargingState, usbState, keyguardProximity, setupWizard, systemKeyPress, telephonyActivity, vrMode, cameraVisibility, powerSaveState, powerState, screenTouch));
    }
}
