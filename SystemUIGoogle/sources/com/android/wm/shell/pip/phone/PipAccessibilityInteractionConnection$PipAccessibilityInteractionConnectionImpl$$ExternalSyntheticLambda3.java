package com.android.wm.shell.pip.phone;

import android.graphics.Region;
import android.os.Bundle;
import android.view.MagnificationSpec;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection;
/* loaded from: classes2.dex */
public final /* synthetic */ class PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ Region f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ IAccessibilityInteractionConnectionCallback f$4;
    public final /* synthetic */ int f$5;
    public final /* synthetic */ int f$6;
    public final /* synthetic */ long f$7;
    public final /* synthetic */ MagnificationSpec f$8;
    public final /* synthetic */ Bundle f$9;

    public /* synthetic */ PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda3(PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl pipAccessibilityInteractionConnectionImpl, long j, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec, Bundle bundle) {
        this.f$0 = pipAccessibilityInteractionConnectionImpl;
        this.f$1 = j;
        this.f$2 = region;
        this.f$3 = i;
        this.f$4 = iAccessibilityInteractionConnectionCallback;
        this.f$5 = i2;
        this.f$6 = i3;
        this.f$7 = j2;
        this.f$8 = magnificationSpec;
        this.f$9 = bundle;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$findAccessibilityNodeInfoByAccessibilityId$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9);
    }
}
