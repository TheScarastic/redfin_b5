package com.android.keyguard;

import android.os.Vibrator;
import android.view.accessibility.AccessibilityManager;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LockIconViewController_Factory implements Factory<LockIconViewController> {
    private final Provider<AccessibilityManager> accessibilityManagerProvider;
    private final Provider<AuthController> authControllerProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<DelayableExecutor> executorProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<KeyguardViewController> keyguardViewControllerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<Vibrator> vibratorProvider;
    private final Provider<LockIconView> viewProvider;

    public LockIconViewController_Factory(Provider<LockIconView> provider, Provider<StatusBarStateController> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<KeyguardViewController> provider4, Provider<KeyguardStateController> provider5, Provider<FalsingManager> provider6, Provider<AuthController> provider7, Provider<DumpManager> provider8, Provider<AccessibilityManager> provider9, Provider<ConfigurationController> provider10, Provider<DelayableExecutor> provider11, Provider<Vibrator> provider12) {
        this.viewProvider = provider;
        this.statusBarStateControllerProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
        this.keyguardViewControllerProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
        this.falsingManagerProvider = provider6;
        this.authControllerProvider = provider7;
        this.dumpManagerProvider = provider8;
        this.accessibilityManagerProvider = provider9;
        this.configurationControllerProvider = provider10;
        this.executorProvider = provider11;
        this.vibratorProvider = provider12;
    }

    @Override // javax.inject.Provider
    public LockIconViewController get() {
        return newInstance(this.viewProvider.get(), this.statusBarStateControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.keyguardViewControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.falsingManagerProvider.get(), this.authControllerProvider.get(), this.dumpManagerProvider.get(), this.accessibilityManagerProvider.get(), this.configurationControllerProvider.get(), this.executorProvider.get(), this.vibratorProvider.get());
    }

    public static LockIconViewController_Factory create(Provider<LockIconView> provider, Provider<StatusBarStateController> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<KeyguardViewController> provider4, Provider<KeyguardStateController> provider5, Provider<FalsingManager> provider6, Provider<AuthController> provider7, Provider<DumpManager> provider8, Provider<AccessibilityManager> provider9, Provider<ConfigurationController> provider10, Provider<DelayableExecutor> provider11, Provider<Vibrator> provider12) {
        return new LockIconViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static LockIconViewController newInstance(LockIconView lockIconView, StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardViewController keyguardViewController, KeyguardStateController keyguardStateController, FalsingManager falsingManager, AuthController authController, DumpManager dumpManager, AccessibilityManager accessibilityManager, ConfigurationController configurationController, DelayableExecutor delayableExecutor, Vibrator vibrator) {
        return new LockIconViewController(lockIconView, statusBarStateController, keyguardUpdateMonitor, keyguardViewController, keyguardStateController, falsingManager, authController, dumpManager, accessibilityManager, configurationController, delayableExecutor, vibrator);
    }
}
