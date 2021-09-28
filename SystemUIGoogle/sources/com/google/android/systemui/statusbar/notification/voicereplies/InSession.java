package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes2.dex */
final class InSession extends VoiceReplyState {
    private final Function2<Session, Continuation<? super Unit>, Object> block;
    private final String initialContent;
    private final VoiceReplyTarget target;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof InSession)) {
            return false;
        }
        InSession inSession = (InSession) obj;
        return Intrinsics.areEqual(this.initialContent, inSession.initialContent) && Intrinsics.areEqual(this.block, inSession.block) && Intrinsics.areEqual(this.target, inSession.target);
    }

    public int hashCode() {
        String str = this.initialContent;
        return ((((str == null ? 0 : str.hashCode()) * 31) + this.block.hashCode()) * 31) + this.target.hashCode();
    }

    public String toString() {
        return "InSession(initialContent=" + ((Object) this.initialContent) + ", block=" + this.block + ", target=" + this.target + ')';
    }

    public final String getInitialContent() {
        return this.initialContent;
    }

    public final Function2<Session, Continuation<? super Unit>, Object> getBlock() {
        return this.block;
    }

    public final VoiceReplyTarget getTarget() {
        return this.target;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: kotlin.jvm.functions.Function2<? super com.google.android.systemui.statusbar.notification.voicereplies.Session, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public InSession(String str, Function2<? super Session, ? super Continuation<? super Unit>, ? extends Object> function2, VoiceReplyTarget voiceReplyTarget) {
        super(null);
        Intrinsics.checkNotNullParameter(function2, "block");
        Intrinsics.checkNotNullParameter(voiceReplyTarget, "target");
        this.initialContent = str;
        this.block = function2;
        this.target = voiceReplyTarget;
    }
}
