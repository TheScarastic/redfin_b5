package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class CompletionHandlerException extends RuntimeException {
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public CompletionHandlerException(@NotNull String str, @NotNull Throwable th) {
        super(str, th);
        Intrinsics.checkParameterIsNotNull(str, "message");
    }
}
