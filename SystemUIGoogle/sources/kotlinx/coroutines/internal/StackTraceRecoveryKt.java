package kotlinx.coroutines.internal;

import java.util.ArrayDeque;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.TypeCastException;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsJVMKt;
import kotlinx.coroutines.DebugKt;
/* compiled from: StackTraceRecovery.kt */
/* loaded from: classes2.dex */
public final class StackTraceRecoveryKt {
    private static final String baseContinuationImplClassName;
    private static final String stackTraceRecoveryClassName;

    /* JADX DEBUG: Multi-variable search result rejected for r1v6, resolved type: java.lang.Object */
    /* JADX DEBUG: Multi-variable search result rejected for r1v10, resolved type: java.lang.Object */
    /* JADX WARN: Multi-variable type inference failed */
    static {
        String str;
        Object obj;
        String str2 = "kotlin.coroutines.jvm.internal.BaseContinuationImpl";
        try {
            Result.Companion companion = Result.Companion;
            Class<?> cls = Class.forName(str2);
            Intrinsics.checkExpressionValueIsNotNull(cls, "Class.forName(baseContinuationImplClass)");
            str = Result.m670constructorimpl(cls.getCanonicalName());
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            str = Result.m670constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m672exceptionOrNullimpl(str) == null) {
            str2 = str;
        }
        baseContinuationImplClassName = str2;
        try {
            Result.Companion companion3 = Result.Companion;
            Intrinsics.checkExpressionValueIsNotNull(StackTraceRecoveryKt.class, "Class.forName(stackTraceRecoveryClass)");
            obj = Result.m670constructorimpl(StackTraceRecoveryKt.class.getCanonicalName());
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.Companion;
            obj = Result.m670constructorimpl(ResultKt.createFailure(th2));
        }
        if (Result.m672exceptionOrNullimpl(obj) != null) {
            obj = "kotlinx.coroutines.internal.StackTraceRecoveryKt";
        }
        stackTraceRecoveryClassName = (String) obj;
    }

    public static final <E extends Throwable> E recoverStackTrace(E e) {
        Intrinsics.checkParameterIsNotNull(e, "exception");
        if (!DebugKt.getRECOVER_STACK_TRACES()) {
            return e;
        }
        Throwable tryCopyException = ExceptionsConstuctorKt.tryCopyException(e);
        return tryCopyException != null ? (E) sanitizeStackTrace(tryCopyException) : e;
    }

    private static final <E extends Throwable> E sanitizeStackTrace(E e) {
        StackTraceElement stackTraceElement;
        StackTraceElement[] stackTrace = e.getStackTrace();
        int length = stackTrace.length;
        Intrinsics.checkExpressionValueIsNotNull(stackTrace, "stackTrace");
        String str = stackTraceRecoveryClassName;
        Intrinsics.checkExpressionValueIsNotNull(str, "stackTraceRecoveryClassName");
        int frameIndex = frameIndex(stackTrace, str);
        int i = frameIndex + 1;
        String str2 = baseContinuationImplClassName;
        Intrinsics.checkExpressionValueIsNotNull(str2, "baseContinuationImplClassName");
        int frameIndex2 = frameIndex(stackTrace, str2);
        int i2 = (length - frameIndex) - (frameIndex2 == -1 ? 0 : length - frameIndex2);
        StackTraceElement[] stackTraceElementArr = new StackTraceElement[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            if (i3 == 0) {
                stackTraceElement = artificialFrame("Coroutine boundary");
            } else {
                stackTraceElement = stackTrace[(i + i3) - 1];
            }
            stackTraceElementArr[i3] = stackTraceElement;
        }
        e.setStackTrace(stackTraceElementArr);
        return e;
    }

    public static final <E extends Throwable> E recoverStackTrace(E e, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(e, "exception");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        return (!DebugKt.getRECOVER_STACK_TRACES() || !(continuation instanceof CoroutineStackFrame)) ? e : (E) recoverFromStackFrame(e, (CoroutineStackFrame) continuation);
    }

    /* access modifiers changed from: private */
    public static final <E extends Throwable> E recoverFromStackFrame(E e, CoroutineStackFrame coroutineStackFrame) {
        Pair causeAndStacktrace = causeAndStacktrace(e);
        Throwable th = (Throwable) causeAndStacktrace.component1();
        StackTraceElement[] stackTraceElementArr = (StackTraceElement[]) causeAndStacktrace.component2();
        Throwable tryCopyException = ExceptionsConstuctorKt.tryCopyException(th);
        if (tryCopyException == null) {
            return e;
        }
        ArrayDeque<StackTraceElement> createStackTrace = createStackTrace(coroutineStackFrame);
        if (createStackTrace.isEmpty()) {
            return e;
        }
        if (th != e) {
            mergeRecoveredTraces(stackTraceElementArr, createStackTrace);
        }
        return (E) createFinalException(th, tryCopyException, createStackTrace);
    }

    private static final <E extends Throwable> E createFinalException(E e, E e2, ArrayDeque<StackTraceElement> arrayDeque) {
        arrayDeque.addFirst(artificialFrame("Coroutine boundary"));
        StackTraceElement[] stackTrace = e.getStackTrace();
        Intrinsics.checkExpressionValueIsNotNull(stackTrace, "causeTrace");
        String str = baseContinuationImplClassName;
        Intrinsics.checkExpressionValueIsNotNull(str, "baseContinuationImplClassName");
        int frameIndex = frameIndex(stackTrace, str);
        int i = 0;
        if (frameIndex == -1) {
            Object[] array = arrayDeque.toArray(new StackTraceElement[0]);
            if (array != null) {
                e2.setStackTrace((StackTraceElement[]) array);
                return e2;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        StackTraceElement[] stackTraceElementArr = new StackTraceElement[arrayDeque.size() + frameIndex];
        for (int i2 = 0; i2 < frameIndex; i2++) {
            stackTraceElementArr[i2] = stackTrace[i2];
        }
        for (StackTraceElement stackTraceElement : arrayDeque) {
            stackTraceElementArr[frameIndex + i] = stackTraceElement;
            i++;
        }
        e2.setStackTrace(stackTraceElementArr);
        return e2;
    }

    private static final <E extends Throwable> Pair<E, StackTraceElement[]> causeAndStacktrace(E e) {
        boolean z;
        Throwable cause = e.getCause();
        if (cause == null || !Intrinsics.areEqual(cause.getClass(), e.getClass())) {
            return TuplesKt.to(e, new StackTraceElement[0]);
        }
        StackTraceElement[] stackTrace = e.getStackTrace();
        Intrinsics.checkExpressionValueIsNotNull(stackTrace, "currentTrace");
        int length = stackTrace.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = false;
                break;
            }
            StackTraceElement stackTraceElement = stackTrace[i];
            Intrinsics.checkExpressionValueIsNotNull(stackTraceElement, "it");
            if (isArtificial(stackTraceElement)) {
                z = true;
                break;
            }
            i++;
        }
        if (z) {
            return TuplesKt.to(cause, stackTrace);
        }
        return TuplesKt.to(e, new StackTraceElement[0]);
    }

    public static final <E extends Throwable> E unwrap(E e) {
        Intrinsics.checkParameterIsNotNull(e, "exception");
        if (!DebugKt.getRECOVER_STACK_TRACES()) {
            return e;
        }
        E e2 = (E) e.getCause();
        if (e2 != null) {
            boolean z = true;
            if (!(!Intrinsics.areEqual(e2.getClass(), e.getClass()))) {
                StackTraceElement[] stackTrace = e.getStackTrace();
                Intrinsics.checkExpressionValueIsNotNull(stackTrace, "exception.stackTrace");
                int length = stackTrace.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        z = false;
                        break;
                    }
                    StackTraceElement stackTraceElement = stackTrace[i];
                    Intrinsics.checkExpressionValueIsNotNull(stackTraceElement, "it");
                    if (isArtificial(stackTraceElement)) {
                        break;
                    }
                    i++;
                }
                if (z) {
                    return e2;
                }
            }
        }
        return e;
    }

    private static final ArrayDeque<StackTraceElement> createStackTrace(CoroutineStackFrame coroutineStackFrame) {
        ArrayDeque<StackTraceElement> arrayDeque = new ArrayDeque<>();
        StackTraceElement stackTraceElement = coroutineStackFrame.getStackTraceElement();
        if (stackTraceElement != null) {
            arrayDeque.add(stackTraceElement);
        }
        while (true) {
            coroutineStackFrame = coroutineStackFrame.getCallerFrame();
            if (coroutineStackFrame == null) {
                return arrayDeque;
            }
            StackTraceElement stackTraceElement2 = coroutineStackFrame.getStackTraceElement();
            if (stackTraceElement2 != null) {
                arrayDeque.add(stackTraceElement2);
            }
        }
    }

    public static final StackTraceElement artificialFrame(String str) {
        Intrinsics.checkParameterIsNotNull(str, "message");
        return new StackTraceElement("\b\b\b(" + str, "\b", "\b", -1);
    }

    public static final boolean isArtificial(StackTraceElement stackTraceElement) {
        Intrinsics.checkParameterIsNotNull(stackTraceElement, "$this$isArtificial");
        String className = stackTraceElement.getClassName();
        Intrinsics.checkExpressionValueIsNotNull(className, "className");
        return StringsKt__StringsJVMKt.startsWith$default(className, "\b\b\b", false, 2, null);
    }

    private static final boolean elementWiseEquals(StackTraceElement stackTraceElement, StackTraceElement stackTraceElement2) {
        return stackTraceElement.getLineNumber() == stackTraceElement2.getLineNumber() && Intrinsics.areEqual(stackTraceElement.getMethodName(), stackTraceElement2.getMethodName()) && Intrinsics.areEqual(stackTraceElement.getFileName(), stackTraceElement2.getFileName()) && Intrinsics.areEqual(stackTraceElement.getClassName(), stackTraceElement2.getClassName());
    }

    private static final int frameIndex(StackTraceElement[] stackTraceElementArr, String str) {
        int length = stackTraceElementArr.length;
        for (int i = 0; i < length; i++) {
            if (Intrinsics.areEqual(str, stackTraceElementArr[i].getClassName())) {
                return i;
            }
        }
        return -1;
    }

    private static final void mergeRecoveredTraces(StackTraceElement[] stackTraceElementArr, ArrayDeque<StackTraceElement> arrayDeque) {
        int length = stackTraceElementArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                i = -1;
                break;
            } else if (isArtificial(stackTraceElementArr[i])) {
                break;
            } else {
                i++;
            }
        }
        int i2 = i + 1;
        int length2 = stackTraceElementArr.length - 1;
        if (length2 >= i2) {
            while (true) {
                StackTraceElement stackTraceElement = stackTraceElementArr[length2];
                StackTraceElement last = arrayDeque.getLast();
                Intrinsics.checkExpressionValueIsNotNull(last, "result.last");
                if (elementWiseEquals(stackTraceElement, last)) {
                    arrayDeque.removeLast();
                }
                arrayDeque.addFirst(stackTraceElementArr[length2]);
                if (length2 != i2) {
                    length2--;
                } else {
                    return;
                }
            }
        }
    }
}
