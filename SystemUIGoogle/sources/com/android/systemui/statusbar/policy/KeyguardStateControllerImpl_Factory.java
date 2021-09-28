package com.android.systemui.statusbar.policy;

import android.content.Context;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.shared.system.smartspace.SmartspaceTransitionController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardStateControllerImpl_Factory implements Factory<KeyguardStateControllerImpl> {
    private final Provider<Context> contextProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<LockPatternUtils> lockPatternUtilsProvider;
    private final Provider<SmartspaceTransitionController> smartspaceTransitionControllerProvider;

    public KeyguardStateControllerImpl_Factory(Provider<Context> provider, Provider<KeyguardUpdateMonitor> provider2, Provider<LockPatternUtils> provider3, Provider<SmartspaceTransitionController> provider4) {
        this.contextProvider = provider;
        this.keyguardUpdateMonitorProvider = provider2;
        this.lockPatternUtilsProvider = provider3;
        this.smartspaceTransitionControllerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public KeyguardStateControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.lockPatternUtilsProvider.get(), this.smartspaceTransitionControllerProvider.get());
    }

    public static KeyguardStateControllerImpl_Factory create(Provider<Context> provider, Provider<KeyguardUpdateMonitor> provider2, Provider<LockPatternUtils> provider3, Provider<SmartspaceTransitionController> provider4) {
        return new KeyguardStateControllerImpl_Factory(provider, provider2, provider3, provider4);
    }

    public static KeyguardStateControllerImpl newInstance(Context context, KeyguardUpdateMonitor keyguardUpdateMonitor, LockPatternUtils lockPatternUtils, SmartspaceTransitionController smartspaceTransitionController) {
        return new KeyguardStateControllerImpl(context, keyguardUpdateMonitor, lockPatternUtils, smartspaceTransitionController);
    }
}
