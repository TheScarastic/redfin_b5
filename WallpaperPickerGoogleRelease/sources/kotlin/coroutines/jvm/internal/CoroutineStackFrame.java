package kotlin.coroutines.jvm.internal;

import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public interface CoroutineStackFrame {
    @Nullable
    CoroutineStackFrame getCallerFrame();

    @Nullable
    StackTraceElement getStackTraceElement();
}
