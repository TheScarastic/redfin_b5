package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.intrinsics.CancellableKt;
/* compiled from: Builders.common.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class LazyStandaloneCoroutine extends StandaloneCoroutine {
    private Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> block;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public LazyStandaloneCoroutine(CoroutineContext coroutineContext, Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2) {
        super(coroutineContext, false);
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        this.block = function2;
    }

    @Override // kotlinx.coroutines.AbstractCoroutine
    protected void onStart() {
        Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2 = this.block;
        if (function2 != null) {
            this.block = null;
            CancellableKt.startCoroutineCancellable(function2, this, this);
            return;
        }
        throw new IllegalStateException("Already started".toString());
    }
}
