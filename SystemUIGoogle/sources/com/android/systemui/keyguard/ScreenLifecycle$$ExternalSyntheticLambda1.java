package com.android.systemui.keyguard;

import com.android.systemui.keyguard.ScreenLifecycle;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenLifecycle$$ExternalSyntheticLambda1 implements Consumer {
    public static final /* synthetic */ ScreenLifecycle$$ExternalSyntheticLambda1 INSTANCE = new ScreenLifecycle$$ExternalSyntheticLambda1();

    private /* synthetic */ ScreenLifecycle$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((ScreenLifecycle.Observer) obj).onScreenTurnedOn();
    }
}
