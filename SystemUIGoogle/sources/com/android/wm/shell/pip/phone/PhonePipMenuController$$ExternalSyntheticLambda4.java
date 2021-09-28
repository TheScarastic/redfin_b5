package com.android.wm.shell.pip.phone;

import com.android.wm.shell.pip.phone.PhonePipMenuController;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class PhonePipMenuController$$ExternalSyntheticLambda4 implements Consumer {
    public static final /* synthetic */ PhonePipMenuController$$ExternalSyntheticLambda4 INSTANCE = new PhonePipMenuController$$ExternalSyntheticLambda4();

    private /* synthetic */ PhonePipMenuController$$ExternalSyntheticLambda4() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((PhonePipMenuController.Listener) obj).onPipExpand();
    }
}
