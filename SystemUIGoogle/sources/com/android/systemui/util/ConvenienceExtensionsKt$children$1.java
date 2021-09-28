package com.android.systemui.util;

import android.view.View;
import android.view.ViewGroup;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.SequenceScope;
/* compiled from: ConvenienceExtensions.kt */
/* access modifiers changed from: package-private */
@DebugMetadata(c = "com.android.systemui.util.ConvenienceExtensionsKt$children$1", f = "ConvenienceExtensions.kt", l = {26}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class ConvenienceExtensionsKt$children$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super View>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ViewGroup $this_children;
    int I$0;
    int I$1;
    int label;
    private /* synthetic */ SequenceScope<View> p$;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ConvenienceExtensionsKt$children$1(ViewGroup viewGroup, Continuation<? super ConvenienceExtensionsKt$children$1> continuation) {
        super(2, continuation);
        this.$this_children = viewGroup;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        ConvenienceExtensionsKt$children$1 convenienceExtensionsKt$children$1 = new ConvenienceExtensionsKt$children$1(this.$this_children, continuation);
        convenienceExtensionsKt$children$1.p$ = (SequenceScope) obj;
        return convenienceExtensionsKt$children$1;
    }

    public final Object invoke(SequenceScope<? super View> sequenceScope, Continuation<? super Unit> continuation) {
        return ((ConvenienceExtensionsKt$children$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0026, code lost:
        if (r1 > 0) goto L_0x0028;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0028, code lost:
        r3 = r7 + 1;
        r4 = r6.p$;
        r7 = r6.$this_children.getChildAt(r7);
        r6.I$0 = r3;
        r6.I$1 = r1;
        r6.label = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003c, code lost:
        if (r4.yield(r7, r6) != r0) goto L_0x0012;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003e, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003f, code lost:
        if (r7 >= r1) goto L_0x0041;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0043, code lost:
        return kotlin.Unit.INSTANCE;
     */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeSuspend(java.lang.Object r7) {
        /*
            r6 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r6.label
            r2 = 1
            if (r1 == 0) goto L_0x001c
            if (r1 != r2) goto L_0x0014
            int r1 = r6.I$1
            int r3 = r6.I$0
            kotlin.ResultKt.throwOnFailure(r7)
        L_0x0012:
            r7 = r3
            goto L_0x003f
        L_0x0014:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
            r6.<init>(r7)
            throw r6
        L_0x001c:
            kotlin.ResultKt.throwOnFailure(r7)
            r7 = 0
            android.view.ViewGroup r1 = r6.$this_children
            int r1 = r1.getChildCount()
            if (r1 <= 0) goto L_0x0041
        L_0x0028:
            int r3 = r7 + 1
            kotlin.sequences.SequenceScope<android.view.View> r4 = r6.p$
            android.view.ViewGroup r5 = r6.$this_children
            android.view.View r7 = r5.getChildAt(r7)
            r6.I$0 = r3
            r6.I$1 = r1
            r6.label = r2
            java.lang.Object r7 = r4.yield(r7, r6)
            if (r7 != r0) goto L_0x0012
            return r0
        L_0x003f:
            if (r7 < r1) goto L_0x0028
        L_0x0041:
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.util.ConvenienceExtensionsKt$children$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
