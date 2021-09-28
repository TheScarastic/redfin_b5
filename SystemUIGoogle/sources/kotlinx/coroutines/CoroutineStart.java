package kotlinx.coroutines;

import kotlin.NoWhenBranchMatchedException;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.ContinuationKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.intrinsics.CancellableKt;
import kotlinx.coroutines.intrinsics.UndispatchedKt;
/* compiled from: CoroutineStart.kt */
/* loaded from: classes2.dex */
public enum CoroutineStart {
    DEFAULT,
    LAZY,
    ATOMIC,
    UNDISPATCHED;

    /* loaded from: classes2.dex */
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1;

        static {
            int[] iArr = new int[CoroutineStart.values().length];
            $EnumSwitchMapping$0 = iArr;
            CoroutineStart coroutineStart = CoroutineStart.DEFAULT;
            iArr[coroutineStart.ordinal()] = 1;
            CoroutineStart coroutineStart2 = CoroutineStart.ATOMIC;
            iArr[coroutineStart2.ordinal()] = 2;
            CoroutineStart coroutineStart3 = CoroutineStart.UNDISPATCHED;
            iArr[coroutineStart3.ordinal()] = 3;
            CoroutineStart coroutineStart4 = CoroutineStart.LAZY;
            iArr[coroutineStart4.ordinal()] = 4;
            int[] iArr2 = new int[CoroutineStart.values().length];
            $EnumSwitchMapping$1 = iArr2;
            iArr2[coroutineStart.ordinal()] = 1;
            iArr2[coroutineStart2.ordinal()] = 2;
            iArr2[coroutineStart3.ordinal()] = 3;
            iArr2[coroutineStart4.ordinal()] = 4;
        }
    }

    public final <R, T> void invoke(Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2, R r, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "block");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        int i = WhenMappings.$EnumSwitchMapping$1[ordinal()];
        if (i == 1) {
            CancellableKt.startCoroutineCancellable(function2, r, continuation);
        } else if (i == 2) {
            ContinuationKt.startCoroutine(function2, r, continuation);
        } else if (i == 3) {
            UndispatchedKt.startCoroutineUndispatched(function2, r, continuation);
        } else if (i != 4) {
            throw new NoWhenBranchMatchedException();
        }
    }

    public final boolean isLazy() {
        return this == LAZY;
    }
}
