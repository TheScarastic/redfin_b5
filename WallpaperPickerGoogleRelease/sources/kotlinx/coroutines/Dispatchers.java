package kotlinx.coroutines;

import java.util.Objects;
import kotlinx.coroutines.scheduling.DefaultScheduler;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class Dispatchers {
    @NotNull
    public static final CoroutineDispatcher Default;
    public static final Dispatchers INSTANCE = null;
    @NotNull
    public static final CoroutineDispatcher IO;

    static {
        Default = CoroutineContextKt.useCoroutinesScheduler ? DefaultScheduler.INSTANCE : CommonPool.INSTANCE;
        Unconfined unconfined = Unconfined.INSTANCE;
        Objects.requireNonNull(DefaultScheduler.INSTANCE);
        IO = DefaultScheduler.IO;
    }
}
