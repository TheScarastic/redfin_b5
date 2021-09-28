package com.android.wm.shell.bubbles.storage;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: BubbleEntity.kt */
/* loaded from: classes2.dex */
public final class BubbleEntity {
    private final int desiredHeight;
    private final int desiredHeightResId;
    private final String key;
    private final String locus;
    private final String packageName;
    private final String shortcutId;
    private final int taskId;
    private final String title;
    private final int userId;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BubbleEntity)) {
            return false;
        }
        BubbleEntity bubbleEntity = (BubbleEntity) obj;
        return this.userId == bubbleEntity.userId && Intrinsics.areEqual(this.packageName, bubbleEntity.packageName) && Intrinsics.areEqual(this.shortcutId, bubbleEntity.shortcutId) && Intrinsics.areEqual(this.key, bubbleEntity.key) && this.desiredHeight == bubbleEntity.desiredHeight && this.desiredHeightResId == bubbleEntity.desiredHeightResId && Intrinsics.areEqual(this.title, bubbleEntity.title) && this.taskId == bubbleEntity.taskId && Intrinsics.areEqual(this.locus, bubbleEntity.locus);
    }

    public int hashCode() {
        int hashCode = ((((((((((Integer.hashCode(this.userId) * 31) + this.packageName.hashCode()) * 31) + this.shortcutId.hashCode()) * 31) + this.key.hashCode()) * 31) + Integer.hashCode(this.desiredHeight)) * 31) + Integer.hashCode(this.desiredHeightResId)) * 31;
        String str = this.title;
        int i = 0;
        int hashCode2 = (((hashCode + (str == null ? 0 : str.hashCode())) * 31) + Integer.hashCode(this.taskId)) * 31;
        String str2 = this.locus;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return hashCode2 + i;
    }

    public String toString() {
        return "BubbleEntity(userId=" + this.userId + ", packageName=" + this.packageName + ", shortcutId=" + this.shortcutId + ", key=" + this.key + ", desiredHeight=" + this.desiredHeight + ", desiredHeightResId=" + this.desiredHeightResId + ", title=" + ((Object) this.title) + ", taskId=" + this.taskId + ", locus=" + ((Object) this.locus) + ')';
    }

    public BubbleEntity(int i, String str, String str2, String str3, int i2, int i3, String str4, int i4, String str5) {
        Intrinsics.checkNotNullParameter(str, "packageName");
        Intrinsics.checkNotNullParameter(str2, "shortcutId");
        Intrinsics.checkNotNullParameter(str3, "key");
        this.userId = i;
        this.packageName = str;
        this.shortcutId = str2;
        this.key = str3;
        this.desiredHeight = i2;
        this.desiredHeightResId = i3;
        this.title = str4;
        this.taskId = i4;
        this.locus = str5;
    }

    public final int getUserId() {
        return this.userId;
    }

    public final String getPackageName() {
        return this.packageName;
    }

    public final String getShortcutId() {
        return this.shortcutId;
    }

    public final String getKey() {
        return this.key;
    }

    public final int getDesiredHeight() {
        return this.desiredHeight;
    }

    public final int getDesiredHeightResId() {
        return this.desiredHeightResId;
    }

    public final String getTitle() {
        return this.title;
    }

    public final int getTaskId() {
        return this.taskId;
    }

    public final String getLocus() {
        return this.locus;
    }
}
