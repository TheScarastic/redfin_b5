package kotlinx.coroutines.flow;

import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.flow.internal.NullSurrogateKt;
import kotlinx.coroutines.internal.Symbol;
/* compiled from: StateFlow.kt */
/* loaded from: classes2.dex */
public final class StateFlowKt {
    private static final Symbol NONE = new Symbol("NONE");
    private static final Symbol PENDING = new Symbol("PENDING");

    public static final <T> MutableStateFlow<T> MutableStateFlow(T t) {
        if (t == null) {
            t = (T) NullSurrogateKt.NULL;
        }
        return new StateFlowImpl(t);
    }

    public static final void increment(MutableStateFlow<Integer> mutableStateFlow, int i) {
        int intValue;
        Intrinsics.checkNotNullParameter(mutableStateFlow, "<this>");
        do {
            intValue = mutableStateFlow.getValue().intValue();
        } while (!mutableStateFlow.compareAndSet(Integer.valueOf(intValue), Integer.valueOf(intValue + i)));
    }
}
