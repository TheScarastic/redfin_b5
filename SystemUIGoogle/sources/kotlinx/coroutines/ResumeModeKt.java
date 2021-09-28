package kotlinx.coroutines;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.internal.ThreadContextKt;
/* compiled from: ResumeMode.kt */
/* loaded from: classes2.dex */
public final class ResumeModeKt {
    public static final boolean isCancellableMode(int i) {
        return i == 1;
    }

    public static final boolean isDispatchedMode(int i) {
        return i == 0 || i == 1;
    }

    public static final <T> void resumeMode(Continuation<? super T> continuation, T t, int i) {
        Intrinsics.checkParameterIsNotNull(continuation, "$this$resumeMode");
        if (i == 0) {
            Result.Companion companion = Result.Companion;
            continuation.resumeWith(Result.m670constructorimpl(t));
        } else if (i == 1) {
            DispatchedKt.resumeCancellable(continuation, t);
        } else if (i == 2) {
            DispatchedKt.resumeDirect(continuation, t);
        } else if (i == 3) {
            DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation;
            CoroutineContext context = dispatchedContinuation.getContext();
            Object updateThreadContext = ThreadContextKt.updateThreadContext(context, dispatchedContinuation.countOrElement);
            try {
                Continuation<T> continuation2 = dispatchedContinuation.continuation;
                Result.Companion companion2 = Result.Companion;
                continuation2.resumeWith(Result.m670constructorimpl(t));
                Unit unit = Unit.INSTANCE;
            } finally {
                ThreadContextKt.restoreThreadContext(context, updateThreadContext);
            }
        } else if (i != 4) {
            throw new IllegalStateException(("Invalid mode " + i).toString());
        }
    }

    public static final <T> void resumeWithExceptionMode(Continuation<? super T> continuation, Throwable th, int i) {
        Intrinsics.checkParameterIsNotNull(continuation, "$this$resumeWithExceptionMode");
        Intrinsics.checkParameterIsNotNull(th, "exception");
        if (i == 0) {
            Result.Companion companion = Result.Companion;
            continuation.resumeWith(Result.m670constructorimpl(ResultKt.createFailure(th)));
        } else if (i == 1) {
            DispatchedKt.resumeCancellableWithException(continuation, th);
        } else if (i == 2) {
            DispatchedKt.resumeDirectWithException(continuation, th);
        } else if (i == 3) {
            DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation;
            CoroutineContext context = dispatchedContinuation.getContext();
            Object updateThreadContext = ThreadContextKt.updateThreadContext(context, dispatchedContinuation.countOrElement);
            try {
                Continuation<T> continuation2 = dispatchedContinuation.continuation;
                Result.Companion companion2 = Result.Companion;
                continuation2.resumeWith(Result.m670constructorimpl(ResultKt.createFailure(StackTraceRecoveryKt.recoverStackTrace(th, continuation2))));
                Unit unit = Unit.INSTANCE;
            } finally {
                ThreadContextKt.restoreThreadContext(context, updateThreadContext);
            }
        } else if (i != 4) {
            throw new IllegalStateException(("Invalid mode " + i).toString());
        }
    }

    public static final <T> void resumeUninterceptedMode(Continuation<? super T> continuation, T t, int i) {
        Intrinsics.checkParameterIsNotNull(continuation, "$this$resumeUninterceptedMode");
        if (i == 0) {
            Continuation continuation2 = IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation);
            Result.Companion companion = Result.Companion;
            continuation2.resumeWith(Result.m670constructorimpl(t));
        } else if (i == 1) {
            DispatchedKt.resumeCancellable(IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation), t);
        } else if (i == 2) {
            Result.Companion companion2 = Result.Companion;
            continuation.resumeWith(Result.m670constructorimpl(t));
        } else if (i == 3) {
            CoroutineContext context = continuation.getContext();
            Object updateThreadContext = ThreadContextKt.updateThreadContext(context, null);
            try {
                Result.Companion companion3 = Result.Companion;
                continuation.resumeWith(Result.m670constructorimpl(t));
                Unit unit = Unit.INSTANCE;
            } finally {
                ThreadContextKt.restoreThreadContext(context, updateThreadContext);
            }
        } else if (i != 4) {
            throw new IllegalStateException(("Invalid mode " + i).toString());
        }
    }

    public static final <T> void resumeUninterceptedWithExceptionMode(Continuation<? super T> continuation, Throwable th, int i) {
        Intrinsics.checkParameterIsNotNull(continuation, "$this$resumeUninterceptedWithExceptionMode");
        Intrinsics.checkParameterIsNotNull(th, "exception");
        if (i == 0) {
            Continuation continuation2 = IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation);
            Result.Companion companion = Result.Companion;
            continuation2.resumeWith(Result.m670constructorimpl(ResultKt.createFailure(th)));
        } else if (i == 1) {
            DispatchedKt.resumeCancellableWithException(IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation), th);
        } else if (i == 2) {
            Result.Companion companion2 = Result.Companion;
            continuation.resumeWith(Result.m670constructorimpl(ResultKt.createFailure(th)));
        } else if (i == 3) {
            CoroutineContext context = continuation.getContext();
            Object updateThreadContext = ThreadContextKt.updateThreadContext(context, null);
            try {
                Result.Companion companion3 = Result.Companion;
                continuation.resumeWith(Result.m670constructorimpl(ResultKt.createFailure(th)));
                Unit unit = Unit.INSTANCE;
            } finally {
                ThreadContextKt.restoreThreadContext(context, updateThreadContext);
            }
        } else if (i != 4) {
            throw new IllegalStateException(("Invalid mode " + i).toString());
        }
    }
}
