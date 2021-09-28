package com.google.android.systemui.statusbar.notification.voicereplies;

import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyLogger {
    public static final Companion Companion = new Companion(null);
    private final UiEventLogger eventLogger;
    private final LogBuffer logBuffer;

    /* compiled from: NotificationVoiceReplyLogger.kt */
    /* loaded from: classes2.dex */
    private enum CtaState {
        NOT_DOZING,
        DISABLED,
        NO_HUN_VIEW,
        SHOWING
    }

    /* compiled from: NotificationVoiceReplyLogger.kt */
    /* loaded from: classes2.dex */
    private enum SendType {
        UNLOCKED,
        BYPASS,
        DELAYED,
        BOUNCED
    }

    public NotificationVoiceReplyLogger(LogBuffer logBuffer, UiEventLogger uiEventLogger) {
        Intrinsics.checkNotNullParameter(logBuffer, "logBuffer");
        Intrinsics.checkNotNullParameter(uiEventLogger, "eventLogger");
        this.logBuffer = logBuffer;
        this.eventLogger = uiEventLogger;
    }

    public final LogBuffer getLogBuffer() {
        return this.logBuffer;
    }

    public final void logReinflation(String str, String str2) {
        Intrinsics.checkNotNullParameter(str, "notifKey");
        Intrinsics.checkNotNullParameter(str2, "reason");
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logReinflation$2 notificationVoiceReplyLogger$logReinflation$2 = NotificationVoiceReplyLogger$logReinflation$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logReinflation$2);
            obtain.setStr1(str);
            obtain.setStr2(str2);
            logBuffer.push(obtain);
        }
    }

    public final void logReinflationDropped(String str, String str2) {
        Intrinsics.checkNotNullParameter(str, "notifKey");
        Intrinsics.checkNotNullParameter(str2, "reason");
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logReinflationDropped$2 notificationVoiceReplyLogger$logReinflationDropped$2 = NotificationVoiceReplyLogger$logReinflationDropped$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logReinflationDropped$2);
            obtain.setStr1(str);
            obtain.setStr2(str2);
            logBuffer.push(obtain);
        }
    }

    public final void logStateNoCandidate() {
        this.eventLogger.log(VoiceReplyEvent.STATE_NO_CANDIDATE);
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logStateNoCandidate$2 notificationVoiceReplyLogger$logStateNoCandidate$2 = NotificationVoiceReplyLogger$logStateNoCandidate$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStateNoCandidate$2);
            obtain.setStr1("NoCandidate");
            logBuffer.push(obtain);
        }
    }

    private final void logStateHasCandidate(String str, CtaState ctaState) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logStateHasCandidate$2 notificationVoiceReplyLogger$logStateHasCandidate$2 = NotificationVoiceReplyLogger$logStateHasCandidate$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStateHasCandidate$2);
            obtain.setStr1(str);
            obtain.setStr2(ctaState.name());
            logBuffer.push(obtain);
        }
    }

    public final void logUserIdMismatch(int i, int i2) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logUserIdMismatch$2 notificationVoiceReplyLogger$logUserIdMismatch$2 = NotificationVoiceReplyLogger$logUserIdMismatch$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logUserIdMismatch$2);
            obtain.setInt1(i);
            obtain.setInt2(i2);
            logBuffer.push(obtain);
        }
    }

    public final void logStateInSession(String str) {
        Intrinsics.checkNotNullParameter(str, "notifKey");
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logStateInSession$2 notificationVoiceReplyLogger$logStateInSession$2 = NotificationVoiceReplyLogger$logStateInSession$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStateInSession$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }

    public final void logSessionAlreadyInProgress(int i) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logSessionAlreadyInProgress$2 notificationVoiceReplyLogger$logSessionAlreadyInProgress$2 = NotificationVoiceReplyLogger$logSessionAlreadyInProgress$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logSessionAlreadyInProgress$2);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logStartSessionNoCandidate(int i) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logStartSessionNoCandidate$2 notificationVoiceReplyLogger$logStartSessionNoCandidate$2 = NotificationVoiceReplyLogger$logStartSessionNoCandidate$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStartSessionNoCandidate$2);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logRegisterCallbacks(int i) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logRegisterCallbacks$2 notificationVoiceReplyLogger$logRegisterCallbacks$2 = NotificationVoiceReplyLogger$logRegisterCallbacks$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logRegisterCallbacks$2);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logUnregisterCallbacks(int i) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logUnregisterCallbacks$2 notificationVoiceReplyLogger$logUnregisterCallbacks$2 = NotificationVoiceReplyLogger$logUnregisterCallbacks$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logUnregisterCallbacks$2);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logStartVoiceReply(int i, int i2, String str) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logStartVoiceReply$2 notificationVoiceReplyLogger$logStartVoiceReply$2 = NotificationVoiceReplyLogger$logStartVoiceReply$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStartVoiceReply$2);
            obtain.setInt1(i);
            obtain.setInt2(i2);
            obtain.setBool1(str != null);
            logBuffer.push(obtain);
        }
    }

    public final void logVoiceAuthStateChanged(int i, int i2, int i3) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logVoiceAuthStateChanged$2 notificationVoiceReplyLogger$logVoiceAuthStateChanged$2 = NotificationVoiceReplyLogger$logVoiceAuthStateChanged$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logVoiceAuthStateChanged$2);
            obtain.setInt1(i);
            obtain.setInt2(i2);
            boolean z = true;
            if (i3 != 1) {
                z = false;
            }
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logFocus(String str, boolean z) {
        VoiceReplyEvent voiceReplyEvent;
        Intrinsics.checkNotNullParameter(str, "notifKey");
        UiEventLogger uiEventLogger = this.eventLogger;
        if (z) {
            voiceReplyEvent = VoiceReplyEvent.STATE_IN_SESSION_HAS_START_TEXT;
        } else {
            voiceReplyEvent = VoiceReplyEvent.STATE_IN_SESSION_NO_START_TEXT;
        }
        uiEventLogger.log(voiceReplyEvent);
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logFocus$2 notificationVoiceReplyLogger$logFocus$2 = NotificationVoiceReplyLogger$logFocus$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logFocus$2);
            obtain.setStr1(str);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logCandidateUserChange(int i, boolean z) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logCandidateUserChange$2 notificationVoiceReplyLogger$logCandidateUserChange$2 = NotificationVoiceReplyLogger$logCandidateUserChange$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logCandidateUserChange$2);
            obtain.setInt1(i);
            obtain.setStr1(z ? "Enabling" : "Disabling");
            logBuffer.push(obtain);
        }
    }

    public final void logRejectCandidate(String str, String str2) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(str2, "reason");
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logRejectCandidate$2 notificationVoiceReplyLogger$logRejectCandidate$2 = NotificationVoiceReplyLogger$logRejectCandidate$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logRejectCandidate$2);
            obtain.setStr1(str);
            obtain.setStr2(str2);
            logBuffer.push(obtain);
        }
    }

    public final void logNoCtaNotDozing(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.eventLogger.log(VoiceReplyEvent.STATE_NEW_CANDIDATE_NO_CTA);
        logStateHasCandidate(str, CtaState.NOT_DOZING);
    }

    public final void logNoCtaDisabled(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.eventLogger.log(VoiceReplyEvent.STATE_NEW_CANDIDATE_CTA_OFF);
        logStateHasCandidate(str, CtaState.DISABLED);
    }

    public final void logNoCtaNoHun(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.eventLogger.log(VoiceReplyEvent.STATE_NEW_CANDIDATE_NO_CTA);
        logStateHasCandidate(str, CtaState.NO_HUN_VIEW);
    }

    public final void logShowCta(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.eventLogger.log(VoiceReplyEvent.STATE_NEW_CANDIDATE_CTA_PULSE);
        logStateHasCandidate(str, CtaState.SHOWING);
    }

    public final void logMsgSentAuthBypassed(String str) {
        Intrinsics.checkNotNullParameter(str, "notifKey");
        this.eventLogger.log(VoiceReplyEvent.MSG_SEND_AUTH_BYPASSED);
        logMsgSent(str, SendType.BYPASS);
    }

    public final void logMsgSentUnlocked(String str) {
        Intrinsics.checkNotNullParameter(str, "notifKey");
        this.eventLogger.log(VoiceReplyEvent.MSG_SEND_UNLOCKED);
        logMsgSent(str, SendType.UNLOCKED);
    }

    public final void logMsgSentDelayed(String str) {
        Intrinsics.checkNotNullParameter(str, "notifKey");
        this.eventLogger.log(VoiceReplyEvent.MSG_SEND_DELAYED);
        logMsgSent(str, SendType.DELAYED);
    }

    public final void logMsgSendBounced(String str) {
        Intrinsics.checkNotNullParameter(str, "notifKey");
        this.eventLogger.log(VoiceReplyEvent.MSG_SEND_BOUNCED);
        logMsgSent(str, SendType.BOUNCED);
    }

    private final void logMsgSent(String str, SendType sendType) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logMsgSent$2 notificationVoiceReplyLogger$logMsgSent$2 = NotificationVoiceReplyLogger$logMsgSent$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logMsgSent$2);
            obtain.setStr1(str);
            obtain.setStr2(sendType.name());
            logBuffer.push(obtain);
        }
    }

    public final void logBadNotifState() {
        this.eventLogger.log(VoiceReplyEvent.SESSION_FAILED_BAD_NOTIF_STATE);
        LogBuffer logBuffer = getLogBuffer();
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logStatic$2 notificationVoiceReplyLogger$logStatic$2 = new NotificationVoiceReplyLogger$logStatic$2("Entry's \"showing layout\" is null");
        if (!logBuffer.getFrozen()) {
            logBuffer.push(logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStatic$2));
        }
    }

    public final void logBadShadeState() {
        this.eventLogger.log(VoiceReplyEvent.SESSION_FAILED_BAD_SHADE_STATE);
        LogBuffer logBuffer = getLogBuffer();
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logStatic$2 notificationVoiceReplyLogger$logStatic$2 = new NotificationVoiceReplyLogger$logStatic$2("Could not expand shade, aborting");
        if (!logBuffer.getFrozen()) {
            logBuffer.push(logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStatic$2));
        }
    }

    public final void logBadWindowState(StatusBarWindowState statusBarWindowState) {
        Intrinsics.checkNotNullParameter(statusBarWindowState, "state");
        this.eventLogger.log(VoiceReplyEvent.SESSION_FAILED_BAD_WINDOW_STATE);
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logBadWindowState$2 notificationVoiceReplyLogger$logBadWindowState$2 = NotificationVoiceReplyLogger$logBadWindowState$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logBadWindowState$2);
            obtain.setStr1(statusBarWindowState.toString());
            logBuffer.push(obtain);
        }
    }

    public final void logBadRemoteInputState() {
        Log.e("NotifVoiceReply", "Could not activate remote input for voice reply");
        this.eventLogger.log(VoiceReplyEvent.SESSION_FAILED_BAD_REMOTE_INPUT_STATE);
    }

    public final void logSessionEnd() {
        this.eventLogger.log(VoiceReplyEvent.SESSION_ENDED);
        LogBuffer logBuffer = getLogBuffer();
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logStatic$2 notificationVoiceReplyLogger$logStatic$2 = new NotificationVoiceReplyLogger$logStatic$2("Session has ended");
        if (!logBuffer.getFrozen()) {
            logBuffer.push(logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStatic$2));
        }
    }

    public final void logSetFeatureEnabled(int i, int i2) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logSetFeatureEnabled$2 notificationVoiceReplyLogger$logSetFeatureEnabled$2 = NotificationVoiceReplyLogger$logSetFeatureEnabled$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logSetFeatureEnabled$2);
            obtain.setInt1(i);
            obtain.setInt2(i2);
            logBuffer.push(obtain);
        }
    }

    /* compiled from: NotificationVoiceReplyLogger.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
