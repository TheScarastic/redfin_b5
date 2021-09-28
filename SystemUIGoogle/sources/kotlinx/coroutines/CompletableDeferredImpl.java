package kotlinx.coroutines;
/* compiled from: CompletableDeferred.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class CompletableDeferredImpl<T> extends JobSupport implements CompletableDeferred<T> {
    @Override // kotlinx.coroutines.JobSupport
    public boolean getOnCancelComplete$kotlinx_coroutines_core() {
        return true;
    }

    public CompletableDeferredImpl(Job job) {
        super(true);
        initParentJobInternal$kotlinx_coroutines_core(job);
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0023  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0035  */
    @Override // kotlinx.coroutines.Deferred
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object await(kotlin.coroutines.Continuation<? super T> r5) {
        /*
            r4 = this;
            boolean r0 = r5 instanceof kotlinx.coroutines.CompletableDeferredImpl$await$1
            if (r0 == 0) goto L_0x0013
            r0 = r5
            kotlinx.coroutines.CompletableDeferredImpl$await$1 r0 = (kotlinx.coroutines.CompletableDeferredImpl$await$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L_0x0013
            int r1 = r1 - r2
            r0.label = r1
            goto L_0x0018
        L_0x0013:
            kotlinx.coroutines.CompletableDeferredImpl$await$1 r0 = new kotlinx.coroutines.CompletableDeferredImpl$await$1
            r0.<init>(r4, r5)
        L_0x0018:
            java.lang.Object r5 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L_0x0035
            if (r2 != r3) goto L_0x002d
            java.lang.Object r4 = r0.L$0
            kotlinx.coroutines.CompletableDeferredImpl r4 = (kotlinx.coroutines.CompletableDeferredImpl) r4
            kotlin.ResultKt.throwOnFailure(r5)
            goto L_0x0043
        L_0x002d:
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
            java.lang.String r5 = "call to 'resume' before 'invoke' with coroutine"
            r4.<init>(r5)
            throw r4
        L_0x0035:
            kotlin.ResultKt.throwOnFailure(r5)
            r0.L$0 = r4
            r0.label = r3
            java.lang.Object r5 = r4.awaitInternal$kotlinx_coroutines_core(r0)
            if (r5 != r1) goto L_0x0043
            return r1
        L_0x0043:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.CompletableDeferredImpl.await(kotlin.coroutines.Continuation):java.lang.Object");
    }

    @Override // kotlinx.coroutines.CompletableDeferred
    public boolean complete(T t) {
        return makeCompleting$kotlinx_coroutines_core(t);
    }
}
