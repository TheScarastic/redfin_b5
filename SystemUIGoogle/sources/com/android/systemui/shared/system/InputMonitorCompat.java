package com.android.systemui.shared.system;

import android.hardware.input.InputManager;
import android.os.Looper;
import android.view.Choreographer;
import android.view.InputMonitor;
/* loaded from: classes.dex */
public class InputMonitorCompat {
    private final InputMonitor mInputMonitor;

    public InputMonitorCompat(String str, int i) {
        this.mInputMonitor = InputManager.getInstance().monitorGestureInput(str, i);
    }

    public void dispose() {
        this.mInputMonitor.dispose();
    }

    public InputChannelCompat$InputEventReceiver getInputReceiver(Looper looper, Choreographer choreographer, InputChannelCompat$InputEventListener inputChannelCompat$InputEventListener) {
        return new InputChannelCompat$InputEventReceiver(this.mInputMonitor.getInputChannel(), looper, choreographer, inputChannelCompat$InputEventListener);
    }
}
