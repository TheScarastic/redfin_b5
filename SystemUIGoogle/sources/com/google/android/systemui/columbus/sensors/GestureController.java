package com.google.android.systemui.columbus.sensors;

import android.os.SystemClock;
import android.util.Log;
import android.util.SparseLongArray;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.Dumpable;
import com.google.android.systemui.columbus.ColumbusEvent;
import com.google.android.systemui.columbus.sensors.GestureController;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: GestureController.kt */
/* loaded from: classes2.dex */
public class GestureController implements Dumpable {
    public static final Companion Companion = new Companion(null);
    private GestureListener gestureListener;
    private final GestureSensor gestureSensor;
    private final GestureController$gestureSensorListener$1 gestureSensorListener;
    private final SparseLongArray lastTimestampMap = new SparseLongArray();
    private final UiEventLogger uiEventLogger;

    /* compiled from: GestureController.kt */
    /* loaded from: classes2.dex */
    public interface GestureListener {
        void onGestureDetected(GestureSensor gestureSensor, int i, GestureSensor.DetectionProperties detectionProperties);
    }

    public GestureController(GestureSensor gestureSensor, UiEventLogger uiEventLogger) {
        Intrinsics.checkNotNullParameter(gestureSensor, "gestureSensor");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        this.gestureSensor = gestureSensor;
        this.uiEventLogger = uiEventLogger;
        GestureController$gestureSensorListener$1 gestureController$gestureSensorListener$1 = new GestureSensor.Listener(this) { // from class: com.google.android.systemui.columbus.sensors.GestureController$gestureSensorListener$1
            final /* synthetic */ GestureController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.google.android.systemui.columbus.sensors.GestureSensor.Listener
            public void onGestureDetected(GestureSensor gestureSensor2, int i, GestureSensor.DetectionProperties detectionProperties) {
                Intrinsics.checkNotNullParameter(gestureSensor2, "sensor");
                if (this.this$0.isThrottled(i)) {
                    Log.w("Columbus/GestureControl", "Gesture " + i + " throttled");
                    return;
                }
                if (i == 1) {
                    this.this$0.uiEventLogger.log(ColumbusEvent.COLUMBUS_DOUBLE_TAP_DETECTED);
                }
                GestureController.GestureListener gestureListener = this.this$0.gestureListener;
                if (gestureListener != null) {
                    gestureListener.onGestureDetected(gestureSensor2, i, detectionProperties);
                }
            }
        };
        this.gestureSensorListener = gestureController$gestureSensorListener$1;
        gestureSensor.setGestureListener(gestureController$gestureSensorListener$1);
    }

    public void setGestureListener(GestureListener gestureListener) {
        this.gestureListener = gestureListener;
    }

    public boolean startListening() {
        if (this.gestureSensor.isListening()) {
            return false;
        }
        this.gestureSensor.startListening();
        return true;
    }

    public boolean stopListening() {
        if (!this.gestureSensor.isListening()) {
            return false;
        }
        this.gestureSensor.stopListening();
        return true;
    }

    /* access modifiers changed from: private */
    public final boolean isThrottled(int i) {
        long uptimeMillis = SystemClock.uptimeMillis();
        long j = this.lastTimestampMap.get(i);
        this.lastTimestampMap.put(i, uptimeMillis);
        return uptimeMillis - j <= 500;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println(Intrinsics.stringPlus("  Gesture Sensor: ", this.gestureSensor));
        GestureSensor gestureSensor = this.gestureSensor;
        if (gestureSensor instanceof Dumpable) {
            ((Dumpable) gestureSensor).dump(fileDescriptor, printWriter, strArr);
        }
    }

    /* compiled from: GestureController.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
