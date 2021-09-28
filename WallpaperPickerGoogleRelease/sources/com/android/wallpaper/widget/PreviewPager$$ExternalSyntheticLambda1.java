package com.android.wallpaper.widget;

import android.app.UiModeManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import androidx.slice.SliceViewManager;
import androidx.slice.widget.SliceLiveData;
import androidx.viewpager.widget.ViewPager;
import com.android.customization.model.mode.DarkModeSectionController;
import com.android.customization.model.mode.DarkModeSectionController$$ExternalSyntheticLambda1;
import com.android.customization.picker.WallpaperPreviewer;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.module.PackageStatusNotifier;
import com.android.wallpaper.picker.CurrentWallpaperBottomSheetPresenter;
import com.android.wallpaper.picker.ImagePreviewFragment;
import com.android.wallpaper.picker.ImagePreviewFragment$$ExternalSyntheticLambda0;
import com.android.wallpaper.picker.LivePreviewFragment;
import com.android.wallpaper.picker.LivePreviewFragment$$ExternalSyntheticLambda2;
import com.android.wallpaper.picker.PreviewFragment;
import com.android.wallpaper.picker.SectionView;
import com.android.wallpaper.picker.TopLevelPickerActivity;
import com.android.wallpaper.picker.WorkspaceSurfaceHolderCallback;
import com.android.wallpaper.picker.individual.IndividualPickerFragment;
import com.android.wallpaper.picker.individual.IndividualPickerUnlockableFragment;
import com.android.wallpaper.util.FullScreenAnimation;
import com.android.wallpaper.util.PreviewUtils;
import com.android.wallpaper.util.PreviewUtils$$ExternalSyntheticLambda0;
import com.android.wallpaper.util.WallpaperSurfaceCallback;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
/* loaded from: classes.dex */
public final /* synthetic */ class PreviewPager$$ExternalSyntheticLambda1 implements ViewPager.PageTransformer, SliceViewManager.SliceCallback, SectionView.SectionViewListener, WallpaperSurfaceCallback.SurfaceListener, Asset.BitmapReceiver, Asset.DimensionsReceiver, FullScreenAnimation.FullScreenStatusListener, Asset.DrawableLoadedListener, WorkspaceSurfaceHolderCallback.WorkspaceRenderListener, PreviewUtils.WorkspacePreviewCallback, PackageStatusNotifier.Listener {
    public final /* synthetic */ int $r8$classId = 9;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ PreviewPager$$ExternalSyntheticLambda1(SurfaceView surfaceView) {
        this.f$0 = surfaceView;
    }

    public /* synthetic */ PreviewPager$$ExternalSyntheticLambda1(SliceLiveData.SliceLiveDataImpl sliceLiveDataImpl) {
        this.f$0 = sliceLiveDataImpl;
    }

    public /* synthetic */ PreviewPager$$ExternalSyntheticLambda1(DarkModeSectionController darkModeSectionController) {
        this.f$0 = darkModeSectionController;
    }

    public /* synthetic */ PreviewPager$$ExternalSyntheticLambda1(WallpaperPreviewer wallpaperPreviewer) {
        this.f$0 = wallpaperPreviewer;
    }

    public /* synthetic */ PreviewPager$$ExternalSyntheticLambda1(Asset.BitmapReceiver bitmapReceiver) {
        this.f$0 = bitmapReceiver;
    }

    public /* synthetic */ PreviewPager$$ExternalSyntheticLambda1(CurrentWallpaperBottomSheetPresenter.RefreshListener refreshListener) {
        this.f$0 = refreshListener;
    }

    public /* synthetic */ PreviewPager$$ExternalSyntheticLambda1(ImagePreviewFragment.WallpaperSurfaceCallback wallpaperSurfaceCallback) {
        this.f$0 = wallpaperSurfaceCallback;
    }

    public /* synthetic */ PreviewPager$$ExternalSyntheticLambda1(LivePreviewFragment livePreviewFragment) {
        this.f$0 = livePreviewFragment;
    }

    public /* synthetic */ PreviewPager$$ExternalSyntheticLambda1(WorkspaceSurfaceHolderCallback workspaceSurfaceHolderCallback) {
        this.f$0 = workspaceSurfaceHolderCallback;
    }

    public /* synthetic */ PreviewPager$$ExternalSyntheticLambda1(IndividualPickerFragment individualPickerFragment) {
        this.f$0 = individualPickerFragment;
    }

    public /* synthetic */ PreviewPager$$ExternalSyntheticLambda1(IndividualPickerUnlockableFragment individualPickerUnlockableFragment) {
        this.f$0 = individualPickerUnlockableFragment;
    }

    public /* synthetic */ PreviewPager$$ExternalSyntheticLambda1(WallpaperSurfaceCallback wallpaperSurfaceCallback) {
        this.f$0 = wallpaperSurfaceCallback;
    }

    public /* synthetic */ PreviewPager$$ExternalSyntheticLambda1(PreviewPager previewPager) {
        this.f$0 = previewPager;
    }

    @Override // com.android.wallpaper.asset.Asset.BitmapReceiver
    public void onBitmapDecoded(Bitmap bitmap) {
        ((Asset.BitmapReceiver) this.f$0).onBitmapDecoded(bitmap);
    }

    @Override // com.android.wallpaper.asset.Asset.DimensionsReceiver
    public void onDimensionsDecoded(Point point) {
        ImagePreviewFragment.WallpaperSurfaceCallback wallpaperSurfaceCallback = (ImagePreviewFragment.WallpaperSurfaceCallback) this.f$0;
        if (ImagePreviewFragment.this.getActivity() != null) {
            if (point == null) {
                ImagePreviewFragment.this.showLoadWallpaperErrorDialog();
                return;
            }
            BottomActionBar bottomActionBar = ((PreviewFragment) ImagePreviewFragment.this).mBottomActionBar;
            if (bottomActionBar != null) {
                bottomActionBar.enableActions();
            }
            ImagePreviewFragment imagePreviewFragment = ImagePreviewFragment.this;
            imagePreviewFragment.mRawWallpaperSize = point;
            SubsamplingScaleImageView subsamplingScaleImageView = imagePreviewFragment.mFullResImageView;
            if (subsamplingScaleImageView != null) {
                if (SubsamplingScaleImageView.VALID_SCALE_TYPES.contains(3)) {
                    subsamplingScaleImageView.minimumScaleType = 3;
                    if (subsamplingScaleImageView.readySent) {
                        subsamplingScaleImageView.fitToBounds(true);
                        subsamplingScaleImageView.invalidate();
                    }
                    SubsamplingScaleImageView subsamplingScaleImageView2 = imagePreviewFragment.mFullResImageView;
                    Objects.requireNonNull(subsamplingScaleImageView2);
                    if (SubsamplingScaleImageView.VALID_PAN_LIMITS.contains(1)) {
                        subsamplingScaleImageView2.panLimit = 1;
                        if (subsamplingScaleImageView2.readySent) {
                            subsamplingScaleImageView2.fitToBounds(true);
                            subsamplingScaleImageView2.invalidate();
                        }
                        Point point2 = new Point(imagePreviewFragment.mRawWallpaperSize);
                        imagePreviewFragment.mWallpaperAsset.decodeBitmap(point2.x, point2.y, new ImagePreviewFragment$$ExternalSyntheticLambda0(imagePreviewFragment, 1));
                        imagePreviewFragment.mFullResImageView.setOnTouchListener(new LivePreviewFragment$$ExternalSyntheticLambda2(imagePreviewFragment));
                        return;
                    }
                    throw new IllegalArgumentException("Invalid pan limit: 1");
                }
                throw new IllegalArgumentException("Invalid scale type: 3");
            }
        }
    }

    @Override // com.android.wallpaper.asset.Asset.DrawableLoadedListener
    public void onDrawableLoaded() {
        CurrentWallpaperBottomSheetPresenter.RefreshListener refreshListener = (CurrentWallpaperBottomSheetPresenter.RefreshListener) this.f$0;
        if (refreshListener != null) {
            ((TopLevelPickerActivity) ((IndividualPickerFragment) refreshListener).mCurrentWallpaperBottomSheetPresenter).setCurrentWallpapersExpanded(true);
        }
    }

    @Override // com.android.wallpaper.util.FullScreenAnimation.FullScreenStatusListener
    public void onFullScreenStatusChange(boolean z) {
        ((LivePreviewFragment) this.f$0).mLockScreenPreviewer.setDateViewVisibility(!z);
    }

    @Override // com.android.wallpaper.module.PackageStatusNotifier.Listener
    public void onPackageChanged(String str, int i) {
        switch (this.$r8$classId) {
            case 11:
                IndividualPickerFragment individualPickerFragment = (IndividualPickerFragment) this.f$0;
                int i2 = IndividualPickerFragment.$r8$clinit;
                if (i != 3 || individualPickerFragment.mCategory.containsThirdParty(str)) {
                    individualPickerFragment.fetchWallpapers(true);
                    return;
                }
                return;
            case 12:
                IndividualPickerUnlockableFragment individualPickerUnlockableFragment = (IndividualPickerUnlockableFragment) this.f$0;
                int i3 = IndividualPickerUnlockableFragment.$r8$clinit;
                if (i != 3 || individualPickerUnlockableFragment.mCategory.containsThirdParty(str)) {
                    individualPickerUnlockableFragment.fetchWallpapers(true);
                    return;
                }
                return;
            default:
                WallpaperSurfaceCallback wallpaperSurfaceCallback = (WallpaperSurfaceCallback) this.f$0;
                Objects.requireNonNull(wallpaperSurfaceCallback);
                if (i != 3 && !wallpaperSurfaceCallback.mSurfaceCreated && wallpaperSurfaceCallback.mHost != null) {
                    wallpaperSurfaceCallback.setupSurfaceWallpaper(false);
                    return;
                }
                return;
        }
    }

    @Override // com.android.wallpaper.util.WallpaperSurfaceCallback.SurfaceListener
    public void onSurfaceCreated() {
        WallpaperPreviewer wallpaperPreviewer = (WallpaperPreviewer) this.f$0;
        ImageView imageView = wallpaperPreviewer.mWallpaperSurfaceCallback.mHomeImageWallpaper;
        if (wallpaperPreviewer.mWallpaper != null && imageView != null) {
            imageView.post(new PreviewUtils$$ExternalSyntheticLambda0(wallpaperPreviewer, imageView));
        }
    }

    @Override // com.android.wallpaper.picker.SectionView.SectionViewListener
    public void onViewActivated(Context context, boolean z) {
        DarkModeSectionController darkModeSectionController = (DarkModeSectionController) this.f$0;
        ExecutorService executorService = DarkModeSectionController.sExecutorService;
        Objects.requireNonNull(darkModeSectionController);
        if (context != null) {
            if (!((Switch) darkModeSectionController.mDarkModeSectionView.findViewById(R.id.dark_mode_toggle)).isEnabled()) {
                Context context2 = darkModeSectionController.mContext;
                Toast.makeText(context2, context2.getString(R.string.mode_disabled_msg), 0).show();
                return;
            }
            new Handler(Looper.getMainLooper()).postDelayed(new DarkModeSectionController$$ExternalSyntheticLambda1((UiModeManager) context.getSystemService(UiModeManager.class), z), (long) context.getResources().getInteger(17694720));
        }
    }
}
