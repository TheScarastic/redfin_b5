package kotlinx.coroutines.flow;

import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.flow.internal.AbstractSharedFlowKt;
import kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot;
/* compiled from: StateFlow.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class StateFlowSlot extends AbstractSharedFlowSlot<StateFlowImpl<?>> {
    private final AtomicRef<Object> _state = AtomicFU.atomic(null);

    public boolean allocateLocked(StateFlowImpl<?> stateFlowImpl) {
        Intrinsics.checkNotNullParameter(stateFlowImpl, "flow");
        if (this._state.getValue() != null) {
            return false;
        }
        this._state.setValue(StateFlowKt.NONE);
        return true;
    }

    public Continuation<Unit>[] freeLocked(StateFlowImpl<?> stateFlowImpl) {
        Intrinsics.checkNotNullParameter(stateFlowImpl, "flow");
        this._state.setValue(null);
        return AbstractSharedFlowKt.EMPTY_RESUMES;
    }

    public final void makePending() {
        AtomicRef<Object> atomicRef = this._state;
        while (true) {
            Object value = atomicRef.getValue();
            if (value != null && value != StateFlowKt.PENDING) {
                if (value == StateFlowKt.NONE) {
                    if (this._state.compareAndSet(value, StateFlowKt.PENDING)) {
                        return;
                    }
                } else if (this._state.compareAndSet(value, StateFlowKt.NONE)) {
                    Unit unit = Unit.INSTANCE;
                    Result.Companion companion = Result.Companion;
                    ((CancellableContinuationImpl) value).resumeWith(Result.m670constructorimpl(unit));
                    return;
                }
            } else {
                return;
            }
        }
    }

    public final boolean takePending() {
        Object andSet = this._state.getAndSet(StateFlowKt.NONE);
        Intrinsics.checkNotNull(andSet);
        if (DebugKt.getASSERTIONS_ENABLED() && !(!(andSet instanceof CancellableContinuationImpl))) {
            throw new AssertionError();
        } else if (andSet == StateFlowKt.PENDING) {
            return true;
        } else {
            return false;
        }
    }

    public final Object awaitPending(Continuation<? super Unit> continuation) {
        boolean z = true;
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation), 1);
        cancellableContinuationImpl.initCancellability();
        if (!DebugKt.getASSERTIONS_ENABLED() || Boxing.boxBoolean(!(this._state.getValue() instanceof CancellableContinuationImpl)).booleanValue()) {
            if (!this._state.compareAndSet(StateFlowKt.NONE, cancellableContinuationImpl)) {
                if (DebugKt.getASSERTIONS_ENABLED()) {
                    if (this._state.getValue() != StateFlowKt.PENDING) {
                        z = false;
                    }
                    if (!Boxing.boxBoolean(z).booleanValue()) {
                        throw new AssertionError();
                    }
                }
                Unit unit = Unit.INSTANCE;
                Result.Companion companion = Result.Companion;
                cancellableContinuationImpl.resumeWith(Result.m670constructorimpl(unit));
            }
            Object result = cancellableContinuationImpl.getResult();
            if (result == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            return result == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? result : Unit.INSTANCE;
        }
        throw new AssertionError();
    }
}
