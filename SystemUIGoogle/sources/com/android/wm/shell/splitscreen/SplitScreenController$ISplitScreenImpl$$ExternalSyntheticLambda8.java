package com.android.wm.shell.splitscreen;

import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda8 implements Consumer {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda8(boolean z) {
        this.f$0 = z;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((SplitScreenController) obj).setSideStageVisibility(this.f$0);
    }
}
