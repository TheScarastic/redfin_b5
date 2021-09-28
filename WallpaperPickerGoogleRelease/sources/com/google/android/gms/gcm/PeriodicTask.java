package com.google.android.gms.gcm;

import android.os.Parcel;
import android.os.Parcelable;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public class PeriodicTask extends Task {
    public static final Parcelable.Creator<PeriodicTask> CREATOR = new zzf();
    public long mFlexInSeconds;
    public long mIntervalInSeconds;

    public PeriodicTask(Parcel parcel, zzf zzf) {
        super(parcel);
        this.mIntervalInSeconds = -1;
        this.mFlexInSeconds = -1;
        this.mIntervalInSeconds = parcel.readLong();
        this.mFlexInSeconds = Math.min(parcel.readLong(), this.mIntervalInSeconds);
    }

    @Override // java.lang.Object
    public String toString() {
        String obj = super.toString();
        long j = this.mIntervalInSeconds;
        long j2 = this.mFlexInSeconds;
        StringBuilder sb = new StringBuilder(XMPPathFactory$$ExternalSyntheticOutline0.m(obj, 54));
        sb.append(obj);
        sb.append(" period=");
        sb.append(j);
        sb.append(" flex=");
        sb.append(j2);
        return sb.toString();
    }

    @Override // com.google.android.gms.gcm.Task, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.zza);
        parcel.writeString(this.zzb);
        parcel.writeInt(this.zzc ? 1 : 0);
        parcel.writeInt(this.zzd ? 1 : 0);
        parcel.writeLong(this.mIntervalInSeconds);
        parcel.writeLong(this.mFlexInSeconds);
    }
}
