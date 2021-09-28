package com.android.systemui;

import android.os.SystemClock;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageWallpaper$GLEngine$$ExternalSyntheticLambda9 implements Supplier {
    public static final /* synthetic */ ImageWallpaper$GLEngine$$ExternalSyntheticLambda9 INSTANCE = new ImageWallpaper$GLEngine$$ExternalSyntheticLambda9();

    private /* synthetic */ ImageWallpaper$GLEngine$$ExternalSyntheticLambda9() {
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        return Long.valueOf(SystemClock.elapsedRealtime());
    }
}
