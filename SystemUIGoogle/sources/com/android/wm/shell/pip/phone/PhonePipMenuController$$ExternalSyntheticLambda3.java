package com.android.wm.shell.pip.phone;

import com.android.wm.shell.pip.phone.PhonePipMenuController;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class PhonePipMenuController$$ExternalSyntheticLambda3 implements Consumer {
    public static final /* synthetic */ PhonePipMenuController$$ExternalSyntheticLambda3 INSTANCE = new PhonePipMenuController$$ExternalSyntheticLambda3();

    private /* synthetic */ PhonePipMenuController$$ExternalSyntheticLambda3() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((PhonePipMenuController.Listener) obj).onPipDismiss();
    }
}
