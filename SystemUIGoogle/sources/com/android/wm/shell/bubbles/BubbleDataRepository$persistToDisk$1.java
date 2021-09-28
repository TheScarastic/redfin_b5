package com.android.wm.shell.bubbles;

import androidx.constraintlayout.widget.R$styleable;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobKt;
import kotlinx.coroutines.YieldKt;
/* compiled from: BubbleDataRepository.kt */
/* access modifiers changed from: package-private */
@DebugMetadata(c = "com.android.wm.shell.bubbles.BubbleDataRepository$persistToDisk$1", f = "BubbleDataRepository.kt", l = {R$styleable.Constraint_visibilityMode, 112}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class BubbleDataRepository$persistToDisk$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Job $prev;
    int label;
    private /* synthetic */ CoroutineScope p$;
    final /* synthetic */ BubbleDataRepository this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public BubbleDataRepository$persistToDisk$1(Job job, BubbleDataRepository bubbleDataRepository, Continuation<? super BubbleDataRepository$persistToDisk$1> continuation) {
        super(2, continuation);
        this.$prev = job;
        this.this$0 = bubbleDataRepository;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        BubbleDataRepository$persistToDisk$1 bubbleDataRepository$persistToDisk$1 = new BubbleDataRepository$persistToDisk$1(this.$prev, this.this$0, continuation);
        bubbleDataRepository$persistToDisk$1.p$ = (CoroutineScope) obj;
        return bubbleDataRepository$persistToDisk$1;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((BubbleDataRepository$persistToDisk$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Job job = this.$prev;
            if (job != null) {
                this.label = 1;
                if (JobKt.cancelAndJoin(job, this) == obj2) {
                    return obj2;
                }
            }
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else if (i == 2) {
            ResultKt.throwOnFailure(obj);
            this.this$0.persistentRepository.persistsToDisk(this.this$0.volatileRepository.getBubbles());
            return Unit.INSTANCE;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        this.label = 2;
        if (YieldKt.yield(this) == obj2) {
            return obj2;
        }
        this.this$0.persistentRepository.persistsToDisk(this.this$0.volatileRepository.getBubbles());
        return Unit.INSTANCE;
    }
}
