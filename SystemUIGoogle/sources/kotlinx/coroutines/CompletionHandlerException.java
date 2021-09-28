package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: Exceptions.kt */
/* loaded from: classes2.dex */
public final class CompletionHandlerException extends RuntimeException {
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public CompletionHandlerException(String str, Throwable th) {
        super(str, th);
        Intrinsics.checkParameterIsNotNull(str, "message");
        Intrinsics.checkParameterIsNotNull(th, "cause");
    }
}
