package com.android.systemui.keyguard.dagger;

import android.app.trust.TrustManager;
import android.content.Context;
import android.os.PowerManager;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardDisplayManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.DismissCallbackRegistry;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.DeviceConfigProxy;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardModule_NewKeyguardViewMediatorFactory implements Factory<KeyguardViewMediator> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DeviceConfigProxy> deviceConfigProvider;
    private final Provider<DismissCallbackRegistry> dismissCallbackRegistryProvider;
    private final Provider<DozeParameters> dozeParametersProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<FalsingCollector> falsingCollectorProvider;
    private final Provider<KeyguardDisplayManager> keyguardDisplayManagerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUnlockAnimationController> keyguardUnlockAnimationControllerProvider;
    private final Provider<LockPatternUtils> lockPatternUtilsProvider;
    private final Provider<NavigationModeController> navigationModeControllerProvider;
    private final Provider<NotificationShadeDepthController> notificationShadeDepthControllerProvider;
    private final Provider<PowerManager> powerManagerProvider;
    private final Provider<KeyguardViewController> statusBarKeyguardViewManagerLazyProvider;
    private final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    private final Provider<TrustManager> trustManagerProvider;
    private final Provider<Executor> uiBgExecutorProvider;
    private final Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;
    private final Provider<KeyguardUpdateMonitor> updateMonitorProvider;
    private final Provider<UserSwitcherController> userSwitcherControllerProvider;

    public KeyguardModule_NewKeyguardViewMediatorFactory(Provider<Context> provider, Provider<FalsingCollector> provider2, Provider<LockPatternUtils> provider3, Provider<BroadcastDispatcher> provider4, Provider<KeyguardViewController> provider5, Provider<DismissCallbackRegistry> provider6, Provider<KeyguardUpdateMonitor> provider7, Provider<DumpManager> provider8, Provider<PowerManager> provider9, Provider<TrustManager> provider10, Provider<UserSwitcherController> provider11, Provider<Executor> provider12, Provider<DeviceConfigProxy> provider13, Provider<NavigationModeController> provider14, Provider<KeyguardDisplayManager> provider15, Provider<DozeParameters> provider16, Provider<SysuiStatusBarStateController> provider17, Provider<KeyguardStateController> provider18, Provider<KeyguardUnlockAnimationController> provider19, Provider<UnlockedScreenOffAnimationController> provider20, Provider<NotificationShadeDepthController> provider21) {
        this.contextProvider = provider;
        this.falsingCollectorProvider = provider2;
        this.lockPatternUtilsProvider = provider3;
        this.broadcastDispatcherProvider = provider4;
        this.statusBarKeyguardViewManagerLazyProvider = provider5;
        this.dismissCallbackRegistryProvider = provider6;
        this.updateMonitorProvider = provider7;
        this.dumpManagerProvider = provider8;
        this.powerManagerProvider = provider9;
        this.trustManagerProvider = provider10;
        this.userSwitcherControllerProvider = provider11;
        this.uiBgExecutorProvider = provider12;
        this.deviceConfigProvider = provider13;
        this.navigationModeControllerProvider = provider14;
        this.keyguardDisplayManagerProvider = provider15;
        this.dozeParametersProvider = provider16;
        this.statusBarStateControllerProvider = provider17;
        this.keyguardStateControllerProvider = provider18;
        this.keyguardUnlockAnimationControllerProvider = provider19;
        this.unlockedScreenOffAnimationControllerProvider = provider20;
        this.notificationShadeDepthControllerProvider = provider21;
    }

    @Override // javax.inject.Provider
    public KeyguardViewMediator get() {
        return newKeyguardViewMediator(this.contextProvider.get(), this.falsingCollectorProvider.get(), this.lockPatternUtilsProvider.get(), this.broadcastDispatcherProvider.get(), DoubleCheck.lazy(this.statusBarKeyguardViewManagerLazyProvider), this.dismissCallbackRegistryProvider.get(), this.updateMonitorProvider.get(), this.dumpManagerProvider.get(), this.powerManagerProvider.get(), this.trustManagerProvider.get(), this.userSwitcherControllerProvider.get(), this.uiBgExecutorProvider.get(), this.deviceConfigProvider.get(), this.navigationModeControllerProvider.get(), this.keyguardDisplayManagerProvider.get(), this.dozeParametersProvider.get(), this.statusBarStateControllerProvider.get(), this.keyguardStateControllerProvider.get(), DoubleCheck.lazy(this.keyguardUnlockAnimationControllerProvider), this.unlockedScreenOffAnimationControllerProvider.get(), DoubleCheck.lazy(this.notificationShadeDepthControllerProvider));
    }

    public static KeyguardModule_NewKeyguardViewMediatorFactory create(Provider<Context> provider, Provider<FalsingCollector> provider2, Provider<LockPatternUtils> provider3, Provider<BroadcastDispatcher> provider4, Provider<KeyguardViewController> provider5, Provider<DismissCallbackRegistry> provider6, Provider<KeyguardUpdateMonitor> provider7, Provider<DumpManager> provider8, Provider<PowerManager> provider9, Provider<TrustManager> provider10, Provider<UserSwitcherController> provider11, Provider<Executor> provider12, Provider<DeviceConfigProxy> provider13, Provider<NavigationModeController> provider14, Provider<KeyguardDisplayManager> provider15, Provider<DozeParameters> provider16, Provider<SysuiStatusBarStateController> provider17, Provider<KeyguardStateController> provider18, Provider<KeyguardUnlockAnimationController> provider19, Provider<UnlockedScreenOffAnimationController> provider20, Provider<NotificationShadeDepthController> provider21) {
        return new KeyguardModule_NewKeyguardViewMediatorFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21);
    }

    public static KeyguardViewMediator newKeyguardViewMediator(Context context, FalsingCollector falsingCollector, LockPatternUtils lockPatternUtils, BroadcastDispatcher broadcastDispatcher, Lazy<KeyguardViewController> lazy, DismissCallbackRegistry dismissCallbackRegistry, KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager, PowerManager powerManager, TrustManager trustManager, UserSwitcherController userSwitcherController, Executor executor, DeviceConfigProxy deviceConfigProxy, NavigationModeController navigationModeController, KeyguardDisplayManager keyguardDisplayManager, DozeParameters dozeParameters, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardStateController keyguardStateController, Lazy<KeyguardUnlockAnimationController> lazy2, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, Lazy<NotificationShadeDepthController> lazy3) {
        return (KeyguardViewMediator) Preconditions.checkNotNullFromProvides(KeyguardModule.newKeyguardViewMediator(context, falsingCollector, lockPatternUtils, broadcastDispatcher, lazy, dismissCallbackRegistry, keyguardUpdateMonitor, dumpManager, powerManager, trustManager, userSwitcherController, executor, deviceConfigProxy, navigationModeController, keyguardDisplayManager, dozeParameters, sysuiStatusBarStateController, keyguardStateController, lazy2, unlockedScreenOffAnimationController, lazy3));
    }
}
