package com.android.systemui.globalactions;

import android.content.Context;
import com.android.systemui.statusbar.BlurUtils;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GlobalActionsImpl_Factory implements Factory<GlobalActionsImpl> {
    private final Provider<BlurUtils> blurUtilsProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    private final Provider<GlobalActionsDialogLite> globalActionsDialogLazyProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;

    public GlobalActionsImpl_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<GlobalActionsDialogLite> provider3, Provider<BlurUtils> provider4, Provider<KeyguardStateController> provider5, Provider<DeviceProvisionedController> provider6) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.globalActionsDialogLazyProvider = provider3;
        this.blurUtilsProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
        this.deviceProvisionedControllerProvider = provider6;
    }

    @Override // javax.inject.Provider
    public GlobalActionsImpl get() {
        return newInstance(this.contextProvider.get(), this.commandQueueProvider.get(), DoubleCheck.lazy(this.globalActionsDialogLazyProvider), this.blurUtilsProvider.get(), this.keyguardStateControllerProvider.get(), this.deviceProvisionedControllerProvider.get());
    }

    public static GlobalActionsImpl_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<GlobalActionsDialogLite> provider3, Provider<BlurUtils> provider4, Provider<KeyguardStateController> provider5, Provider<DeviceProvisionedController> provider6) {
        return new GlobalActionsImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static GlobalActionsImpl newInstance(Context context, CommandQueue commandQueue, Lazy<GlobalActionsDialogLite> lazy, BlurUtils blurUtils, KeyguardStateController keyguardStateController, DeviceProvisionedController deviceProvisionedController) {
        return new GlobalActionsImpl(context, commandQueue, lazy, blurUtils, keyguardStateController, deviceProvisionedController);
    }
}
