package kotlinx.coroutines.scheduling;

import android.support.media.ExifInterface$$ExternalSyntheticOutline0;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.internal.SystemPropsKt;
import kotlinx.coroutines.internal.SystemPropsKt__SystemPropsKt;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class DefaultScheduler extends ExperimentalCoroutineDispatcher {
    public static final DefaultScheduler INSTANCE;
    @NotNull
    public static final CoroutineDispatcher IO;

    static {
        DefaultScheduler defaultScheduler = new DefaultScheduler();
        INSTANCE = defaultScheduler;
        int i = SystemPropsKt__SystemPropsKt.AVAILABLE_PROCESSORS;
        int systemProp$default = SystemPropsKt.systemProp$default("kotlinx.coroutines.io.parallelism", 64 < i ? i : 64, 0, 0, 12, (Object) null);
        if (systemProp$default > 0) {
            IO = new LimitingDispatcher(defaultScheduler, systemProp$default, TaskMode.PROBABLY_BLOCKING);
            return;
        }
        throw new IllegalArgumentException(ExifInterface$$ExternalSyntheticOutline0.m("Expected positive parallelism level, but have ", systemProp$default).toString());
    }

    public DefaultScheduler() {
        super(0, 0, null, 7);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        throw new UnsupportedOperationException("DefaultDispatcher cannot be closed");
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher, java.lang.Object
    @NotNull
    public String toString() {
        return "DefaultDispatcher";
    }
}
