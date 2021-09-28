package com.google.android.gms.internal;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class zzfib {
    public static HashMap<String, String> zzf;
    public static Object zzk;
    public static boolean zzl;
    public static final Uri zzc = Uri.parse("content://com.google.android.gsf.gservices");
    public static final Uri zzd = Uri.parse("content://com.google.android.gsf.gservices/prefix");
    public static final Pattern zza = Pattern.compile("^(1|true|t|on|yes|y)$", 2);
    public static final Pattern zzb = Pattern.compile("^(0|false|f|off|no|n)$", 2);
    public static final AtomicBoolean zze = new AtomicBoolean();
    public static final HashMap<String, Boolean> zzg = new HashMap<>();
    public static final HashMap<String, Integer> zzh = new HashMap<>();
    public static final HashMap<String, Long> zzi = new HashMap<>();
    public static final HashMap<String, Float> zzj = new HashMap<>();
    public static String[] zzm = new String[0];

    public static void zza(ContentResolver contentResolver) {
        if (zzf == null) {
            zze.set(false);
            zzf = new HashMap<>();
            zzk = new Object();
            zzl = false;
            contentResolver.registerContentObserver(zzc, true, new zzfic());
        } else if (zze.getAndSet(false)) {
            zzf.clear();
            zzg.clear();
            zzh.clear();
            zzi.clear();
            zzj.clear();
            zzk = new Object();
            zzl = false;
        }
    }

    public static String zza(ContentResolver contentResolver, String str) {
        synchronized (zzfib.class) {
            zza(contentResolver);
            Object obj = zzk;
            String str2 = null;
            if (zzf.containsKey(str)) {
                String str3 = zzf.get(str);
                if (str3 != null) {
                    str2 = str3;
                }
                return str2;
            }
            for (String str4 : zzm) {
                if (str.startsWith(str4)) {
                    if (!zzl || zzf.isEmpty()) {
                        String[] strArr = zzm;
                        HashMap<String, String> hashMap = zzf;
                        Cursor query = contentResolver.query(zzd, null, null, strArr, null);
                        TreeMap treeMap = new TreeMap();
                        if (query != null) {
                            while (query.moveToNext()) {
                                treeMap.put(query.getString(0), query.getString(1));
                            }
                            query.close();
                        }
                        hashMap.putAll(treeMap);
                        zzl = true;
                        if (zzf.containsKey(str)) {
                            String str5 = zzf.get(str);
                            if (str5 != null) {
                                str2 = str5;
                            }
                            return str2;
                        }
                    }
                    return null;
                }
            }
            Cursor query2 = contentResolver.query(zzc, null, null, new String[]{str}, null);
            if (query2 != null) {
                try {
                    if (query2.moveToFirst()) {
                        String string = query2.getString(1);
                        if (string != null && string.equals(null)) {
                            string = null;
                        }
                        zza(obj, str, string);
                        if (string != null) {
                            str2 = string;
                        }
                        query2.close();
                        return str2;
                    }
                } finally {
                    if (query2 != null) {
                        query2.close();
                    }
                }
            }
            zza(obj, str, (String) null);
            return null;
        }
    }

    public static void zza(Object obj, String str, String str2) {
        synchronized (zzfib.class) {
            if (obj == zzk) {
                zzf.put(str, str2);
            }
        }
    }

    public static <T> T zza(HashMap<String, T> hashMap, String str, T t) {
        synchronized (zzfib.class) {
            if (!hashMap.containsKey(str)) {
                return null;
            }
            T t2 = hashMap.get(str);
            if (t2 != null) {
                t = t2;
            }
            return t;
        }
    }

    public static <T> void zza(Object obj, HashMap<String, T> hashMap, String str, T t) {
        synchronized (zzfib.class) {
            if (obj == zzk) {
                hashMap.put(str, t);
                zzf.remove(str);
            }
        }
    }
}
