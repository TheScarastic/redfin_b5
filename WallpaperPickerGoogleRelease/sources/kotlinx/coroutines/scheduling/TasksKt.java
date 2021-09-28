package kotlinx.coroutines.scheduling;

import java.util.concurrent.TimeUnit;
import kotlinx.coroutines.internal.SystemPropsKt;
import kotlinx.coroutines.internal.SystemPropsKt__SystemPropsKt;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class TasksKt {
    public static final int CORE_POOL_SIZE;
    public static final long IDLE_WORKER_KEEP_ALIVE_NS;
    public static final int MAX_POOL_SIZE;
    @NotNull
    public static TimeSource schedulerTimeSource;
    public static final long WORK_STEALING_TIME_RESOLUTION_NS = SystemPropsKt.systemProp$default("kotlinx.coroutines.scheduler.resolution.ns", 100000L, 0L, 0L, 12, (Object) null);
    public static final int QUEUE_SIZE_OFFLOAD_THRESHOLD = SystemPropsKt.systemProp$default("kotlinx.coroutines.scheduler.offload.threshold", 96, 0, 128, 4, (Object) null);

    static {
        SystemPropsKt.systemProp$default("kotlinx.coroutines.scheduler.blocking.parallelism", 16, 0, 0, 12, (Object) null);
        int i = SystemPropsKt__SystemPropsKt.AVAILABLE_PROCESSORS;
        int systemProp$default = SystemPropsKt.systemProp$default("kotlinx.coroutines.scheduler.core.pool.size", i < 2 ? 2 : i, 1, 0, 8, (Object) null);
        CORE_POOL_SIZE = systemProp$default;
        int i2 = i * 128;
        if (systemProp$default <= 2097150) {
            MAX_POOL_SIZE = SystemPropsKt.systemProp$default("kotlinx.coroutines.scheduler.max.pool.size", i2 < systemProp$default ? systemProp$default : i2 > 2097150 ? 2097150 : i2, 0, 2097150, 4, (Object) null);
            IDLE_WORKER_KEEP_ALIVE_NS = TimeUnit.SECONDS.toNanos(SystemPropsKt.systemProp$default("kotlinx.coroutines.scheduler.keep.alive.sec", 5L, 0L, 0L, 12, (Object) null));
            schedulerTimeSource = NanoTimeSource.INSTANCE;
            return;
        }
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum 2097150 is less than minimum " + systemProp$default + '.');
    }
}
