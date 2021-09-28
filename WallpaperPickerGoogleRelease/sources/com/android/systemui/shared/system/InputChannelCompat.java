package com.android.systemui.shared.system;

import android.graphics.Matrix;
import android.os.Looper;
import android.view.BatchedInputEventReceiver;
import android.view.Choreographer;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.MotionEvent;
/* loaded from: classes.dex */
public class InputChannelCompat {

    /* loaded from: classes.dex */
    public interface InputEventListener {
        void onInputEvent(InputEvent inputEvent);
    }

    /* loaded from: classes.dex */
    public static class InputEventReceiver {
        private final BatchedInputEventReceiver mReceiver;

        public InputEventReceiver(InputChannel inputChannel, Looper looper, Choreographer choreographer, final InputEventListener inputEventListener) {
            this.mReceiver = new BatchedInputEventReceiver(inputChannel, looper, choreographer) { // from class: com.android.systemui.shared.system.InputChannelCompat.InputEventReceiver.1
                public void onInputEvent(InputEvent inputEvent) {
                    inputEventListener.onInputEvent(inputEvent);
                    finishInputEvent(inputEvent, true);
                }
            };
        }

        public void dispose() {
            this.mReceiver.dispose();
        }

        public void setBatchingEnabled(boolean z) {
            this.mReceiver.setBatchingEnabled(z);
        }
    }

    public static Matrix createRotationMatrix(int i, int i2, int i3) {
        return MotionEvent.createRotateMatrix(i, i2, i3);
    }

    public static boolean mergeMotionEvent(MotionEvent motionEvent, MotionEvent motionEvent2) {
        return motionEvent2.addBatch(motionEvent);
    }
}
