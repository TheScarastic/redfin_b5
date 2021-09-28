package com.google.android.gms.internal;

import android.content.SharedPreferences;
import android.util.Log;
/* loaded from: classes.dex */
public final class zzfix extends zzfis<String> {
    public zzfix(zzfiz zzfiz, String str, String str2) {
        super(zzfiz, str, str2, null);
    }

    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // com.google.android.gms.internal.zzfis
    public final String zza(SharedPreferences sharedPreferences) {
        try {
            return sharedPreferences.getString(this.zza, null);
        } catch (ClassCastException e) {
            String valueOf = String.valueOf(this.zza);
            Log.e("PhenotypeFlag", valueOf.length() != 0 ? "Invalid string value in SharedPreferences for ".concat(valueOf) : new String("Invalid string value in SharedPreferences for "), e);
            return null;
        }
    }

    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // com.google.android.gms.internal.zzfis
    /* renamed from: zza */
    public final /* synthetic */ String mo9zza(String str) {
        return str;
    }
}
