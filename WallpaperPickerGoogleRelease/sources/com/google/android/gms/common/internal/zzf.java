package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import androidx.collection.SimpleArrayMap;
import com.android.systemui.shared.R;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.util.zzi;
import com.google.android.gms.internal.zzbmj;
import com.google.android.gms.internal.zzbmk;
/* loaded from: classes.dex */
public final class zzf {
    public static final SimpleArrayMap<String, String> zza = new SimpleArrayMap<>();

    public static String zza(Context context, int i) {
        Resources resources = context.getResources();
        switch (i) {
            case 1:
                return resources.getString(R.string.common_google_play_services_install_title);
            case 2:
                return resources.getString(R.string.common_google_play_services_update_title);
            case 3:
                return resources.getString(R.string.common_google_play_services_enable_title);
            case 4:
            case 6:
            case 18:
                return null;
            case 5:
                Log.e("GoogleApiAvailability", "An invalid account was specified when connecting. Please provide a valid account.");
                return zza(context, "common_google_play_services_invalid_account_title");
            case 7:
                Log.e("GoogleApiAvailability", "Network error occurred. Please retry request later.");
                return zza(context, "common_google_play_services_network_error_title");
            case 8:
                Log.e("GoogleApiAvailability", "Internal error occurred. Please see logs for detailed information");
                return null;
            case 9:
                Log.e("GoogleApiAvailability", "Google Play services is invalid. Cannot recover.");
                return null;
            case 10:
                Log.e("GoogleApiAvailability", "Developer error occurred. Please see logs for detailed information");
                return null;
            case 11:
                Log.e("GoogleApiAvailability", "The application is not licensed to the user.");
                return null;
            case 12:
            case 13:
            case 14:
            case 15:
            case 19:
            default:
                StringBuilder sb = new StringBuilder(33);
                sb.append("Unexpected error code ");
                sb.append(i);
                Log.e("GoogleApiAvailability", sb.toString());
                return null;
            case 16:
                Log.e("GoogleApiAvailability", "One of the API components you attempted to connect to is not available.");
                return null;
            case 17:
                Log.e("GoogleApiAvailability", "The specified account could not be signed in.");
                return zza(context, "common_google_play_services_sign_in_failed_title");
            case 20:
                Log.e("GoogleApiAvailability", "The current user profile is restricted and could not use authenticated features.");
                return zza(context, "common_google_play_services_restricted_profile_title");
        }
    }

    public static String zzb(Context context) {
        String packageName = context.getPackageName();
        try {
            zzbmj zza2 = zzbmk.zza(context);
            return zza2.zza.getPackageManager().getApplicationLabel(zza2.zza.getPackageManager().getApplicationInfo(packageName, 0)).toString();
        } catch (PackageManager.NameNotFoundException | NullPointerException unused) {
            String str = context.getApplicationInfo().name;
            return TextUtils.isEmpty(str) ? packageName : str;
        }
    }

    public static String zzc(Context context, int i) {
        Resources resources = context.getResources();
        String zzb = zzb(context);
        if (i == 1) {
            return resources.getString(R.string.common_google_play_services_install_text, zzb);
        }
        if (i != 2) {
            if (i == 3) {
                return resources.getString(R.string.common_google_play_services_enable_text, zzb);
            }
            if (i == 5) {
                return zza(context, "common_google_play_services_invalid_account_text", zzb);
            }
            if (i == 7) {
                return zza(context, "common_google_play_services_network_error_text", zzb);
            }
            if (i == 9) {
                return resources.getString(R.string.common_google_play_services_unsupported_text, zzb);
            }
            if (i == 20) {
                return zza(context, "common_google_play_services_restricted_profile_text", zzb);
            }
            switch (i) {
                case 16:
                    return zza(context, "common_google_play_services_api_unavailable_text", zzb);
                case 17:
                    return zza(context, "common_google_play_services_sign_in_failed_text", zzb);
                case 18:
                    return resources.getString(R.string.common_google_play_services_updating_text, zzb);
                default:
                    return resources.getString(R.string.common_google_play_services_unknown_issue, zzb);
            }
        } else if (zzi.zzb(context)) {
            return resources.getString(R.string.common_google_play_services_wear_update_text);
        } else {
            return resources.getString(R.string.common_google_play_services_update_text, zzb);
        }
    }

    public static String zza(Context context, String str, String str2) {
        Resources resources = context.getResources();
        String zza2 = zza(context, str);
        if (zza2 == null) {
            zza2 = resources.getString(R.string.common_google_play_services_unknown_issue);
        }
        return String.format(resources.getConfiguration().locale, zza2, str2);
    }

    public static String zza(Context context, String str) {
        Resources resources;
        SimpleArrayMap<String, String> simpleArrayMap = zza;
        synchronized (simpleArrayMap) {
            String orDefault = simpleArrayMap.getOrDefault(str, null);
            if (orDefault != null) {
                return orDefault;
            }
            int i = GooglePlayServicesUtil.$r8$clinit;
            try {
                resources = context.getPackageManager().getResourcesForApplication("com.google.android.gms");
            } catch (PackageManager.NameNotFoundException unused) {
                resources = null;
            }
            if (resources == null) {
                return null;
            }
            int identifier = resources.getIdentifier(str, "string", "com.google.android.gms");
            if (identifier == 0) {
                String valueOf = String.valueOf(str);
                Log.w("GoogleApiAvailability", valueOf.length() != 0 ? "Missing resource: ".concat(valueOf) : new String("Missing resource: "));
                return null;
            }
            String string = resources.getString(identifier);
            if (TextUtils.isEmpty(string)) {
                String valueOf2 = String.valueOf(str);
                Log.w("GoogleApiAvailability", valueOf2.length() != 0 ? "Got empty resource: ".concat(valueOf2) : new String("Got empty resource: "));
                return null;
            }
            zza.put(str, string);
            return string;
        }
    }
}
