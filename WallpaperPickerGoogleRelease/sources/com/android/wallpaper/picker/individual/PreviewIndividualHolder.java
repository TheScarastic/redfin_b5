package com.android.wallpaper.picker.individual;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import com.android.wallpaper.model.InlinePreviewIntentFactory;
import com.android.wallpaper.model.LiveWallpaperInfo;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.module.DefaultWallpaperPersister;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.WallpaperPersister;
import com.android.wallpaper.picker.PreviewActivity;
/* loaded from: classes.dex */
public class PreviewIndividualHolder extends IndividualHolder implements View.OnClickListener {
    public InlinePreviewIntentFactory mPreviewIntentFactory = new PreviewActivity.PreviewActivityIntentFactory();
    public WallpaperPersister mWallpaperPersister;

    public PreviewIndividualHolder(Activity activity, int i, View view) {
        super(activity, i, view);
        this.mTileLayout.setOnClickListener(this);
        this.mWallpaperPersister = InjectorProvider.getInjector().getWallpaperPersister(activity);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.mActivity.isFinishing()) {
            Log.w("PreviewIndividualHolder", "onClick received on VH on finishing Activity");
            return;
        }
        InjectorProvider.getInjector().getUserEventLogger(this.mActivity).logIndividualWallpaperSelected(this.mWallpaper.getCollectionId(this.mActivity));
        WallpaperInfo wallpaperInfo = this.mWallpaper;
        ((DefaultWallpaperPersister) this.mWallpaperPersister).mWallpaperInfoInPreview = wallpaperInfo;
        wallpaperInfo.showPreview(this.mActivity, this.mPreviewIntentFactory, wallpaperInfo instanceof LiveWallpaperInfo ? 4 : 1);
    }
}
