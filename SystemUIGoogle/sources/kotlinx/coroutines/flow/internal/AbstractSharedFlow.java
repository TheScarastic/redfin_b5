package kotlinx.coroutines.flow.internal;

import java.util.Arrays;
import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;
import kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot;
/* compiled from: AbstractSharedFlow.kt */
/* loaded from: classes2.dex */
public abstract class AbstractSharedFlow<S extends AbstractSharedFlowSlot<?>> {
    private MutableStateFlow<Integer> _subscriptionCount;
    private int nCollectors;
    private int nextIndex;
    private S[] slots;

    protected abstract S createSlot();

    protected abstract S[] createSlotArray(int i);

    /* access modifiers changed from: protected */
    public final S allocateSlot() {
        S s;
        MutableStateFlow<Integer> mutableStateFlow;
        synchronized (this) {
            S[] slots = getSlots();
            if (slots == null) {
                slots = createSlotArray(2);
                this.slots = slots;
            } else if (getNCollectors() >= slots.length) {
                Object[] copyOf = Arrays.copyOf(slots, slots.length * 2);
                Intrinsics.checkNotNullExpressionValue(copyOf, "java.util.Arrays.copyOf(this, newSize)");
                this.slots = (S[]) ((AbstractSharedFlowSlot[]) copyOf);
                slots = (S[]) ((AbstractSharedFlowSlot[]) copyOf);
            }
            int i = this.nextIndex;
            do {
                s = slots[i];
                if (s == null) {
                    s = createSlot();
                    slots[i] = s;
                }
                i++;
                if (i >= slots.length) {
                    i = 0;
                }
            } while (!s.allocateLocked(this));
            this.nextIndex = i;
            this.nCollectors = getNCollectors() + 1;
            mutableStateFlow = this._subscriptionCount;
        }
        if (mutableStateFlow != null) {
            StateFlowKt.increment(mutableStateFlow, 1);
        }
        return s;
    }

    /* access modifiers changed from: protected */
    public final void freeSlot(S s) {
        MutableStateFlow<Integer> mutableStateFlow;
        int i;
        Continuation<Unit>[] freeLocked;
        Intrinsics.checkNotNullParameter(s, "slot");
        synchronized (this) {
            this.nCollectors = getNCollectors() - 1;
            mutableStateFlow = this._subscriptionCount;
            i = 0;
            if (getNCollectors() == 0) {
                this.nextIndex = 0;
            }
            freeLocked = s.freeLocked(this);
        }
        int length = freeLocked.length;
        while (i < length) {
            Continuation<Unit> continuation = freeLocked[i];
            i++;
            if (continuation != null) {
                Unit unit = Unit.INSTANCE;
                Result.Companion companion = Result.Companion;
                continuation.resumeWith(Result.m670constructorimpl(unit));
            }
        }
        if (mutableStateFlow != null) {
            StateFlowKt.increment(mutableStateFlow, -1);
        }
    }

    /* access modifiers changed from: protected */
    public final S[] getSlots() {
        return this.slots;
    }

    /* access modifiers changed from: protected */
    public final int getNCollectors() {
        return this.nCollectors;
    }
}
