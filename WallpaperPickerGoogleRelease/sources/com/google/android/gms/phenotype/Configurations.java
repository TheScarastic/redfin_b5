package com.google.android.gms.phenotype;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import androidx.appcompat.R$drawable;
import androidx.slice.view.R$id;
import com.google.android.gms.internal.zzbkv;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
/* loaded from: classes.dex */
public class Configurations extends zzbkv {
    public static final Parcelable.Creator<Configurations> CREATOR = new zzc();
    public final Map<Integer, Configuration> configurationMap = new TreeMap();
    public final long configurationVersion;
    public final Configuration[] configurations;
    public final byte[] experimentToken;
    public final boolean isDelta;
    public final String serverToken;
    public final String snapshotToken;

    public Configurations(String str, String str2, Configuration[] configurationArr, boolean z, byte[] bArr, long j) {
        this.snapshotToken = str;
        this.serverToken = str2;
        this.configurations = configurationArr;
        this.isDelta = z;
        this.experimentToken = bArr;
        this.configurationVersion = j;
        for (Configuration configuration : configurationArr) {
            this.configurationMap.put(Integer.valueOf(configuration.flagType), configuration);
        }
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (obj instanceof Configurations) {
            Configurations configurations = (Configurations) obj;
            if (R$drawable.zza(this.snapshotToken, configurations.snapshotToken) && R$drawable.zza(this.serverToken, configurations.serverToken) && this.configurationMap.equals(configurations.configurationMap) && this.isDelta == configurations.isDelta && Arrays.equals(this.experimentToken, configurations.experimentToken) && this.configurationVersion == configurations.configurationVersion) {
                return true;
            }
        }
        return false;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.snapshotToken, this.serverToken, this.configurationMap, Boolean.valueOf(this.isDelta), this.experimentToken, Long.valueOf(this.configurationVersion)});
    }

    @Override // java.lang.Object
    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder("Configurations('");
        sb.append(this.snapshotToken);
        sb.append('\'');
        sb.append(", ");
        sb.append('\'');
        sb.append(this.serverToken);
        sb.append('\'');
        sb.append(", ");
        sb.append('(');
        for (Configuration configuration : this.configurationMap.values()) {
            sb.append(configuration);
            sb.append(", ");
        }
        sb.append(')');
        sb.append(", ");
        sb.append(this.isDelta);
        sb.append(", ");
        byte[] bArr = this.experimentToken;
        if (bArr == null) {
            str = "null";
        } else {
            str = Base64.encodeToString(bArr, 3);
        }
        sb.append(str);
        sb.append(", ");
        sb.append(this.configurationVersion);
        sb.append(')');
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        R$id.zza(parcel, 2, this.snapshotToken, false);
        R$id.zza(parcel, 3, this.serverToken, false);
        R$id.zza(parcel, 4, this.configurations, i);
        boolean z = this.isDelta;
        R$id.zzb(parcel, 5, 4);
        parcel.writeInt(z ? 1 : 0);
        R$id.zza(parcel, 6, this.experimentToken, false);
        long j = this.configurationVersion;
        R$id.zzb(parcel, 7, 8);
        parcel.writeLong(j);
        R$id.zzc(parcel, zzb);
    }
}
