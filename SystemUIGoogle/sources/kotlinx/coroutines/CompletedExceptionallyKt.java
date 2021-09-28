package kotlinx.coroutines;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CompletedExceptionally.kt */
/* loaded from: classes2.dex */
public final class CompletedExceptionallyKt {
    public static final <T> Object toState(Object obj) {
        if (Result.m675isSuccessimpl(obj)) {
            ResultKt.throwOnFailure(obj);
            return obj;
        }
        Throwable r4 = Result.m672exceptionOrNullimpl(obj);
        if (r4 == null) {
            Intrinsics.throwNpe();
        }
        return new CompletedExceptionally(r4, false, 2, null);
    }
}
