package com.android.systemui.statusbar.phone;

import android.hardware.Sensor;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.sensors.AsyncSensorManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: KeyguardLiftController.kt */
/* loaded from: classes.dex */
public final class KeyguardLiftController extends KeyguardUpdateMonitorCallback implements StatusBarStateController.StateListener, Dumpable {
    private final AsyncSensorManager asyncSensorManager;
    private boolean bouncerVisible;
    private boolean isListening;
    private final KeyguardUpdateMonitor keyguardUpdateMonitor;
    private final TriggerEventListener listener = new TriggerEventListener(this) { // from class: com.android.systemui.statusbar.phone.KeyguardLiftController$listener$1
        final /* synthetic */ KeyguardLiftController this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // android.hardware.TriggerEventListener
        public void onTrigger(TriggerEvent triggerEvent) {
            Assert.isMainThread();
            this.this$0.isListening = false;
            this.this$0.updateListeningState();
            this.this$0.keyguardUpdateMonitor.requestFaceAuth(true);
        }
    };
    private final Sensor pickupSensor;
    private final StatusBarStateController statusBarStateController;

    public KeyguardLiftController(StatusBarStateController statusBarStateController, AsyncSensorManager asyncSensorManager, KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(asyncSensorManager, "asyncSensorManager");
        Intrinsics.checkNotNullParameter(keyguardUpdateMonitor, "keyguardUpdateMonitor");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.statusBarStateController = statusBarStateController;
        this.asyncSensorManager = asyncSensorManager;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor;
        this.pickupSensor = asyncSensorManager.getDefaultSensor(25);
        String name = KeyguardLiftController.class.getName();
        Intrinsics.checkNotNullExpressionValue(name, "javaClass.name");
        dumpManager.registerDumpable(name, this);
        statusBarStateController.addCallback(this);
        keyguardUpdateMonitor.registerCallback(this);
        updateListeningState();
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onDozingChanged(boolean z) {
        updateListeningState();
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public void onKeyguardBouncerChanged(boolean z) {
        this.bouncerVisible = z;
        updateListeningState();
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public void onKeyguardVisibilityChanged(boolean z) {
        updateListeningState();
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("KeyguardLiftController:");
        printWriter.println(Intrinsics.stringPlus("  pickupSensor: ", this.pickupSensor));
        printWriter.println(Intrinsics.stringPlus("  isListening: ", Boolean.valueOf(this.isListening)));
        printWriter.println(Intrinsics.stringPlus("  bouncerVisible: ", Boolean.valueOf(this.bouncerVisible)));
    }

    /* access modifiers changed from: private */
    public final void updateListeningState() {
        if (this.pickupSensor != null) {
            boolean z = true;
            boolean z2 = this.keyguardUpdateMonitor.isKeyguardVisible() && !this.statusBarStateController.isDozing();
            boolean isFaceAuthEnabledForUser = this.keyguardUpdateMonitor.isFaceAuthEnabledForUser(KeyguardUpdateMonitor.getCurrentUser());
            if ((!z2 && !this.bouncerVisible) || !isFaceAuthEnabledForUser) {
                z = false;
            }
            if (z != this.isListening) {
                this.isListening = z;
                if (z) {
                    this.asyncSensorManager.requestTriggerSensor(this.listener, this.pickupSensor);
                } else {
                    this.asyncSensorManager.cancelTriggerSensor(this.listener, this.pickupSensor);
                }
            }
        }
    }
}
