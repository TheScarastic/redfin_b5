package com.google.android.systemui.statusbar.notification.voicereplies;

import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CompletableDeferred;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableSharedFlow;
/* compiled from: NotificationVoiceReplyManager.kt */
/* access modifiers changed from: package-private */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1", f = "NotificationVoiceReplyManager.kt", l = {330}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ CompletableDeferred<Unit> $completion;
    final /* synthetic */ String $key;
    final /* synthetic */ NotificationVoiceReplyController.Connection $this_logUiEventsForActivatedVoiceReplyInputs;
    int label;
    private /* synthetic */ CoroutineScope p$;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1(NotificationVoiceReplyController.Connection connection, CompletableDeferred<Unit> completableDeferred, String str, Continuation<? super NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1> continuation) {
        super(2, continuation);
        this.$this_logUiEventsForActivatedVoiceReplyInputs = connection;
        this.$completion = completableDeferred;
        this.$key = str;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1 notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1 = new NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1(this.$this_logUiEventsForActivatedVoiceReplyInputs, this.$completion, this.$key, continuation);
        notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* compiled from: NotificationVoiceReplyManager.kt */
    /* access modifiers changed from: package-private */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1$1", f = "NotificationVoiceReplyManager.kt", l = {}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<String, Continuation<? super Boolean>, Object> {
        /* synthetic */ String it;
        int label;

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj3, Continuation<?> continuation) {
            AnonymousClass1 r0 = new AnonymousClass1(str, continuation);
            r0.it = (String) obj3;
            return r0;
        }

        public final Object invoke(String str, Continuation<? super Boolean> continuation) {
            return ((AnonymousClass1) create(str, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object unused = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (this.label == 0) {
                ResultKt.throwOnFailure(obj);
                return Boxing.boxBoolean(Intrinsics.areEqual(this.it, str));
            }
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            MutableSharedFlow<String> entryRemovals = this.$this_logUiEventsForActivatedVoiceReplyInputs.getEntryRemovals();
            final String str = this.$key;
            AnonymousClass1 r1 = new AnonymousClass1(null);
            this.label = 1;
            if (FlowKt.first(entryRemovals, r1, this) == obj2) {
                return obj2;
            }
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        CompletableDeferred<Unit> completableDeferred = this.$completion;
        Unit unit = Unit.INSTANCE;
        completableDeferred.complete(unit);
        return unit;
    }
}
