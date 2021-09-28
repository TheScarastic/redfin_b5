package com.android.systemui.controls.ui;

import android.content.Context;
import android.content.SharedPreferences;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ControlsUiControllerImpl_Factory implements Factory<ControlsUiControllerImpl> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<DelayableExecutor> bgExecutorProvider;
    private final Provider<Context> contextProvider;
    private final Provider<ControlActionCoordinator> controlActionCoordinatorProvider;
    private final Provider<ControlsController> controlsControllerProvider;
    private final Provider<ControlsListingController> controlsListingControllerProvider;
    private final Provider<ControlsMetricsLogger> controlsMetricsLoggerProvider;
    private final Provider<CustomIconCache> iconCacheProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<ShadeController> shadeControllerProvider;
    private final Provider<SharedPreferences> sharedPreferencesProvider;
    private final Provider<DelayableExecutor> uiExecutorProvider;

    public ControlsUiControllerImpl_Factory(Provider<ControlsController> provider, Provider<Context> provider2, Provider<DelayableExecutor> provider3, Provider<DelayableExecutor> provider4, Provider<ControlsListingController> provider5, Provider<SharedPreferences> provider6, Provider<ControlActionCoordinator> provider7, Provider<ActivityStarter> provider8, Provider<ShadeController> provider9, Provider<CustomIconCache> provider10, Provider<ControlsMetricsLogger> provider11, Provider<KeyguardStateController> provider12) {
        this.controlsControllerProvider = provider;
        this.contextProvider = provider2;
        this.uiExecutorProvider = provider3;
        this.bgExecutorProvider = provider4;
        this.controlsListingControllerProvider = provider5;
        this.sharedPreferencesProvider = provider6;
        this.controlActionCoordinatorProvider = provider7;
        this.activityStarterProvider = provider8;
        this.shadeControllerProvider = provider9;
        this.iconCacheProvider = provider10;
        this.controlsMetricsLoggerProvider = provider11;
        this.keyguardStateControllerProvider = provider12;
    }

    @Override // javax.inject.Provider
    public ControlsUiControllerImpl get() {
        return newInstance(DoubleCheck.lazy(this.controlsControllerProvider), this.contextProvider.get(), this.uiExecutorProvider.get(), this.bgExecutorProvider.get(), DoubleCheck.lazy(this.controlsListingControllerProvider), this.sharedPreferencesProvider.get(), this.controlActionCoordinatorProvider.get(), this.activityStarterProvider.get(), this.shadeControllerProvider.get(), this.iconCacheProvider.get(), this.controlsMetricsLoggerProvider.get(), this.keyguardStateControllerProvider.get());
    }

    public static ControlsUiControllerImpl_Factory create(Provider<ControlsController> provider, Provider<Context> provider2, Provider<DelayableExecutor> provider3, Provider<DelayableExecutor> provider4, Provider<ControlsListingController> provider5, Provider<SharedPreferences> provider6, Provider<ControlActionCoordinator> provider7, Provider<ActivityStarter> provider8, Provider<ShadeController> provider9, Provider<CustomIconCache> provider10, Provider<ControlsMetricsLogger> provider11, Provider<KeyguardStateController> provider12) {
        return new ControlsUiControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static ControlsUiControllerImpl newInstance(Lazy<ControlsController> lazy, Context context, DelayableExecutor delayableExecutor, DelayableExecutor delayableExecutor2, Lazy<ControlsListingController> lazy2, SharedPreferences sharedPreferences, ControlActionCoordinator controlActionCoordinator, ActivityStarter activityStarter, ShadeController shadeController, CustomIconCache customIconCache, ControlsMetricsLogger controlsMetricsLogger, KeyguardStateController keyguardStateController) {
        return new ControlsUiControllerImpl(lazy, context, delayableExecutor, delayableExecutor2, lazy2, sharedPreferences, controlActionCoordinator, activityStarter, shadeController, customIconCache, controlsMetricsLogger, keyguardStateController);
    }
}
