package com.android.wallpaper.picker;

import android.app.WallpaperColors;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.cardview.R$attr;
import androidx.cardview.R$color;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.FragmentActivity;
import com.android.systemui.shared.R;
import com.android.systemui.shared.system.WallpaperManagerCompat;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.module.BitmapCropper;
import com.android.wallpaper.module.DefaultBitmapCropper;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.WallpaperPersister;
import com.android.wallpaper.picker.PreviewFragment;
import com.android.wallpaper.util.FullScreenAnimation;
import com.android.wallpaper.util.PreviewUtils$$ExternalSyntheticLambda0;
import com.android.wallpaper.util.ScreenSizeCalculator;
import com.android.wallpaper.util.WallpaperCropUtils;
import com.android.wallpaper.widget.BottomActionBar;
import com.android.wallpaper.widget.LockScreenPreviewer;
import com.android.wallpaper.widget.PreviewPager$$ExternalSyntheticLambda1;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.common.math.IntMath;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public class ImagePreviewFragment extends PreviewFragment {
    public static final Executor sExecutor = Executors.newCachedThreadPool();
    public ConstraintLayout mContainer;
    public SubsamplingScaleImageView mFullResImageView;
    public ViewGroup mLockPreviewContainer;
    public LockScreenPreviewer mLockScreenPreviewer;
    public ImageView mLowResImageView;
    public Future<Integer> mPlaceholderColorFuture;
    public Point mRawWallpaperSize;
    public Point mScreenSize;
    public TouchForwardingLayout mTouchForwardingLayout;
    public Asset mWallpaperAsset;
    public SurfaceView mWallpaperSurface;
    public SurfaceView mWorkspaceSurface;
    public WorkspaceSurfaceHolderCallback mWorkspaceSurfaceCallback;
    public final WallpaperSurfaceCallback mWallpaperSurfaceCallback = new WallpaperSurfaceCallback(null);
    public final AtomicInteger mImageScaleChangeCounter = new AtomicInteger(0);
    public final AtomicInteger mRecalculateColorCounter = new AtomicInteger(0);

    /* loaded from: classes.dex */
    public class WallpaperSurfaceCallback implements SurfaceHolder.Callback {
        public SurfaceControlViewHost mHost;
        public Surface mLastSurface;

        public WallpaperSurfaceCallback(AnonymousClass1 r2) {
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            if (this.mLastSurface != surfaceHolder.getSurface()) {
                this.mLastSurface = surfaceHolder.getSurface();
                SubsamplingScaleImageView subsamplingScaleImageView = ImagePreviewFragment.this.mFullResImageView;
                if (subsamplingScaleImageView != null) {
                    subsamplingScaleImageView.reset(true);
                    subsamplingScaleImageView.bitmapPaint = null;
                    subsamplingScaleImageView.tileBgPaint = null;
                }
                Context context = ImagePreviewFragment.this.getContext();
                View inflate = LayoutInflater.from(context).inflate(R.layout.fullscreen_wallpaper_preview, (ViewGroup) null);
                ImagePreviewFragment.this.mFullResImageView = (SubsamplingScaleImageView) inflate.findViewById(R.id.full_res_image);
                ImagePreviewFragment.this.mLowResImageView = (ImageView) inflate.findViewById(R.id.low_res_image);
                ImagePreviewFragment imagePreviewFragment = ImagePreviewFragment.this;
                imagePreviewFragment.mWallpaperAsset.decodeRawDimensions(imagePreviewFragment.getActivity(), new PreviewPager$$ExternalSyntheticLambda1(this));
                float wallpaperZoomOutMaxScale = WallpaperManagerCompat.getWallpaperZoomOutMaxScale(context);
                int width = ImagePreviewFragment.this.mWallpaperSurface.getWidth();
                int i = (int) (((float) width) * wallpaperZoomOutMaxScale);
                int height = ImagePreviewFragment.this.mWallpaperSurface.getHeight();
                int i2 = (int) (((float) height) * wallpaperZoomOutMaxScale);
                int i3 = (width - i) / 2;
                int i4 = (height - i2) / 2;
                if (WallpaperCropUtils.isRtl(context)) {
                    i3 *= -1;
                }
                ViewGroup.LayoutParams layoutParams = ImagePreviewFragment.this.mWallpaperSurface.getLayoutParams();
                layoutParams.width = i;
                layoutParams.height = i2;
                ImagePreviewFragment.this.mWallpaperSurface.setX((float) i3);
                ImagePreviewFragment.this.mWallpaperSurface.setY((float) i4);
                ImagePreviewFragment.this.mWallpaperSurface.setLayoutParams(layoutParams);
                ImagePreviewFragment.this.mWallpaperSurface.requestLayout();
                FragmentActivity requireActivity = ImagePreviewFragment.this.requireActivity();
                int colorAttr = R$attr.getColorAttr(requireActivity, 16842801);
                if (ImagePreviewFragment.this.mPlaceholderColorFuture.isDone()) {
                    try {
                        colorAttr = ImagePreviewFragment.this.mWallpaper.computePlaceholderColor(context).get().intValue();
                    } catch (InterruptedException | ExecutionException unused) {
                    }
                }
                ImagePreviewFragment.this.mWallpaperSurface.setResizeBackgroundColor(colorAttr);
                ImagePreviewFragment.this.mWallpaperSurface.setBackgroundColor(colorAttr);
                ImagePreviewFragment imagePreviewFragment2 = ImagePreviewFragment.this;
                imagePreviewFragment2.mWallpaperAsset.loadLowResDrawable(requireActivity, imagePreviewFragment2.mLowResImageView, colorAttr, imagePreviewFragment2.mPreviewBitmapTransformation);
                inflate.measure(View.MeasureSpec.makeMeasureSpec(i, IntMath.MAX_SIGNED_POWER_OF_TWO), View.MeasureSpec.makeMeasureSpec(i2, IntMath.MAX_SIGNED_POWER_OF_TWO));
                inflate.layout(0, 0, i, i2);
                ImagePreviewFragment imagePreviewFragment3 = ImagePreviewFragment.this;
                imagePreviewFragment3.mTouchForwardingLayout.mView = imagePreviewFragment3.mFullResImageView;
                SurfaceControlViewHost surfaceControlViewHost = this.mHost;
                if (surfaceControlViewHost != null) {
                    surfaceControlViewHost.release();
                    this.mHost = null;
                }
                SurfaceControlViewHost surfaceControlViewHost2 = new SurfaceControlViewHost(context, context.getDisplay(), ImagePreviewFragment.this.mWallpaperSurface.getHostToken());
                this.mHost = surfaceControlViewHost2;
                surfaceControlViewHost2.setView(inflate, inflate.getWidth(), inflate.getHeight());
                ImagePreviewFragment.this.mWallpaperSurface.setChildSurfacePackage(this.mHost.getSurfacePackage());
            }
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        }
    }

    public final Rect calculateCropRect(Context context) {
        float f = this.mFullResImageView.scale;
        Context applicationContext = context.getApplicationContext();
        Rect rect = new Rect();
        SubsamplingScaleImageView subsamplingScaleImageView = this.mFullResImageView;
        if (subsamplingScaleImageView.vTranslate != null && subsamplingScaleImageView.readySent) {
            rect.set(0, 0, subsamplingScaleImageView.getWidth(), subsamplingScaleImageView.getHeight());
            if (subsamplingScaleImageView.vTranslate != null && subsamplingScaleImageView.readySent) {
                rect.set((int) subsamplingScaleImageView.viewToSourceX((float) rect.left), (int) subsamplingScaleImageView.viewToSourceY((float) rect.top), (int) subsamplingScaleImageView.viewToSourceX((float) rect.right), (int) subsamplingScaleImageView.viewToSourceY((float) rect.bottom));
                subsamplingScaleImageView.fileSRect(rect, rect);
                rect.set(Math.max(0, rect.left), Math.max(0, rect.top), Math.min(subsamplingScaleImageView.sWidth, rect.right), Math.min(subsamplingScaleImageView.sHeight, rect.bottom));
            }
        }
        int measuredWidth = this.mWallpaperSurface.getMeasuredWidth();
        int measuredHeight = this.mWallpaperSurface.getMeasuredHeight();
        return WallpaperCropUtils.calculateCropRect(applicationContext, new Point(measuredWidth, measuredHeight), WallpaperCropUtils.calculateCropSurfaceSize(applicationContext.getResources(), Math.max(measuredWidth, measuredHeight), Math.min(measuredWidth, measuredHeight)), this.mRawWallpaperSize, rect, f);
    }

    @Override // com.android.wallpaper.picker.PreviewFragment
    public int getLayoutResId() {
        return R.layout.fragment_image_preview;
    }

    @Override // com.android.wallpaper.picker.PreviewFragment, com.android.wallpaper.picker.AppbarFragment, com.android.wallpaper.picker.BottomActionBarFragment
    public void onBottomActionBarReady(BottomActionBar bottomActionBar) {
        super.onBottomActionBarReady(bottomActionBar);
        BottomActionBar bottomActionBar2 = ((PreviewFragment) this).mBottomActionBar;
        PreviewFragment.WallpaperInfoContent wallpaperInfoContent = new PreviewFragment.WallpaperInfoContent(getContext());
        BottomActionBar.BottomAction bottomAction = BottomActionBar.BottomAction.INFORMATION;
        bottomActionBar2.bindBottomSheetContentWithAction(wallpaperInfoContent, bottomAction);
        BottomActionBar bottomActionBar3 = ((PreviewFragment) this).mBottomActionBar;
        BottomActionBar.BottomAction bottomAction2 = BottomActionBar.BottomAction.APPLY;
        bottomActionBar3.showActionsOnly(bottomAction, BottomActionBar.BottomAction.EDIT, bottomAction2);
        ((PreviewFragment) this).mBottomActionBar.setActionClickListener(bottomAction2, new AppbarFragment$$ExternalSyntheticLambda0(this));
        final View findViewById = this.mView.findViewById(R.id.separated_tabs_container);
        BottomActionBar bottomActionBar4 = ((PreviewFragment) this).mBottomActionBar;
        bottomActionBar4.mAccessibilityCallback = new BottomActionBar.AccessibilityCallback() { // from class: com.android.wallpaper.picker.ImagePreviewFragment.1
            @Override // com.android.wallpaper.widget.BottomActionBar.AccessibilityCallback
            public void onBottomSheetCollapsed() {
                ImagePreviewFragment.this.mContainer.setImportantForAccessibility(1);
                findViewById.setImportantForAccessibility(1);
            }

            @Override // com.android.wallpaper.widget.BottomActionBar.AccessibilityCallback
            public void onBottomSheetExpanded() {
                ImagePreviewFragment.this.mContainer.setImportantForAccessibility(4);
                findViewById.setImportantForAccessibility(4);
            }
        };
        bottomActionBar4.setVisibility(0);
        ((PreviewFragment) this).mBottomActionBar.disableActions();
        if (this.mRawWallpaperSize != null) {
            ((PreviewFragment) this).mBottomActionBar.enableActions();
        }
    }

    @Override // com.android.wallpaper.picker.PreviewFragment, com.android.wallpaper.picker.LoadWallpaperErrorDialogFragment.Listener
    public void onClickOk() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    @Override // com.android.wallpaper.picker.PreviewFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mWallpaperAsset = this.mWallpaper.getAsset(requireContext().getApplicationContext());
        this.mPlaceholderColorFuture = this.mWallpaper.computePlaceholderColor(requireContext());
    }

    @Override // com.android.wallpaper.picker.PreviewFragment, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        FragmentActivity requireActivity = requireActivity();
        this.mScreenSize = ScreenSizeCalculator.getInstance().getScreenSize(requireActivity.getWindowManager().getDefaultDisplay());
        ConstraintLayout constraintLayout = (ConstraintLayout) onCreateView.findViewById(R.id.container);
        this.mContainer = constraintLayout;
        TouchForwardingLayout touchForwardingLayout = (TouchForwardingLayout) constraintLayout.findViewById(R.id.touch_forwarding_layout);
        this.mTouchForwardingLayout = touchForwardingLayout;
        touchForwardingLayout.mForwardingEnabled = true;
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.mContainer);
        constraintSet.get(this.mTouchForwardingLayout.getId()).layout.dimensionRatio = String.format(Locale.US, "%d:%d", Integer.valueOf(this.mScreenSize.x), Integer.valueOf(this.mScreenSize.y));
        constraintSet.applyTo(this.mContainer);
        SurfaceView surfaceView = (SurfaceView) this.mContainer.findViewById(R.id.workspace_surface);
        this.mWorkspaceSurface = surfaceView;
        this.mWorkspaceSurfaceCallback = createWorkspaceSurfaceCallback(surfaceView);
        this.mWallpaperSurface = (SurfaceView) this.mContainer.findViewById(R.id.wallpaper_surface);
        this.mLockPreviewContainer = (ViewGroup) this.mContainer.findViewById(R.id.lock_screen_preview_container);
        this.mWorkspaceSurface.setResizeBackgroundColor(R$attr.getColorAttr(getContext(), 16842801));
        LockScreenPreviewer lockScreenPreviewer = new LockScreenPreviewer(this.mLifecycleRegistry, getContext(), this.mLockPreviewContainer);
        this.mLockScreenPreviewer = lockScreenPreviewer;
        lockScreenPreviewer.setDateViewVisibility(!this.mFullScreenAnimation.mIsFullScreen);
        this.mFullScreenAnimation.mFullScreenStatusListener = new ImagePreviewFragment$$ExternalSyntheticLambda0(this, 0);
        setUpTabs((TabLayout) onCreateView.findViewById(R.id.separated_tabs));
        onCreateView.measure(View.MeasureSpec.makeMeasureSpec(this.mScreenSize.x, IntMath.MAX_SIGNED_POWER_OF_TWO), View.MeasureSpec.makeMeasureSpec(this.mScreenSize.y, IntMath.MAX_SIGNED_POWER_OF_TWO));
        ((CardView) this.mWorkspaceSurface.getParent()).setRadius(R$color.getPreviewCornerRadius(requireActivity, this.mContainer.getMeasuredWidth()));
        this.mWallpaperSurface.getHolder().addCallback(this.mWallpaperSurfaceCallback);
        this.mWorkspaceSurface.setZOrderMediaOverlay(true);
        this.mWorkspaceSurface.getHolder().addCallback(this.mWorkspaceSurfaceCallback);
        Glide.get(requireActivity).setMemoryCategory(MemoryCategory.LOW);
        return onCreateView;
    }

    @Override // com.android.wallpaper.picker.PreviewFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        this.mCalled = true;
        this.mWallpaperSetter.cleanUp();
        SubsamplingScaleImageView subsamplingScaleImageView = this.mFullResImageView;
        if (subsamplingScaleImageView != null) {
            subsamplingScaleImageView.reset(true);
            subsamplingScaleImageView.bitmapPaint = null;
            subsamplingScaleImageView.tileBgPaint = null;
        }
        LockScreenPreviewer lockScreenPreviewer = this.mLockScreenPreviewer;
        if (lockScreenPreviewer != null) {
            lockScreenPreviewer.release();
        }
        WallpaperSurfaceCallback wallpaperSurfaceCallback = this.mWallpaperSurfaceCallback;
        SurfaceControlViewHost surfaceControlViewHost = wallpaperSurfaceCallback.mHost;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.release();
            wallpaperSurfaceCallback.mHost = null;
        }
        this.mWorkspaceSurfaceCallback.cleanUp();
    }

    @Override // com.android.wallpaper.picker.BottomActionBarFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void onWallpaperColorsChanged(WallpaperColors wallpaperColors) {
        ((PreviewFragment) this).mBottomActionBar.enableActionButtonsWithBottomSheet(true);
        this.mLockScreenPreviewer.setColor(wallpaperColors);
        FullScreenAnimation fullScreenAnimation = this.mFullScreenAnimation;
        fullScreenAnimation.mFullScreenTextColor = (wallpaperColors == null || (wallpaperColors.getColorHints() & 1) == 0) ? 3 : 2;
        fullScreenAnimation.animateColor(fullScreenAnimation.mIsFullScreen);
    }

    public final void recalculateColors() {
        Context context = getContext();
        if (context == null) {
            Log.e("ImagePreviewFragment", "Got null context, skip recalculating colors");
            return;
        }
        ((DefaultBitmapCropper) InjectorProvider.getInjector().getBitmapCropper()).cropAndScaleBitmap(this.mWallpaperAsset, this.mFullResImageView.scale, calculateCropRect(context), false, new BitmapCropper.Callback() { // from class: com.android.wallpaper.picker.ImagePreviewFragment.3
            @Override // com.android.wallpaper.module.BitmapCropper.Callback
            public void onBitmapCropped(Bitmap bitmap) {
                ImagePreviewFragment.this.mRecalculateColorCounter.incrementAndGet();
                ImagePreviewFragment.sExecutor.execute(new PreviewUtils$$ExternalSyntheticLambda0(this, bitmap));
            }

            @Override // com.android.wallpaper.module.BitmapCropper.Callback
            public void onError(Throwable th) {
                Log.w("ImagePreviewFragment", "Recalculate colors, crop and scale bitmap failed.", th);
            }
        });
    }

    @Override // com.android.wallpaper.picker.PreviewFragment
    public void setCurrentWallpaper(final int i) {
        this.mWallpaperSetter.setCurrentWallpaper(getActivity(), this.mWallpaper, this.mWallpaperAsset, i, this.mFullResImageView.scale, calculateCropRect(getContext()), new WallpaperPersister.SetWallpaperCallback() { // from class: com.android.wallpaper.picker.ImagePreviewFragment.5
            @Override // com.android.wallpaper.module.WallpaperPersister.SetWallpaperCallback
            public void onError(Throwable th) {
                ImagePreviewFragment.this.showSetWallpaperErrorDialog(i);
            }

            @Override // com.android.wallpaper.module.WallpaperPersister.SetWallpaperCallback
            public void onSuccess(WallpaperInfo wallpaperInfo) {
                ImagePreviewFragment.this.finishActivity(true);
            }
        });
    }

    @Override // com.android.wallpaper.picker.PreviewFragment
    public void updateScreenPreview(boolean z) {
        int i = 0;
        this.mWorkspaceSurface.setVisibility(z ? 0 : 4);
        ViewGroup viewGroup = this.mLockPreviewContainer;
        if (z) {
            i = 4;
        }
        viewGroup.setVisibility(i);
        this.mFullScreenAnimation.mIsHomeSelected = z;
    }
}
