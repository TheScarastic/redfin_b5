package kotlinx.coroutines.internal;

import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.AbstractCoroutine;
/* compiled from: Scopes.kt */
/* loaded from: classes2.dex */
public final class ScopesKt {
    public static final Throwable tryRecover(AbstractCoroutine<?> abstractCoroutine, Throwable th) {
        Continuation<T> continuation;
        Intrinsics.checkParameterIsNotNull(abstractCoroutine, "$this$tryRecover");
        Intrinsics.checkParameterIsNotNull(th, "exception");
        if (!(abstractCoroutine instanceof ScopeCoroutine)) {
            abstractCoroutine = null;
        }
        ScopeCoroutine scopeCoroutine = (ScopeCoroutine) abstractCoroutine;
        return (scopeCoroutine == null || (continuation = scopeCoroutine.uCont) == 0) ? th : StackTraceRecoveryKt.recoverStackTrace(th, continuation);
    }
}
