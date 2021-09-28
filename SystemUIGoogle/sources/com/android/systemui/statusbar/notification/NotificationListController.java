package com.android.systemui.statusbar.notification;

import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import java.util.Objects;
/* loaded from: classes.dex */
public class NotificationListController {
    private final DeviceProvisionedController mDeviceProvisionedController;
    private final NotificationEntryManager mEntryManager;
    private final NotificationListContainer mListContainer;
    private final NotificationEntryListener mEntryListener = new NotificationEntryListener() { // from class: com.android.systemui.statusbar.notification.NotificationListController.1
        @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
        public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
            NotificationListController.this.mListContainer.cleanUpViewStateForEntry(notificationEntry);
        }
    };
    private final DeviceProvisionedController.DeviceProvisionedListener mDeviceProvisionedListener = new DeviceProvisionedController.DeviceProvisionedListener() { // from class: com.android.systemui.statusbar.notification.NotificationListController.2
        @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
        public void onDeviceProvisionedChanged() {
            NotificationListController.this.mEntryManager.updateNotifications("device provisioned changed");
        }
    };

    public NotificationListController(NotificationEntryManager notificationEntryManager, NotificationListContainer notificationListContainer, DeviceProvisionedController deviceProvisionedController) {
        Objects.requireNonNull(notificationEntryManager);
        this.mEntryManager = notificationEntryManager;
        Objects.requireNonNull(notificationListContainer);
        this.mListContainer = notificationListContainer;
        Objects.requireNonNull(deviceProvisionedController);
        this.mDeviceProvisionedController = deviceProvisionedController;
    }

    public void bind() {
        this.mEntryManager.addNotificationEntryListener(this.mEntryListener);
        this.mDeviceProvisionedController.addCallback(this.mDeviceProvisionedListener);
    }
}
