package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class ThreadLocalEventLoop {
    public static final ThreadLocalEventLoop INSTANCE = null;
    public static final ThreadLocal<EventLoop> ref = new ThreadLocal<>();

    @NotNull
    public static final EventLoop getEventLoop$kotlinx_coroutines_core() {
        ThreadLocal<EventLoop> threadLocal = ref;
        EventLoop eventLoop = threadLocal.get();
        if (eventLoop != null) {
            return eventLoop;
        }
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
        BlockingEventLoop blockingEventLoop = new BlockingEventLoop(currentThread);
        threadLocal.set(blockingEventLoop);
        return blockingEventLoop;
    }
}
