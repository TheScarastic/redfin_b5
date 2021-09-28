package com.google.android.systemui.elmyra.feedback;

import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class OpaLockscreen_Factory implements Factory<OpaLockscreen> {
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<StatusBar> statusBarProvider;

    public OpaLockscreen_Factory(Provider<StatusBar> provider, Provider<KeyguardStateController> provider2) {
        this.statusBarProvider = provider;
        this.keyguardStateControllerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public OpaLockscreen get() {
        return newInstance(this.statusBarProvider.get(), this.keyguardStateControllerProvider.get());
    }

    public static OpaLockscreen_Factory create(Provider<StatusBar> provider, Provider<KeyguardStateController> provider2) {
        return new OpaLockscreen_Factory(provider, provider2);
    }

    public static OpaLockscreen newInstance(StatusBar statusBar, KeyguardStateController keyguardStateController) {
        return new OpaLockscreen(statusBar, keyguardStateController);
    }
}
