package com.android.wallpaper.module;

import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class WallpaperChangedNotifier {
    public static WallpaperChangedNotifier sInstance;
    public static final Object sInstanceLock = new Object();
    public List<Listener> mListeners = new ArrayList();

    /* loaded from: classes.dex */
    public interface Listener {
        void onWallpaperChanged();
    }

    public static WallpaperChangedNotifier getInstance() {
        WallpaperChangedNotifier wallpaperChangedNotifier;
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                sInstance = new WallpaperChangedNotifier();
            }
            wallpaperChangedNotifier = sInstance;
        }
        return wallpaperChangedNotifier;
    }

    public void notifyWallpaperChanged() {
        for (int i = 0; i < this.mListeners.size(); i++) {
            this.mListeners.get(i).onWallpaperChanged();
        }
    }
}
