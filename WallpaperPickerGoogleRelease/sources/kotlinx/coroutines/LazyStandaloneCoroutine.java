package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.intrinsics.CancellableKt;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class LazyStandaloneCoroutine extends StandaloneCoroutine {
    public Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> block;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public LazyStandaloneCoroutine(@NotNull CoroutineContext coroutineContext, @NotNull Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2) {
        super(coroutineContext, false);
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        this.block = function2;
    }

    @Override // kotlinx.coroutines.AbstractCoroutine
    public void onStart() {
        Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2 = this.block;
        if (function2 != null) {
            this.block = null;
            CancellableKt.startCoroutineCancellable(function2, this, this);
            return;
        }
        throw new IllegalStateException("Already started".toString());
    }
}
