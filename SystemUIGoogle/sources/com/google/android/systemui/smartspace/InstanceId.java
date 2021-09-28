package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceTarget;
/* loaded from: classes2.dex */
public class InstanceId {
    public static Integer create(SmartspaceTarget smartspaceTarget) {
        String smartspaceTargetId = smartspaceTarget.getSmartspaceTargetId();
        if (smartspaceTargetId == null || smartspaceTargetId.isEmpty()) {
            return Integer.valueOf(SmallHash.hash(String.valueOf(smartspaceTarget.getCreationTimeMillis())));
        }
        return Integer.valueOf(SmallHash.hash(smartspaceTargetId));
    }
}
