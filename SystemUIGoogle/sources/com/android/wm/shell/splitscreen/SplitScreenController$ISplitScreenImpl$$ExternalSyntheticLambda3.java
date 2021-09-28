package com.android.wm.shell.splitscreen;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ PendingIntent f$0;
    public final /* synthetic */ Intent f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ Bundle f$4;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda3(PendingIntent pendingIntent, Intent intent, int i, int i2, Bundle bundle) {
        this.f$0 = pendingIntent;
        this.f$1 = intent;
        this.f$2 = i;
        this.f$3 = i2;
        this.f$4 = bundle;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((SplitScreenController) obj).startIntent(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
