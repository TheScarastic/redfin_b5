package kotlinx.coroutines.intrinsics;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.DispatchedKt;
/* compiled from: Cancellable.kt */
/* loaded from: classes2.dex */
public final class CancellableKt {
    public static final <R, T> void startCoroutineCancellable(Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2, R r, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "$this$startCoroutineCancellable");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        try {
            DispatchedKt.resumeCancellable(IntrinsicsKt__IntrinsicsJvmKt.intercepted(IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted(function2, r, continuation)), Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion = Result.Companion;
            continuation.resumeWith(Result.m670constructorimpl(ResultKt.createFailure(th)));
        }
    }
}
