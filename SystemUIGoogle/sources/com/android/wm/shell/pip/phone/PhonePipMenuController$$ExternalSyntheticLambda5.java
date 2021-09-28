package com.android.wm.shell.pip.phone;

import com.android.wm.shell.pip.phone.PhonePipMenuController;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class PhonePipMenuController$$ExternalSyntheticLambda5 implements Consumer {
    public static final /* synthetic */ PhonePipMenuController$$ExternalSyntheticLambda5 INSTANCE = new PhonePipMenuController$$ExternalSyntheticLambda5();

    private /* synthetic */ PhonePipMenuController$$ExternalSyntheticLambda5() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((PhonePipMenuController.Listener) obj).onPipShowMenu();
    }
}
