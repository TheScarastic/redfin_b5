package kotlinx.coroutines;
/* compiled from: Job.kt */
/* loaded from: classes2.dex */
public interface ChildHandle extends DisposableHandle {
    boolean childCancelled(Throwable th);
}
