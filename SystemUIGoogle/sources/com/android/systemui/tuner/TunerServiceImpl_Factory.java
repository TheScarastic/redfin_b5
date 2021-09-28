package com.android.systemui.tuner;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.leak.LeakDetector;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TunerServiceImpl_Factory implements Factory<TunerServiceImpl> {
    private final Provider<Context> contextProvider;
    private final Provider<DemoModeController> demoModeControllerProvider;
    private final Provider<LeakDetector> leakDetectorProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public TunerServiceImpl_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<LeakDetector> provider3, Provider<DemoModeController> provider4, Provider<UserTracker> provider5) {
        this.contextProvider = provider;
        this.mainHandlerProvider = provider2;
        this.leakDetectorProvider = provider3;
        this.demoModeControllerProvider = provider4;
        this.userTrackerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public TunerServiceImpl get() {
        return newInstance(this.contextProvider.get(), this.mainHandlerProvider.get(), this.leakDetectorProvider.get(), this.demoModeControllerProvider.get(), this.userTrackerProvider.get());
    }

    public static TunerServiceImpl_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<LeakDetector> provider3, Provider<DemoModeController> provider4, Provider<UserTracker> provider5) {
        return new TunerServiceImpl_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static TunerServiceImpl newInstance(Context context, Handler handler, LeakDetector leakDetector, DemoModeController demoModeController, UserTracker userTracker) {
        return new TunerServiceImpl(context, handler, leakDetector, demoModeController, userTracker);
    }
}
