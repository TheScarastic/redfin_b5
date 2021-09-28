package com.android.systemui.statusbar.notification.row.dagger;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.StatusBar;
/* loaded from: classes.dex */
public interface ExpandableNotificationRowComponent {

    /* loaded from: classes.dex */
    public interface Builder {
        ExpandableNotificationRowComponent build();

        Builder expandableNotificationRow(ExpandableNotificationRow expandableNotificationRow);

        Builder listContainer(NotificationListContainer notificationListContainer);

        Builder notificationEntry(NotificationEntry notificationEntry);

        Builder onExpandClickListener(ExpandableNotificationRow.OnExpandClickListener onExpandClickListener);
    }

    ExpandableNotificationRowController getExpandableNotificationRowController();

    /* loaded from: classes.dex */
    public static abstract class ExpandableNotificationRowModule {
        /* access modifiers changed from: package-private */
        public static StatusBarNotification provideStatusBarNotification(NotificationEntry notificationEntry) {
            return notificationEntry.getSbn();
        }

        /* access modifiers changed from: package-private */
        public static String provideNotificationKey(StatusBarNotification statusBarNotification) {
            return statusBarNotification.getKey();
        }

        /* access modifiers changed from: package-private */
        public static String provideAppName(Context context, StatusBarNotification statusBarNotification) {
            PackageManager packageManagerForUser = StatusBar.getPackageManagerForUser(context, statusBarNotification.getUser().getIdentifier());
            String packageName = statusBarNotification.getPackageName();
            try {
                ApplicationInfo applicationInfo = packageManagerForUser.getApplicationInfo(packageName, 8704);
                if (applicationInfo != null) {
                    return String.valueOf(packageManagerForUser.getApplicationLabel(applicationInfo));
                }
            } catch (PackageManager.NameNotFoundException unused) {
            }
            return packageName;
        }
    }
}
