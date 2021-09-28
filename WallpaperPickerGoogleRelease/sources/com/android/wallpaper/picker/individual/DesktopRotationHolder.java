package com.android.wallpaper.picker.individual;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.shared.R;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.WallpaperPreferences;
import com.android.wallpaper.picker.RotationStarter;
import java.util.Objects;
/* loaded from: classes.dex */
public class DesktopRotationHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SelectableHolder {
    public Activity mActivity;
    public RotationStarter mRotationStarter;
    public SelectionAnimator mSelectionAnimator;
    public ImageView mThumbnailView;
    public View mTile;
    public WallpaperPreferences mWallpaperPreferences;

    public DesktopRotationHolder(Activity activity, int i, View view, SelectionAnimator selectionAnimator, RotationStarter rotationStarter) {
        super(view);
        this.mWallpaperPreferences = InjectorProvider.getInjector().getPreferences(activity);
        this.mActivity = activity;
        this.mTile = view.findViewById(R.id.tile);
        this.mThumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
        this.mTile.setOnClickListener(this);
        this.mTile.getLayoutParams().height = i;
        view.getLayoutParams().height = i;
        this.mSelectionAnimator = selectionAnimator;
        this.mRotationStarter = rotationStarter;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Objects.requireNonNull(this.mSelectionAnimator);
        Objects.requireNonNull(this.mSelectionAnimator);
        this.mRotationStarter.startRotation(0);
    }

    @Override // com.android.wallpaper.picker.individual.SelectableHolder
    public void setSelectionState(int i) {
        if (i == 2) {
            Objects.requireNonNull(this.mSelectionAnimator);
        } else if (i == 0) {
            Objects.requireNonNull(this.mSelectionAnimator);
        } else if (i == 1) {
            Objects.requireNonNull(this.mSelectionAnimator);
        }
    }
}
