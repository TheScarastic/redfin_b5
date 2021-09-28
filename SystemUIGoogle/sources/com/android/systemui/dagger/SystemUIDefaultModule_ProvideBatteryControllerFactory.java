package com.android.systemui.dagger;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.statusbar.policy.BatteryController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SystemUIDefaultModule_ProvideBatteryControllerFactory implements Factory<BatteryController> {
    private final Provider<Handler> bgHandlerProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DemoModeController> demoModeControllerProvider;
    private final Provider<EnhancedEstimates> enhancedEstimatesProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<PowerManager> powerManagerProvider;

    public SystemUIDefaultModule_ProvideBatteryControllerFactory(Provider<Context> provider, Provider<EnhancedEstimates> provider2, Provider<PowerManager> provider3, Provider<BroadcastDispatcher> provider4, Provider<DemoModeController> provider5, Provider<Handler> provider6, Provider<Handler> provider7) {
        this.contextProvider = provider;
        this.enhancedEstimatesProvider = provider2;
        this.powerManagerProvider = provider3;
        this.broadcastDispatcherProvider = provider4;
        this.demoModeControllerProvider = provider5;
        this.mainHandlerProvider = provider6;
        this.bgHandlerProvider = provider7;
    }

    @Override // javax.inject.Provider
    public BatteryController get() {
        return provideBatteryController(this.contextProvider.get(), this.enhancedEstimatesProvider.get(), this.powerManagerProvider.get(), this.broadcastDispatcherProvider.get(), this.demoModeControllerProvider.get(), this.mainHandlerProvider.get(), this.bgHandlerProvider.get());
    }

    public static SystemUIDefaultModule_ProvideBatteryControllerFactory create(Provider<Context> provider, Provider<EnhancedEstimates> provider2, Provider<PowerManager> provider3, Provider<BroadcastDispatcher> provider4, Provider<DemoModeController> provider5, Provider<Handler> provider6, Provider<Handler> provider7) {
        return new SystemUIDefaultModule_ProvideBatteryControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static BatteryController provideBatteryController(Context context, EnhancedEstimates enhancedEstimates, PowerManager powerManager, BroadcastDispatcher broadcastDispatcher, DemoModeController demoModeController, Handler handler, Handler handler2) {
        return (BatteryController) Preconditions.checkNotNullFromProvides(SystemUIDefaultModule.provideBatteryController(context, enhancedEstimates, powerManager, broadcastDispatcher, demoModeController, handler, handler2));
    }
}
