package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class UnlockedScreenOffAnimationController_Factory implements Factory<UnlockedScreenOffAnimationController> {
    private final Provider<Context> contextProvider;
    private final Provider<DozeParameters> dozeParametersProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardViewMediator> keyguardViewMediatorLazyProvider;
    private final Provider<StatusBarStateControllerImpl> statusBarStateControllerImplProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public UnlockedScreenOffAnimationController_Factory(Provider<Context> provider, Provider<WakefulnessLifecycle> provider2, Provider<StatusBarStateControllerImpl> provider3, Provider<KeyguardViewMediator> provider4, Provider<KeyguardStateController> provider5, Provider<DozeParameters> provider6) {
        this.contextProvider = provider;
        this.wakefulnessLifecycleProvider = provider2;
        this.statusBarStateControllerImplProvider = provider3;
        this.keyguardViewMediatorLazyProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
        this.dozeParametersProvider = provider6;
    }

    @Override // javax.inject.Provider
    public UnlockedScreenOffAnimationController get() {
        return newInstance(this.contextProvider.get(), this.wakefulnessLifecycleProvider.get(), this.statusBarStateControllerImplProvider.get(), DoubleCheck.lazy(this.keyguardViewMediatorLazyProvider), this.keyguardStateControllerProvider.get(), DoubleCheck.lazy(this.dozeParametersProvider));
    }

    public static UnlockedScreenOffAnimationController_Factory create(Provider<Context> provider, Provider<WakefulnessLifecycle> provider2, Provider<StatusBarStateControllerImpl> provider3, Provider<KeyguardViewMediator> provider4, Provider<KeyguardStateController> provider5, Provider<DozeParameters> provider6) {
        return new UnlockedScreenOffAnimationController_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static UnlockedScreenOffAnimationController newInstance(Context context, WakefulnessLifecycle wakefulnessLifecycle, StatusBarStateControllerImpl statusBarStateControllerImpl, Lazy<KeyguardViewMediator> lazy, KeyguardStateController keyguardStateController, Lazy<DozeParameters> lazy2) {
        return new UnlockedScreenOffAnimationController(context, wakefulnessLifecycle, statusBarStateControllerImpl, lazy, keyguardStateController, lazy2);
    }
}
