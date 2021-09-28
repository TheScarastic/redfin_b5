package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class zzgsu extends zzgrt<zzgsu> implements Cloneable {
    public byte[][] zzc = zzgsc.zzg;

    public zzgsu() {
        this.zzay = null;
        this.zzaz = -1;
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final Object clone() throws CloneNotSupportedException {
        try {
            zzgsu zzgsu = (zzgsu) super.clone();
            byte[][] bArr = this.zzc;
            if (bArr != null && bArr.length > 0) {
                zzgsu.zzc = (byte[][]) bArr.clone();
            }
            return zzgsu;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final int computeSerializedSize() {
        super.computeSerializedSize();
        byte[] bArr = zzgsc.zzh;
        int i = 0;
        int zzb = !Arrays.equals(bArr, bArr) ? zzgrr.zzb(1, bArr) + 0 : 0;
        byte[][] bArr2 = this.zzc;
        if (bArr2 == null || bArr2.length <= 0) {
            return zzb;
        }
        int i2 = 0;
        int i3 = 0;
        while (true) {
            byte[][] bArr3 = this.zzc;
            if (i >= bArr3.length) {
                return zzb + i2 + (i3 * 1);
            }
            byte[] bArr4 = bArr3[i];
            if (bArr4 != null) {
                i3++;
                i2 += zzgrr.zzd(bArr4.length) + bArr4.length;
            }
            i++;
        }
    }

    @Override // java.lang.Object
    public final boolean equals(Object obj) {
        int i;
        int i2;
        boolean z;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgsu)) {
            return false;
        }
        zzgsu zzgsu = (zzgsu) obj;
        byte[] bArr = zzgsc.zzh;
        Objects.requireNonNull(zzgsu);
        if (!Arrays.equals(bArr, bArr)) {
            return false;
        }
        byte[][] bArr2 = this.zzc;
        byte[][] bArr3 = zzgsu.zzc;
        Object obj2 = zzgrx.zzb;
        if (bArr2 == null) {
            i = 0;
        } else {
            i = bArr2.length;
        }
        if (bArr3 == null) {
            i2 = 0;
        } else {
            i2 = bArr3.length;
        }
        int i3 = 0;
        int i4 = 0;
        while (true) {
            if (i3 >= i || bArr2[i3] != null) {
                while (i4 < i2 && bArr3[i4] == null) {
                    i4++;
                }
                boolean z2 = i3 >= i;
                boolean z3 = i4 >= i2;
                if (z2 && z3) {
                    z = true;
                    break;
                } else if (z2 == z3 && Arrays.equals(bArr2[i3], bArr3[i4])) {
                    i3++;
                    i4++;
                }
            } else {
                i3++;
            }
        }
        z = false;
        if (!z) {
            return false;
        }
        zzgrv zzgrv = this.zzay;
        if (zzgrv != null && !zzgrv.zzb()) {
            return this.zzay.equals(zzgsu.zzay);
        }
        zzgrv zzgrv2 = zzgsu.zzay;
        return zzgrv2 == null || zzgrv2.zzb();
    }

    @Override // java.lang.Object
    public final int hashCode() {
        int i;
        int i2 = 0;
        int hashCode = (((Arrays.hashCode(zzgsc.zzh) - 1208406223) * 31) + 0) * 31;
        byte[][] bArr = this.zzc;
        Object obj = zzgrx.zzb;
        if (bArr == null) {
            i = 0;
        } else {
            i = bArr.length;
        }
        int i3 = 0;
        for (int i4 = 0; i4 < i; i4++) {
            byte[] bArr2 = bArr[i4];
            if (bArr2 != null) {
                i3 = (i3 * 31) + Arrays.hashCode(bArr2);
            }
        }
        int i5 = (((hashCode + i3) * 31) + 1237) * 31;
        zzgrv zzgrv = this.zzay;
        if (zzgrv != null && !zzgrv.zzb()) {
            i2 = this.zzay.hashCode();
        }
        return i5 + i2;
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final void writeTo(zzgrr zzgrr) throws IOException {
        byte[] bArr = zzgsc.zzh;
        if (!Arrays.equals(bArr, bArr)) {
            zzgrr.zza(1, bArr);
        }
        byte[][] bArr2 = this.zzc;
        if (bArr2 != null && bArr2.length > 0) {
            int i = 0;
            while (true) {
                byte[][] bArr3 = this.zzc;
                if (i >= bArr3.length) {
                    break;
                }
                byte[] bArr4 = bArr3[i];
                if (bArr4 != null) {
                    zzgrr.zza(2, bArr4);
                }
                i++;
            }
        }
        super.writeTo(zzgrr);
    }
}
