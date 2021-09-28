package com.google.android.gms.internal;

import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class zzgsb {
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgsb)) {
            return false;
        }
        Objects.requireNonNull((zzgsb) obj);
        return Arrays.equals((byte[]) null, (byte[]) null);
    }

    public final int hashCode() {
        return Arrays.hashCode((byte[]) null) + 16337;
    }
}
