package com.google.android.systemui.statusbar.notification.voicereplies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.android.systemui.broadcast.BroadcastDispatcher;
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
import kotlin.jvm.internal.Ref$BooleanRef;
import kotlin.jvm.internal.Ref$IntRef;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;
/* compiled from: NotificationVoiceReplyManager.kt */
/* access modifiers changed from: package-private */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.DebugNotificationVoiceReplyClient$startClient$job$1", f = "NotificationVoiceReplyManager.kt", l = {1018}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class DebugNotificationVoiceReplyClient$startClient$job$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private /* synthetic */ CoroutineScope p$;
    final /* synthetic */ DebugNotificationVoiceReplyClient this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public DebugNotificationVoiceReplyClient$startClient$job$1(DebugNotificationVoiceReplyClient debugNotificationVoiceReplyClient, Continuation<? super DebugNotificationVoiceReplyClient$startClient$job$1> continuation) {
        super(2, continuation);
        this.this$0 = debugNotificationVoiceReplyClient;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        DebugNotificationVoiceReplyClient$startClient$job$1 debugNotificationVoiceReplyClient$startClient$job$1 = new DebugNotificationVoiceReplyClient$startClient$job$1(this.this$0, continuation);
        debugNotificationVoiceReplyClient$startClient$job$1.p$ = (CoroutineScope) obj;
        return debugNotificationVoiceReplyClient$startClient$job$1;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((DebugNotificationVoiceReplyClient$startClient$job$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Throwable th;
        VoiceReplySubscription voiceReplySubscription;
        DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1 debugNotificationVoiceReplyClient$startClient$job$1$receiver$1;
        DebugNotificationVoiceReplyClient debugNotificationVoiceReplyClient;
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            NotificationVoiceReplyManager connect = this.this$0.voiceReplyInitializer.connect(this.p$);
            Ref$BooleanRef ref$BooleanRef = new Ref$BooleanRef();
            VoiceReplySubscription registerHandler = connect.registerHandler(new NotificationVoiceReplyHandler(this.this$0, ref$BooleanRef) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.DebugNotificationVoiceReplyClient$startClient$job$1$subscription$1
                final /* synthetic */ Ref$BooleanRef $notifAvailable;
                private final StateFlow<Boolean> showCta = StateFlowKt.MutableStateFlow(Boolean.TRUE);
                final /* synthetic */ DebugNotificationVoiceReplyClient this$0;
                private final int userId;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$notifAvailable = r2;
                    this.userId = r1.lockscreenUserManager.getCurrentUserId();
                }

                @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler
                public int getUserId() {
                    return this.userId;
                }

                @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler
                public StateFlow<Boolean> getShowCta() {
                    return this.showCta;
                }

                @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler
                public void onNotifAvailableForReplyChanged(boolean z) {
                    this.$notifAvailable.element = z;
                }
            });
            DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1 debugNotificationVoiceReplyClient$startClient$job$1$receiver$12 = new BroadcastReceiver(ref$BooleanRef, this, registerHandler, new Ref$IntRef()) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1
                final /* synthetic */ Ref$BooleanRef $notifAvailable;
                final /* synthetic */ VoiceReplySubscription $subscription;
                final /* synthetic */ Ref$IntRef $token;
                final /* synthetic */ DebugNotificationVoiceReplyClient$startClient$job$1 this$0;

                /* access modifiers changed from: package-private */
                {
                    this.$notifAvailable = r1;
                    this.this$0 = r2;
                    this.$subscription = r3;
                    this.$token = r4;
                }

                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if (!this.$notifAvailable.element) {
                        Log.d("NotifVoiceReplyDebug", "no notification available for voice reply");
                    }
                    Job unused = BuildersKt__Builders_commonKt.launch$default(this.this$0.p$, null, null, new DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1$onReceive$1(this.$subscription, this.$token, null), 3, null);
                }
            };
            BroadcastDispatcher broadcastDispatcher = this.this$0.broadcastDispatcher;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.google.android.systemui.START_VOICE_REPLY");
            Unit unit = Unit.INSTANCE;
            BroadcastDispatcher.registerReceiver$default(broadcastDispatcher, debugNotificationVoiceReplyClient$startClient$job$1$receiver$12, intentFilter, null, null, 12, null);
            DebugNotificationVoiceReplyClient debugNotificationVoiceReplyClient2 = this.this$0;
            try {
                this.L$0 = registerHandler;
                this.L$1 = debugNotificationVoiceReplyClient$startClient$job$1$receiver$12;
                this.L$2 = debugNotificationVoiceReplyClient2;
                this.label = 1;
                Object result = new CancellableContinuationImpl(IntrinsicsKt__IntrinsicsJvmKt.intercepted(this), 1).getResult();
                if (result == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    DebugProbesKt.probeCoroutineSuspended(this);
                }
                if (result == obj2) {
                    return obj2;
                }
                voiceReplySubscription = registerHandler;
                debugNotificationVoiceReplyClient = debugNotificationVoiceReplyClient2;
                debugNotificationVoiceReplyClient$startClient$job$1$receiver$1 = debugNotificationVoiceReplyClient$startClient$job$1$receiver$12;
            } catch (Throwable th2) {
                th = th2;
                debugNotificationVoiceReplyClient = debugNotificationVoiceReplyClient2;
                debugNotificationVoiceReplyClient$startClient$job$1$receiver$1 = debugNotificationVoiceReplyClient$startClient$job$1$receiver$12;
                voiceReplySubscription = registerHandler;
                debugNotificationVoiceReplyClient.broadcastDispatcher.unregisterReceiver(debugNotificationVoiceReplyClient$startClient$job$1$receiver$1);
                voiceReplySubscription.unsubscribe();
                throw th;
            }
        } else if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            debugNotificationVoiceReplyClient = (DebugNotificationVoiceReplyClient) this.L$2;
            debugNotificationVoiceReplyClient$startClient$job$1$receiver$1 = (DebugNotificationVoiceReplyClient$startClient$job$1$receiver$1) this.L$1;
            voiceReplySubscription = (VoiceReplySubscription) this.L$0;
            try {
                ResultKt.throwOnFailure(obj);
            } catch (Throwable th3) {
                th = th3;
                debugNotificationVoiceReplyClient.broadcastDispatcher.unregisterReceiver(debugNotificationVoiceReplyClient$startClient$job$1$receiver$1);
                voiceReplySubscription.unsubscribe();
                throw th;
            }
        }
        throw new KotlinNothingValueException();
    }
}
