package kotlinx.coroutines;

import java.util.concurrent.CancellationException;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public interface ParentJob extends Job {
    @NotNull
    CancellationException getChildJobCancellationCause();
}
