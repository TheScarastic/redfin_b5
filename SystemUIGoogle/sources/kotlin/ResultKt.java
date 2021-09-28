package kotlin;

import kotlin.Result;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Result.kt */
/* loaded from: classes2.dex */
public final class ResultKt {
    public static final Object createFailure(Throwable th) {
        Intrinsics.checkNotNullParameter(th, "exception");
        return new Result.Failure(th);
    }

    public static final void throwOnFailure(Object obj) {
        if (obj instanceof Result.Failure) {
            throw ((Result.Failure) obj).exception;
        }
    }
}
