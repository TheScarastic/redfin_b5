package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Ref$IntRef;
import kotlinx.coroutines.CoroutineScope;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1$onReceive$1", f = "NotificationVoiceReplyManager.kt", l = {991}, m = "invokeSuspend")
/* loaded from: classes2.dex */
final class DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1$onReceive$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ VoiceReplySubscription $subscription;
    final /* synthetic */ Ref$IntRef $token;
    int label;
    private /* synthetic */ CoroutineScope p$;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1$onReceive$1(VoiceReplySubscription voiceReplySubscription, Ref$IntRef ref$IntRef, Continuation<? super DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1$onReceive$1> continuation) {
        super(2, continuation);
        this.$subscription = voiceReplySubscription;
        this.$token = ref$IntRef;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1$onReceive$1 debugNotificationVoiceReplyClient$startClient$job$1$receiver$1$onReceive$1 = new DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1$onReceive$1(this.$subscription, this.$token, continuation);
        debugNotificationVoiceReplyClient$startClient$job$1$receiver$1$onReceive$1.p$ = (CoroutineScope) obj;
        return debugNotificationVoiceReplyClient$startClient$job$1$receiver$1$onReceive$1;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1$onReceive$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            VoiceReplySubscription voiceReplySubscription = this.$subscription;
            Ref$IntRef ref$IntRef = this.$token;
            int i2 = ref$IntRef.element;
            ref$IntRef.element = i2 + 1;
            AnonymousClass1 r4 = AnonymousClass1.INSTANCE;
            AnonymousClass2 r5 = new AnonymousClass2(null);
            this.label = 1;
            if (voiceReplySubscription.startVoiceReply(i2, null, r4, r5, this) == obj2) {
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
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1$onReceive$1$2", f = "NotificationVoiceReplyManager.kt", l = {}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1$onReceive$1$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static final class AnonymousClass2 extends SuspendLambda implements Function2<Session, Continuation<? super Unit>, Object> {
        int label;
        private /* synthetic */ Session p$;

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj3, Continuation<?> continuation) {
            AnonymousClass2 r0 = new AnonymousClass2(continuation);
            r0.p$ = (Session) obj3;
            return r0;
        }

        public final Object invoke(Session session, Continuation<? super Unit> continuation) {
            return ((AnonymousClass2) create(session, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object unused = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (this.label == 0) {
                ResultKt.throwOnFailure(obj);
                return Unit.INSTANCE;
            }
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
