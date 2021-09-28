package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public interface CoroutineScope {
    @NotNull
    CoroutineContext getCoroutineContext();
}
