package com.android.systemui;

import android.graphics.Bitmap;
import com.android.systemui.ImageWallpaper;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageWallpaper$GLEngine$$ExternalSyntheticLambda8 implements Consumer {
    public final /* synthetic */ ImageWallpaper.GLEngine f$0;

    public /* synthetic */ ImageWallpaper$GLEngine$$ExternalSyntheticLambda8(ImageWallpaper.GLEngine gLEngine) {
        this.f$0 = gLEngine;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.updateMiniBitmap((Bitmap) obj);
    }
}
