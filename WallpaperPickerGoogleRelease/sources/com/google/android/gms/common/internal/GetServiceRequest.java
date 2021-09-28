package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import androidx.slice.view.R$id;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.internal.zzbkv;
/* loaded from: classes.dex */
public class GetServiceRequest extends zzbkv {
    public static final Parcelable.Creator<GetServiceRequest> CREATOR = new zzk();
    public final int zza;
    public final int zzb;
    public int zzc;
    public String zzd;
    public IBinder zze;
    public Scope[] zzf;
    public Bundle zzg;
    public Account zzh;
    public Feature[] zzi;

    public GetServiceRequest(int i) {
        this.zza = 3;
        this.zzc = GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        this.zzb = i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        int i2 = this.zza;
        R$id.zzb(parcel, 1, 4);
        parcel.writeInt(i2);
        int i3 = this.zzb;
        R$id.zzb(parcel, 2, 4);
        parcel.writeInt(i3);
        int i4 = this.zzc;
        R$id.zzb(parcel, 3, 4);
        parcel.writeInt(i4);
        R$id.zza(parcel, 4, this.zzd, false);
        R$id.zza(parcel, 5, this.zze);
        R$id.zza(parcel, 6, this.zzf, i);
        R$id.zza(parcel, 7, this.zzg, false);
        R$id.zza(parcel, 8, this.zzh, i, false);
        R$id.zza(parcel, 10, this.zzi, i);
        R$id.zzc(parcel, zzb);
    }

    public GetServiceRequest(int i, int i2, int i3, String str, IBinder iBinder, Scope[] scopeArr, Bundle bundle, Account account, Feature[] featureArr) {
        IAccountAccessor iAccountAccessor;
        this.zza = i;
        this.zzb = i2;
        this.zzc = i3;
        if ("com.google.android.gms".equals(str)) {
            this.zzd = "com.google.android.gms";
        } else {
            this.zzd = str;
        }
        if (i < 2) {
            Account account2 = null;
            if (iBinder != null) {
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IAccountAccessor");
                if (queryLocalInterface instanceof IAccountAccessor) {
                    iAccountAccessor = (IAccountAccessor) queryLocalInterface;
                } else {
                    iAccountAccessor = new zzw(iBinder);
                }
                int i4 = zza.$r8$clinit;
                if (iAccountAccessor != null) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        account2 = iAccountAccessor.getAccount();
                    } catch (RemoteException unused) {
                        Log.w("AccountAccessor", "Remote account accessor probably died");
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
            }
            this.zzh = account2;
        } else {
            this.zze = iBinder;
            this.zzh = account;
        }
        this.zzf = scopeArr;
        this.zzg = bundle;
        this.zzi = featureArr;
    }
}
