package com.android.wallpaper.picker;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;
import androidx.fragment.app.BackStackRecord;
import androidx.fragment.app.Fragment;
import com.android.systemui.shared.R;
import com.android.wallpaper.model.DownloadableLiveWallpaperInfo;
import com.android.wallpaper.model.LiveWallpaperInfo;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.module.DefaultWallpaperPersister;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.WallpaperPersister;
import com.android.wallpaper.util.ActivityUtils;
import com.android.wallpaper.util.DiskBasedLogger$$ExternalSyntheticLambda1;
import com.android.wallpaper.widget.BottomActionBar;
import com.google.android.material.tabs.TabLayout;
/* loaded from: classes.dex */
public class DownloadablePreviewFragment extends LivePreviewFragment {
    public static final /* synthetic */ int $r8$clinit = 0;
    public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.wallpaper.picker.DownloadablePreviewFragment.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            BottomActionBar.BottomAction bottomAction = BottomActionBar.BottomAction.PROGRESS;
            BottomActionBar.BottomAction bottomAction2 = BottomActionBar.BottomAction.DOWNLOAD;
            if (TextUtils.equals("com.google.pixel.livewallpaper.action.DOWNLOAD_STATE", intent.getAction())) {
                int intExtra = intent.getIntExtra("com.google.pixel.livewallpaper.download_state", 0);
                if (intExtra == 1) {
                    DownloadablePreviewFragment downloadablePreviewFragment = DownloadablePreviewFragment.this;
                    int i = DownloadablePreviewFragment.$r8$clinit;
                    ((PreviewFragment) downloadablePreviewFragment).mBottomActionBar.hideActions(bottomAction2, bottomAction);
                    ((PreviewFragment) downloadablePreviewFragment).mBottomActionBar.showActions(BottomActionBar.BottomAction.DELETE, BottomActionBar.BottomAction.APPLY);
                    LiveWallpaperInfo liveWallpaperInfo = new LiveWallpaperInfo(downloadablePreviewFragment.mWallpaper.getWallpaperComponent());
                    ((DefaultWallpaperPersister) downloadablePreviewFragment.mWallpaperPersister).mWallpaperInfoInPreview = liveWallpaperInfo;
                    Fragment previewFragment = InjectorProvider.getInjector().getPreviewFragment(downloadablePreviewFragment.mContext, liveWallpaperInfo, 1, ((TabLayout) downloadablePreviewFragment.mView.findViewById(R.id.separated_tabs)).getSelectedTabPosition() == 0, false);
                    BackStackRecord backStackRecord = new BackStackRecord(downloadablePreviewFragment.mFragmentManager);
                    backStackRecord.replace(R.id.fragment_container, previewFragment);
                    backStackRecord.commitAllowingStateLoss();
                } else if (intExtra == 3) {
                    DownloadablePreviewFragment downloadablePreviewFragment2 = DownloadablePreviewFragment.this;
                    int i2 = DownloadablePreviewFragment.$r8$clinit;
                    if (downloadablePreviewFragment2.mWallpaper != null) {
                        ((PreviewFragment) downloadablePreviewFragment2).mBottomActionBar.showActions(bottomAction2);
                    }
                    ((PreviewFragment) downloadablePreviewFragment2).mBottomActionBar.hideActions(bottomAction);
                }
                ((PreviewFragment) DownloadablePreviewFragment.this).mBottomActionBar.deselectAction(bottomAction2);
                ((PreviewFragment) DownloadablePreviewFragment.this).mBottomActionBar.enableActions();
                DownloadablePreviewFragment.this.mIsCancelingDownload = false;
            }
        }
    };
    public Context mContext;
    public boolean mIsCancelingDownload;
    public WallpaperPersister mWallpaperPersister;

    @Override // com.android.wallpaper.picker.AppbarFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter("com.google.pixel.livewallpaper.action.DOWNLOAD_STATE"), "com.google.pixel.livewallpaper.permission.DOWNLOAD_LIVE_WALLPAPER", null);
    }

    @Override // com.android.wallpaper.picker.BottomActionBarFragment
    public boolean onBackPressed() {
        if (this.mIsCancelingDownload) {
            return true;
        }
        WallpaperInfo wallpaperInfo = this.mWallpaper;
        if (!(wallpaperInfo instanceof DownloadableLiveWallpaperInfo)) {
            return false;
        }
        this.mIsCancelingDownload = true;
        android.app.WallpaperInfo wallpaperComponent = wallpaperInfo.getWallpaperComponent();
        Intent intent = new Intent("com.google.pixel.livewallpaper.action.CANCEL_LIVE_WALLPAPER");
        intent.setComponent(new ComponentName("com.google.pixel.livewallpaper", "com.google.pixel.livewallpaper.split.FeatureActivity"));
        intent.addFlags(805306368);
        intent.putExtra("android.live_wallpaper.info", wallpaperComponent);
        ActivityUtils.startActivityForResultSafely(getActivity(), intent, 4);
        return false;
    }

    @Override // com.android.wallpaper.picker.LivePreviewFragment, com.android.wallpaper.picker.PreviewFragment, com.android.wallpaper.picker.AppbarFragment, com.android.wallpaper.picker.BottomActionBarFragment
    public void onBottomActionBarReady(BottomActionBar bottomActionBar) {
        super.onBottomActionBarReady(bottomActionBar);
        BottomActionBar bottomActionBar2 = ((PreviewFragment) this).mBottomActionBar;
        BottomActionBar.BottomAction bottomAction = BottomActionBar.BottomAction.DOWNLOAD;
        bottomActionBar2.showActionsOnly(BottomActionBar.BottomAction.INFORMATION, bottomAction);
        ((PreviewFragment) this).mBottomActionBar.setActionClickListener(bottomAction, new AppbarFragment$$ExternalSyntheticLambda0(this));
        ((PreviewFragment) this).mBottomActionBar.setVisibility(0);
        ((PreviewFragment) this).mBottomActionBar.enableActions();
    }

    @Override // com.android.wallpaper.picker.LivePreviewFragment, com.android.wallpaper.picker.PreviewFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mWallpaperPersister = InjectorProvider.getInjector().getWallpaperPersister(this.mContext);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        this.mCalled = true;
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
    }

    @Override // com.android.wallpaper.picker.LivePreviewFragment
    public void previewLiveWallpaper(ImageView imageView) {
        Handler.getMain().post(new DiskBasedLogger$$ExternalSyntheticLambda1(this));
    }
}
