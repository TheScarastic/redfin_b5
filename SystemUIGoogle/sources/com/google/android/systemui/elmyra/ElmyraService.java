package com.google.android.systemui.elmyra;

import android.content.Context;
import android.metrics.LogMaker;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.Dumpable;
import com.google.android.systemui.elmyra.actions.Action;
import com.google.android.systemui.elmyra.feedback.FeedbackEffect;
import com.google.android.systemui.elmyra.gates.Gate;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class ElmyraService implements Dumpable {
    private final List<Action> mActions;
    private final Context mContext;
    private final List<FeedbackEffect> mFeedbackEffects;
    private final List<Gate> mGates;
    private final GestureSensor.Listener mGestureListener;
    private final GestureSensor mGestureSensor;
    private Action mLastActiveAction;
    private long mLastPrimedGesture;
    private int mLastStage;
    private final PowerManager mPowerManager;
    private final UiEventLogger mUiEventLogger;
    private final PowerManager.WakeLock mWakeLock;
    private final Action.Listener mActionListener = new Action.Listener() { // from class: com.google.android.systemui.elmyra.ElmyraService.1
        @Override // com.google.android.systemui.elmyra.actions.Action.Listener
        public void onActionAvailabilityChanged(Action action) {
            ElmyraService.this.updateSensorListener();
        }
    };
    private final Gate.Listener mGateListener = new Gate.Listener() { // from class: com.google.android.systemui.elmyra.ElmyraService.2
        @Override // com.google.android.systemui.elmyra.gates.Gate.Listener
        public void onGateChanged(Gate gate) {
            ElmyraService.this.updateSensorListener();
        }
    };
    private final MetricsLogger mLogger = new MetricsLogger();

    public ElmyraService(Context context, ServiceConfiguration serviceConfiguration, UiEventLogger uiEventLogger) {
        GestureListener gestureListener = new GestureListener();
        this.mGestureListener = gestureListener;
        this.mContext = context;
        this.mUiEventLogger = uiEventLogger;
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        this.mPowerManager = powerManager;
        this.mWakeLock = powerManager.newWakeLock(1, "Elmyra/ElmyraService");
        ArrayList arrayList = new ArrayList(serviceConfiguration.getActions());
        this.mActions = arrayList;
        arrayList.forEach(new Consumer() { // from class: com.google.android.systemui.elmyra.ElmyraService$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ElmyraService.this.lambda$new$0((Action) obj);
            }
        });
        this.mFeedbackEffects = new ArrayList(serviceConfiguration.getFeedbackEffects());
        ArrayList arrayList2 = new ArrayList(serviceConfiguration.getGates());
        this.mGates = arrayList2;
        arrayList2.forEach(new Consumer() { // from class: com.google.android.systemui.elmyra.ElmyraService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ElmyraService.this.lambda$new$1((Gate) obj);
            }
        });
        GestureSensor gestureSensor = serviceConfiguration.getGestureSensor();
        this.mGestureSensor = gestureSensor;
        if (gestureSensor != null) {
            gestureSensor.setGestureListener(gestureListener);
        }
        updateSensorListener();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Action action) {
        action.setListener(this.mActionListener);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Gate gate) {
        gate.setListener(this.mGateListener);
    }

    private void activateGates() {
        for (int i = 0; i < this.mGates.size(); i++) {
            this.mGates.get(i).activate();
        }
    }

    private void deactivateGates() {
        for (int i = 0; i < this.mGates.size(); i++) {
            this.mGates.get(i).deactivate();
        }
    }

    private Gate blockingGate() {
        for (int i = 0; i < this.mGates.size(); i++) {
            if (this.mGates.get(i).isBlocking()) {
                return this.mGates.get(i);
            }
        }
        return null;
    }

    private Action firstAvailableAction() {
        for (int i = 0; i < this.mActions.size(); i++) {
            if (this.mActions.get(i).isAvailable()) {
                return this.mActions.get(i);
            }
        }
        return null;
    }

    private void startListening() {
        GestureSensor gestureSensor = this.mGestureSensor;
        if (gestureSensor != null && !gestureSensor.isListening()) {
            this.mGestureSensor.startListening();
        }
    }

    private void stopListening() {
        GestureSensor gestureSensor = this.mGestureSensor;
        if (gestureSensor != null && gestureSensor.isListening()) {
            this.mGestureSensor.stopListening();
            for (int i = 0; i < this.mFeedbackEffects.size(); i++) {
                this.mFeedbackEffects.get(i).onRelease();
            }
            Action updateActiveAction = updateActiveAction();
            if (updateActiveAction != null) {
                updateActiveAction.onProgress(0.0f, 0);
            }
        }
    }

    /* access modifiers changed from: private */
    public Action updateActiveAction() {
        Action firstAvailableAction = firstAvailableAction();
        Action action = this.mLastActiveAction;
        if (!(action == null || firstAvailableAction == action)) {
            Log.i("Elmyra/ElmyraService", "Switching action from " + this.mLastActiveAction + " to " + firstAvailableAction);
            this.mLastActiveAction.onProgress(0.0f, 0);
        }
        this.mLastActiveAction = firstAvailableAction;
        return firstAvailableAction;
    }

    protected void updateSensorListener() {
        Action updateActiveAction = updateActiveAction();
        if (updateActiveAction == null) {
            Log.i("Elmyra/ElmyraService", "No available actions");
            deactivateGates();
            stopListening();
            return;
        }
        activateGates();
        Gate blockingGate = blockingGate();
        if (blockingGate != null) {
            Log.i("Elmyra/ElmyraService", "Gated by " + blockingGate);
            stopListening();
            return;
        }
        Log.i("Elmyra/ElmyraService", "Unblocked; current action: " + updateActiveAction);
        startListening();
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        String str2;
        printWriter.println(ElmyraService.class.getSimpleName() + " state:");
        printWriter.println("  Gates:");
        int i = 0;
        while (true) {
            str = "X ";
            if (i >= this.mGates.size()) {
                break;
            }
            printWriter.print("    ");
            if (this.mGates.get(i).isActive()) {
                if (!this.mGates.get(i).isBlocking()) {
                    str = "O ";
                }
                printWriter.print(str);
            } else {
                printWriter.print("- ");
            }
            printWriter.println(this.mGates.get(i).toString());
            i++;
        }
        printWriter.println("  Actions:");
        for (int i2 = 0; i2 < this.mActions.size(); i2++) {
            printWriter.print("    ");
            if (this.mActions.get(i2).isAvailable()) {
                str2 = "O ";
            } else {
                str2 = str;
            }
            printWriter.print(str2);
            printWriter.println(this.mActions.get(i2).toString());
        }
        printWriter.println("  Active: " + this.mLastActiveAction);
        printWriter.println("  Feedback Effects:");
        for (int i3 = 0; i3 < this.mFeedbackEffects.size(); i3++) {
            printWriter.print("    ");
            printWriter.println(this.mFeedbackEffects.get(i3).toString());
        }
        printWriter.println("  Gesture Sensor: " + this.mGestureSensor.toString());
        GestureSensor gestureSensor = this.mGestureSensor;
        if (gestureSensor instanceof Dumpable) {
            ((Dumpable) gestureSensor).dump(fileDescriptor, printWriter, strArr);
        }
    }

    /* loaded from: classes2.dex */
    private class GestureListener implements GestureSensor.Listener {
        private GestureListener() {
        }

        @Override // com.google.android.systemui.elmyra.sensors.GestureSensor.Listener
        public void onGestureProgress(GestureSensor gestureSensor, float f, int i) {
            Action updateActiveAction = ElmyraService.this.updateActiveAction();
            if (updateActiveAction != null) {
                updateActiveAction.onProgress(f, i);
                for (int i2 = 0; i2 < ElmyraService.this.mFeedbackEffects.size(); i2++) {
                    ((FeedbackEffect) ElmyraService.this.mFeedbackEffects.get(i2)).onProgress(f, i);
                }
            }
            if (i != ElmyraService.this.mLastStage) {
                long uptimeMillis = SystemClock.uptimeMillis();
                if (i == 2) {
                    ElmyraService.this.mUiEventLogger.log(ElmyraEvent.ELMYRA_PRIMED);
                    ElmyraService.this.mLogger.action(998);
                    ElmyraService.this.mLastPrimedGesture = uptimeMillis;
                } else if (i == 0 && ElmyraService.this.mLastPrimedGesture != 0) {
                    ElmyraService.this.mUiEventLogger.log(ElmyraEvent.ELMYRA_RELEASED);
                    ElmyraService.this.mLogger.write(new LogMaker(997).setType(4).setLatency(uptimeMillis - ElmyraService.this.mLastPrimedGesture));
                }
                ElmyraService.this.mLastStage = i;
            }
        }

        @Override // com.google.android.systemui.elmyra.sensors.GestureSensor.Listener
        public void onGestureDetected(GestureSensor gestureSensor, GestureSensor.DetectionProperties detectionProperties) {
            ElmyraEvent elmyraEvent;
            ElmyraService.this.mWakeLock.acquire(2000);
            boolean isInteractive = ElmyraService.this.mPowerManager.isInteractive();
            LogMaker latency = new LogMaker(999).setType(4).setSubtype((detectionProperties == null || !detectionProperties.isHostSuspended()) ? !isInteractive ? 2 : 1 : 3).setLatency(isInteractive ? SystemClock.uptimeMillis() - ElmyraService.this.mLastPrimedGesture : 0);
            ElmyraService.this.mLastPrimedGesture = 0;
            Action updateActiveAction = ElmyraService.this.updateActiveAction();
            if (updateActiveAction != null) {
                Log.i("Elmyra/ElmyraService", "Triggering " + updateActiveAction);
                updateActiveAction.onTrigger(detectionProperties);
                for (int i = 0; i < ElmyraService.this.mFeedbackEffects.size(); i++) {
                    ((FeedbackEffect) ElmyraService.this.mFeedbackEffects.get(i)).onResolve(detectionProperties);
                }
                latency.setPackageName(updateActiveAction.getClass().getName());
            }
            UiEventLogger uiEventLogger = ElmyraService.this.mUiEventLogger;
            if (detectionProperties != null && detectionProperties.isHostSuspended()) {
                elmyraEvent = ElmyraEvent.ELMYRA_TRIGGERED_AP_SUSPENDED;
            } else if (!isInteractive) {
                elmyraEvent = ElmyraEvent.ELMYRA_TRIGGERED_SCREEN_OFF;
            } else {
                elmyraEvent = ElmyraEvent.ELMYRA_TRIGGERED_SCREEN_ON;
            }
            uiEventLogger.log(elmyraEvent);
            ElmyraService.this.mLogger.write(latency);
        }
    }
}
