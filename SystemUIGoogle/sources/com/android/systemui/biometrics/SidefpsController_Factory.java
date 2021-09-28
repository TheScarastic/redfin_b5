package com.android.systemui.biometrics;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.WindowManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SidefpsController_Factory implements Factory<SidefpsController> {
    private final Provider<Context> contextProvider;
    private final Provider<DisplayManager> displayManagerProvider;
    private final Provider<DelayableExecutor> fgExecutorProvider;
    private final Provider<FingerprintManager> fingerprintManagerProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<LayoutInflater> inflaterProvider;
    private final Provider<WindowManager> windowManagerProvider;

    public SidefpsController_Factory(Provider<Context> provider, Provider<LayoutInflater> provider2, Provider<FingerprintManager> provider3, Provider<WindowManager> provider4, Provider<DelayableExecutor> provider5, Provider<DisplayManager> provider6, Provider<Handler> provider7) {
        this.contextProvider = provider;
        this.inflaterProvider = provider2;
        this.fingerprintManagerProvider = provider3;
        this.windowManagerProvider = provider4;
        this.fgExecutorProvider = provider5;
        this.displayManagerProvider = provider6;
        this.handlerProvider = provider7;
    }

    @Override // javax.inject.Provider
    public SidefpsController get() {
        return newInstance(this.contextProvider.get(), this.inflaterProvider.get(), this.fingerprintManagerProvider.get(), this.windowManagerProvider.get(), this.fgExecutorProvider.get(), this.displayManagerProvider.get(), this.handlerProvider.get());
    }

    public static SidefpsController_Factory create(Provider<Context> provider, Provider<LayoutInflater> provider2, Provider<FingerprintManager> provider3, Provider<WindowManager> provider4, Provider<DelayableExecutor> provider5, Provider<DisplayManager> provider6, Provider<Handler> provider7) {
        return new SidefpsController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static SidefpsController newInstance(Context context, LayoutInflater layoutInflater, FingerprintManager fingerprintManager, WindowManager windowManager, DelayableExecutor delayableExecutor, DisplayManager displayManager, Handler handler) {
        return new SidefpsController(context, layoutInflater, fingerprintManager, windowManager, delayableExecutor, displayManager, handler);
    }
}
