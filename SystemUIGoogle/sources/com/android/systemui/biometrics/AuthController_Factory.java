package com.android.systemui.biometrics;

import android.app.ActivityTaskManager;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Handler;
import android.view.WindowManager;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AuthController_Factory implements Factory<AuthController> {
    private final Provider<ActivityTaskManager> activityTaskManagerProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DisplayManager> displayManagerProvider;
    private final Provider<FaceManager> faceManagerProvider;
    private final Provider<FingerprintManager> fingerprintManagerProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<SidefpsController> sidefpsControllerFactoryProvider;
    private final Provider<UdfpsController> udfpsControllerFactoryProvider;
    private final Provider<WindowManager> windowManagerProvider;

    public AuthController_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<ActivityTaskManager> provider3, Provider<WindowManager> provider4, Provider<FingerprintManager> provider5, Provider<FaceManager> provider6, Provider<UdfpsController> provider7, Provider<SidefpsController> provider8, Provider<DisplayManager> provider9, Provider<Handler> provider10) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.activityTaskManagerProvider = provider3;
        this.windowManagerProvider = provider4;
        this.fingerprintManagerProvider = provider5;
        this.faceManagerProvider = provider6;
        this.udfpsControllerFactoryProvider = provider7;
        this.sidefpsControllerFactoryProvider = provider8;
        this.displayManagerProvider = provider9;
        this.handlerProvider = provider10;
    }

    @Override // javax.inject.Provider
    public AuthController get() {
        return newInstance(this.contextProvider.get(), this.commandQueueProvider.get(), this.activityTaskManagerProvider.get(), this.windowManagerProvider.get(), this.fingerprintManagerProvider.get(), this.faceManagerProvider.get(), this.udfpsControllerFactoryProvider, this.sidefpsControllerFactoryProvider, this.displayManagerProvider.get(), this.handlerProvider.get());
    }

    public static AuthController_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<ActivityTaskManager> provider3, Provider<WindowManager> provider4, Provider<FingerprintManager> provider5, Provider<FaceManager> provider6, Provider<UdfpsController> provider7, Provider<SidefpsController> provider8, Provider<DisplayManager> provider9, Provider<Handler> provider10) {
        return new AuthController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static AuthController newInstance(Context context, CommandQueue commandQueue, ActivityTaskManager activityTaskManager, WindowManager windowManager, FingerprintManager fingerprintManager, FaceManager faceManager, Provider<UdfpsController> provider, Provider<SidefpsController> provider2, DisplayManager displayManager, Handler handler) {
        return new AuthController(context, commandQueue, activityTaskManager, windowManager, fingerprintManager, faceManager, provider, provider2, displayManager, handler);
    }
}
