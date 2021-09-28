package com.android.systemui.media;

import java.util.Objects;
/* loaded from: classes.dex */
public final class SmallHash {
    public static int hash(String str) {
        return hash(Objects.hashCode(str));
    }

    public static int hash(int i) {
        return Math.abs(Math.floorMod(i, 8192));
    }
}
