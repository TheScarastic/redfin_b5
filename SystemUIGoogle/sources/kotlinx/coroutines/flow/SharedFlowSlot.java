package kotlinx.coroutines.flow;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot;
/* compiled from: SharedFlow.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class SharedFlowSlot extends AbstractSharedFlowSlot<SharedFlowImpl<?>> {
    public Continuation<? super Unit> cont;
    public long index = -1;

    public boolean allocateLocked(SharedFlowImpl<?> sharedFlowImpl) {
        Intrinsics.checkNotNullParameter(sharedFlowImpl, "flow");
        if (this.index >= 0) {
            return false;
        }
        this.index = sharedFlowImpl.updateNewCollectorIndexLocked$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        return true;
    }

    public Continuation<Unit>[] freeLocked(SharedFlowImpl<?> sharedFlowImpl) {
        Intrinsics.checkNotNullParameter(sharedFlowImpl, "flow");
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(this.index >= 0)) {
                throw new AssertionError();
            }
        }
        long j = this.index;
        this.index = -1;
        this.cont = null;
        return sharedFlowImpl.updateCollectorIndexLocked$external__kotlinx_coroutines__android_common__kotlinx_coroutines(j);
    }
}
