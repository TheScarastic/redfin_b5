package com.google.android.systemui.statusbar.notification.voicereplies;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes2.dex */
public final class StatusBarWindowState {
    private final boolean bouncerShowing;
    private final boolean keyguardOccluded;
    private final boolean keyguardShowing;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StatusBarWindowState)) {
            return false;
        }
        StatusBarWindowState statusBarWindowState = (StatusBarWindowState) obj;
        return this.keyguardShowing == statusBarWindowState.keyguardShowing && this.keyguardOccluded == statusBarWindowState.keyguardOccluded && this.bouncerShowing == statusBarWindowState.bouncerShowing;
    }

    public int hashCode() {
        boolean z = this.keyguardShowing;
        int i = 1;
        if (z) {
            z = true;
        }
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        int i4 = z ? 1 : 0;
        int i5 = i2 * 31;
        boolean z2 = this.keyguardOccluded;
        if (z2) {
            z2 = true;
        }
        int i6 = z2 ? 1 : 0;
        int i7 = z2 ? 1 : 0;
        int i8 = z2 ? 1 : 0;
        int i9 = (i5 + i6) * 31;
        boolean z3 = this.bouncerShowing;
        if (!z3) {
            i = z3 ? 1 : 0;
        }
        return i9 + i;
    }

    public String toString() {
        return "StatusBarWindowState(keyguardShowing=" + this.keyguardShowing + ", keyguardOccluded=" + this.keyguardOccluded + ", bouncerShowing=" + this.bouncerShowing + ')';
    }

    public StatusBarWindowState(boolean z, boolean z2, boolean z3) {
        this.keyguardShowing = z;
        this.keyguardOccluded = z2;
        this.bouncerShowing = z3;
    }

    public final boolean getKeyguardShowing() {
        return this.keyguardShowing;
    }

    public final boolean getKeyguardOccluded() {
        return this.keyguardOccluded;
    }

    public final boolean getBouncerShowing() {
        return this.bouncerShowing;
    }
}
