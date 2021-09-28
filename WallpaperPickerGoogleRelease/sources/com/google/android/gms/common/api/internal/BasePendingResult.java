package com.google.android.gms.common.api.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import androidx.appcompat.R$dimen$$ExternalSyntheticOutline0;
import androidx.preference.R$layout;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
@KeepName
/* loaded from: classes.dex */
public abstract class BasePendingResult<R extends Result> extends PendingResult<R> {
    public static final ThreadLocal<Boolean> zzc = new zzt();
    @KeepName
    public zzb mResultGuardian;
    public final Object zza;
    public final zza<R> zzb;
    public final WeakReference<GoogleApiClient> zzd;
    public final CountDownLatch zze;
    public final ArrayList<PendingResult.zza> zzf;
    public ResultCallback<? super R> zzg;
    public final AtomicReference<zzdu> zzh;
    public R zzi;
    public Status zzj;
    public volatile boolean zzk;
    public boolean zzl;
    public boolean zzm;
    public boolean zzq;

    /* loaded from: classes.dex */
    public static class zza<R extends Result> extends Handler {
        public zza(Looper looper) {
            super(looper);
        }

        /* JADX DEBUG: Multi-variable search result rejected for r2v2, resolved type: com.google.android.gms.common.api.ResultCallback */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                Pair pair = (Pair) message.obj;
                ResultCallback resultCallback = (ResultCallback) pair.first;
                Result result = (Result) pair.second;
                try {
                    resultCallback.onResult(result);
                } catch (RuntimeException e) {
                    BasePendingResult.zzb(result);
                    throw e;
                }
            } else if (i != 2) {
                Log.wtf("BasePendingResult", R$dimen$$ExternalSyntheticOutline0.m(45, "Don't know how to handle message: ", i), new Exception());
            } else {
                ((BasePendingResult) message.obj).zzd(Status.zzd);
            }
        }
    }

    /* loaded from: classes.dex */
    public final class zzb {
        public zzb(zzt zzt) {
        }

        public final void finalize() throws Throwable {
            BasePendingResult.zzb(BasePendingResult.this.zzi);
            super.finalize();
        }
    }

    @Deprecated
    public BasePendingResult() {
        this.zza = new Object();
        this.zze = new CountDownLatch(1);
        this.zzf = new ArrayList<>();
        this.zzh = new AtomicReference<>();
        this.zzq = false;
        this.zzb = new zza<>(Looper.getMainLooper());
        this.zzd = new WeakReference<>(null);
    }

    public static void zzb(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (RuntimeException e) {
                String valueOf = String.valueOf(result);
                StringBuilder sb = new StringBuilder(valueOf.length() + 18);
                sb.append("Unable to release ");
                sb.append(valueOf);
                Log.w("BasePendingResult", sb.toString(), e);
            }
        }
    }

    public abstract R zza(Status status);

    public final void zza(R r) {
        synchronized (this.zza) {
            if (this.zzm || this.zzl) {
                zzb(r);
                return;
            }
            zze();
            boolean z = true;
            R$layout.zza(!zze(), "Results have already been set");
            if (this.zzk) {
                z = false;
            }
            R$layout.zza(z, "Result has already been consumed");
            zzc(r);
        }
    }

    public final void zzc(R r) {
        this.zzi = r;
        this.zze.countDown();
        this.zzj = this.zzi.getStatus();
        if (this.zzl) {
            this.zzg = null;
        } else if (this.zzg != null) {
            this.zzb.removeMessages(2);
            zza<R> zza2 = this.zzb;
            ResultCallback<? super R> resultCallback = this.zzg;
            R zza3 = zza();
            Objects.requireNonNull(zza2);
            zza2.sendMessage(zza2.obtainMessage(1, new Pair(resultCallback, zza3)));
        } else if (this.zzi instanceof Releasable) {
            this.mResultGuardian = new zzb(null);
        }
        ArrayList<PendingResult.zza> arrayList = this.zzf;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            PendingResult.zza zza4 = arrayList.get(i);
            i++;
            zza4.zza(this.zzj);
        }
        this.zzf.clear();
    }

    public final void zzd(Status status) {
        synchronized (this.zza) {
            if (!zze()) {
                zza((BasePendingResult<R>) zza(status));
                this.zzm = true;
            }
        }
    }

    public final boolean zze() {
        return this.zze.getCount() == 0;
    }

    public final void zzg() {
        this.zzq = this.zzq || zzc.get().booleanValue();
    }

    public BasePendingResult(GoogleApiClient googleApiClient) {
        this.zza = new Object();
        this.zze = new CountDownLatch(1);
        this.zzf = new ArrayList<>();
        this.zzh = new AtomicReference<>();
        this.zzq = false;
        this.zzb = new zza<>(googleApiClient != null ? googleApiClient.zzc() : Looper.getMainLooper());
        this.zzd = new WeakReference<>(googleApiClient);
    }

    public final R zza() {
        R r;
        synchronized (this.zza) {
            R$layout.zza(!this.zzk, "Result has already been consumed.");
            R$layout.zza(zze(), "Result is not ready.");
            r = this.zzi;
            this.zzi = null;
            this.zzg = null;
            this.zzk = true;
        }
        zzdu andSet = this.zzh.getAndSet(null);
        if (andSet != null) {
            andSet.zza(this);
        }
        return r;
    }
}
