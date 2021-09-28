package com.android.wm.shell.splitscreen;

import android.os.Bundle;
import android.window.IRemoteTransition;
import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ Bundle f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ Bundle f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ IRemoteTransition f$5;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda2(int i, Bundle bundle, int i2, Bundle bundle2, int i3, IRemoteTransition iRemoteTransition) {
        this.f$0 = i;
        this.f$1 = bundle;
        this.f$2 = i2;
        this.f$3 = bundle2;
        this.f$4 = i3;
        this.f$5 = iRemoteTransition;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        SplitScreenController.ISplitScreenImpl.lambda$startTasks$7(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, (SplitScreenController) obj);
    }
}
