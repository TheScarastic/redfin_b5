package com.google.android.systemui.reversecharging;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ReverseChargingViewController_Factory implements Factory<ReverseChargingViewController> {
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<KeyguardIndicationControllerGoogle> keyguardIndicationControllerProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<StatusBarIconController> statusBarIconControllerProvider;
    private final Provider<StatusBar> statusBarLazyProvider;

    public ReverseChargingViewController_Factory(Provider<Context> provider, Provider<BatteryController> provider2, Provider<StatusBar> provider3, Provider<StatusBarIconController> provider4, Provider<BroadcastDispatcher> provider5, Provider<Executor> provider6, Provider<KeyguardIndicationControllerGoogle> provider7) {
        this.contextProvider = provider;
        this.batteryControllerProvider = provider2;
        this.statusBarLazyProvider = provider3;
        this.statusBarIconControllerProvider = provider4;
        this.broadcastDispatcherProvider = provider5;
        this.mainExecutorProvider = provider6;
        this.keyguardIndicationControllerProvider = provider7;
    }

    @Override // javax.inject.Provider
    public ReverseChargingViewController get() {
        return newInstance(this.contextProvider.get(), this.batteryControllerProvider.get(), DoubleCheck.lazy(this.statusBarLazyProvider), this.statusBarIconControllerProvider.get(), this.broadcastDispatcherProvider.get(), this.mainExecutorProvider.get(), this.keyguardIndicationControllerProvider.get());
    }

    public static ReverseChargingViewController_Factory create(Provider<Context> provider, Provider<BatteryController> provider2, Provider<StatusBar> provider3, Provider<StatusBarIconController> provider4, Provider<BroadcastDispatcher> provider5, Provider<Executor> provider6, Provider<KeyguardIndicationControllerGoogle> provider7) {
        return new ReverseChargingViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static ReverseChargingViewController newInstance(Context context, BatteryController batteryController, Lazy<StatusBar> lazy, StatusBarIconController statusBarIconController, BroadcastDispatcher broadcastDispatcher, Executor executor, KeyguardIndicationControllerGoogle keyguardIndicationControllerGoogle) {
        return new ReverseChargingViewController(context, batteryController, lazy, statusBarIconController, broadcastDispatcher, executor, keyguardIndicationControllerGoogle);
    }
}
