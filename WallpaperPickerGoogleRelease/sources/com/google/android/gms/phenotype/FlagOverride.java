package com.google.android.gms.phenotype;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.appcompat.R$drawable;
import androidx.slice.view.R$id;
import com.google.android.gms.internal.zzbkv;
/* loaded from: classes.dex */
public class FlagOverride extends zzbkv {
    public static final Parcelable.Creator<FlagOverride> CREATOR = new zzl();
    public final boolean committed;
    public final String configurationName;
    public final Flag flag;
    public final String userName;

    public FlagOverride(String str, String str2, Flag flag, boolean z) {
        this.configurationName = str;
        this.userName = str2;
        this.flag = flag;
        this.committed = z;
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FlagOverride)) {
            return false;
        }
        FlagOverride flagOverride = (FlagOverride) obj;
        return R$drawable.zza(this.configurationName, flagOverride.configurationName) && R$drawable.zza(this.userName, flagOverride.userName) && R$drawable.zza(this.flag, flagOverride.flag) && this.committed == flagOverride.committed;
    }

    @Override // java.lang.Object
    public String toString() {
        return toString(new StringBuilder());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        R$id.zza(parcel, 2, this.configurationName, false);
        R$id.zza(parcel, 3, this.userName, false);
        R$id.zza(parcel, 4, this.flag, i, false);
        boolean z = this.committed;
        R$id.zzb(parcel, 5, 4);
        parcel.writeInt(z ? 1 : 0);
        R$id.zzc(parcel, zzb);
    }

    public String toString(StringBuilder sb) {
        sb.append("FlagOverride(");
        sb.append(this.configurationName);
        sb.append(", ");
        sb.append(this.userName);
        sb.append(", ");
        this.flag.toString(sb);
        sb.append(", ");
        sb.append(this.committed);
        sb.append(")");
        return sb.toString();
    }
}
