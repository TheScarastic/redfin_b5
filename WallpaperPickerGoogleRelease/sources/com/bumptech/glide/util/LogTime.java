package com.bumptech.glide.util;

import android.os.SystemClock;
/* loaded from: classes.dex */
public final class LogTime {
    public static final /* synthetic */ int $r8$clinit = 0;
    public static final double MILLIS_MULTIPLIER = 1.0d / Math.pow(10.0d, 6.0d);

    public static double getElapsedMillis(long j) {
        return ((double) (SystemClock.elapsedRealtimeNanos() - j)) * MILLIS_MULTIPLIER;
    }
}
