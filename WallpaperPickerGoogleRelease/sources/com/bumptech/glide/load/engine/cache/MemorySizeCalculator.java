package com.bumptech.glide.load.engine.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import androidx.preference.R$string$$ExternalSyntheticOutline0;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.android.systemui.shared.system.QuickStepContract;
/* loaded from: classes.dex */
public final class MemorySizeCalculator {
    public static final int BYTES_PER_ARGB_8888_PIXEL = 4;
    public final int arrayPoolSize;
    public final int bitmapPoolSize;
    public final Context context;
    public final int memoryCacheSize;

    /* loaded from: classes.dex */
    public static final class Builder {
        public static final int MEMORY_CACHE_TARGET_SCREENS = 2;
        public ActivityManager activityManager;
        public float bitmapPoolScreens;
        public final Context context;
        public float memoryCacheScreens = 2.0f;
        public ScreenDimensions screenDimensions;

        public Builder(Context context) {
            this.bitmapPoolScreens = (float) 1;
            this.context = context;
            this.activityManager = (ActivityManager) context.getSystemService("activity");
            this.screenDimensions = new DisplayMetricsScreenDimensions(context.getResources().getDisplayMetrics());
            if (this.activityManager.isLowRamDevice()) {
                this.bitmapPoolScreens = 0.0f;
            }
        }

        public Builder setActivityManager(ActivityManager activityManager) {
            this.activityManager = activityManager;
            return this;
        }

        public Builder setScreenDimensions(ScreenDimensions screenDimensions) {
            this.screenDimensions = screenDimensions;
            return this;
        }
    }

    /* loaded from: classes.dex */
    public static final class DisplayMetricsScreenDimensions implements ScreenDimensions {
        public final DisplayMetrics displayMetrics;

        public DisplayMetricsScreenDimensions(DisplayMetrics displayMetrics) {
            this.displayMetrics = displayMetrics;
        }
    }

    /* loaded from: classes.dex */
    public interface ScreenDimensions {
    }

    public MemorySizeCalculator(Builder builder) {
        this.context = builder.context;
        int i = builder.activityManager.isLowRamDevice() ? 2097152 : 4194304;
        this.arrayPoolSize = i;
        ActivityManager activityManager = builder.activityManager;
        int round = Math.round(((float) (activityManager.getMemoryClass() * QuickStepContract.SYSUI_STATE_SEARCH_DISABLED * QuickStepContract.SYSUI_STATE_SEARCH_DISABLED)) * (activityManager.isLowRamDevice() ? 0.33f : 0.4f));
        DisplayMetrics displayMetrics = ((DisplayMetricsScreenDimensions) builder.screenDimensions).displayMetrics;
        float f = (float) (displayMetrics.widthPixels * displayMetrics.heightPixels * 4);
        int round2 = Math.round(builder.bitmapPoolScreens * f);
        int round3 = Math.round(f * builder.memoryCacheScreens);
        int i2 = round - i;
        int i3 = round3 + round2;
        if (i3 <= i2) {
            this.memoryCacheSize = round3;
            this.bitmapPoolSize = round2;
        } else {
            float f2 = (float) i2;
            float f3 = builder.bitmapPoolScreens;
            float f4 = builder.memoryCacheScreens;
            float f5 = f2 / (f3 + f4);
            this.memoryCacheSize = Math.round(f4 * f5);
            this.bitmapPoolSize = Math.round(f5 * builder.bitmapPoolScreens);
        }
        if (Log.isLoggable("MemorySizeCalculator", 3)) {
            String mb = toMb(this.memoryCacheSize);
            String mb2 = toMb(this.bitmapPoolSize);
            String mb3 = toMb(i);
            boolean z = i3 > round;
            String mb4 = toMb(round);
            int memoryClass = builder.activityManager.getMemoryClass();
            boolean isLowRamDevice = builder.activityManager.isLowRamDevice();
            StringBuilder m = R$string$$ExternalSyntheticOutline0.m(XMPPathFactory$$ExternalSyntheticOutline0.m(mb4, XMPPathFactory$$ExternalSyntheticOutline0.m(mb3, XMPPathFactory$$ExternalSyntheticOutline0.m(mb2, XMPPathFactory$$ExternalSyntheticOutline0.m(mb, 177)))), "Calculation complete, Calculated memory cache size: ", mb, ", pool size: ", mb2);
            m.append(", byte array size: ");
            m.append(mb3);
            m.append(", memory class limited? ");
            m.append(z);
            m.append(", max size: ");
            m.append(mb4);
            m.append(", memoryClass: ");
            m.append(memoryClass);
            m.append(", isLowMemoryDevice: ");
            m.append(isLowRamDevice);
            Log.d("MemorySizeCalculator", m.toString());
        }
    }

    public final String toMb(int i) {
        return Formatter.formatFileSize(this.context, (long) i);
    }
}
