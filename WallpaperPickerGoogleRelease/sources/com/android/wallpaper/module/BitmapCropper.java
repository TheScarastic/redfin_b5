package com.android.wallpaper.module;

import android.graphics.Bitmap;
/* loaded from: classes.dex */
public interface BitmapCropper {

    /* loaded from: classes.dex */
    public interface Callback {
        void onBitmapCropped(Bitmap bitmap);

        void onError(Throwable th);
    }
}
