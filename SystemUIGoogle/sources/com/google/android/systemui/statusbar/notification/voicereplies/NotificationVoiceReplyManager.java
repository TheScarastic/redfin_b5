package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Job;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes2.dex */
public interface NotificationVoiceReplyManager extends Job {

    /* compiled from: NotificationVoiceReplyManager.kt */
    /* loaded from: classes2.dex */
    public interface Initializer {
        NotificationVoiceReplyManager connect(CoroutineScope coroutineScope);
    }

    VoiceReplySubscription registerHandler(NotificationVoiceReplyHandler notificationVoiceReplyHandler);
}
