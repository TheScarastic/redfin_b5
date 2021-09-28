package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: Exceptions.common.kt */
/* loaded from: classes2.dex */
public final class CoroutinesInternalError extends Error {
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public CoroutinesInternalError(String str, Throwable th) {
        super(str, th);
        Intrinsics.checkParameterIsNotNull(str, "message");
        Intrinsics.checkParameterIsNotNull(th, "cause");
    }
}
