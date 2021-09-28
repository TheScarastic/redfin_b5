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
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.MutableSharedFlow;
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$refreshCandidateOnNotifChanges$listener$1$onEntryRemoved$1", f = "NotificationVoiceReplyManager.kt", l = {254}, m = "invokeSuspend")
/* loaded from: classes2.dex */
final class NotificationVoiceReplyController$refreshCandidateOnNotifChanges$listener$1$onEntryRemoved$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ NotificationEntry $entry;
    final /* synthetic */ NotificationVoiceReplyController.Connection $this_refreshCandidateOnNotifChanges;
    int label;
    private /* synthetic */ CoroutineScope p$;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$refreshCandidateOnNotifChanges$listener$1$onEntryRemoved$1(NotificationVoiceReplyController.Connection connection, NotificationEntry notificationEntry, Continuation<? super NotificationVoiceReplyController$refreshCandidateOnNotifChanges$listener$1$onEntryRemoved$1> continuation) {
        super(2, continuation);
        this.$this_refreshCandidateOnNotifChanges = connection;
        this.$entry = notificationEntry;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        NotificationVoiceReplyController$refreshCandidateOnNotifChanges$listener$1$onEntryRemoved$1 notificationVoiceReplyController$refreshCandidateOnNotifChanges$listener$1$onEntryRemoved$1 = new NotificationVoiceReplyController$refreshCandidateOnNotifChanges$listener$1$onEntryRemoved$1(this.$this_refreshCandidateOnNotifChanges, this.$entry, continuation);
        notificationVoiceReplyController$refreshCandidateOnNotifChanges$listener$1$onEntryRemoved$1.p$ = (CoroutineScope) obj;
        return notificationVoiceReplyController$refreshCandidateOnNotifChanges$listener$1$onEntryRemoved$1;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((NotificationVoiceReplyController$refreshCandidateOnNotifChanges$listener$1$onEntryRemoved$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object obj2 = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            MutableSharedFlow<String> entryRemovals = this.$this_refreshCandidateOnNotifChanges.getEntryRemovals();
            String key = this.$entry.getKey();
            Intrinsics.checkNotNullExpressionValue(key, "entry.key");
            this.label = 1;
            if (entryRemovals.emit(key, this) == obj2) {
                return obj2;
            }
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }
}
