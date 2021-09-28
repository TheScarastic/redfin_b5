package kotlinx.coroutines.flow;

import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
/* compiled from: SafeCollector.kt */
/* loaded from: classes2.dex */
public final class FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1 implements Flow<T> {
    final /* synthetic */ Function2 $action$inlined;
    final /* synthetic */ Flow $this_unsafeTransform$inlined;

    public FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1(Flow flow, Function2 function2) {
        this.$this_unsafeTransform$inlined = flow;
        this.$action$inlined = function2;
    }

    @Override // kotlinx.coroutines.flow.Flow
    public Object collect(final FlowCollector flowCollector, Continuation continuation) {
        return this.$this_unsafeTransform$inlined.collect(new FlowCollector<?>() { // from class: kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1.2
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, kotlin.coroutines.Continuation] */
            /* JADX WARNING: Removed duplicated region for block: B:10:0x0024  */
            /* JADX WARNING: Removed duplicated region for block: B:16:0x005e  */
            /* JADX WARNING: Removed duplicated region for block: B:22:0x009a A[RETURN] */
            /* JADX WARNING: Removed duplicated region for block: B:23:0x009b A[PHI: r10 
              PHI: (r10v2 java.lang.Object) = (r10v3 java.lang.Object), (r10v1 java.lang.Object) binds: [B:21:0x0098, B:12:0x0028] A[DONT_GENERATE, DONT_INLINE], RETURN] */
            @Override // kotlinx.coroutines.flow.FlowCollector
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public java.lang.Object emit(java.lang.Object r9, kotlin.coroutines.Continuation r10) {
                /*
                    r8 = this;
                    boolean r0 = r10 instanceof kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1.AnonymousClass2.AnonymousClass1
                    if (r0 == 0) goto L_0x0013
                    r0 = r10
                    kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1$2$1 r0 = (kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1.AnonymousClass2.AnonymousClass1) r0
                    int r1 = r0.label
                    r2 = -2147483648(0xffffffff80000000, float:-0.0)
                    r3 = r1 & r2
                    if (r3 == 0) goto L_0x0013
                    int r1 = r1 - r2
                    r0.label = r1
                    goto L_0x0018
                L_0x0013:
                    kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1$2$1 r0 = new kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1$2$1
                    r0.<init>(r10)
                L_0x0018:
                    java.lang.Object r10 = r0.result
                    java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                    int r2 = r0.label
                    r3 = 2
                    r4 = 1
                    if (r2 == 0) goto L_0x005e
                    if (r2 == r4) goto L_0x0044
                    if (r2 != r3) goto L_0x003c
                    java.lang.Object r8 = r0.L$6
                    kotlinx.coroutines.flow.FlowCollector r8 = (kotlinx.coroutines.flow.FlowCollector) r8
                    java.lang.Object r8 = r0.L$4
                    kotlin.coroutines.Continuation r8 = (kotlin.coroutines.Continuation) r8
                    java.lang.Object r8 = r0.L$2
                    kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1$2$1 r8 = (kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1.AnonymousClass2.AnonymousClass1) r8
                    java.lang.Object r8 = r0.L$0
                    kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1$2 r8 = (kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1.AnonymousClass2) r8
                    kotlin.ResultKt.throwOnFailure(r10)
                    goto L_0x009b
                L_0x003c:
                    java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
                    java.lang.String r9 = "call to 'resume' before 'invoke' with coroutine"
                    r8.<init>(r9)
                    throw r8
                L_0x0044:
                    java.lang.Object r8 = r0.L$6
                    kotlinx.coroutines.flow.FlowCollector r8 = (kotlinx.coroutines.flow.FlowCollector) r8
                    java.lang.Object r9 = r0.L$5
                    java.lang.Object r2 = r0.L$4
                    kotlin.coroutines.Continuation r2 = (kotlin.coroutines.Continuation) r2
                    java.lang.Object r4 = r0.L$3
                    java.lang.Object r5 = r0.L$2
                    kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1$2$1 r5 = (kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1.AnonymousClass2.AnonymousClass1) r5
                    java.lang.Object r6 = r0.L$1
                    java.lang.Object r7 = r0.L$0
                    kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1$2 r7 = (kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1.AnonymousClass2) r7
                    kotlin.ResultKt.throwOnFailure(r10)
                    goto L_0x0084
                L_0x005e:
                    kotlin.ResultKt.throwOnFailure(r10)
                    kotlinx.coroutines.flow.FlowCollector r10 = kotlinx.coroutines.flow.FlowCollector.this
                    kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1 r2 = r2
                    kotlin.jvm.functions.Function2 r2 = r2.$action$inlined
                    r0.L$0 = r8
                    r0.L$1 = r9
                    r0.L$2 = r0
                    r0.L$3 = r9
                    r0.L$4 = r0
                    r0.L$5 = r9
                    r0.L$6 = r10
                    r0.label = r4
                    java.lang.Object r2 = r2.invoke(r9, r0)
                    if (r2 != r1) goto L_0x007e
                    return r1
                L_0x007e:
                    r7 = r8
                    r4 = r9
                    r6 = r4
                    r8 = r10
                    r2 = r0
                    r5 = r2
                L_0x0084:
                    r0.L$0 = r7
                    r0.L$1 = r6
                    r0.L$2 = r5
                    r0.L$3 = r4
                    r0.L$4 = r2
                    r0.L$5 = r9
                    r0.L$6 = r8
                    r0.label = r3
                    java.lang.Object r10 = r8.emit(r9, r0)
                    if (r10 != r1) goto L_0x009b
                    return r1
                L_0x009b:
                    return r10
                */
                throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__TransformKt$onEach$$inlined$unsafeTransform$1.AnonymousClass2.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
            }
        }, continuation);
    }
}
