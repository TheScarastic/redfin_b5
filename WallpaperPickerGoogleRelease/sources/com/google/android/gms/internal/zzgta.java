package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;
/* loaded from: classes.dex */
public final class zzgta extends zzgrt<zzgta> {
    public long zza = 0;
    public zzgsz[] zzb;
    public byte[] zzc;
    public String zze;
    public String zzf;

    public zzgta() {
        if (zzgsz.zzc == null) {
            synchronized (zzgrx.zzb) {
                if (zzgsz.zzc == null) {
                    zzgsz.zzc = new zzgsz[0];
                }
            }
        }
        this.zzb = zzgsz.zzc;
        this.zzc = zzgsc.zzh;
        this.zze = "";
        this.zzf = "";
        this.zzay = null;
        this.zzaz = -1;
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final int computeSerializedSize() {
        super.computeSerializedSize();
        long j = this.zza;
        int i = 0;
        int zzf = j != 0 ? zzgrr.zzf(1, j) + 0 : 0;
        zzgsz[] zzgszArr = this.zzb;
        if (zzgszArr != null && zzgszArr.length > 0) {
            while (true) {
                zzgsz[] zzgszArr2 = this.zzb;
                if (i >= zzgszArr2.length) {
                    break;
                }
                zzgsz zzgsz = zzgszArr2[i];
                if (zzgsz != null) {
                    zzf += zzgrr.zzb(2, zzgsz);
                }
                i++;
            }
        }
        if (!Arrays.equals(this.zzc, zzgsc.zzh)) {
            zzf += zzgrr.zzb(3, this.zzc);
        }
        String str = this.zze;
        if (str != null && !str.equals("")) {
            zzf += zzgrr.zzb(5, this.zze);
        }
        String str2 = this.zzf;
        return (str2 == null || str2.equals("")) ? zzf : zzf + zzgrr.zzb(6, this.zzf);
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgta)) {
            return false;
        }
        zzgta zzgta = (zzgta) obj;
        if (this.zza != zzgta.zza || !zzgrx.zza(this.zzb, zzgta.zzb) || !Arrays.equals(this.zzc, zzgta.zzc)) {
            return false;
        }
        String str = this.zze;
        if (str == null) {
            if (zzgta.zze != null) {
                return false;
            }
        } else if (!str.equals(zzgta.zze)) {
            return false;
        }
        String str2 = this.zzf;
        if (str2 == null) {
            if (zzgta.zzf != null) {
                return false;
            }
        } else if (!str2.equals(zzgta.zzf)) {
            return false;
        }
        zzgrv zzgrv = this.zzay;
        if (zzgrv != null && !zzgrv.zzb()) {
            return this.zzay.equals(zzgta.zzay);
        }
        zzgrv zzgrv2 = zzgta.zzay;
        return zzgrv2 == null || zzgrv2.zzb();
    }

    public final int hashCode() {
        long j = this.zza;
        int hashCode = (Arrays.hashCode(this.zzc) + ((zzgrx.zza(this.zzb) + ((((-1208405882 + ((int) (j ^ (j >>> 32)))) * 31) + ((int) 0)) * 31)) * 31)) * 31;
        String str = this.zze;
        int i = 0;
        int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
        String str2 = this.zzf;
        int hashCode3 = (hashCode2 + (str2 == null ? 0 : str2.hashCode())) * 31;
        zzgrv zzgrv = this.zzay;
        if (zzgrv != null && !zzgrv.zzb()) {
            i = this.zzay.hashCode();
        }
        return hashCode3 + i;
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final void writeTo(zzgrr zzgrr) throws IOException {
        long j = this.zza;
        if (j != 0) {
            zzgrr.zzb(1, j);
        }
        zzgsz[] zzgszArr = this.zzb;
        if (zzgszArr != null && zzgszArr.length > 0) {
            int i = 0;
            while (true) {
                zzgsz[] zzgszArr2 = this.zzb;
                if (i >= zzgszArr2.length) {
                    break;
                }
                zzgsz zzgsz = zzgszArr2[i];
                if (zzgsz != null) {
                    zzgrr.zza(2, zzgsz);
                }
                i++;
            }
        }
        if (!Arrays.equals(this.zzc, zzgsc.zzh)) {
            zzgrr.zza(3, this.zzc);
        }
        String str = this.zze;
        if (str != null && !str.equals("")) {
            zzgrr.zza(5, this.zze);
        }
        String str2 = this.zzf;
        if (str2 != null && !str2.equals("")) {
            zzgrr.zza(6, this.zzf);
        }
        super.writeTo(zzgrr);
    }
}
