package kotlin.coroutines.intrinsics;

import java.util.Objects;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.jvm.internal.BaseContinuationImpl;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.coroutines.jvm.internal.RestrictedContinuationImpl;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
/* compiled from: IntrinsicsJvm.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class IntrinsicsKt__IntrinsicsJvmKt {
    public static <R, T> Continuation<Unit> createCoroutineUnintercepted(Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2, R r, Continuation<? super T> continuation) {
        Intrinsics.checkNotNullParameter(function2, "$this$createCoroutineUnintercepted");
        Intrinsics.checkNotNullParameter(continuation, "completion");
        Continuation<?> probeCoroutineCreated = DebugProbesKt.probeCoroutineCreated(continuation);
        if (function2 instanceof BaseContinuationImpl) {
            return ((BaseContinuationImpl) function2).create(r, probeCoroutineCreated);
        }
        CoroutineContext context = probeCoroutineCreated.getContext();
        return context == EmptyCoroutineContext.INSTANCE ? new RestrictedContinuationImpl(probeCoroutineCreated, probeCoroutineCreated, function2, r) { // from class: kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnintercepted$$inlined$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt$3
            final /* synthetic */ Continuation $completion;
            final /* synthetic */ Object $receiver$inlined;
            final /* synthetic */ Function2 $this_createCoroutineUnintercepted$inlined;
            private int label;

            {
                this.$completion = r1;
                this.$this_createCoroutineUnintercepted$inlined = r3;
                this.$receiver$inlined = r4;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            protected Object invokeSuspend(Object obj) {
                int i = this.label;
                if (i == 0) {
                    this.label = 1;
                    ResultKt.throwOnFailure(obj);
                    Function2 function22 = this.$this_createCoroutineUnintercepted$inlined;
                    Objects.requireNonNull(function22, "null cannot be cast to non-null type (R, kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                    return ((Function2) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function22, 2)).invoke(this.$receiver$inlined, this);
                } else if (i == 1) {
                    this.label = 2;
                    ResultKt.throwOnFailure(obj);
                    return obj;
                } else {
                    throw new IllegalStateException("This coroutine had already completed".toString());
                }
            }
        } : new ContinuationImpl(probeCoroutineCreated, context, probeCoroutineCreated, context, function2, r) { // from class: kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnintercepted$$inlined$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt$4
            final /* synthetic */ Continuation $completion;
            final /* synthetic */ CoroutineContext $context;
            final /* synthetic */ Object $receiver$inlined;
            final /* synthetic */ Function2 $this_createCoroutineUnintercepted$inlined;
            private int label;

            {
                this.$completion = r1;
                this.$context = r2;
                this.$this_createCoroutineUnintercepted$inlined = r5;
                this.$receiver$inlined = r6;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            protected Object invokeSuspend(Object obj) {
                int i = this.label;
                if (i == 0) {
                    this.label = 1;
                    ResultKt.throwOnFailure(obj);
                    Function2 function22 = this.$this_createCoroutineUnintercepted$inlined;
                    Objects.requireNonNull(function22, "null cannot be cast to non-null type (R, kotlin.coroutines.Continuation<T>) -> kotlin.Any?");
                    return ((Function2) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function22, 2)).invoke(this.$receiver$inlined, this);
                } else if (i == 1) {
                    this.label = 2;
                    ResultKt.throwOnFailure(obj);
                    return obj;
                } else {
                    throw new IllegalStateException("This coroutine had already completed".toString());
                }
            }
        };
    }

    /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: kotlin.coroutines.Continuation<? super T> */
    /* JADX WARN: Multi-variable type inference failed */
    public static <T> Continuation<T> intercepted(Continuation<? super T> continuation) {
        Continuation<T> continuation2;
        Intrinsics.checkNotNullParameter(continuation, "$this$intercepted");
        ContinuationImpl continuationImpl = !(continuation instanceof ContinuationImpl) ? null : continuation;
        return (continuationImpl == null || (continuation2 = (Continuation<T>) continuationImpl.intercepted()) == null) ? continuation : continuation2;
    }
}
