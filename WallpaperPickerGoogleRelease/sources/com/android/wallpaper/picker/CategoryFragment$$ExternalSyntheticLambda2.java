package com.android.wallpaper.picker;

import android.view.View;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.model.WallpaperSectionController;
import com.android.wallpaper.module.UserEventLogger;
/* loaded from: classes.dex */
public final /* synthetic */ class CategoryFragment$$ExternalSyntheticLambda2 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId = 1;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ WallpaperInfo f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ UserEventLogger f$3;

    public /* synthetic */ CategoryFragment$$ExternalSyntheticLambda2(WallpaperSectionController wallpaperSectionController, WallpaperInfo wallpaperInfo, boolean z, UserEventLogger userEventLogger) {
        this.f$0 = wallpaperSectionController;
        this.f$1 = wallpaperInfo;
        this.f$2 = z;
        this.f$3 = userEventLogger;
    }

    public /* synthetic */ CategoryFragment$$ExternalSyntheticLambda2(CategoryFragment categoryFragment, WallpaperInfo wallpaperInfo, boolean z, UserEventLogger userEventLogger) {
        this.f$0 = categoryFragment;
        this.f$1 = wallpaperInfo;
        this.f$2 = z;
        this.f$3 = userEventLogger;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                WallpaperInfo wallpaperInfo = this.f$1;
                boolean z = this.f$2;
                UserEventLogger userEventLogger = this.f$3;
                int i = CategoryFragment.$r8$clinit;
                ((CategoryFragment) this.f$0).getFragmentHost().showViewOnlyPreview(wallpaperInfo, z);
                userEventLogger.logCurrentWallpaperPreviewed();
                return;
            default:
                WallpaperInfo wallpaperInfo2 = this.f$1;
                boolean z2 = this.f$2;
                UserEventLogger userEventLogger2 = this.f$3;
                ((WallpaperSectionController) this.f$0).mWallpaperPreviewNavigator.showViewOnlyPreview(wallpaperInfo2, z2);
                userEventLogger2.logCurrentWallpaperPreviewed();
                return;
        }
    }
}
