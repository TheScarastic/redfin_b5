package com.google.android.gms.internal;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes.dex */
public final class zzfiq {
    public static final ConcurrentHashMap<Uri, zzfiq> zza = new ConcurrentHashMap<>();
    public static final String[] zzg = {"key", "value"};
    public final ContentResolver zzb;
    public final Uri zzc;
    public volatile Map<String, String> zzf;
    public final Object zze = new Object();
    public final ContentObserver zzd = new zzfir(this);

    public zzfiq(ContentResolver contentResolver, Uri uri) {
        this.zzb = contentResolver;
        this.zzc = uri;
    }

    /* JADX INFO: finally extract failed */
    public final Map<String, String> zzc() {
        try {
            HashMap hashMap = new HashMap();
            Cursor query = this.zzb.query(this.zzc, zzg, null, null, null);
            if (query != null) {
                while (query.moveToNext()) {
                    try {
                        hashMap.put(query.getString(0), query.getString(1));
                    } catch (Throwable th) {
                        query.close();
                        throw th;
                    }
                }
                query.close();
            }
            return hashMap;
        } catch (SQLiteException | SecurityException unused) {
            Log.e("ConfigurationContentLoader", "PhenotypeFlag unable to load ContentProvider, using default values");
            return null;
        }
    }
}
