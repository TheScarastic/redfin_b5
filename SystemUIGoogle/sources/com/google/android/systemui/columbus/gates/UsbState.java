package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UsbState.kt */
/* loaded from: classes2.dex */
public final class UsbState extends TransientGate {
    private final long gateDuration;
    private boolean usbConnected;
    private final UsbState$usbReceiver$1 usbReceiver = new UsbState$usbReceiver$1(this);

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public UsbState(Context context, Handler handler, long j) {
        super(context, handler);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(handler, "handler");
        this.gateDuration = j;
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onActivate() {
        IntentFilter intentFilter = new IntentFilter("android.hardware.usb.action.USB_STATE");
        Intent registerReceiver = getContext().registerReceiver(null, intentFilter);
        if (registerReceiver != null) {
            this.usbConnected = registerReceiver.getBooleanExtra("connected", false);
        }
        getContext().registerReceiver(this.usbReceiver, intentFilter);
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onDeactivate() {
        getContext().unregisterReceiver(this.usbReceiver);
    }
}
