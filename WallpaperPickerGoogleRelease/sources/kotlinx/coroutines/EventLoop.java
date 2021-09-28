package kotlinx.coroutines;

import kotlin.collections.ArraysKt___ArraysKt;
import kotlinx.coroutines.internal.ArrayQueue;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public abstract class EventLoop extends CoroutineDispatcher {
    public boolean shared;
    public ArrayQueue<DispatchedTask<?>> unconfinedQueue;
    public long useCount;

    public final void decrementUseCount(boolean z) {
        long delta = this.useCount - delta(z);
        this.useCount = delta;
        if (delta <= 0) {
            boolean z2 = DebugKt.DEBUG;
            if (this.shared) {
                shutdown();
            }
        }
    }

    public final long delta(boolean z) {
        return z ? 4294967296L : 1;
    }

    public final void dispatchUnconfined(@NotNull DispatchedTask<?> dispatchedTask) {
        ArrayQueue<DispatchedTask<?>> arrayQueue = this.unconfinedQueue;
        if (arrayQueue == null) {
            arrayQueue = new ArrayQueue<>();
            this.unconfinedQueue = arrayQueue;
        }
        Object[] objArr = arrayQueue.elements;
        int i = arrayQueue.tail;
        objArr[i] = dispatchedTask;
        int length = (i + 1) & (objArr.length - 1);
        arrayQueue.tail = length;
        int i2 = arrayQueue.head;
        if (length == i2) {
            int length2 = objArr.length;
            Object[] objArr2 = new Object[length2 << 1];
            ArraysKt___ArraysKt.copyInto$default(objArr, objArr2, 0, i2, 0, 10);
            Object[] objArr3 = arrayQueue.elements;
            int length3 = objArr3.length;
            int i3 = arrayQueue.head;
            ArraysKt___ArraysKt.copyInto$default(objArr3, objArr2, length3 - i3, 0, i3, 4);
            arrayQueue.elements = objArr2;
            arrayQueue.head = 0;
            arrayQueue.tail = length2;
        }
    }

    public final void incrementUseCount(boolean z) {
        this.useCount = delta(z) + this.useCount;
        if (!z) {
            this.shared = true;
        }
    }

    public final boolean isUnconfinedLoopActive() {
        return this.useCount >= delta(true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r5v0 */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean processUnconfinedEvent() {
        /*
            r6 = this;
            kotlinx.coroutines.internal.ArrayQueue<kotlinx.coroutines.DispatchedTask<?>> r6 = r6.unconfinedQueue
            r0 = 0
            if (r6 == 0) goto L_0x002f
            int r1 = r6.head
            int r2 = r6.tail
            r3 = 0
            r4 = 1
            if (r1 != r2) goto L_0x000e
            goto L_0x001e
        L_0x000e:
            java.lang.Object[] r2 = r6.elements
            r5 = r2[r1]
            r2[r1] = r3
            int r1 = r1 + r4
            int r2 = r2.length
            int r2 = r2 + -1
            r1 = r1 & r2
            r6.head = r1
            if (r5 == 0) goto L_0x0027
            r3 = r5
        L_0x001e:
            kotlinx.coroutines.DispatchedTask r3 = (kotlinx.coroutines.DispatchedTask) r3
            if (r3 == 0) goto L_0x0026
            r3.run()
            return r4
        L_0x0026:
            return r0
        L_0x0027:
            kotlin.TypeCastException r6 = new kotlin.TypeCastException
            java.lang.String r0 = "null cannot be cast to non-null type T"
            r6.<init>(r0)
            throw r6
        L_0x002f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.EventLoop.processUnconfinedEvent():boolean");
    }

    public void shutdown() {
    }
}
