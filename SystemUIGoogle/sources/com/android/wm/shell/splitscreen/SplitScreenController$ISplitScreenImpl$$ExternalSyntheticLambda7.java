package com.android.wm.shell.splitscreen;

import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda7 implements Consumer {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda7(boolean z) {
        this.f$0 = z;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((SplitScreenController) obj).exitSplitScreenOnHide(this.f$0);
    }
}
