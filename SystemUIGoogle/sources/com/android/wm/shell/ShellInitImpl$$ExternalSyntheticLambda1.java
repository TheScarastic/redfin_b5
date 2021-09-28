package com.android.wm.shell;

import com.android.wm.shell.bubbles.BubbleController;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class ShellInitImpl$$ExternalSyntheticLambda1 implements Consumer {
    public static final /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda1 INSTANCE = new ShellInitImpl$$ExternalSyntheticLambda1();

    private /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((BubbleController) obj).initialize();
    }
}
