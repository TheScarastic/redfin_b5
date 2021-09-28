package com.android.systemui;

import com.android.systemui.ImageWallpaper;
import java.util.List;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageWallpaper$GLEngine$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ ImageWallpaper.GLEngine f$0;
    public final /* synthetic */ List f$1;

    public /* synthetic */ ImageWallpaper$GLEngine$$ExternalSyntheticLambda6(ImageWallpaper.GLEngine gLEngine, List list) {
        this.f$0 = gLEngine;
        this.f$1 = list;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$addLocalColorsAreas$2(this.f$1);
    }
}
