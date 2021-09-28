package com.android.wm.shell.splitscreen;

import android.os.Bundle;
import android.os.UserHandle;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda6 implements Consumer {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ Bundle f$4;
    public final /* synthetic */ UserHandle f$5;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda6(String str, String str2, int i, int i2, Bundle bundle, UserHandle userHandle) {
        this.f$0 = str;
        this.f$1 = str2;
        this.f$2 = i;
        this.f$3 = i2;
        this.f$4 = bundle;
        this.f$5 = userHandle;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((SplitScreenController) obj).startShortcut(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
