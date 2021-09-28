package kotlinx.coroutines.internal;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlinx.coroutines.DebugKt;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class LockFreeTaskQueueCore<E> {
    public static final Companion Companion = new Companion(null);
    @NotNull
    public static final Symbol REMOVE_FROZEN = new Symbol("REMOVE_FROZEN");
    public static final AtomicReferenceFieldUpdater _next$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeTaskQueueCore.class, Object.class, "_next");
    public static final /* synthetic */ AtomicLongFieldUpdater _state$FU$internal = AtomicLongFieldUpdater.newUpdater(LockFreeTaskQueueCore.class, "_state$internal");
    public volatile Object _next = null;
    public volatile /* synthetic */ long _state$internal = 0;
    public /* synthetic */ AtomicReferenceArray array$internal;
    public final int capacity;
    public final int mask;
    public final boolean singleConsumer;

    /* loaded from: classes.dex */
    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
        }

        public final long updateHead(long j, int i) {
            return (j & -1073741824) | (((long) i) << 0);
        }
    }

    /* loaded from: classes.dex */
    public static final class Placeholder {
        public final int index;

        public Placeholder(int i) {
            this.index = i;
        }
    }

    public LockFreeTaskQueueCore(int i, boolean z) {
        this.capacity = i;
        this.singleConsumer = z;
        int i2 = i - 1;
        this.mask = i2;
        this.array$internal = new AtomicReferenceArray(i);
        boolean z2 = false;
        if (i2 <= 1073741823) {
            if (!((i & i2) == 0 ? true : z2)) {
                throw new IllegalStateException("Check failed.".toString());
            }
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    public static final LockFreeTaskQueueCore access$removeSlowPath(LockFreeTaskQueueCore lockFreeTaskQueueCore, int i, int i2) {
        long j;
        Companion companion;
        int i3;
        do {
            j = lockFreeTaskQueueCore._state$internal;
            companion = Companion;
            i3 = (int) ((1073741823 & j) >> 0);
            boolean z = DebugKt.DEBUG;
            if ((1152921504606846976L & j) != 0) {
                return lockFreeTaskQueueCore.next();
            }
        } while (!_state$FU$internal.compareAndSet(lockFreeTaskQueueCore, j, companion.updateHead(j, i2)));
        lockFreeTaskQueueCore.array$internal.set(lockFreeTaskQueueCore.mask & i3, null);
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0053, code lost:
        return 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int addLast(@org.jetbrains.annotations.NotNull E r15) {
        /*
            r14 = this;
            java.lang.String r0 = "element"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r15, r0)
        L_0x0005:
            long r3 = r14._state$internal
            r0 = 3458764513820540928(0x3000000000000000, double:1.727233711018889E-77)
            long r0 = r0 & r3
            r7 = 0
            int r0 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1))
            r1 = 1
            if (r0 == 0) goto L_0x001a
            r14 = 2305843009213693952(0x2000000000000000, double:1.4916681462400413E-154)
            long r14 = r14 & r3
            int r14 = (r14 > r7 ? 1 : (r14 == r7 ? 0 : -1))
            if (r14 == 0) goto L_0x0019
            r1 = 2
        L_0x0019:
            return r1
        L_0x001a:
            r5 = 1073741823(0x3fffffff, double:5.304989472E-315)
            long r5 = r5 & r3
            r0 = 0
            long r5 = r5 >> r0
            int r2 = (int) r5
            r5 = 1152921503533105152(0xfffffffc0000000, double:1.2882296003504729E-231)
            long r5 = r5 & r3
            r9 = 30
            long r5 = r5 >> r9
            int r10 = (int) r5
            int r11 = r14.mask
            int r5 = r10 + 2
            r5 = r5 & r11
            r6 = r2 & r11
            if (r5 != r6) goto L_0x0035
            return r1
        L_0x0035:
            boolean r5 = r14.singleConsumer
            r6 = 1073741823(0x3fffffff, float:1.9999999)
            if (r5 != 0) goto L_0x0054
            java.util.concurrent.atomic.AtomicReferenceArray r5 = r14.array$internal
            r12 = r10 & r11
            java.lang.Object r5 = r5.get(r12)
            if (r5 == 0) goto L_0x0054
            int r0 = r14.capacity
            r3 = 1024(0x400, float:1.435E-42)
            if (r0 < r3) goto L_0x0053
            int r10 = r10 - r2
            r2 = r10 & r6
            int r0 = r0 >> 1
            if (r2 <= r0) goto L_0x0005
        L_0x0053:
            return r1
        L_0x0054:
            int r1 = r10 + 1
            r1 = r1 & r6
            java.util.concurrent.atomic.AtomicLongFieldUpdater r2 = kotlinx.coroutines.internal.LockFreeTaskQueueCore._state$FU$internal
            r5 = -1152921503533105153(0xf00000003fffffff, double:-3.1050369248997324E231)
            long r5 = r5 & r3
            long r12 = (long) r1
            long r12 = r12 << r9
            long r5 = r5 | r12
            r1 = r2
            r2 = r14
            boolean r1 = r1.compareAndSet(r2, r3, r5)
            if (r1 == 0) goto L_0x0005
            java.util.concurrent.atomic.AtomicReferenceArray r1 = r14.array$internal
            r2 = r10 & r11
            r1.set(r2, r15)
        L_0x0071:
            long r1 = r14._state$internal
            r3 = 1152921504606846976(0x1000000000000000, double:1.2882297539194267E-231)
            long r1 = r1 & r3
            int r1 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r1 != 0) goto L_0x007b
            goto L_0x009f
        L_0x007b:
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r14 = r14.next()
            java.util.concurrent.atomic.AtomicReferenceArray r1 = r14.array$internal
            int r2 = r14.mask
            r2 = r2 & r10
            java.lang.Object r1 = r1.get(r2)
            boolean r2 = r1 instanceof kotlinx.coroutines.internal.LockFreeTaskQueueCore.Placeholder
            if (r2 == 0) goto L_0x009b
            kotlinx.coroutines.internal.LockFreeTaskQueueCore$Placeholder r1 = (kotlinx.coroutines.internal.LockFreeTaskQueueCore.Placeholder) r1
            int r1 = r1.index
            if (r1 != r10) goto L_0x009b
            java.util.concurrent.atomic.AtomicReferenceArray r1 = r14.array$internal
            int r2 = r14.mask
            r2 = r2 & r10
            r1.set(r2, r15)
            goto L_0x009c
        L_0x009b:
            r14 = 0
        L_0x009c:
            if (r14 == 0) goto L_0x009f
            goto L_0x0071
        L_0x009f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.LockFreeTaskQueueCore.addLast(java.lang.Object):int");
    }

    public final boolean close() {
        long j;
        do {
            j = this._state$internal;
            if ((j & 2305843009213693952L) != 0) {
                return true;
            }
            if ((1152921504606846976L & j) != 0) {
                return false;
            }
        } while (!_state$FU$internal.compareAndSet(this, j, j | 2305843009213693952L));
        return true;
    }

    public final boolean isEmpty() {
        long j = this._state$internal;
        return ((int) ((1073741823 & j) >> 0)) == ((int) ((j & 1152921503533105152L) >> 30));
    }

    /* JADX DEBUG: Multi-variable search result rejected for r8v2, resolved type: java.util.concurrent.atomic.AtomicReferenceArray */
    /* JADX WARN: Multi-variable type inference failed */
    @NotNull
    public final LockFreeTaskQueueCore<E> next() {
        long j;
        while (true) {
            j = this._state$internal;
            if ((j & 1152921504606846976L) == 0) {
                long j2 = j | 1152921504606846976L;
                if (_state$FU$internal.compareAndSet(this, j, j2)) {
                    j = j2;
                    break;
                }
            } else {
                break;
            }
        }
        while (true) {
            LockFreeTaskQueueCore<E> lockFreeTaskQueueCore = (LockFreeTaskQueueCore) this._next;
            if (lockFreeTaskQueueCore != null) {
                return lockFreeTaskQueueCore;
            }
            AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = _next$FU;
            LockFreeTaskQueueCore lockFreeTaskQueueCore2 = new LockFreeTaskQueueCore(this.capacity * 2, this.singleConsumer);
            int i = (int) ((1073741823 & j) >> 0);
            int i2 = (int) ((1152921503533105152L & j) >> 30);
            while (true) {
                int i3 = this.mask;
                int i4 = i & i3;
                if (i4 != (i3 & i2)) {
                    Object obj = this.array$internal.get(i4);
                    if (obj == null) {
                        obj = new Placeholder(i);
                    }
                    lockFreeTaskQueueCore2.array$internal.set(lockFreeTaskQueueCore2.mask & i, obj);
                    i++;
                }
            }
            lockFreeTaskQueueCore2._state$internal = -1152921504606846977L & j;
            atomicReferenceFieldUpdater.compareAndSet(this, null, lockFreeTaskQueueCore2);
        }
    }
}
