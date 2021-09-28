package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationVoiceReplyManager.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class StartSessionRequest {
    private final Function2<Session, Continuation<? super Unit>, Object> block;
    private final NotificationVoiceReplyHandler handler;
    private final String initialContent;
    private final Function0<Unit> onFail;
    private final int token;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StartSessionRequest)) {
            return false;
        }
        StartSessionRequest startSessionRequest = (StartSessionRequest) obj;
        return Intrinsics.areEqual(this.handler, startSessionRequest.handler) && this.token == startSessionRequest.token && Intrinsics.areEqual(this.initialContent, startSessionRequest.initialContent) && Intrinsics.areEqual(this.onFail, startSessionRequest.onFail) && Intrinsics.areEqual(this.block, startSessionRequest.block);
    }

    public int hashCode() {
        int hashCode = ((this.handler.hashCode() * 31) + Integer.hashCode(this.token)) * 31;
        String str = this.initialContent;
        return ((((hashCode + (str == null ? 0 : str.hashCode())) * 31) + this.onFail.hashCode()) * 31) + this.block.hashCode();
    }

    public String toString() {
        return "StartSessionRequest(handler=" + this.handler + ", token=" + this.token + ", initialContent=" + ((Object) this.initialContent) + ", onFail=" + this.onFail + ", block=" + this.block + ')';
    }

    /* JADX DEBUG: Multi-variable search result rejected for r6v0, resolved type: kotlin.jvm.functions.Function2<? super com.google.android.systemui.statusbar.notification.voicereplies.Session, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> */
    /* JADX WARN: Multi-variable type inference failed */
    public StartSessionRequest(NotificationVoiceReplyHandler notificationVoiceReplyHandler, int i, String str, Function0<Unit> function0, Function2<? super Session, ? super Continuation<? super Unit>, ? extends Object> function2) {
        Intrinsics.checkNotNullParameter(notificationVoiceReplyHandler, "handler");
        Intrinsics.checkNotNullParameter(function0, "onFail");
        Intrinsics.checkNotNullParameter(function2, "block");
        this.handler = notificationVoiceReplyHandler;
        this.token = i;
        this.initialContent = str;
        this.onFail = function0;
        this.block = function2;
    }

    public final NotificationVoiceReplyHandler getHandler() {
        return this.handler;
    }

    public final String getInitialContent() {
        return this.initialContent;
    }

    public final Function0<Unit> getOnFail() {
        return this.onFail;
    }

    public final Function2<Session, Continuation<? super Unit>, Object> getBlock() {
        return this.block;
    }
}
