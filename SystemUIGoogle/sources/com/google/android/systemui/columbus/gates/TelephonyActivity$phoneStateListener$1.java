package com.google.android.systemui.columbus.gates;

import android.telephony.TelephonyCallback;
/* compiled from: TelephonyActivity.kt */
/* loaded from: classes2.dex */
public final class TelephonyActivity$phoneStateListener$1 implements TelephonyCallback.CallStateListener {
    final /* synthetic */ TelephonyActivity this$0;

    /* access modifiers changed from: package-private */
    public TelephonyActivity$phoneStateListener$1(TelephonyActivity telephonyActivity) {
        this.this$0 = telephonyActivity;
    }

    public void onCallStateChanged(int i) {
        TelephonyActivity telephonyActivity = this.this$0;
        TelephonyActivity.access$setCallBlocked$p(telephonyActivity, TelephonyActivity.access$isCallBlocked(telephonyActivity, Integer.valueOf(i)));
        TelephonyActivity.access$updateBlocking(this.this$0);
    }
}
