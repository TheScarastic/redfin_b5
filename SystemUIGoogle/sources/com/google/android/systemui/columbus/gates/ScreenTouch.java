package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Choreographer;
import android.view.InputEvent;
import android.view.MotionEvent;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import com.android.systemui.shared.system.InputChannelCompat$InputEventReceiver;
import com.android.systemui.shared.system.InputMonitorCompat;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ScreenTouch.kt */
/* loaded from: classes2.dex */
public final class ScreenTouch extends Gate {
    public static final Companion Companion = new Companion(null);
    private final Handler handler;
    private InputChannelCompat$InputEventReceiver inputEventReceiver;
    private InputMonitorCompat inputMonitor;
    private final PowerState powerState;
    private final ScreenTouch$gateListener$1 gateListener = new ScreenTouch$gateListener$1(this);
    private final InputChannelCompat$InputEventListener inputEventListener = new InputChannelCompat$InputEventListener(this) { // from class: com.google.android.systemui.columbus.gates.ScreenTouch$inputEventListener$1
        final /* synthetic */ ScreenTouch this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // com.android.systemui.shared.system.InputChannelCompat$InputEventListener
        public final void onInputEvent(InputEvent inputEvent) {
            if (inputEvent != null) {
                this.this$0.onInputEvent(inputEvent);
            }
        }
    };
    private final Runnable clearBlocking = new Runnable(this) { // from class: com.google.android.systemui.columbus.gates.ScreenTouch$clearBlocking$1
        final /* synthetic */ ScreenTouch this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // java.lang.Runnable
        public final void run() {
            this.this$0.setBlocking(false);
        }
    };

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ScreenTouch(Context context, PowerState powerState, Handler handler) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(powerState, "powerState");
        Intrinsics.checkNotNullParameter(handler, "handler");
        this.powerState = powerState;
        this.handler = handler;
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onActivate() {
        this.powerState.registerListener(this.gateListener);
        if (!this.powerState.isBlocking()) {
            startListeningForTouch();
        }
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onDeactivate() {
        this.powerState.unregisterListener(this.gateListener);
        stopListeningForTouch();
    }

    /* access modifiers changed from: private */
    public final void startListeningForTouch() {
        if (this.inputEventReceiver == null) {
            InputMonitorCompat inputMonitorCompat = new InputMonitorCompat("Quick Tap", 0);
            this.inputMonitor = inputMonitorCompat;
            this.inputEventReceiver = inputMonitorCompat.getInputReceiver(Looper.getMainLooper(), Choreographer.getInstance(), this.inputEventListener);
        }
    }

    /* access modifiers changed from: private */
    public final void stopListeningForTouch() {
        InputChannelCompat$InputEventReceiver inputChannelCompat$InputEventReceiver = this.inputEventReceiver;
        if (inputChannelCompat$InputEventReceiver != null) {
            inputChannelCompat$InputEventReceiver.dispose();
        }
        this.inputEventReceiver = null;
        InputMonitorCompat inputMonitorCompat = this.inputMonitor;
        if (inputMonitorCompat != null) {
            inputMonitorCompat.dispose();
        }
        this.inputMonitor = null;
    }

    /* access modifiers changed from: private */
    public final void onInputEvent(InputEvent inputEvent) {
        MotionEvent motionEvent = inputEvent instanceof MotionEvent ? (MotionEvent) inputEvent : null;
        if (motionEvent != null) {
            if (motionEvent.getAction() == 0) {
                this.handler.removeCallbacks(this.clearBlocking);
                setBlocking(true);
            } else if (motionEvent.getAction() == 1 && isBlocking()) {
                this.handler.postDelayed(this.clearBlocking, 500);
            }
        }
    }

    /* compiled from: ScreenTouch.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
