package kotlinx.coroutines.internal;

import kotlin.coroutines.CoroutineContext;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class ThreadState {
    public Object[] a;
    @NotNull
    public final CoroutineContext context;
    public int i;

    public ThreadState(@NotNull CoroutineContext coroutineContext, int i) {
        this.context = coroutineContext;
        this.a = new Object[i];
    }
}
