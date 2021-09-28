package com.android.systemui.biometrics;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.hardware.biometrics.BiometricSourceType;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.statusbar.CircleReveal;
import com.android.systemui.statusbar.LightRevealEffect;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ViewController;
import java.io.PrintWriter;
import java.util.List;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringNumberConversionsJVMKt;
/* compiled from: AuthRippleController.kt */
/* loaded from: classes.dex */
public final class AuthRippleController extends ViewController<AuthRippleView> {
    private final AuthController authController;
    private final BiometricUnlockController biometricUnlockController;
    private final KeyguardBypassController bypassController;
    private LightRevealEffect circleReveal;
    private final CommandRegistry commandRegistry;
    private final ConfigurationController configurationController;
    private PointF faceSensorLocation;
    private PointF fingerprintSensorLocation;
    private final KeyguardUpdateMonitor keyguardUpdateMonitor;
    private final NotificationShadeWindowController notificationShadeWindowController;
    private final StatusBar statusBar;
    private final Context sysuiContext;
    private final KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback(this) { // from class: com.android.systemui.biometrics.AuthRippleController$keyguardUpdateMonitorCallback$1
        final /* synthetic */ AuthRippleController this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            AuthRippleController.access$showRipple(this.this$0, biometricSourceType);
        }
    };
    private final ConfigurationController.ConfigurationListener configurationChangedListener = new ConfigurationController.ConfigurationListener(this) { // from class: com.android.systemui.biometrics.AuthRippleController$configurationChangedListener$1
        final /* synthetic */ AuthRippleController this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onConfigChanged(Configuration configuration) {
            this.this$0.updateSensorLocation();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onUiModeChanged() {
            AuthRippleController.access$updateRippleColor(this.this$0);
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onThemeChanged() {
            AuthRippleController.access$updateRippleColor(this.this$0);
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onOverlayChanged() {
            AuthRippleController.access$updateRippleColor(this.this$0);
        }
    };
    private final AuthController.Callback authControllerCallback = new AuthController.Callback(this) { // from class: com.android.systemui.biometrics.AuthRippleController$authControllerCallback$1
        final /* synthetic */ AuthRippleController this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // com.android.systemui.biometrics.AuthController.Callback
        public final void onAllAuthenticatorsRegistered() {
            this.this$0.updateSensorLocation();
        }
    };

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public AuthRippleController(StatusBar statusBar, Context context, AuthController authController, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, CommandRegistry commandRegistry, NotificationShadeWindowController notificationShadeWindowController, KeyguardBypassController keyguardBypassController, BiometricUnlockController biometricUnlockController, AuthRippleView authRippleView) {
        super(authRippleView);
        Intrinsics.checkNotNullParameter(statusBar, "statusBar");
        Intrinsics.checkNotNullParameter(context, "sysuiContext");
        Intrinsics.checkNotNullParameter(authController, "authController");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(keyguardUpdateMonitor, "keyguardUpdateMonitor");
        Intrinsics.checkNotNullParameter(commandRegistry, "commandRegistry");
        Intrinsics.checkNotNullParameter(notificationShadeWindowController, "notificationShadeWindowController");
        Intrinsics.checkNotNullParameter(keyguardBypassController, "bypassController");
        Intrinsics.checkNotNullParameter(biometricUnlockController, "biometricUnlockController");
        this.statusBar = statusBar;
        this.sysuiContext = context;
        this.authController = authController;
        this.configurationController = configurationController;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor;
        this.commandRegistry = commandRegistry;
        this.notificationShadeWindowController = notificationShadeWindowController;
        this.bypassController = keyguardBypassController;
        this.biometricUnlockController = biometricUnlockController;
    }

    public final PointF getFingerprintSensorLocation() {
        return this.fingerprintSensorLocation;
    }

    @Override // com.android.systemui.util.ViewController
    public void onViewAttached() {
        updateRippleColor();
        updateSensorLocation();
        this.authController.addCallback(this.authControllerCallback);
        this.configurationController.addCallback(this.configurationChangedListener);
        this.keyguardUpdateMonitor.registerCallback(this.keyguardUpdateMonitorCallback);
        this.commandRegistry.registerCommand("auth-ripple", new Function0<Command>(this) { // from class: com.android.systemui.biometrics.AuthRippleController$onViewAttached$1
            final /* synthetic */ AuthRippleController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // kotlin.jvm.functions.Function0
            public final Command invoke() {
                return new AuthRippleController.AuthRippleCommand(this.this$0);
            }
        });
    }

    @Override // com.android.systemui.util.ViewController
    public void onViewDetached() {
        this.authController.removeCallback(this.authControllerCallback);
        this.keyguardUpdateMonitor.removeCallback(this.keyguardUpdateMonitorCallback);
        this.configurationController.removeCallback(this.configurationChangedListener);
        this.commandRegistry.unregisterCommand("auth-ripple");
        this.notificationShadeWindowController.setForcePluginOpen(false, this);
    }

    public final void showRipple(BiometricSourceType biometricSourceType) {
        PointF pointF;
        if (this.keyguardUpdateMonitor.isKeyguardVisible() && !this.keyguardUpdateMonitor.userNeedsStrongAuth()) {
            if (biometricSourceType == BiometricSourceType.FINGERPRINT && (pointF = this.fingerprintSensorLocation) != null) {
                Intrinsics.checkNotNull(pointF);
                ((AuthRippleView) this.mView).setSensorLocation(pointF);
                showRipple();
            } else if (biometricSourceType == BiometricSourceType.FACE && this.faceSensorLocation != null && this.bypassController.canBypass()) {
                PointF pointF2 = this.faceSensorLocation;
                Intrinsics.checkNotNull(pointF2);
                ((AuthRippleView) this.mView).setSensorLocation(pointF2);
                showRipple();
            }
        }
    }

    public final void showRipple() {
        boolean z = true;
        this.notificationShadeWindowController.setForcePluginOpen(true, this);
        int mode = this.biometricUnlockController.getMode();
        if (this.circleReveal == null || !(mode == 1 || mode == 2 || mode == 6)) {
            z = false;
        }
        LightRevealScrim lightRevealScrim = this.statusBar.getLightRevealScrim();
        if (z && lightRevealScrim != null) {
            LightRevealEffect lightRevealEffect = this.circleReveal;
            Intrinsics.checkNotNull(lightRevealEffect);
            lightRevealScrim.setRevealEffect(lightRevealEffect);
        }
        AuthRippleView authRippleView = (AuthRippleView) this.mView;
        AuthRippleController$showRipple$1 authRippleController$showRipple$1 = new Runnable(this) { // from class: com.android.systemui.biometrics.AuthRippleController$showRipple$1
            final /* synthetic */ AuthRippleController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // java.lang.Runnable
            public final void run() {
                AuthRippleController.access$getNotificationShadeWindowController$p(this.this$0).setForcePluginOpen(false, this.this$0);
            }
        };
        if (!z) {
            lightRevealScrim = null;
        }
        authRippleView.startRipple(authRippleController$showRipple$1, lightRevealScrim);
    }

    public final void updateSensorLocation() {
        this.fingerprintSensorLocation = this.authController.getFingerprintSensorLocation();
        this.faceSensorLocation = this.authController.getFaceAuthSensorLocation();
        PointF pointF = this.fingerprintSensorLocation;
        if (pointF != null) {
            float f = pointF.x;
            this.circleReveal = new CircleReveal(f, pointF.y, 0.0f, Math.max(Math.max(f, this.statusBar.getDisplayWidth() - pointF.x), Math.max(pointF.y, this.statusBar.getDisplayHeight() - pointF.y)));
        }
    }

    public final void updateRippleColor() {
        ((AuthRippleView) this.mView).setColor(Utils.getColorAttr(this.sysuiContext, 16843829).getDefaultColor());
    }

    /* compiled from: AuthRippleController.kt */
    /* loaded from: classes.dex */
    public final class AuthRippleCommand implements Command {
        final /* synthetic */ AuthRippleController this$0;

        public AuthRippleCommand(AuthRippleController authRippleController) {
            Intrinsics.checkNotNullParameter(authRippleController, "this$0");
            this.this$0 = authRippleController;
        }

        @Override // com.android.systemui.statusbar.commandline.Command
        public void execute(PrintWriter printWriter, List<String> list) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            Intrinsics.checkNotNullParameter(list, "args");
            if (list.isEmpty()) {
                invalidCommand(printWriter);
                return;
            }
            String str = list.get(0);
            int hashCode = str.hashCode();
            if (hashCode != -1375934236) {
                if (hashCode != -1349088399) {
                    if (hashCode == 3135069 && str.equals("face")) {
                        printWriter.println(Intrinsics.stringPlus("face ripple sensorLocation=", this.this$0.faceSensorLocation));
                        this.this$0.showRipple(BiometricSourceType.FACE);
                        return;
                    }
                } else if (str.equals("custom")) {
                    if (list.size() != 3 || StringsKt__StringNumberConversionsJVMKt.toFloatOrNull(list.get(1)) == null || StringsKt__StringNumberConversionsJVMKt.toFloatOrNull(list.get(2)) == null) {
                        invalidCommand(printWriter);
                        return;
                    }
                    printWriter.println("custom ripple sensorLocation=" + Float.parseFloat(list.get(1)) + ", " + Float.parseFloat(list.get(2)));
                    ((AuthRippleView) ((ViewController) this.this$0).mView).setSensorLocation(new PointF(Float.parseFloat(list.get(1)), Float.parseFloat(list.get(2))));
                    this.this$0.showRipple();
                    return;
                }
            } else if (str.equals("fingerprint")) {
                printWriter.println(Intrinsics.stringPlus("fingerprint ripple sensorLocation=", this.this$0.getFingerprintSensorLocation()));
                this.this$0.showRipple(BiometricSourceType.FINGERPRINT);
                return;
            }
            invalidCommand(printWriter);
        }

        public void help(PrintWriter printWriter) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            printWriter.println("Usage: adb shell cmd statusbar auth-ripple <command>");
            printWriter.println("Available commands:");
            printWriter.println("  fingerprint");
            printWriter.println("  face");
            printWriter.println("  custom <x-location: int> <y-location: int>");
        }

        public final void invalidCommand(PrintWriter printWriter) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            printWriter.println("invalid command");
            help(printWriter);
        }
    }
}
