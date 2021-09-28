package com.android.systemui.controls.management;

import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.ui.ControlsUiController;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ControlsProviderSelectorActivity_Factory implements Factory<ControlsProviderSelectorActivity> {
    private final Provider<Executor> backExecutorProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<ControlsController> controlsControllerProvider;
    private final Provider<Executor> executorProvider;
    private final Provider<ControlsListingController> listingControllerProvider;
    private final Provider<ControlsUiController> uiControllerProvider;

    public ControlsProviderSelectorActivity_Factory(Provider<Executor> provider, Provider<Executor> provider2, Provider<ControlsListingController> provider3, Provider<ControlsController> provider4, Provider<BroadcastDispatcher> provider5, Provider<ControlsUiController> provider6) {
        this.executorProvider = provider;
        this.backExecutorProvider = provider2;
        this.listingControllerProvider = provider3;
        this.controlsControllerProvider = provider4;
        this.broadcastDispatcherProvider = provider5;
        this.uiControllerProvider = provider6;
    }

    @Override // javax.inject.Provider
    public ControlsProviderSelectorActivity get() {
        return newInstance(this.executorProvider.get(), this.backExecutorProvider.get(), this.listingControllerProvider.get(), this.controlsControllerProvider.get(), this.broadcastDispatcherProvider.get(), this.uiControllerProvider.get());
    }

    public static ControlsProviderSelectorActivity_Factory create(Provider<Executor> provider, Provider<Executor> provider2, Provider<ControlsListingController> provider3, Provider<ControlsController> provider4, Provider<BroadcastDispatcher> provider5, Provider<ControlsUiController> provider6) {
        return new ControlsProviderSelectorActivity_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static ControlsProviderSelectorActivity newInstance(Executor executor, Executor executor2, ControlsListingController controlsListingController, ControlsController controlsController, BroadcastDispatcher broadcastDispatcher, ControlsUiController controlsUiController) {
        return new ControlsProviderSelectorActivity(executor, executor2, controlsListingController, controlsController, broadcastDispatcher, controlsUiController);
    }
}
