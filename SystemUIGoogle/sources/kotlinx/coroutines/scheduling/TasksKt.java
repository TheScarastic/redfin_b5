package kotlinx.coroutines.scheduling;

import java.util.concurrent.TimeUnit;
import kotlin.ranges.RangesKt___RangesKt;
import kotlinx.coroutines.internal.SystemPropsKt;
import kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt;
/* compiled from: Tasks.kt */
/* loaded from: classes2.dex */
public final class TasksKt {
    public static final int CORE_POOL_SIZE;
    public static final int MAX_POOL_SIZE;
    public static final long WORK_STEALING_TIME_RESOLUTION_NS = SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.resolution.ns", 100000L, 0L, 0L, 12, (Object) null);
    public static final int QUEUE_SIZE_OFFLOAD_THRESHOLD = SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.offload.threshold", 96, 0, 128, 4, (Object) null);
    public static final int BLOCKING_DEFAULT_PARALLELISM = SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.blocking.parallelism", 16, 0, 0, 12, (Object) null);
    public static final long IDLE_WORKER_KEEP_ALIVE_NS = TimeUnit.SECONDS.toNanos(SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.keep.alive.sec", 5L, 0L, 0L, 12, (Object) null));
    public static TimeSource schedulerTimeSource = NanoTimeSource.INSTANCE;

    static {
        int i = SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.core.pool.size", RangesKt___RangesKt.coerceAtLeast(SystemPropsKt.getAVAILABLE_PROCESSORS(), 2), 1, 0, 8, (Object) null);
        CORE_POOL_SIZE = i;
        MAX_POOL_SIZE = SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.max.pool.size", RangesKt___RangesKt.coerceIn(SystemPropsKt.getAVAILABLE_PROCESSORS() * 128, i, 2097150), 0, 2097150, 4, (Object) null);
    }
}
