package com.google.android.systemui.statusbar;
/* compiled from: NotificationVoiceReplyManagerService.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class OnVoiceAuthStateChangedData {
    private final int newState;
    private final int sessionToken;
    private final int userId;

    public final int component3() {
        return this.newState;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OnVoiceAuthStateChangedData)) {
            return false;
        }
        OnVoiceAuthStateChangedData onVoiceAuthStateChangedData = (OnVoiceAuthStateChangedData) obj;
        return this.userId == onVoiceAuthStateChangedData.userId && this.sessionToken == onVoiceAuthStateChangedData.sessionToken && this.newState == onVoiceAuthStateChangedData.newState;
    }

    public int hashCode() {
        return (((Integer.hashCode(this.userId) * 31) + Integer.hashCode(this.sessionToken)) * 31) + Integer.hashCode(this.newState);
    }

    public String toString() {
        return "OnVoiceAuthStateChangedData(userId=" + this.userId + ", sessionToken=" + this.sessionToken + ", newState=" + this.newState + ')';
    }

    public OnVoiceAuthStateChangedData(int i, int i2, int i3) {
        this.userId = i;
        this.sessionToken = i2;
        this.newState = i3;
    }

    public final int getUserId() {
        return this.userId;
    }

    public final int getSessionToken() {
        return this.sessionToken;
    }
}
