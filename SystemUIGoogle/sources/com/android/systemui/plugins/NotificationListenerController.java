package com.android.systemui.plugins;

import android.app.NotificationChannel;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.android.systemui.plugins.annotations.DependsOn;
import com.android.systemui.plugins.annotations.ProvidesInterface;
@ProvidesInterface(action = NotificationListenerController.ACTION, version = 1)
@DependsOn(target = NotificationProvider.class)
/* loaded from: classes.dex */
public interface NotificationListenerController extends Plugin {
    public static final String ACTION = "com.android.systemui.action.PLUGIN_NOTIFICATION_ASSISTANT";
    public static final int VERSION = 1;

    @ProvidesInterface(version = 1)
    /* loaded from: classes.dex */
    public interface NotificationProvider {
        public static final int VERSION = 1;

        void addNotification(StatusBarNotification statusBarNotification);

        StatusBarNotification[] getActiveNotifications();

        NotificationListenerService.RankingMap getRankingMap();

        void removeNotification(StatusBarNotification statusBarNotification);

        void updateRanking();
    }

    default StatusBarNotification[] getActiveNotifications(StatusBarNotification[] statusBarNotificationArr) {
        return statusBarNotificationArr;
    }

    default NotificationListenerService.RankingMap getCurrentRanking(NotificationListenerService.RankingMap rankingMap) {
        return rankingMap;
    }

    void onListenerConnected(NotificationProvider notificationProvider);

    default boolean onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        return false;
    }

    default boolean onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        return false;
    }

    default boolean onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        return false;
    }
}
