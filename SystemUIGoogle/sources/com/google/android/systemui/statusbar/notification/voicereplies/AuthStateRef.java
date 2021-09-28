package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.jvm.internal.DefaultConstructorMarker;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes2.dex */
final class AuthStateRef {
    private int value;

    public AuthStateRef() {
        this(0, 1, null);
    }

    public AuthStateRef(int i) {
        this.value = i;
    }

    public /* synthetic */ AuthStateRef(int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? 0 : i);
    }

    public final int getValue() {
        return this.value;
    }

    public final void setValue(int i) {
        this.value = i;
    }
}
