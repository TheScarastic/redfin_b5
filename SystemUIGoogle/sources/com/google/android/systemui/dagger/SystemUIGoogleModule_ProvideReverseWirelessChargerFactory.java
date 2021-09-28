package com.google.android.systemui.dagger;

import android.content.Context;
import com.google.android.systemui.reversecharging.ReverseWirelessCharger;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SystemUIGoogleModule_ProvideReverseWirelessChargerFactory implements Factory<Optional<ReverseWirelessCharger>> {
    private final Provider<Context> contextProvider;

    public SystemUIGoogleModule_ProvideReverseWirelessChargerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public Optional<ReverseWirelessCharger> get() {
        return provideReverseWirelessCharger(this.contextProvider.get());
    }

    public static SystemUIGoogleModule_ProvideReverseWirelessChargerFactory create(Provider<Context> provider) {
        return new SystemUIGoogleModule_ProvideReverseWirelessChargerFactory(provider);
    }

    public static Optional<ReverseWirelessCharger> provideReverseWirelessCharger(Context context) {
        return (Optional) Preconditions.checkNotNullFromProvides(SystemUIGoogleModule.provideReverseWirelessCharger(context));
    }
}
