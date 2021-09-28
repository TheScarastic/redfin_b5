package com.android.systemui.keyguard.dagger;

import android.content.Context;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardLiftController;
import com.android.systemui.util.sensors.AsyncSensorManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardModule_ProvideKeyguardLiftControllerFactory implements Factory<KeyguardLiftController> {
    private final Provider<AsyncSensorManager> asyncSensorManagerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public KeyguardModule_ProvideKeyguardLiftControllerFactory(Provider<Context> provider, Provider<StatusBarStateController> provider2, Provider<AsyncSensorManager> provider3, Provider<KeyguardUpdateMonitor> provider4, Provider<DumpManager> provider5) {
        this.contextProvider = provider;
        this.statusBarStateControllerProvider = provider2;
        this.asyncSensorManagerProvider = provider3;
        this.keyguardUpdateMonitorProvider = provider4;
        this.dumpManagerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public KeyguardLiftController get() {
        return provideKeyguardLiftController(this.contextProvider.get(), this.statusBarStateControllerProvider.get(), this.asyncSensorManagerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.dumpManagerProvider.get());
    }

    public static KeyguardModule_ProvideKeyguardLiftControllerFactory create(Provider<Context> provider, Provider<StatusBarStateController> provider2, Provider<AsyncSensorManager> provider3, Provider<KeyguardUpdateMonitor> provider4, Provider<DumpManager> provider5) {
        return new KeyguardModule_ProvideKeyguardLiftControllerFactory(provider, provider2, provider3, provider4, provider5);
    }

    public static KeyguardLiftController provideKeyguardLiftController(Context context, StatusBarStateController statusBarStateController, AsyncSensorManager asyncSensorManager, KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager) {
        return KeyguardModule.provideKeyguardLiftController(context, statusBarStateController, asyncSensorManager, keyguardUpdateMonitor, dumpManager);
    }
}
