package com.android.wallpaper.picker;

import android.app.WallpaperColors;
import com.android.wallpaper.util.TimeUtils$TimeTicker;
import com.android.wallpaper.widget.LockScreenPreviewer;
import com.android.wallpaper.widget.WallpaperColorsLoader;
/* loaded from: classes.dex */
public final /* synthetic */ class CategoryFragment$$ExternalSyntheticLambda4 implements WallpaperColorsLoader.Callback, TimeUtils$TimeTicker.TimeListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ LockScreenPreviewer f$0;

    public /* synthetic */ CategoryFragment$$ExternalSyntheticLambda4(LockScreenPreviewer lockScreenPreviewer, int i) {
        this.$r8$classId = i;
        this.f$0 = lockScreenPreviewer;
    }

    @Override // com.android.wallpaper.widget.WallpaperColorsLoader.Callback
    public void onLoaded(WallpaperColors wallpaperColors) {
        switch (this.$r8$classId) {
            case 0:
            default:
                this.f$0.setColor(wallpaperColors);
                return;
        }
    }
}
