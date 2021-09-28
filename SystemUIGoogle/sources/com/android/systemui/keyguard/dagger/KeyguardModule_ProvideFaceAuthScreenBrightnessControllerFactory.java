package com.android.systemui.keyguard.dagger;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.face.FaceManager;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.FaceAuthScreenBrightnessController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SystemSettings;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardModule_ProvideFaceAuthScreenBrightnessControllerFactory implements Factory<Optional<FaceAuthScreenBrightnessController>> {
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<FaceManager> faceManagerProvider;
    private final Provider<GlobalSettings> globalSettingProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    private final Provider<PackageManager> packageManagerProvider;
    private final Provider<Resources> resourcesProvider;
    private final Provider<SystemSettings> systemSettingsProvider;

    public KeyguardModule_ProvideFaceAuthScreenBrightnessControllerFactory(Provider<Context> provider, Provider<NotificationShadeWindowController> provider2, Provider<Resources> provider3, Provider<Handler> provider4, Provider<FaceManager> provider5, Provider<PackageManager> provider6, Provider<KeyguardUpdateMonitor> provider7, Provider<GlobalSettings> provider8, Provider<SystemSettings> provider9, Provider<DumpManager> provider10) {
        this.contextProvider = provider;
        this.notificationShadeWindowControllerProvider = provider2;
        this.resourcesProvider = provider3;
        this.handlerProvider = provider4;
        this.faceManagerProvider = provider5;
        this.packageManagerProvider = provider6;
        this.keyguardUpdateMonitorProvider = provider7;
        this.globalSettingProvider = provider8;
        this.systemSettingsProvider = provider9;
        this.dumpManagerProvider = provider10;
    }

    @Override // javax.inject.Provider
    public Optional<FaceAuthScreenBrightnessController> get() {
        return provideFaceAuthScreenBrightnessController(this.contextProvider.get(), this.notificationShadeWindowControllerProvider.get(), this.resourcesProvider.get(), this.handlerProvider.get(), this.faceManagerProvider.get(), this.packageManagerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.globalSettingProvider.get(), this.systemSettingsProvider.get(), this.dumpManagerProvider.get());
    }

    public static KeyguardModule_ProvideFaceAuthScreenBrightnessControllerFactory create(Provider<Context> provider, Provider<NotificationShadeWindowController> provider2, Provider<Resources> provider3, Provider<Handler> provider4, Provider<FaceManager> provider5, Provider<PackageManager> provider6, Provider<KeyguardUpdateMonitor> provider7, Provider<GlobalSettings> provider8, Provider<SystemSettings> provider9, Provider<DumpManager> provider10) {
        return new KeyguardModule_ProvideFaceAuthScreenBrightnessControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static Optional<FaceAuthScreenBrightnessController> provideFaceAuthScreenBrightnessController(Context context, NotificationShadeWindowController notificationShadeWindowController, Resources resources, Handler handler, FaceManager faceManager, PackageManager packageManager, KeyguardUpdateMonitor keyguardUpdateMonitor, GlobalSettings globalSettings, SystemSettings systemSettings, DumpManager dumpManager) {
        return (Optional) Preconditions.checkNotNullFromProvides(KeyguardModule.provideFaceAuthScreenBrightnessController(context, notificationShadeWindowController, resources, handler, faceManager, packageManager, keyguardUpdateMonitor, globalSettings, systemSettings, dumpManager));
    }
}
