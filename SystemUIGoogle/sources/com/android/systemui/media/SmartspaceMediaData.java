package com.android.systemui.media;

import android.app.smartspace.SmartspaceAction;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SmartspaceMediaData.kt */
/* loaded from: classes.dex */
public final class SmartspaceMediaData {
    private final int backgroundColor;
    private final SmartspaceAction cardAction;
    private final boolean isActive;
    private final boolean isValid;
    private final String packageName;
    private final List<SmartspaceAction> recommendations;
    private final String targetId;

    /* JADX DEBUG: Multi-variable search result rejected for r5v0, resolved type: com.android.systemui.media.SmartspaceMediaData */
    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ SmartspaceMediaData copy$default(SmartspaceMediaData smartspaceMediaData, String str, boolean z, boolean z2, String str2, SmartspaceAction smartspaceAction, List list, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = smartspaceMediaData.targetId;
        }
        if ((i2 & 2) != 0) {
            z = smartspaceMediaData.isActive;
        }
        if ((i2 & 4) != 0) {
            z2 = smartspaceMediaData.isValid;
        }
        if ((i2 & 8) != 0) {
            str2 = smartspaceMediaData.packageName;
        }
        if ((i2 & 16) != 0) {
            smartspaceAction = smartspaceMediaData.cardAction;
        }
        if ((i2 & 32) != 0) {
            list = smartspaceMediaData.recommendations;
        }
        if ((i2 & 64) != 0) {
            i = smartspaceMediaData.backgroundColor;
        }
        return smartspaceMediaData.copy(str, z, z2, str2, smartspaceAction, list, i);
    }

    public final SmartspaceMediaData copy(String str, boolean z, boolean z2, String str2, SmartspaceAction smartspaceAction, List<SmartspaceAction> list, int i) {
        Intrinsics.checkNotNullParameter(str, "targetId");
        Intrinsics.checkNotNullParameter(str2, "packageName");
        Intrinsics.checkNotNullParameter(list, "recommendations");
        return new SmartspaceMediaData(str, z, z2, str2, smartspaceAction, list, i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SmartspaceMediaData)) {
            return false;
        }
        SmartspaceMediaData smartspaceMediaData = (SmartspaceMediaData) obj;
        return Intrinsics.areEqual(this.targetId, smartspaceMediaData.targetId) && this.isActive == smartspaceMediaData.isActive && this.isValid == smartspaceMediaData.isValid && Intrinsics.areEqual(this.packageName, smartspaceMediaData.packageName) && Intrinsics.areEqual(this.cardAction, smartspaceMediaData.cardAction) && Intrinsics.areEqual(this.recommendations, smartspaceMediaData.recommendations) && this.backgroundColor == smartspaceMediaData.backgroundColor;
    }

    public int hashCode() {
        int hashCode = this.targetId.hashCode() * 31;
        boolean z = this.isActive;
        int i = 1;
        if (z) {
            z = true;
        }
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        int i4 = z ? 1 : 0;
        int i5 = (hashCode + i2) * 31;
        boolean z2 = this.isValid;
        if (!z2) {
            i = z2 ? 1 : 0;
        }
        int hashCode2 = (((i5 + i) * 31) + this.packageName.hashCode()) * 31;
        SmartspaceAction smartspaceAction = this.cardAction;
        return ((((hashCode2 + (smartspaceAction == null ? 0 : smartspaceAction.hashCode())) * 31) + this.recommendations.hashCode()) * 31) + Integer.hashCode(this.backgroundColor);
    }

    public String toString() {
        return "SmartspaceMediaData(targetId=" + this.targetId + ", isActive=" + this.isActive + ", isValid=" + this.isValid + ", packageName=" + this.packageName + ", cardAction=" + this.cardAction + ", recommendations=" + this.recommendations + ", backgroundColor=" + this.backgroundColor + ')';
    }

    public SmartspaceMediaData(String str, boolean z, boolean z2, String str2, SmartspaceAction smartspaceAction, List<SmartspaceAction> list, int i) {
        Intrinsics.checkNotNullParameter(str, "targetId");
        Intrinsics.checkNotNullParameter(str2, "packageName");
        Intrinsics.checkNotNullParameter(list, "recommendations");
        this.targetId = str;
        this.isActive = z;
        this.isValid = z2;
        this.packageName = str2;
        this.cardAction = smartspaceAction;
        this.recommendations = list;
        this.backgroundColor = i;
    }

    public final String getTargetId() {
        return this.targetId;
    }

    public final boolean isActive() {
        return this.isActive;
    }

    public final boolean isValid() {
        return this.isValid;
    }

    public final String getPackageName() {
        return this.packageName;
    }

    public final SmartspaceAction getCardAction() {
        return this.cardAction;
    }

    public final List<SmartspaceAction> getRecommendations() {
        return this.recommendations;
    }

    public final int getBackgroundColor() {
        return this.backgroundColor;
    }
}
