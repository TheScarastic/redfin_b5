package kotlinx.coroutines;

import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public interface ChildHandle extends DisposableHandle {
    boolean childCancelled(@NotNull Throwable th);
}
