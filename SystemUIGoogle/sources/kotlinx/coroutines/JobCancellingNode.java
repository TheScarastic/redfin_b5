package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.Job;
/* compiled from: JobSupport.kt */
/* loaded from: classes2.dex */
public abstract class JobCancellingNode<J extends Job> extends JobNode<J> {
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public JobCancellingNode(J j) {
        super(j);
        Intrinsics.checkParameterIsNotNull(j, "job");
    }
}
