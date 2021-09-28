package kotlinx.coroutines.internal;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlinx.coroutines.DebugKt;
/* compiled from: LockFreeTaskQueue.kt */
/* loaded from: classes2.dex */
public final class LockFreeTaskQueueCore<E> {
    public static final Companion Companion = new Companion(null);
    public static final Symbol REMOVE_FROZEN = new Symbol("REMOVE_FROZEN");
    private static final AtomicReferenceFieldUpdater _next$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeTaskQueueCore.class, Object.class, "_next");
    public static final /* synthetic */ AtomicLongFieldUpdater _state$FU$internal = AtomicLongFieldUpdater.newUpdater(LockFreeTaskQueueCore.class, "_state$internal");
    private volatile Object _next = null;
    public volatile /* synthetic */ long _state$internal = 0;
    public /* synthetic */ AtomicReferenceArray array$internal;
    private final int capacity;
    private final int mask;
    private final boolean singleConsumer;

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

    public final boolean isEmpty() {
        long j = this._state$internal;
        return ((int) ((1073741823 & j) >> 0)) == ((int) ((j & 1152921503533105152L) >> 30));
    }

    public final int getSize() {
        long j = this._state$internal;
        return 1073741823 & (((int) ((j & 1152921503533105152L) >> 30)) - ((int) ((1073741823 & j) >> 0)));
    }

    private final LockFreeTaskQueueCore<E> fillPlaceholder(int i, E e) {
        Object obj = this.array$internal.get(this.mask & i);
        if (!(obj instanceof Placeholder) || ((Placeholder) obj).index != i) {
            return null;
        }
        this.array$internal.set(i & this.mask, e);
        return this;
    }

    public final LockFreeTaskQueueCore<E> next() {
        return allocateOrGetNextCopy(markFrozen());
    }

    /* JADX DEBUG: Multi-variable search result rejected for r4v3, resolved type: java.util.concurrent.atomic.AtomicReferenceArray */
    /* JADX WARN: Multi-variable type inference failed */
    private final LockFreeTaskQueueCore<E> allocateNextCopy(long j) {
        LockFreeTaskQueueCore<E> lockFreeTaskQueueCore = new LockFreeTaskQueueCore<>(this.capacity * 2, this.singleConsumer);
        int i = (int) ((1073741823 & j) >> 0);
        int i2 = (int) ((1152921503533105152L & j) >> 30);
        while (true) {
            int i3 = this.mask;
            if ((i & i3) != (i2 & i3)) {
                Object obj = this.array$internal.get(i3 & i);
                if (obj == null) {
                    obj = new Placeholder(i);
                }
                lockFreeTaskQueueCore.array$internal.set(lockFreeTaskQueueCore.mask & i, obj);
                i++;
            } else {
                lockFreeTaskQueueCore._state$internal = Companion.wo(j, 1152921504606846976L);
                return lockFreeTaskQueueCore;
            }
        }
    }

    /* compiled from: LockFreeTaskQueue.kt */
    /* loaded from: classes2.dex */
    public static final class Placeholder {
        public final int index;

        public Placeholder(int i) {
            this.index = i;
        }
    }

    /* compiled from: LockFreeTaskQueue.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public final int addFailReason(long j) {
            return (j & 2305843009213693952L) != 0 ? 2 : 1;
        }

        public final long wo(long j, long j2) {
            return j & (~j2);
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final long updateHead(long j, int i) {
            return wo(j, 1073741823) | (((long) i) << 0);
        }

        public final long updateTail(long j, int i) {
            return wo(j, 1152921503533105152L) | (((long) i) << 30);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0053, code lost:
        return 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int addLast(E r14) {
        /*
            r13 = this;
            java.lang.String r0 = "element"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r14, r0)
        L_0x0005:
            long r3 = r13._state$internal
            r0 = 3458764513820540928(0x3000000000000000, double:1.727233711018889E-77)
            long r0 = r0 & r3
            r7 = 0
            int r0 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1))
            if (r0 == 0) goto L_0x0017
            kotlinx.coroutines.internal.LockFreeTaskQueueCore$Companion r13 = kotlinx.coroutines.internal.LockFreeTaskQueueCore.Companion
            int r13 = r13.addFailReason(r3)
            return r13
        L_0x0017:
            kotlinx.coroutines.internal.LockFreeTaskQueueCore$Companion r0 = kotlinx.coroutines.internal.LockFreeTaskQueueCore.Companion
            r1 = 1073741823(0x3fffffff, double:5.304989472E-315)
            long r1 = r1 & r3
            r9 = 0
            long r1 = r1 >> r9
            int r1 = (int) r1
            r5 = 1152921503533105152(0xfffffffc0000000, double:1.2882296003504729E-231)
            long r5 = r5 & r3
            r2 = 30
            long r5 = r5 >> r2
            int r10 = (int) r5
            int r11 = r13.mask
            int r2 = r10 + 2
            r2 = r2 & r11
            r5 = r1 & r11
            r6 = 1
            if (r2 != r5) goto L_0x0035
            return r6
        L_0x0035:
            boolean r2 = r13.singleConsumer
            r5 = 1073741823(0x3fffffff, float:1.9999999)
            if (r2 != 0) goto L_0x0054
            java.util.concurrent.atomic.AtomicReferenceArray r2 = r13.array$internal
            r12 = r10 & r11
            java.lang.Object r2 = r2.get(r12)
            if (r2 == 0) goto L_0x0054
            int r0 = r13.capacity
            r2 = 1024(0x400, float:1.435E-42)
            if (r0 < r2) goto L_0x0053
            int r10 = r10 - r1
            r1 = r10 & r5
            int r0 = r0 >> 1
            if (r1 <= r0) goto L_0x0005
        L_0x0053:
            return r6
        L_0x0054:
            int r1 = r10 + 1
            r1 = r1 & r5
            java.util.concurrent.atomic.AtomicLongFieldUpdater r2 = kotlinx.coroutines.internal.LockFreeTaskQueueCore._state$FU$internal
            long r5 = r0.updateTail(r3, r1)
            r1 = r2
            r2 = r13
            boolean r0 = r1.compareAndSet(r2, r3, r5)
            if (r0 == 0) goto L_0x0005
            java.util.concurrent.atomic.AtomicReferenceArray r0 = r13.array$internal
            r1 = r10 & r11
            r0.set(r1, r14)
        L_0x006c:
            long r0 = r13._state$internal
            r2 = 1152921504606846976(0x1000000000000000, double:1.2882297539194267E-231)
            long r0 = r0 & r2
            int r0 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1))
            if (r0 != 0) goto L_0x0076
            goto L_0x0081
        L_0x0076:
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r13 = r13.next()
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r13 = r13.fillPlaceholder(r10, r14)
            if (r13 == 0) goto L_0x0081
            goto L_0x006c
        L_0x0081:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.LockFreeTaskQueueCore.addLast(java.lang.Object):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0070, code lost:
        r10 = r10.removeSlowPath(r7, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0074, code lost:
        if (r10 == null) goto L_0x0077;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object removeFirstOrNull() {
        /*
            r10 = this;
        L_0x0000:
            long r2 = r10._state$internal
            r0 = 1152921504606846976(0x1000000000000000, double:1.2882297539194267E-231)
            long r0 = r0 & r2
            r4 = 0
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            r6 = 0
            if (r0 == 0) goto L_0x0010
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.internal.LockFreeTaskQueueCore.REMOVE_FROZEN
            goto L_0x0078
        L_0x0010:
            kotlinx.coroutines.internal.LockFreeTaskQueueCore$Companion r0 = kotlinx.coroutines.internal.LockFreeTaskQueueCore.Companion
            r4 = 1073741823(0x3fffffff, double:5.304989472E-315)
            long r4 = r4 & r2
            r1 = 0
            long r4 = r4 >> r1
            int r7 = (int) r4
            r4 = 1152921503533105152(0xfffffffc0000000, double:1.2882296003504729E-231)
            long r4 = r4 & r2
            r1 = 30
            long r4 = r4 >> r1
            int r1 = (int) r4
            int r4 = access$getMask$p(r10)
            r1 = r1 & r4
            int r4 = access$getMask$p(r10)
            r4 = r4 & r7
            if (r1 != r4) goto L_0x0030
            goto L_0x0078
        L_0x0030:
            java.util.concurrent.atomic.AtomicReferenceArray r1 = r10.array$internal
            int r4 = access$getMask$p(r10)
            r4 = r4 & r7
            java.lang.Object r8 = r1.get(r4)
            if (r8 != 0) goto L_0x0044
            boolean r0 = access$getSingleConsumer$p(r10)
            if (r0 == 0) goto L_0x0000
            goto L_0x0078
        L_0x0044:
            boolean r1 = r8 instanceof kotlinx.coroutines.internal.LockFreeTaskQueueCore.Placeholder
            if (r1 == 0) goto L_0x0049
            goto L_0x0078
        L_0x0049:
            int r1 = r7 + 1
            r4 = 1073741823(0x3fffffff, float:1.9999999)
            r9 = r1 & r4
            java.util.concurrent.atomic.AtomicLongFieldUpdater r1 = kotlinx.coroutines.internal.LockFreeTaskQueueCore._state$FU$internal
            long r4 = r0.updateHead(r2, r9)
            r0 = r1
            r1 = r10
            boolean r0 = r0.compareAndSet(r1, r2, r4)
            if (r0 == 0) goto L_0x0069
            java.util.concurrent.atomic.AtomicReferenceArray r0 = r10.array$internal
            int r10 = access$getMask$p(r10)
            r10 = r10 & r7
            r0.set(r10, r6)
            goto L_0x0077
        L_0x0069:
            boolean r0 = access$getSingleConsumer$p(r10)
            if (r0 != 0) goto L_0x0070
            goto L_0x0000
        L_0x0070:
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r10 = access$removeSlowPath(r10, r7, r9)
            if (r10 == 0) goto L_0x0077
            goto L_0x0070
        L_0x0077:
            r6 = r8
        L_0x0078:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.LockFreeTaskQueueCore.removeFirstOrNull():java.lang.Object");
    }

    /* access modifiers changed from: private */
    public final LockFreeTaskQueueCore<E> removeSlowPath(int i, int i2) {
        long j;
        Companion companion;
        int i3;
        do {
            j = this._state$internal;
            companion = Companion;
            boolean z = false;
            i3 = (int) ((1073741823 & j) >> 0);
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (i3 == i) {
                    z = true;
                }
                if (!z) {
                    throw new AssertionError();
                }
            }
            if ((1152921504606846976L & j) != 0) {
                return next();
            }
        } while (!_state$FU$internal.compareAndSet(this, j, companion.updateHead(j, i2)));
        this.array$internal.set(this.mask & i3, null);
        return null;
    }

    private final LockFreeTaskQueueCore<E> allocateOrGetNextCopy(long j) {
        while (true) {
            LockFreeTaskQueueCore<E> lockFreeTaskQueueCore = (LockFreeTaskQueueCore) this._next;
            if (lockFreeTaskQueueCore != null) {
                return lockFreeTaskQueueCore;
            }
            _next$FU.compareAndSet(this, null, allocateNextCopy(j));
        }
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

    private final long markFrozen() {
        long j;
        long j2;
        do {
            j = this._state$internal;
            if ((j & 1152921504606846976L) != 0) {
                return j;
            }
            j2 = j | 1152921504606846976L;
        } while (!_state$FU$internal.compareAndSet(this, j, j2));
        return j2;
    }
}
