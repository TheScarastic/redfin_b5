package com.android.wallpaper.model;

import android.content.Context;
import android.os.Parcelable;
/* loaded from: classes.dex */
public interface WallpaperRotationInitializer extends Parcelable {

    /* loaded from: classes.dex */
    public interface Listener {
        void onError();

        void onFirstWallpaperInRotationSet();
    }

    void setFirstWallpaperInRotation(Context context, int i, Listener listener);

    boolean startRotation(Context context);
}
