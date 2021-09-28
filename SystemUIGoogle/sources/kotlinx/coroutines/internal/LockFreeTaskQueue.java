package kotlinx.coroutines.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
/* compiled from: LockFreeTaskQueue.kt */
/* loaded from: classes2.dex */
public class LockFreeTaskQueue<E> {
    public static final /* synthetic */ AtomicReferenceFieldUpdater _cur$FU$internal = AtomicReferenceFieldUpdater.newUpdater(LockFreeTaskQueue.class, Object.class, "_cur$internal");
    public volatile /* synthetic */ Object _cur$internal;

    public LockFreeTaskQueue(boolean z) {
        this._cur$internal = new LockFreeTaskQueueCore(8, z);
    }

    public final int getSize() {
        return ((LockFreeTaskQueueCore) this._cur$internal).getSize();
    }

    public final void close() {
        while (true) {
            LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) this._cur$internal;
            if (!lockFreeTaskQueueCore.close()) {
                _cur$FU$internal.compareAndSet(this, lockFreeTaskQueueCore, lockFreeTaskQueueCore.next());
            } else {
                return;
            }
        }
    }

    public final boolean addLast(E e) {
        Intrinsics.checkParameterIsNotNull(e, "element");
        while (true) {
            LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) this._cur$internal;
            int addLast = lockFreeTaskQueueCore.addLast(e);
            if (addLast == 0) {
                return true;
            }
            if (addLast == 1) {
                _cur$FU$internal.compareAndSet(this, lockFreeTaskQueueCore, lockFreeTaskQueueCore.next());
            } else if (addLast == 2) {
                return false;
            }
        }
    }

    public final E removeFirstOrNull() {
        E e;
        Object obj;
        while (true) {
            LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) this._cur$internal;
            while (true) {
                long j = lockFreeTaskQueueCore._state$internal;
                e = null;
                if ((1152921504606846976L & j) == 0) {
                    LockFreeTaskQueueCore.Companion companion = LockFreeTaskQueueCore.Companion;
                    int i = (int) ((1073741823 & j) >> 0);
                    if ((((int) ((1152921503533105152L & j) >> 30)) & lockFreeTaskQueueCore.mask) == (lockFreeTaskQueueCore.mask & i)) {
                        break;
                    }
                    obj = lockFreeTaskQueueCore.array$internal.get(lockFreeTaskQueueCore.mask & i);
                    if (obj != null) {
                        if (!(obj instanceof LockFreeTaskQueueCore.Placeholder)) {
                            int i2 = (i + 1) & 1073741823;
                            if (!LockFreeTaskQueueCore._state$FU$internal.compareAndSet(lockFreeTaskQueueCore, j, companion.updateHead(j, i2))) {
                                if (lockFreeTaskQueueCore.singleConsumer) {
                                    LockFreeTaskQueueCore lockFreeTaskQueueCore2 = lockFreeTaskQueueCore;
                                    do {
                                        lockFreeTaskQueueCore2 = lockFreeTaskQueueCore2.removeSlowPath(i, i2);
                                    } while (lockFreeTaskQueueCore2 != null);
                                    break;
                                }
                            } else {
                                lockFreeTaskQueueCore.array$internal.set(lockFreeTaskQueueCore.mask & i, null);
                                break;
                            }
                        } else {
                            break;
                        }
                    } else if (lockFreeTaskQueueCore.singleConsumer) {
                        break;
                    }
                } else {
                    e = (E) LockFreeTaskQueueCore.REMOVE_FROZEN;
                    break;
                }
            }
            e = (E) obj;
            if (e != LockFreeTaskQueueCore.REMOVE_FROZEN) {
                return e;
            }
            _cur$FU$internal.compareAndSet(this, lockFreeTaskQueueCore, lockFreeTaskQueueCore.next());
        }
    }
}
