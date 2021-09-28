package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ChargingState.kt */
/* loaded from: classes2.dex */
public final class ChargingState extends TransientGate {
    private final long gateDuration;
    private final ChargingState$powerReceiver$1 powerReceiver = new ChargingState$powerReceiver$1(this);

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ChargingState(Context context, Handler handler, long j) {
        super(context, handler);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(handler, "handler");
        this.gateDuration = j;
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onActivate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        getContext().registerReceiver(this.powerReceiver, intentFilter);
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onDeactivate() {
        getContext().unregisterReceiver(this.powerReceiver);
    }
}
