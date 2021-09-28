package kotlinx.coroutines.scheduling;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
/* compiled from: WorkQueue.kt */
/* loaded from: classes2.dex */
public final class WorkQueue {
    private static final AtomicReferenceFieldUpdater lastScheduledTask$FU = AtomicReferenceFieldUpdater.newUpdater(WorkQueue.class, Object.class, "lastScheduledTask");
    static final AtomicIntegerFieldUpdater producerIndex$FU = AtomicIntegerFieldUpdater.newUpdater(WorkQueue.class, "producerIndex");
    static final AtomicIntegerFieldUpdater consumerIndex$FU = AtomicIntegerFieldUpdater.newUpdater(WorkQueue.class, "consumerIndex");
    private final AtomicReferenceArray<Task> buffer = new AtomicReferenceArray<>(128);
    private volatile Object lastScheduledTask = null;
    volatile int producerIndex = 0;
    volatile int consumerIndex = 0;

    public final int getBufferSize$kotlinx_coroutines_core() {
        return this.producerIndex - this.consumerIndex;
    }

    public final Task poll() {
        Task task = (Task) lastScheduledTask$FU.getAndSet(this, null);
        if (task != null) {
            return task;
        }
        while (true) {
            int i = this.consumerIndex;
            if (i - this.producerIndex == 0) {
                return null;
            }
            int i2 = i & 127;
            if (((Task) this.buffer.get(i2)) != null && consumerIndex$FU.compareAndSet(this, i, i + 1)) {
                return (Task) this.buffer.getAndSet(i2, null);
            }
        }
    }

    public final boolean add(Task task, GlobalQueue globalQueue) {
        Intrinsics.checkParameterIsNotNull(task, "task");
        Intrinsics.checkParameterIsNotNull(globalQueue, "globalQueue");
        Task task2 = (Task) lastScheduledTask$FU.getAndSet(this, task);
        if (task2 != null) {
            return addLast(task2, globalQueue);
        }
        return true;
    }

    public final boolean addLast(Task task, GlobalQueue globalQueue) {
        Intrinsics.checkParameterIsNotNull(task, "task");
        Intrinsics.checkParameterIsNotNull(globalQueue, "globalQueue");
        boolean z = true;
        while (!tryAddLast(task)) {
            offloadWork(globalQueue);
            z = false;
        }
        return z;
    }

    public final boolean trySteal(WorkQueue workQueue, GlobalQueue globalQueue) {
        Task task;
        Intrinsics.checkParameterIsNotNull(workQueue, "victim");
        Intrinsics.checkParameterIsNotNull(globalQueue, "globalQueue");
        long nanoTime = TasksKt.schedulerTimeSource.nanoTime();
        int bufferSize$kotlinx_coroutines_core = workQueue.getBufferSize$kotlinx_coroutines_core();
        if (bufferSize$kotlinx_coroutines_core == 0) {
            return tryStealLastScheduled(nanoTime, workQueue, globalQueue);
        }
        int i = RangesKt___RangesKt.coerceAtLeast(bufferSize$kotlinx_coroutines_core / 2, 1);
        int i2 = 0;
        boolean z = false;
        while (i2 < i) {
            while (true) {
                int i3 = workQueue.consumerIndex;
                task = null;
                if (i3 - workQueue.producerIndex != 0) {
                    int i4 = i3 & 127;
                    Task task2 = (Task) workQueue.buffer.get(i4);
                    if (task2 != null) {
                        if (!(nanoTime - task2.submissionTime >= TasksKt.WORK_STEALING_TIME_RESOLUTION_NS || workQueue.getBufferSize$kotlinx_coroutines_core() > TasksKt.QUEUE_SIZE_OFFLOAD_THRESHOLD)) {
                            break;
                        } else if (consumerIndex$FU.compareAndSet(workQueue, i3, i3 + 1)) {
                            task = (Task) workQueue.buffer.getAndSet(i4, null);
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
            if (task == null) {
                break;
            }
            add(task, globalQueue);
            i2++;
            z = true;
        }
        return z;
    }

    private final boolean tryStealLastScheduled(long j, WorkQueue workQueue, GlobalQueue globalQueue) {
        Task task = (Task) workQueue.lastScheduledTask;
        if (task == null || j - task.submissionTime < TasksKt.WORK_STEALING_TIME_RESOLUTION_NS || !lastScheduledTask$FU.compareAndSet(workQueue, task, null)) {
            return false;
        }
        add(task, globalQueue);
        return true;
    }

    public final int size$kotlinx_coroutines_core() {
        Object obj = this.lastScheduledTask;
        int bufferSize$kotlinx_coroutines_core = getBufferSize$kotlinx_coroutines_core();
        return obj != null ? bufferSize$kotlinx_coroutines_core + 1 : bufferSize$kotlinx_coroutines_core;
    }

    private final void offloadWork(GlobalQueue globalQueue) {
        Task task;
        int i = RangesKt___RangesKt.coerceAtLeast(getBufferSize$kotlinx_coroutines_core() / 2, 1);
        for (int i2 = 0; i2 < i; i2++) {
            while (true) {
                int i3 = this.consumerIndex;
                task = null;
                if (i3 - this.producerIndex == 0) {
                    break;
                }
                int i4 = i3 & 127;
                if (((Task) this.buffer.get(i4)) != null && consumerIndex$FU.compareAndSet(this, i3, i3 + 1)) {
                    task = (Task) this.buffer.getAndSet(i4, null);
                    break;
                }
            }
            if (task != null) {
                addToGlobalQueue(globalQueue, task);
            } else {
                return;
            }
        }
    }

    private final void addToGlobalQueue(GlobalQueue globalQueue, Task task) {
        if (!globalQueue.addLast(task)) {
            throw new IllegalStateException("GlobalQueue could not be closed yet".toString());
        }
    }

    public final void offloadAllWork$kotlinx_coroutines_core(GlobalQueue globalQueue) {
        Task task;
        Intrinsics.checkParameterIsNotNull(globalQueue, "globalQueue");
        Task task2 = (Task) lastScheduledTask$FU.getAndSet(this, null);
        if (task2 != null) {
            addToGlobalQueue(globalQueue, task2);
        }
        while (true) {
            int i = this.consumerIndex;
            if (i - this.producerIndex == 0) {
                task = null;
            } else {
                int i2 = i & 127;
                if (((Task) this.buffer.get(i2)) != null && consumerIndex$FU.compareAndSet(this, i, i + 1)) {
                    task = (Task) this.buffer.getAndSet(i2, null);
                }
            }
            if (task != null) {
                addToGlobalQueue(globalQueue, task);
            } else {
                return;
            }
        }
    }

    private final boolean tryAddLast(Task task) {
        if (getBufferSize$kotlinx_coroutines_core() == 127) {
            return false;
        }
        int i = this.producerIndex & 127;
        if (this.buffer.get(i) != null) {
            return false;
        }
        this.buffer.lazySet(i, task);
        producerIndex$FU.incrementAndGet(this);
        return true;
    }
}
