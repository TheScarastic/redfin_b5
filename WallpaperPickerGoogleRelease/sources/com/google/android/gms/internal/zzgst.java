package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;
/* loaded from: classes.dex */
public final class zzgst extends zzgrt<zzgst> implements Cloneable {
    public String[] zza;
    public String[] zzb;
    public int[] zzc = zzgsc.zza;
    public long[] zzd;
    public long[] zze;

    public zzgst() {
        String[] strArr = zzgsc.zzf;
        this.zza = strArr;
        this.zzb = strArr;
        long[] jArr = zzgsc.zzb;
        this.zzd = jArr;
        this.zze = jArr;
        this.zzay = null;
        this.zzaz = -1;
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final Object clone() throws CloneNotSupportedException {
        try {
            zzgst zzgst = (zzgst) super.clone();
            String[] strArr = this.zza;
            if (strArr != null && strArr.length > 0) {
                zzgst.zza = (String[]) strArr.clone();
            }
            String[] strArr2 = this.zzb;
            if (strArr2 != null && strArr2.length > 0) {
                zzgst.zzb = (String[]) strArr2.clone();
            }
            int[] iArr = this.zzc;
            if (iArr != null && iArr.length > 0) {
                zzgst.zzc = (int[]) iArr.clone();
            }
            long[] jArr = this.zzd;
            if (jArr != null && jArr.length > 0) {
                zzgst.zzd = (long[]) jArr.clone();
            }
            long[] jArr2 = this.zze;
            if (jArr2 != null && jArr2.length > 0) {
                zzgst.zze = (long[]) jArr2.clone();
            }
            return zzgst;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final int computeSerializedSize() {
        int i;
        long[] jArr;
        int[] iArr;
        super.computeSerializedSize();
        String[] strArr = this.zza;
        int i2 = 0;
        if (strArr == null || strArr.length <= 0) {
            i = 0;
        } else {
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            while (true) {
                String[] strArr2 = this.zza;
                if (i3 >= strArr2.length) {
                    break;
                }
                String str = strArr2[i3];
                if (str != null) {
                    i5++;
                    int zza = zzgrr.zza(str);
                    i4 += zzgrr.zzd(zza) + zza;
                }
                i3++;
            }
            i = (i5 * 1) + i4 + 0;
        }
        String[] strArr3 = this.zzb;
        if (strArr3 != null && strArr3.length > 0) {
            int i6 = 0;
            int i7 = 0;
            int i8 = 0;
            while (true) {
                String[] strArr4 = this.zzb;
                if (i6 >= strArr4.length) {
                    break;
                }
                String str2 = strArr4[i6];
                if (str2 != null) {
                    i8++;
                    int zza2 = zzgrr.zza(str2);
                    i7 += zzgrr.zzd(zza2) + zza2;
                }
                i6++;
            }
            i = i + i7 + (i8 * 1);
        }
        int[] iArr2 = this.zzc;
        if (iArr2 != null && iArr2.length > 0) {
            int i9 = 0;
            int i10 = 0;
            while (true) {
                iArr = this.zzc;
                if (i9 >= iArr.length) {
                    break;
                }
                i10 += zzgrr.zza(iArr[i9]);
                i9++;
            }
            i = i + i10 + (iArr.length * 1);
        }
        long[] jArr2 = this.zzd;
        if (jArr2 != null && jArr2.length > 0) {
            int i11 = 0;
            int i12 = 0;
            while (true) {
                jArr = this.zzd;
                if (i11 >= jArr.length) {
                    break;
                }
                i12 += zzgrr.zzb(jArr[i11]);
                i11++;
            }
            i = i + i12 + (jArr.length * 1);
        }
        long[] jArr3 = this.zze;
        if (jArr3 == null || jArr3.length <= 0) {
            return i;
        }
        int i13 = 0;
        while (true) {
            long[] jArr4 = this.zze;
            if (i2 >= jArr4.length) {
                return i + i13 + (jArr4.length * 1);
            }
            i13 += zzgrr.zzb(jArr4[i2]);
            i2++;
        }
    }

    @Override // java.lang.Object
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgst)) {
            return false;
        }
        zzgst zzgst = (zzgst) obj;
        if (!zzgrx.zza(this.zza, zzgst.zza) || !zzgrx.zza(this.zzb, zzgst.zzb) || !zzgrx.zza(this.zzc, zzgst.zzc) || !zzgrx.zza(this.zzd, zzgst.zzd) || !zzgrx.zza(this.zze, zzgst.zze)) {
            return false;
        }
        zzgrv zzgrv = this.zzay;
        if (zzgrv != null && !zzgrv.zzb()) {
            return this.zzay.equals(zzgst.zzay);
        }
        zzgrv zzgrv2 = zzgst.zzay;
        return zzgrv2 == null || zzgrv2.zzb();
    }

    @Override // java.lang.Object
    public final int hashCode() {
        int zza = (((zzgrx.zza(this.zza) - 1208406254) * 31) + zzgrx.zza(this.zzb)) * 31;
        int[] iArr = this.zzc;
        int i = 0;
        int hashCode = (zza + ((iArr == null || iArr.length == 0) ? 0 : Arrays.hashCode(iArr))) * 31;
        long[] jArr = this.zzd;
        int hashCode2 = (hashCode + ((jArr == null || jArr.length == 0) ? 0 : Arrays.hashCode(jArr))) * 31;
        long[] jArr2 = this.zze;
        int hashCode3 = (hashCode2 + ((jArr2 == null || jArr2.length == 0) ? 0 : Arrays.hashCode(jArr2))) * 31;
        zzgrv zzgrv = this.zzay;
        if (zzgrv != null && !zzgrv.zzb()) {
            i = this.zzay.hashCode();
        }
        return hashCode3 + i;
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final void writeTo(zzgrr zzgrr) throws IOException {
        String[] strArr = this.zza;
        int i = 0;
        if (strArr != null && strArr.length > 0) {
            int i2 = 0;
            while (true) {
                String[] strArr2 = this.zza;
                if (i2 >= strArr2.length) {
                    break;
                }
                String str = strArr2[i2];
                if (str != null) {
                    zzgrr.zza(1, str);
                }
                i2++;
            }
        }
        String[] strArr3 = this.zzb;
        if (strArr3 != null && strArr3.length > 0) {
            int i3 = 0;
            while (true) {
                String[] strArr4 = this.zzb;
                if (i3 >= strArr4.length) {
                    break;
                }
                String str2 = strArr4[i3];
                if (str2 != null) {
                    zzgrr.zza(2, str2);
                }
                i3++;
            }
        }
        int[] iArr = this.zzc;
        if (iArr != null && iArr.length > 0) {
            int i4 = 0;
            while (true) {
                int[] iArr2 = this.zzc;
                if (i4 >= iArr2.length) {
                    break;
                }
                zzgrr.zza(3, iArr2[i4]);
                i4++;
            }
        }
        long[] jArr = this.zzd;
        if (jArr != null && jArr.length > 0) {
            int i5 = 0;
            while (true) {
                long[] jArr2 = this.zzd;
                if (i5 >= jArr2.length) {
                    break;
                }
                zzgrr.zzb(4, jArr2[i5]);
                i5++;
            }
        }
        long[] jArr3 = this.zze;
        if (jArr3 != null && jArr3.length > 0) {
            while (true) {
                long[] jArr4 = this.zze;
                if (i >= jArr4.length) {
                    break;
                }
                zzgrr.zzb(5, jArr4[i]);
                i++;
            }
        }
        super.writeTo(zzgrr);
    }
}
