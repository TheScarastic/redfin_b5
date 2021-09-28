package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.slice.view.R$id;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import java.util.List;
/* loaded from: classes.dex */
public final class WakeLockEvent extends StatsEvent {
    public static final Parcelable.Creator<WakeLockEvent> CREATOR = new zzd();
    public final int zza;
    public final long zzb;
    public int zzc;
    public final String zzd;
    public final String zze;
    public final String zzf;
    public final int zzg;
    public final List<String> zzh;
    public final String zzi;
    public final long zzj;
    public int zzk;
    public final String zzl;
    public final float zzm;
    public final long zzn;
    public long zzo = -1;

    public WakeLockEvent(int i, long j, int i2, String str, int i3, List<String> list, String str2, long j2, int i4, String str3, String str4, float f, long j3, String str5) {
        this.zza = i;
        this.zzb = j;
        this.zzc = i2;
        this.zzd = str;
        this.zze = str3;
        this.zzf = str5;
        this.zzg = i3;
        this.zzh = list;
        this.zzi = str2;
        this.zzj = j2;
        this.zzk = i4;
        this.zzl = str4;
        this.zzm = f;
        this.zzn = j3;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        int i2 = this.zza;
        R$id.zzb(parcel, 1, 4);
        parcel.writeInt(i2);
        long j = this.zzb;
        R$id.zzb(parcel, 2, 8);
        parcel.writeLong(j);
        R$id.zza(parcel, 4, this.zzd, false);
        int i3 = this.zzg;
        R$id.zzb(parcel, 5, 4);
        parcel.writeInt(i3);
        List<String> list = this.zzh;
        if (list != null) {
            int zzb2 = R$id.zzb(parcel, 6);
            parcel.writeStringList(list);
            R$id.zzc(parcel, zzb2);
        }
        long j2 = this.zzj;
        R$id.zzb(parcel, 8, 8);
        parcel.writeLong(j2);
        R$id.zza(parcel, 10, this.zze, false);
        int i4 = this.zzc;
        R$id.zzb(parcel, 11, 4);
        parcel.writeInt(i4);
        R$id.zza(parcel, 12, this.zzi, false);
        R$id.zza(parcel, 13, this.zzl, false);
        int i5 = this.zzk;
        R$id.zzb(parcel, 14, 4);
        parcel.writeInt(i5);
        float f = this.zzm;
        R$id.zzb(parcel, 15, 4);
        parcel.writeFloat(f);
        long j3 = this.zzn;
        R$id.zzb(parcel, 16, 8);
        parcel.writeLong(j3);
        R$id.zza(parcel, 17, this.zzf, false);
        R$id.zzc(parcel, zzb);
    }

    @Override // com.google.android.gms.common.stats.StatsEvent
    public final long zza() {
        return this.zzb;
    }

    @Override // com.google.android.gms.common.stats.StatsEvent
    public final int zzb() {
        return this.zzc;
    }

    @Override // com.google.android.gms.common.stats.StatsEvent
    public final long zzc() {
        return this.zzo;
    }

    @Override // com.google.android.gms.common.stats.StatsEvent
    public final String zzd() {
        String str;
        String str2 = this.zzd;
        int i = this.zzg;
        List<String> list = this.zzh;
        String str3 = "";
        if (list == null) {
            str = str3;
        } else {
            str = TextUtils.join(",", list);
        }
        int i2 = this.zzk;
        String str4 = this.zze;
        if (str4 == null) {
            str4 = str3;
        }
        String str5 = this.zzl;
        if (str5 == null) {
            str5 = str3;
        }
        float f = this.zzm;
        String str6 = this.zzf;
        if (str6 != null) {
            str3 = str6;
        }
        StringBuilder sb = new StringBuilder(XMPPathFactory$$ExternalSyntheticOutline0.m(str3, XMPPathFactory$$ExternalSyntheticOutline0.m(str5, XMPPathFactory$$ExternalSyntheticOutline0.m(str4, XMPPathFactory$$ExternalSyntheticOutline0.m(str, XMPPathFactory$$ExternalSyntheticOutline0.m(str2, 45))))));
        sb.append("\t");
        sb.append(str2);
        sb.append("\t");
        sb.append(i);
        sb.append("\t");
        sb.append(str);
        sb.append("\t");
        sb.append(i2);
        sb.append("\t");
        sb.append(str4);
        sb.append("\t");
        sb.append(str5);
        sb.append("\t");
        sb.append(f);
        sb.append("\t");
        sb.append(str3);
        return sb.toString();
    }
}
