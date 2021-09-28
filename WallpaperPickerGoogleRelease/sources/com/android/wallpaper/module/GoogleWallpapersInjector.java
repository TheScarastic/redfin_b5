package com.android.wallpaper.module;

import android.content.Context;
import com.android.customization.module.CustomizationInjector;
import com.android.wallpaper.network.ServerFetcher;
import com.google.android.apps.wallpaper.module.GoogleWallpaperPreferences;
/* loaded from: classes.dex */
public interface GoogleWallpapersInjector extends CustomizationInjector {
    GoogleWallpaperPreferences getGooglePreferences(Context context);

    ServerFetcher getServerFetcher(Context context);
}
