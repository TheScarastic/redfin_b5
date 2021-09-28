package kotlinx.coroutines.scheduling;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: Tasks.kt */
/* loaded from: classes2.dex */
public abstract class Task implements Runnable {
    public long submissionTime;
    public TaskContext taskContext;

    public Task(long j, TaskContext taskContext) {
        Intrinsics.checkParameterIsNotNull(taskContext, "taskContext");
        this.submissionTime = j;
        this.taskContext = taskContext;
    }

    public Task() {
        this(0, NonBlockingContext.INSTANCE);
    }

    public final TaskMode getMode() {
        return this.taskContext.getTaskMode();
    }
}
