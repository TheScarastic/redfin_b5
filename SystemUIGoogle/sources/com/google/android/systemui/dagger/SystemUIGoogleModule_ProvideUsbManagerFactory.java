package com.google.android.systemui.dagger;

import android.content.Context;
import android.hardware.usb.UsbManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SystemUIGoogleModule_ProvideUsbManagerFactory implements Factory<Optional<UsbManager>> {
    private final Provider<Context> contextProvider;

    public SystemUIGoogleModule_ProvideUsbManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public Optional<UsbManager> get() {
        return provideUsbManager(this.contextProvider.get());
    }

    public static SystemUIGoogleModule_ProvideUsbManagerFactory create(Provider<Context> provider) {
        return new SystemUIGoogleModule_ProvideUsbManagerFactory(provider);
    }

    public static Optional<UsbManager> provideUsbManager(Context context) {
        return (Optional) Preconditions.checkNotNullFromProvides(SystemUIGoogleModule.provideUsbManager(context));
    }
}
