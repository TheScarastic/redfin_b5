package kotlinx.coroutines.scheduling;

import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public abstract class Task implements Runnable {
    public long submissionTime;
    @NotNull
    public TaskContext taskContext;

    public Task(long j, @NotNull TaskContext taskContext) {
        this.submissionTime = j;
        this.taskContext = taskContext;
    }

    @NotNull
    public final TaskMode getMode() {
        return this.taskContext.getTaskMode();
    }

    public Task() {
        NonBlockingContext nonBlockingContext = NonBlockingContext.INSTANCE;
        this.submissionTime = 0;
        this.taskContext = nonBlockingContext;
    }
}
