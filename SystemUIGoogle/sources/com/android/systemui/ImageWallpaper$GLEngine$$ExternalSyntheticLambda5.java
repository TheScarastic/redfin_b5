package com.android.systemui;

import android.view.SurfaceHolder;
import com.android.systemui.ImageWallpaper;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageWallpaper$GLEngine$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ ImageWallpaper.GLEngine f$0;
    public final /* synthetic */ SurfaceHolder f$1;

    public /* synthetic */ ImageWallpaper$GLEngine$$ExternalSyntheticLambda5(ImageWallpaper.GLEngine gLEngine, SurfaceHolder surfaceHolder) {
        this.f$0 = gLEngine;
        this.f$1 = surfaceHolder;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onSurfaceCreated$4(this.f$1);
    }
}
