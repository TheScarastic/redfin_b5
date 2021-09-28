package com.android.systemui.usb;

import com.android.systemui.broadcast.BroadcastDispatcher;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class UsbDebuggingSecondaryUserActivity_Factory implements Factory<UsbDebuggingSecondaryUserActivity> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;

    public UsbDebuggingSecondaryUserActivity_Factory(Provider<BroadcastDispatcher> provider) {
        this.broadcastDispatcherProvider = provider;
    }

    @Override // javax.inject.Provider
    public UsbDebuggingSecondaryUserActivity get() {
        return newInstance(this.broadcastDispatcherProvider.get());
    }

    public static UsbDebuggingSecondaryUserActivity_Factory create(Provider<BroadcastDispatcher> provider) {
        return new UsbDebuggingSecondaryUserActivity_Factory(provider);
    }

    public static UsbDebuggingSecondaryUserActivity newInstance(BroadcastDispatcher broadcastDispatcher) {
        return new UsbDebuggingSecondaryUserActivity(broadcastDispatcher);
    }
}
