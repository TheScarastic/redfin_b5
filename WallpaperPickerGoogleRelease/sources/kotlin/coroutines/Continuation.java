package kotlin.coroutines;

import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public interface Continuation<T> {
    @NotNull
    CoroutineContext getContext();

    void resumeWith(@NotNull Object obj);
}
