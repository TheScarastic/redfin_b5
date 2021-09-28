package com.android.systemui.doze;

import com.android.keyguard.KeyguardUpdateMonitor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeAuthRemover_Factory implements Factory<DozeAuthRemover> {
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;

    public DozeAuthRemover_Factory(Provider<KeyguardUpdateMonitor> provider) {
        this.keyguardUpdateMonitorProvider = provider;
    }

    @Override // javax.inject.Provider
    public DozeAuthRemover get() {
        return newInstance(this.keyguardUpdateMonitorProvider.get());
    }

    public static DozeAuthRemover_Factory create(Provider<KeyguardUpdateMonitor> provider) {
        return new DozeAuthRemover_Factory(provider);
    }

    public static DozeAuthRemover newInstance(KeyguardUpdateMonitor keyguardUpdateMonitor) {
        return new DozeAuthRemover(keyguardUpdateMonitor);
    }
}
