package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;
/* loaded from: classes.dex */
public final class zzgsv extends zzgrt<zzgsv> implements Cloneable {
    public long zza = 0;
    public long zzb = 0;
    public byte[] zzf;
    public long zzg;
    public byte[] zzh;
    public zzgsw[] zzk;
    public byte[] zzl;
    public String zzn;
    public String zzo;
    public zzgst zzp;
    public String zzq;
    public zzgsu zzr;
    public String zzs;
    public int[] zzu;
    public zzgsx zzw;

    public zzgsv() {
        if (zzgsw.zza == null) {
            synchronized (zzgrx.zzb) {
                if (zzgsw.zza == null) {
                    zzgsw.zza = new zzgsw[0];
                }
            }
        }
        this.zzk = zzgsw.zza;
        byte[] bArr = zzgsc.zzh;
        this.zzl = bArr;
        this.zzf = bArr;
        this.zzn = "";
        this.zzo = "";
        this.zzp = null;
        this.zzq = "";
        this.zzg = 180000;
        this.zzr = null;
        this.zzh = bArr;
        this.zzs = "";
        this.zzu = zzgsc.zza;
        this.zzw = null;
        this.zzay = null;
        this.zzaz = -1;
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final Object clone() throws CloneNotSupportedException {
        try {
            zzgsv zzgsv = (zzgsv) super.clone();
            zzgsw[] zzgswArr = this.zzk;
            if (zzgswArr != null && zzgswArr.length > 0) {
                zzgsv.zzk = new zzgsw[zzgswArr.length];
                int i = 0;
                while (true) {
                    zzgsw[] zzgswArr2 = this.zzk;
                    if (i >= zzgswArr2.length) {
                        break;
                    }
                    if (zzgswArr2[i] != null) {
                        zzgsv.zzk[i] = (zzgsw) zzgswArr2[i].clone();
                    }
                    i++;
                }
            }
            zzgst zzgst = this.zzp;
            if (zzgst != null) {
                zzgsv.zzp = (zzgst) zzgst.clone();
            }
            zzgsu zzgsu = this.zzr;
            if (zzgsu != null) {
                zzgsv.zzr = (zzgsu) zzgsu.clone();
            }
            int[] iArr = this.zzu;
            if (iArr != null && iArr.length > 0) {
                zzgsv.zzu = (int[]) iArr.clone();
            }
            zzgsx zzgsx = this.zzw;
            if (zzgsx != null) {
                zzgsv.zzw = (zzgsx) zzgsx.clone();
            }
            return zzgsv;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final int computeSerializedSize() {
        int[] iArr;
        super.computeSerializedSize();
        long j = this.zza;
        int i = 0;
        int zzf = j != 0 ? zzgrr.zzf(1, j) + 0 : 0;
        zzgsw[] zzgswArr = this.zzk;
        if (zzgswArr != null && zzgswArr.length > 0) {
            int i2 = 0;
            while (true) {
                zzgsw[] zzgswArr2 = this.zzk;
                if (i2 >= zzgswArr2.length) {
                    break;
                }
                zzgsw zzgsw = zzgswArr2[i2];
                if (zzgsw != null) {
                    zzf += zzgrr.zzb(3, zzgsw);
                }
                i2++;
            }
        }
        byte[] bArr = this.zzl;
        byte[] bArr2 = zzgsc.zzh;
        if (!Arrays.equals(bArr, bArr2)) {
            zzf += zzgrr.zzb(4, this.zzl);
        }
        if (!Arrays.equals(this.zzf, bArr2)) {
            zzf += zzgrr.zzb(6, this.zzf);
        }
        zzgst zzgst = this.zzp;
        if (zzgst != null) {
            zzf += zzgrr.zzb(7, zzgst);
        }
        String str = this.zzn;
        if (str != null && !str.equals("")) {
            zzf += zzgrr.zzb(8, this.zzn);
        }
        String str2 = this.zzo;
        if (str2 != null && !str2.equals("")) {
            zzf += zzgrr.zzb(13, this.zzo);
        }
        String str3 = this.zzq;
        if (str3 != null && !str3.equals("")) {
            zzf += zzgrr.zzb(14, this.zzq);
        }
        long j2 = this.zzg;
        if (j2 != 180000) {
            zzf += zzgrr.zzb((j2 >> 63) ^ (j2 << 1)) + zzgrr.zzb(15);
        }
        zzgsu zzgsu = this.zzr;
        if (zzgsu != null) {
            zzf += zzgrr.zzb(16, zzgsu);
        }
        long j3 = this.zzb;
        if (j3 != 0) {
            zzf += zzgrr.zzf(17, j3);
        }
        if (!Arrays.equals(this.zzh, bArr2)) {
            zzf += zzgrr.zzb(18, this.zzh);
        }
        int[] iArr2 = this.zzu;
        if (iArr2 != null && iArr2.length > 0) {
            int i3 = 0;
            while (true) {
                iArr = this.zzu;
                if (i >= iArr.length) {
                    break;
                }
                i3 += zzgrr.zza(iArr[i]);
                i++;
            }
            zzf = zzf + i3 + (iArr.length * 2);
        }
        zzgsx zzgsx = this.zzw;
        if (zzgsx != null) {
            zzf += zzgrr.zzb(23, zzgsx);
        }
        String str4 = this.zzs;
        return (str4 == null || str4.equals("")) ? zzf : zzf + zzgrr.zzb(24, this.zzs);
    }

    @Override // java.lang.Object
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgsv)) {
            return false;
        }
        zzgsv zzgsv = (zzgsv) obj;
        if (this.zza != zzgsv.zza || this.zzb != zzgsv.zzb || !zzgrx.zza(this.zzk, zzgsv.zzk) || !Arrays.equals(this.zzl, zzgsv.zzl) || !Arrays.equals(this.zzf, zzgsv.zzf)) {
            return false;
        }
        String str = this.zzn;
        if (str == null) {
            if (zzgsv.zzn != null) {
                return false;
            }
        } else if (!str.equals(zzgsv.zzn)) {
            return false;
        }
        String str2 = this.zzo;
        if (str2 == null) {
            if (zzgsv.zzo != null) {
                return false;
            }
        } else if (!str2.equals(zzgsv.zzo)) {
            return false;
        }
        zzgst zzgst = this.zzp;
        if (zzgst == null) {
            if (zzgsv.zzp != null) {
                return false;
            }
        } else if (!zzgst.equals(zzgsv.zzp)) {
            return false;
        }
        String str3 = this.zzq;
        if (str3 == null) {
            if (zzgsv.zzq != null) {
                return false;
            }
        } else if (!str3.equals(zzgsv.zzq)) {
            return false;
        }
        if (this.zzg != zzgsv.zzg) {
            return false;
        }
        zzgsu zzgsu = this.zzr;
        if (zzgsu == null) {
            if (zzgsv.zzr != null) {
                return false;
            }
        } else if (!zzgsu.equals(zzgsv.zzr)) {
            return false;
        }
        if (!Arrays.equals(this.zzh, zzgsv.zzh)) {
            return false;
        }
        String str4 = this.zzs;
        if (str4 == null) {
            if (zzgsv.zzs != null) {
                return false;
            }
        } else if (!str4.equals(zzgsv.zzs)) {
            return false;
        }
        if (!zzgrx.zza(this.zzu, zzgsv.zzu)) {
            return false;
        }
        zzgsx zzgsx = this.zzw;
        if (zzgsx == null) {
            if (zzgsv.zzw != null) {
                return false;
            }
        } else if (!zzgsx.equals(zzgsv.zzw)) {
            return false;
        }
        zzgrv zzgrv = this.zzay;
        if (zzgrv != null && !zzgrv.zzb()) {
            return this.zzay.equals(zzgsv.zzay);
        }
        zzgrv zzgrv2 = zzgsv.zzay;
        return zzgrv2 == null || zzgrv2.zzb();
    }

    @Override // java.lang.Object
    public final int hashCode() {
        int i;
        int i2;
        int i3;
        long j = this.zza;
        long j2 = this.zzb;
        int i4 = (int) 0;
        int i5 = 0;
        int hashCode = (Arrays.hashCode(this.zzf) + ((((Arrays.hashCode(this.zzl) + ((((((((((((((((((int) (j ^ (j >>> 32))) - 1208406192) * 31) + ((int) (j2 ^ (j2 >>> 32)))) * 31) + i4) * 31) + 0) * 31) + 0) * 31) + 0) * 31) + 1237) * 31) + zzgrx.zza(this.zzk)) * 31)) * 31) + 0) * 31)) * 31;
        String str = this.zzn;
        int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
        String str2 = this.zzo;
        int hashCode3 = hashCode2 + (str2 == null ? 0 : str2.hashCode());
        zzgst zzgst = this.zzp;
        int i6 = hashCode3 * 31;
        if (zzgst == null) {
            i = 0;
        } else {
            i = zzgst.hashCode();
        }
        int i7 = (i6 + i) * 31;
        String str3 = this.zzq;
        int hashCode4 = str3 == null ? 0 : str3.hashCode();
        long j3 = this.zzg;
        zzgsu zzgsu = this.zzr;
        int i8 = (((i7 + hashCode4) * 31) + ((int) (j3 ^ (j3 >>> 32)))) * 31;
        if (zzgsu == null) {
            i2 = 0;
        } else {
            i2 = zzgsu.hashCode();
        }
        int hashCode5 = (Arrays.hashCode(this.zzh) + ((i8 + i2) * 31)) * 31;
        String str4 = this.zzs;
        int hashCode6 = (((hashCode5 + (str4 == null ? 0 : str4.hashCode())) * 31) + 0) * 31;
        int[] iArr = this.zzu;
        int hashCode7 = (iArr == null || iArr.length == 0) ? 0 : Arrays.hashCode(iArr);
        zzgsx zzgsx = this.zzw;
        int i9 = (((hashCode6 + hashCode7) * 31) + i4) * 31;
        if (zzgsx == null) {
            i3 = 0;
        } else {
            i3 = zzgsx.hashCode();
        }
        int i10 = (((i9 + i3) * 31) + 1237) * 31;
        zzgrv zzgrv = this.zzay;
        if (zzgrv != null && !zzgrv.zzb()) {
            i5 = this.zzay.hashCode();
        }
        return i10 + i5;
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final void writeTo(zzgrr zzgrr) throws IOException {
        long j = this.zza;
        if (j != 0) {
            zzgrr.zzb(1, j);
        }
        zzgsw[] zzgswArr = this.zzk;
        int i = 0;
        if (zzgswArr != null && zzgswArr.length > 0) {
            int i2 = 0;
            while (true) {
                zzgsw[] zzgswArr2 = this.zzk;
                if (i2 >= zzgswArr2.length) {
                    break;
                }
                zzgsw zzgsw = zzgswArr2[i2];
                if (zzgsw != null) {
                    zzgrr.zza(3, zzgsw);
                }
                i2++;
            }
        }
        byte[] bArr = this.zzl;
        byte[] bArr2 = zzgsc.zzh;
        if (!Arrays.equals(bArr, bArr2)) {
            zzgrr.zza(4, this.zzl);
        }
        if (!Arrays.equals(this.zzf, bArr2)) {
            zzgrr.zza(6, this.zzf);
        }
        zzgst zzgst = this.zzp;
        if (zzgst != null) {
            zzgrr.zza(7, zzgst);
        }
        String str = this.zzn;
        if (str != null && !str.equals("")) {
            zzgrr.zza(8, this.zzn);
        }
        String str2 = this.zzo;
        if (str2 != null && !str2.equals("")) {
            zzgrr.zza(13, this.zzo);
        }
        String str3 = this.zzq;
        if (str3 != null && !str3.equals("")) {
            zzgrr.zza(14, this.zzq);
        }
        long j2 = this.zzg;
        if (j2 != 180000) {
            zzgrr.zzc(120);
            zzgrr.zza((j2 >> 63) ^ (j2 << 1));
        }
        zzgsu zzgsu = this.zzr;
        if (zzgsu != null) {
            zzgrr.zza(16, zzgsu);
        }
        long j3 = this.zzb;
        if (j3 != 0) {
            zzgrr.zzb(17, j3);
        }
        if (!Arrays.equals(this.zzh, bArr2)) {
            zzgrr.zza(18, this.zzh);
        }
        int[] iArr = this.zzu;
        if (iArr != null && iArr.length > 0) {
            while (true) {
                int[] iArr2 = this.zzu;
                if (i >= iArr2.length) {
                    break;
                }
                zzgrr.zza(20, iArr2[i]);
                i++;
            }
        }
        zzgsx zzgsx = this.zzw;
        if (zzgsx != null) {
            zzgrr.zza(23, zzgsx);
        }
        String str4 = this.zzs;
        if (str4 != null && !str4.equals("")) {
            zzgrr.zza(24, this.zzs);
        }
        super.writeTo(zzgrr);
    }
}
