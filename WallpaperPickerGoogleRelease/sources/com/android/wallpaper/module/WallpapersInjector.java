package com.android.wallpaper.module;

import android.content.Context;
import android.os.Bundle;
import com.android.customization.module.DefaultCustomizationInjector;
import com.android.customization.module.StatsLogUserEventLogger;
import com.android.customization.module.ThemesUserEventLogger;
import com.android.wallpaper.model.CategoryProvider;
import com.android.wallpaper.network.ServerFetcher;
import com.android.wallpaper.picker.individual.IndividualPickerFragment;
import com.android.wallpaper.picker.individual.IndividualPickerUnlockableFragment;
import com.google.android.apps.wallpaper.backdrop.BackdropFetcher;
import com.google.android.apps.wallpaper.backdrop.BackdropWallpaperRotationRefresher;
import com.google.android.apps.wallpaper.module.AlwaysLoggingOptInStatusProvider;
import com.google.android.apps.wallpaper.module.ClearcutUserEventLogger;
import com.google.android.apps.wallpaper.module.CompositeUserEventLogger;
import com.google.android.apps.wallpaper.module.DefaultGoogleWallpaperPreferences;
import com.google.android.apps.wallpaper.module.DeviceConfigFilteringLabelProvider;
import com.google.android.apps.wallpaper.module.GooglePartnerProvider;
import com.google.android.apps.wallpaper.module.GoogleWallpaperPreferences;
import com.google.android.apps.wallpaper.module.WallpaperCategoryProvider;
import java.util.Objects;
/* loaded from: classes.dex */
public class WallpapersInjector extends DefaultCustomizationInjector implements GoogleWallpapersInjector {
    public BackdropFetcher mBackdropFetcher;
    public CategoryProvider mCategoryProvider;
    public CustomizationSections mCustomizationSections;
    public DrawableLayerResolver mDrawableLayerResolver;
    public NoOpUserEventLogger mLiveWallpaperInfoFactory;
    public LoggingOptInStatusProvider mLoggingOptInStatusProvider;
    public GooglePartnerProvider mPartnerProvider;
    public GoogleWallpaperPreferences mPrefs;
    public ThemesUserEventLogger mUserEventLogger;
    public WallpaperRotationRefresher mWallpaperRotationRefresher;

    @Override // com.android.wallpaper.module.Injector
    public synchronized CategoryProvider getCategoryProvider(Context context) {
        if (this.mCategoryProvider == null) {
            this.mCategoryProvider = new WallpaperCategoryProvider(context.getApplicationContext());
        }
        return this.mCategoryProvider;
    }

    @Override // com.android.wallpaper.module.Injector
    public CustomizationSections getCustomizationSections() {
        if (this.mCustomizationSections == null) {
            this.mCustomizationSections = new GoogleCustomizationSections();
        }
        return this.mCustomizationSections;
    }

    @Override // com.android.wallpaper.module.Injector
    public String getDownloadableIntentAction() {
        return "com.google.pixel.livewallpaper.action.DOWNLOAD_LIVE_WALLPAPER";
    }

    @Override // com.android.wallpaper.module.Injector
    public DrawableLayerResolver getDrawableLayerResolver() {
        if (this.mDrawableLayerResolver == null) {
            this.mDrawableLayerResolver = new DeviceColorLayerResolver();
        }
        return this.mDrawableLayerResolver;
    }

    @Override // com.android.wallpaper.module.GoogleWallpapersInjector
    public GoogleWallpaperPreferences getGooglePreferences(Context context) {
        return (GoogleWallpaperPreferences) getPreferences(context);
    }

    @Override // com.android.wallpaper.module.Injector
    public synchronized IndividualPickerFragment getIndividualPickerFragment(String str) {
        IndividualPickerUnlockableFragment individualPickerUnlockableFragment;
        int i = IndividualPickerUnlockableFragment.$r8$clinit;
        Bundle bundle = new Bundle();
        bundle.putString("category_collection_id", str);
        individualPickerUnlockableFragment = new IndividualPickerUnlockableFragment();
        individualPickerUnlockableFragment.setArguments(bundle);
        return individualPickerUnlockableFragment;
    }

    @Override // com.android.wallpaper.module.Injector
    public NoOpUserEventLogger getLiveWallpaperInfoFactory(Context context) {
        if (this.mLiveWallpaperInfoFactory == null) {
            this.mLiveWallpaperInfoFactory = new NoOpUserEventLogger(1);
        }
        return this.mLiveWallpaperInfoFactory;
    }

    @Override // com.android.wallpaper.module.Injector
    public synchronized LoggingOptInStatusProvider getLoggingOptInStatusProvider(Context context) {
        if (this.mLoggingOptInStatusProvider == null) {
            Objects.requireNonNull(getFormFactorChecker(context));
            this.mLoggingOptInStatusProvider = new AlwaysLoggingOptInStatusProvider();
        }
        return this.mLoggingOptInStatusProvider;
    }

    @Override // com.android.wallpaper.module.Injector
    public synchronized PartnerProvider getPartnerProvider(Context context) {
        if (this.mPartnerProvider == null) {
            this.mPartnerProvider = new GooglePartnerProvider(context.getApplicationContext());
        }
        return this.mPartnerProvider;
    }

    @Override // com.android.wallpaper.module.Injector
    public synchronized WallpaperPreferences getPreferences(Context context) {
        if (this.mPrefs == null) {
            this.mPrefs = new DefaultGoogleWallpaperPreferences(context.getApplicationContext());
        }
        return this.mPrefs;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00cd, code lost:
        if (r11 == false) goto L_0x00d0;
     */
    @Override // com.android.wallpaper.module.Injector
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public androidx.fragment.app.Fragment getPreviewFragment(android.content.Context r11, com.android.wallpaper.model.WallpaperInfo r12, int r13, boolean r14, boolean r15) {
        /*
        // Method dump skipped, instructions count: 279
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.module.WallpapersInjector.getPreviewFragment(android.content.Context, com.android.wallpaper.model.WallpaperInfo, int, boolean, boolean):androidx.fragment.app.Fragment");
    }

    @Override // com.android.wallpaper.module.GoogleWallpapersInjector
    public ServerFetcher getServerFetcher(Context context) {
        if (this.mBackdropFetcher == null) {
            this.mBackdropFetcher = new BackdropFetcher(getRequester(context), new DeviceConfigFilteringLabelProvider());
        }
        return this.mBackdropFetcher;
    }

    @Override // com.android.wallpaper.module.Injector
    public UserEventLogger getUserEventLogger(Context context) {
        ThemesUserEventLogger themesUserEventLogger;
        synchronized (this) {
            if (this.mUserEventLogger == null) {
                this.mUserEventLogger = new CompositeUserEventLogger(new StatsLogUserEventLogger(), new ClearcutUserEventLogger(context.getApplicationContext()));
            }
            themesUserEventLogger = this.mUserEventLogger;
        }
        return themesUserEventLogger;
    }

    @Override // com.android.wallpaper.module.Injector
    public synchronized WallpaperRotationRefresher getWallpaperRotationRefresher() {
        if (this.mWallpaperRotationRefresher == null) {
            this.mWallpaperRotationRefresher = new BackdropWallpaperRotationRefresher();
        }
        return this.mWallpaperRotationRefresher;
    }
}
