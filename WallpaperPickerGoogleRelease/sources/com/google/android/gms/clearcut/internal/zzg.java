package com.google.android.gms.clearcut.internal;

import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.clearcut.ClearcutLogger;
import com.google.android.gms.clearcut.Counters;
import com.google.android.gms.clearcut.LogEventParcelable;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zzn;
import com.google.android.gms.internal.zzgrz;
import com.google.android.gms.internal.zzgsv;
/* loaded from: classes.dex */
public final class zzg extends zzn<Status, zzi> {
    public final LogEventParcelable zza;

    public zzg(LogEventParcelable logEventParcelable, GoogleApiClient googleApiClient) {
        super(ClearcutLogger.API, googleApiClient);
        this.zza = logEventParcelable;
    }

    @Override // com.google.android.gms.common.api.internal.BasePendingResult
    public final /* synthetic */ Result zza(Status status) {
        return status;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [com.google.android.gms.common.api.Api$zzb] */
    @Override // com.google.android.gms.common.api.internal.zzn
    public final void zza(zzi zzi) throws RemoteException {
        zzi zzi2 = zzi;
        zzh zzh = new zzh(this);
        try {
            LogEventParcelable logEventParcelable = this.zza;
            ClearcutLogger.MessageProducer messageProducer = logEventParcelable.extensionProducer;
            if (messageProducer != null) {
                zzgsv zzgsv = logEventParcelable.logEvent;
                if (zzgsv.zzf.length == 0) {
                    zzgsv.zzf = zzgrz.toByteArray(((Counters.zzb) messageProducer).zza());
                }
            }
            ClearcutLogger.MessageProducer messageProducer2 = logEventParcelable.clientVisualElementsProducer;
            if (messageProducer2 != null) {
                zzgsv zzgsv2 = logEventParcelable.logEvent;
                if (zzgsv2.zzh.length == 0) {
                    zzgsv2.zzh = zzgrz.toByteArray(((Counters.zzb) messageProducer2).zza());
                }
            }
            logEventParcelable.logEventBytes = zzgrz.toByteArray(logEventParcelable.logEvent);
            ((zzq) zzi2.zzag()).zza(zzh, this.zza);
        } catch (RuntimeException e) {
            Log.e("ClearcutLoggerApiImpl", "derived ClearcutLogger.MessageProducer ", e);
            zzc(new Status(10, "MessageProducer"));
        }
    }
}
