package kotlinx.coroutines;

import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.ArrayQueue;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import kotlinx.coroutines.internal.ThreadSafeHeap;
import kotlinx.coroutines.internal.ThreadSafeHeapNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public abstract class EventLoopImplBase extends EventLoopImplPlatform {
    public volatile boolean isCompleted;
    public static final AtomicReferenceFieldUpdater _queue$FU = AtomicReferenceFieldUpdater.newUpdater(EventLoopImplBase.class, Object.class, "_queue");
    public static final AtomicReferenceFieldUpdater _delayed$FU = AtomicReferenceFieldUpdater.newUpdater(EventLoopImplBase.class, Object.class, "_delayed");
    public volatile Object _queue = null;
    public volatile Object _delayed = null;

    /* loaded from: classes.dex */
    public static abstract class DelayedTask implements Runnable, Comparable<DelayedTask>, DisposableHandle, ThreadSafeHeapNode {
        public Object _heap;
        public long nanoTime;

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // java.lang.Comparable
        public int compareTo(DelayedTask delayedTask) {
            DelayedTask delayedTask2 = delayedTask;
            Intrinsics.checkParameterIsNotNull(delayedTask2, "other");
            int i = ((this.nanoTime - delayedTask2.nanoTime) > 0 ? 1 : ((this.nanoTime - delayedTask2.nanoTime) == 0 ? 0 : -1));
            if (i > 0) {
                return 1;
            }
            return i < 0 ? -1 : 0;
        }

        @Override // kotlinx.coroutines.internal.ThreadSafeHeapNode
        public void setHeap(@Nullable ThreadSafeHeap<?> threadSafeHeap) {
            if (this._heap != EventLoop_commonKt.DISPOSED_TASK) {
                this._heap = threadSafeHeap;
                return;
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        @Override // kotlinx.coroutines.internal.ThreadSafeHeapNode
        public void setIndex(int i) {
        }

        @Override // java.lang.Object
        @NotNull
        public String toString() {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Delayed[nanos=");
            m.append(this.nanoTime);
            m.append(']');
            return m.toString();
        }
    }

    /* loaded from: classes.dex */
    public static final class DelayedTaskQueue extends ThreadSafeHeap<DelayedTask> {
        public long timeNow;

        public DelayedTaskQueue(long j) {
            this.timeNow = j;
        }
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public final void dispatch(@NotNull CoroutineContext coroutineContext, @NotNull Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        enqueue(runnable);
    }

    public final void enqueue(@NotNull Runnable runnable) {
        if (enqueueImpl(runnable)) {
            Thread thread = getThread();
            if (Thread.currentThread() != thread) {
                LockSupport.unpark(thread);
                return;
            }
            return;
        }
        DefaultExecutor.INSTANCE.enqueue(runnable);
    }

    public final boolean enqueueImpl(Runnable runnable) {
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
            } else if (obj == EventLoop_commonKt.CLOSED_EMPTY) {
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

    /* JADX WARNING: Removed duplicated region for block: B:13:0x001e A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x001f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long getNextTime() {
        /*
            r7 = this;
            kotlinx.coroutines.internal.ArrayQueue<kotlinx.coroutines.DispatchedTask<?>> r0 = r7.unconfinedQueue
            r1 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            r3 = 0
            if (r0 == 0) goto L_0x0019
            int r5 = r0.head
            int r0 = r0.tail
            if (r5 != r0) goto L_0x0013
            r0 = 1
            goto L_0x0014
        L_0x0013:
            r0 = 0
        L_0x0014:
            if (r0 == 0) goto L_0x0017
            goto L_0x0019
        L_0x0017:
            r5 = r3
            goto L_0x001a
        L_0x0019:
            r5 = r1
        L_0x001a:
            int r0 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r0 != 0) goto L_0x001f
            return r3
        L_0x001f:
            java.lang.Object r0 = r7._queue
            if (r0 != 0) goto L_0x0024
            goto L_0x0031
        L_0x0024:
            boolean r5 = r0 instanceof kotlinx.coroutines.internal.LockFreeTaskQueueCore
            if (r5 == 0) goto L_0x0053
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r0 = (kotlinx.coroutines.internal.LockFreeTaskQueueCore) r0
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L_0x0031
            return r3
        L_0x0031:
            java.lang.Object r7 = r7._delayed
            kotlinx.coroutines.EventLoopImplBase$DelayedTaskQueue r7 = (kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue) r7
            if (r7 == 0) goto L_0x0052
            monitor-enter(r7)
            kotlinx.coroutines.internal.ThreadSafeHeapNode r0 = r7.firstImpl()     // Catch: all -> 0x004f
            monitor-exit(r7)
            kotlinx.coroutines.EventLoopImplBase$DelayedTask r0 = (kotlinx.coroutines.EventLoopImplBase.DelayedTask) r0
            if (r0 == 0) goto L_0x0052
            long r0 = r0.nanoTime
            long r5 = java.lang.System.nanoTime()
            long r0 = r0 - r5
            int r7 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r7 >= 0) goto L_0x004d
            goto L_0x004e
        L_0x004d:
            r3 = r0
        L_0x004e:
            return r3
        L_0x004f:
            r0 = move-exception
            monitor-exit(r7)
            throw r0
        L_0x0052:
            return r1
        L_0x0053:
            kotlinx.coroutines.internal.Symbol r7 = kotlinx.coroutines.EventLoop_commonKt.CLOSED_EMPTY
            if (r0 != r7) goto L_0x0058
            return r1
        L_0x0058:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.EventLoopImplBase.getNextTime():long");
    }

    public boolean isEmpty() {
        ArrayQueue<DispatchedTask<?>> arrayQueue = this.unconfinedQueue;
        if (!(arrayQueue == null || arrayQueue.head == arrayQueue.tail)) {
            return false;
        }
        DelayedTaskQueue delayedTaskQueue = (DelayedTaskQueue) this._delayed;
        if (delayedTaskQueue != null) {
            if (!(delayedTaskQueue._size == 0)) {
                return false;
            }
        }
        Object obj = this._queue;
        if (obj == null) {
            return true;
        }
        if (obj instanceof LockFreeTaskQueueCore) {
            return ((LockFreeTaskQueueCore) obj).isEmpty();
        }
        return obj == EventLoop_commonKt.CLOSED_EMPTY;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0096, code lost:
        r6 = null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long processNextEvent() {
        /*
        // Method dump skipped, instructions count: 241
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.EventLoopImplBase.processNextEvent():long");
    }

    @Override // kotlinx.coroutines.EventLoop
    public void shutdown() {
        DelayedTask removeAtImpl;
        ThreadLocalEventLoop threadLocalEventLoop = ThreadLocalEventLoop.INSTANCE;
        ThreadLocalEventLoop.ref.set(null);
        this.isCompleted = true;
        boolean z = DebugKt.DEBUG;
        while (true) {
            Object obj = this._queue;
            if (obj == null) {
                if (_queue$FU.compareAndSet(this, null, EventLoop_commonKt.CLOSED_EMPTY)) {
                    break;
                }
            } else if (obj instanceof LockFreeTaskQueueCore) {
                ((LockFreeTaskQueueCore) obj).close();
                break;
            } else if (obj == EventLoop_commonKt.CLOSED_EMPTY) {
                break;
            } else {
                LockFreeTaskQueueCore lockFreeTaskQueueCore = new LockFreeTaskQueueCore(8, true);
                lockFreeTaskQueueCore.addLast((Runnable) obj);
                if (_queue$FU.compareAndSet(this, obj, lockFreeTaskQueueCore)) {
                    break;
                }
            }
        }
        do {
        } while (processNextEvent() <= 0);
        long nanoTime = System.nanoTime();
        while (true) {
            DelayedTaskQueue delayedTaskQueue = (DelayedTaskQueue) this._delayed;
            if (delayedTaskQueue != null) {
                synchronized (delayedTaskQueue) {
                    removeAtImpl = delayedTaskQueue._size > 0 ? delayedTaskQueue.removeAtImpl(0) : null;
                }
                DelayedTask delayedTask = removeAtImpl;
                if (delayedTask != null) {
                    reschedule(nanoTime, delayedTask);
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }
}
