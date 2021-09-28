package com.google.android.systemui.columbus.gates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ChargingState.kt */
/* loaded from: classes2.dex */
public final class ChargingState$powerReceiver$1 extends BroadcastReceiver {
    final /* synthetic */ ChargingState this$0;

    /* access modifiers changed from: package-private */
    public ChargingState$powerReceiver$1(ChargingState chargingState) {
        this.this$0 = chargingState;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        ChargingState chargingState = this.this$0;
        chargingState.blockForMillis(ChargingState.access$getGateDuration$p(chargingState));
    }
}
