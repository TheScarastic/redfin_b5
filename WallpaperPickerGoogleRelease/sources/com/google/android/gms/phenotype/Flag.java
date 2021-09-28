package com.google.android.gms.phenotype;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import androidx.appcompat.R$dimen$$ExternalSyntheticOutline0;
import androidx.appcompat.R$drawable;
import androidx.slice.view.R$id;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.google.android.gms.internal.zzbkv;
import java.util.Arrays;
/* loaded from: classes.dex */
public class Flag extends zzbkv implements Comparable<Flag> {
    public static final Parcelable.Creator<Flag> CREATOR = new zzk();
    public final int flagStorageType;
    public final int flagValueType;
    public final String name;
    public final long zza;
    public final boolean zzb;
    public final double zzc;
    public final String zzd;
    public final byte[] zze;

    public Flag(String str, long j, boolean z, double d, String str2, byte[] bArr, int i, int i2) {
        this.name = str;
        this.zza = j;
        this.zzb = z;
        this.zzc = d;
        this.zzd = str2;
        this.zze = bArr;
        this.flagValueType = i;
        this.flagStorageType = i2;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0066, code lost:
        if (r7 == r8) goto L_0x00aa;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x009b, code lost:
        if (r7 != false) goto L_0x00ac;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00a8, code lost:
        if (r7 == 0) goto L_0x00aa;
     */
    @Override // java.lang.Comparable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int compareTo(com.google.android.gms.phenotype.Flag r8) {
        /*
            r7 = this;
            com.google.android.gms.phenotype.Flag r8 = (com.google.android.gms.phenotype.Flag) r8
            java.lang.String r0 = r7.name
            java.lang.String r1 = r8.name
            int r0 = r0.compareTo(r1)
            r1 = 1
            if (r0 == 0) goto L_0x000f
            goto L_0x00ad
        L_0x000f:
            int r0 = r7.flagValueType
            int r2 = r8.flagValueType
            r3 = -1
            r4 = 0
            if (r0 >= r2) goto L_0x0019
            r2 = r3
            goto L_0x001e
        L_0x0019:
            if (r0 != r2) goto L_0x001d
            r2 = r4
            goto L_0x001e
        L_0x001d:
            r2 = r1
        L_0x001e:
            if (r2 == 0) goto L_0x0023
        L_0x0020:
            r0 = r2
            goto L_0x00ad
        L_0x0023:
            if (r0 == r1) goto L_0x009e
            r2 = 2
            if (r0 == r2) goto L_0x0094
            r2 = 3
            if (r0 == r2) goto L_0x008b
            r2 = 4
            if (r0 == r2) goto L_0x0079
            r2 = 5
            if (r0 != r2) goto L_0x0069
            byte[] r0 = r7.zze
            byte[] r2 = r8.zze
            if (r0 != r2) goto L_0x0039
            goto L_0x00aa
        L_0x0039:
            if (r0 != 0) goto L_0x003d
            goto L_0x00a6
        L_0x003d:
            if (r2 != 0) goto L_0x0041
            goto L_0x00ac
        L_0x0041:
            r0 = r4
        L_0x0042:
            byte[] r2 = r7.zze
            int r2 = r2.length
            byte[] r5 = r8.zze
            int r5 = r5.length
            int r2 = java.lang.Math.min(r2, r5)
            if (r0 >= r2) goto L_0x005d
            byte[] r2 = r7.zze
            byte r2 = r2[r0]
            byte[] r5 = r8.zze
            byte r5 = r5[r0]
            int r2 = r2 - r5
            if (r2 == 0) goto L_0x005a
            goto L_0x0020
        L_0x005a:
            int r0 = r0 + 1
            goto L_0x0042
        L_0x005d:
            byte[] r7 = r7.zze
            int r7 = r7.length
            byte[] r8 = r8.zze
            int r8 = r8.length
            if (r7 >= r8) goto L_0x0066
            goto L_0x00a6
        L_0x0066:
            if (r7 != r8) goto L_0x00ac
            goto L_0x00aa
        L_0x0069:
            java.lang.AssertionError r8 = new java.lang.AssertionError
            int r7 = r7.flagValueType
            r0 = 31
            java.lang.String r1 = "Invalid enum value: "
            java.lang.String r7 = androidx.appcompat.R$dimen$$ExternalSyntheticOutline0.m(r0, r1, r7)
            r8.<init>(r7)
            throw r8
        L_0x0079:
            java.lang.String r7 = r7.zzd
            java.lang.String r8 = r8.zzd
            if (r7 != r8) goto L_0x0080
            goto L_0x00aa
        L_0x0080:
            if (r7 != 0) goto L_0x0083
            goto L_0x00a6
        L_0x0083:
            if (r8 != 0) goto L_0x0086
            goto L_0x00ac
        L_0x0086:
            int r0 = r7.compareTo(r8)
            goto L_0x00ad
        L_0x008b:
            double r0 = r7.zzc
            double r7 = r8.zzc
            int r0 = java.lang.Double.compare(r0, r7)
            goto L_0x00ad
        L_0x0094:
            boolean r7 = r7.zzb
            boolean r8 = r8.zzb
            if (r7 != r8) goto L_0x009b
            goto L_0x00aa
        L_0x009b:
            if (r7 == 0) goto L_0x00a6
            goto L_0x00ac
        L_0x009e:
            long r5 = r7.zza
            long r7 = r8.zza
            int r7 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r7 >= 0) goto L_0x00a8
        L_0x00a6:
            r0 = r3
            goto L_0x00ad
        L_0x00a8:
            if (r7 != 0) goto L_0x00ac
        L_0x00aa:
            r0 = r4
            goto L_0x00ad
        L_0x00ac:
            r0 = r1
        L_0x00ad:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.phenotype.Flag.compareTo(java.lang.Object):int");
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        int i;
        if (obj instanceof Flag) {
            Flag flag = (Flag) obj;
            if (R$drawable.zza(this.name, flag.name) && (i = this.flagValueType) == flag.flagValueType && this.flagStorageType == flag.flagStorageType) {
                if (i != 1) {
                    if (i != 2) {
                        if (i != 3) {
                            if (i == 4) {
                                return R$drawable.zza(this.zzd, flag.zzd);
                            }
                            if (i == 5) {
                                return Arrays.equals(this.zze, flag.zze);
                            }
                            throw new AssertionError(R$dimen$$ExternalSyntheticOutline0.m(31, "Invalid enum value: ", this.flagValueType));
                        } else if (this.zzc == flag.zzc) {
                            return true;
                        } else {
                            return false;
                        }
                    } else if (this.zzb == flag.zzb) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (this.zza == flag.zza) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // java.lang.Object
    public String toString() {
        return toString(new StringBuilder());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        R$id.zza(parcel, 2, this.name, false);
        long j = this.zza;
        R$id.zzb(parcel, 3, 8);
        parcel.writeLong(j);
        boolean z = this.zzb;
        R$id.zzb(parcel, 4, 4);
        parcel.writeInt(z ? 1 : 0);
        double d = this.zzc;
        R$id.zzb(parcel, 5, 8);
        parcel.writeDouble(d);
        R$id.zza(parcel, 6, this.zzd, false);
        R$id.zza(parcel, 7, this.zze, false);
        int i2 = this.flagValueType;
        R$id.zzb(parcel, 8, 4);
        parcel.writeInt(i2);
        int i3 = this.flagStorageType;
        R$id.zzb(parcel, 9, 4);
        parcel.writeInt(i3);
        R$id.zzc(parcel, zzb);
    }

    public String toString(StringBuilder sb) {
        sb.append("Flag(");
        sb.append(this.name);
        sb.append(", ");
        int i = this.flagValueType;
        if (i == 1) {
            sb.append(this.zza);
        } else if (i == 2) {
            sb.append(this.zzb);
        } else if (i == 3) {
            sb.append(this.zzc);
        } else if (i == 4) {
            sb.append("'");
            sb.append(this.zzd);
            sb.append("'");
        } else if (i != 5) {
            String str = this.name;
            int i2 = this.flagValueType;
            StringBuilder sb2 = new StringBuilder(XMPPathFactory$$ExternalSyntheticOutline0.m(str, 27));
            sb2.append("Invalid type: ");
            sb2.append(str);
            sb2.append(", ");
            sb2.append(i2);
            throw new AssertionError(sb2.toString());
        } else if (this.zze == null) {
            sb.append("null");
        } else {
            sb.append("'");
            sb.append(Base64.encodeToString(this.zze, 3));
            sb.append("'");
        }
        sb.append(", ");
        sb.append(this.flagValueType);
        sb.append(", ");
        sb.append(this.flagStorageType);
        sb.append(")");
        return sb.toString();
    }
}
