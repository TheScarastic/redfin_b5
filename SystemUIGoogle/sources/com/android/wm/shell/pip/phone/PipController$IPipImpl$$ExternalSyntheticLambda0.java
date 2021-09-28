package com.android.wm.shell.pip.phone;

import android.content.ComponentName;
import android.graphics.Rect;
import android.view.SurfaceControl;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class PipController$IPipImpl$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ ComponentName f$0;
    public final /* synthetic */ Rect f$1;
    public final /* synthetic */ SurfaceControl f$2;

    public /* synthetic */ PipController$IPipImpl$$ExternalSyntheticLambda0(ComponentName componentName, Rect rect, SurfaceControl surfaceControl) {
        this.f$0 = componentName;
        this.f$1 = rect;
        this.f$2 = surfaceControl;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        PipController.access$2300((PipController) obj, this.f$0, this.f$1, this.f$2);
    }
}
