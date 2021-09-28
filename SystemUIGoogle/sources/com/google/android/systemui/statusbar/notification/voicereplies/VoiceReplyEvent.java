package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.internal.logging.UiEventLogger;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public enum VoiceReplyEvent implements UiEventLogger.UiEventEnum {
    MSG_SEND_AUTH_BYPASSED(779),
    MSG_SEND_UNLOCKED(780),
    MSG_SEND_BOUNCED(781),
    MSG_SEND_DELAYED(782),
    STATE_NO_CANDIDATE(783),
    STATE_NEW_CANDIDATE_NO_CTA(784),
    STATE_NEW_CANDIDATE_CTA_OFF(785),
    STATE_NEW_CANDIDATE_CTA_PULSE(786),
    STATE_NEW_CANDIDATE_CTA_LOCKSCREEN(787),
    STATE_IN_SESSION_NO_START_TEXT(788),
    STATE_IN_SESSION_HAS_START_TEXT(789),
    SESSION_FAILED_BAD_NOTIF_STATE(790),
    SESSION_FAILED_BAD_SHADE_STATE(791),
    SESSION_FAILED_BAD_REMOTE_INPUT_STATE(792),
    SESSION_ENDED(793),
    SESSION_FAILED_BAD_WINDOW_STATE(826);
    
    private final int _id;

    VoiceReplyEvent(int i) {
        this._id = i;
    }

    public int getId() {
        return this._id;
    }
}
