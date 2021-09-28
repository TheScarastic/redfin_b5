package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: Supervisor.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class SupervisorJobImpl extends JobImpl {
    @Override // kotlinx.coroutines.JobSupport
    public boolean childCancelled(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "cause");
        return false;
    }

    public SupervisorJobImpl(Job job) {
        super(job);
    }
}
