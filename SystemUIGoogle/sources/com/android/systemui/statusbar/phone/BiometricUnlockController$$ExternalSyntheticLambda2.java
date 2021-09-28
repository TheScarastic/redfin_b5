package com.android.systemui.statusbar.phone;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class BiometricUnlockController$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ UiEventLogger f$0;

    public /* synthetic */ BiometricUnlockController$$ExternalSyntheticLambda2(UiEventLogger uiEventLogger) {
        this.f$0 = uiEventLogger;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.log((BiometricUnlockController.BiometricUiEvent) obj);
    }
}
