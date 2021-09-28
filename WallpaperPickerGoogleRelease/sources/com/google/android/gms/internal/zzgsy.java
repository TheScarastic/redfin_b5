package com.google.android.gms.internal;

import java.io.IOException;
/* loaded from: classes.dex */
public final class zzgsy extends zzgrt<zzgsy> {
    public static volatile zzgsy[] zzc;
    public long zza = 0;
    public long zzb = 0;

    public zzgsy() {
        this.zzay = null;
        this.zzaz = -1;
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final int computeSerializedSize() {
        super.computeSerializedSize();
        long j = this.zza;
        int i = 0;
        if (j != 0) {
            i = 0 + zzgrr.zzf(1, j);
        }
        long j2 = this.zzb;
        return j2 != 0 ? i + zzgrr.zzf(2, j2) : i;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgsy)) {
            return false;
        }
        zzgsy zzgsy = (zzgsy) obj;
        if (this.zza != zzgsy.zza || this.zzb != zzgsy.zzb) {
            return false;
        }
        zzgrv zzgrv = this.zzay;
        if (zzgrv != null && !zzgrv.zzb()) {
            return this.zzay.equals(zzgsy.zzay);
        }
        zzgrv zzgrv2 = zzgsy.zzay;
        return zzgrv2 == null || zzgrv2.zzb();
    }

    public final int hashCode() {
        long j = this.zza;
        long j2 = this.zzb;
        int i = (((-1208406099 + ((int) (j ^ (j >>> 32)))) * 31) + ((int) (j2 ^ (j2 >>> 32)))) * 31;
        zzgrv zzgrv = this.zzay;
        return i + ((zzgrv == null || zzgrv.zzb()) ? 0 : this.zzay.hashCode());
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final void writeTo(zzgrr zzgrr) throws IOException {
        long j = this.zza;
        if (j != 0) {
            zzgrr.zzb(1, j);
        }
        long j2 = this.zzb;
        if (j2 != 0) {
            zzgrr.zzb(2, j2);
        }
        super.writeTo(zzgrr);
    }
}
