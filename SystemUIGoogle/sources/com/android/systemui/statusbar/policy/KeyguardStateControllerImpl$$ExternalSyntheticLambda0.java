package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardStateControllerImpl$$ExternalSyntheticLambda0 implements Consumer {
    public static final /* synthetic */ KeyguardStateControllerImpl$$ExternalSyntheticLambda0 INSTANCE = new KeyguardStateControllerImpl$$ExternalSyntheticLambda0();

    private /* synthetic */ KeyguardStateControllerImpl$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((KeyguardStateController.Callback) obj).onKeyguardDismissAmountChanged();
    }
}
