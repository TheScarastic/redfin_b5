package com.android.systemui.statusbar;

import android.app.WallpaperManager;
import android.view.Choreographer;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationShadeDepthController_Factory implements Factory<NotificationShadeDepthController> {
    private final Provider<BiometricUnlockController> biometricUnlockControllerProvider;
    private final Provider<BlurUtils> blurUtilsProvider;
    private final Provider<Choreographer> choreographerProvider;
    private final Provider<DozeParameters> dozeParametersProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<WallpaperManager> wallpaperManagerProvider;

    public NotificationShadeDepthController_Factory(Provider<StatusBarStateController> provider, Provider<BlurUtils> provider2, Provider<BiometricUnlockController> provider3, Provider<KeyguardStateController> provider4, Provider<Choreographer> provider5, Provider<WallpaperManager> provider6, Provider<NotificationShadeWindowController> provider7, Provider<DozeParameters> provider8, Provider<DumpManager> provider9) {
        this.statusBarStateControllerProvider = provider;
        this.blurUtilsProvider = provider2;
        this.biometricUnlockControllerProvider = provider3;
        this.keyguardStateControllerProvider = provider4;
        this.choreographerProvider = provider5;
        this.wallpaperManagerProvider = provider6;
        this.notificationShadeWindowControllerProvider = provider7;
        this.dozeParametersProvider = provider8;
        this.dumpManagerProvider = provider9;
    }

    @Override // javax.inject.Provider
    public NotificationShadeDepthController get() {
        return newInstance(this.statusBarStateControllerProvider.get(), this.blurUtilsProvider.get(), this.biometricUnlockControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.choreographerProvider.get(), this.wallpaperManagerProvider.get(), this.notificationShadeWindowControllerProvider.get(), this.dozeParametersProvider.get(), this.dumpManagerProvider.get());
    }

    public static NotificationShadeDepthController_Factory create(Provider<StatusBarStateController> provider, Provider<BlurUtils> provider2, Provider<BiometricUnlockController> provider3, Provider<KeyguardStateController> provider4, Provider<Choreographer> provider5, Provider<WallpaperManager> provider6, Provider<NotificationShadeWindowController> provider7, Provider<DozeParameters> provider8, Provider<DumpManager> provider9) {
        return new NotificationShadeDepthController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static NotificationShadeDepthController newInstance(StatusBarStateController statusBarStateController, BlurUtils blurUtils, BiometricUnlockController biometricUnlockController, KeyguardStateController keyguardStateController, Choreographer choreographer, WallpaperManager wallpaperManager, NotificationShadeWindowController notificationShadeWindowController, DozeParameters dozeParameters, DumpManager dumpManager) {
        return new NotificationShadeDepthController(statusBarStateController, blurUtils, biometricUnlockController, keyguardStateController, choreographer, wallpaperManager, notificationShadeWindowController, dozeParameters, dumpManager);
    }
}
