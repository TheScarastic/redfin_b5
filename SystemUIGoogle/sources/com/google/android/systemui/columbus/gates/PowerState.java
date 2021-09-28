package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.PowerManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import dagger.Lazy;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PowerState.kt */
/* loaded from: classes2.dex */
public class PowerState extends Gate {
    private final PowerManager powerManager;
    private final Lazy<WakefulnessLifecycle> wakefulnessLifecycle;
    private final PowerState$wakefulnessLifecycleObserver$1 wakefulnessLifecycleObserver = new PowerState$wakefulnessLifecycleObserver$1(this);

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public PowerState(Context context, Lazy<WakefulnessLifecycle> lazy) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(lazy, "wakefulnessLifecycle");
        this.wakefulnessLifecycle = lazy;
        this.powerManager = (PowerManager) context.getSystemService("power");
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onActivate() {
        this.wakefulnessLifecycle.get().addObserver(this.wakefulnessLifecycleObserver);
        updateBlocking();
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onDeactivate() {
        this.wakefulnessLifecycle.get().removeObserver(this.wakefulnessLifecycleObserver);
    }

    /* access modifiers changed from: private */
    public final void updateBlocking() {
        PowerManager powerManager = this.powerManager;
        setBlocking(Intrinsics.areEqual(powerManager == null ? null : Boolean.valueOf(powerManager.isInteractive()), Boolean.FALSE));
    }
}
