package com.google.android.gms.common;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.R$id;
import com.google.android.gms.internal.zzbkv;
import com.google.android.gms.internal.zzfit;
import java.util.Arrays;
/* loaded from: classes.dex */
public final class ConnectionResult extends zzbkv {
    public final int zzb;
    public final int zzc;
    public final PendingIntent zzd;
    public final String zze;
    public static final ConnectionResult zza = new ConnectionResult(0);
    public static final Parcelable.Creator<ConnectionResult> CREATOR = new zzb();

    public ConnectionResult(int i) {
        this.zzb = 1;
        this.zzc = i;
        this.zzd = null;
        this.zze = null;
    }

    public static String zza(int i) {
        if (i == 99) {
            return "UNFINISHED";
        }
        if (i == 1500) {
            return "DRIVE_EXTERNAL_STORAGE_REQUIRED";
        }
        switch (i) {
            case -1:
                return "UNKNOWN";
            case 0:
                return "SUCCESS";
            case 1:
                return "SERVICE_MISSING";
            case 2:
                return "SERVICE_VERSION_UPDATE_REQUIRED";
            case 3:
                return "SERVICE_DISABLED";
            case 4:
                return "SIGN_IN_REQUIRED";
            case 5:
                return "INVALID_ACCOUNT";
            case 6:
                return "RESOLUTION_REQUIRED";
            case 7:
                return "NETWORK_ERROR";
            case 8:
                return "INTERNAL_ERROR";
            case 9:
                return "SERVICE_INVALID";
            case 10:
                return "DEVELOPER_ERROR";
            case 11:
                return "LICENSE_CHECK_FAILED";
            default:
                switch (i) {
                    case 13:
                        return "CANCELED";
                    case 14:
                        return "TIMEOUT";
                    case 15:
                        return "INTERRUPTED";
                    case 16:
                        return "API_UNAVAILABLE";
                    case 17:
                        return "SIGN_IN_FAILED";
                    case 18:
                        return "SERVICE_UPDATING";
                    case 19:
                        return "SERVICE_MISSING_PERMISSION";
                    case 20:
                        return "RESTRICTED_PROFILE";
                    case 21:
                        return "API_VERSION_UPDATE_REQUIRED";
                    default:
                        StringBuilder sb = new StringBuilder(31);
                        sb.append("UNKNOWN_ERROR_CODE(");
                        sb.append(i);
                        sb.append(")");
                        return sb.toString();
                }
        }
    }

    @Override // java.lang.Object
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ConnectionResult)) {
            return false;
        }
        ConnectionResult connectionResult = (ConnectionResult) obj;
        return this.zzc == connectionResult.zzc && R$id.zza(this.zzd, connectionResult.zzd) && R$id.zza(this.zze, connectionResult.zze);
    }

    public final boolean hasResolution() {
        return (this.zzc == 0 || this.zzd == null) ? false : true;
    }

    @Override // java.lang.Object
    public final int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.zzc), this.zzd, this.zze});
    }

    public final boolean isSuccess() {
        return this.zzc == 0;
    }

    @Override // java.lang.Object
    public final String toString() {
        zzfit zza2 = R$id.zza(this);
        zza2.zza("statusCode", zza(this.zzc));
        zza2.zza("resolution", this.zzd);
        zza2.zza("message", this.zze);
        return zza2.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int zzb = androidx.slice.view.R$id.zzb(parcel, 20293);
        int i2 = this.zzb;
        androidx.slice.view.R$id.zzb(parcel, 1, 4);
        parcel.writeInt(i2);
        int i3 = this.zzc;
        androidx.slice.view.R$id.zzb(parcel, 2, 4);
        parcel.writeInt(i3);
        androidx.slice.view.R$id.zza(parcel, 3, this.zzd, i, false);
        androidx.slice.view.R$id.zza(parcel, 4, this.zze, false);
        androidx.slice.view.R$id.zzc(parcel, zzb);
    }

    public ConnectionResult(int i, int i2, PendingIntent pendingIntent, String str) {
        this.zzb = i;
        this.zzc = i2;
        this.zzd = pendingIntent;
        this.zze = str;
    }

    public ConnectionResult(int i, PendingIntent pendingIntent) {
        this.zzb = 1;
        this.zzc = i;
        this.zzd = pendingIntent;
        this.zze = null;
    }
}
