package com.android.systemui;

import android.content.Context;
import android.os.PowerManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LatencyTester_Factory implements Factory<LatencyTester> {
    private final Provider<BiometricUnlockController> biometricUnlockControllerProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<PowerManager> powerManagerProvider;

    public LatencyTester_Factory(Provider<Context> provider, Provider<BiometricUnlockController> provider2, Provider<PowerManager> provider3, Provider<BroadcastDispatcher> provider4) {
        this.contextProvider = provider;
        this.biometricUnlockControllerProvider = provider2;
        this.powerManagerProvider = provider3;
        this.broadcastDispatcherProvider = provider4;
    }

    @Override // javax.inject.Provider
    public LatencyTester get() {
        return newInstance(this.contextProvider.get(), this.biometricUnlockControllerProvider.get(), this.powerManagerProvider.get(), this.broadcastDispatcherProvider.get());
    }

    public static LatencyTester_Factory create(Provider<Context> provider, Provider<BiometricUnlockController> provider2, Provider<PowerManager> provider3, Provider<BroadcastDispatcher> provider4) {
        return new LatencyTester_Factory(provider, provider2, provider3, provider4);
    }

    public static LatencyTester newInstance(Context context, BiometricUnlockController biometricUnlockController, PowerManager powerManager, BroadcastDispatcher broadcastDispatcher) {
        return new LatencyTester(context, biometricUnlockController, powerManager, broadcastDispatcher);
    }
}
