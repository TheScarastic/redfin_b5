package kotlinx.coroutines;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import kotlinx.coroutines.internal.ThreadSafeHeap;
import kotlinx.coroutines.internal.ThreadSafeHeapNode;
/* compiled from: EventLoop.common.kt */
/* loaded from: classes2.dex */
public abstract class EventLoopImplBase extends EventLoopImplPlatform {
    private volatile boolean isCompleted;
    private static final AtomicReferenceFieldUpdater _queue$FU = AtomicReferenceFieldUpdater.newUpdater(EventLoopImplBase.class, Object.class, "_queue");
    private static final AtomicReferenceFieldUpdater _delayed$FU = AtomicReferenceFieldUpdater.newUpdater(EventLoopImplBase.class, Object.class, "_delayed");
    private volatile Object _queue = null;
    private volatile Object _delayed = null;

    /* access modifiers changed from: protected */
    public boolean isEmpty() {
        if (!isUnconfinedQueueEmpty()) {
            return false;
        }
        DelayedTaskQueue delayedTaskQueue = (DelayedTaskQueue) this._delayed;
        if (delayedTaskQueue != null && !delayedTaskQueue.isEmpty()) {
            return false;
        }
        Object obj = this._queue;
        if (obj != null) {
            if (obj instanceof LockFreeTaskQueueCore) {
                return ((LockFreeTaskQueueCore) obj).isEmpty();
            }
            if (obj != EventLoop_commonKt.access$getCLOSED_EMPTY$p()) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    @Override // kotlinx.coroutines.EventLoop
    public long getNextTime() {
        DelayedTask peek;
        if (super.getNextTime() == 0) {
            return 0;
        }
        Object obj = this._queue;
        if (obj != null) {
            if (obj instanceof LockFreeTaskQueueCore) {
                if (!((LockFreeTaskQueueCore) obj).isEmpty()) {
                    return 0;
                }
            } else if (obj == EventLoop_commonKt.access$getCLOSED_EMPTY$p()) {
                return Long.MAX_VALUE;
            } else {
                return 0;
            }
        }
        DelayedTaskQueue delayedTaskQueue = (DelayedTaskQueue) this._delayed;
        if (delayedTaskQueue == null || (peek = delayedTaskQueue.peek()) == null) {
            return Long.MAX_VALUE;
        }
        long j = peek.nanoTime;
        TimeSource timeSource = TimeSourceKt.getTimeSource();
        return RangesKt___RangesKt.coerceAtLeast(j - (timeSource != null ? timeSource.nanoTime() : System.nanoTime()), 0);
    }

    @Override // kotlinx.coroutines.EventLoop
    protected void shutdown() {
        ThreadLocalEventLoop.INSTANCE.resetEventLoop$kotlinx_coroutines_core();
        this.isCompleted = true;
        closeQueue();
        do {
        } while (processNextEvent() <= 0);
        rescheduleAllDelayed();
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0055  */
    @Override // kotlinx.coroutines.EventLoop
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long processNextEvent() {
        /*
            r7 = this;
            boolean r0 = r7.processUnconfinedEvent()
            if (r0 == 0) goto L_0x000b
            long r0 = r7.getNextTime()
            return r0
        L_0x000b:
            java.lang.Object r0 = r7._delayed
            kotlinx.coroutines.EventLoopImplBase$DelayedTaskQueue r0 = (kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue) r0
            if (r0 == 0) goto L_0x004f
            boolean r1 = r0.isEmpty()
            if (r1 != 0) goto L_0x004f
            kotlinx.coroutines.TimeSource r1 = kotlinx.coroutines.TimeSourceKt.getTimeSource()
            if (r1 == 0) goto L_0x0022
            long r1 = r1.nanoTime()
            goto L_0x0026
        L_0x0022:
            long r1 = java.lang.System.nanoTime()
        L_0x0026:
            monitor-enter(r0)
            kotlinx.coroutines.internal.ThreadSafeHeapNode r3 = r0.firstImpl()     // Catch: all -> 0x004c
            r4 = 0
            if (r3 == 0) goto L_0x0046
            kotlinx.coroutines.EventLoopImplBase$DelayedTask r3 = (kotlinx.coroutines.EventLoopImplBase.DelayedTask) r3     // Catch: all -> 0x004c
            boolean r5 = r3.timeToExecute(r1)     // Catch: all -> 0x004c
            r6 = 0
            if (r5 == 0) goto L_0x003c
            boolean r3 = r7.enqueueImpl(r3)     // Catch: all -> 0x004c
            goto L_0x003d
        L_0x003c:
            r3 = r6
        L_0x003d:
            if (r3 == 0) goto L_0x0044
            kotlinx.coroutines.internal.ThreadSafeHeapNode r3 = r0.removeAtImpl(r6)     // Catch: all -> 0x004c
            r4 = r3
        L_0x0044:
            monitor-exit(r0)
            goto L_0x0047
        L_0x0046:
            monitor-exit(r0)
        L_0x0047:
            kotlinx.coroutines.EventLoopImplBase$DelayedTask r4 = (kotlinx.coroutines.EventLoopImplBase.DelayedTask) r4
            if (r4 == 0) goto L_0x004f
            goto L_0x0026
        L_0x004c:
            r7 = move-exception
            monitor-exit(r0)
            throw r7
        L_0x004f:
            java.lang.Runnable r0 = r7.dequeue()
            if (r0 == 0) goto L_0x0058
            r0.run()
        L_0x0058:
            long r0 = r7.getNextTime()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.EventLoopImplBase.processNextEvent():long");
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public final void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        enqueue(runnable);
    }

    public final void enqueue(Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(runnable, "task");
        if (enqueueImpl(runnable)) {
            unpark();
        } else {
            DefaultExecutor.INSTANCE.enqueue(runnable);
        }
    }

    private final void closeQueue() {
        if (!DebugKt.getASSERTIONS_ENABLED() || this.isCompleted) {
            while (true) {
                Object obj = this._queue;
                if (obj == null) {
                    if (_queue$FU.compareAndSet(this, null, EventLoop_commonKt.access$getCLOSED_EMPTY$p())) {
                        return;
                    }
                } else if (obj instanceof LockFreeTaskQueueCore) {
                    ((LockFreeTaskQueueCore) obj).close();
                    return;
                } else if (obj != EventLoop_commonKt.access$getCLOSED_EMPTY$p()) {
                    LockFreeTaskQueueCore lockFreeTaskQueueCore = new LockFreeTaskQueueCore(8, true);
                    lockFreeTaskQueueCore.addLast((Runnable) obj);
                    if (_queue$FU.compareAndSet(this, obj, lockFreeTaskQueueCore)) {
                        return;
                    }
                } else {
                    return;
                }
            }
        } else {
            throw new AssertionError();
        }
    }

    public final void schedule(long j, DelayedTask delayedTask) {
        Intrinsics.checkParameterIsNotNull(delayedTask, "delayedTask");
        int scheduleImpl = scheduleImpl(j, delayedTask);
        if (scheduleImpl != 0) {
            if (scheduleImpl == 1) {
                reschedule(j, delayedTask);
            } else if (scheduleImpl != 2) {
                throw new IllegalStateException("unexpected result".toString());
            }
        } else if (shouldUnpark(delayedTask)) {
            unpark();
        }
    }

    private final boolean shouldUnpark(DelayedTask delayedTask) {
        DelayedTaskQueue delayedTaskQueue = (DelayedTaskQueue) this._delayed;
        return (delayedTaskQueue != null ? delayedTaskQueue.peek() : null) == delayedTask;
    }

    private final int scheduleImpl(long j, DelayedTask delayedTask) {
        if (this.isCompleted) {
            return 1;
        }
        DelayedTaskQueue delayedTaskQueue = (DelayedTaskQueue) this._delayed;
        if (delayedTaskQueue == null) {
            _delayed$FU.compareAndSet(this, null, new DelayedTaskQueue(j));
            Object obj = this._delayed;
            if (obj == null) {
                Intrinsics.throwNpe();
            }
            delayedTaskQueue = (DelayedTaskQueue) obj;
        }
        return delayedTask.scheduleTask(j, delayedTaskQueue, this);
    }

    /* access modifiers changed from: protected */
    public final void resetAll() {
        this._queue = null;
        this._delayed = null;
    }

    private final void rescheduleAllDelayed() {
        DelayedTask removeFirstOrNull;
        TimeSource timeSource = TimeSourceKt.getTimeSource();
        long nanoTime = timeSource != null ? timeSource.nanoTime() : System.nanoTime();
        while (true) {
            DelayedTaskQueue delayedTaskQueue = (DelayedTaskQueue) this._delayed;
            if (delayedTaskQueue != null && (removeFirstOrNull = delayedTaskQueue.removeFirstOrNull()) != null) {
                reschedule(nanoTime, removeFirstOrNull);
            } else {
                return;
            }
        }
    }

    /* compiled from: EventLoop.common.kt */
    /* loaded from: classes2.dex */
    public static abstract class DelayedTask implements Runnable, Comparable<DelayedTask>, DisposableHandle, ThreadSafeHeapNode {
        private Object _heap;
        private int index;
        public long nanoTime;

        @Override // kotlinx.coroutines.internal.ThreadSafeHeapNode
        public ThreadSafeHeap<?> getHeap() {
            Object obj = this._heap;
            if (!(obj instanceof ThreadSafeHeap)) {
                obj = null;
            }
            return (ThreadSafeHeap) obj;
        }

        @Override // kotlinx.coroutines.internal.ThreadSafeHeapNode
        public void setHeap(ThreadSafeHeap<?> threadSafeHeap) {
            if (this._heap != EventLoop_commonKt.access$getDISPOSED_TASK$p()) {
                this._heap = threadSafeHeap;
                return;
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        @Override // kotlinx.coroutines.internal.ThreadSafeHeapNode
        public int getIndex() {
            return this.index;
        }

        @Override // kotlinx.coroutines.internal.ThreadSafeHeapNode
        public void setIndex(int i) {
            this.index = i;
        }

        public int compareTo(DelayedTask delayedTask) {
            Intrinsics.checkParameterIsNotNull(delayedTask, "other");
            int i = ((this.nanoTime - delayedTask.nanoTime) > 0 ? 1 : ((this.nanoTime - delayedTask.nanoTime) == 0 ? 0 : -1));
            if (i > 0) {
                return 1;
            }
            return i < 0 ? -1 : 0;
        }

        public final boolean timeToExecute(long j) {
            return j - this.nanoTime >= 0;
        }

        public final synchronized int scheduleTask(long j, DelayedTaskQueue delayedTaskQueue, EventLoopImplBase eventLoopImplBase) {
            Intrinsics.checkParameterIsNotNull(delayedTaskQueue, "delayed");
            Intrinsics.checkParameterIsNotNull(eventLoopImplBase, "eventLoop");
            if (this._heap == EventLoop_commonKt.access$getDISPOSED_TASK$p()) {
                return 2;
            }
            synchronized (delayedTaskQueue) {
                DelayedTask firstImpl = delayedTaskQueue.firstImpl();
                if (eventLoopImplBase.isCompleted) {
                    return 1;
                }
                if (firstImpl == null) {
                    delayedTaskQueue.timeNow = j;
                } else {
                    long j2 = firstImpl.nanoTime;
                    if (j2 - j < 0) {
                        j = j2;
                    }
                    if (j - delayedTaskQueue.timeNow > 0) {
                        delayedTaskQueue.timeNow = j;
                    }
                }
                long j3 = this.nanoTime;
                long j4 = delayedTaskQueue.timeNow;
                if (j3 - j4 < 0) {
                    this.nanoTime = j4;
                }
                delayedTaskQueue.addImpl(this);
                return 0;
            }
        }

        @Override // kotlinx.coroutines.DisposableHandle
        public final synchronized void dispose() {
            Object obj = this._heap;
            if (obj != EventLoop_commonKt.access$getDISPOSED_TASK$p()) {
                if (!(obj instanceof DelayedTaskQueue)) {
                    obj = null;
                }
                DelayedTaskQueue delayedTaskQueue = (DelayedTaskQueue) obj;
                if (delayedTaskQueue != null) {
                    delayedTaskQueue.remove(this);
                }
                this._heap = EventLoop_commonKt.access$getDISPOSED_TASK$p();
            }
        }

        @Override // java.lang.Object
        public String toString() {
            return "Delayed[nanos=" + this.nanoTime + ']';
        }
    }

    /* compiled from: EventLoop.common.kt */
    /* loaded from: classes2.dex */
    public static final class DelayedTaskQueue extends ThreadSafeHeap<DelayedTask> {
        public long timeNow;

        public DelayedTaskQueue(long j) {
            this.timeNow = j;
        }
    }

    private final boolean enqueueImpl(Runnable runnable) {
        while (true) {
            Object obj = this._queue;
            if (this.isCompleted) {
                return false;
            }
            if (obj == null) {
                if (_queue$FU.compareAndSet(this, null, runnable)) {
                    return true;
                }
            } else if (obj instanceof LockFreeTaskQueueCore) {
                LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) obj;
                int addLast = lockFreeTaskQueueCore.addLast(runnable);
                if (addLast == 0) {
                    return true;
                }
                if (addLast == 1) {
                    _queue$FU.compareAndSet(this, obj, lockFreeTaskQueueCore.next());
                } else if (addLast == 2) {
                    return false;
                }
            } else if (obj == EventLoop_commonKt.access$getCLOSED_EMPTY$p()) {
                return false;
            } else {
                LockFreeTaskQueueCore lockFreeTaskQueueCore2 = new LockFreeTaskQueueCore(8, true);
                lockFreeTaskQueueCore2.addLast((Runnable) obj);
                lockFreeTaskQueueCore2.addLast(runnable);
                if (_queue$FU.compareAndSet(this, obj, lockFreeTaskQueueCore2)) {
                    return true;
                }
            }
        }
    }

    private final Runnable dequeue() {
        while (true) {
            Object obj = this._queue;
            if (obj == null) {
                return null;
            }
            if (obj instanceof LockFreeTaskQueueCore) {
                LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) obj;
                Object removeFirstOrNull = lockFreeTaskQueueCore.removeFirstOrNull();
                if (removeFirstOrNull != LockFreeTaskQueueCore.REMOVE_FROZEN) {
                    return (Runnable) removeFirstOrNull;
                }
                _queue$FU.compareAndSet(this, obj, lockFreeTaskQueueCore.next());
            } else if (obj == EventLoop_commonKt.access$getCLOSED_EMPTY$p()) {
                return null;
            } else {
                if (_queue$FU.compareAndSet(this, obj, null)) {
                    return (Runnable) obj;
                }
            }
        }
    }
}
