package com.android.systemui.util.leak;

import android.content.Intent;
import com.android.systemui.util.leak.GarbageMonitor;
/* loaded from: classes2.dex */
public final /* synthetic */ class GarbageMonitor$MemoryTile$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ GarbageMonitor.MemoryTile.AnonymousClass1 f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ GarbageMonitor$MemoryTile$1$$ExternalSyntheticLambda0(GarbageMonitor.MemoryTile.AnonymousClass1 r1, Intent intent) {
        this.f$0 = r1;
        this.f$1 = intent;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$run$0(this.f$1);
    }
}
