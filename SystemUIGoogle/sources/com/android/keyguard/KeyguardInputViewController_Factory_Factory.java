package com.android.keyguard;

import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardInputViewController;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardInputViewController_Factory_Factory implements Factory<KeyguardInputViewController.Factory> {
    private final Provider<EmergencyButtonController.Factory> emergencyButtonControllerFactoryProvider;
    private final Provider<FalsingCollector> falsingCollectorProvider;
    private final Provider<InputMethodManager> inputMethodManagerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<LatencyTracker> latencyTrackerProvider;
    private final Provider<LiftToActivateListener> liftToActivateListenerProvider;
    private final Provider<LockPatternUtils> lockPatternUtilsProvider;
    private final Provider<DelayableExecutor> mainExecutorProvider;
    private final Provider<KeyguardMessageAreaController.Factory> messageAreaControllerFactoryProvider;
    private final Provider<Resources> resourcesProvider;
    private final Provider<TelephonyManager> telephonyManagerProvider;

    public KeyguardInputViewController_Factory_Factory(Provider<KeyguardUpdateMonitor> provider, Provider<LockPatternUtils> provider2, Provider<LatencyTracker> provider3, Provider<KeyguardMessageAreaController.Factory> provider4, Provider<InputMethodManager> provider5, Provider<DelayableExecutor> provider6, Provider<Resources> provider7, Provider<LiftToActivateListener> provider8, Provider<TelephonyManager> provider9, Provider<FalsingCollector> provider10, Provider<EmergencyButtonController.Factory> provider11) {
        this.keyguardUpdateMonitorProvider = provider;
        this.lockPatternUtilsProvider = provider2;
        this.latencyTrackerProvider = provider3;
        this.messageAreaControllerFactoryProvider = provider4;
        this.inputMethodManagerProvider = provider5;
        this.mainExecutorProvider = provider6;
        this.resourcesProvider = provider7;
        this.liftToActivateListenerProvider = provider8;
        this.telephonyManagerProvider = provider9;
        this.falsingCollectorProvider = provider10;
        this.emergencyButtonControllerFactoryProvider = provider11;
    }

    @Override // javax.inject.Provider
    public KeyguardInputViewController.Factory get() {
        return newInstance(this.keyguardUpdateMonitorProvider.get(), this.lockPatternUtilsProvider.get(), this.latencyTrackerProvider.get(), this.messageAreaControllerFactoryProvider.get(), this.inputMethodManagerProvider.get(), this.mainExecutorProvider.get(), this.resourcesProvider.get(), this.liftToActivateListenerProvider.get(), this.telephonyManagerProvider.get(), this.falsingCollectorProvider.get(), this.emergencyButtonControllerFactoryProvider.get());
    }

    public static KeyguardInputViewController_Factory_Factory create(Provider<KeyguardUpdateMonitor> provider, Provider<LockPatternUtils> provider2, Provider<LatencyTracker> provider3, Provider<KeyguardMessageAreaController.Factory> provider4, Provider<InputMethodManager> provider5, Provider<DelayableExecutor> provider6, Provider<Resources> provider7, Provider<LiftToActivateListener> provider8, Provider<TelephonyManager> provider9, Provider<FalsingCollector> provider10, Provider<EmergencyButtonController.Factory> provider11) {
        return new KeyguardInputViewController_Factory_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static KeyguardInputViewController.Factory newInstance(KeyguardUpdateMonitor keyguardUpdateMonitor, LockPatternUtils lockPatternUtils, LatencyTracker latencyTracker, KeyguardMessageAreaController.Factory factory, InputMethodManager inputMethodManager, DelayableExecutor delayableExecutor, Resources resources, Object obj, TelephonyManager telephonyManager, FalsingCollector falsingCollector, EmergencyButtonController.Factory factory2) {
        return new KeyguardInputViewController.Factory(keyguardUpdateMonitor, lockPatternUtils, latencyTracker, factory, inputMethodManager, delayableExecutor, resources, (LiftToActivateListener) obj, telephonyManager, falsingCollector, factory2);
    }
}
