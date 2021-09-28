package com.android.systemui.keyguard;

import com.android.systemui.keyguard.ScreenLifecycle;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenLifecycle$$ExternalSyntheticLambda0 implements Consumer {
    public static final /* synthetic */ ScreenLifecycle$$ExternalSyntheticLambda0 INSTANCE = new ScreenLifecycle$$ExternalSyntheticLambda0();

    private /* synthetic */ ScreenLifecycle$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((ScreenLifecycle.Observer) obj).onScreenTurnedOff();
    }
}
