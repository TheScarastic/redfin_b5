package com.android.keyguard;

import android.content.Context;
import android.os.Looper;
import android.os.Vibrator;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.RingerModeTracker;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardUpdateMonitor_Factory implements Factory<KeyguardUpdateMonitor> {
    private final Provider<AuthController> authControllerProvider;
    private final Provider<Executor> backgroundExecutorProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<LockPatternUtils> lockPatternUtilsProvider;
    private final Provider<Looper> mainLooperProvider;
    private final Provider<RingerModeTracker> ringerModeTrackerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
    private final Provider<Vibrator> vibratorProvider;

    public KeyguardUpdateMonitor_Factory(Provider<Context> provider, Provider<Looper> provider2, Provider<BroadcastDispatcher> provider3, Provider<DumpManager> provider4, Provider<RingerModeTracker> provider5, Provider<Executor> provider6, Provider<StatusBarStateController> provider7, Provider<LockPatternUtils> provider8, Provider<AuthController> provider9, Provider<TelephonyListenerManager> provider10, Provider<FeatureFlags> provider11, Provider<Vibrator> provider12) {
        this.contextProvider = provider;
        this.mainLooperProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
        this.dumpManagerProvider = provider4;
        this.ringerModeTrackerProvider = provider5;
        this.backgroundExecutorProvider = provider6;
        this.statusBarStateControllerProvider = provider7;
        this.lockPatternUtilsProvider = provider8;
        this.authControllerProvider = provider9;
        this.telephonyListenerManagerProvider = provider10;
        this.featureFlagsProvider = provider11;
        this.vibratorProvider = provider12;
    }

    @Override // javax.inject.Provider
    public KeyguardUpdateMonitor get() {
        return newInstance(this.contextProvider.get(), this.mainLooperProvider.get(), this.broadcastDispatcherProvider.get(), this.dumpManagerProvider.get(), this.ringerModeTrackerProvider.get(), this.backgroundExecutorProvider.get(), this.statusBarStateControllerProvider.get(), this.lockPatternUtilsProvider.get(), this.authControllerProvider.get(), this.telephonyListenerManagerProvider.get(), this.featureFlagsProvider.get(), this.vibratorProvider.get());
    }

    public static KeyguardUpdateMonitor_Factory create(Provider<Context> provider, Provider<Looper> provider2, Provider<BroadcastDispatcher> provider3, Provider<DumpManager> provider4, Provider<RingerModeTracker> provider5, Provider<Executor> provider6, Provider<StatusBarStateController> provider7, Provider<LockPatternUtils> provider8, Provider<AuthController> provider9, Provider<TelephonyListenerManager> provider10, Provider<FeatureFlags> provider11, Provider<Vibrator> provider12) {
        return new KeyguardUpdateMonitor_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static KeyguardUpdateMonitor newInstance(Context context, Looper looper, BroadcastDispatcher broadcastDispatcher, DumpManager dumpManager, RingerModeTracker ringerModeTracker, Executor executor, StatusBarStateController statusBarStateController, LockPatternUtils lockPatternUtils, AuthController authController, TelephonyListenerManager telephonyListenerManager, FeatureFlags featureFlags, Vibrator vibrator) {
        return new KeyguardUpdateMonitor(context, looper, broadcastDispatcher, dumpManager, ringerModeTracker, executor, statusBarStateController, lockPatternUtils, authController, telephonyListenerManager, featureFlags, vibrator);
    }
}
