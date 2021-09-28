package com.google.android.gms.common.images;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.R$id;
import com.google.android.gms.internal.zzbkv;
import java.util.Arrays;
import java.util.Locale;
/* loaded from: classes.dex */
public final class WebImage extends zzbkv {
    public static final Parcelable.Creator<WebImage> CREATOR = new zze();
    public final int zza;
    public final Uri zzb;
    public final int zzc;
    public final int zzd;

    public WebImage(int i, Uri uri, int i2, int i3) {
        this.zza = i;
        this.zzb = uri;
        this.zzc = i2;
        this.zzd = i3;
    }

    @Override // java.lang.Object
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && (obj instanceof WebImage)) {
            WebImage webImage = (WebImage) obj;
            if (R$id.zza(this.zzb, webImage.zzb) && this.zzc == webImage.zzc && this.zzd == webImage.zzd) {
                return true;
            }
        }
        return false;
    }

    @Override // java.lang.Object
    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.zzb, Integer.valueOf(this.zzc), Integer.valueOf(this.zzd)});
    }

    @Override // java.lang.Object
    public final String toString() {
        return String.format(Locale.US, "Image %dx%d %s", Integer.valueOf(this.zzc), Integer.valueOf(this.zzd), this.zzb.toString());
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int zzb = androidx.slice.view.R$id.zzb(parcel, 20293);
        int i2 = this.zza;
        androidx.slice.view.R$id.zzb(parcel, 1, 4);
        parcel.writeInt(i2);
        androidx.slice.view.R$id.zza(parcel, 2, this.zzb, i, false);
        int i3 = this.zzc;
        androidx.slice.view.R$id.zzb(parcel, 3, 4);
        parcel.writeInt(i3);
        int i4 = this.zzd;
        androidx.slice.view.R$id.zzb(parcel, 4, 4);
        parcel.writeInt(i4);
        androidx.slice.view.R$id.zzc(parcel, zzb);
    }
}
