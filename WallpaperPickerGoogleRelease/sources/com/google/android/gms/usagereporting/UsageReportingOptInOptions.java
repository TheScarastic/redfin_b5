package com.google.android.gms.usagereporting;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.slice.view.R$id;
import com.google.android.gms.internal.zzbkv;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class UsageReportingOptInOptions extends zzbkv {
    public static final Parcelable.Creator<UsageReportingOptInOptions> CREATOR = new zzb();
    public int zza;

    public UsageReportingOptInOptions(int i) {
        new ArrayList();
        this.zza = i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        int i2 = this.zza;
        R$id.zzb(parcel, 2, 4);
        parcel.writeInt(i2);
        R$id.zzc(parcel, zzb);
    }
}
