package com.android.systemui.navigationbar.gestural;

import android.graphics.Region;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
/* loaded from: classes.dex */
public final /* synthetic */ class EdgeBackGestureHandler$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ EdgeBackGestureHandler.AnonymousClass1 f$0;
    public final /* synthetic */ Region f$1;
    public final /* synthetic */ Region f$2;

    public /* synthetic */ EdgeBackGestureHandler$1$$ExternalSyntheticLambda0(EdgeBackGestureHandler.AnonymousClass1 r1, Region region, Region region2) {
        this.f$0 = r1;
        this.f$1 = region;
        this.f$2 = region2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onSystemGestureExclusionChanged$0(this.f$1, this.f$2);
    }
}
