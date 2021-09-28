package com.google.android.systemui.dagger;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.settings.UserContentResolverProvider;
import com.android.systemui.statusbar.policy.BatteryController;
import com.google.android.systemui.reversecharging.ReverseChargingController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SystemUIGoogleModule_ProvideBatteryControllerFactory implements Factory<BatteryController> {
    private final Provider<Handler> bgHandlerProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<UserContentResolverProvider> contentResolverProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DemoModeController> demoModeControllerProvider;
    private final Provider<EnhancedEstimates> enhancedEstimatesProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<PowerManager> powerManagerProvider;
    private final Provider<ReverseChargingController> reverseChargingControllerProvider;

    public SystemUIGoogleModule_ProvideBatteryControllerFactory(Provider<Context> provider, Provider<EnhancedEstimates> provider2, Provider<PowerManager> provider3, Provider<BroadcastDispatcher> provider4, Provider<DemoModeController> provider5, Provider<Handler> provider6, Provider<Handler> provider7, Provider<UserContentResolverProvider> provider8, Provider<ReverseChargingController> provider9) {
        this.contextProvider = provider;
        this.enhancedEstimatesProvider = provider2;
        this.powerManagerProvider = provider3;
        this.broadcastDispatcherProvider = provider4;
        this.demoModeControllerProvider = provider5;
        this.mainHandlerProvider = provider6;
        this.bgHandlerProvider = provider7;
        this.contentResolverProvider = provider8;
        this.reverseChargingControllerProvider = provider9;
    }

    @Override // javax.inject.Provider
    public BatteryController get() {
        return provideBatteryController(this.contextProvider.get(), this.enhancedEstimatesProvider.get(), this.powerManagerProvider.get(), this.broadcastDispatcherProvider.get(), this.demoModeControllerProvider.get(), this.mainHandlerProvider.get(), this.bgHandlerProvider.get(), this.contentResolverProvider.get(), this.reverseChargingControllerProvider.get());
    }

    public static SystemUIGoogleModule_ProvideBatteryControllerFactory create(Provider<Context> provider, Provider<EnhancedEstimates> provider2, Provider<PowerManager> provider3, Provider<BroadcastDispatcher> provider4, Provider<DemoModeController> provider5, Provider<Handler> provider6, Provider<Handler> provider7, Provider<UserContentResolverProvider> provider8, Provider<ReverseChargingController> provider9) {
        return new SystemUIGoogleModule_ProvideBatteryControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static BatteryController provideBatteryController(Context context, EnhancedEstimates enhancedEstimates, PowerManager powerManager, BroadcastDispatcher broadcastDispatcher, DemoModeController demoModeController, Handler handler, Handler handler2, UserContentResolverProvider userContentResolverProvider, ReverseChargingController reverseChargingController) {
        return (BatteryController) Preconditions.checkNotNullFromProvides(SystemUIGoogleModule.provideBatteryController(context, enhancedEstimates, powerManager, broadcastDispatcher, demoModeController, handler, handler2, userContentResolverProvider, reverseChargingController));
    }
}
