package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardStateControllerImpl$$ExternalSyntheticLambda1 implements Consumer {
    public static final /* synthetic */ KeyguardStateControllerImpl$$ExternalSyntheticLambda1 INSTANCE = new KeyguardStateControllerImpl$$ExternalSyntheticLambda1();

    private /* synthetic */ KeyguardStateControllerImpl$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((KeyguardStateController.Callback) obj).onKeyguardShowingChanged();
    }
}
