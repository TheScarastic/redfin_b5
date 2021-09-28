package kotlinx.coroutines.scheduling;

import kotlinx.coroutines.internal.LockFreeTaskQueue;
/* loaded from: classes.dex */
public class GlobalQueue extends LockFreeTaskQueue<Task> {
    public GlobalQueue() {
        super(false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0078, code lost:
        r7 = r9;
     */
    @org.jetbrains.annotations.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final kotlinx.coroutines.scheduling.Task removeFirstWithModeOrNull(@org.jetbrains.annotations.NotNull kotlinx.coroutines.scheduling.TaskMode r12) {
        /*
            r11 = this;
        L_0x0000:
            java.lang.Object r0 = r11._cur$internal
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r0 = (kotlinx.coroutines.internal.LockFreeTaskQueueCore) r0
        L_0x0004:
            long r3 = r0._state$internal
            r1 = 1152921504606846976(0x1000000000000000, double:1.2882297539194267E-231)
            long r1 = r1 & r3
            r5 = 0
            int r1 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            r7 = 0
            if (r1 == 0) goto L_0x0014
            kotlinx.coroutines.internal.Symbol r7 = kotlinx.coroutines.internal.LockFreeTaskQueueCore.REMOVE_FROZEN
            goto L_0x0079
        L_0x0014:
            kotlinx.coroutines.internal.LockFreeTaskQueueCore$Companion r1 = kotlinx.coroutines.internal.LockFreeTaskQueueCore.Companion
            r5 = 1073741823(0x3fffffff, double:5.304989472E-315)
            long r5 = r5 & r3
            r2 = 0
            long r5 = r5 >> r2
            int r8 = (int) r5
            r5 = 1152921503533105152(0xfffffffc0000000, double:1.2882296003504729E-231)
            long r5 = r5 & r3
            r9 = 30
            long r5 = r5 >> r9
            int r5 = (int) r5
            int r6 = r0.mask
            r5 = r5 & r6
            r6 = r6 & r8
            if (r5 != r6) goto L_0x002e
            goto L_0x0079
        L_0x002e:
            java.util.concurrent.atomic.AtomicReferenceArray r5 = r0.array$internal
            java.lang.Object r9 = r5.get(r6)
            if (r9 != 0) goto L_0x003b
            boolean r1 = r0.singleConsumer
            if (r1 == 0) goto L_0x0004
            goto L_0x0079
        L_0x003b:
            boolean r5 = r9 instanceof kotlinx.coroutines.internal.LockFreeTaskQueueCore.Placeholder
            if (r5 == 0) goto L_0x0040
            goto L_0x0079
        L_0x0040:
            r5 = r9
            kotlinx.coroutines.scheduling.Task r5 = (kotlinx.coroutines.scheduling.Task) r5
            kotlinx.coroutines.scheduling.TaskMode r5 = r5.getMode()
            if (r5 != r12) goto L_0x004a
            r2 = 1
        L_0x004a:
            if (r2 != 0) goto L_0x004d
            goto L_0x0079
        L_0x004d:
            int r2 = r8 + 1
            r5 = 1073741823(0x3fffffff, float:1.9999999)
            r10 = r2 & r5
            java.util.concurrent.atomic.AtomicLongFieldUpdater r2 = kotlinx.coroutines.internal.LockFreeTaskQueueCore._state$FU$internal
            long r5 = r1.updateHead(r3, r10)
            r1 = r2
            r2 = r0
            boolean r1 = r1.compareAndSet(r2, r3, r5)
            if (r1 == 0) goto L_0x006b
            java.util.concurrent.atomic.AtomicReferenceArray r1 = r0.array$internal
            int r2 = r0.mask
            r2 = r2 & r8
            r1.set(r2, r7)
            goto L_0x0078
        L_0x006b:
            boolean r1 = r0.singleConsumer
            if (r1 != 0) goto L_0x0070
            goto L_0x0004
        L_0x0070:
            r1 = r0
        L_0x0071:
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r1 = kotlinx.coroutines.internal.LockFreeTaskQueueCore.access$removeSlowPath(r1, r8, r10)
            if (r1 == 0) goto L_0x0078
            goto L_0x0071
        L_0x0078:
            r7 = r9
        L_0x0079:
            kotlinx.coroutines.internal.Symbol r1 = kotlinx.coroutines.internal.LockFreeTaskQueueCore.REMOVE_FROZEN
            if (r7 == r1) goto L_0x0080
            kotlinx.coroutines.scheduling.Task r7 = (kotlinx.coroutines.scheduling.Task) r7
            return r7
        L_0x0080:
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater r1 = kotlinx.coroutines.internal.LockFreeTaskQueue._cur$FU$internal
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r2 = r0.next()
            r1.compareAndSet(r11, r0, r2)
            goto L_0x0000
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.scheduling.GlobalQueue.removeFirstWithModeOrNull(kotlinx.coroutines.scheduling.TaskMode):kotlinx.coroutines.scheduling.Task");
    }
}
