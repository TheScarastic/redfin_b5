package com.google.android.systemui.reversecharging;

import android.app.AlarmManager;
import android.content.Context;
import android.hardware.usb.UsbManager;
import com.android.systemui.BootCompleteCache;
import com.android.systemui.broadcast.BroadcastDispatcher;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ReverseChargingController_Factory implements Factory<ReverseChargingController> {
    private final Provider<AlarmManager> alarmManagerProvider;
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<BootCompleteCache> bootCompleteCacheProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<Optional<ReverseWirelessCharger>> rtxChargerManagerOptionalProvider;
    private final Provider<Optional<UsbManager>> usbManagerOptionalProvider;

    public ReverseChargingController_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<Optional<ReverseWirelessCharger>> provider3, Provider<AlarmManager> provider4, Provider<Optional<UsbManager>> provider5, Provider<Executor> provider6, Provider<Executor> provider7, Provider<BootCompleteCache> provider8) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.rtxChargerManagerOptionalProvider = provider3;
        this.alarmManagerProvider = provider4;
        this.usbManagerOptionalProvider = provider5;
        this.mainExecutorProvider = provider6;
        this.bgExecutorProvider = provider7;
        this.bootCompleteCacheProvider = provider8;
    }

    @Override // javax.inject.Provider
    public ReverseChargingController get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get(), this.rtxChargerManagerOptionalProvider.get(), this.alarmManagerProvider.get(), this.usbManagerOptionalProvider.get(), this.mainExecutorProvider.get(), this.bgExecutorProvider.get(), this.bootCompleteCacheProvider.get());
    }

    public static ReverseChargingController_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<Optional<ReverseWirelessCharger>> provider3, Provider<AlarmManager> provider4, Provider<Optional<UsbManager>> provider5, Provider<Executor> provider6, Provider<Executor> provider7, Provider<BootCompleteCache> provider8) {
        return new ReverseChargingController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static ReverseChargingController newInstance(Context context, BroadcastDispatcher broadcastDispatcher, Optional<ReverseWirelessCharger> optional, AlarmManager alarmManager, Optional<UsbManager> optional2, Executor executor, Executor executor2, BootCompleteCache bootCompleteCache) {
        return new ReverseChargingController(context, broadcastDispatcher, optional, alarmManager, optional2, executor, executor2, bootCompleteCache);
    }
}
