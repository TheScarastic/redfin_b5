package com.google.android.gms.phenotype;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import androidx.appcompat.R$drawable;
import androidx.slice.view.R$id;
import androidx.viewpager2.widget.FakeDrag$$ExternalSyntheticOutline0;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.google.android.gms.internal.zzbkv;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class ExperimentTokens extends zzbkv {
    public static final Parcelable.Creator<ExperimentTokens> CREATOR = new zzi();
    public final byte[][] additionalDirectExperimentTokens;
    public final byte[][] alwaysCrossExperimentTokens;
    public final byte[] directExperimentToken;
    public final byte[][] gaiaCrossExperimentTokens;
    public final byte[][] otherCrossExperimentTokens;
    public final byte[][] pseudonymousCrossExperimentTokens;
    public final String user;
    public final int[] weakExperimentIds;

    static {
        byte[][] bArr = new byte[0];
        new ExperimentTokens("", null, bArr, bArr, bArr, bArr, null, null);
    }

    public ExperimentTokens(String str, byte[] bArr, byte[][] bArr2, byte[][] bArr3, byte[][] bArr4, byte[][] bArr5, int[] iArr, byte[][] bArr6) {
        this.user = str;
        this.directExperimentToken = bArr;
        this.gaiaCrossExperimentTokens = bArr2;
        this.pseudonymousCrossExperimentTokens = bArr3;
        this.alwaysCrossExperimentTokens = bArr4;
        this.otherCrossExperimentTokens = bArr5;
        this.weakExperimentIds = iArr;
        this.additionalDirectExperimentTokens = bArr6;
    }

    public static void zza(StringBuilder sb, String str, byte[][] bArr) {
        sb.append(str);
        sb.append("=");
        if (bArr == null) {
            sb.append("null");
            return;
        }
        sb.append("(");
        int length = bArr.length;
        boolean z = true;
        int i = 0;
        while (i < length) {
            byte[] bArr2 = bArr[i];
            if (!z) {
                sb.append(", ");
            }
            sb.append("'");
            sb.append(Base64.encodeToString(bArr2, 3));
            sb.append("'");
            i++;
            z = false;
        }
        sb.append(")");
    }

    public static List<String> zzb(byte[][] bArr) {
        if (bArr == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(bArr.length);
        for (byte[] bArr2 : bArr) {
            arrayList.add(Base64.encodeToString(bArr2, 3));
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (obj instanceof ExperimentTokens) {
            ExperimentTokens experimentTokens = (ExperimentTokens) obj;
            if (R$drawable.zza(this.user, experimentTokens.user) && Arrays.equals(this.directExperimentToken, experimentTokens.directExperimentToken) && R$drawable.zza(zzb(this.gaiaCrossExperimentTokens), zzb(experimentTokens.gaiaCrossExperimentTokens)) && R$drawable.zza(zzb(this.pseudonymousCrossExperimentTokens), zzb(experimentTokens.pseudonymousCrossExperimentTokens)) && R$drawable.zza(zzb(this.alwaysCrossExperimentTokens), zzb(experimentTokens.alwaysCrossExperimentTokens)) && R$drawable.zza(zzb(this.otherCrossExperimentTokens), zzb(experimentTokens.otherCrossExperimentTokens)) && R$drawable.zza(zza(this.weakExperimentIds), zza(experimentTokens.weakExperimentIds)) && R$drawable.zza(zzb(this.additionalDirectExperimentTokens), zzb(experimentTokens.additionalDirectExperimentTokens))) {
                return true;
            }
        }
        return false;
    }

    @Override // java.lang.Object
    public String toString() {
        StringBuilder sb = new StringBuilder("ExperimentTokens");
        sb.append("(");
        String str = this.user;
        sb.append(str == null ? "null" : FakeDrag$$ExternalSyntheticOutline0.m(XMPPathFactory$$ExternalSyntheticOutline0.m(str, 2), "'", str, "'"));
        sb.append(", ");
        byte[] bArr = this.directExperimentToken;
        sb.append("direct");
        sb.append("=");
        if (bArr == null) {
            sb.append("null");
        } else {
            sb.append("'");
            sb.append(Base64.encodeToString(bArr, 3));
            sb.append("'");
        }
        sb.append(", ");
        zza(sb, "GAIA", this.gaiaCrossExperimentTokens);
        sb.append(", ");
        zza(sb, "PSEUDO", this.pseudonymousCrossExperimentTokens);
        sb.append(", ");
        zza(sb, "ALWAYS", this.alwaysCrossExperimentTokens);
        sb.append(", ");
        zza(sb, "OTHER", this.otherCrossExperimentTokens);
        sb.append(", ");
        int[] iArr = this.weakExperimentIds;
        sb.append("weak");
        sb.append("=");
        if (iArr == null) {
            sb.append("null");
        } else {
            sb.append("(");
            int length = iArr.length;
            boolean z = true;
            int i = 0;
            while (i < length) {
                int i2 = iArr[i];
                if (!z) {
                    sb.append(", ");
                }
                sb.append(i2);
                i++;
                z = false;
            }
            sb.append(")");
        }
        sb.append(", ");
        zza(sb, "directs", this.additionalDirectExperimentTokens);
        sb.append(")");
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        R$id.zza(parcel, 2, this.user, false);
        R$id.zza(parcel, 3, this.directExperimentToken, false);
        R$id.zza(parcel, 4, this.gaiaCrossExperimentTokens);
        R$id.zza(parcel, 5, this.pseudonymousCrossExperimentTokens);
        R$id.zza(parcel, 6, this.alwaysCrossExperimentTokens);
        R$id.zza(parcel, 7, this.otherCrossExperimentTokens);
        R$id.zza(parcel, 8, this.weakExperimentIds, false);
        R$id.zza(parcel, 9, this.additionalDirectExperimentTokens);
        R$id.zzc(parcel, zzb);
    }

    public static List<Integer> zza(int[] iArr) {
        if (iArr == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(iArr.length);
        for (int i : iArr) {
            arrayList.add(Integer.valueOf(i));
        }
        Collections.sort(arrayList);
        return arrayList;
    }
}
