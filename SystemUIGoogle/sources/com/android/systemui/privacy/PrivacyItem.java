package com.android.systemui.privacy;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: PrivacyItem.kt */
/* loaded from: classes.dex */
public final class PrivacyItem {
    private final PrivacyApplication application;
    private final String log;
    private final boolean paused;
    private final PrivacyType privacyType;
    private final long timeStampElapsed;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PrivacyItem)) {
            return false;
        }
        PrivacyItem privacyItem = (PrivacyItem) obj;
        return this.privacyType == privacyItem.privacyType && Intrinsics.areEqual(this.application, privacyItem.application) && this.timeStampElapsed == privacyItem.timeStampElapsed && this.paused == privacyItem.paused;
    }

    public int hashCode() {
        int hashCode = ((((this.privacyType.hashCode() * 31) + this.application.hashCode()) * 31) + Long.hashCode(this.timeStampElapsed)) * 31;
        boolean z = this.paused;
        if (z) {
            z = true;
        }
        int i = z ? 1 : 0;
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        return hashCode + i;
    }

    public String toString() {
        return "PrivacyItem(privacyType=" + this.privacyType + ", application=" + this.application + ", timeStampElapsed=" + this.timeStampElapsed + ", paused=" + this.paused + ')';
    }

    public PrivacyItem(PrivacyType privacyType, PrivacyApplication privacyApplication, long j, boolean z) {
        Intrinsics.checkNotNullParameter(privacyType, "privacyType");
        Intrinsics.checkNotNullParameter(privacyApplication, "application");
        this.privacyType = privacyType;
        this.application = privacyApplication;
        this.timeStampElapsed = j;
        this.paused = z;
        this.log = '(' + privacyType.getLogName() + ", " + privacyApplication.getPackageName() + '(' + privacyApplication.getUid() + "), " + j + ", paused=" + z + ')';
    }

    public final PrivacyType getPrivacyType() {
        return this.privacyType;
    }

    public final PrivacyApplication getApplication() {
        return this.application;
    }

    public final long getTimeStampElapsed() {
        return this.timeStampElapsed;
    }

    public final boolean getPaused() {
        return this.paused;
    }

    public final String getLog() {
        return this.log;
    }
}
