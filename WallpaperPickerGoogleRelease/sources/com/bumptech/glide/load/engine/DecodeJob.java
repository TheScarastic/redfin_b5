package com.bumptech.glide.load.engine;

import android.os.SystemClock;
import android.support.v4.app.FragmentTabHost$SavedState$$ExternalSyntheticOutline0;
import android.util.Log;
import androidx.constraintlayout.solver.SolverVariable$Type$r8$EnumUnboxingUtility;
import androidx.core.util.Pools$Pool;
import androidx.preference.R$string$$ExternalSyntheticOutline0;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.data.DataRewinderRegistry;
import com.bumptech.glide.load.engine.DataFetcherGenerator;
import com.bumptech.glide.load.engine.DecodePath;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class DecodeJob<R> implements DataFetcherGenerator.FetcherReadyCallback, Runnable, Comparable<DecodeJob<?>>, FactoryPools.Poolable {
    public Callback<R> callback;
    public Key currentAttemptingKey;
    public Object currentData;
    public DataSource currentDataSource;
    public DataFetcher<?> currentFetcher;
    public volatile DataFetcherGenerator currentGenerator;
    public Key currentSourceKey;
    public Thread currentThread;
    public final DiskCacheProvider diskCacheProvider;
    public DiskCacheStrategy diskCacheStrategy;
    public GlideContext glideContext;
    public int height;
    public volatile boolean isCallbackNotified;
    public volatile boolean isCancelled;
    public EngineKey loadKey;
    public Object model;
    public boolean onlyRetrieveFromCache;
    public Options options;
    public int order;
    public final Pools$Pool<DecodeJob<?>> pool;
    public Priority priority;
    public int runReason;
    public Key signature;
    public int stage;
    public long startFetchTime;
    public int width;
    public final DecodeHelper<R> decodeHelper = new DecodeHelper<>();
    public final List<Throwable> throwables = new ArrayList();
    public final StateVerifier stateVerifier = new StateVerifier.DefaultStateVerifier();
    public final DeferredEncodeManager<?> deferredEncodeManager = new DeferredEncodeManager<>();
    public final ReleaseManager releaseManager = new ReleaseManager();

    /* loaded from: classes.dex */
    public interface Callback<R> {
    }

    /* loaded from: classes.dex */
    public final class DecodeCallback<Z> implements DecodePath.DecodeCallback<Z> {
        public final DataSource dataSource;

        public DecodeCallback(DataSource dataSource) {
            this.dataSource = dataSource;
        }
    }

    /* loaded from: classes.dex */
    public static class DeferredEncodeManager<Z> {
        public ResourceEncoder<Z> encoder;
        public Key key;
        public LockedResource<Z> toEncode;
    }

    /* loaded from: classes.dex */
    public interface DiskCacheProvider {
    }

    /* loaded from: classes.dex */
    public static class ReleaseManager {
        public boolean isEncodeComplete;
        public boolean isFailed;
        public boolean isReleased;

        public final boolean isComplete(boolean z) {
            return (this.isFailed || z || this.isEncodeComplete) && this.isReleased;
        }
    }

    public DecodeJob(DiskCacheProvider diskCacheProvider, Pools$Pool<DecodeJob<?>> pools$Pool) {
        this.diskCacheProvider = diskCacheProvider;
        this.pool = pools$Pool;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // java.lang.Comparable
    public int compareTo(DecodeJob<?> decodeJob) {
        DecodeJob<?> decodeJob2 = decodeJob;
        int ordinal = this.priority.ordinal() - decodeJob2.priority.ordinal();
        return ordinal == 0 ? this.order - decodeJob2.order : ordinal;
    }

    public final <Data> Resource<R> decodeFromData(DataFetcher<?> dataFetcher, Data data, DataSource dataSource) throws GlideException {
        if (data == null) {
            return null;
        }
        try {
            int i = LogTime.$r8$clinit;
            long elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
            Resource<R> decodeFromFetcher = decodeFromFetcher(data, dataSource);
            if (Log.isLoggable("DecodeJob", 2)) {
                String valueOf = String.valueOf(decodeFromFetcher);
                StringBuilder sb = new StringBuilder(valueOf.length() + 15);
                sb.append("Decoded result ");
                sb.append(valueOf);
                logWithTimeAndKey(sb.toString(), elapsedRealtimeNanos, null);
            }
            return decodeFromFetcher;
        } finally {
            dataFetcher.cleanup();
        }
    }

    public final <Data> Resource<R> decodeFromFetcher(Data data, DataSource dataSource) throws GlideException {
        DataRewinder<Data> build;
        LoadPath<Data, ?, R> loadPath = this.decodeHelper.getLoadPath(data.getClass());
        Options options = this.options;
        boolean z = dataSource == DataSource.RESOURCE_DISK_CACHE || this.decodeHelper.isScaleOnlyOrNoTransform;
        Option<Boolean> option = Downsampler.ALLOW_HARDWARE_CONFIG;
        Boolean bool = (Boolean) options.get(option);
        if (bool == null || (bool.booleanValue() && !z)) {
            options = new Options();
            options.putAll(this.options);
            options.values.put(option, Boolean.valueOf(z));
        }
        DataRewinderRegistry dataRewinderRegistry = this.glideContext.registry.dataRewinderRegistry;
        synchronized (dataRewinderRegistry) {
            DataRewinder.Factory<?> factory = dataRewinderRegistry.rewinders.get(data.getClass());
            if (factory == null) {
                Iterator<DataRewinder.Factory<?>> it = dataRewinderRegistry.rewinders.values().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    DataRewinder.Factory<?> next = it.next();
                    if (next.getDataClass().isAssignableFrom(data.getClass())) {
                        factory = next;
                        break;
                    }
                }
            }
            if (factory == null) {
                factory = DataRewinderRegistry.DEFAULT_FACTORY;
            }
            build = factory.build(data);
        }
        try {
            return loadPath.load(build, options, this.width, this.height, new DecodeCallback(dataSource));
        } finally {
            build.cleanup();
        }
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:17:0x0073 */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX DEBUG: Type inference failed for r1v11. Raw type applied. Possible types: com.bumptech.glide.load.engine.Resource<R> */
    public final void decodeFromRetrievedData() {
        Resource resource;
        boolean isComplete;
        if (Log.isLoggable("DecodeJob", 2)) {
            long j = this.startFetchTime;
            String valueOf = String.valueOf(this.currentData);
            String valueOf2 = String.valueOf(this.currentSourceKey);
            String valueOf3 = String.valueOf(this.currentFetcher);
            logWithTimeAndKey("Retrieved data", j, FragmentTabHost$SavedState$$ExternalSyntheticOutline0.m(R$string$$ExternalSyntheticOutline0.m(valueOf3.length() + valueOf2.length() + valueOf.length() + 30, "data: ", valueOf, ", cache key: ", valueOf2), ", fetcher: ", valueOf3));
        }
        LockedResource lockedResource = null;
        try {
            resource = (Resource<R>) decodeFromData(this.currentFetcher, this.currentData, this.currentDataSource);
        } catch (GlideException e) {
            e.setLoggingDetails(this.currentAttemptingKey, this.currentDataSource);
            this.throwables.add(e);
            resource = (Resource<R>) null;
        }
        if (resource != null) {
            DataSource dataSource = this.currentDataSource;
            if (resource instanceof Initializable) {
                ((Initializable) resource).initialize();
            }
            if (this.deferredEncodeManager.toEncode != null) {
                lockedResource = LockedResource.obtain(resource);
                resource = lockedResource;
            }
            setNotifiedOrThrow();
            EngineJob engineJob = (EngineJob) this.callback;
            engineJob.resource = resource;
            engineJob.dataSource = dataSource;
            EngineJob.MAIN_THREAD_HANDLER.obtainMessage(1, engineJob).sendToTarget();
            this.stage = 5;
            try {
                DeferredEncodeManager<?> deferredEncodeManager = this.deferredEncodeManager;
                if (deferredEncodeManager.toEncode != null) {
                    ((Engine.LazyDiskCacheProvider) this.diskCacheProvider).getDiskCache().put(deferredEncodeManager.key, new DataCacheWriter(deferredEncodeManager.encoder, deferredEncodeManager.toEncode, this.options));
                    deferredEncodeManager.toEncode.unlock();
                }
                ReleaseManager releaseManager = this.releaseManager;
                synchronized (releaseManager) {
                    releaseManager.isEncodeComplete = true;
                    isComplete = releaseManager.isComplete(false);
                }
                if (isComplete) {
                    releaseInternal();
                }
            } finally {
                if (lockedResource != null) {
                    lockedResource.unlock();
                }
            }
        } else {
            runGenerators();
        }
    }

    public final DataFetcherGenerator getNextGenerator() {
        int $enumboxing$ordinal = SolverVariable$Type$r8$EnumUnboxingUtility.$enumboxing$ordinal(this.stage);
        if ($enumboxing$ordinal == 1) {
            return new ResourceCacheGenerator(this.decodeHelper, this);
        }
        if ($enumboxing$ordinal == 2) {
            return new DataCacheGenerator(this.decodeHelper, this);
        }
        if ($enumboxing$ordinal == 3) {
            return new SourceGenerator(this.decodeHelper, this);
        }
        if ($enumboxing$ordinal == 5) {
            return null;
        }
        String string$valueOf$com$bumptech$glide$load$engine$DecodeJob$Stage = SolverVariable$Type$r8$EnumUnboxingUtility.string$valueOf$com$bumptech$glide$load$engine$DecodeJob$Stage(this.stage);
        throw new IllegalStateException(Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(string$valueOf$com$bumptech$glide$load$engine$DecodeJob$Stage.length() + 20, "Unrecognized stage: ", string$valueOf$com$bumptech$glide$load$engine$DecodeJob$Stage));
    }

    public final int getNextStage$enumunboxing$(int i) {
        int $enumboxing$ordinal = SolverVariable$Type$r8$EnumUnboxingUtility.$enumboxing$ordinal(i);
        if ($enumboxing$ordinal != 0) {
            if ($enumboxing$ordinal != 1) {
                if ($enumboxing$ordinal != 2) {
                    if ($enumboxing$ordinal == 3 || $enumboxing$ordinal == 5) {
                        return 6;
                    }
                    String string$valueOf$com$bumptech$glide$load$engine$DecodeJob$Stage = SolverVariable$Type$r8$EnumUnboxingUtility.string$valueOf$com$bumptech$glide$load$engine$DecodeJob$Stage(i);
                    throw new IllegalArgumentException(Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(string$valueOf$com$bumptech$glide$load$engine$DecodeJob$Stage.length() + 20, "Unrecognized stage: ", string$valueOf$com$bumptech$glide$load$engine$DecodeJob$Stage));
                } else if (this.onlyRetrieveFromCache) {
                    return 6;
                } else {
                    return 4;
                }
            } else if (this.diskCacheStrategy.decodeCachedData()) {
                return 3;
            } else {
                return getNextStage$enumunboxing$(3);
            }
        } else if (this.diskCacheStrategy.decodeCachedResource()) {
            return 2;
        } else {
            return getNextStage$enumunboxing$(2);
        }
    }

    @Override // com.bumptech.glide.util.pool.FactoryPools.Poolable
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }

    public final void logWithTimeAndKey(String str, long j, String str2) {
        double elapsedMillis = LogTime.getElapsedMillis(j);
        String valueOf = String.valueOf(this.loadKey);
        String concat = str2 != null ? str2.length() != 0 ? ", ".concat(str2) : new String(", ") : "";
        String name = Thread.currentThread().getName();
        StringBuilder sb = new StringBuilder(XMPPathFactory$$ExternalSyntheticOutline0.m(name, XMPPathFactory$$ExternalSyntheticOutline0.m(concat, valueOf.length() + XMPPathFactory$$ExternalSyntheticOutline0.m(str, 50))));
        sb.append(str);
        sb.append(" in ");
        sb.append(elapsedMillis);
        sb.append(", load key: ");
        sb.append(valueOf);
        sb.append(concat);
        sb.append(", thread: ");
        sb.append(name);
        Log.v("DecodeJob", sb.toString());
    }

    public final void notifyFailed() {
        boolean isComplete;
        setNotifiedOrThrow();
        GlideException glideException = new GlideException("Failed to load resource", new ArrayList(this.throwables));
        EngineJob engineJob = (EngineJob) this.callback;
        engineJob.exception = glideException;
        EngineJob.MAIN_THREAD_HANDLER.obtainMessage(2, engineJob).sendToTarget();
        ReleaseManager releaseManager = this.releaseManager;
        synchronized (releaseManager) {
            releaseManager.isFailed = true;
            isComplete = releaseManager.isComplete(false);
        }
        if (isComplete) {
            releaseInternal();
        }
    }

    @Override // com.bumptech.glide.load.engine.DataFetcherGenerator.FetcherReadyCallback
    public void onDataFetcherFailed(Key key, Exception exc, DataFetcher<?> dataFetcher, DataSource dataSource) {
        dataFetcher.cleanup();
        GlideException glideException = new GlideException("Fetching data failed", exc);
        glideException.setLoggingDetails(key, dataSource, dataFetcher.getDataClass());
        this.throwables.add(glideException);
        if (Thread.currentThread() != this.currentThread) {
            this.runReason = 2;
            ((EngineJob) this.callback).reschedule(this);
            return;
        }
        runGenerators();
    }

    @Override // com.bumptech.glide.load.engine.DataFetcherGenerator.FetcherReadyCallback
    public void onDataFetcherReady(Key key, Object obj, DataFetcher<?> dataFetcher, DataSource dataSource, Key key2) {
        this.currentSourceKey = key;
        this.currentData = obj;
        this.currentFetcher = dataFetcher;
        this.currentDataSource = dataSource;
        this.currentAttemptingKey = key2;
        if (Thread.currentThread() != this.currentThread) {
            this.runReason = 3;
            ((EngineJob) this.callback).reschedule(this);
            return;
        }
        decodeFromRetrievedData();
    }

    public final void releaseInternal() {
        ReleaseManager releaseManager = this.releaseManager;
        synchronized (releaseManager) {
            releaseManager.isEncodeComplete = false;
            releaseManager.isReleased = false;
            releaseManager.isFailed = false;
        }
        DeferredEncodeManager<?> deferredEncodeManager = this.deferredEncodeManager;
        deferredEncodeManager.key = null;
        deferredEncodeManager.encoder = null;
        deferredEncodeManager.toEncode = null;
        DecodeHelper<R> decodeHelper = this.decodeHelper;
        decodeHelper.glideContext = null;
        decodeHelper.model = null;
        decodeHelper.signature = null;
        decodeHelper.resourceClass = null;
        decodeHelper.transcodeClass = null;
        decodeHelper.options = null;
        decodeHelper.priority = null;
        decodeHelper.transformations = null;
        decodeHelper.diskCacheStrategy = null;
        decodeHelper.loadData.clear();
        decodeHelper.isLoadDataSet = false;
        decodeHelper.cacheKeys.clear();
        decodeHelper.isCacheKeysSet = false;
        this.isCallbackNotified = false;
        this.glideContext = null;
        this.signature = null;
        this.options = null;
        this.priority = null;
        this.loadKey = null;
        this.callback = null;
        this.stage = 0;
        this.currentGenerator = null;
        this.currentThread = null;
        this.currentSourceKey = null;
        this.currentData = null;
        this.currentDataSource = null;
        this.currentFetcher = null;
        this.startFetchTime = 0;
        this.isCancelled = false;
        this.model = null;
        this.throwables.clear();
        this.pool.release(this);
    }

    @Override // com.bumptech.glide.load.engine.DataFetcherGenerator.FetcherReadyCallback
    public void reschedule() {
        this.runReason = 2;
        ((EngineJob) this.callback).reschedule(this);
    }

    @Override // java.lang.Runnable
    public void run() {
        DataFetcher<?> dataFetcher = this.currentFetcher;
        try {
        } catch (Throwable th) {
            try {
                if (Log.isLoggable("DecodeJob", 3)) {
                    boolean z = this.isCancelled;
                    String string$valueOf$com$bumptech$glide$load$engine$DecodeJob$Stage = SolverVariable$Type$r8$EnumUnboxingUtility.string$valueOf$com$bumptech$glide$load$engine$DecodeJob$Stage(this.stage);
                    StringBuilder sb = new StringBuilder(string$valueOf$com$bumptech$glide$load$engine$DecodeJob$Stage.length() + 57);
                    sb.append("DecodeJob threw unexpectedly, isCancelled: ");
                    sb.append(z);
                    sb.append(", stage: ");
                    sb.append(string$valueOf$com$bumptech$glide$load$engine$DecodeJob$Stage);
                    Log.d("DecodeJob", sb.toString(), th);
                }
                if (this.stage != 5) {
                    this.throwables.add(th);
                    notifyFailed();
                }
                if (!this.isCancelled) {
                    throw th;
                } else if (dataFetcher == null) {
                    return;
                }
            } catch (Throwable th2) {
                if (dataFetcher != null) {
                    dataFetcher.cleanup();
                }
                throw th2;
            }
        }
        if (this.isCancelled) {
            notifyFailed();
            if (dataFetcher != null) {
                dataFetcher.cleanup();
                return;
            }
            return;
        }
        runWrapped();
        if (dataFetcher == null) {
            return;
        }
        dataFetcher.cleanup();
    }

    public final void runGenerators() {
        this.currentThread = Thread.currentThread();
        int i = LogTime.$r8$clinit;
        this.startFetchTime = SystemClock.elapsedRealtimeNanos();
        boolean z = false;
        while (!this.isCancelled && this.currentGenerator != null && !(z = this.currentGenerator.startNext())) {
            this.stage = getNextStage$enumunboxing$(this.stage);
            this.currentGenerator = getNextGenerator();
            if (this.stage == 4) {
                this.runReason = 2;
                ((EngineJob) this.callback).reschedule(this);
                return;
            }
        }
        if ((this.stage == 6 || this.isCancelled) && !z) {
            notifyFailed();
        }
    }

    public final void runWrapped() {
        int $enumboxing$ordinal = SolverVariable$Type$r8$EnumUnboxingUtility.$enumboxing$ordinal(this.runReason);
        if ($enumboxing$ordinal == 0) {
            this.stage = getNextStage$enumunboxing$(1);
            this.currentGenerator = getNextGenerator();
            runGenerators();
        } else if ($enumboxing$ordinal == 1) {
            runGenerators();
        } else if ($enumboxing$ordinal == 2) {
            decodeFromRetrievedData();
        } else {
            String string$valueOf$com$bumptech$glide$load$engine$DecodeJob$RunReason = SolverVariable$Type$r8$EnumUnboxingUtility.string$valueOf$com$bumptech$glide$load$engine$DecodeJob$RunReason(this.runReason);
            throw new IllegalStateException(Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(string$valueOf$com$bumptech$glide$load$engine$DecodeJob$RunReason.length() + 25, "Unrecognized run reason: ", string$valueOf$com$bumptech$glide$load$engine$DecodeJob$RunReason));
        }
    }

    public final void setNotifiedOrThrow() {
        this.stateVerifier.throwIfRecycled();
        if (!this.isCallbackNotified) {
            this.isCallbackNotified = true;
            return;
        }
        throw new IllegalStateException("Already notified");
    }
}
