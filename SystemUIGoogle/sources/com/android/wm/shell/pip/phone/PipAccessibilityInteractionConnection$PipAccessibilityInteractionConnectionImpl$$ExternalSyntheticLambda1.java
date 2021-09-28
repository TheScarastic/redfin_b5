package com.android.wm.shell.pip.phone;

import android.graphics.Region;
import android.view.MagnificationSpec;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection;
/* loaded from: classes2.dex */
public final /* synthetic */ class PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ Region f$3;
    public final /* synthetic */ int f$4;
    public final /* synthetic */ IAccessibilityInteractionConnectionCallback f$5;
    public final /* synthetic */ int f$6;
    public final /* synthetic */ int f$7;
    public final /* synthetic */ long f$8;
    public final /* synthetic */ MagnificationSpec f$9;

    public /* synthetic */ PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda1(PipAccessibilityInteractionConnection.PipAccessibilityInteractionConnectionImpl pipAccessibilityInteractionConnectionImpl, long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec) {
        this.f$0 = pipAccessibilityInteractionConnectionImpl;
        this.f$1 = j;
        this.f$2 = i;
        this.f$3 = region;
        this.f$4 = i2;
        this.f$5 = iAccessibilityInteractionConnectionCallback;
        this.f$6 = i3;
        this.f$7 = i4;
        this.f$8 = j2;
        this.f$9 = magnificationSpec;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$findFocus$3(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9);
    }
}
