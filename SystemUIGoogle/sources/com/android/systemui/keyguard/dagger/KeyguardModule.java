package com.android.systemui.keyguard.dagger;

import android.app.trust.TrustManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.face.FaceManager;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.os.Handler;
import android.os.PowerManager;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardDisplayManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.DismissCallbackRegistry;
import com.android.systemui.keyguard.FaceAuthScreenBrightnessController;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.KeyguardLiftController;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SystemSettings;
import dagger.Lazy;
import java.util.Optional;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class KeyguardModule {
    public static KeyguardViewMediator newKeyguardViewMediator(Context context, FalsingCollector falsingCollector, LockPatternUtils lockPatternUtils, BroadcastDispatcher broadcastDispatcher, Lazy<KeyguardViewController> lazy, DismissCallbackRegistry dismissCallbackRegistry, KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager, PowerManager powerManager, TrustManager trustManager, UserSwitcherController userSwitcherController, Executor executor, DeviceConfigProxy deviceConfigProxy, NavigationModeController navigationModeController, KeyguardDisplayManager keyguardDisplayManager, DozeParameters dozeParameters, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardStateController keyguardStateController, Lazy<KeyguardUnlockAnimationController> lazy2, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, Lazy<NotificationShadeDepthController> lazy3) {
        return new KeyguardViewMediator(context, falsingCollector, lockPatternUtils, broadcastDispatcher, lazy, dismissCallbackRegistry, keyguardUpdateMonitor, dumpManager, executor, powerManager, trustManager, userSwitcherController, deviceConfigProxy, navigationModeController, keyguardDisplayManager, dozeParameters, sysuiStatusBarStateController, keyguardStateController, lazy2, unlockedScreenOffAnimationController, lazy3);
    }

    /* access modifiers changed from: package-private */
    public static KeyguardLiftController provideKeyguardLiftController(Context context, StatusBarStateController statusBarStateController, AsyncSensorManager asyncSensorManager, KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager) {
        if (!context.getPackageManager().hasSystemFeature("android.hardware.biometrics.face")) {
            return null;
        }
        return new KeyguardLiftController(statusBarStateController, asyncSensorManager, keyguardUpdateMonitor, dumpManager);
    }

    /* access modifiers changed from: package-private */
    public static Optional<FaceAuthScreenBrightnessController> provideFaceAuthScreenBrightnessController(Context context, NotificationShadeWindowController notificationShadeWindowController, Resources resources, Handler handler, FaceManager faceManager, PackageManager packageManager, KeyguardUpdateMonitor keyguardUpdateMonitor, GlobalSettings globalSettings, SystemSettings systemSettings, DumpManager dumpManager) {
        if (faceManager == null || !packageManager.hasSystemFeature("android.hardware.biometrics.face")) {
            return Optional.empty();
        }
        if (!faceManager.getSensorPropertiesInternal().stream().anyMatch(KeyguardModule$$ExternalSyntheticLambda0.INSTANCE)) {
            return Optional.empty();
        }
        return Optional.of(new FaceAuthScreenBrightnessController(notificationShadeWindowController, keyguardUpdateMonitor, resources, globalSettings, systemSettings, handler, dumpManager, false));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$provideFaceAuthScreenBrightnessController$0(FaceSensorPropertiesInternal faceSensorPropertiesInternal) {
        return !faceSensorPropertiesInternal.supportsSelfIllumination;
    }
}
