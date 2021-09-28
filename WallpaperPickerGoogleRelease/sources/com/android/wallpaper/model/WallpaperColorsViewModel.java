package com.android.wallpaper.model;

import android.app.WallpaperColors;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import kotlin.Lazy;
import kotlin.LazyKt__LazyKt;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class WallpaperColorsViewModel extends ViewModel {
    @NotNull
    public final Lazy homeWallpaperColors$delegate = LazyKt__LazyKt.lazy(WallpaperColorsViewModel$homeWallpaperColors$2.INSTANCE);
    @NotNull
    public final Lazy lockWallpaperColors$delegate = LazyKt__LazyKt.lazy(WallpaperColorsViewModel$lockWallpaperColors$2.INSTANCE);

    @NotNull
    public final MutableLiveData<WallpaperColors> getHomeWallpaperColors() {
        return (MutableLiveData) this.homeWallpaperColors$delegate.getValue();
    }

    @NotNull
    public final MutableLiveData<WallpaperColors> getLockWallpaperColors() {
        return (MutableLiveData) this.lockWallpaperColors$delegate.getValue();
    }
}
