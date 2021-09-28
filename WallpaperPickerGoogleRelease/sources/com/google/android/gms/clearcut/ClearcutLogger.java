package com.google.android.gms.clearcut;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.gms.clearcut.internal.zzb;
import com.google.android.gms.clearcut.internal.zzs;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.zzh;
import com.google.android.gms.internal.zzgsv;
import com.google.android.gms.phenotype.ExperimentTokens;
import java.util.Objects;
import java.util.TimeZone;
/* loaded from: classes.dex */
public final class ClearcutLogger {
    @Deprecated
    public static final Api<?> API = new Api<>("ClearcutLogger.API", new zzd(), new Api.ClientKey());
    public static final ExperimentTokens[] zzc = new ExperimentTokens[0];
    public static final String[] zzd = new String[0];
    public static final byte[][] zze = new byte[0];
    public final String zzf;
    public final int zzg;
    public String zzh;
    public int zzi;
    public String zzj;
    public final ClearcutLoggerApi zzn;
    public final Clock zzo;
    public TimeZoneOffsetProvider zzp;
    public final LogSampler zzq;

    /* loaded from: classes.dex */
    public class LogEventBuilder {
        public int zza;
        public String zzb;
        public String zzc;
        public String zzd;
        public final MessageProducer zzf;
        public final zzgsv zzn;
        public boolean zzm = true;
        public boolean zzo = false;

        public LogEventBuilder(byte[] bArr, MessageProducer messageProducer) {
            this.zza = ClearcutLogger.this.zzi;
            this.zzb = ClearcutLogger.this.zzh;
            this.zzc = ClearcutLogger.this.zzj;
            this.zzd = null;
            zzgsv zzgsv = new zzgsv();
            this.zzn = zzgsv;
            this.zzc = ClearcutLogger.this.zzj;
            this.zzd = null;
            Objects.requireNonNull((zzh) ClearcutLogger.this.zzo);
            zzgsv.zza = System.currentTimeMillis();
            Objects.requireNonNull((zzh) ClearcutLogger.this.zzo);
            zzgsv.zzb = SystemClock.elapsedRealtime();
            TimeZoneOffsetProvider timeZoneOffsetProvider = ClearcutLogger.this.zzp;
            long j = zzgsv.zza;
            Objects.requireNonNull(timeZoneOffsetProvider);
            zzgsv.zzg = (long) (TimeZone.getDefault().getOffset(j) / 1000);
            if (bArr != null) {
                zzgsv.zzf = bArr;
            }
            this.zzf = messageProducer;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:113:0x021a, code lost:
            if (r2 < r10) goto L_0x021c;
         */
        /* JADX WARNING: Removed duplicated region for block: B:117:0x0221  */
        /* JADX WARNING: Removed duplicated region for block: B:119:0x0236  */
        /* JADX WARNING: Removed duplicated region for block: B:73:0x015b  */
        @java.lang.Deprecated
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.google.android.gms.common.api.PendingResult<com.google.android.gms.common.api.Status> logAsync() {
            /*
            // Method dump skipped, instructions count: 621
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.clearcut.ClearcutLogger.LogEventBuilder.logAsync():com.google.android.gms.common.api.PendingResult");
        }
    }

    /* loaded from: classes.dex */
    public interface LogSampler {
    }

    /* loaded from: classes.dex */
    public interface MessageProducer {
    }

    /* loaded from: classes.dex */
    public static class TimeZoneOffsetProvider {
    }

    public ClearcutLogger(Context context, String str, String str2) {
        zzb zzb = new zzb(context);
        zzh zzh = zzh.zza;
        zzs zzs = new zzs(context);
        this.zzi = -1;
        this.zzf = context.getPackageName();
        int i = 0;
        try {
            i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.wtf("ClearcutLogger", "This can't happen.", e);
        }
        this.zzg = i;
        this.zzi = -1;
        this.zzh = str;
        this.zzj = null;
        this.zzn = zzb;
        this.zzo = zzh;
        this.zzp = new TimeZoneOffsetProvider();
        this.zzq = zzs;
    }
}
