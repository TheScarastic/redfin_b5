package kotlinx.coroutines;

import kotlinx.coroutines.Job;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public abstract class JobCancellingNode<J extends Job> extends JobNode<J> {
    public JobCancellingNode(@NotNull J j) {
        super(j);
    }
}
