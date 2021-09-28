package com.android.wallpaper.picker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.widget.ImageView;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.CurrentWallpaperAssetVN;
import com.android.wallpaper.util.DiskBasedLogger$$ExternalSyntheticLambda1;
import com.android.wallpaper.util.FullScreenAnimation;
import com.android.wallpaper.util.WallpaperCropUtils;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final /* synthetic */ class ImagePreviewFragment$$ExternalSyntheticLambda0 implements FullScreenAnimation.FullScreenStatusListener, Asset.BitmapReceiver {
    public final /* synthetic */ ImagePreviewFragment f$0;

    public /* synthetic */ ImagePreviewFragment$$ExternalSyntheticLambda0(ImagePreviewFragment imagePreviewFragment, int i) {
        this.f$0 = imagePreviewFragment;
    }

    @Override // com.android.wallpaper.asset.Asset.BitmapReceiver
    public void onBitmapDecoded(Bitmap bitmap) {
        ImagePreviewFragment imagePreviewFragment = this.f$0;
        Executor executor = ImagePreviewFragment.sExecutor;
        if (imagePreviewFragment.getActivity() != null) {
            if (bitmap == null) {
                imagePreviewFragment.showLoadWallpaperErrorDialog();
                return;
            }
            imagePreviewFragment.mWallpaperSurface.setBackgroundColor(0);
            SubsamplingScaleImageView subsamplingScaleImageView = imagePreviewFragment.mFullResImageView;
            if (subsamplingScaleImageView != null) {
                subsamplingScaleImageView.setImage(new ImageSource(bitmap, false));
                boolean z = imagePreviewFragment.mWallpaperAsset instanceof CurrentWallpaperAssetVN;
                Point point = new Point(imagePreviewFragment.mWallpaperSurface.getMeasuredWidth(), imagePreviewFragment.mWallpaperSurface.getMeasuredHeight());
                Rect calculateVisibleRect = WallpaperCropUtils.calculateVisibleRect(imagePreviewFragment.mRawWallpaperSize, point);
                if (z) {
                    if (WallpaperCropUtils.isRtl(imagePreviewFragment.requireContext())) {
                        calculateVisibleRect.offsetTo(imagePreviewFragment.mRawWallpaperSize.x - calculateVisibleRect.width(), calculateVisibleRect.top);
                    } else {
                        calculateVisibleRect.offsetTo(0, calculateVisibleRect.top);
                    }
                }
                PointF pointF = new PointF((float) calculateVisibleRect.centerX(), (float) calculateVisibleRect.centerY());
                float calculateMinZoom = WallpaperCropUtils.calculateMinZoom(new Point(calculateVisibleRect.width(), calculateVisibleRect.height()), point);
                imagePreviewFragment.mFullResImageView.maxScale = Math.max(8.0f, calculateMinZoom);
                SubsamplingScaleImageView subsamplingScaleImageView2 = imagePreviewFragment.mFullResImageView;
                subsamplingScaleImageView2.minScale = calculateMinZoom;
                subsamplingScaleImageView2.anim = null;
                subsamplingScaleImageView2.pendingScale = Float.valueOf(calculateMinZoom);
                subsamplingScaleImageView2.sPendingCenter = pointF;
                subsamplingScaleImageView2.sRequestedCenter = pointF;
                subsamplingScaleImageView2.invalidate();
                long integer = (long) imagePreviewFragment.getResources().getInteger(17694720);
                imagePreviewFragment.mFullResImageView.setAlpha(0.0f);
                imagePreviewFragment.mFullResImageView.animate().alpha(1.0f).setInterpolator(PreviewFragment.ALPHA_OUT).setDuration(integer).setListener(new AnimatorListenerAdapter() { // from class: com.android.wallpaper.picker.ImagePreviewFragment.4
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        ImageView imageView = ImagePreviewFragment.this.mLowResImageView;
                        if (imageView != null) {
                            imageView.setImageBitmap(null);
                        }
                    }
                });
                SubsamplingScaleImageView subsamplingScaleImageView3 = imagePreviewFragment.mFullResImageView;
                subsamplingScaleImageView3.onStateChangedListener = new SubsamplingScaleImageView.DefaultOnStateChangedListener() { // from class: com.android.wallpaper.picker.ImagePreviewFragment.2
                };
                subsamplingScaleImageView3.post(new DiskBasedLogger$$ExternalSyntheticLambda1(imagePreviewFragment));
            }
        }
    }

    @Override // com.android.wallpaper.util.FullScreenAnimation.FullScreenStatusListener
    public void onFullScreenStatusChange(boolean z) {
        this.f$0.mLockScreenPreviewer.setDateViewVisibility(!z);
    }
}
