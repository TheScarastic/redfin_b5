package kotlinx.coroutines.flow;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Ref$ObjectRef;
/* compiled from: Collect.kt */
/* loaded from: classes2.dex */
public final class FlowKt__ReduceKt$first$$inlined$collect$2 implements FlowCollector<T> {
    final /* synthetic */ Function2 $predicate$inlined;
    final /* synthetic */ Ref$ObjectRef $result$inlined;

    public FlowKt__ReduceKt$first$$inlined$collect$2(Function2 function2, Ref$ObjectRef ref$ObjectRef) {
        this.$predicate$inlined = function2;
        this.$result$inlined = ref$ObjectRef;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r6v1, resolved type: T */
    /* JADX DEBUG: Multi-variable search result rejected for r6v5, resolved type: T */
    /* JADX DEBUG: Multi-variable search result rejected for r6v12, resolved type: T */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0024  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x006e A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x006f  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x007a  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x007d  */
    @Override // kotlinx.coroutines.flow.FlowCollector
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object emit(java.lang.Object r7, kotlin.coroutines.Continuation r8) {
        /*
            r6 = this;
            boolean r0 = r8 instanceof kotlinx.coroutines.flow.FlowKt__ReduceKt$first$$inlined$collect$2.AnonymousClass1
            if (r0 == 0) goto L_0x0013
            r0 = r8
            kotlinx.coroutines.flow.FlowKt__ReduceKt$first$$inlined$collect$2$1 r0 = (kotlinx.coroutines.flow.FlowKt__ReduceKt$first$$inlined$collect$2.AnonymousClass1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L_0x0013
            int r1 = r1 - r2
            r0.label = r1
            goto L_0x0018
        L_0x0013:
            kotlinx.coroutines.flow.FlowKt__ReduceKt$first$$inlined$collect$2$1 r0 = new kotlinx.coroutines.flow.FlowKt__ReduceKt$first$$inlined$collect$2$1
            r0.<init>(r8)
        L_0x0018:
            java.lang.Object r8 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 2
            r4 = 1
            if (r2 == 0) goto L_0x004c
            if (r2 == r4) goto L_0x003e
            if (r2 != r3) goto L_0x0036
            java.lang.Object r6 = r0.L$3
            java.lang.Object r7 = r0.L$2
            kotlin.coroutines.Continuation r7 = (kotlin.coroutines.Continuation) r7
            java.lang.Object r7 = r0.L$0
            kotlinx.coroutines.flow.FlowKt__ReduceKt$first$$inlined$collect$2 r7 = (kotlinx.coroutines.flow.FlowKt__ReduceKt$first$$inlined$collect$2) r7
            kotlin.ResultKt.throwOnFailure(r8)
            goto L_0x0072
        L_0x0036:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
            r6.<init>(r7)
            throw r6
        L_0x003e:
            java.lang.Object r7 = r0.L$3
            java.lang.Object r6 = r0.L$2
            kotlin.coroutines.Continuation r6 = (kotlin.coroutines.Continuation) r6
            java.lang.Object r6 = r0.L$0
            kotlinx.coroutines.flow.FlowKt__ReduceKt$first$$inlined$collect$2 r6 = (kotlinx.coroutines.flow.FlowKt__ReduceKt$first$$inlined$collect$2) r6
            kotlin.ResultKt.throwOnFailure(r8)
            goto L_0x006c
        L_0x004c:
            kotlin.ResultKt.throwOnFailure(r8)
            kotlin.jvm.functions.Function2 r8 = r6.$predicate$inlined
            r0.L$0 = r6
            r0.L$1 = r7
            r0.L$2 = r0
            r0.L$3 = r7
            r0.label = r3
            r0.L$0 = r6
            r0.L$1 = r7
            r0.L$2 = r0
            r0.L$3 = r7
            r0.label = r4
            java.lang.Object r8 = r8.invoke(r7, r0)
            if (r8 != r1) goto L_0x006c
            return r1
        L_0x006c:
            if (r8 != r1) goto L_0x006f
            return r1
        L_0x006f:
            r5 = r7
            r7 = r6
            r6 = r5
        L_0x0072:
            java.lang.Boolean r8 = (java.lang.Boolean) r8
            boolean r8 = r8.booleanValue()
            if (r8 != 0) goto L_0x007d
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            return r6
        L_0x007d:
            kotlin.jvm.internal.Ref$ObjectRef r7 = r7.$result$inlined
            r7.element = r6
            kotlinx.coroutines.flow.internal.AbortFlowException r6 = new kotlinx.coroutines.flow.internal.AbortFlowException
            r6.<init>()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__ReduceKt$first$$inlined$collect$2.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
    }
}
