package com.android.systemui;

import android.os.Handler;
import com.android.systemui.appops.AppOpsController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ForegroundServiceController_Factory implements Factory<ForegroundServiceController> {
    private final Provider<AppOpsController> appOpsControllerProvider;
    private final Provider<Handler> mainHandlerProvider;

    public ForegroundServiceController_Factory(Provider<AppOpsController> provider, Provider<Handler> provider2) {
        this.appOpsControllerProvider = provider;
        this.mainHandlerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public ForegroundServiceController get() {
        return newInstance(this.appOpsControllerProvider.get(), this.mainHandlerProvider.get());
    }

    public static ForegroundServiceController_Factory create(Provider<AppOpsController> provider, Provider<Handler> provider2) {
        return new ForegroundServiceController_Factory(provider, provider2);
    }

    public static ForegroundServiceController newInstance(AppOpsController appOpsController, Handler handler) {
        return new ForegroundServiceController(appOpsController, handler);
    }
}
