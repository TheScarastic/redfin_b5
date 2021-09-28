package com.android.wm.shell.splitscreen;

import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ int f$0;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda0(int i) {
        this.f$0 = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((SplitScreenController) obj).removeFromSideStage(this.f$0);
    }
}
