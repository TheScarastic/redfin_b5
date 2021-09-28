package com.google.android.systemui.columbus.gates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/* compiled from: UsbState.kt */
/* loaded from: classes2.dex */
public final class UsbState$usbReceiver$1 extends BroadcastReceiver {
    final /* synthetic */ UsbState this$0;

    /* access modifiers changed from: package-private */
    public UsbState$usbReceiver$1(UsbState usbState) {
        this.this$0 = usbState;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            UsbState usbState = this.this$0;
            boolean booleanExtra = intent.getBooleanExtra("connected", false);
            if (booleanExtra != UsbState.access$getUsbConnected$p(usbState)) {
                UsbState.access$setUsbConnected$p(usbState, booleanExtra);
                usbState.blockForMillis(UsbState.access$getGateDuration$p(usbState));
            }
        }
    }
}
