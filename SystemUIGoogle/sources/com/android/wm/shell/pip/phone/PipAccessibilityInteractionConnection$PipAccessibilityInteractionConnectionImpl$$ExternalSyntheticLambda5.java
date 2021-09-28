package com.android.wm.shell.pip.phone;

import android.graphics.Region;
import android.view.MagnificationSpec;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection;
/* loaded from: classes2.dex */
public final /* synthetic */ class PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ Region f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ IAccessibilityInteractionConnectionCallback f$5;
    public final /* synthetic */ int f$6;
    public final /* synthetic */ int f$7;
    public final /* synthetic */ long f$8;
    public final /* synthetic */ MagnificationSpec f$9;

    public /* synthetic */ PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda5(PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl pipAccessibilityInteractionConnectionImpl, long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec) {
        this.f$0 = pipAccessibilityInteractionConnectionImpl;
        this.f$1 = j;
        this.f$2 = str;
        this.f$3 = region;
        this.f$4 = i;
        this.f$5 = iAccessibilityInteractionConnectionCallback;
        this.f$6 = i2;
        this.f$7 = i3;
        this.f$8 = j2;
        this.f$9 = magnificationSpec;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$findAccessibilityNodeInfosByViewId$1(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9);
    }
}
