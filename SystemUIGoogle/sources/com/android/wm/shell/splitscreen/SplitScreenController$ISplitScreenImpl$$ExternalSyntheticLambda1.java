package com.android.wm.shell.splitscreen;

import android.os.Bundle;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ Bundle f$3;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda1(int i, int i2, int i3, Bundle bundle) {
        this.f$0 = i;
        this.f$1 = i2;
        this.f$2 = i3;
        this.f$3 = bundle;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((SplitScreenController) obj).startTask(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
