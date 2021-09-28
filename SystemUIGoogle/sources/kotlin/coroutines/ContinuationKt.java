package kotlin.coroutines;

import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Continuation.kt */
/* loaded from: classes2.dex */
public final class ContinuationKt {
    public static final <R, T> void startCoroutine(Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2, R r, Continuation<? super T> continuation) {
        Intrinsics.checkNotNullParameter(function2, "$this$startCoroutine");
        Intrinsics.checkNotNullParameter(continuation, "completion");
        Continuation continuation2 = IntrinsicsKt__IntrinsicsJvmKt.intercepted(IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted(function2, r, continuation));
        Unit unit = Unit.INSTANCE;
        Result.Companion companion = Result.Companion;
        continuation2.resumeWith(Result.m670constructorimpl(unit));
    }
}
