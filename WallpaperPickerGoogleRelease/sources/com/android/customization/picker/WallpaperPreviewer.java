package com.android.customization.picker;

import android.app.Activity;
import android.app.WallpaperColors;
import android.graphics.Rect;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.cardview.R$color;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.util.ScreenSizeCalculator;
import com.android.wallpaper.util.WallpaperConnection;
import com.android.wallpaper.util.WallpaperSurfaceCallback;
import com.android.wallpaper.widget.PreviewPager$$ExternalSyntheticLambda1;
import java.util.Objects;
/* loaded from: classes.dex */
public class WallpaperPreviewer implements LifecycleObserver {
    public final Activity mActivity;
    public final ImageView mHomePreview;
    public WallpaperInfo mWallpaper;
    public WallpaperColorsListener mWallpaperColorsListener;
    public WallpaperConnection mWallpaperConnection;
    public final SurfaceView mWallpaperSurface;
    public WallpaperSurfaceCallback mWallpaperSurfaceCallback;
    public final Rect mPreviewLocalRect = new Rect();
    public final Rect mPreviewGlobalRect = new Rect();
    public final int[] mLivePreviewLocation = new int[2];

    /* loaded from: classes.dex */
    public interface WallpaperColorsListener {
        void onWallpaperColorsChanged(WallpaperColors wallpaperColors);
    }

    public WallpaperPreviewer(Lifecycle lifecycle, Activity activity, ImageView imageView, SurfaceView surfaceView) {
        lifecycle.addObserver(this);
        this.mActivity = activity;
        this.mHomePreview = imageView;
        this.mWallpaperSurface = surfaceView;
        this.mWallpaperSurfaceCallback = new WallpaperSurfaceCallback(activity, imageView, surfaceView, new PreviewPager$$ExternalSyntheticLambda1(this));
        surfaceView.setZOrderMediaOverlay(true);
        surfaceView.getHolder().addCallback(this.mWallpaperSurfaceCallback);
        final View rootView = imageView.getRootView();
        rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.customization.picker.WallpaperPreviewer.1
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                WallpaperPreviewer wallpaperPreviewer = WallpaperPreviewer.this;
                Objects.requireNonNull(wallpaperPreviewer);
                float screenAspectRatio = ScreenSizeCalculator.getInstance().getScreenAspectRatio(wallpaperPreviewer.mActivity);
                CardView cardView = (CardView) wallpaperPreviewer.mHomePreview.getParent();
                int measuredHeight = (int) (((float) cardView.getMeasuredHeight()) / screenAspectRatio);
                ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                layoutParams.width = measuredHeight;
                cardView.setLayoutParams(layoutParams);
                cardView.setRadius(R$color.getPreviewCornerRadius(wallpaperPreviewer.mActivity, measuredHeight));
                rootView.removeOnLayoutChangeListener(this);
            }
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        WallpaperConnection wallpaperConnection = this.mWallpaperConnection;
        if (wallpaperConnection != null) {
            wallpaperConnection.disconnect();
            this.mWallpaperConnection = null;
        }
        this.mWallpaperSurfaceCallback.cleanUp();
        this.mWallpaperSurface.getHolder().removeCallback(this.mWallpaperSurfaceCallback);
        Surface surface = this.mWallpaperSurface.getHolder().getSurface();
        if (surface != null) {
            surface.release();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        WallpaperConnection wallpaperConnection = this.mWallpaperConnection;
        if (wallpaperConnection != null) {
            wallpaperConnection.mIsVisible = false;
            wallpaperConnection.setEngineVisibility(false);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        WallpaperConnection wallpaperConnection = this.mWallpaperConnection;
        if (wallpaperConnection != null) {
            wallpaperConnection.mIsVisible = true;
            wallpaperConnection.setEngineVisibility(true);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        WallpaperConnection wallpaperConnection = this.mWallpaperConnection;
        if (wallpaperConnection != null) {
            wallpaperConnection.disconnect();
            this.mWallpaperConnection = null;
        }
    }
}
