package com.android.systemui.privacy;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: PrivacyItem.kt */
/* loaded from: classes.dex */
public final class PrivacyApplication {
    private final String packageName;
    private final int uid;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PrivacyApplication)) {
            return false;
        }
        PrivacyApplication privacyApplication = (PrivacyApplication) obj;
        return Intrinsics.areEqual(this.packageName, privacyApplication.packageName) && this.uid == privacyApplication.uid;
    }

    public int hashCode() {
        return (this.packageName.hashCode() * 31) + Integer.hashCode(this.uid);
    }

    public String toString() {
        return "PrivacyApplication(packageName=" + this.packageName + ", uid=" + this.uid + ')';
    }

    public PrivacyApplication(String str, int i) {
        Intrinsics.checkNotNullParameter(str, "packageName");
        this.packageName = str;
        this.uid = i;
    }

    public final String getPackageName() {
        return this.packageName;
    }

    public final int getUid() {
        return this.uid;
    }
}
