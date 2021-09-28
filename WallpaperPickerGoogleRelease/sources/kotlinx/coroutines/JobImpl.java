package kotlinx.coroutines;

import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public class JobImpl extends JobSupport implements CompletableJob {
    public final boolean handlesException;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public JobImpl(@Nullable Job job) {
        super(true);
        JobSupport jobSupport;
        boolean z = true;
        initParentJobInternal$kotlinx_coroutines_core(job);
        ChildHandle childHandle = this.parentHandle;
        ChildHandleNode childHandleNode = (ChildHandleNode) (!(childHandle instanceof ChildHandleNode) ? null : childHandle);
        if (childHandleNode != null && (jobSupport = (JobSupport) childHandleNode.job) != null) {
            while (!jobSupport.getHandlesException$kotlinx_coroutines_core()) {
                ChildHandle childHandle2 = jobSupport.parentHandle;
                ChildHandleNode childHandleNode2 = (ChildHandleNode) (!(childHandle2 instanceof ChildHandleNode) ? null : childHandle2);
                if (childHandleNode2 != null) {
                    jobSupport = (JobSupport) childHandleNode2.job;
                    if (jobSupport == null) {
                    }
                }
            }
            this.handlesException = z;
        }
        z = false;
        this.handlesException = z;
    }

    @Override // kotlinx.coroutines.JobSupport
    public boolean getHandlesException$kotlinx_coroutines_core() {
        return this.handlesException;
    }

    @Override // kotlinx.coroutines.JobSupport
    public boolean getOnCancelComplete$kotlinx_coroutines_core() {
        return true;
    }
}
