package com.google.android.gms.clearcut;

import android.os.SystemClock;
import android.util.Log;
import androidx.preference.R$layout;
import com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0;
import com.google.android.gms.clearcut.ClearcutLogger;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zzdh;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.zzh;
import com.google.android.gms.internal.zzgsy;
import com.google.android.gms.internal.zzgsz;
import com.google.android.gms.internal.zzgta;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/* loaded from: classes.dex */
public class Counters {
    public static final Charset zza = Charset.forName("UTF-8");
    public static final Comparator zzs = new zzp(0);
    public final String zzc;
    public final int zzd;
    public final Clock zze;
    public LogEventModifier zzf;
    public long zzk;
    public final ClearcutLogger zzl;
    public final ReentrantReadWriteLock zzm = new ReentrantReadWriteLock();
    public Map<String, AbstractCounter> zzn = new TreeMap();
    public Integer zzp = null;
    public TreeMap<byte[], Integer> zzq = new TreeMap<>(zzs);

    /* loaded from: classes.dex */
    public interface Alias {
    }

    /* loaded from: classes.dex */
    public class BooleanHistogram extends AbstractCounter {
        public BooleanHistogram(Counters counters, BooleanHistogram booleanHistogram, boolean z, zzp zzp) {
            super(counters, booleanHistogram, z);
        }
    }

    /* loaded from: classes.dex */
    public class Counter extends AbstractCounter {
        public Counter(Counters counters, String str, zzp zzp) {
            super(str);
        }

        public Counter(Counters counters, Counter counter, boolean z, zzp zzp) {
            super(counters, counter, z);
        }
    }

    /* loaded from: classes.dex */
    public class IntegerHistogram extends AbstractCounter {
        public IntegerHistogram(Counters counters, String str, zzp zzp) {
            super(str);
        }

        public void increment(int i) {
            incrementBase((long) i, 1);
        }

        public IntegerHistogram(Counters counters, IntegerHistogram integerHistogram, boolean z, zzp zzp) {
            super(counters, integerHistogram, z);
        }
    }

    /* loaded from: classes.dex */
    public interface LogEventModifier {
        ClearcutLogger.LogEventBuilder modify(ClearcutLogger.LogEventBuilder logEventBuilder);
    }

    /* loaded from: classes.dex */
    public class LongHistogram extends zza {
        public LongHistogram(Counters counters, LongHistogram longHistogram, boolean z, zzp zzp) {
            super(counters, longHistogram, z);
        }
    }

    /* loaded from: classes.dex */
    public class TimerHistogram extends zza {
        public TimerHistogram(Counters counters, TimerHistogram timerHistogram, boolean z, zzp zzp) {
            super(counters, timerHistogram, z);
        }
    }

    /* loaded from: classes.dex */
    public class zza extends AbstractCounter {
        public final Alias zzb;

        public zza(Counters counters, zza zza, boolean z) {
            super(counters, zza, z);
            this.zzb = zza.zzb;
        }
    }

    /* loaded from: classes.dex */
    public class zzb implements ClearcutLogger.MessageProducer {
        public final byte[] zza;
        public final Integer zzb;
        public final ArrayList<AbstractCounter> zzc;

        public zzb(byte[] bArr) {
            this.zza = bArr;
            Integer num = Counters.this.zzq.get(bArr);
            this.zzb = num;
            ArrayList<AbstractCounter> arrayList = new ArrayList<>(Counters.this.zzn.size());
            for (AbstractCounter abstractCounter : Counters.this.zzn.values()) {
                if (abstractCounter.zza.containsKey(num)) {
                    arrayList.add(abstractCounter);
                }
            }
            this.zzc = arrayList;
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof zzb)) {
                return false;
            }
            return zza().equals(((zzb) obj).zza());
        }

        public final int hashCode() {
            return 1;
        }

        public final String toString() {
            return zza().toString();
        }

        public final zzgta zza() {
            zzgta zzgta = new zzgta();
            zzgta.zza = Counters.this.zzk;
            byte[] bArr = this.zza;
            if (bArr != null) {
                zzgta.zzc = bArr;
            }
            zzgta.zzb = new zzgsz[this.zzc.size()];
            ArrayList<AbstractCounter> arrayList = this.zzc;
            int size = arrayList.size();
            int i = 0;
            int i2 = 0;
            while (i < size) {
                AbstractCounter abstractCounter = arrayList.get(i);
                i++;
                AbstractCounter abstractCounter2 = abstractCounter;
                zzgsz[] zzgszArr = zzgta.zzb;
                Map<Long, long[]> map = abstractCounter2.zza.get(this.zzb);
                zzgsz zzgsz = new zzgsz();
                String str = abstractCounter2.zzb;
                try {
                    MessageDigest instance = MessageDigest.getInstance("MD5");
                    instance.update(str.getBytes(Counters.zza));
                    zzgsz.zza = ByteBuffer.wrap(instance.digest()).getLong();
                    zzgsz.zzb = new zzgsy[map.size()];
                    int i3 = 0;
                    for (Map.Entry<Long, long[]> entry : map.entrySet()) {
                        zzgsy zzgsy = new zzgsy();
                        zzgsy.zza = entry.getKey().longValue();
                        zzgsy.zzb = entry.getValue()[0];
                        i3++;
                        zzgsz.zzb[i3] = zzgsy;
                    }
                    zzgszArr[i2] = zzgsz;
                    i2++;
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
            return zzgta;
        }
    }

    public Counters(ClearcutLogger clearcutLogger, String str, int i, Clock clock) {
        Objects.requireNonNull(clearcutLogger, "null reference");
        Objects.requireNonNull(str, "null reference");
        R$layout.zzb(i > 0);
        Objects.requireNonNull(clock, "null reference");
        this.zzl = clearcutLogger;
        this.zzc = str;
        this.zzd = i;
        this.zze = clock;
        this.zzk = SystemClock.elapsedRealtime();
    }

    public Counter getCounter(String str) {
        this.zzm.writeLock().lock();
        try {
            AbstractCounter abstractCounter = this.zzn.get(str);
            if (abstractCounter == null) {
                this.zzm.writeLock().lock();
                Counter counter = new Counter(this, str, null);
                this.zzm.writeLock().unlock();
                return counter;
            }
            try {
                return (Counter) abstractCounter;
            } catch (ClassCastException unused) {
                String valueOf = String.valueOf(str);
                throw new IllegalArgumentException(valueOf.length() != 0 ? "another type of counter exists with name: ".concat(valueOf) : new String("another type of counter exists with name: "));
            }
        } finally {
            this.zzm.writeLock().unlock();
        }
    }

    public IntegerHistogram getIntegerHistogram(String str) {
        this.zzm.writeLock().lock();
        try {
            AbstractCounter abstractCounter = this.zzn.get(str);
            if (abstractCounter == null) {
                this.zzm.writeLock().lock();
                IntegerHistogram integerHistogram = new IntegerHistogram(this, str, null);
                this.zzm.writeLock().unlock();
                return integerHistogram;
            }
            try {
                return (IntegerHistogram) abstractCounter;
            } catch (ClassCastException unused) {
                throw new IllegalArgumentException(str.length() != 0 ? "another type of counter exists with name: ".concat(str) : new String("another type of counter exists with name: "));
            }
        } finally {
            this.zzm.writeLock().unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    public void logAllAsync(LogEventModifier logEventModifier) {
        this.zzm.writeLock().lock();
        try {
            Counters snapshotAndReset = snapshotAndReset();
            this.zzm.writeLock().unlock();
            Set<byte[]> keySet = snapshotAndReset.zzq.keySet();
            int size = keySet.size();
            ClearcutLogger.MessageProducer[] messageProducerArr = new ClearcutLogger.MessageProducer[size];
            int i = 0;
            for (byte[] bArr : keySet) {
                messageProducerArr[i] = new zzb(bArr);
                i++;
            }
            PendingResult<Status> pendingResult = null;
            for (int i2 = 0; i2 < size; i2++) {
                ClearcutLogger.MessageProducer messageProducer = messageProducerArr[i2];
                ClearcutLogger clearcutLogger = snapshotAndReset.zzl;
                Objects.requireNonNull(clearcutLogger);
                ClearcutLogger.LogEventBuilder logEventBuilder = new ClearcutLogger.LogEventBuilder(null, messageProducer);
                logEventBuilder.zzb = snapshotAndReset.zzc;
                if (logEventModifier != null) {
                    logEventBuilder = logEventModifier.modify(logEventBuilder);
                }
                pendingResult = logEventBuilder.logAsync();
            }
            if (pendingResult == null) {
                Status status = Status.zza;
                R$layout.zza(status, "Result must not be null");
                new zzdh(null).zza((zzdh) status);
            }
        } catch (Throwable th) {
            this.zzm.writeLock().unlock();
            throw th;
        }
    }

    public Counters snapshotAndReset() {
        Counters counters = new Counters(this.zzl, this.zzc, this.zzd, this.zze);
        ReentrantReadWriteLock.WriteLock writeLock = this.zzm.writeLock();
        writeLock.lock();
        try {
            counters.zzp = this.zzp;
            counters.zzk = this.zzk;
            counters.zzf = this.zzf;
            counters.zzn = new TreeMap();
            for (Map.Entry<String, AbstractCounter> entry : this.zzn.entrySet()) {
                counters.zzn.put(entry.getKey(), counters.zza(entry.getValue(), true));
            }
            TreeMap<byte[], Integer> treeMap = counters.zzq;
            counters.zzq = this.zzq;
            this.zzq = treeMap;
            this.zzp = null;
            Objects.requireNonNull((zzh) counters.zze);
            this.zzk = SystemClock.elapsedRealtime();
            return counters;
        } finally {
            writeLock.unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.zzm.readLock().lock();
        try {
            sb.append("{");
            for (Map.Entry<byte[], Integer> entry : this.zzq.entrySet()) {
                sb.append(entry.getKey() == null ? "null" : new String(entry.getKey()));
                sb.append(", ");
            }
            sb.append("}\n");
            for (AbstractCounter abstractCounter : this.zzn.values()) {
                sb.append(abstractCounter.toString());
                sb.append("\n");
            }
            this.zzm.readLock().unlock();
            return sb.toString();
        } catch (Throwable th) {
            this.zzm.readLock().unlock();
            throw th;
        }
    }

    public final AbstractCounter zza(AbstractCounter abstractCounter, boolean z) {
        if (abstractCounter instanceof Counter) {
            return new Counter(this, (Counter) abstractCounter, z, null);
        }
        if (abstractCounter instanceof TimerHistogram) {
            return new TimerHistogram(this, (TimerHistogram) abstractCounter, z, null);
        }
        if (abstractCounter instanceof IntegerHistogram) {
            return new IntegerHistogram(this, (IntegerHistogram) abstractCounter, z, null);
        }
        if (abstractCounter instanceof LongHistogram) {
            return new LongHistogram(this, (LongHistogram) abstractCounter, z, null);
        }
        if (abstractCounter instanceof BooleanHistogram) {
            return new BooleanHistogram(this, (BooleanHistogram) abstractCounter, z, null);
        }
        String valueOf = String.valueOf(abstractCounter);
        throw new IllegalArgumentException(Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(valueOf.length() + 21, "Unkown counter type: ", valueOf));
    }

    /* loaded from: classes.dex */
    public abstract class AbstractCounter {
        public Map<Integer, Map<Long, long[]>> zza;
        public final String zzb;
        public int zzc;
        public final Object zze;

        public AbstractCounter(Counters counters, AbstractCounter abstractCounter, boolean z) {
            this(abstractCounter.zzb);
            synchronized (abstractCounter.zze) {
                this.zzc = abstractCounter.zzc;
                if (z) {
                    Map<Integer, Map<Long, long[]>> map = this.zza;
                    this.zza = abstractCounter.zza;
                    abstractCounter.zza = map;
                    abstractCounter.zzc = 0;
                    return;
                }
                this.zza = new HashMap(abstractCounter.zza.size());
                for (Map.Entry<Integer, Map<Long, long[]>> entry : abstractCounter.zza.entrySet()) {
                    HashMap hashMap = new HashMap(entry.getValue().size());
                    for (Map.Entry<Long, long[]> entry2 : entry.getValue().entrySet()) {
                        hashMap.put(entry2.getKey(), new long[]{entry2.getValue()[0]});
                    }
                    this.zza.put(entry.getKey(), hashMap);
                }
            }
        }

        public final void incrementBase(long j, long j2) {
            boolean z;
            Counters.this.zzm.readLock().lock();
            try {
                if (Counters.this.zzp == null) {
                    z = true;
                } else {
                    zzb(j, j2);
                    z = false;
                }
                if (z) {
                    zza(j, j2);
                }
                Objects.requireNonNull(Counters.this);
            } finally {
                Counters.this.zzm.readLock().unlock();
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("AbstractCounter");
            sb.append("(");
            sb.append(this.zzb);
            sb.append(")[");
            synchronized (this.zze) {
                for (Map.Entry<Integer, Map<Long, long[]>> entry : this.zza.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append(" -> [");
                    for (Map.Entry<Long, long[]> entry2 : entry.getValue().entrySet()) {
                        sb.append(entry2.getKey());
                        sb.append(" = ");
                        sb.append(entry2.getValue()[0]);
                        sb.append(", ");
                    }
                    sb.append("], ");
                }
            }
            sb.append("]");
            return sb.toString();
        }

        public final boolean zza(long j, long j2) {
            Lock writeLock = Counters.this.zzm.writeLock();
            writeLock.lock();
            try {
                Counters counters = Counters.this;
                Objects.requireNonNull(counters);
                Integer num = counters.zzq.get(null);
                if (num == null) {
                    num = Integer.valueOf(counters.zzq.size());
                    counters.zzq.put(null, num);
                }
                counters.zzp = num;
                Counters.this.zzm.readLock().lock();
                writeLock.unlock();
                writeLock = Counters.this.zzm.readLock();
                zzb(j, j2);
                return false;
            } finally {
                writeLock.unlock();
            }
        }

        public final boolean zzb(long j, long j2) {
            synchronized (this.zze) {
                Map<Long, long[]> map = this.zza.get(Counters.this.zzp);
                if (map == null) {
                    map = new HashMap<>();
                    this.zza.put(Counters.this.zzp, map);
                }
                int i = this.zzc;
                int i2 = Counters.this.zzd;
                if (i >= i2) {
                    if (i == i2) {
                        String valueOf = String.valueOf(this.zzb);
                        Log.i("Counters", valueOf.length() != 0 ? "exceeded sample count in ".concat(valueOf) : new String("exceeded sample count in "));
                    }
                    return false;
                }
                this.zzc = i + 1;
                long[] jArr = map.get(Long.valueOf(j));
                if (jArr == null) {
                    jArr = new long[]{0};
                    map.put(Long.valueOf(j), jArr);
                }
                jArr[0] = jArr[0] + j2;
                Objects.requireNonNull(Counters.this);
                return false;
            }
        }

        public AbstractCounter(String str) {
            int i = Counters.this.zzd;
            this.zza = new HashMap();
            this.zze = new Object();
            if (Counters.this.zzn.containsKey(str)) {
                String valueOf = String.valueOf(str);
                throw new IllegalStateException(valueOf.length() != 0 ? "counter/histogram already exists: ".concat(valueOf) : new String("counter/histogram already exists: "));
            }
            Counters.this.zzn.put(str, this);
            this.zzb = str;
        }
    }
}
