package androidx.viewpager2.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.util.Log;
import androidx.collection.ArrayMap;
import androidx.preference.R$layout;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.Rpc;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class FakeDrag {
    public static FakeDrag zza$com$google$android$gms$gcm$GcmNetworkManager;
    public final Object mRecyclerView;
    public final Object mScrollEventAdapter;
    public Object mViewPager;

    public FakeDrag(Context context) {
        this.mRecyclerView = new ArrayMap();
        this.mViewPager = context;
        this.mScrollEventAdapter = PendingIntent.getBroadcast(context, 0, new Intent().setPackage("com.google.example.invalidpackage"), 0);
    }

    public static FakeDrag getInstance(Context context) {
        FakeDrag fakeDrag;
        synchronized (FakeDrag.class) {
            if (zza$com$google$android$gms$gcm$GcmNetworkManager == null) {
                zza$com$google$android$gms$gcm$GcmNetworkManager = new FakeDrag(context.getApplicationContext());
            }
            fakeDrag = zza$com$google$android$gms$gcm$GcmNetworkManager;
        }
        return fakeDrag;
    }

    public Intent zza() {
        String findIidPackage = Rpc.findIidPackage((Context) this.mViewPager);
        int zza = findIidPackage != null ? GoogleCloudMessaging.zza((Context) this.mViewPager) : -1;
        if (findIidPackage == null || zza < 5000000) {
            StringBuilder sb = new StringBuilder(91);
            sb.append("Google Play Services is not available, dropping GcmNetworkManager request. code=");
            sb.append(zza);
            Log.e("GcmNetworkManager", sb.toString());
            return null;
        }
        Intent intent = new Intent("com.google.android.gms.gcm.ACTION_SCHEDULE");
        intent.setPackage(findIidPackage);
        intent.putExtra("app", (PendingIntent) this.mScrollEventAdapter);
        intent.putExtra("source", 4);
        intent.putExtra("source_version", 12529000);
        return intent;
    }

    public synchronized void zzb(String str, String str2) {
        Map map = (Map) ((Map) this.mRecyclerView).get(str2);
        if (map != null) {
            if ((map.remove(str) != null) && map.isEmpty()) {
                ((Map) this.mRecyclerView).remove(str2);
            }
        }
    }

    public boolean zzc(String str) {
        boolean z;
        R$layout.zza(str, "GcmTaskService must not be null.");
        List<ResolveInfo> zzd = zzd(str);
        if (zzd == null) {
            z = true;
        } else {
            z = zzd.isEmpty();
        }
        if (z) {
            Log.e("GcmNetworkManager", str.concat(" is not available. This may cause the task to be lost."));
            return true;
        }
        for (ResolveInfo resolveInfo : zzd) {
            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            if (serviceInfo != null && serviceInfo.enabled) {
                return true;
            }
        }
        throw new IllegalArgumentException(FakeDrag$$ExternalSyntheticOutline0.m(str.length() + 118, "The GcmTaskService class you provided ", str, " does not seem to support receiving com.google.android.gms.gcm.ACTION_TASK_READY"));
    }

    public List<ResolveInfo> zzd(String str) {
        PackageManager packageManager = ((Context) this.mViewPager).getPackageManager();
        if (packageManager == null) {
            return Collections.emptyList();
        }
        return packageManager.queryIntentServices(new Intent("com.google.android.gms.gcm.ACTION_TASK_READY").setClassName((Context) this.mViewPager, str), 0);
    }

    public synchronized boolean zzb(String str) {
        return ((Map) this.mRecyclerView).containsKey(str);
    }

    public FakeDrag(ViewPager2 viewPager2, ScrollEventAdapter scrollEventAdapter, RecyclerView recyclerView) {
        this.mViewPager = viewPager2;
        this.mScrollEventAdapter = scrollEventAdapter;
        this.mRecyclerView = recyclerView;
    }

    public synchronized boolean zzc(String str, String str2) {
        Map map = (Map) ((Map) this.mRecyclerView).get(str2);
        if (map == null) {
            return false;
        }
        Boolean bool = (Boolean) map.get(str);
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    public synchronized boolean zza(String str, String str2) {
        Map map;
        map = (Map) ((Map) this.mRecyclerView).get(str2);
        if (map == null) {
            map = new ArrayMap();
            ((Map) this.mRecyclerView).put(str2, map);
        }
        return map.put(str, Boolean.FALSE) == null;
    }
}
