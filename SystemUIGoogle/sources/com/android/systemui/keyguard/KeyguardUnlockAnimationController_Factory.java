package com.android.systemui.keyguard;

import android.content.Context;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.shared.system.smartspace.SmartspaceTransitionController;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardUnlockAnimationController_Factory implements Factory<KeyguardUnlockAnimationController> {
    private final Provider<Context> contextProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardViewController> keyguardViewControllerProvider;
    private final Provider<KeyguardViewMediator> keyguardViewMediatorProvider;
    private final Provider<SmartspaceTransitionController> smartspaceTransitionControllerProvider;

    public KeyguardUnlockAnimationController_Factory(Provider<Context> provider, Provider<KeyguardStateController> provider2, Provider<KeyguardViewMediator> provider3, Provider<KeyguardViewController> provider4, Provider<SmartspaceTransitionController> provider5, Provider<FeatureFlags> provider6) {
        this.contextProvider = provider;
        this.keyguardStateControllerProvider = provider2;
        this.keyguardViewMediatorProvider = provider3;
        this.keyguardViewControllerProvider = provider4;
        this.smartspaceTransitionControllerProvider = provider5;
        this.featureFlagsProvider = provider6;
    }

    @Override // javax.inject.Provider
    public KeyguardUnlockAnimationController get() {
        return newInstance(this.contextProvider.get(), this.keyguardStateControllerProvider.get(), DoubleCheck.lazy(this.keyguardViewMediatorProvider), this.keyguardViewControllerProvider.get(), this.smartspaceTransitionControllerProvider.get(), this.featureFlagsProvider.get());
    }

    public static KeyguardUnlockAnimationController_Factory create(Provider<Context> provider, Provider<KeyguardStateController> provider2, Provider<KeyguardViewMediator> provider3, Provider<KeyguardViewController> provider4, Provider<SmartspaceTransitionController> provider5, Provider<FeatureFlags> provider6) {
        return new KeyguardUnlockAnimationController_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static KeyguardUnlockAnimationController newInstance(Context context, KeyguardStateController keyguardStateController, Lazy<KeyguardViewMediator> lazy, KeyguardViewController keyguardViewController, SmartspaceTransitionController smartspaceTransitionController, FeatureFlags featureFlags) {
        return new KeyguardUnlockAnimationController(context, keyguardStateController, lazy, keyguardViewController, smartspaceTransitionController, featureFlags);
    }
}
