package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Objects;
/* loaded from: classes.dex */
public final class zzgsw extends zzgrt<zzgsw> implements Cloneable {
    public static volatile zzgsw[] zza;

    public zzgsw() {
        this.zzay = null;
        this.zzaz = -1;
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final Object clone() throws CloneNotSupportedException {
        try {
            return (zzgsw) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final int computeSerializedSize() {
        super.computeSerializedSize();
        return 0;
    }

    @Override // java.lang.Object
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgsw)) {
            return false;
        }
        zzgsw zzgsw = (zzgsw) obj;
        Objects.requireNonNull(zzgsw);
        Objects.requireNonNull(zzgsw);
        zzgrv zzgrv = this.zzay;
        if (zzgrv != null && !zzgrv.zzb()) {
            return this.zzay.equals(zzgsw.zzay);
        }
        zzgrv zzgrv2 = zzgsw.zzay;
        return zzgrv2 == null || zzgrv2.zzb();
    }

    @Override // java.lang.Object
    public final int hashCode() {
        zzgrv zzgrv = this.zzay;
        return -1637150801 + ((zzgrv == null || zzgrv.zzb()) ? 0 : this.zzay.hashCode());
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final void writeTo(zzgrr zzgrr) throws IOException {
        super.writeTo(zzgrr);
    }
}
