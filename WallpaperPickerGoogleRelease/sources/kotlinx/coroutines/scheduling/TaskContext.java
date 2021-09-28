package kotlinx.coroutines.scheduling;

import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public interface TaskContext {
    void afterTask();

    @NotNull
    TaskMode getTaskMode();
}
