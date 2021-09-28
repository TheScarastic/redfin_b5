package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class CoroutinesInternalError extends Error {
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public CoroutinesInternalError(@NotNull String str, @NotNull Throwable th) {
        super(str, th);
        Intrinsics.checkParameterIsNotNull(str, "message");
        Intrinsics.checkParameterIsNotNull(th, "cause");
    }
}
