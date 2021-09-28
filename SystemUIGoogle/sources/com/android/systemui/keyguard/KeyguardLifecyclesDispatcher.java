package com.android.systemui.keyguard;

import android.os.Handler;
import android.os.Message;
/* loaded from: classes.dex */
public class KeyguardLifecyclesDispatcher {
    private Handler mHandler = new Handler() { // from class: com.android.systemui.keyguard.KeyguardLifecyclesDispatcher.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    KeyguardLifecyclesDispatcher.this.mScreenLifecycle.dispatchScreenTurningOn();
                    return;
                case 1:
                    KeyguardLifecyclesDispatcher.this.mScreenLifecycle.dispatchScreenTurnedOn();
                    return;
                case 2:
                    KeyguardLifecyclesDispatcher.this.mScreenLifecycle.dispatchScreenTurningOff();
                    return;
                case 3:
                    KeyguardLifecyclesDispatcher.this.mScreenLifecycle.dispatchScreenTurnedOff();
                    return;
                case 4:
                    KeyguardLifecyclesDispatcher.this.mWakefulnessLifecycle.dispatchStartedWakingUp(message.arg1);
                    return;
                case 5:
                    KeyguardLifecyclesDispatcher.this.mWakefulnessLifecycle.dispatchFinishedWakingUp();
                    return;
                case 6:
                    KeyguardLifecyclesDispatcher.this.mWakefulnessLifecycle.dispatchStartedGoingToSleep(message.arg1);
                    return;
                case 7:
                    KeyguardLifecyclesDispatcher.this.mWakefulnessLifecycle.dispatchFinishedGoingToSleep();
                    return;
                default:
                    throw new IllegalArgumentException("Unknown message: " + message);
            }
        }
    };
    private final ScreenLifecycle mScreenLifecycle;
    private final WakefulnessLifecycle mWakefulnessLifecycle;

    public KeyguardLifecyclesDispatcher(ScreenLifecycle screenLifecycle, WakefulnessLifecycle wakefulnessLifecycle) {
        this.mScreenLifecycle = screenLifecycle;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
    }

    /* access modifiers changed from: package-private */
    public void dispatch(int i) {
        this.mHandler.obtainMessage(i).sendToTarget();
    }

    /* access modifiers changed from: package-private */
    public void dispatch(int i, int i2) {
        Message obtainMessage = this.mHandler.obtainMessage(i);
        obtainMessage.arg1 = i2;
        obtainMessage.sendToTarget();
    }
}
