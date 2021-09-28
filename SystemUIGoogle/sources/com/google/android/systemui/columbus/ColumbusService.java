package com.google.android.systemui.columbus;

import android.util.Log;
import com.android.systemui.Dumpable;
import com.google.android.systemui.columbus.PowerManagerWrapper;
import com.google.android.systemui.columbus.actions.Action;
import com.google.android.systemui.columbus.feedback.FeedbackEffect;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.sensors.GestureController;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ColumbusService.kt */
/* loaded from: classes2.dex */
public class ColumbusService implements Dumpable {
    public static final Companion Companion = new Companion(null);
    private final List<Action> actions;
    private final Set<FeedbackEffect> effects;
    private final Set<Gate> gates;
    private final GestureController gestureController;
    private Action lastActiveAction;
    private final PowerManagerWrapper.WakeLockWrapper wakeLock;
    private final ColumbusService$actionListener$1 actionListener = new Action.Listener(this) { // from class: com.google.android.systemui.columbus.ColumbusService$actionListener$1
        final /* synthetic */ ColumbusService this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // com.google.android.systemui.columbus.actions.Action.Listener
        public void onActionAvailabilityChanged(Action action) {
            Intrinsics.checkNotNullParameter(action, "action");
            ColumbusService.access$updateSensorListener(this.this$0);
        }
    };
    private final ColumbusService$gateListener$1 gateListener = new ColumbusService$gateListener$1(this);
    private final ColumbusService$gestureListener$1 gestureListener = new GestureController.GestureListener(this) { // from class: com.google.android.systemui.columbus.ColumbusService$gestureListener$1
        final /* synthetic */ ColumbusService this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // com.google.android.systemui.columbus.sensors.GestureController.GestureListener
        public void onGestureDetected(GestureSensor gestureSensor, int i, GestureSensor.DetectionProperties detectionProperties) {
            Intrinsics.checkNotNullParameter(gestureSensor, "sensor");
            if (i != 0) {
                ColumbusService.access$getWakeLock$p(this.this$0).acquire(2000);
            }
            Action access$updateActiveAction = ColumbusService.access$updateActiveAction(this.this$0);
            if (access$updateActiveAction != null) {
                ColumbusService columbusService = this.this$0;
                access$updateActiveAction.onGestureDetected(i, detectionProperties);
                for (FeedbackEffect feedbackEffect : ColumbusService.access$getEffects$p(columbusService)) {
                    feedbackEffect.onGestureDetected(i, detectionProperties);
                }
            }
        }
    };

    public ColumbusService(List<Action> list, Set<FeedbackEffect> set, Set<Gate> set2, GestureController gestureController, PowerManagerWrapper powerManagerWrapper) {
        Intrinsics.checkNotNullParameter(list, "actions");
        Intrinsics.checkNotNullParameter(set, "effects");
        Intrinsics.checkNotNullParameter(set2, "gates");
        Intrinsics.checkNotNullParameter(gestureController, "gestureController");
        Intrinsics.checkNotNullParameter(powerManagerWrapper, "powerManager");
        this.actions = list;
        this.effects = set;
        this.gates = set2;
        this.gestureController = gestureController;
        this.wakeLock = powerManagerWrapper.newWakeLock(1, "Columbus/Service");
        for (Action action : list) {
            action.registerListener(this.actionListener);
        }
        this.gestureController.setGestureListener(this.gestureListener);
        updateSensorListener();
    }

    private final void activateGates() {
        for (Gate gate : this.gates) {
            gate.registerListener(this.gateListener);
        }
    }

    private final void deactivateGates() {
        for (Gate gate : this.gates) {
            gate.unregisterListener(this.gateListener);
        }
    }

    private final Gate blockingGate() {
        Object obj;
        Iterator<T> it = this.gates.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (((Gate) obj).isBlocking()) {
                break;
            }
        }
        return (Gate) obj;
    }

    private final Action firstAvailableAction() {
        Object obj;
        Iterator<T> it = this.actions.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (((Action) obj).isAvailable()) {
                break;
            }
        }
        return (Action) obj;
    }

    private final boolean startListening() {
        return this.gestureController.startListening();
    }

    private final void stopListening() {
        if (this.gestureController.stopListening()) {
            for (FeedbackEffect feedbackEffect : this.effects) {
                feedbackEffect.onGestureDetected(0, null);
            }
            Action updateActiveAction = updateActiveAction();
            if (updateActiveAction != null) {
                updateActiveAction.onGestureDetected(0, null);
            }
        }
    }

    public final Action updateActiveAction() {
        Action firstAvailableAction = firstAvailableAction();
        Action action = this.lastActiveAction;
        if (!(action == null || firstAvailableAction == action)) {
            Log.i("Columbus/Service", "Switching action from " + action + " to " + firstAvailableAction);
            action.onGestureDetected(0, null);
        }
        this.lastActiveAction = firstAvailableAction;
        return firstAvailableAction;
    }

    public final void updateSensorListener() {
        Action updateActiveAction = updateActiveAction();
        if (updateActiveAction == null) {
            Log.i("Columbus/Service", "No available actions");
            deactivateGates();
            stopListening();
            return;
        }
        activateGates();
        Gate blockingGate = blockingGate();
        if (blockingGate != null) {
            Log.i("Columbus/Service", Intrinsics.stringPlus("Gated by ", blockingGate));
            stopListening();
            return;
        }
        Log.i("Columbus/Service", Intrinsics.stringPlus("Unblocked; current action: ", updateActiveAction));
        startListening();
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        String str2;
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println(Intrinsics.stringPlus(ColumbusService.class.getSimpleName(), " state:"));
        printWriter.println("  Gates:");
        Iterator<T> it = this.gates.iterator();
        while (true) {
            str = "X ";
            if (!it.hasNext()) {
                break;
            }
            Gate gate = (Gate) it.next();
            printWriter.print("    ");
            if (gate.getActive()) {
                if (!gate.isBlocking()) {
                    str = "O ";
                }
                printWriter.print(str);
            } else {
                printWriter.print("- ");
            }
            printWriter.println(gate.toString());
        }
        printWriter.println("  Actions:");
        for (Action action : this.actions) {
            printWriter.print("    ");
            if (action.isAvailable()) {
                str2 = "O ";
            } else {
                str2 = str;
            }
            printWriter.print(str2);
            printWriter.println(action.toString());
        }
        printWriter.println(Intrinsics.stringPlus("  Active: ", this.lastActiveAction));
        printWriter.println("  Feedback Effects:");
        for (FeedbackEffect feedbackEffect : this.effects) {
            printWriter.print("    ");
            printWriter.println(feedbackEffect.toString());
        }
        this.gestureController.dump(fileDescriptor, printWriter, strArr);
    }

    /* compiled from: ColumbusService.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
