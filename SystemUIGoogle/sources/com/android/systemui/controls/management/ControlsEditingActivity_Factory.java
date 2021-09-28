package com.android.systemui.controls.management;

import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.ui.ControlsUiController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ControlsEditingActivity_Factory implements Factory<ControlsEditingActivity> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<ControlsControllerImpl> controllerProvider;
    private final Provider<CustomIconCache> customIconCacheProvider;
    private final Provider<ControlsUiController> uiControllerProvider;

    public ControlsEditingActivity_Factory(Provider<ControlsControllerImpl> provider, Provider<BroadcastDispatcher> provider2, Provider<CustomIconCache> provider3, Provider<ControlsUiController> provider4) {
        this.controllerProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.customIconCacheProvider = provider3;
        this.uiControllerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public ControlsEditingActivity get() {
        return newInstance(this.controllerProvider.get(), this.broadcastDispatcherProvider.get(), this.customIconCacheProvider.get(), this.uiControllerProvider.get());
    }

    public static ControlsEditingActivity_Factory create(Provider<ControlsControllerImpl> provider, Provider<BroadcastDispatcher> provider2, Provider<CustomIconCache> provider3, Provider<ControlsUiController> provider4) {
        return new ControlsEditingActivity_Factory(provider, provider2, provider3, provider4);
    }

    public static ControlsEditingActivity newInstance(ControlsControllerImpl controlsControllerImpl, BroadcastDispatcher broadcastDispatcher, CustomIconCache customIconCache, ControlsUiController controlsUiController) {
        return new ControlsEditingActivity(controlsControllerImpl, broadcastDispatcher, customIconCache, controlsUiController);
    }
}
