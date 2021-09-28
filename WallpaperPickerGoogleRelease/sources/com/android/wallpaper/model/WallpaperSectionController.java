package com.android.wallpaper.model;

import android.animation.Animator;
import android.app.Activity;
import android.app.WallpaperColors;
import android.app.WallpaperInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.R$attr;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.core.widget.ContentLoadingProgressBar$$ExternalSyntheticLambda0;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.BitmapCachingAsset;
import com.android.wallpaper.model.CustomizationSectionController;
import com.android.wallpaper.module.DefaultCurrentWallpaperInfoFactory;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.UserEventLogger;
import com.android.wallpaper.picker.AppbarFragment$$ExternalSyntheticLambda0;
import com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda2;
import com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda3;
import com.android.wallpaper.picker.WallpaperSectionView;
import com.android.wallpaper.picker.WorkspaceSurfaceHolderCallback;
import com.android.wallpaper.util.DiskBasedLogger$$ExternalSyntheticLambda1;
import com.android.wallpaper.util.WallpaperConnection;
import com.android.wallpaper.util.WallpaperSurfaceCallback;
import com.android.wallpaper.widget.LockScreenPreviewer;
/* loaded from: classes.dex */
public class WallpaperSectionController implements CustomizationSectionController<WallpaperSectionView>, LifecycleObserver {
    public final Activity mActivity;
    public final Context mAppContext;
    public CardView mHomePreviewCard;
    public ContentLoadingProgressBar mHomePreviewProgress;
    public WallpaperInfo mHomePreviewWallpaperInfo;
    public SurfaceView mHomeWallpaperSurface;
    public WallpaperSurfaceCallback mHomeWallpaperSurfaceCallback;
    public final LifecycleOwner mLifecycleOwner;
    public ViewGroup mLockPreviewContainer;
    public WallpaperInfo mLockPreviewWallpaperInfo;
    public LockScreenPreviewer mLockScreenPreviewer;
    public SurfaceView mLockWallpaperSurface;
    public WallpaperSurfaceCallback mLockWallpaperSurfaceCallback;
    public CardView mLockscreenPreviewCard;
    public ContentLoadingProgressBar mLockscreenPreviewProgress;
    public final PermissionRequester mPermissionRequester;
    public final Bundle mSavedInstanceState;
    public final CustomizationSectionController.CustomizationSectionNavigationController mSectionNavigationController;
    public final WallpaperColorsViewModel mWallpaperColorsViewModel;
    public WallpaperConnection mWallpaperConnection;
    public final WallpaperPreviewNavigator mWallpaperPreviewNavigator;
    public SurfaceView mWorkspaceSurface;
    public WorkspaceSurfaceHolderCallback mWorkspaceSurfaceCallback;
    public final WorkspaceViewModel mWorkspaceViewModel;

    public WallpaperSectionController(Activity activity, LifecycleOwner lifecycleOwner, PermissionRequester permissionRequester, WallpaperColorsViewModel wallpaperColorsViewModel, WorkspaceViewModel workspaceViewModel, CustomizationSectionController.CustomizationSectionNavigationController customizationSectionNavigationController, WallpaperPreviewNavigator wallpaperPreviewNavigator, Bundle bundle) {
        this.mActivity = activity;
        this.mLifecycleOwner = lifecycleOwner;
        this.mPermissionRequester = permissionRequester;
        this.mAppContext = activity.getApplicationContext();
        this.mWallpaperColorsViewModel = wallpaperColorsViewModel;
        this.mWorkspaceViewModel = workspaceViewModel;
        this.mSectionNavigationController = customizationSectionNavigationController;
        this.mWallpaperPreviewNavigator = wallpaperPreviewNavigator;
        this.mSavedInstanceState = bundle;
    }

    public static void access$300(WallpaperSectionController wallpaperSectionController, int i) {
        SurfaceView surfaceView = wallpaperSectionController.mHomeWallpaperSurface;
        if (surfaceView != null) {
            surfaceView.setVisibility(i);
        }
        SurfaceView surfaceView2 = wallpaperSectionController.mLockWallpaperSurface;
        if (surfaceView2 != null) {
            surfaceView2.setVisibility(i);
        }
        SurfaceView surfaceView3 = wallpaperSectionController.mWorkspaceSurface;
        if (surfaceView3 != null) {
            surfaceView3.setVisibility(i);
        }
        ViewGroup viewGroup = wallpaperSectionController.mLockPreviewContainer;
        if (viewGroup != null) {
            viewGroup.setVisibility(i);
        }
    }

    /* Return type fixed from 'com.android.wallpaper.picker.SectionView' to match base method */
    @Override // com.android.wallpaper.model.CustomizationSectionController
    public WallpaperSectionView createView(Context context) {
        WallpaperSectionView wallpaperSectionView = (WallpaperSectionView) LayoutInflater.from(context).inflate(R.layout.wallpaper_section_view, (ViewGroup) null);
        CardView cardView = (CardView) wallpaperSectionView.findViewById(R.id.home_preview);
        this.mHomePreviewCard = cardView;
        cardView.setContentDescription(this.mAppContext.getString(R.string.wallpaper_preview_card_content_description));
        this.mWorkspaceSurface = (SurfaceView) this.mHomePreviewCard.findViewById(R.id.workspace_surface);
        this.mHomePreviewProgress = (ContentLoadingProgressBar) this.mHomePreviewCard.findViewById(R.id.wallpaper_preview_spinner);
        this.mWorkspaceSurfaceCallback = new WorkspaceSurfaceHolderCallback(this.mWorkspaceSurface, this.mAppContext, false);
        SurfaceView surfaceView = (SurfaceView) this.mHomePreviewCard.findViewById(R.id.wallpaper_surface);
        this.mHomeWallpaperSurface = surfaceView;
        this.mHomeWallpaperSurfaceCallback = new WallpaperSurfaceCallback(this.mActivity, this.mHomePreviewCard, surfaceView, new WallpaperSectionController$$ExternalSyntheticLambda0(this, 1));
        CardView cardView2 = (CardView) wallpaperSectionView.findViewById(R.id.lock_preview);
        this.mLockscreenPreviewCard = cardView2;
        cardView2.setContentDescription(this.mAppContext.getString(R.string.lockscreen_wallpaper_preview_card_content_description));
        this.mLockscreenPreviewProgress = (ContentLoadingProgressBar) this.mLockscreenPreviewCard.findViewById(R.id.wallpaper_preview_spinner);
        this.mLockscreenPreviewCard.findViewById(R.id.workspace_surface).setVisibility(8);
        SurfaceView surfaceView2 = (SurfaceView) this.mLockscreenPreviewCard.findViewById(R.id.wallpaper_surface);
        this.mLockWallpaperSurface = surfaceView2;
        this.mLockWallpaperSurfaceCallback = new WallpaperSurfaceCallback(this.mActivity, this.mLockscreenPreviewCard, surfaceView2, new WallpaperSectionController$$ExternalSyntheticLambda0(this, 2));
        ViewGroup viewGroup = (ViewGroup) this.mLockscreenPreviewCard.findViewById(R.id.lock_screen_preview_container);
        this.mLockPreviewContainer = viewGroup;
        viewGroup.setVisibility(4);
        this.mLockScreenPreviewer = new LockScreenPreviewer(this.mLifecycleOwner.getLifecycle(), context, this.mLockPreviewContainer);
        if (isPermissionGranted(this.mAppContext, "android.permission.READ_WALLPAPER_INTERNAL") || isPermissionGranted(this.mAppContext, "android.permission.READ_EXTERNAL_STORAGE")) {
            showCurrentWallpaper(wallpaperSectionView, true);
        } else {
            showCurrentWallpaper(wallpaperSectionView, false);
            ((Button) wallpaperSectionView.findViewById(R.id.permission_needed_allow_access_button)).setOnClickListener(new CategoryFragment$$ExternalSyntheticLambda3(this, wallpaperSectionView));
            Resources resources = this.mAppContext.getResources();
            ((TextView) wallpaperSectionView.findViewById(R.id.permission_needed_explanation)).setText(resources.getString(R.string.permission_needed_explanation, resources.getString(R.string.app_name)));
        }
        int integer = this.mAppContext.getResources().getInteger(17694720);
        setupFade(this.mHomePreviewCard, this.mHomePreviewProgress, integer, true);
        setupFade(this.mLockscreenPreviewCard, this.mLockscreenPreviewProgress, integer, true);
        this.mLifecycleOwner.getLifecycle().addObserver(this);
        this.mHomeWallpaperSurface.getHolder().addCallback(this.mHomeWallpaperSurfaceCallback);
        this.mHomeWallpaperSurface.setZOrderMediaOverlay(true);
        this.mLockWallpaperSurface.getHolder().addCallback(this.mLockWallpaperSurfaceCallback);
        this.mLockWallpaperSurface.setZOrderMediaOverlay(true);
        this.mWorkspaceSurface.setZOrderMediaOverlay(true);
        this.mWorkspaceSurface.getHolder().addCallback(this.mWorkspaceSurfaceCallback);
        wallpaperSectionView.findViewById(R.id.wallpaper_picker_entry).setOnClickListener(new AppbarFragment$$ExternalSyntheticLambda0(this));
        ((MutableLiveData) this.mWorkspaceViewModel.updateWorkspace$delegate.getValue()).observe(this.mLifecycleOwner, new WallpaperSectionController$$ExternalSyntheticLambda0(this, 3));
        return wallpaperSectionView;
    }

    public final boolean isActivityAlive() {
        return !this.mActivity.isDestroyed() && !this.mActivity.isFinishing();
    }

    @Override // com.android.wallpaper.model.CustomizationSectionController
    public boolean isAvailable(Context context) {
        return true;
    }

    public final boolean isPermissionGranted(Context context, String str) {
        return context.getPackageManager().checkPermission(str, context.getPackageName()) == 0;
    }

    public final Asset maybeLoadThumbnail(WallpaperInfo wallpaperInfo, WallpaperSurfaceCallback wallpaperSurfaceCallback) {
        ImageView imageView = wallpaperSurfaceCallback.mHomeImageWallpaper;
        Context context = this.mAppContext;
        BitmapCachingAsset bitmapCachingAsset = new BitmapCachingAsset(context, wallpaperInfo.getThumbAsset(context));
        if (imageView != null && imageView.getDrawable() == null) {
            Activity activity = this.mActivity;
            bitmapCachingAsset.mOriginalAsset.loadPreviewImage(activity, imageView, R$attr.getColorAttr(activity, 16844080));
        }
        return bitmapCachingAsset;
    }

    public final void onHomeWallpaperColorsChanged(WallpaperColors wallpaperColors) {
        if (wallpaperColors == null || !wallpaperColors.equals(this.mWallpaperColorsViewModel.getHomeWallpaperColors().getValue())) {
            this.mWallpaperColorsViewModel.getHomeWallpaperColors().setValue(wallpaperColors);
        }
    }

    public final void onLockWallpaperColorsChanged(WallpaperColors wallpaperColors) {
        if (wallpaperColors == null || !wallpaperColors.equals(this.mWallpaperColorsViewModel.getLockWallpaperColors().getValue())) {
            this.mWallpaperColorsViewModel.getLockWallpaperColors().setValue(wallpaperColors);
            LockScreenPreviewer lockScreenPreviewer = this.mLockScreenPreviewer;
            if (lockScreenPreviewer != null) {
                lockScreenPreviewer.setColor(wallpaperColors);
            }
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
        ((DefaultCurrentWallpaperInfoFactory) InjectorProvider.getInjector().getCurrentWallpaperFactory(this.mAppContext)).createCurrentWallpaperInfos(new WallpaperSectionController$$ExternalSyntheticLambda0(this, 0), this.mSavedInstanceState == null);
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

    @Override // com.android.wallpaper.model.CustomizationSectionController
    public void onTransitionOut() {
        SurfaceView surfaceView = this.mHomeWallpaperSurface;
        if (surfaceView != null) {
            surfaceView.setUseAlpha();
            this.mHomeWallpaperSurface.setAlpha(0.0f);
        }
        SurfaceView surfaceView2 = this.mLockWallpaperSurface;
        if (surfaceView2 != null) {
            surfaceView2.setUseAlpha();
            this.mLockWallpaperSurface.setAlpha(0.0f);
        }
        SurfaceView surfaceView3 = this.mWorkspaceSurface;
        if (surfaceView3 != null) {
            surfaceView3.setUseAlpha();
            this.mWorkspaceSurface.setAlpha(0.0f);
        }
        ViewGroup viewGroup = this.mLockPreviewContainer;
        if (viewGroup != null) {
            viewGroup.setAlpha(0.0f);
        }
    }

    @Override // com.android.wallpaper.model.CustomizationSectionController
    public void release() {
        LockScreenPreviewer lockScreenPreviewer = this.mLockScreenPreviewer;
        if (lockScreenPreviewer != null) {
            lockScreenPreviewer.release();
            this.mLockScreenPreviewer = null;
        }
        WallpaperSurfaceCallback wallpaperSurfaceCallback = this.mHomeWallpaperSurfaceCallback;
        if (wallpaperSurfaceCallback != null) {
            wallpaperSurfaceCallback.cleanUp();
        }
        WallpaperSurfaceCallback wallpaperSurfaceCallback2 = this.mLockWallpaperSurfaceCallback;
        if (wallpaperSurfaceCallback2 != null) {
            wallpaperSurfaceCallback2.cleanUp();
        }
        WorkspaceSurfaceHolderCallback workspaceSurfaceHolderCallback = this.mWorkspaceSurfaceCallback;
        if (workspaceSurfaceHolderCallback != null) {
            workspaceSurfaceHolderCallback.cleanUp();
        }
        LifecycleRegistry lifecycleRegistry = (LifecycleRegistry) this.mLifecycleOwner.getLifecycle();
        lifecycleRegistry.enforceMainThreadIfNeeded("removeObserver");
        lifecycleRegistry.mObserverMap.remove(this);
    }

    public final void setupFade(CardView cardView, final ContentLoadingProgressBar contentLoadingProgressBar, int i, boolean z) {
        float f = 0.0f;
        cardView.setAlpha(z ? 0.0f : 1.0f);
        long j = (long) i;
        cardView.animate().alpha(z ? 1.0f : 0.0f).setDuration(j).setListener(new Animator.AnimatorListener() { // from class: com.android.wallpaper.model.WallpaperSectionController.3
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                contentLoadingProgressBar.hide();
                WallpaperSectionController.access$300(WallpaperSectionController.this, 0);
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                contentLoadingProgressBar.hide();
                WallpaperSectionController.access$300(WallpaperSectionController.this, 0);
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                WallpaperSectionController.access$300(WallpaperSectionController.this, 4);
            }
        });
        ViewPropertyAnimator animate = contentLoadingProgressBar.animate();
        if (z) {
            f = 1.0f;
        }
        animate.alpha(f).setDuration((long) (i * 2)).setStartDelay(j).withStartAction(new ContentLoadingProgressBar$$ExternalSyntheticLambda0(contentLoadingProgressBar, 4)).withEndAction(new ContentLoadingProgressBar$$ExternalSyntheticLambda0(contentLoadingProgressBar, 5));
    }

    public final void showCurrentWallpaper(View view, boolean z) {
        int i = 0;
        view.findViewById(R.id.home_preview).setVisibility(z ? 0 : 8);
        view.findViewById(R.id.lock_preview).setVisibility(z ? 0 : 8);
        View findViewById = view.findViewById(R.id.permission_needed);
        if (z) {
            i = 8;
        }
        findViewById.setVisibility(i);
    }

    public final void updatePreview(WallpaperInfo wallpaperInfo, boolean z) {
        if (wallpaperInfo != null && isActivityAlive()) {
            UserEventLogger userEventLogger = InjectorProvider.getInjector().getUserEventLogger(this.mAppContext);
            maybeLoadThumbnail(wallpaperInfo, z ? this.mHomeWallpaperSurfaceCallback : this.mLockWallpaperSurfaceCallback);
            if (z) {
                WallpaperConnection wallpaperConnection = this.mWallpaperConnection;
                SurfaceView surfaceView = null;
                if (wallpaperConnection != null) {
                    wallpaperConnection.disconnect();
                    this.mWallpaperConnection = null;
                }
                if ((wallpaperInfo instanceof LiveWallpaperInfo) && isActivityAlive() && WallpaperConnection.isPreviewAvailable()) {
                    final boolean z2 = this.mLockPreviewWallpaperInfo instanceof LiveWallpaperInfo;
                    WallpaperInfo wallpaperComponent = wallpaperInfo.getWallpaperComponent();
                    Intent className = new Intent("android.service.wallpaper.WallpaperService").setClassName(wallpaperComponent.getPackageName(), wallpaperComponent.getServiceName());
                    Activity activity = this.mActivity;
                    AnonymousClass2 r6 = new WallpaperConnection.WallpaperConnectionListener() { // from class: com.android.wallpaper.model.WallpaperSectionController.2
                        @Override // com.android.wallpaper.util.WallpaperConnection.WallpaperConnectionListener
                        public void onWallpaperColorsChanged(WallpaperColors wallpaperColors, int i) {
                            LockScreenPreviewer lockScreenPreviewer;
                            if (z2 && (lockScreenPreviewer = WallpaperSectionController.this.mLockScreenPreviewer) != null) {
                                lockScreenPreviewer.setColor(wallpaperColors);
                                WallpaperSectionController.this.onLockWallpaperColorsChanged(wallpaperColors);
                            }
                            WallpaperSectionController.this.onHomeWallpaperColorsChanged(wallpaperColors);
                        }
                    };
                    SurfaceView surfaceView2 = this.mHomeWallpaperSurface;
                    if (z2) {
                        surfaceView = this.mLockWallpaperSurface;
                    }
                    WallpaperConnection wallpaperConnection2 = new WallpaperConnection(className, activity, r6, surfaceView2, surfaceView);
                    this.mWallpaperConnection = wallpaperConnection2;
                    wallpaperConnection2.mIsVisible = true;
                    wallpaperConnection2.setEngineVisibility(true);
                    this.mHomeWallpaperSurface.post(new DiskBasedLogger$$ExternalSyntheticLambda1(this));
                }
            }
            (z ? this.mHomePreviewCard : this.mLockscreenPreviewCard).setOnClickListener(new CategoryFragment$$ExternalSyntheticLambda2(this, wallpaperInfo, z, userEventLogger));
        }
    }
}
