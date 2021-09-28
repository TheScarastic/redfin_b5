package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: EventLoop.kt */
/* loaded from: classes2.dex */
public final class EventLoopKt {
    public static final EventLoop createEventLoop() {
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkExpressionValueIsNotNull(currentThread, "Thread.currentThread()");
        return new BlockingEventLoop(currentThread);
    }
}
