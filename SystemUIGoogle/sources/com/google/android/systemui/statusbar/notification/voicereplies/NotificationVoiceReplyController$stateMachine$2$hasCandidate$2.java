package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.Flow;
/* compiled from: NotificationVoiceReplyManager.kt */
/* access modifiers changed from: package-private */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2", f = "NotificationVoiceReplyManager.kt", l = {440}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyController$stateMachine$2$hasCandidate$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ VoiceReplyTarget $candidate;
    final /* synthetic */ Flow<NotificationEntry> $reinflations;
    final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
    Object L$0;
    int label;
    private /* synthetic */ CoroutineScope p$;
    final /* synthetic */ NotificationVoiceReplyController this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$stateMachine$2$hasCandidate$2(NotificationVoiceReplyController.Connection connection, VoiceReplyTarget voiceReplyTarget, NotificationVoiceReplyController notificationVoiceReplyController, Flow<NotificationEntry> flow, Continuation<? super NotificationVoiceReplyController$stateMachine$2$hasCandidate$2> continuation) {
        super(2, continuation);
        this.$this_stateMachine = connection;
        this.$candidate = voiceReplyTarget;
        this.this$0 = notificationVoiceReplyController;
        this.$reinflations = flow;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$stateMachine$2$hasCandidate$2 notificationVoiceReplyController$stateMachine$2$hasCandidate$2 = new NotificationVoiceReplyController$stateMachine$2$hasCandidate$2(this.$this_stateMachine, this.$candidate, this.this$0, this.$reinflations, continuation);
        notificationVoiceReplyController$stateMachine$2$hasCandidate$2.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyController$stateMachine$2$hasCandidate$2;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$stateMachine$2$hasCandidate$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0075 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x007e  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x00cc  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeSuspend(java.lang.Object r18) {
        /*
            r17 = this;
            r0 = r17
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L_0x001f
            if (r2 != r3) goto L_0x0017
            java.lang.Object r2 = r0.L$0
            kotlinx.coroutines.channels.ChannelIterator r2 = (kotlinx.coroutines.channels.ChannelIterator) r2
            kotlin.ResultKt.throwOnFailure(r18)
            r4 = r18
            goto L_0x0076
        L_0x0017:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "call to 'resume' before 'invoke' with coroutine"
            r0.<init>(r1)
            throw r0
        L_0x001f:
            kotlin.ResultKt.throwOnFailure(r18)
            kotlinx.coroutines.CoroutineScope r4 = r0.p$
            r5 = 0
            r6 = 0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1 r7 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r2 = r0.$candidate
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r8 = r0.this$0
            r10 = 0
            r7.<init>(r2, r8, r10)
            r8 = 3
            r9 = 0
            kotlinx.coroutines.BuildersKt.launch$default(r4, r5, r6, r7, r8, r9)
            kotlinx.coroutines.CoroutineScope r11 = r0.p$
            r12 = 0
            r13 = 0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2 r14 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$2
            kotlinx.coroutines.flow.Flow<com.android.systemui.statusbar.notification.collection.NotificationEntry> r5 = r0.$reinflations
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r6 = r0.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r7 = r0.$this_stateMachine
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r8 = r0.$candidate
            r4 = r14
            r4.<init>(r5, r6, r7, r8, r9)
            r15 = 3
            r16 = 0
            kotlinx.coroutines.BuildersKt.launch$default(r11, r12, r13, r14, r15, r16)
            kotlinx.coroutines.CoroutineScope r4 = r0.p$
            r5 = 0
            r6 = 0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$3 r7 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$3
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r2 = r0.$this_stateMachine
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r8 = r0.$candidate
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r9 = r0.this$0
            r7.<init>(r2, r8, r9, r10)
            r8 = 3
            r9 = 0
            kotlinx.coroutines.BuildersKt.launch$default(r4, r5, r6, r7, r8, r9)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r2 = r0.$this_stateMachine
            kotlinx.coroutines.channels.Channel r2 = r2.getStartSessionRequests()
            kotlinx.coroutines.channels.ChannelIterator r2 = r2.iterator()
        L_0x006b:
            r0.L$0 = r2
            r0.label = r3
            java.lang.Object r4 = r2.hasNext(r0)
            if (r4 != r1) goto L_0x0076
            return r1
        L_0x0076:
            java.lang.Boolean r4 = (java.lang.Boolean) r4
            boolean r4 = r4.booleanValue()
            if (r4 == 0) goto L_0x00cc
            java.lang.Object r4 = r2.next()
            com.google.android.systemui.statusbar.notification.voicereplies.StartSessionRequest r4 = (com.google.android.systemui.statusbar.notification.voicereplies.StartSessionRequest) r4
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler r5 = r4.getHandler()
            int r5 = r5.getUserId()
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r6 = r0.$candidate
            int r6 = r6.getUserId()
            if (r5 == r6) goto L_0x00b3
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r5 = r0.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r5 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController.access$getLogger$p(r5)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler r6 = r4.getHandler()
            int r6 = r6.getUserId()
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r7 = r0.$candidate
            int r7 = r7.getUserId()
            r5.logUserIdMismatch(r6, r7)
            kotlin.jvm.functions.Function0 r4 = r4.getOnFail()
            r4.invoke()
            goto L_0x006b
        L_0x00b3:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r5 = r0.$this_stateMachine
            kotlinx.coroutines.flow.MutableStateFlow r5 = r5.getStateFlow()
            com.google.android.systemui.statusbar.notification.voicereplies.InSession r6 = new com.google.android.systemui.statusbar.notification.voicereplies.InSession
            java.lang.String r7 = r4.getInitialContent()
            kotlin.jvm.functions.Function2 r4 = r4.getBlock()
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r8 = r0.$candidate
            r6.<init>(r7, r4, r8)
            r5.setValue(r6)
            goto L_0x006b
        L_0x00cc:
            kotlin.Unit r0 = kotlin.Unit.INSTANCE
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$hasCandidate$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
