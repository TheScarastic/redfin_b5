package com.google.android.systemui.statusbar;

import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager;
import com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplySubscription;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: NotificationVoiceReplyManagerService.kt */
/* access modifiers changed from: package-private */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2", f = "NotificationVoiceReplyManagerService.kt", l = {273}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ INotificationVoiceReplyServiceCallbacks $callbacks;
    final /* synthetic */ boolean $showCTA;
    Object L$0;
    int label;
    private /* synthetic */ CoroutineScope p$;
    final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$0;
    final /* synthetic */ NotificationVoiceReplyManagerService this$1;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2(NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1, boolean z, INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks, NotificationVoiceReplyManagerService notificationVoiceReplyManagerService, Continuation<? super NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2> continuation) {
        super(2, continuation);
        this.this$0 = notificationVoiceReplyManagerService$binder$1;
        this.$showCTA = z;
        this.$callbacks = iNotificationVoiceReplyServiceCallbacks;
        this.this$1 = notificationVoiceReplyManagerService;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2 notificationVoiceReplyManagerService$binder$1$enableCallbacks$2 = new NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2(this.this$0, this.$showCTA, this.$callbacks, this.this$1, continuation);
        notificationVoiceReplyManagerService$binder$1$enableCallbacks$2.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyManagerService$binder$1$enableCallbacks$2;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Throwable th;
        VoiceReplySubscription voiceReplySubscription;
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            CallbacksHandler callbacksHandler = new CallbacksHandler(this.this$0.getUserId(), this.$callbacks, NotificationVoiceReplyManagerServiceKt.stateIn(new NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$map$1(new NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$filter$1(this.this$0.getSetFeatureEnabledFlow(), this.this$0)), this.p$, Boxing.boxBoolean(this.$showCTA)));
            NotificationVoiceReplyManager notificationVoiceReplyManager = this.this$1.voiceReplyManager;
            if (notificationVoiceReplyManager != null) {
                VoiceReplySubscription registerHandler = notificationVoiceReplyManager.registerHandler(callbacksHandler);
                try {
                    NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$filter$2 notificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$filter$2 = new NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$filter$2(this.this$0.getStartVoiceReplyFlow(), this.this$0);
                    NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$collect$1 notificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$collect$1 = new FlowCollector<StartVoiceReplyData>(this, this.this$0, registerHandler, callbacksHandler) { // from class: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$collect$1
                        final /* synthetic */ CallbacksHandler $handler$inlined;
                        final /* synthetic */ VoiceReplySubscription $registration$inlined;
                        final /* synthetic */ NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2 this$0;
                        final /* synthetic */ NotificationVoiceReplyManagerService$binder$1 this$1$inlined;

                        {
                            this.this$0 = r1;
                            this.this$1$inlined = r2;
                            this.$registration$inlined = r3;
                            this.$handler$inlined = r4;
                        }

                        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, kotlin.coroutines.Continuation] */
                        @Override // kotlinx.coroutines.flow.FlowCollector
                        public Object emit(StartVoiceReplyData startVoiceReplyData, Continuation continuation) {
                            StartVoiceReplyData startVoiceReplyData2 = startVoiceReplyData;
                            Job job = BuildersKt__Builders_commonKt.launch$default(this.this$0.p$, null, null, new NotificationVoiceReplyManagerService$binder$1$enableCallbacks$2$2$1(this.this$1$inlined, this.$registration$inlined, this.$handler$inlined, startVoiceReplyData2.component2(), startVoiceReplyData2.component3(), null), 3, null);
                            if (job == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                                return job;
                            }
                            return Unit.INSTANCE;
                        }
                    };
                    this.L$0 = registerHandler;
                    this.label = 1;
                    if (notificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$filter$2.collect(notificationVoiceReplyManagerService$binder$1$enableCallbacks$2$invokeSuspend$$inlined$collect$1, this) == obj2) {
                        return obj2;
                    }
                    voiceReplySubscription = registerHandler;
                } catch (Throwable th2) {
                    th = th2;
                    voiceReplySubscription = registerHandler;
                    this.this$1.logger.logUnregisterCallbacks(this.this$0.getUserId());
                    voiceReplySubscription.unsubscribe();
                    throw th;
                }
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("voiceReplyManager");
                throw null;
            }
        } else if (i == 1) {
            voiceReplySubscription = (VoiceReplySubscription) this.L$0;
            try {
                ResultKt.throwOnFailure(obj);
            } catch (Throwable th3) {
                th = th3;
                this.this$1.logger.logUnregisterCallbacks(this.this$0.getUserId());
                voiceReplySubscription.unsubscribe();
                throw th;
            }
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        this.this$1.logger.logUnregisterCallbacks(this.this$0.getUserId());
        voiceReplySubscription.unsubscribe();
        return Unit.INSTANCE;
    }
}
