package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.policy.RemoteInputView;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import kotlin.Pair;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: Collect.kt */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1 implements FlowCollector<Pair<? extends String, ? extends RemoteInputView>> {
    final /* synthetic */ Flow $remoteInputViewActivatedForVoiceReply$inlined;
    final /* synthetic */ NotificationVoiceReplyController.Connection $this_logUiEventsForActivatedVoiceReplyInputs$inlined;
    final /* synthetic */ NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2 this$0;
    final /* synthetic */ NotificationVoiceReplyController this$1$inlined;

    public NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1(NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2 notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2, NotificationVoiceReplyController.Connection connection, NotificationVoiceReplyController notificationVoiceReplyController, Flow flow) {
        this.this$0 = notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2;
        this.$this_logUiEventsForActivatedVoiceReplyInputs$inlined = connection;
        this.this$1$inlined = notificationVoiceReplyController;
        this.$remoteInputViewActivatedForVoiceReply$inlined = flow;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, kotlin.coroutines.Continuation] */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0028  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0043  */
    @Override // kotlinx.coroutines.flow.FlowCollector
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object emit(kotlin.Pair<? extends java.lang.String, ? extends com.android.systemui.statusbar.policy.RemoteInputView> r19, kotlin.coroutines.Continuation r20) {
        /*
            r18 = this;
            r0 = r18
            r1 = r20
            boolean r2 = r1 instanceof com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1.AnonymousClass1
            if (r2 == 0) goto L_0x0017
            r2 = r1
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1$1 r2 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1.AnonymousClass1) r2
            int r3 = r2.label
            r4 = -2147483648(0xffffffff80000000, float:-0.0)
            r5 = r3 & r4
            if (r5 == 0) goto L_0x0017
            int r3 = r3 - r4
            r2.label = r3
            goto L_0x001c
        L_0x0017:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1$1 r2 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1$1
            r2.<init>(r0, r1)
        L_0x001c:
            java.lang.Object r1 = r2.result
            java.lang.Object r3 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r4 = r2.label
            r5 = 1
            r6 = 0
            if (r4 == 0) goto L_0x0043
            if (r4 != r5) goto L_0x003b
            java.lang.Object r0 = r2.L$2
            kotlinx.coroutines.Job r0 = (kotlinx.coroutines.Job) r0
            java.lang.Object r3 = r2.L$1
            kotlinx.coroutines.Job r3 = (kotlinx.coroutines.Job) r3
            java.lang.Object r2 = r2.L$0
            kotlinx.coroutines.Job r2 = (kotlinx.coroutines.Job) r2
            kotlin.ResultKt.throwOnFailure(r1)
            goto L_0x00b7
        L_0x003b:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "call to 'resume' before 'invoke' with coroutine"
            r0.<init>(r1)
            throw r0
        L_0x0043:
            kotlin.ResultKt.throwOnFailure(r1)
            r1 = r19
            kotlin.Pair r1 = (kotlin.Pair) r1
            java.lang.Object r4 = r1.component1()
            java.lang.String r4 = (java.lang.String) r4
            java.lang.Object r1 = r1.component2()
            r8 = r1
            com.android.systemui.statusbar.policy.RemoteInputView r8 = (com.android.systemui.statusbar.policy.RemoteInputView) r8
            kotlinx.coroutines.CompletableDeferred r1 = kotlinx.coroutines.CompletableDeferredKt.CompletableDeferred$default(r6, r5, r6)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2 r7 = r0.this$0
            kotlinx.coroutines.CoroutineScope r14 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2.access$getP$$p(r7)
            r15 = 0
            r16 = 0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1 r17 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r9 = r0.$this_logUiEventsForActivatedVoiceReplyInputs$inlined
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r11 = r0.this$1$inlined
            r13 = 0
            r7 = r17
            r10 = r4
            r12 = r1
            r7.<init>(r8, r9, r10, r11, r12, r13)
            r13 = 3
            r7 = 0
            r9 = r14
            r10 = r15
            r11 = r16
            r12 = r17
            r14 = r7
            kotlinx.coroutines.Job r7 = kotlinx.coroutines.BuildersKt.launch$default(r9, r10, r11, r12, r13, r14)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2 r8 = r0.this$0
            kotlinx.coroutines.CoroutineScope r9 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2.access$getP$$p(r8)
            r10 = 0
            r11 = 0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1 r12 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$removalJob$1
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection r8 = r0.$this_logUiEventsForActivatedVoiceReplyInputs$inlined
            r12.<init>(r8, r1, r4, r6)
            r14 = 0
            kotlinx.coroutines.Job r8 = kotlinx.coroutines.BuildersKt.launch$default(r9, r10, r11, r12, r13, r14)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2 r9 = r0.this$0
            kotlinx.coroutines.CoroutineScope r10 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2.access$getP$$p(r9)
            r12 = 0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$restartedJob$1 r13 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$restartedJob$1
            kotlinx.coroutines.flow.Flow r0 = r0.$remoteInputViewActivatedForVoiceReply$inlined
            r13.<init>(r0, r1, r4, r6)
            r14 = 3
            kotlinx.coroutines.Job r0 = kotlinx.coroutines.BuildersKt.launch$default(r10, r11, r12, r13, r14, r15)
            r2.L$0 = r7
            r2.L$1 = r8
            r2.L$2 = r0
            r2.label = r5
            java.lang.Object r1 = r1.await(r2)
            if (r1 != r3) goto L_0x00b5
            return r3
        L_0x00b5:
            r2 = r7
            r3 = r8
        L_0x00b7:
            kotlinx.coroutines.Job.DefaultImpls.cancel$default(r2, r6, r5, r6)
            kotlinx.coroutines.Job.DefaultImpls.cancel$default(r3, r6, r5, r6)
            kotlinx.coroutines.Job.DefaultImpls.cancel$default(r0, r6, r5, r6)
            kotlin.Unit r0 = kotlin.Unit.INSTANCE
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$invokeSuspend$$inlined$collect$1.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
    }
}
