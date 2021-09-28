package com.android.systemui.biometrics;

import android.media.AudioAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import java.io.PrintWriter;
import java.util.List;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UdfpsHapticsSimulator.kt */
/* loaded from: classes.dex */
public final class UdfpsHapticsSimulator implements Command {
    private final KeyguardUpdateMonitor keyguardUpdateMonitor;
    private final AudioAttributes sonificationEffects = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    private UdfpsController udfpsController;
    private final Vibrator vibrator;

    public UdfpsHapticsSimulator(CommandRegistry commandRegistry, Vibrator vibrator, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        Intrinsics.checkNotNullParameter(commandRegistry, "commandRegistry");
        Intrinsics.checkNotNullParameter(keyguardUpdateMonitor, "keyguardUpdateMonitor");
        this.vibrator = vibrator;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor;
        commandRegistry.registerCommand("udfps-haptic", new Function0<Command>(this) { // from class: com.android.systemui.biometrics.UdfpsHapticsSimulator.1
            final /* synthetic */ UdfpsHapticsSimulator this$0;

            {
                this.this$0 = r1;
            }

            @Override // kotlin.jvm.functions.Function0
            public final Command invoke() {
                return this.this$0;
            }
        });
    }

    public final void setUdfpsController(UdfpsController udfpsController) {
        this.udfpsController = udfpsController;
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
        switch (str.hashCode()) {
            case -1867169789:
                if (str.equals("success")) {
                    Vibrator vibrator = this.vibrator;
                    if (vibrator != null) {
                        vibrator.vibrate(VibrationEffect.get(0), this.sonificationEffects);
                        return;
                    }
                    return;
                }
                break;
            case -1731151282:
                if (str.equals("acquired")) {
                    this.keyguardUpdateMonitor.playAcquiredHaptic();
                    return;
                }
                break;
            case 96784904:
                if (str.equals("error")) {
                    Vibrator vibrator2 = this.vibrator;
                    if (vibrator2 != null) {
                        vibrator2.vibrate(VibrationEffect.get(1), this.sonificationEffects);
                        return;
                    }
                    return;
                }
                break;
            case 109757538:
                if (str.equals("start")) {
                    UdfpsController udfpsController = this.udfpsController;
                    if (udfpsController != null) {
                        udfpsController.playStartHaptic();
                        return;
                    }
                    return;
                }
                break;
        }
        invalidCommand(printWriter);
    }

    public void help(PrintWriter printWriter) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        printWriter.println("Usage: adb shell cmd statusbar udfps-haptic <haptic>");
        printWriter.println("Available commands:");
        printWriter.println("  start");
        printWriter.println("  acquired");
        printWriter.println("  success, always plays CLICK haptic");
        printWriter.println("  error, always plays DOUBLE_CLICK haptic");
    }

    public final void invalidCommand(PrintWriter printWriter) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        printWriter.println("invalid command");
        help(printWriter);
    }
}
