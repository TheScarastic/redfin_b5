package com.google.android.gms.internal;

import java.io.IOException;
/* loaded from: classes.dex */
public final class zzgsz extends zzgrt<zzgsz> {
    public static volatile zzgsz[] zzc;
    public long zza = 0;
    public zzgsy[] zzb;

    public zzgsz() {
        if (zzgsy.zzc == null) {
            synchronized (zzgrx.zzb) {
                if (zzgsy.zzc == null) {
                    zzgsy.zzc = new zzgsy[0];
                }
            }
        }
        this.zzb = zzgsy.zzc;
        this.zzay = null;
        this.zzaz = -1;
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final int computeSerializedSize() {
        super.computeSerializedSize();
        int i = 0;
        int zzb = this.zza != 0 ? zzgrr.zzb(1) + 8 + 0 : 0;
        zzgsy[] zzgsyArr = this.zzb;
        if (zzgsyArr != null && zzgsyArr.length > 0) {
            while (true) {
                zzgsy[] zzgsyArr2 = this.zzb;
                if (i >= zzgsyArr2.length) {
                    break;
                }
                zzgsy zzgsy = zzgsyArr2[i];
                if (zzgsy != null) {
                    zzb += zzgrr.zzb(3, zzgsy);
                }
                i++;
            }
        }
        return zzb;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgsz)) {
            return false;
        }
        zzgsz zzgsz = (zzgsz) obj;
        if (this.zza != zzgsz.zza || !zzgrx.zza(this.zzb, zzgsz.zzb)) {
            return false;
        }
        zzgrv zzgrv = this.zzay;
        if (zzgrv != null && !zzgrv.zzb()) {
            return this.zzay.equals(zzgsz.zzay);
        }
        zzgrv zzgrv2 = zzgsz.zzay;
        return zzgrv2 == null || zzgrv2.zzb();
    }

    public final int hashCode() {
        long j = this.zza;
        int i = 0;
        int zza = (zzgrx.zza(this.zzb) + ((((-1208406068 + ((int) (j ^ (j >>> 32)))) * 31) + 0) * 31)) * 31;
        zzgrv zzgrv = this.zzay;
        if (zzgrv != null && !zzgrv.zzb()) {
            i = this.zzay.hashCode();
        }
        return zza + i;
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final void writeTo(zzgrr zzgrr) throws IOException {
        long j = this.zza;
        if (j != 0) {
            zzgrr.zzc(9);
            if (zzgrr.zza.remaining() >= 8) {
                zzgrr.zza.putLong(j);
            } else {
                throw new zzgrs(zzgrr.zza.position(), zzgrr.zza.limit());
            }
        }
        zzgsy[] zzgsyArr = this.zzb;
        if (zzgsyArr != null && zzgsyArr.length > 0) {
            int i = 0;
            while (true) {
                zzgsy[] zzgsyArr2 = this.zzb;
                if (i >= zzgsyArr2.length) {
                    break;
                }
                zzgsy zzgsy = zzgsyArr2[i];
                if (zzgsy != null) {
                    zzgrr.zza(3, zzgsy);
                }
                i++;
            }
        }
        super.writeTo(zzgrr);
    }
}
