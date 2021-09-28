package com.android.systemui.usb;

import com.android.systemui.broadcast.BroadcastDispatcher;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class UsbDebuggingActivity_Factory implements Factory<UsbDebuggingActivity> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;

    public UsbDebuggingActivity_Factory(Provider<BroadcastDispatcher> provider) {
        this.broadcastDispatcherProvider = provider;
    }

    @Override // javax.inject.Provider
    public UsbDebuggingActivity get() {
        return newInstance(this.broadcastDispatcherProvider.get());
    }

    public static UsbDebuggingActivity_Factory create(Provider<BroadcastDispatcher> provider) {
        return new UsbDebuggingActivity_Factory(provider);
    }

    public static UsbDebuggingActivity newInstance(BroadcastDispatcher broadcastDispatcher) {
        return new UsbDebuggingActivity(broadcastDispatcher);
    }
}
