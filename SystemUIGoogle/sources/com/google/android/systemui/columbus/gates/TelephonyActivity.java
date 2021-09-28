package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.telephony.TelephonyManager;
import com.android.systemui.telephony.TelephonyListenerManager;
import dagger.Lazy;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TelephonyActivity.kt */
/* loaded from: classes2.dex */
public final class TelephonyActivity extends Gate {
    private boolean isCallBlocked;
    private final TelephonyActivity$phoneStateListener$1 phoneStateListener = new TelephonyActivity$phoneStateListener$1(this);
    private final Lazy<TelephonyListenerManager> telephonyListenerManager;
    private final Lazy<TelephonyManager> telephonyManager;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public TelephonyActivity(Context context, Lazy<TelephonyManager> lazy, Lazy<TelephonyListenerManager> lazy2) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(lazy, "telephonyManager");
        Intrinsics.checkNotNullParameter(lazy2, "telephonyListenerManager");
        this.telephonyManager = lazy;
        this.telephonyListenerManager = lazy2;
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onActivate() {
        this.isCallBlocked = isCallBlocked(Integer.valueOf(this.telephonyManager.get().getCallState()));
        this.telephonyListenerManager.get().addCallStateListener(this.phoneStateListener);
        updateBlocking();
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onDeactivate() {
        this.telephonyListenerManager.get().removeCallStateListener(this.phoneStateListener);
    }

    public final void updateBlocking() {
        setBlocking(this.isCallBlocked);
    }

    public final boolean isCallBlocked(Integer num) {
        return num != null && num.intValue() == 2;
    }
}
