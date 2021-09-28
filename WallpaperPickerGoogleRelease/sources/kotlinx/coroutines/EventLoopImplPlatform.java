package kotlinx.coroutines;

import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public abstract class EventLoopImplPlatform extends EventLoop {
    @NotNull
    public abstract Thread getThread();

    /* JADX WARNING: Removed duplicated region for block: B:37:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0082  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void reschedule(long r13, @org.jetbrains.annotations.NotNull kotlinx.coroutines.EventLoopImplBase.DelayedTask r15) {
        /*
            r12 = this;
            boolean r12 = kotlinx.coroutines.DebugKt.DEBUG
            kotlinx.coroutines.DefaultExecutor r12 = kotlinx.coroutines.DefaultExecutor.INSTANCE
            java.util.Objects.requireNonNull(r12)
            boolean r0 = r12.isCompleted
            r1 = 0
            r2 = 2
            r3 = 1
            r4 = 0
            if (r0 == 0) goto L_0x0010
            goto L_0x003e
        L_0x0010:
            java.lang.Object r0 = r12._delayed
            kotlinx.coroutines.EventLoopImplBase$DelayedTaskQueue r0 = (kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue) r0
            if (r0 == 0) goto L_0x0017
            goto L_0x0027
        L_0x0017:
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater r0 = kotlinx.coroutines.EventLoopImplBase._delayed$FU
            kotlinx.coroutines.EventLoopImplBase$DelayedTaskQueue r5 = new kotlinx.coroutines.EventLoopImplBase$DelayedTaskQueue
            r5.<init>(r13)
            r0.compareAndSet(r12, r4, r5)
            java.lang.Object r0 = r12._delayed
            if (r0 == 0) goto L_0x00ae
            kotlinx.coroutines.EventLoopImplBase$DelayedTaskQueue r0 = (kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue) r0
        L_0x0027:
            monitor-enter(r15)
            java.lang.Object r5 = r15._heap     // Catch: all -> 0x00ab
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.EventLoop_commonKt.DISPOSED_TASK     // Catch: all -> 0x00ab
            if (r5 != r6) goto L_0x0031
            monitor-exit(r15)
            r0 = r2
            goto L_0x006b
        L_0x0031:
            monitor-enter(r0)     // Catch: all -> 0x00ab
            kotlinx.coroutines.internal.ThreadSafeHeapNode r5 = r0.firstImpl()     // Catch: all -> 0x00a8
            kotlinx.coroutines.EventLoopImplBase$DelayedTask r5 = (kotlinx.coroutines.EventLoopImplBase.DelayedTask) r5     // Catch: all -> 0x00a8
            boolean r6 = r12.isCompleted     // Catch: all -> 0x00a8
            if (r6 == 0) goto L_0x0040
            monitor-exit(r0)     // Catch: all -> 0x00ab
            monitor-exit(r15)
        L_0x003e:
            r0 = r3
            goto L_0x006b
        L_0x0040:
            r6 = 0
            if (r5 != 0) goto L_0x0047
            r0.timeNow = r13     // Catch: all -> 0x00a8
            goto L_0x005a
        L_0x0047:
            long r8 = r5.nanoTime     // Catch: all -> 0x00a8
            long r10 = r8 - r13
            int r5 = (r10 > r6 ? 1 : (r10 == r6 ? 0 : -1))
            if (r5 < 0) goto L_0x0050
            r8 = r13
        L_0x0050:
            long r10 = r0.timeNow     // Catch: all -> 0x00a8
            long r10 = r8 - r10
            int r5 = (r10 > r6 ? 1 : (r10 == r6 ? 0 : -1))
            if (r5 <= 0) goto L_0x005a
            r0.timeNow = r8     // Catch: all -> 0x00a8
        L_0x005a:
            long r8 = r15.nanoTime     // Catch: all -> 0x00a8
            long r10 = r0.timeNow     // Catch: all -> 0x00a8
            long r8 = r8 - r10
            int r5 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
            if (r5 >= 0) goto L_0x0065
            r15.nanoTime = r10     // Catch: all -> 0x00a8
        L_0x0065:
            r0.addImpl(r15)     // Catch: all -> 0x00a8
            monitor-exit(r0)     // Catch: all -> 0x00ab
            monitor-exit(r15)
            r0 = r1
        L_0x006b:
            if (r0 == 0) goto L_0x0082
            if (r0 == r3) goto L_0x007e
            if (r0 != r2) goto L_0x0072
            goto L_0x00a7
        L_0x0072:
            java.lang.String r12 = "unexpected result"
            java.lang.IllegalStateException r13 = new java.lang.IllegalStateException
            java.lang.String r12 = r12.toString()
            r13.<init>(r12)
            throw r13
        L_0x007e:
            r12.reschedule(r13, r15)
            goto L_0x00a7
        L_0x0082:
            java.lang.Object r13 = r12._delayed
            kotlinx.coroutines.EventLoopImplBase$DelayedTaskQueue r13 = (kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue) r13
            if (r13 == 0) goto L_0x0095
            monitor-enter(r13)
            kotlinx.coroutines.internal.ThreadSafeHeapNode r14 = r13.firstImpl()     // Catch: all -> 0x0092
            monitor-exit(r13)
            r4 = r14
            kotlinx.coroutines.EventLoopImplBase$DelayedTask r4 = (kotlinx.coroutines.EventLoopImplBase.DelayedTask) r4
            goto L_0x0095
        L_0x0092:
            r12 = move-exception
            monitor-exit(r13)
            throw r12
        L_0x0095:
            if (r4 != r15) goto L_0x0098
            r1 = r3
        L_0x0098:
            if (r1 == 0) goto L_0x00a7
            java.lang.Thread r12 = r12.getThread()
            java.lang.Thread r13 = java.lang.Thread.currentThread()
            if (r13 == r12) goto L_0x00a7
            java.util.concurrent.locks.LockSupport.unpark(r12)
        L_0x00a7:
            return
        L_0x00a8:
            r12 = move-exception
            monitor-exit(r0)     // Catch: all -> 0x00ab
            throw r12     // Catch: all -> 0x00ab
        L_0x00ab:
            r12 = move-exception
            monitor-exit(r15)
            throw r12
        L_0x00ae:
            kotlin.jvm.internal.Intrinsics.throwNpe()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.EventLoopImplPlatform.reschedule(long, kotlinx.coroutines.EventLoopImplBase$DelayedTask):void");
    }
}
