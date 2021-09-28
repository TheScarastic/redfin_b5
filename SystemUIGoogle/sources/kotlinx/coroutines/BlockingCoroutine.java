package kotlinx.coroutines;

import java.util.concurrent.locks.LockSupport;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Builders.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class BlockingCoroutine<T> extends AbstractCoroutine<T> {
    private final Thread blockedThread;
    private final EventLoop eventLoop;

    @Override // kotlinx.coroutines.JobSupport
    protected boolean isScopedCoroutine() {
        return true;
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public BlockingCoroutine(CoroutineContext coroutineContext, Thread thread, EventLoop eventLoop) {
        super(coroutineContext, true);
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        Intrinsics.checkParameterIsNotNull(thread, "blockedThread");
        this.blockedThread = thread;
        this.eventLoop = eventLoop;
    }

    @Override // kotlinx.coroutines.JobSupport
    protected void afterCompletionInternal(Object obj, int i) {
        if (!Intrinsics.areEqual(Thread.currentThread(), this.blockedThread)) {
            LockSupport.unpark(this.blockedThread);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for r6v3, resolved type: T */
    /* JADX WARN: Multi-variable type inference failed */
    public final T joinBlocking() {
        TimeSource timeSource = TimeSourceKt.getTimeSource();
        if (timeSource != null) {
            timeSource.registerTimeLoopThread();
        }
        try {
            EventLoop eventLoop = this.eventLoop;
            CompletedExceptionally completedExceptionally = null;
            if (eventLoop != null) {
                EventLoop.incrementUseCount$default(eventLoop, false, 1, null);
            }
            while (!Thread.interrupted()) {
                EventLoop eventLoop2 = this.eventLoop;
                long processNextEvent = eventLoop2 != null ? eventLoop2.processNextEvent() : Long.MAX_VALUE;
                if (isCompleted()) {
                    EventLoop eventLoop3 = this.eventLoop;
                    if (eventLoop3 != null) {
                        EventLoop.decrementUseCount$default(eventLoop3, false, 1, null);
                    }
                    T t = (T) JobSupportKt.unboxState(getState$kotlinx_coroutines_core());
                    if (t instanceof CompletedExceptionally) {
                        completedExceptionally = t;
                    }
                    CompletedExceptionally completedExceptionally2 = completedExceptionally;
                    if (completedExceptionally2 == null) {
                        return t;
                    }
                    throw completedExceptionally2.cause;
                }
                TimeSource timeSource2 = TimeSourceKt.getTimeSource();
                if (timeSource2 != null) {
                    timeSource2.parkNanos(this, processNextEvent);
                } else {
                    LockSupport.parkNanos(this, processNextEvent);
                }
            }
            InterruptedException interruptedException = new InterruptedException();
            cancelCoroutine(interruptedException);
            throw interruptedException;
        } finally {
            TimeSource timeSource3 = TimeSourceKt.getTimeSource();
            if (timeSource3 != null) {
                timeSource3.unregisterTimeLoopThread();
            }
        }
    }
}
