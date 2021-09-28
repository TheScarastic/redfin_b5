package kotlinx.coroutines;

import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class BlockingEventLoop extends EventLoopImplBase {
    @NotNull
    public final Thread thread;

    public BlockingEventLoop(@NotNull Thread thread) {
        this.thread = thread;
    }

    @Override // kotlinx.coroutines.EventLoopImplPlatform
    @NotNull
    public Thread getThread() {
        return this.thread;
    }
}
