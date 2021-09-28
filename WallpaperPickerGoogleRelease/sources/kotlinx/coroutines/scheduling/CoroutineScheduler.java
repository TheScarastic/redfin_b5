package kotlinx.coroutines.scheduling;

import android.support.media.ExifInterface$$ExternalSyntheticOutline0;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import kotlinx.coroutines.DebugStringsKt;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.SystemPropsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class CoroutineScheduler implements Executor, Closeable {
    public static final int MAX_PARK_TIME_NS;
    public static final int MAX_SPINS;
    public static final int MAX_YIELDS;
    public static final int MIN_PARK_TIME_NS;
    public static final Symbol NOT_IN_STACK;
    public static final AtomicIntegerFieldUpdater _isTerminated$FU;
    public static final AtomicLongFieldUpdater controlState$FU;
    public static final AtomicLongFieldUpdater parkedWorkersStack$FU;
    public volatile int _isTerminated;
    public volatile long controlState;
    public final int corePoolSize;
    public final Semaphore cpuPermits;
    public final GlobalQueue globalQueue;
    public final long idleWorkerKeepAliveNs;
    public final int maxPoolSize;
    public volatile long parkedWorkersStack;
    public final Random random;
    public final String schedulerName;
    public final Worker[] workers;

    /* loaded from: classes.dex */
    public final class Worker extends Thread {
        public static final AtomicIntegerFieldUpdater terminationState$FU = AtomicIntegerFieldUpdater.newUpdater(Worker.class, "terminationState");
        public volatile int indexInArray;
        public long lastExhaustionTime;
        public int lastStealIndex;
        public int rngState;
        public volatile int spins;
        public long terminationDeadline;
        @NotNull
        public final WorkQueue localQueue = new WorkQueue();
        @NotNull
        public volatile WorkerState state = WorkerState.RETIRING;
        public volatile int terminationState = 0;
        @Nullable
        public volatile Object nextParkedWorker = CoroutineScheduler.NOT_IN_STACK;
        public int parkTimeNs = CoroutineScheduler.MIN_PARK_TIME_NS;

        public Worker(int i) {
            setDaemon(true);
            this.rngState = CoroutineScheduler.this.random.nextInt();
            setIndexInArray(i);
        }

        public final boolean blockingQuiescence() {
            Task removeFirstWithModeOrNull = CoroutineScheduler.this.globalQueue.removeFirstWithModeOrNull(TaskMode.PROBABLY_BLOCKING);
            if (removeFirstWithModeOrNull == null) {
                return true;
            }
            this.localQueue.add(removeFirstWithModeOrNull, CoroutineScheduler.this.globalQueue);
            return false;
        }

        /* JADX WARNING: Removed duplicated region for block: B:8:0x0038 A[RETURN] */
        /* JADX WARNING: Removed duplicated region for block: B:9:0x003a  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final boolean doPark(long r10) {
            /*
                r9 = this;
                kotlinx.coroutines.scheduling.CoroutineScheduler r6 = kotlinx.coroutines.scheduling.CoroutineScheduler.this
                java.util.concurrent.atomic.AtomicLongFieldUpdater r0 = kotlinx.coroutines.scheduling.CoroutineScheduler.parkedWorkersStack$FU
                java.util.Objects.requireNonNull(r6)
                java.lang.Object r0 = r9.nextParkedWorker
                kotlinx.coroutines.internal.Symbol r1 = kotlinx.coroutines.scheduling.CoroutineScheduler.NOT_IN_STACK
                if (r0 == r1) goto L_0x000e
                goto L_0x0032
            L_0x000e:
                long r2 = r6.parkedWorkersStack
                r0 = 2097151(0x1fffff, double:1.0361303E-317)
                long r0 = r0 & r2
                int r0 = (int) r0
                r4 = 2097152(0x200000, double:1.0361308E-317)
                long r4 = r4 + r2
                r7 = -2097152(0xffffffffffe00000, double:NaN)
                long r4 = r4 & r7
                int r1 = r9.indexInArray
                boolean r7 = kotlinx.coroutines.DebugKt.DEBUG
                kotlinx.coroutines.scheduling.CoroutineScheduler$Worker[] r7 = r6.workers
                r0 = r7[r0]
                r9.nextParkedWorker = r0
                java.util.concurrent.atomic.AtomicLongFieldUpdater r0 = kotlinx.coroutines.scheduling.CoroutineScheduler.parkedWorkersStack$FU
                long r7 = (long) r1
                long r4 = r4 | r7
                r1 = r6
                boolean r0 = r0.compareAndSet(r1, r2, r4)
                if (r0 == 0) goto L_0x000e
            L_0x0032:
                boolean r9 = r9.blockingQuiescence()
                if (r9 != 0) goto L_0x003a
                r9 = 0
                return r9
            L_0x003a:
                java.util.concurrent.locks.LockSupport.parkNanos(r10)
                r9 = 1
                return r9
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.scheduling.CoroutineScheduler.Worker.doPark(long):boolean");
        }

        @Nullable
        public final Task findTask$kotlinx_coroutines_core() {
            Task task;
            Task removeFirstOrNull;
            Task removeFirstWithModeOrNull;
            if (tryAcquireCpuPermit()) {
                boolean z = false;
                boolean z2 = nextInt$kotlinx_coroutines_core(CoroutineScheduler.this.corePoolSize * 2) == 0;
                if (z2 && (removeFirstWithModeOrNull = CoroutineScheduler.this.globalQueue.removeFirstWithModeOrNull(TaskMode.NON_BLOCKING)) != null) {
                    return removeFirstWithModeOrNull;
                }
                Task poll = this.localQueue.poll();
                if (poll != null) {
                    return poll;
                }
                if (!z2 && (removeFirstOrNull = CoroutineScheduler.this.globalQueue.removeFirstOrNull()) != null) {
                    return removeFirstOrNull;
                }
                int i = (int) (CoroutineScheduler.this.controlState & 2097151);
                if (i < 2) {
                    return null;
                }
                int i2 = this.lastStealIndex;
                if (i2 == 0) {
                    i2 = nextInt$kotlinx_coroutines_core(i);
                }
                int i3 = i2 + 1;
                if (i3 > i) {
                    i3 = 1;
                }
                this.lastStealIndex = i3;
                CoroutineScheduler coroutineScheduler = CoroutineScheduler.this;
                Worker worker = coroutineScheduler.workers[i3];
                if (worker == null || worker == this) {
                    return null;
                }
                WorkQueue workQueue = this.localQueue;
                WorkQueue workQueue2 = worker.localQueue;
                GlobalQueue globalQueue = coroutineScheduler.globalQueue;
                Objects.requireNonNull(workQueue);
                Intrinsics.checkParameterIsNotNull(workQueue2, "victim");
                Intrinsics.checkParameterIsNotNull(globalQueue, "globalQueue");
                Objects.requireNonNull((NanoTimeSource) TasksKt.schedulerTimeSource);
                long nanoTime = System.nanoTime();
                int bufferSize$kotlinx_coroutines_core = workQueue2.getBufferSize$kotlinx_coroutines_core();
                if (bufferSize$kotlinx_coroutines_core == 0) {
                    Task task2 = (Task) workQueue2.lastScheduledTask;
                    if (task2 != null && nanoTime - task2.submissionTime >= TasksKt.WORK_STEALING_TIME_RESOLUTION_NS && WorkQueue.lastScheduledTask$FU.compareAndSet(workQueue2, task2, null)) {
                        workQueue.add(task2, globalQueue);
                        z = true;
                    }
                } else {
                    int i4 = bufferSize$kotlinx_coroutines_core / 2;
                    if (i4 < 1) {
                        i4 = 1;
                    }
                    int i5 = 0;
                    boolean z3 = false;
                    while (i5 < i4) {
                        while (true) {
                            int i6 = workQueue2.consumerIndex;
                            if (i6 - workQueue2.producerIndex == 0) {
                                break;
                            }
                            int i7 = i6 & 127;
                            Task task3 = workQueue2.buffer.get(i7);
                            if (task3 != null) {
                                if (!(nanoTime - task3.submissionTime >= TasksKt.WORK_STEALING_TIME_RESOLUTION_NS || workQueue2.getBufferSize$kotlinx_coroutines_core() > TasksKt.QUEUE_SIZE_OFFLOAD_THRESHOLD)) {
                                    break;
                                } else if (WorkQueue.consumerIndex$FU.compareAndSet(workQueue2, i6, i6 + 1)) {
                                    task = workQueue2.buffer.getAndSet(i7, null);
                                    break;
                                }
                            }
                        }
                        task = null;
                        if (task == null) {
                            break;
                        }
                        workQueue.add(task, globalQueue);
                        i5++;
                        z3 = true;
                    }
                    z = z3;
                }
                if (z) {
                    return this.localQueue.poll();
                }
                return null;
            }
            Task poll2 = this.localQueue.poll();
            return poll2 != null ? poll2 : CoroutineScheduler.this.globalQueue.removeFirstWithModeOrNull(TaskMode.PROBABLY_BLOCKING);
        }

        public final int nextInt$kotlinx_coroutines_core(int i) {
            int i2 = this.rngState;
            int i3 = i2 ^ (i2 << 13);
            this.rngState = i3;
            int i4 = i3 ^ (i3 >> 17);
            this.rngState = i4;
            int i5 = i4 ^ (i4 << 5);
            this.rngState = i5;
            int i6 = i - 1;
            if ((i6 & i) == 0) {
                return i6 & i5;
            }
            return (Integer.MAX_VALUE & i5) % i;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:57:0x00eb, code lost:
            kotlin.jvm.internal.Intrinsics.throwNpe();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x00ee, code lost:
            throw null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:91:0x0188, code lost:
            tryReleaseCpu$kotlinx_coroutines_core(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:92:0x018b, code lost:
            return;
         */
        /* JADX WARNING: Removed duplicated region for block: B:86:0x016f  */
        @Override // java.lang.Thread, java.lang.Runnable
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            // Method dump skipped, instructions count: 396
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.scheduling.CoroutineScheduler.Worker.run():void");
        }

        public final void setIndexInArray(int i) {
            StringBuilder sb = new StringBuilder();
            sb.append(CoroutineScheduler.this.schedulerName);
            sb.append("-worker-");
            sb.append(i == 0 ? "TERMINATED" : String.valueOf(i));
            setName(sb.toString());
            this.indexInArray = i;
        }

        public final boolean tryAcquireCpuPermit() {
            WorkerState workerState = this.state;
            WorkerState workerState2 = WorkerState.CPU_ACQUIRED;
            if (workerState == workerState2) {
                return true;
            }
            if (!CoroutineScheduler.this.cpuPermits.tryAcquire()) {
                return false;
            }
            this.state = workerState2;
            return true;
        }

        public final boolean tryReleaseCpu$kotlinx_coroutines_core(@NotNull WorkerState workerState) {
            WorkerState workerState2 = this.state;
            boolean z = workerState2 == WorkerState.CPU_ACQUIRED;
            if (z) {
                CoroutineScheduler.this.cpuPermits.release();
            }
            if (workerState2 != workerState) {
                this.state = workerState;
            }
            return z;
        }
    }

    /* loaded from: classes.dex */
    public enum WorkerState {
        CPU_ACQUIRED,
        BLOCKING,
        PARKING,
        RETIRING,
        TERMINATED
    }

    static {
        int systemProp$default = SystemPropsKt.systemProp$default("kotlinx.coroutines.scheduler.spins", 1000, 1, 0, 8, (Object) null);
        MAX_SPINS = systemProp$default;
        MAX_YIELDS = systemProp$default + SystemPropsKt.systemProp$default("kotlinx.coroutines.scheduler.yields", 0, 0, 0, 8, (Object) null);
        int nanos = (int) TimeUnit.SECONDS.toNanos(1);
        MAX_PARK_TIME_NS = nanos;
        long j = TasksKt.WORK_STEALING_TIME_RESOLUTION_NS / ((long) 4);
        if (j < 10) {
            j = 10;
        }
        MIN_PARK_TIME_NS = (int) RangesKt___RangesKt.coerceAtMost(j, (long) nanos);
        NOT_IN_STACK = new Symbol("NOT_IN_STACK");
        parkedWorkersStack$FU = AtomicLongFieldUpdater.newUpdater(CoroutineScheduler.class, "parkedWorkersStack");
        controlState$FU = AtomicLongFieldUpdater.newUpdater(CoroutineScheduler.class, "controlState");
        _isTerminated$FU = AtomicIntegerFieldUpdater.newUpdater(CoroutineScheduler.class, "_isTerminated");
    }

    public CoroutineScheduler(int i, int i2, long j, @NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "schedulerName");
        this.corePoolSize = i;
        this.maxPoolSize = i2;
        this.idleWorkerKeepAliveNs = j;
        this.schedulerName = str;
        if (i >= 1) {
            if (i2 >= i) {
                if (i2 <= 2097150) {
                    if (j > 0) {
                        this.globalQueue = new GlobalQueue();
                        this.cpuPermits = new Semaphore(i, false);
                        this.parkedWorkersStack = 0;
                        this.workers = new Worker[i2 + 1];
                        this.controlState = 0;
                        this.random = new Random();
                        this._isTerminated = 0;
                        return;
                    }
                    throw new IllegalArgumentException(("Idle worker keep alive time " + j + " must be positive").toString());
                }
                throw new IllegalArgumentException(("Max pool size " + i2 + " should not exceed maximal supported number of threads 2097150").toString());
            }
            throw new IllegalArgumentException(("Max pool size " + i2 + " should be greater than or equals to core pool size " + i).toString());
        }
        throw new IllegalArgumentException(("Core pool size " + i + " should be at least 1").toString());
    }

    public static final void access$parkedWorkersStackTopUpdate(CoroutineScheduler coroutineScheduler, Worker worker, int i, int i2) {
        while (true) {
            long j = coroutineScheduler.parkedWorkersStack;
            int i3 = (int) (2097151 & j);
            long j2 = (2097152 + j) & -2097152;
            if (i3 == i) {
                i3 = i2 == 0 ? coroutineScheduler.parkedWorkersStackNextIndex(worker) : i2;
            }
            if (i3 >= 0 && parkedWorkersStack$FU.compareAndSet(coroutineScheduler, j, j2 | ((long) i3))) {
                return;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0099, code lost:
        if (r1 != null) goto L_0x00a4;
     */
    @Override // java.io.Closeable, java.lang.AutoCloseable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() {
        /*
            r10 = this;
            java.util.concurrent.atomic.AtomicIntegerFieldUpdater r0 = kotlinx.coroutines.scheduling.CoroutineScheduler._isTerminated$FU
            r1 = 0
            r2 = 1
            boolean r0 = r0.compareAndSet(r10, r1, r2)
            if (r0 != 0) goto L_0x000c
            goto L_0x00b9
        L_0x000c:
            kotlinx.coroutines.scheduling.CoroutineScheduler$Worker r0 = r10.currentWorker()
            kotlinx.coroutines.scheduling.CoroutineScheduler$Worker[] r1 = r10.workers
            monitor-enter(r1)
            long r3 = r10.controlState     // Catch: all -> 0x00c4
            r5 = 2097151(0x1fffff, double:1.0361303E-317)
            long r3 = r3 & r5
            int r3 = (int) r3
            monitor-exit(r1)
            if (r2 > r3) goto L_0x0087
        L_0x001d:
            kotlinx.coroutines.scheduling.CoroutineScheduler$Worker[] r1 = r10.workers
            r1 = r1[r2]
            r4 = 0
            if (r1 == 0) goto L_0x0083
            if (r1 == r0) goto L_0x007e
        L_0x0026:
            boolean r5 = r1.isAlive()
            if (r5 == 0) goto L_0x0035
            java.util.concurrent.locks.LockSupport.unpark(r1)
            r5 = 10000(0x2710, double:4.9407E-320)
            r1.join(r5)
            goto L_0x0026
        L_0x0035:
            boolean r5 = kotlinx.coroutines.DebugKt.DEBUG
            kotlinx.coroutines.scheduling.WorkQueue r1 = r1.localQueue
            kotlinx.coroutines.scheduling.GlobalQueue r5 = r10.globalQueue
            java.util.Objects.requireNonNull(r1)
            java.lang.String r6 = "globalQueue"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r5, r6)
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater r6 = kotlinx.coroutines.scheduling.WorkQueue.lastScheduledTask$FU
            java.lang.Object r6 = r6.getAndSet(r1, r4)
            kotlinx.coroutines.scheduling.Task r6 = (kotlinx.coroutines.scheduling.Task) r6
            if (r6 == 0) goto L_0x0050
            r1.addToGlobalQueue(r5, r6)
        L_0x0050:
            int r6 = r1.consumerIndex
            int r7 = r1.producerIndex
            int r7 = r6 - r7
            if (r7 != 0) goto L_0x005a
            r6 = r4
            goto L_0x0078
        L_0x005a:
            r7 = r6 & 127(0x7f, float:1.78E-43)
            java.util.concurrent.atomic.AtomicReferenceArray<kotlinx.coroutines.scheduling.Task> r8 = r1.buffer
            java.lang.Object r8 = r8.get(r7)
            kotlinx.coroutines.scheduling.Task r8 = (kotlinx.coroutines.scheduling.Task) r8
            if (r8 == 0) goto L_0x0050
            java.util.concurrent.atomic.AtomicIntegerFieldUpdater r8 = kotlinx.coroutines.scheduling.WorkQueue.consumerIndex$FU
            int r9 = r6 + 1
            boolean r6 = r8.compareAndSet(r1, r6, r9)
            if (r6 == 0) goto L_0x0050
            java.util.concurrent.atomic.AtomicReferenceArray<kotlinx.coroutines.scheduling.Task> r6 = r1.buffer
            java.lang.Object r6 = r6.getAndSet(r7, r4)
            kotlinx.coroutines.scheduling.Task r6 = (kotlinx.coroutines.scheduling.Task) r6
        L_0x0078:
            if (r6 == 0) goto L_0x007e
            r1.addToGlobalQueue(r5, r6)
            goto L_0x0050
        L_0x007e:
            if (r2 == r3) goto L_0x0087
            int r2 = r2 + 1
            goto L_0x001d
        L_0x0083:
            kotlin.jvm.internal.Intrinsics.throwNpe()
            throw r4
        L_0x0087:
            kotlinx.coroutines.scheduling.GlobalQueue r2 = r10.globalQueue
        L_0x0089:
            java.lang.Object r1 = r2._cur$internal
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r1 = (kotlinx.coroutines.internal.LockFreeTaskQueueCore) r1
            boolean r3 = r1.close()
            if (r3 == 0) goto L_0x00ba
        L_0x0093:
            if (r0 == 0) goto L_0x009c
            kotlinx.coroutines.scheduling.Task r1 = r0.findTask$kotlinx_coroutines_core()
            if (r1 == 0) goto L_0x009c
            goto L_0x00a4
        L_0x009c:
            kotlinx.coroutines.scheduling.GlobalQueue r1 = r10.globalQueue
            java.lang.Object r1 = r1.removeFirstOrNull()
            kotlinx.coroutines.scheduling.Task r1 = (kotlinx.coroutines.scheduling.Task) r1
        L_0x00a4:
            if (r1 == 0) goto L_0x00aa
            r10.runSafely(r1)
            goto L_0x0093
        L_0x00aa:
            if (r0 == 0) goto L_0x00b1
            kotlinx.coroutines.scheduling.CoroutineScheduler$WorkerState r1 = kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.TERMINATED
            r0.tryReleaseCpu$kotlinx_coroutines_core(r1)
        L_0x00b1:
            boolean r0 = kotlinx.coroutines.DebugKt.DEBUG
            r0 = 0
            r10.parkedWorkersStack = r0
            r10.controlState = r0
        L_0x00b9:
            return
        L_0x00ba:
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater r3 = kotlinx.coroutines.internal.LockFreeTaskQueue._cur$FU$internal
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r4 = r1.next()
            r3.compareAndSet(r2, r1, r4)
            goto L_0x0089
        L_0x00c4:
            r10 = move-exception
            monitor-exit(r1)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.scheduling.CoroutineScheduler.close():void");
    }

    public final int createNewWorker() {
        synchronized (this.workers) {
            boolean z = false;
            if (this._isTerminated != 0) {
                return -1;
            }
            long j = this.controlState;
            int i = (int) (j & 2097151);
            int i2 = i - ((int) ((j & 4398044413952L) >> 21));
            if (i2 >= this.corePoolSize) {
                return 0;
            }
            if (i < this.maxPoolSize && this.cpuPermits.availablePermits() != 0) {
                int i3 = ((int) (this.controlState & 2097151)) + 1;
                if (i3 > 0 && this.workers[i3] == null) {
                    Worker worker = new Worker(i3);
                    worker.start();
                    if (i3 == ((int) (2097151 & controlState$FU.incrementAndGet(this)))) {
                        z = true;
                    }
                    if (z) {
                        this.workers[i3] = worker;
                        return i2 + 1;
                    }
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            return 0;
        }
    }

    @NotNull
    public final Task createTask$kotlinx_coroutines_core(@NotNull Runnable runnable, @NotNull TaskContext taskContext) {
        Objects.requireNonNull((NanoTimeSource) TasksKt.schedulerTimeSource);
        long nanoTime = System.nanoTime();
        if (!(runnable instanceof Task)) {
            return new TaskImpl(runnable, nanoTime, taskContext);
        }
        Task task = (Task) runnable;
        task.submissionTime = nanoTime;
        task.taskContext = taskContext;
        return task;
    }

    public final Worker currentWorker() {
        Thread currentThread = Thread.currentThread();
        if (!(currentThread instanceof Worker)) {
            currentThread = null;
        }
        Worker worker = (Worker) currentThread;
        if (worker == null || !Intrinsics.areEqual(CoroutineScheduler.this, this)) {
            return null;
        }
        return worker;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0037, code lost:
        if (r7.tryAcquireCpuPermit() == false) goto L_0x005d;
     */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003d  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0060  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0085 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void dispatch(@org.jetbrains.annotations.NotNull java.lang.Runnable r6, @org.jetbrains.annotations.NotNull kotlinx.coroutines.scheduling.TaskContext r7, boolean r8) {
        /*
            r5 = this;
            java.lang.String r0 = "block"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r6, r0)
            java.lang.String r0 = "taskContext"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r7, r0)
            kotlinx.coroutines.scheduling.Task r6 = r5.createTask$kotlinx_coroutines_core(r6, r7)
            kotlinx.coroutines.scheduling.CoroutineScheduler$Worker r7 = r5.currentWorker()
            r0 = -1
            r1 = 1
            if (r7 == 0) goto L_0x005d
            kotlinx.coroutines.scheduling.CoroutineScheduler$WorkerState r2 = r7.state
            kotlinx.coroutines.scheduling.CoroutineScheduler$WorkerState r3 = kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.TERMINATED
            if (r2 != r3) goto L_0x001d
            goto L_0x005d
        L_0x001d:
            kotlinx.coroutines.scheduling.TaskMode r2 = r6.getMode()
            kotlinx.coroutines.scheduling.TaskMode r3 = kotlinx.coroutines.scheduling.TaskMode.NON_BLOCKING
            r4 = 0
            if (r2 != r3) goto L_0x003a
            kotlinx.coroutines.scheduling.CoroutineScheduler$WorkerState r2 = r7.state
            kotlinx.coroutines.scheduling.CoroutineScheduler$WorkerState r3 = kotlinx.coroutines.scheduling.CoroutineScheduler.WorkerState.BLOCKING
            if (r2 != r3) goto L_0x002e
            r2 = r1
            goto L_0x002f
        L_0x002e:
            r2 = r4
        L_0x002f:
            if (r2 == 0) goto L_0x0033
            r2 = r4
            goto L_0x003b
        L_0x0033:
            boolean r2 = r7.tryAcquireCpuPermit()
            if (r2 != 0) goto L_0x003a
            goto L_0x005d
        L_0x003a:
            r2 = r0
        L_0x003b:
            if (r8 == 0) goto L_0x0046
            kotlinx.coroutines.scheduling.WorkQueue r8 = r7.localQueue
            kotlinx.coroutines.scheduling.GlobalQueue r3 = r5.globalQueue
            boolean r8 = r8.addLast(r6, r3)
            goto L_0x004e
        L_0x0046:
            kotlinx.coroutines.scheduling.WorkQueue r8 = r7.localQueue
            kotlinx.coroutines.scheduling.GlobalQueue r3 = r5.globalQueue
            boolean r8 = r8.add(r6, r3)
        L_0x004e:
            if (r8 == 0) goto L_0x005e
            kotlinx.coroutines.scheduling.WorkQueue r7 = r7.localQueue
            int r7 = r7.getBufferSize$kotlinx_coroutines_core()
            int r8 = kotlinx.coroutines.scheduling.TasksKt.QUEUE_SIZE_OFFLOAD_THRESHOLD
            if (r7 <= r8) goto L_0x005b
            goto L_0x005e
        L_0x005b:
            r4 = r2
            goto L_0x005e
        L_0x005d:
            r4 = r1
        L_0x005e:
            if (r4 == r0) goto L_0x0085
            if (r4 == r1) goto L_0x0066
            r5.requestCpuWorker()
            goto L_0x0071
        L_0x0066:
            kotlinx.coroutines.scheduling.GlobalQueue r7 = r5.globalQueue
            boolean r6 = r7.addLast(r6)
            if (r6 == 0) goto L_0x0072
            r5.requestCpuWorker()
        L_0x0071:
            return
        L_0x0072:
            java.util.concurrent.RejectedExecutionException r6 = new java.util.concurrent.RejectedExecutionException
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r5 = r5.schedulerName
            java.lang.String r8 = " was terminated"
            java.lang.String r5 = android.support.v4.app.FragmentTabHost$SavedState$$ExternalSyntheticOutline0.m(r7, r5, r8)
            r6.<init>(r5)
            throw r6
        L_0x0085:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.scheduling.CoroutineScheduler.dispatch(java.lang.Runnable, kotlinx.coroutines.scheduling.TaskContext, boolean):void");
    }

    @Override // java.util.concurrent.Executor
    public void execute(@NotNull Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(runnable, "command");
        dispatch(runnable, NonBlockingContext.INSTANCE, false);
    }

    public final int parkedWorkersStackNextIndex(Worker worker) {
        Object obj = worker.nextParkedWorker;
        while (obj != NOT_IN_STACK) {
            if (obj == null) {
                return 0;
            }
            Worker worker2 = (Worker) obj;
            int i = worker2.indexInArray;
            if (i != 0) {
                return i;
            }
            obj = worker2.nextParkedWorker;
        }
        return -1;
    }

    public final void requestCpuWorker() {
        if (this.cpuPermits.availablePermits() == 0) {
            tryUnpark();
        } else if (!tryUnpark()) {
            long j = this.controlState;
            if (((int) (2097151 & j)) - ((int) ((j & 4398044413952L) >> 21)) < this.corePoolSize) {
                int createNewWorker = createNewWorker();
                if (createNewWorker == 1 && this.corePoolSize > 1) {
                    createNewWorker();
                }
                if (createNewWorker > 0) {
                    return;
                }
            }
            tryUnpark();
        }
    }

    public final void runSafely(Task task) {
        try {
            task.run();
        } finally {
        }
    }

    @Override // java.lang.Object
    @NotNull
    public String toString() {
        ArrayList arrayList = new ArrayList();
        Worker[] workerArr = this.workers;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        for (Worker worker : workerArr) {
            if (worker != null) {
                WorkQueue workQueue = worker.localQueue;
                Object obj = workQueue.lastScheduledTask;
                int bufferSize$kotlinx_coroutines_core = workQueue.getBufferSize$kotlinx_coroutines_core();
                if (obj != null) {
                    bufferSize$kotlinx_coroutines_core++;
                }
                int ordinal = worker.state.ordinal();
                if (ordinal == 0) {
                    i++;
                    arrayList.add(String.valueOf(bufferSize$kotlinx_coroutines_core) + "c");
                } else if (ordinal == 1) {
                    i2++;
                    arrayList.add(String.valueOf(bufferSize$kotlinx_coroutines_core) + "b");
                } else if (ordinal == 2) {
                    i3++;
                } else if (ordinal == 3) {
                    i4++;
                    if (bufferSize$kotlinx_coroutines_core > 0) {
                        arrayList.add(String.valueOf(bufferSize$kotlinx_coroutines_core) + "r");
                    }
                } else if (ordinal == 4) {
                    i5++;
                }
            }
        }
        long j = this.controlState;
        StringBuilder sb = new StringBuilder();
        sb.append(this.schedulerName);
        sb.append('@');
        sb.append(DebugStringsKt.getHexAddress(this));
        sb.append('[');
        sb.append("Pool Size {");
        sb.append("core = ");
        sb.append(this.corePoolSize);
        sb.append(", ");
        sb.append("max = ");
        sb.append(this.maxPoolSize);
        sb.append("}, ");
        sb.append("Worker States {");
        sb.append("CPU = ");
        sb.append(i);
        sb.append(", ");
        sb.append("blocking = ");
        sb.append(i2);
        sb.append(", ");
        sb.append("parked = ");
        sb.append(i3);
        sb.append(", ");
        sb.append("retired = ");
        sb.append(i4);
        sb.append(", ");
        sb.append("terminated = ");
        sb.append(i5);
        sb.append("}, ");
        sb.append("running workers queues = ");
        sb.append(arrayList);
        sb.append(", ");
        sb.append("global queue size = ");
        long j2 = ((LockFreeTaskQueueCore) this.globalQueue._cur$internal)._state$internal;
        sb.append(1073741823 & (((int) ((j2 & 1152921503533105152L) >> 30)) - ((int) ((1073741823 & j2) >> 0))));
        sb.append(", ");
        sb.append("Control State Workers {");
        sb.append("created = ");
        sb.append((int) (2097151 & j));
        sb.append(", ");
        sb.append("blocking = ");
        sb.append((int) ((j & 4398044413952L) >> 21));
        sb.append('}');
        sb.append("]");
        return sb.toString();
    }

    public final boolean tryUnpark() {
        while (true) {
            long j = this.parkedWorkersStack;
            Worker worker = this.workers[(int) (2097151 & j)];
            if (worker != null) {
                long j2 = (2097152 + j) & -2097152;
                int parkedWorkersStackNextIndex = parkedWorkersStackNextIndex(worker);
                if (parkedWorkersStackNextIndex >= 0 && parkedWorkersStack$FU.compareAndSet(this, j, ((long) parkedWorkersStackNextIndex) | j2)) {
                    worker.nextParkedWorker = NOT_IN_STACK;
                }
            } else {
                worker = null;
            }
            boolean z = false;
            if (worker == null) {
                return false;
            }
            worker.parkTimeNs = MIN_PARK_TIME_NS;
            worker.spins = 0;
            boolean z2 = worker.state == WorkerState.PARKING;
            LockSupport.unpark(worker);
            if (z2) {
                int i = worker.terminationState;
                if (!(i == 1 || i == -1)) {
                    if (i == 0) {
                        z = Worker.terminationState$FU.compareAndSet(worker, 0, -1);
                    } else {
                        throw new IllegalStateException(ExifInterface$$ExternalSyntheticOutline0.m("Invalid terminationState = ", i).toString());
                    }
                }
                if (z) {
                    return true;
                }
            } else {
                continue;
            }
        }
    }
}
