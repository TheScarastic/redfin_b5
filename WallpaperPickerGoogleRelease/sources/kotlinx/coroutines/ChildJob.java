package kotlinx.coroutines;

import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public interface ChildJob extends Job {
    void parentCancelled(@NotNull ParentJob parentJob);
}
