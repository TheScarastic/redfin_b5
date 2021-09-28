package androidx.arch.core.executor;
/* loaded from: classes.dex */
public abstract class TaskExecutor {
    public abstract boolean isMainThread();

    public abstract void postToMainThread(Runnable runnable);
}
