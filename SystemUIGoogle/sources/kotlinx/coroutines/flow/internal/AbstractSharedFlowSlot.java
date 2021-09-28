package kotlinx.coroutines.flow.internal;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
/* compiled from: AbstractSharedFlow.kt */
/* loaded from: classes2.dex */
public abstract class AbstractSharedFlowSlot<F> {
    public abstract boolean allocateLocked(F f);

    public abstract Continuation<Unit>[] freeLocked(F f);
}
