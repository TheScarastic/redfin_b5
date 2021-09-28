package com.android.wallpaper.util;

import android.content.res.Resources;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
/* loaded from: classes.dex */
public class DisplayMetricsRetriever {
    public static DisplayMetricsRetriever sInstance;
    public Object mLandscapeDisplayMetrics;
    public Object mPortraitDisplayMetrics;

    public static DisplayMetricsRetriever getInstance() {
        if (sInstance == null) {
            sInstance = new DisplayMetricsRetriever();
        }
        return sInstance;
    }

    public DisplayMetrics getDisplayMetrics(Resources resources, Display display) {
        int i = resources.getConfiguration().orientation;
        if (i == 1) {
            return getPortraitDisplayMetrics(display);
        }
        if (i != 2) {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Unknown device orientation: ");
            m.append(resources.getConfiguration().orientation);
            Log.e("DisplayMetricsRetriever", m.toString());
            return getPortraitDisplayMetrics(display);
        }
        if (((DisplayMetrics) this.mLandscapeDisplayMetrics) == null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.mLandscapeDisplayMetrics = displayMetrics;
            display.getMetrics(displayMetrics);
        }
        return (DisplayMetrics) this.mLandscapeDisplayMetrics;
    }

    public DisplayMetrics getPortraitDisplayMetrics(Display display) {
        if (((DisplayMetrics) this.mPortraitDisplayMetrics) == null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.mPortraitDisplayMetrics = displayMetrics;
            display.getMetrics(displayMetrics);
        }
        return (DisplayMetrics) this.mPortraitDisplayMetrics;
    }
}
