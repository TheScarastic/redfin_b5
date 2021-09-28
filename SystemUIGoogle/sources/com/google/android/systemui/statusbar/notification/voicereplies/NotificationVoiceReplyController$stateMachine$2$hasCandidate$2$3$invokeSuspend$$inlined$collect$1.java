package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.FlowCollector;
/* compiled from: Collect.kt */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$3$invokeSuspend$$inlined$collect$1 implements FlowCollector<String> {
    final /* synthetic */ NotificationVoiceReplyController.Connection $this_stateMachine$inlined;
    final /* synthetic */ NotificationVoiceReplyController this$0;

    public NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$3$invokeSuspend$$inlined$collect$1(NotificationVoiceReplyController notificationVoiceReplyController, NotificationVoiceReplyController.Connection connection) {
        this.this$0 = notificationVoiceReplyController;
        this.$this_stateMachine$inlined = connection;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, kotlin.coroutines.Continuation] */
    @Override // kotlinx.coroutines.flow.FlowCollector
    public Object emit(String str, Continuation continuation) {
        LogBuffer logBuffer = this.this$0.logger.getLogBuffer();
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logStatic$2 notificationVoiceReplyLogger$logStatic$2 = new NotificationVoiceReplyLogger$logStatic$2("Candidate notification was removed");
        if (!logBuffer.getFrozen()) {
            logBuffer.push(logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStatic$2));
        }
        this.$this_stateMachine$inlined.getStateFlow().setValue(this.this$0.queryInitialState(this.$this_stateMachine$inlined));
        return Unit.INSTANCE;
    }
}
