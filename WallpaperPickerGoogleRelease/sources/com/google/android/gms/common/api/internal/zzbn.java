package com.google.android.gms.common.api.internal;

import android.app.ActivityManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import androidx.collection.ArrayMap;
import androidx.collection.ArraySet;
import androidx.collection.IndexBasedArrayIterator;
import androidx.preference.R$layout;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzv;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public final class zzbn implements Handler.Callback {
    public static final Status zza = new Status(4, "Sign-out occurred while this API call was in progress.");
    public static final Status zzb = new Status(4, "The user must be signed in to make this API call.");
    public static final Object zzf = new Object();
    public static zzbn zzg;
    public final Context zzh;
    public final GoogleApiAvailability zzi;
    public final zzv zzj;
    public final Handler zzq;
    public long zze = 10000;
    public final AtomicInteger zzk = new AtomicInteger(1);
    public final AtomicInteger zzl = new AtomicInteger(0);
    public final Map<zzi<?>, zzbp<?>> zzm = new ConcurrentHashMap(5, 0.75f, 1);
    public final Set<zzi<?>> zzo = new ArraySet(0);
    public final Set<zzi<?>> zzp = new ArraySet(0);

    public zzbn(Context context, Looper looper, GoogleApiAvailability googleApiAvailability) {
        this.zzh = context;
        Handler handler = new Handler(looper, this);
        this.zzq = handler;
        this.zzi = googleApiAvailability;
        this.zzj = new zzv(googleApiAvailability);
        handler.sendMessage(handler.obtainMessage(6));
    }

    public static zzbn zza(Context context) {
        zzbn zzbn;
        synchronized (zzf) {
            if (zzg == null) {
                HandlerThread handlerThread = new HandlerThread("GoogleApiHandler", 9);
                handlerThread.start();
                Looper looper = handlerThread.getLooper();
                Context applicationContext = context.getApplicationContext();
                Object obj = GoogleApiAvailability.zza;
                zzg = new zzbn(applicationContext, looper, GoogleApiAvailability.zzb);
            }
            zzbn = zzg;
        }
        return zzbn;
    }

    @Override // android.os.Handler.Callback
    public final boolean handleMessage(Message message) {
        Status status;
        int i = message.what;
        long j = 300000;
        boolean z = false;
        zzbp<?> zzbp = null;
        switch (i) {
            case 1:
                if (((Boolean) message.obj).booleanValue()) {
                    j = 10000;
                }
                this.zze = j;
                this.zzq.removeMessages(12);
                for (zzi<?> zzi : this.zzm.keySet()) {
                    Handler handler = this.zzq;
                    handler.sendMessageDelayed(handler.obtainMessage(12, zzi), this.zze);
                }
                break;
            case 2:
                zzk zzk = (zzk) message.obj;
                Iterator it = ((ArrayMap.KeySet) zzk.zza.keySet()).iterator();
                while (true) {
                    IndexBasedArrayIterator indexBasedArrayIterator = (IndexBasedArrayIterator) it;
                    if (!indexBasedArrayIterator.hasNext()) {
                        break;
                    } else {
                        zzi<?> zzi2 = (zzi) indexBasedArrayIterator.next();
                        zzbp<?> zzbp2 = this.zzm.get(zzi2);
                        if (zzbp2 == null) {
                            zzk.zza(zzi2, new ConnectionResult(13), null);
                            break;
                        } else if (zzbp2.zzc.isConnected()) {
                            zzk.zza(zzi2, ConnectionResult.zza, zzbp2.zzc.zzab());
                        } else {
                            R$layout.zza(zzbp2.zza.zzq);
                            if (zzbp2.zzm != null) {
                                R$layout.zza(zzbp2.zza.zzq);
                                zzk.zza(zzi2, zzbp2.zzm, null);
                            } else {
                                R$layout.zza(zzbp2.zza.zzq);
                                zzbp2.zzg.add(zzk);
                            }
                        }
                    }
                }
            case 3:
                for (zzbp<?> zzbp3 : this.zzm.values()) {
                    zzbp3.zzd();
                    zzbp3.zzi();
                }
                break;
            case 4:
            case 8:
            case 13:
                zzcu zzcu = (zzcu) message.obj;
                zzbp<?> zzbp4 = this.zzm.get(zzcu.zzc.zze);
                if (zzbp4 == null) {
                    zzb(zzcu.zzc);
                    zzbp4 = this.zzm.get(zzcu.zzc.zze);
                }
                if (!zzbp4.zzk() || this.zzl.get() == zzcu.zzb) {
                    zzbp4.zza(zzcu.zza);
                    break;
                } else {
                    zzcu.zza.zza(zza);
                    zzbp4.zza();
                    break;
                }
            case 5:
                int i2 = message.arg1;
                ConnectionResult connectionResult = (ConnectionResult) message.obj;
                Iterator<zzbp<?>> it2 = this.zzm.values().iterator();
                while (true) {
                    if (it2.hasNext()) {
                        zzbp<?> next = it2.next();
                        if (next.zzi == i2) {
                            zzbp = next;
                        }
                    }
                }
                if (zzbp != null) {
                    GoogleApiAvailability googleApiAvailability = this.zzi;
                    int i3 = connectionResult.zzc;
                    Objects.requireNonNull(googleApiAvailability);
                    AtomicBoolean atomicBoolean = GooglePlayServicesUtilLight.zza;
                    String zza2 = ConnectionResult.zza(i3);
                    String str = connectionResult.zze;
                    zzbp.zza(new Status(17, Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1.m(XMPPathFactory$$ExternalSyntheticOutline0.m(str, XMPPathFactory$$ExternalSyntheticOutline0.m(zza2, 69)), "Error resolution was canceled by the user, original error message: ", zza2, ": ", str)));
                    break;
                } else {
                    StringBuilder sb = new StringBuilder(76);
                    sb.append("Could not find API instance ");
                    sb.append(i2);
                    sb.append(" while trying to fail enqueued calls.");
                    Log.wtf("GoogleApiManager", sb.toString(), new Exception());
                    break;
                }
            case 6:
                if (this.zzh.getApplicationContext() instanceof Application) {
                    Application application = (Application) this.zzh.getApplicationContext();
                    zzl zzl = zzl.zza;
                    synchronized (zzl) {
                        if (!zzl.zze) {
                            application.registerActivityLifecycleCallbacks(zzl);
                            application.registerComponentCallbacks(zzl);
                            zzl.zze = true;
                        }
                    }
                    zzbo zzbo = new zzbo(this);
                    synchronized (zzl) {
                        zzl.zzd.add(zzbo);
                    }
                    if (!zzl.zzc.get()) {
                        ActivityManager.RunningAppProcessInfo runningAppProcessInfo = new ActivityManager.RunningAppProcessInfo();
                        ActivityManager.getMyMemoryState(runningAppProcessInfo);
                        if (!zzl.zzc.getAndSet(true) && runningAppProcessInfo.importance > 100) {
                            zzl.zzb.set(true);
                        }
                    }
                    if (!zzl.zzb.get()) {
                        this.zze = 300000;
                        break;
                    }
                }
                break;
            case 7:
                zzb((GoogleApi) message.obj);
                break;
            case 9:
                if (this.zzm.containsKey(message.obj)) {
                    zzbp<?> zzbp5 = this.zzm.get(message.obj);
                    R$layout.zza(zzbp5.zza.zzq);
                    if (zzbp5.zzk) {
                        zzbp5.zzi();
                        break;
                    }
                }
                break;
            case 10:
                for (zzi<?> zzi3 : this.zzp) {
                    this.zzm.remove(zzi3).zza();
                }
                this.zzp.clear();
                break;
            case 11:
                if (this.zzm.containsKey(message.obj)) {
                    zzbp<?> zzbp6 = this.zzm.get(message.obj);
                    R$layout.zza(zzbp6.zza.zzq);
                    if (zzbp6.zzk) {
                        zzbp6.zzp();
                        zzbn zzbn = zzbp6.zza;
                        if (zzbn.zzi.isGooglePlayServicesAvailable(zzbn.zzh) == 18) {
                            status = new Status(8, "Connection timed out while waiting for Google Play services update to complete.");
                        } else {
                            status = new Status(8, "API failed to connect while resuming due to an unknown error.");
                        }
                        zzbp6.zza(status);
                        zzbp6.zzc.disconnect();
                        break;
                    }
                }
                break;
            case 12:
                if (this.zzm.containsKey(message.obj)) {
                    zzbp<?> zzbp7 = this.zzm.get(message.obj);
                    R$layout.zza(zzbp7.zza.zzq);
                    if (zzbp7.zzc.isConnected() && zzbp7.zzh.size() == 0) {
                        zzaf zzaf = zzbp7.zzf;
                        if (!zzaf.zza.isEmpty() || !zzaf.zzb.isEmpty()) {
                            z = true;
                        }
                        if (z) {
                            zzbp7.zzq();
                            break;
                        } else {
                            zzbp7.zzc.disconnect();
                            break;
                        }
                    }
                }
                break;
            default:
                StringBuilder sb2 = new StringBuilder(31);
                sb2.append("Unknown message id: ");
                sb2.append(i);
                Log.w("GoogleApiManager", sb2.toString());
                return false;
        }
        return true;
    }

    public final void zzb(GoogleApi<?> googleApi) {
        zzi<?> zzi = googleApi.zze;
        zzbp<?> zzbp = this.zzm.get(zzi);
        if (zzbp == null) {
            zzbp = new zzbp<>(this, googleApi);
            this.zzm.put(zzi, zzbp);
        }
        if (zzbp.zzk()) {
            this.zzp.add(zzi);
        }
        zzbp.zzi();
    }

    public final boolean zza(ConnectionResult connectionResult, int i) {
        PendingIntent pendingIntent;
        GoogleApiAvailability googleApiAvailability = this.zzi;
        Context context = this.zzh;
        Objects.requireNonNull(googleApiAvailability);
        if (connectionResult.hasResolution()) {
            pendingIntent = connectionResult.zzd;
        } else {
            pendingIntent = googleApiAvailability.getErrorResolutionPendingIntent(context, connectionResult.zzc, 0, null);
        }
        if (pendingIntent == null) {
            return false;
        }
        int i2 = connectionResult.zzc;
        int i3 = GoogleApiActivity.$r8$clinit;
        Intent intent = new Intent(context, GoogleApiActivity.class);
        intent.putExtra("pending_intent", pendingIntent);
        intent.putExtra("failing_client_id", i);
        intent.putExtra("notify_manager", true);
        googleApiAvailability.zza(context, i2, PendingIntent.getActivity(context, 0, intent, 134217728));
        return true;
    }
}
