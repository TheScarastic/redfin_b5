package com.android.systemui.keyguard;

import com.android.systemui.keyguard.WakefulnessLifecycle;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class WakefulnessLifecycle$$ExternalSyntheticLambda3 implements Consumer {
    public static final /* synthetic */ WakefulnessLifecycle$$ExternalSyntheticLambda3 INSTANCE = new WakefulnessLifecycle$$ExternalSyntheticLambda3();

    private /* synthetic */ WakefulnessLifecycle$$ExternalSyntheticLambda3() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((WakefulnessLifecycle.Observer) obj).onStartedWakingUp();
    }
}
