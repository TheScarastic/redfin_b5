package com.android.wallpaper.picker.individual;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.R$attr;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.model.WallpaperInfo;
import java.util.List;
/* loaded from: classes.dex */
public abstract class IndividualHolder extends RecyclerView.ViewHolder {
    public Activity mActivity;
    public ImageView mThumbnailView;
    public View mTileLayout;
    public TextView mTitleView;
    public WallpaperInfo mWallpaper;

    public IndividualHolder(Activity activity, int i, View view) {
        super(view);
        this.mActivity = activity;
        this.mTileLayout = view.findViewById(R.id.tile);
        this.mThumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
        ImageView imageView = (ImageView) view.findViewById(R.id.overlay_icon);
        this.mTitleView = (TextView) view.findViewById(R.id.title);
        view.findViewById(R.id.wallpaper_container).getLayoutParams().height = i;
    }

    public void bindWallpaper(WallpaperInfo wallpaperInfo) {
        this.mWallpaper = wallpaperInfo;
        String title = wallpaperInfo.getTitle(this.mActivity);
        List<String> attributions = wallpaperInfo.getAttributions(this.mActivity);
        String str = attributions.size() > 0 ? attributions.get(0) : null;
        if (title != null) {
            this.mTitleView.setText(title);
            this.mTitleView.setVisibility(0);
            this.mTileLayout.setContentDescription(title);
        } else if (str != null) {
            this.mTileLayout.setContentDescription(str);
        }
        Asset thumbAsset = wallpaperInfo.getThumbAsset(this.mActivity.getApplicationContext());
        Activity activity = this.mActivity;
        thumbAsset.loadDrawable(activity, this.mThumbnailView, R$attr.getColorAttr(activity, 16844080));
    }
}
