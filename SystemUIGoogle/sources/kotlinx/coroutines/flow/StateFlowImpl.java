package kotlinx.coroutines.flow;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.flow.internal.AbstractSharedFlow;
import kotlinx.coroutines.flow.internal.NullSurrogateKt;
import kotlinx.coroutines.internal.Symbol;
/* compiled from: StateFlow.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class StateFlowImpl<T> extends AbstractSharedFlow<StateFlowSlot> implements MutableStateFlow<T>, Flow {
    private final AtomicRef<Object> _state;
    private int sequence;

    public StateFlowImpl(Object obj) {
        Intrinsics.checkNotNullParameter(obj, "initialState");
        this._state = AtomicFU.atomic(obj);
    }

    @Override // kotlinx.coroutines.flow.MutableStateFlow, kotlinx.coroutines.flow.StateFlow
    public T getValue() {
        Symbol symbol = NullSurrogateKt.NULL;
        T t = (T) this._state.getValue();
        if (t == symbol) {
            return null;
        }
        return t;
    }

    @Override // kotlinx.coroutines.flow.MutableStateFlow
    public void setValue(T t) {
        if (t == null) {
            t = (T) NullSurrogateKt.NULL;
        }
        updateState(null, t);
    }

    @Override // kotlinx.coroutines.flow.MutableStateFlow
    public boolean compareAndSet(T t, T t2) {
        if (t == null) {
            t = (T) NullSurrogateKt.NULL;
        }
        if (t2 == null) {
            t2 = (T) NullSurrogateKt.NULL;
        }
        return updateState(t, t2);
    }

    private final boolean updateState(Object obj, Object obj2) {
        int i;
        StateFlowSlot[] slots;
        getSlots();
        synchronized (this) {
            Object value = this._state.getValue();
            if (!(obj == null || Intrinsics.areEqual(value, obj))) {
                return false;
            }
            if (Intrinsics.areEqual(value, obj2)) {
                return true;
            }
            this._state.setValue(obj2);
            int i2 = this.sequence;
            if ((i2 & 1) == 0) {
                int i3 = i2 + 1;
                this.sequence = i3;
                StateFlowSlot[] slots2 = getSlots();
                Unit unit = Unit.INSTANCE;
                while (true) {
                    StateFlowSlot[] stateFlowSlotArr = slots2;
                    if (stateFlowSlotArr != null) {
                        for (StateFlowSlot stateFlowSlot : stateFlowSlotArr) {
                            if (stateFlowSlot != null) {
                                stateFlowSlot.makePending();
                            }
                        }
                    }
                    synchronized (this) {
                        i = this.sequence;
                        if (i == i3) {
                            this.sequence = i3 + 1;
                            return true;
                        }
                        slots = getSlots();
                        Unit unit2 = Unit.INSTANCE;
                    }
                    slots2 = slots;
                    i3 = i;
                }
            } else {
                this.sequence = i2 + 2;
                return true;
            }
        }
    }

    @Override // kotlinx.coroutines.flow.MutableSharedFlow
    public boolean tryEmit(T t) {
        setValue(t);
        return true;
    }

    @Override // kotlinx.coroutines.flow.FlowCollector
    public Object emit(T t, Continuation<? super Unit> continuation) {
        setValue(t);
        return Unit.INSTANCE;
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:55:0x0024 */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:20:0x0063 */
    /* JADX WARN: Type inference failed for: r2v0, types: [int] */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v5, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r2v19 */
    /* JADX WARN: Type inference failed for: r2v20 */
    /* JADX WARN: Type inference failed for: r2v21 */
    /* JADX WARN: Type inference failed for: r2v22 */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00b9, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r12, r7) == false) goto L_0x00bb;
     */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0026  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00af  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00b0 A[Catch: all -> 0x00f3, TryCatch #0 {all -> 0x00f3, blocks: (B:22:0x0074, B:24:0x0082, B:26:0x0086, B:29:0x0098, B:30:0x00a7, B:33:0x00b0, B:35:0x00b5, B:37:0x00bb, B:41:0x00c2, B:46:0x00da, B:48:0x00e0), top: B:55:0x0024 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00b5 A[Catch: all -> 0x00f3, TryCatch #0 {all -> 0x00f3, blocks: (B:22:0x0074, B:24:0x0082, B:26:0x0086, B:29:0x0098, B:30:0x00a7, B:33:0x00b0, B:35:0x00b5, B:37:0x00bb, B:41:0x00c2, B:46:0x00da, B:48:0x00e0), top: B:55:0x0024 }] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00bf  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00c1  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00d4 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00d5  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00e0 A[Catch: all -> 0x00f3, TRY_LEAVE, TryCatch #0 {all -> 0x00f3, blocks: (B:22:0x0074, B:24:0x0082, B:26:0x0086, B:29:0x0098, B:30:0x00a7, B:33:0x00b0, B:35:0x00b5, B:37:0x00bb, B:41:0x00c2, B:46:0x00da, B:48:0x00e0), top: B:55:0x0024 }] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @Override // kotlinx.coroutines.flow.Flow
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super T> r11, kotlin.coroutines.Continuation<? super kotlin.Unit> r12) {
        /*
        // Method dump skipped, instructions count: 250
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.StateFlowImpl.collect(kotlinx.coroutines.flow.FlowCollector, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* access modifiers changed from: protected */
    @Override // kotlinx.coroutines.flow.internal.AbstractSharedFlow
    public StateFlowSlot createSlot() {
        return new StateFlowSlot();
    }

    /* access modifiers changed from: protected */
    @Override // kotlinx.coroutines.flow.internal.AbstractSharedFlow
    public StateFlowSlot[] createSlotArray(int i) {
        return new StateFlowSlot[i];
    }
}
