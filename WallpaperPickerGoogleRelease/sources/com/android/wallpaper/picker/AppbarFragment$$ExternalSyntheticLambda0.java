package com.android.wallpaper.picker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.android.customization.model.CustomizationManager;
import com.android.customization.model.grid.GridOption;
import com.android.customization.model.grid.GridOptionsManager;
import com.android.customization.model.grid.LauncherGridOptionsProvider;
import com.android.customization.picker.grid.GridFragment;
import com.android.customization.picker.mode.DarkModeSectionView;
import com.android.customization.picker.themedicon.ThemedIconSectionView;
import com.android.systemui.shared.R;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.model.WallpaperSectionController;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.WallpaperRotationRefresher;
import com.android.wallpaper.picker.PreviewFragment;
import com.android.wallpaper.picker.SectionView;
import com.android.wallpaper.picker.TopLevelPickerActivity;
import com.android.wallpaper.util.ActivityUtils;
import com.android.wallpaper.widget.BottomActionBar;
import com.google.android.apps.wallpaper.backdrop.BackdropPreferences;
import com.google.android.apps.wallpaper.backdrop.BackdropWallpaperRotationRefresher;
import com.google.android.apps.wallpaper.backdrop.BackdropWallpaperRotator;
import java.util.Objects;
/* loaded from: classes.dex */
public final /* synthetic */ class AppbarFragment$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId = 9;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ AppbarFragment$$ExternalSyntheticLambda0(Activity activity) {
        this.f$0 = activity;
    }

    public /* synthetic */ AppbarFragment$$ExternalSyntheticLambda0(GridFragment gridFragment) {
        this.f$0 = gridFragment;
    }

    public /* synthetic */ AppbarFragment$$ExternalSyntheticLambda0(DarkModeSectionView darkModeSectionView) {
        this.f$0 = darkModeSectionView;
    }

    public /* synthetic */ AppbarFragment$$ExternalSyntheticLambda0(ThemedIconSectionView themedIconSectionView) {
        this.f$0 = themedIconSectionView;
    }

    public /* synthetic */ AppbarFragment$$ExternalSyntheticLambda0(WallpaperSectionController wallpaperSectionController) {
        this.f$0 = wallpaperSectionController;
    }

    public /* synthetic */ AppbarFragment$$ExternalSyntheticLambda0(AppbarFragment appbarFragment) {
        this.f$0 = appbarFragment;
    }

    public /* synthetic */ AppbarFragment$$ExternalSyntheticLambda0(DownloadablePreviewFragment downloadablePreviewFragment) {
        this.f$0 = downloadablePreviewFragment;
    }

    public /* synthetic */ AppbarFragment$$ExternalSyntheticLambda0(ImagePreviewFragment imagePreviewFragment) {
        this.f$0 = imagePreviewFragment;
    }

    public /* synthetic */ AppbarFragment$$ExternalSyntheticLambda0(PreviewFragment.WallpaperInfoContent wallpaperInfoContent) {
        this.f$0 = wallpaperInfoContent;
    }

    public /* synthetic */ AppbarFragment$$ExternalSyntheticLambda0(TopLevelPickerActivity.AnonymousClass4 r2) {
        this.f$0 = r2;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                ((AppbarFragment) this.f$0).mHost.onUpArrowPressed();
                return;
            case 1:
                GridFragment gridFragment = (GridFragment) this.f$0;
                GridOption gridOption = gridFragment.mSelectedOption;
                gridFragment.mBottomActionBar.disableActions();
                GridOptionsManager gridOptionsManager = gridFragment.mGridManager;
                CustomizationManager.Callback callback = gridFragment.mApplyGridCallback;
                LauncherGridOptionsProvider launcherGridOptionsProvider = gridOptionsManager.mProvider;
                String str = gridOption.name;
                Objects.requireNonNull(launcherGridOptionsProvider);
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", str);
                if (launcherGridOptionsProvider.mContext.getContentResolver().update(launcherGridOptionsProvider.mPreviewUtils.getUri("default_grid"), contentValues, null, null) == 1) {
                    gridOptionsManager.mEventLogger.logGridApplied(gridOption);
                    callback.onSuccess();
                    return;
                }
                callback.onError(null);
                return;
            case 2:
                DarkModeSectionView darkModeSectionView = (DarkModeSectionView) this.f$0;
                boolean z = !darkModeSectionView.mIsDarkModeActivated;
                darkModeSectionView.mIsDarkModeActivated = z;
                SectionView.SectionViewListener sectionViewListener = darkModeSectionView.mSectionViewListener;
                if (sectionViewListener != null) {
                    sectionViewListener.onViewActivated(darkModeSectionView.getContext(), z);
                    return;
                }
                return;
            case 3:
                ((ThemedIconSectionView) this.f$0).mSwitchView.toggle();
                return;
            case 4:
                ((CustomizationPickerFragment) ((WallpaperSectionController) this.f$0).mSectionNavigationController).navigateTo(new CategorySelectorFragment());
                return;
            case 5:
                DownloadablePreviewFragment downloadablePreviewFragment = (DownloadablePreviewFragment) this.f$0;
                int i = DownloadablePreviewFragment.$r8$clinit;
                WallpaperInfo wallpaperInfo = downloadablePreviewFragment.mWallpaper;
                BottomActionBar bottomActionBar = ((PreviewFragment) downloadablePreviewFragment).mBottomActionBar;
                BottomActionBar.BottomAction bottomAction = BottomActionBar.BottomAction.DOWNLOAD;
                bottomActionBar.disableActions(bottomAction);
                ((PreviewFragment) downloadablePreviewFragment).mBottomActionBar.showActions(BottomActionBar.BottomAction.PROGRESS);
                ((PreviewFragment) downloadablePreviewFragment).mBottomActionBar.hideActions(bottomAction);
                downloadablePreviewFragment.requireContext();
                android.app.WallpaperInfo wallpaperComponent = wallpaperInfo.getWallpaperComponent();
                Intent intent = new Intent("com.google.pixel.livewallpaper.action.DOWNLOAD_LIVE_WALLPAPER");
                intent.setComponent(new ComponentName("com.google.pixel.livewallpaper", "com.google.pixel.livewallpaper.split.FeatureActivity"));
                intent.addFlags(805306368);
                intent.putExtra("android.live_wallpaper.info", wallpaperComponent);
                ActivityUtils.startActivityForResultSafely(downloadablePreviewFragment.getActivity(), intent, 4);
                return;
            case 6:
                ((ImagePreviewFragment) this.f$0).onSetWallpaperClicked(view);
                return;
            case 7:
                PreviewFragment.WallpaperInfoContent wallpaperInfoContent = (PreviewFragment.WallpaperInfoContent) this.f$0;
                Context context = PreviewFragment.this.getContext();
                if (context != null) {
                    PreviewFragment previewFragment = PreviewFragment.this;
                    previewFragment.mUserEventLogger.logActionClicked(previewFragment.mWallpaper.getCollectionId(context), PreviewFragment.this.mWallpaper.getActionLabelRes(context));
                    PreviewFragment.this.startActivity(wallpaperInfoContent.mExploreIntent);
                    return;
                }
                return;
            case 8:
                TopLevelPickerActivity topLevelPickerActivity = TopLevelPickerActivity.this;
                int i2 = TopLevelPickerActivity.$r8$clinit;
                Objects.requireNonNull(topLevelPickerActivity);
                ProgressDialog progressDialog = new ProgressDialog(topLevelPickerActivity, R.style.LightDialogTheme);
                topLevelPickerActivity.mRefreshWallpaperProgressDialog = progressDialog;
                progressDialog.setTitle((CharSequence) null);
                topLevelPickerActivity.mRefreshWallpaperProgressDialog.setMessage(topLevelPickerActivity.getResources().getString(R.string.refreshing_daily_wallpaper_dialog_message));
                topLevelPickerActivity.mRefreshWallpaperProgressDialog.setIndeterminate(true);
                topLevelPickerActivity.mRefreshWallpaperProgressDialog.show();
                WallpaperRotationRefresher wallpaperRotationRefresher = InjectorProvider.getInjector().getWallpaperRotationRefresher();
                TopLevelPickerActivity.AnonymousClass5 r0 = new WallpaperRotationRefresher.Listener() { // from class: com.android.wallpaper.picker.TopLevelPickerActivity.5
                };
                BackdropWallpaperRotationRefresher backdropWallpaperRotationRefresher = (BackdropWallpaperRotationRefresher) wallpaperRotationRefresher;
                Objects.requireNonNull(backdropWallpaperRotationRefresher);
                Context applicationContext = topLevelPickerActivity.getApplicationContext();
                BackdropPreferences instance = BackdropPreferences.getInstance(applicationContext);
                InjectorProvider.getInjector().getUserEventLogger(applicationContext).logRefreshDailyWallpaperButtonClicked();
                BackdropWallpaperRotator.updateWallpaper(applicationContext, instance.getCollectionId(), instance.getCollectionName(), instance.getResumeToken(), new BackdropWallpaperRotator.RotationListener(backdropWallpaperRotationRefresher, instance, r0) { // from class: com.google.android.apps.wallpaper.backdrop.BackdropWallpaperRotationRefresher.1
                    public final /* synthetic */ BackdropPreferences val$backdropPreferences;
                    public final /* synthetic */ WallpaperRotationRefresher.Listener val$listener;

                    {
                        this.val$backdropPreferences = r2;
                        this.val$listener = r3;
                    }

                    @Override // com.google.android.apps.wallpaper.backdrop.BackdropWallpaperRotator.RotationListener
                    public void onError() {
                        Log.e("BackdropRefresher", "Fetching and setting the next wallpaper failed.");
                        TopLevelPickerActivity.AnonymousClass5 r2 = (TopLevelPickerActivity.AnonymousClass5) this.val$listener;
                        ProgressDialog progressDialog2 = TopLevelPickerActivity.this.mRefreshWallpaperProgressDialog;
                        if (progressDialog2 != null) {
                            progressDialog2.dismiss();
                        }
                        new AlertDialog.Builder(TopLevelPickerActivity.this, R.style.LightDialogTheme).setMessage(R.string.refresh_daily_wallpaper_failed_message).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).create().show();
                    }

                    @Override // com.google.android.apps.wallpaper.backdrop.BackdropWallpaperRotator.RotationListener
                    public void onSuccess(String str2) {
                        this.val$backdropPreferences.setResumeToken(str2);
                        Log.e("BackdropRefresher", "Fetching and setting the next wallpaper succeeded with resumeToken: " + str2);
                        TopLevelPickerActivity.AnonymousClass5 r2 = (TopLevelPickerActivity.AnonymousClass5) this.val$listener;
                        if (!TopLevelPickerActivity.this.isDestroyed()) {
                            ProgressDialog progressDialog2 = TopLevelPickerActivity.this.mRefreshWallpaperProgressDialog;
                            if (progressDialog2 != null) {
                                progressDialog2.dismiss();
                            }
                            TopLevelPickerActivity.this.refreshCurrentWallpapers(null);
                        }
                    }
                });
                return;
            default:
                int i3 = BottomActionBar.$r8$clinit;
                ((Activity) this.f$0).onBackPressed();
                return;
        }
    }
}
