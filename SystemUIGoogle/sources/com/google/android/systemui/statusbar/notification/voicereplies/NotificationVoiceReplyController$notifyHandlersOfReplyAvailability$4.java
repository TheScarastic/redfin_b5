package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
/* compiled from: NotificationVoiceReplyManager.kt */
/* access modifiers changed from: package-private */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfReplyAvailability$4", f = "NotificationVoiceReplyManager.kt", l = {1019}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyController$notifyHandlersOfReplyAvailability$4 extends SuspendLambda implements Function2<VoiceReplyTarget, Continuation<? super Unit>, Object> {
    Object L$0;
    /* synthetic */ VoiceReplyTarget candidate;
    int label;
    final /* synthetic */ NotificationVoiceReplyController this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$notifyHandlersOfReplyAvailability$4(NotificationVoiceReplyController notificationVoiceReplyController, Continuation<? super NotificationVoiceReplyController$notifyHandlersOfReplyAvailability$4> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyController;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$notifyHandlersOfReplyAvailability$4 notificationVoiceReplyController$notifyHandlersOfReplyAvailability$4 = new NotificationVoiceReplyController$notifyHandlersOfReplyAvailability$4(this.this$0, continuation);
        notificationVoiceReplyController$notifyHandlersOfReplyAvailability$4.candidate = (VoiceReplyTarget) obj;
        return notificationVoiceReplyController$notifyHandlersOfReplyAvailability$4;
    }

    public final Object invoke(VoiceReplyTarget voiceReplyTarget, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$notifyHandlersOfReplyAvailability$4) create(voiceReplyTarget, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0098 A[LOOP:1: B:30:0x0092->B:32:0x0098, LOOP_END] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeSuspend(java.lang.Object r6) {
        /*
            r5 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r5.label
            r2 = 1
            if (r1 == 0) goto L_0x001d
            if (r1 == r2) goto L_0x0013
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r6)
            throw r5
        L_0x0013:
            java.lang.Object r0 = r5.L$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r0 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController) r0
            kotlin.ResultKt.throwOnFailure(r6)     // Catch: all -> 0x001b
            goto L_0x0070
        L_0x001b:
            r6 = move-exception
            goto L_0x007a
        L_0x001d:
            kotlin.ResultKt.throwOnFailure(r6)
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r6 = r5.candidate
            if (r6 != 0) goto L_0x0027
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            return r5
        L_0x0027:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r6 = r5.this$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r6 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController.access$getLogger$p(r6)
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r1 = r5.candidate
            int r1 = r1.getUserId()
            r6.logCandidateUserChange(r1, r2)
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r6 = r5.candidate
            java.util.List r6 = r6.getHandlers()
            java.util.Iterator r6 = r6.iterator()
        L_0x0040:
            boolean r1 = r6.hasNext()
            if (r1 == 0) goto L_0x0050
            java.lang.Object r1 = r6.next()
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler r1 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler) r1
            r1.onNotifAvailableForReplyChanged(r2)
            goto L_0x0040
        L_0x0050:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r6 = r5.this$0
            r5.L$0 = r6     // Catch: all -> 0x0076
            r5.label = r2     // Catch: all -> 0x0076
            kotlinx.coroutines.CancellableContinuationImpl r1 = new kotlinx.coroutines.CancellableContinuationImpl     // Catch: all -> 0x0076
            kotlin.coroutines.Continuation r3 = kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(r5)     // Catch: all -> 0x0076
            r1.<init>(r3, r2)     // Catch: all -> 0x0076
            java.lang.Object r1 = r1.getResult()     // Catch: all -> 0x0076
            java.lang.Object r2 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()     // Catch: all -> 0x0076
            if (r1 != r2) goto L_0x006c
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(r5)     // Catch: all -> 0x0076
        L_0x006c:
            if (r1 != r0) goto L_0x006f
            return r0
        L_0x006f:
            r0 = r6
        L_0x0070:
            kotlin.KotlinNothingValueException r6 = new kotlin.KotlinNothingValueException     // Catch: all -> 0x001b
            r6.<init>()     // Catch: all -> 0x001b
            throw r6     // Catch: all -> 0x001b
        L_0x0076:
            r0 = move-exception
            r4 = r0
            r0 = r6
            r6 = r4
        L_0x007a:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r0 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController.access$getLogger$p(r0)
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r1 = r5.candidate
            int r1 = r1.getUserId()
            r2 = 0
            r0.logCandidateUserChange(r1, r2)
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r5 = r5.candidate
            java.util.List r5 = r5.getHandlers()
            java.util.Iterator r5 = r5.iterator()
        L_0x0092:
            boolean r0 = r5.hasNext()
            if (r0 == 0) goto L_0x00a2
            java.lang.Object r0 = r5.next()
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler r0 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler) r0
            r0.onNotifAvailableForReplyChanged(r2)
            goto L_0x0092
        L_0x00a2:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$notifyHandlersOfReplyAvailability$4.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
