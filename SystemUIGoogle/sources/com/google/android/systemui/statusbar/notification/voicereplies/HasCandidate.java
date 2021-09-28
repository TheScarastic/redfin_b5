package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes2.dex */
final class HasCandidate extends VoiceReplyState {
    private final VoiceReplyTarget candidate;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof HasCandidate) && Intrinsics.areEqual(this.candidate, ((HasCandidate) obj).candidate);
    }

    public int hashCode() {
        return this.candidate.hashCode();
    }

    public String toString() {
        return "HasCandidate(candidate=" + this.candidate + ')';
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public HasCandidate(VoiceReplyTarget voiceReplyTarget) {
        super(null);
        Intrinsics.checkNotNullParameter(voiceReplyTarget, "candidate");
        this.candidate = voiceReplyTarget;
    }

    public final VoiceReplyTarget getCandidate() {
        return this.candidate;
    }
}
