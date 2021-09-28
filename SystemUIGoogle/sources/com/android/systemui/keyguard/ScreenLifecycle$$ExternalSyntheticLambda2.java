package com.android.systemui.keyguard;

import com.android.systemui.keyguard.ScreenLifecycle;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenLifecycle$$ExternalSyntheticLambda2 implements Consumer {
    public static final /* synthetic */ ScreenLifecycle$$ExternalSyntheticLambda2 INSTANCE = new ScreenLifecycle$$ExternalSyntheticLambda2();

    private /* synthetic */ ScreenLifecycle$$ExternalSyntheticLambda2() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((ScreenLifecycle.Observer) obj).onScreenTurningOff();
    }
}
