package kotlin.internal.jdk7;

import kotlin.internal.PlatformImplementations;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public class JDK7PlatformImplementations extends PlatformImplementations {
    @Override // kotlin.internal.PlatformImplementations
    public void addSuppressed(@NotNull Throwable th, @NotNull Throwable th2) {
        th.addSuppressed(th2);
    }
}
