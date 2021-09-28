package com.android.systemui.keyguard;

import com.android.systemui.keyguard.ScreenLifecycle;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenLifecycle$$ExternalSyntheticLambda3 implements Consumer {
    public static final /* synthetic */ ScreenLifecycle$$ExternalSyntheticLambda3 INSTANCE = new ScreenLifecycle$$ExternalSyntheticLambda3();

    private /* synthetic */ ScreenLifecycle$$ExternalSyntheticLambda3() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((ScreenLifecycle.Observer) obj).onScreenTurningOn();
    }
}
