package com.android.customization.module;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.lifecycle.MethodCallsLogger;
import com.android.wallpaper.compat.WallpaperManagerCompatV16;
import com.android.wallpaper.module.BitmapCropper;
import com.android.wallpaper.module.CurrentWallpaperInfoFactory;
import com.android.wallpaper.module.DefaultBitmapCropper;
import com.android.wallpaper.module.DefaultCurrentWallpaperInfoFactory;
import com.android.wallpaper.module.DefaultExploreIntentChecker;
import com.android.wallpaper.module.DefaultNetworkStatusNotifier;
import com.android.wallpaper.module.DefaultPackageStatusNotifier;
import com.android.wallpaper.module.DefaultSystemFeatureChecker;
import com.android.wallpaper.module.DefaultWallpaperPersister;
import com.android.wallpaper.module.DefaultWallpaperRefresher;
import com.android.wallpaper.module.ExploreIntentChecker;
import com.android.wallpaper.module.Injector;
import com.android.wallpaper.module.NetworkStatusNotifier;
import com.android.wallpaper.module.PackageStatusNotifier;
import com.android.wallpaper.module.WallpaperPersister;
import com.android.wallpaper.module.WallpaperRefresher;
import com.android.wallpaper.picker.CustomizationPickerActivity;
import com.android.wallpaper.util.DownloadableUtils;
import com.google.android.gms.internal.zzfit;
/* loaded from: classes.dex */
public class DefaultCustomizationInjector implements CustomizationInjector, Injector {
    public MethodCallsLogger mAlarmManagerWrapper;
    public BitmapCropper mBitmapCropper;
    public CurrentWallpaperInfoFactory mCurrentWallpaperFactory;
    public ExploreIntentChecker mExploreIntentChecker;
    public DownloadableUtils mFormFactorChecker;
    public NetworkStatusNotifier mNetworkStatusNotifier;
    public PackageStatusNotifier mPackageStatusNotifier;
    public zzfit mRequester;
    public DefaultSystemFeatureChecker mSystemFeatureChecker;
    public WallpaperManagerCompatV16 mWallpaperManagerCompat;
    public WallpaperPersister mWallpaperPersister;
    public WallpaperRefresher mWallpaperRefresher;

    @Override // com.android.wallpaper.module.Injector
    public synchronized MethodCallsLogger getAlarmManagerWrapper(Context context) {
        if (this.mAlarmManagerWrapper == null) {
            this.mAlarmManagerWrapper = new MethodCallsLogger(context.getApplicationContext());
        }
        return this.mAlarmManagerWrapper;
    }

    @Override // com.android.wallpaper.module.Injector
    public synchronized BitmapCropper getBitmapCropper() {
        if (this.mBitmapCropper == null) {
            this.mBitmapCropper = new DefaultBitmapCropper();
        }
        return this.mBitmapCropper;
    }

    @Override // com.android.wallpaper.module.Injector
    public synchronized CurrentWallpaperInfoFactory getCurrentWallpaperFactory(Context context) {
        if (this.mCurrentWallpaperFactory == null) {
            this.mCurrentWallpaperFactory = new DefaultCurrentWallpaperInfoFactory(context.getApplicationContext());
        }
        return this.mCurrentWallpaperFactory;
    }

    @Override // com.android.wallpaper.module.Injector
    public Intent getDeepLinkRedirectIntent(Context context, Uri uri) {
        Intent intent = new Intent();
        intent.setClass(context, CustomizationPickerActivity.class);
        intent.setData(uri);
        intent.setFlags(268468224);
        return intent;
    }

    @Override // com.android.wallpaper.module.Injector
    public synchronized ExploreIntentChecker getExploreIntentChecker(Context context) {
        if (this.mExploreIntentChecker == null) {
            this.mExploreIntentChecker = new DefaultExploreIntentChecker(context.getApplicationContext());
        }
        return this.mExploreIntentChecker;
    }

    @Override // com.android.wallpaper.module.Injector
    public synchronized DownloadableUtils getFormFactorChecker(Context context) {
        if (this.mFormFactorChecker == null) {
            this.mFormFactorChecker = new DownloadableUtils(context.getApplicationContext(), 1);
        }
        return this.mFormFactorChecker;
    }

    @Override // com.android.wallpaper.module.Injector
    public synchronized NetworkStatusNotifier getNetworkStatusNotifier(Context context) {
        if (this.mNetworkStatusNotifier == null) {
            this.mNetworkStatusNotifier = new DefaultNetworkStatusNotifier(context.getApplicationContext());
        }
        return this.mNetworkStatusNotifier;
    }

    @Override // com.android.wallpaper.module.Injector
    public synchronized PackageStatusNotifier getPackageStatusNotifier(Context context) {
        if (this.mPackageStatusNotifier == null) {
            this.mPackageStatusNotifier = new DefaultPackageStatusNotifier(context.getApplicationContext());
        }
        return this.mPackageStatusNotifier;
    }

    @Override // com.android.wallpaper.module.Injector
    public synchronized zzfit getRequester(Context context) {
        if (this.mRequester == null) {
            this.mRequester = new zzfit(context.getApplicationContext());
        }
        return this.mRequester;
    }

    @Override // com.android.wallpaper.module.Injector
    public synchronized DefaultSystemFeatureChecker getSystemFeatureChecker() {
        if (this.mSystemFeatureChecker == null) {
            this.mSystemFeatureChecker = new DefaultSystemFeatureChecker();
        }
        return this.mSystemFeatureChecker;
    }

    @Override // com.android.wallpaper.module.Injector
    public synchronized WallpaperManagerCompatV16 getWallpaperManagerCompat(Context context) {
        if (this.mWallpaperManagerCompat == null) {
            this.mWallpaperManagerCompat = WallpaperManagerCompatV16.getInstance(context);
        }
        return this.mWallpaperManagerCompat;
    }

    @Override // com.android.wallpaper.module.Injector
    public synchronized WallpaperPersister getWallpaperPersister(Context context) {
        if (this.mWallpaperPersister == null) {
            this.mWallpaperPersister = new DefaultWallpaperPersister(context.getApplicationContext());
        }
        return this.mWallpaperPersister;
    }

    @Override // com.android.wallpaper.module.Injector
    public synchronized WallpaperRefresher getWallpaperRefresher(Context context) {
        if (this.mWallpaperRefresher == null) {
            this.mWallpaperRefresher = new DefaultWallpaperRefresher(context.getApplicationContext());
        }
        return this.mWallpaperRefresher;
    }
}
