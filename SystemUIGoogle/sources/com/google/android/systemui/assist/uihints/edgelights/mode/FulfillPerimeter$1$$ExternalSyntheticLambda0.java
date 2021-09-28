package com.google.android.systemui.assist.uihints.edgelights.mode;

import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import com.google.android.systemui.assist.uihints.edgelights.mode.FulfillPerimeter;
/* loaded from: classes2.dex */
public final /* synthetic */ class FulfillPerimeter$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ FulfillPerimeter.AnonymousClass1 f$0;
    public final /* synthetic */ EdgeLightsView f$1;

    public /* synthetic */ FulfillPerimeter$1$$ExternalSyntheticLambda0(FulfillPerimeter.AnonymousClass1 r1, EdgeLightsView edgeLightsView) {
        this.f$0 = r1;
        this.f$1 = edgeLightsView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onAnimationEnd$0(this.f$1);
    }
}
