package com.android.systemui.statusbar.phone;

import android.service.notification.StatusBarNotification;
import com.android.systemui.Dependency;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
/* loaded from: classes.dex */
public class KeyguardEnvironmentImpl implements NotificationEntryManager.KeyguardEnvironment {
    private final NotificationLockscreenUserManager mLockscreenUserManager = (NotificationLockscreenUserManager) Dependency.get(NotificationLockscreenUserManager.class);
    private final DeviceProvisionedController mDeviceProvisionedController = (DeviceProvisionedController) Dependency.get(DeviceProvisionedController.class);

    @Override // com.android.systemui.statusbar.notification.NotificationEntryManager.KeyguardEnvironment
    public boolean isDeviceProvisioned() {
        return this.mDeviceProvisionedController.isDeviceProvisioned();
    }

    @Override // com.android.systemui.statusbar.notification.NotificationEntryManager.KeyguardEnvironment
    public boolean isNotificationForCurrentProfiles(StatusBarNotification statusBarNotification) {
        return this.mLockscreenUserManager.isCurrentProfile(statusBarNotification.getUserId());
    }
}
