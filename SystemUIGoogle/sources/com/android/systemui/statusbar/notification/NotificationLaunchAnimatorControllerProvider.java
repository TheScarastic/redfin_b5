package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationLaunchAnimatorController.kt */
/* loaded from: classes.dex */
public final class NotificationLaunchAnimatorControllerProvider {
    private final HeadsUpManagerPhone headsUpManager;
    private final NotificationListContainer notificationListContainer;
    private final NotificationShadeWindowViewController notificationShadeWindowViewController;

    public NotificationLaunchAnimatorControllerProvider(NotificationShadeWindowViewController notificationShadeWindowViewController, NotificationListContainer notificationListContainer, HeadsUpManagerPhone headsUpManagerPhone) {
        Intrinsics.checkNotNullParameter(notificationShadeWindowViewController, "notificationShadeWindowViewController");
        Intrinsics.checkNotNullParameter(notificationListContainer, "notificationListContainer");
        Intrinsics.checkNotNullParameter(headsUpManagerPhone, "headsUpManager");
        this.notificationShadeWindowViewController = notificationShadeWindowViewController;
        this.notificationListContainer = notificationListContainer;
        this.headsUpManager = headsUpManagerPhone;
    }

    public final NotificationLaunchAnimatorController getAnimatorController(ExpandableNotificationRow expandableNotificationRow) {
        Intrinsics.checkNotNullParameter(expandableNotificationRow, "notification");
        return new NotificationLaunchAnimatorController(this.notificationShadeWindowViewController, this.notificationListContainer, this.headsUpManager, expandableNotificationRow);
    }
}
