package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: NotificationVoiceReplyManager.kt */
/* access modifiers changed from: package-private */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$noCandidate$2", f = "NotificationVoiceReplyManager.kt", l = {1019}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyController$stateMachine$2$noCandidate$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Flow<NotificationEntry> $reinflations;
    final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
    int label;
    private /* synthetic */ CoroutineScope p$;
    final /* synthetic */ NotificationVoiceReplyController this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$stateMachine$2$noCandidate$2(NotificationVoiceReplyController notificationVoiceReplyController, Flow<NotificationEntry> flow, NotificationVoiceReplyController.Connection connection, Continuation<? super NotificationVoiceReplyController$stateMachine$2$noCandidate$2> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyController;
        this.$reinflations = flow;
        this.$this_stateMachine = connection;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$stateMachine$2$noCandidate$2 notificationVoiceReplyController$stateMachine$2$noCandidate$2 = new NotificationVoiceReplyController$stateMachine$2$noCandidate$2(this.this$0, this.$reinflations, this.$this_stateMachine, continuation);
        notificationVoiceReplyController$stateMachine$2$noCandidate$2.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyController$stateMachine$2$noCandidate$2;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$stateMachine$2$noCandidate$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            this.this$0.logger.logStateNoCandidate();
            CoroutineScope coroutineScope = this.p$;
            final NotificationVoiceReplyController.Connection connection = this.$this_stateMachine;
            final NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
            Job unused = BuildersKt__Builders_commonKt.launch$default(coroutineScope, null, null, new AnonymousClass1(null), 3, null);
            NotificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$mapNotNull$1 notificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$mapNotNull$1 = new NotificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$mapNotNull$1(this.$reinflations, this.this$0, this.$this_stateMachine);
            NotificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$collect$1 notificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$collect$1 = new FlowCollector<VoiceReplyTarget>(this.$this_stateMachine) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$collect$1
                final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine$inlined;

                {
                    this.$this_stateMachine$inlined = r1;
                }

                /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, kotlin.coroutines.Continuation] */
                @Override // kotlinx.coroutines.flow.FlowCollector
                public Object emit(VoiceReplyTarget voiceReplyTarget, Continuation continuation) {
                    this.$this_stateMachine$inlined.getStateFlow().setValue(new HasCandidate(voiceReplyTarget));
                    return Unit.INSTANCE;
                }
            };
            this.label = 1;
            if (notificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$mapNotNull$1.collect(notificationVoiceReplyController$stateMachine$2$noCandidate$2$invokeSuspend$$inlined$collect$1, this) == obj2) {
                return obj2;
            }
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }

    /* compiled from: NotificationVoiceReplyManager.kt */
    /* access modifiers changed from: package-private */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$noCandidate$2$1", f = "NotificationVoiceReplyManager.kt", l = {376}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$noCandidate$2$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        Object L$0;
        int label;
        private /* synthetic */ CoroutineScope p$;

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj3, Continuation<?> continuation) {
            AnonymousClass1 r0 = new AnonymousClass1(connection, notificationVoiceReplyController, continuation);
            r0.p$ = (CoroutineScope) obj3;
            return r0;
        }

        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARNING: Removed duplicated region for block: B:11:0x0033 A[RETURN] */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x003c  */
        /* JADX WARNING: Removed duplicated region for block: B:15:0x005b  */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final java.lang.Object invokeSuspend(java.lang.Object r6) {
            /*
                r5 = this;
                java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                int r1 = r5.label
                r2 = 1
                if (r1 == 0) goto L_0x001b
                if (r1 != r2) goto L_0x0013
                java.lang.Object r1 = r5.L$0
                kotlinx.coroutines.channels.ChannelIterator r1 = (kotlinx.coroutines.channels.ChannelIterator) r1
                kotlin.ResultKt.throwOnFailure(r6)
                goto L_0x0034
            L_0x0013:
                java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
                java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
                r5.<init>(r6)
                throw r5
            L_0x001b:
                kotlin.ResultKt.throwOnFailure(r6)
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r6 = r10
                kotlinx.coroutines.channels.Channel r6 = r6.getStartSessionRequests()
                kotlinx.coroutines.channels.ChannelIterator r6 = r6.iterator()
                r1 = r6
            L_0x0029:
                r5.L$0 = r1
                r5.label = r2
                java.lang.Object r6 = r1.hasNext(r5)
                if (r6 != r0) goto L_0x0034
                return r0
            L_0x0034:
                java.lang.Boolean r6 = (java.lang.Boolean) r6
                boolean r6 = r6.booleanValue()
                if (r6 == 0) goto L_0x005b
                java.lang.Object r6 = r1.next()
                com.google.android.systemui.statusbar.notification.voicereplies.StartSessionRequest r6 = (com.google.android.systemui.statusbar.notification.voicereplies.StartSessionRequest) r6
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r3 = r1
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r3 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController.access$getLogger$p(r3)
                com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler r4 = r6.getHandler()
                int r4 = r4.getUserId()
                r3.logStartSessionNoCandidate(r4)
                kotlin.jvm.functions.Function0 r6 = r6.getOnFail()
                r6.invoke()
                goto L_0x0029
            L_0x005b:
                kotlin.Unit r5 = kotlin.Unit.INSTANCE
                return r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$noCandidate$2.AnonymousClass1.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }
}
