package com.google.android.systemui.columbus.gates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/* compiled from: PowerSaveState.kt */
/* loaded from: classes2.dex */
public final class PowerSaveState$receiver$1 extends BroadcastReceiver {
    final /* synthetic */ PowerSaveState this$0;

    /* access modifiers changed from: package-private */
    public PowerSaveState$receiver$1(PowerSaveState powerSaveState) {
        this.this$0 = powerSaveState;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        this.this$0.refreshStatus();
    }
}
