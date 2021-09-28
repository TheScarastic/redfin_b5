package com.android.systemui.controls.dagger;

import android.content.Context;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ControlsComponent_Factory implements Factory<ControlsComponent> {
    private final Provider<Context> contextProvider;
    private final Provider<ControlsController> controlsControllerProvider;
    private final Provider<ControlsListingController> controlsListingControllerProvider;
    private final Provider<ControlsUiController> controlsUiControllerProvider;
    private final Provider<Boolean> featureEnabledProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<LockPatternUtils> lockPatternUtilsProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public ControlsComponent_Factory(Provider<Boolean> provider, Provider<Context> provider2, Provider<ControlsController> provider3, Provider<ControlsUiController> provider4, Provider<ControlsListingController> provider5, Provider<LockPatternUtils> provider6, Provider<KeyguardStateController> provider7, Provider<UserTracker> provider8, Provider<SecureSettings> provider9) {
        this.featureEnabledProvider = provider;
        this.contextProvider = provider2;
        this.controlsControllerProvider = provider3;
        this.controlsUiControllerProvider = provider4;
        this.controlsListingControllerProvider = provider5;
        this.lockPatternUtilsProvider = provider6;
        this.keyguardStateControllerProvider = provider7;
        this.userTrackerProvider = provider8;
        this.secureSettingsProvider = provider9;
    }

    @Override // javax.inject.Provider
    public ControlsComponent get() {
        return newInstance(this.featureEnabledProvider.get().booleanValue(), this.contextProvider.get(), DoubleCheck.lazy(this.controlsControllerProvider), DoubleCheck.lazy(this.controlsUiControllerProvider), DoubleCheck.lazy(this.controlsListingControllerProvider), this.lockPatternUtilsProvider.get(), this.keyguardStateControllerProvider.get(), this.userTrackerProvider.get(), this.secureSettingsProvider.get());
    }

    public static ControlsComponent_Factory create(Provider<Boolean> provider, Provider<Context> provider2, Provider<ControlsController> provider3, Provider<ControlsUiController> provider4, Provider<ControlsListingController> provider5, Provider<LockPatternUtils> provider6, Provider<KeyguardStateController> provider7, Provider<UserTracker> provider8, Provider<SecureSettings> provider9) {
        return new ControlsComponent_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static ControlsComponent newInstance(boolean z, Context context, Lazy<ControlsController> lazy, Lazy<ControlsUiController> lazy2, Lazy<ControlsListingController> lazy3, LockPatternUtils lockPatternUtils, KeyguardStateController keyguardStateController, UserTracker userTracker, SecureSettings secureSettings) {
        return new ControlsComponent(z, context, lazy, lazy2, lazy3, lockPatternUtils, keyguardStateController, userTracker, secureSettings);
    }
}
