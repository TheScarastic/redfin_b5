package kotlinx.coroutines.scheduling;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class WorkQueue {
    public static final AtomicReferenceFieldUpdater lastScheduledTask$FU = AtomicReferenceFieldUpdater.newUpdater(WorkQueue.class, Object.class, "lastScheduledTask");
    public static final AtomicIntegerFieldUpdater producerIndex$FU = AtomicIntegerFieldUpdater.newUpdater(WorkQueue.class, "producerIndex");
    public static final AtomicIntegerFieldUpdater consumerIndex$FU = AtomicIntegerFieldUpdater.newUpdater(WorkQueue.class, "consumerIndex");
    public final AtomicReferenceArray<Task> buffer = new AtomicReferenceArray<>(128);
    public volatile Object lastScheduledTask = null;
    public volatile int producerIndex = 0;
    public volatile int consumerIndex = 0;

    public final boolean add(@NotNull Task task, @NotNull GlobalQueue globalQueue) {
        Intrinsics.checkParameterIsNotNull(globalQueue, "globalQueue");
        Task task2 = (Task) lastScheduledTask$FU.getAndSet(this, task);
        if (task2 != null) {
            return addLast(task2, globalQueue);
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x006a A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean addLast(@org.jetbrains.annotations.NotNull kotlinx.coroutines.scheduling.Task r10, @org.jetbrains.annotations.NotNull kotlinx.coroutines.scheduling.GlobalQueue r11) {
        /*
            r9 = this;
            java.lang.String r0 = "globalQueue"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r11, r0)
            r0 = 0
            r1 = 1
            r2 = r1
        L_0x0008:
            int r3 = r9.getBufferSize$kotlinx_coroutines_core()
            r4 = 127(0x7f, float:1.78E-43)
            if (r3 != r4) goto L_0x0011
            goto L_0x001c
        L_0x0011:
            int r3 = r9.producerIndex
            r3 = r3 & r4
            java.util.concurrent.atomic.AtomicReferenceArray<kotlinx.coroutines.scheduling.Task> r4 = r9.buffer
            java.lang.Object r4 = r4.get(r3)
            if (r4 == 0) goto L_0x001e
        L_0x001c:
            r3 = r0
            goto L_0x0029
        L_0x001e:
            java.util.concurrent.atomic.AtomicReferenceArray<kotlinx.coroutines.scheduling.Task> r4 = r9.buffer
            r4.lazySet(r3, r10)
            java.util.concurrent.atomic.AtomicIntegerFieldUpdater r3 = kotlinx.coroutines.scheduling.WorkQueue.producerIndex$FU
            r3.incrementAndGet(r9)
            r3 = r1
        L_0x0029:
            if (r3 != 0) goto L_0x006a
            int r2 = r9.getBufferSize$kotlinx_coroutines_core()
            int r2 = r2 / 2
            if (r2 >= r1) goto L_0x0034
            r2 = r1
        L_0x0034:
            r3 = r0
        L_0x0035:
            if (r3 >= r2) goto L_0x0068
        L_0x0037:
            int r4 = r9.consumerIndex
            int r5 = r9.producerIndex
            int r5 = r4 - r5
            r6 = 0
            if (r5 != 0) goto L_0x0041
            goto L_0x0060
        L_0x0041:
            r5 = r4 & 127(0x7f, float:1.78E-43)
            java.util.concurrent.atomic.AtomicReferenceArray<kotlinx.coroutines.scheduling.Task> r7 = r9.buffer
            java.lang.Object r7 = r7.get(r5)
            kotlinx.coroutines.scheduling.Task r7 = (kotlinx.coroutines.scheduling.Task) r7
            if (r7 == 0) goto L_0x0037
            java.util.concurrent.atomic.AtomicIntegerFieldUpdater r7 = kotlinx.coroutines.scheduling.WorkQueue.consumerIndex$FU
            int r8 = r4 + 1
            boolean r4 = r7.compareAndSet(r9, r4, r8)
            if (r4 == 0) goto L_0x0037
            java.util.concurrent.atomic.AtomicReferenceArray<kotlinx.coroutines.scheduling.Task> r4 = r9.buffer
            java.lang.Object r4 = r4.getAndSet(r5, r6)
            r6 = r4
            kotlinx.coroutines.scheduling.Task r6 = (kotlinx.coroutines.scheduling.Task) r6
        L_0x0060:
            if (r6 == 0) goto L_0x0068
            r9.addToGlobalQueue(r11, r6)
            int r3 = r3 + 1
            goto L_0x0035
        L_0x0068:
            r2 = r0
            goto L_0x0008
        L_0x006a:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.scheduling.WorkQueue.addLast(kotlinx.coroutines.scheduling.Task, kotlinx.coroutines.scheduling.GlobalQueue):boolean");
    }

    public final void addToGlobalQueue(GlobalQueue globalQueue, Task task) {
        if (!globalQueue.addLast(task)) {
            throw new IllegalStateException("GlobalQueue could not be closed yet".toString());
        }
    }

    public final int getBufferSize$kotlinx_coroutines_core() {
        return this.producerIndex - this.consumerIndex;
    }

    @Nullable
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
            if (this.buffer.get(i2) != null && consumerIndex$FU.compareAndSet(this, i, i + 1)) {
                return this.buffer.getAndSet(i2, null);
            }
        }
    }
}
