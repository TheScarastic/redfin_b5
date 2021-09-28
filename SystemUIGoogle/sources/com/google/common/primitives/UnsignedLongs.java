package com.google.common.primitives;
/* loaded from: classes2.dex */
public final class UnsignedLongs {
    private static long flip(long j) {
        return j ^ Long.MIN_VALUE;
    }

    public static int compare(long j, long j2) {
        return Longs.compare(flip(j), flip(j2));
    }
}
