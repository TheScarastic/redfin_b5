package kotlinx.coroutines.internal;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;
import kotlinx.coroutines.DebugKt;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class StackTraceRecoveryKt {
    public static final String baseContinuationImplClassName;

    /* JADX DEBUG: Multi-variable search result rejected for r1v3, resolved type: java.lang.Object */
    /* JADX WARN: Multi-variable type inference failed */
    static {
        String str;
        Object obj;
        String str2 = "kotlin.coroutines.jvm.internal.BaseContinuationImpl";
        try {
            str = Class.forName(str2).getCanonicalName();
        } catch (Throwable th) {
            str = ResultKt.createFailure(th);
        }
        if (Result.m23exceptionOrNullimpl(str) == null) {
            str2 = str;
        }
        baseContinuationImplClassName = str2;
        try {
            obj = StackTraceRecoveryKt.class.getCanonicalName();
        } catch (Throwable th2) {
            obj = ResultKt.createFailure(th2);
        }
        if (Result.m23exceptionOrNullimpl(obj) != null) {
            obj = "kotlinx.coroutines.internal.StackTraceRecoveryKt";
        }
        String str3 = (String) obj;
    }

    public static final boolean isArtificial(@NotNull StackTraceElement stackTraceElement) {
        Intrinsics.checkParameterIsNotNull(stackTraceElement, "$this$isArtificial");
        String className = stackTraceElement.getClassName();
        Intrinsics.checkExpressionValueIsNotNull(className, "className");
        return StringsKt__StringsKt.startsWith$default(className, "\b\b\b", false, 2);
    }

    /* JADX INFO: finally extract failed */
    /* JADX DEBUG: Multi-variable search result rejected for r9v8, resolved type: java.util.WeakHashMap<java.lang.Class<? extends java.lang.Throwable>, kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable>> */
    /* JADX DEBUG: Multi-variable search result rejected for r8v13, resolved type: java.util.WeakHashMap<java.lang.Class<? extends java.lang.Throwable>, kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable>> */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x01e5  */
    /* JADX WARNING: Removed duplicated region for block: B:173:0x007a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:187:0x019d A[EDGE_INSN: B:187:0x019d->B:87:0x019d ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:206:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0091  */
    @org.jetbrains.annotations.NotNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final <E extends java.lang.Throwable> E recoverStackTrace(@org.jetbrains.annotations.NotNull E r12, @org.jetbrains.annotations.NotNull kotlin.coroutines.Continuation<?> r13) {
        /*
        // Method dump skipped, instructions count: 767
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverStackTrace(java.lang.Throwable, kotlin.coroutines.Continuation):java.lang.Throwable");
    }

    @NotNull
    public static final <E extends Throwable> E unwrap(@NotNull E e) {
        Intrinsics.checkParameterIsNotNull(e, "exception");
        if (!DebugKt.RECOVER_STACK_TRACES) {
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
}
