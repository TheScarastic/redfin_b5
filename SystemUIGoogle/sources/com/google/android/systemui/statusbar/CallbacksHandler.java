package com.google.android.systemui.statusbar;

import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.flow.StateFlow;
/* compiled from: NotificationVoiceReplyManagerService.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class CallbacksHandler implements NotificationVoiceReplyHandler {
    private final INotificationVoiceReplyServiceCallbacks callbacks;
    private final StateFlow<Boolean> showCta;
    private final int userId;

    public CallbacksHandler(int i, INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks, StateFlow<Boolean> stateFlow) {
        Intrinsics.checkNotNullParameter(iNotificationVoiceReplyServiceCallbacks, "callbacks");
        Intrinsics.checkNotNullParameter(stateFlow, "showCta");
        this.userId = i;
        this.callbacks = iNotificationVoiceReplyServiceCallbacks;
        this.showCta = stateFlow;
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
        this.callbacks.onNotifAvailableForReplyChanged(z);
    }

    public final void onVoiceReplyStarted(int i) {
        this.callbacks.onVoiceReplyHandled(i, 0);
    }

    public final void onVoiceReplyError(int i) {
        this.callbacks.onVoiceReplyHandled(i, 1);
    }
}
