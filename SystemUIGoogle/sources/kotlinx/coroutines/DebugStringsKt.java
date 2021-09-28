package kotlinx.coroutines;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DebugStrings.kt */
/* loaded from: classes2.dex */
public final class DebugStringsKt {
    public static final String getHexAddress(Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "$this$hexAddress");
        String hexString = Integer.toHexString(System.identityHashCode(obj));
        Intrinsics.checkExpressionValueIsNotNull(hexString, "Integer.toHexString(System.identityHashCode(this))");
        return hexString;
    }

    public static final String toDebugString(Continuation<?> continuation) {
        Object obj;
        Intrinsics.checkParameterIsNotNull(continuation, "$this$toDebugString");
        if (continuation instanceof DispatchedContinuation) {
            return continuation.toString();
        }
        try {
            Result.Companion companion = Result.Companion;
            obj = Result.m670constructorimpl(continuation + '@' + getHexAddress(continuation));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.m670constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m672exceptionOrNullimpl(obj) != null) {
            obj = continuation.getClass().getName() + '@' + getHexAddress(continuation);
        }
        return (String) obj;
    }

    public static final String getClassSimpleName(Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "$this$classSimpleName");
        String simpleName = obj.getClass().getSimpleName();
        Intrinsics.checkExpressionValueIsNotNull(simpleName, "this::class.java.simpleName");
        return simpleName;
    }
}
