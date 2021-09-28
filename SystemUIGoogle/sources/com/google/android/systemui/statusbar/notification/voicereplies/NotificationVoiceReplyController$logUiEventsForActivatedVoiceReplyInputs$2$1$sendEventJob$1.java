package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.policy.RemoteInputView;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import kotlin.KotlinNothingValueException;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CompletableDeferred;
import kotlinx.coroutines.CoroutineScope;
/* compiled from: NotificationVoiceReplyManager.kt */
/* access modifiers changed from: package-private */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1", f = "NotificationVoiceReplyManager.kt", l = {1017}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ CompletableDeferred<Unit> $completion;
    final /* synthetic */ String $key;
    final /* synthetic */ RemoteInputView $riv;
    final /* synthetic */ NotificationVoiceReplyController.Connection $this_logUiEventsForActivatedVoiceReplyInputs;
    Object L$0;
    Object L$1;
    int label;
    private /* synthetic */ CoroutineScope p$;
    final /* synthetic */ NotificationVoiceReplyController this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1(RemoteInputView remoteInputView, NotificationVoiceReplyController.Connection connection, String str, NotificationVoiceReplyController notificationVoiceReplyController, CompletableDeferred<Unit> completableDeferred, Continuation<? super NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1> continuation) {
        super(2, continuation);
        this.$riv = remoteInputView;
        this.$this_logUiEventsForActivatedVoiceReplyInputs = connection;
        this.$key = str;
        this.this$0 = notificationVoiceReplyController;
        this.$completion = completableDeferred;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1 notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1 = new NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1(this.$riv, this.$this_logUiEventsForActivatedVoiceReplyInputs, this.$key, this.this$0, this.$completion, continuation);
        notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Throwable th;
        NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1 notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1;
        RemoteInputView remoteInputView;
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1 notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$12 = new RemoteInputView.OnSendRemoteInputListener(this.$this_logUiEventsForActivatedVoiceReplyInputs, this.$key, this.this$0, this.$completion) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1
                final /* synthetic */ CompletableDeferred<Unit> $completion;
                final /* synthetic */ String $key;
                final /* synthetic */ NotificationVoiceReplyController.Connection $this_logUiEventsForActivatedVoiceReplyInputs;
                final /* synthetic */ NotificationVoiceReplyController this$0;

                /* access modifiers changed from: package-private */
                {
                    this.$this_logUiEventsForActivatedVoiceReplyInputs = r1;
                    this.$key = r2;
                    this.this$0 = r3;
                    this.$completion = r4;
                }

                @Override // com.android.systemui.statusbar.policy.RemoteInputView.OnSendRemoteInputListener
                public void onSendRemoteInput() {
                    VoiceReplyState value = this.$this_logUiEventsForActivatedVoiceReplyInputs.getStateFlow().getValue();
                    String str = null;
                    InSession inSession = value instanceof InSession ? (InSession) value : null;
                    if (inSession != null) {
                        str = inSession.getTarget().getNotifKey();
                    }
                    if (!Intrinsics.areEqual(str, this.$key)) {
                        this.this$0.logger.logMsgSentDelayed(this.$key);
                    } else if (this.this$0.statusBarStateController.getState() == 2) {
                        this.this$0.logger.logMsgSentAuthBypassed(this.$key);
                    } else {
                        this.this$0.logger.logMsgSentUnlocked(this.$key);
                    }
                    this.$completion.complete(Unit.INSTANCE);
                }

                @Override // com.android.systemui.statusbar.policy.RemoteInputView.OnSendRemoteInputListener
                public void onSendRequestBounced() {
                    this.this$0.logger.logMsgSendBounced(this.$key);
                }
            };
            this.$riv.addOnSendRemoteInputListener(notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$12);
            RemoteInputView remoteInputView2 = this.$riv;
            try {
                this.L$0 = notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$12;
                this.L$1 = remoteInputView2;
                this.label = 1;
                Object result = new CancellableContinuationImpl(IntrinsicsKt__IntrinsicsJvmKt.intercepted(this), 1).getResult();
                if (result == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    DebugProbesKt.probeCoroutineSuspended(this);
                }
                if (result == obj2) {
                    return obj2;
                }
                notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1 = notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$12;
                remoteInputView = remoteInputView2;
            } catch (Throwable th2) {
                th = th2;
                remoteInputView = remoteInputView2;
                notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1 = notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$12;
                remoteInputView.removeOnSendRemoteInputListener(notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1);
                throw th;
            }
        } else if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            remoteInputView = (RemoteInputView) this.L$1;
            notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1 = (NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1) this.L$0;
            try {
                ResultKt.throwOnFailure(obj);
            } catch (Throwable th3) {
                th = th3;
                remoteInputView.removeOnSendRemoteInputListener(notificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2$1$sendEventJob$1$listener$1);
                throw th;
            }
        }
        throw new KotlinNothingValueException();
    }
}
