package com.google.android.systemui.statusbar;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationVoiceReplyManagerService.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class StartVoiceReplyData {
    private final int sessionToken;
    private final int userId;
    private final String userMessageContent;

    public final int component2() {
        return this.sessionToken;
    }

    public final String component3() {
        return this.userMessageContent;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StartVoiceReplyData)) {
            return false;
        }
        StartVoiceReplyData startVoiceReplyData = (StartVoiceReplyData) obj;
        return this.userId == startVoiceReplyData.userId && this.sessionToken == startVoiceReplyData.sessionToken && Intrinsics.areEqual(this.userMessageContent, startVoiceReplyData.userMessageContent);
    }

    public int hashCode() {
        int hashCode = ((Integer.hashCode(this.userId) * 31) + Integer.hashCode(this.sessionToken)) * 31;
        String str = this.userMessageContent;
        return hashCode + (str == null ? 0 : str.hashCode());
    }

    public String toString() {
        return "StartVoiceReplyData(userId=" + this.userId + ", sessionToken=" + this.sessionToken + ", userMessageContent=" + ((Object) this.userMessageContent) + ')';
    }

    public StartVoiceReplyData(int i, int i2, String str) {
        this.userId = i;
        this.sessionToken = i2;
        this.userMessageContent = str;
    }

    public final int getUserId() {
        return this.userId;
    }
}
