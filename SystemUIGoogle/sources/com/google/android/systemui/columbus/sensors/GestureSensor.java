package com.google.android.systemui.columbus.sensors;

import java.util.Random;
/* compiled from: GestureSensor.kt */
/* loaded from: classes2.dex */
public abstract class GestureSensor implements Sensor {
    private Listener listener;

    /* compiled from: GestureSensor.kt */
    /* loaded from: classes2.dex */
    public interface Listener {
        void onGestureDetected(GestureSensor gestureSensor, int i, DetectionProperties detectionProperties);
    }

    /* compiled from: GestureSensor.kt */
    /* loaded from: classes2.dex */
    public static final class DetectionProperties {
        private final long actionId = new Random().nextLong();
        private final boolean isHapticConsumed;

        public DetectionProperties(boolean z) {
            this.isHapticConsumed = z;
        }

        public final boolean isHapticConsumed() {
            return this.isHapticConsumed;
        }

        public final long getActionId() {
            return this.actionId;
        }
    }

    public final void setGestureListener(Listener listener) {
        this.listener = listener;
    }

    public final void reportGestureDetected(int i, DetectionProperties detectionProperties) {
        Listener listener = this.listener;
        if (listener != null) {
            listener.onGestureDetected(this, i, detectionProperties);
        }
    }
}
