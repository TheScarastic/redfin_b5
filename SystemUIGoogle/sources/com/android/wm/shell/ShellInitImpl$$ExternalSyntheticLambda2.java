package com.android.wm.shell;

import com.android.wm.shell.pip.phone.PipTouchHandler;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class ShellInitImpl$$ExternalSyntheticLambda2 implements Consumer {
    public static final /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda2 INSTANCE = new ShellInitImpl$$ExternalSyntheticLambda2();

    private /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda2() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((PipTouchHandler) obj).init();
    }
}
