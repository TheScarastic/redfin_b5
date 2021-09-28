package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.json.JSONException;
/* loaded from: classes.dex */
public final class zzaa {
    public static final Lock zza = new ReentrantLock();
    public static zzaa zzb;
    public final Lock zzc = new ReentrantLock();
    public final SharedPreferences zzd;

    public zzaa(Context context) {
        this.zzd = context.getSharedPreferences("com.google.android.gms.signin", 0);
    }

    public static zzaa zza(Context context) {
        Objects.requireNonNull(context, "null reference");
        Lock lock = zza;
        ((ReentrantLock) lock).lock();
        try {
            if (zzb == null) {
                zzb = new zzaa(context.getApplicationContext());
            }
            zzaa zzaa = zzb;
            ((ReentrantLock) lock).unlock();
            return zzaa;
        } catch (Throwable th) {
            ((ReentrantLock) zza).unlock();
            throw th;
        }
    }

    public final GoogleSignInAccount zza() {
        String zza2 = zza("defaultGoogleSignInAccount");
        if (TextUtils.isEmpty(zza2)) {
            return null;
        }
        StringBuilder sb = new StringBuilder(XMPPathFactory$$ExternalSyntheticOutline0.m(zza2, 20));
        sb.append("googleSignInAccount");
        sb.append(":");
        sb.append(zza2);
        String zza3 = zza(sb.toString());
        if (zza3 == null) {
            return null;
        }
        try {
            return GoogleSignInAccount.zza(zza3);
        } catch (JSONException unused) {
            return null;
        }
    }

    public final String zza(String str) {
        this.zzc.lock();
        try {
            return this.zzd.getString(str, null);
        } finally {
            this.zzc.unlock();
        }
    }
}
