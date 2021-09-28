package kotlinx.coroutines;

import java.util.concurrent.locks.LockSupport;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.EventLoopImplBase;
/* compiled from: EventLoop.kt */
/* loaded from: classes2.dex */
public abstract class EventLoopImplPlatform extends EventLoop {
    protected abstract Thread getThread();

    /* access modifiers changed from: protected */
    public final void unpark() {
        Thread thread = getThread();
        if (Thread.currentThread() != thread) {
            TimeSource timeSource = TimeSourceKt.getTimeSource();
            if (timeSource != null) {
                timeSource.unpark(thread);
            } else {
                LockSupport.unpark(thread);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void reschedule(long j, EventLoopImplBase.DelayedTask delayedTask) {
        Intrinsics.checkParameterIsNotNull(delayedTask, "delayedTask");
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(this != DefaultExecutor.INSTANCE)) {
                throw new AssertionError();
            }
        }
        DefaultExecutor.INSTANCE.schedule(j, delayedTask);
    }
}
