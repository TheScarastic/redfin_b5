package com.android.keyguard.dagger;

import com.android.keyguard.KeyguardClockSwitch;
import com.android.keyguard.KeyguardStatusView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardStatusViewModule_GetKeyguardClockSwitchFactory implements Factory<KeyguardClockSwitch> {
    private final Provider<KeyguardStatusView> keyguardPresentationProvider;

    public KeyguardStatusViewModule_GetKeyguardClockSwitchFactory(Provider<KeyguardStatusView> provider) {
        this.keyguardPresentationProvider = provider;
    }

    @Override // javax.inject.Provider
    public KeyguardClockSwitch get() {
        return getKeyguardClockSwitch(this.keyguardPresentationProvider.get());
    }

    public static KeyguardStatusViewModule_GetKeyguardClockSwitchFactory create(Provider<KeyguardStatusView> provider) {
        return new KeyguardStatusViewModule_GetKeyguardClockSwitchFactory(provider);
    }

    public static KeyguardClockSwitch getKeyguardClockSwitch(KeyguardStatusView keyguardStatusView) {
        return (KeyguardClockSwitch) Preconditions.checkNotNullFromProvides(KeyguardStatusViewModule.getKeyguardClockSwitch(keyguardStatusView));
    }
}
