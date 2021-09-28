package com.android.wm.shell;

import com.android.wm.shell.apppairs.AppPairsController;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class ShellInitImpl$$ExternalSyntheticLambda0 implements Consumer {
    public static final /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda0 INSTANCE = new ShellInitImpl$$ExternalSyntheticLambda0();

    private /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((AppPairsController) obj).onOrganizerRegistered();
    }
}
