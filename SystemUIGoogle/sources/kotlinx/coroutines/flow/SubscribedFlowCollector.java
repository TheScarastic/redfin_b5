package kotlinx.coroutines.flow;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
/* compiled from: Share.kt */
/* loaded from: classes2.dex */
public final class SubscribedFlowCollector<T> implements FlowCollector<T> {
    private final Function2<FlowCollector<? super T>, Continuation<? super Unit>, Object> action;
    private final FlowCollector<T> collector;

    @Override // kotlinx.coroutines.flow.FlowCollector
    public Object emit(T t, Continuation<? super Unit> continuation) {
        return this.collector.emit(t, continuation);
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0024  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x006a  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x007d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object onSubscription(kotlin.coroutines.Continuation<? super kotlin.Unit> r7) {
        /*
            r6 = this;
            boolean r0 = r7 instanceof kotlinx.coroutines.flow.SubscribedFlowCollector$onSubscription$1
            if (r0 == 0) goto L_0x0013
            r0 = r7
            kotlinx.coroutines.flow.SubscribedFlowCollector$onSubscription$1 r0 = (kotlinx.coroutines.flow.SubscribedFlowCollector$onSubscription$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L_0x0013
            int r1 = r1 - r2
            r0.label = r1
            goto L_0x0018
        L_0x0013:
            kotlinx.coroutines.flow.SubscribedFlowCollector$onSubscription$1 r0 = new kotlinx.coroutines.flow.SubscribedFlowCollector$onSubscription$1
            r0.<init>(r6, r7)
        L_0x0018:
            java.lang.Object r7 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 2
            r4 = 1
            if (r2 == 0) goto L_0x0042
            if (r2 == r4) goto L_0x0034
            if (r2 != r3) goto L_0x002c
            kotlin.ResultKt.throwOnFailure(r7)
            goto L_0x007a
        L_0x002c:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
            r6.<init>(r7)
            throw r6
        L_0x0034:
            java.lang.Object r6 = r0.L$1
            kotlinx.coroutines.flow.internal.SafeCollector r6 = (kotlinx.coroutines.flow.internal.SafeCollector) r6
            java.lang.Object r2 = r0.L$0
            kotlinx.coroutines.flow.SubscribedFlowCollector r2 = (kotlinx.coroutines.flow.SubscribedFlowCollector) r2
            kotlin.ResultKt.throwOnFailure(r7)     // Catch: all -> 0x0040
            goto L_0x0061
        L_0x0040:
            r7 = move-exception
            goto L_0x0082
        L_0x0042:
            kotlin.ResultKt.throwOnFailure(r7)
            kotlinx.coroutines.flow.FlowCollector<T> r7 = r6.collector
            kotlin.coroutines.CoroutineContext r2 = r0.getContext()
            kotlinx.coroutines.flow.internal.SafeCollector r5 = new kotlinx.coroutines.flow.internal.SafeCollector
            r5.<init>(r7, r2)
            kotlin.jvm.functions.Function2<kotlinx.coroutines.flow.FlowCollector<? super T>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> r7 = r6.action     // Catch: all -> 0x0080
            r0.L$0 = r6     // Catch: all -> 0x0080
            r0.L$1 = r5     // Catch: all -> 0x0080
            r0.label = r4     // Catch: all -> 0x0080
            java.lang.Object r7 = r7.invoke(r5, r0)     // Catch: all -> 0x0080
            if (r7 != r1) goto L_0x005f
            return r1
        L_0x005f:
            r2 = r6
            r6 = r5
        L_0x0061:
            r6.releaseIntercepted()
            kotlinx.coroutines.flow.FlowCollector<T> r6 = r2.collector
            boolean r7 = r6 instanceof kotlinx.coroutines.flow.SubscribedFlowCollector
            if (r7 == 0) goto L_0x007d
            kotlinx.coroutines.flow.SubscribedFlowCollector r6 = (kotlinx.coroutines.flow.SubscribedFlowCollector) r6
            r7 = 0
            r0.L$0 = r7
            r0.L$1 = r7
            r0.label = r3
            java.lang.Object r6 = r6.onSubscription(r0)
            if (r6 != r1) goto L_0x007a
            return r1
        L_0x007a:
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            return r6
        L_0x007d:
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            return r6
        L_0x0080:
            r7 = move-exception
            r6 = r5
        L_0x0082:
            r6.releaseIntercepted()
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.SubscribedFlowCollector.onSubscription(kotlin.coroutines.Continuation):java.lang.Object");
    }
}
