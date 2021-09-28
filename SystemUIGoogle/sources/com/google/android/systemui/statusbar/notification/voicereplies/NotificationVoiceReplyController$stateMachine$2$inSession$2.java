package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.policy.RemoteInputView;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.MutableSharedFlow;
/* compiled from: NotificationVoiceReplyManager.kt */
/* access modifiers changed from: package-private */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2", f = "NotificationVoiceReplyManager.kt", l = {485}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyController$stateMachine$2$inSession$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2<Session, Continuation<? super Unit>, Object> $block;
    final /* synthetic */ String $initialContent;
    final /* synthetic */ MutableSharedFlow<Pair<String, RemoteInputView>> $remoteInputViewActivatedForVoiceReply;
    final /* synthetic */ VoiceReplyTarget $target;
    final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine;
    Object L$0;
    int label;
    private /* synthetic */ CoroutineScope p$;
    final /* synthetic */ NotificationVoiceReplyController this$0;

    /* JADX DEBUG: Multi-variable search result rejected for r6v0, resolved type: kotlin.jvm.functions.Function2<? super com.google.android.systemui.statusbar.notification.voicereplies.Session, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$stateMachine$2$inSession$2(NotificationVoiceReplyController notificationVoiceReplyController, VoiceReplyTarget voiceReplyTarget, NotificationVoiceReplyController.Connection connection, String str, MutableSharedFlow<Pair<String, RemoteInputView>> mutableSharedFlow, Function2<? super Session, ? super Continuation<? super Unit>, ? extends Object> function2, Continuation<? super NotificationVoiceReplyController$stateMachine$2$inSession$2> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyController;
        this.$target = voiceReplyTarget;
        this.$this_stateMachine = connection;
        this.$initialContent = str;
        this.$remoteInputViewActivatedForVoiceReply = mutableSharedFlow;
        this.$block = function2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$stateMachine$2$inSession$2 notificationVoiceReplyController$stateMachine$2$inSession$2 = new NotificationVoiceReplyController$stateMachine$2$inSession$2(this.this$0, this.$target, this.$this_stateMachine, this.$initialContent, this.$remoteInputViewActivatedForVoiceReply, this.$block, continuation);
        notificationVoiceReplyController$stateMachine$2$inSession$2.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyController$stateMachine$2$inSession$2;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$stateMachine$2$inSession$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0076 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x009e  */
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
            goto L_0x0077
        L_0x0017:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "call to 'resume' before 'invoke' with coroutine"
            r0.<init>(r1)
            throw r0
        L_0x001f:
            kotlin.ResultKt.throwOnFailure(r18)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r2 = r0.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r2 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController.access$getLogger$p(r2)
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r4 = r0.$target
            java.lang.String r4 = r4.getNotifKey()
            r2.logStateInSession(r4)
            kotlinx.coroutines.CoroutineScope r5 = r0.p$
            r6 = 0
            r7 = 0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$1 r2 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$1
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r9 = r0.$target
            java.lang.String r10 = r0.$initialContent
            kotlinx.coroutines.flow.MutableSharedFlow<kotlin.Pair<java.lang.String, com.android.systemui.statusbar.policy.RemoteInputView>> r11 = r0.$remoteInputViewActivatedForVoiceReply
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r12 = r0.$this_stateMachine
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r13 = r0.this$0
            kotlin.jvm.functions.Function2<com.google.android.systemui.statusbar.notification.voicereplies.Session, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> r14 = r0.$block
            r15 = 0
            r8 = r2
            r8.<init>(r9, r10, r11, r12, r13, r14, r15)
            r9 = 3
            r10 = 0
            kotlinx.coroutines.BuildersKt.launch$default(r5, r6, r7, r8, r9, r10)
            kotlinx.coroutines.CoroutineScope r11 = r0.p$
            r12 = 0
            r13 = 0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$2 r14 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2$2
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r2 = r0.$this_stateMachine
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r4 = r0.$target
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r5 = r0.this$0
            r14.<init>(r2, r4, r5, r6)
            r15 = 3
            r16 = 0
            kotlinx.coroutines.BuildersKt.launch$default(r11, r12, r13, r14, r15, r16)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r2 = r0.$this_stateMachine
            kotlinx.coroutines.channels.Channel r2 = r2.getStartSessionRequests()
            kotlinx.coroutines.channels.ChannelIterator r2 = r2.iterator()
        L_0x006c:
            r0.L$0 = r2
            r0.label = r3
            java.lang.Object r4 = r2.hasNext(r0)
            if (r4 != r1) goto L_0x0077
            return r1
        L_0x0077:
            java.lang.Boolean r4 = (java.lang.Boolean) r4
            boolean r4 = r4.booleanValue()
            if (r4 == 0) goto L_0x009e
            java.lang.Object r4 = r2.next()
            com.google.android.systemui.statusbar.notification.voicereplies.StartSessionRequest r4 = (com.google.android.systemui.statusbar.notification.voicereplies.StartSessionRequest) r4
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r5 = r0.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r5 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController.access$getLogger$p(r5)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler r6 = r4.getHandler()
            int r6 = r6.getUserId()
            r5.logSessionAlreadyInProgress(r6)
            kotlin.jvm.functions.Function0 r4 = r4.getOnFail()
            r4.invoke()
            goto L_0x006c
        L_0x009e:
            kotlin.Unit r0 = kotlin.Unit.INSTANCE
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$stateMachine$2$inSession$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
