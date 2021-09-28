package kotlinx.coroutines.scheduling;
/* compiled from: Tasks.kt */
/* loaded from: classes2.dex */
public final class NonBlockingContext implements TaskContext {
    public static final NonBlockingContext INSTANCE = new NonBlockingContext();
    private static final TaskMode taskMode = TaskMode.NON_BLOCKING;

    @Override // kotlinx.coroutines.scheduling.TaskContext
    public void afterTask() {
    }

    private NonBlockingContext() {
    }

    @Override // kotlinx.coroutines.scheduling.TaskContext
    public TaskMode getTaskMode() {
        return taskMode;
    }
}
