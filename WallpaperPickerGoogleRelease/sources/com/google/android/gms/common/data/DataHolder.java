package com.google.android.gms.common.data;

import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import androidx.slice.view.R$id;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.internal.zzbkv;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
@KeepName
/* loaded from: classes.dex */
public final class DataHolder extends zzbkv implements Closeable {
    public static final Parcelable.Creator<DataHolder> CREATOR = new zzf();
    public final int zza;
    public final String[] zzb;
    public Bundle zzc;
    public final CursorWindow[] zzd;
    public final int zze;
    public final Bundle zzf;
    public int[] zzg;
    public boolean zzi = false;
    public boolean zzj = true;

    static {
        Objects.requireNonNull(new String[0], "null reference");
        new ArrayList();
        new HashMap();
    }

    public DataHolder(int i, String[] strArr, CursorWindow[] cursorWindowArr, int i2, Bundle bundle) {
        this.zza = i;
        this.zzb = strArr;
        this.zzd = cursorWindowArr;
        this.zze = i2;
        this.zzf = bundle;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final void close() {
        synchronized (this) {
            if (!this.zzi) {
                this.zzi = true;
                int i = 0;
                while (true) {
                    CursorWindow[] cursorWindowArr = this.zzd;
                    if (i >= cursorWindowArr.length) {
                        break;
                    }
                    cursorWindowArr[i].close();
                    i++;
                }
            }
        }
    }

    @Override // java.lang.Object
    public final void finalize() throws Throwable {
        boolean z;
        try {
            if (this.zzj && this.zzd.length > 0) {
                synchronized (this) {
                    z = this.zzi;
                }
                if (!z) {
                    close();
                    String obj = toString();
                    StringBuilder sb = new StringBuilder(String.valueOf(obj).length() + 178);
                    sb.append("Internal data leak within a DataBuffer object detected!  Be sure to explicitly call release() on all DataBuffer extending objects when you are done with them. (internal object: ");
                    sb.append(obj);
                    sb.append(")");
                    Log.e("DataBuffer", sb.toString());
                }
            }
        } finally {
            super.finalize();
        }
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        R$id.zza(parcel, 1, this.zzb, false);
        R$id.zza(parcel, 2, this.zzd, i);
        int i2 = this.zze;
        R$id.zzb(parcel, 3, 4);
        parcel.writeInt(i2);
        R$id.zza(parcel, 4, this.zzf, false);
        int i3 = this.zza;
        R$id.zzb(parcel, 1000, 4);
        parcel.writeInt(i3);
        R$id.zzc(parcel, zzb);
        if ((i & 1) != 0) {
            close();
        }
    }
}
