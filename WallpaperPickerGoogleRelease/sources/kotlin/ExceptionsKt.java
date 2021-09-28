package kotlin;

import kotlin.internal.PlatformImplementationsKt;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class ExceptionsKt {
    public static final void addSuppressed(@NotNull Throwable th, @NotNull Throwable th2) {
        if (th != th2) {
            PlatformImplementationsKt.IMPLEMENTATIONS.addSuppressed(th, th2);
        }
    }
}
