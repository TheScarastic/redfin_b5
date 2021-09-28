package kotlinx.coroutines.internal;

import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public interface ThreadSafeHeapNode {
    void setHeap(@Nullable ThreadSafeHeap<?> threadSafeHeap);

    void setIndex(int i);
}
