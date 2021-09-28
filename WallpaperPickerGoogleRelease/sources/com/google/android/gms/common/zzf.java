package com.google.android.gms.common;

import android.os.RemoteException;
import android.util.Log;
import androidx.preference.R$layout;
import com.google.android.gms.common.internal.zzaa;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.zzn;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
/* loaded from: classes.dex */
public abstract class zzf extends zzab {
    public int zza;

    public zzf(byte[] bArr) {
        R$layout.zzb(bArr.length == 25);
        this.zza = Arrays.hashCode(bArr);
    }

    public static byte[] zza(String str) {
        try {
            return str.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        IObjectWrapper zzb;
        if (obj != null && (obj instanceof zzaa)) {
            try {
                zzaa zzaa = (zzaa) obj;
                if (zzaa.zzc() == this.zza && (zzb = zzaa.zzb()) != null) {
                    return Arrays.equals(zza(), (byte[]) zzn.zza(zzb));
                }
                return false;
            } catch (RemoteException e) {
                Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e);
            }
        }
        return false;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return this.zza;
    }

    public abstract byte[] zza();

    @Override // com.google.android.gms.common.internal.zzaa
    public final IObjectWrapper zzb() {
        return new zzn(zza());
    }

    @Override // com.google.android.gms.common.internal.zzaa
    public final int zzc() {
        return this.zza;
    }
}
