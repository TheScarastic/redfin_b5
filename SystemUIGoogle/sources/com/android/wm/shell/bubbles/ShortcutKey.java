package com.android.wm.shell.bubbles;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: BubbleDataRepository.kt */
/* loaded from: classes2.dex */
public final class ShortcutKey {
    private final String pkg;
    private final int userId;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ShortcutKey)) {
            return false;
        }
        ShortcutKey shortcutKey = (ShortcutKey) obj;
        return this.userId == shortcutKey.userId && Intrinsics.areEqual(this.pkg, shortcutKey.pkg);
    }

    public int hashCode() {
        return (Integer.hashCode(this.userId) * 31) + this.pkg.hashCode();
    }

    public String toString() {
        return "ShortcutKey(userId=" + this.userId + ", pkg=" + this.pkg + ')';
    }

    public ShortcutKey(int i, String str) {
        Intrinsics.checkNotNullParameter(str, "pkg");
        this.userId = i;
        this.pkg = str;
    }

    public final String getPkg() {
        return this.pkg;
    }

    public final int getUserId() {
        return this.userId;
    }
}
