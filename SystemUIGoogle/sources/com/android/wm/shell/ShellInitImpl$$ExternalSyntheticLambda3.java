package com.android.wm.shell;

import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class ShellInitImpl$$ExternalSyntheticLambda3 implements Consumer {
    public static final /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda3 INSTANCE = new ShellInitImpl$$ExternalSyntheticLambda3();

    private /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda3() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((SplitScreenController) obj).onOrganizerRegistered();
    }
}
