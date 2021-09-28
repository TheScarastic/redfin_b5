package com.google.common.primitives;

import androidx.recyclerview.widget.RecyclerView;
/* loaded from: classes.dex */
public final class Ints {
    public static int saturatedCast(long j) {
        if (j > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return j < -2147483648L ? RecyclerView.UNDEFINED_DURATION : (int) j;
    }
}
