package com.android.systemui.shared.system;

import android.app.AppGlobals;
import android.app.admin.DevicePolicyManager;
/* loaded from: classes.dex */
public class DevicePolicyManagerWrapper {
    private static final DevicePolicyManagerWrapper sInstance = new DevicePolicyManagerWrapper();
    private static final DevicePolicyManager sDevicePolicyManager = (DevicePolicyManager) AppGlobals.getInitialApplication().getSystemService(DevicePolicyManager.class);

    private DevicePolicyManagerWrapper() {
    }

    public static DevicePolicyManagerWrapper getInstance() {
        return sInstance;
    }

    public boolean isLockTaskPermitted(String str) {
        return sDevicePolicyManager.isLockTaskPermitted(str);
    }
}
