package com.android.systemui.statusbar.tv.notifications;

import android.app.Notification;
import android.content.Context;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.util.SparseArray;
import com.android.systemui.SystemUI;
import com.android.systemui.statusbar.NotificationListener;
/* loaded from: classes.dex */
public class TvNotificationHandler extends SystemUI implements NotificationListener.NotificationHandler {
    private final NotificationListener mNotificationListener;
    private final SparseArray<StatusBarNotification> mNotifications = new SparseArray<>();
    private Listener mUpdateListener;

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Listener {
        void notificationsUpdated(SparseArray<StatusBarNotification> sparseArray);
    }

    @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
    public void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
    }

    @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
    public void onNotificationsInitialized() {
    }

    public TvNotificationHandler(Context context, NotificationListener notificationListener) {
        super(context);
        this.mNotificationListener = notificationListener;
    }

    public SparseArray<StatusBarNotification> getCurrentNotifications() {
        return this.mNotifications;
    }

    public void setTvNotificationListener(Listener listener) {
        this.mUpdateListener = listener;
    }

    @Override // com.android.systemui.SystemUI
    public void start() {
        this.mNotificationListener.addNotificationHandler(this);
        this.mNotificationListener.registerAsSystemService();
    }

    @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
    public void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        if (!new Notification.TvExtender(statusBarNotification.getNotification()).isAvailableOnTv()) {
            Log.v("TvNotificationHandler", "Notification not added because it isn't relevant for tv");
            return;
        }
        this.mNotifications.put(statusBarNotification.getId(), statusBarNotification);
        Listener listener = this.mUpdateListener;
        if (listener != null) {
            listener.notificationsUpdated(this.mNotifications);
        }
        Log.d("TvNotificationHandler", "Notification added");
    }

    public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        if (this.mNotifications.contains(statusBarNotification.getId())) {
            this.mNotifications.remove(statusBarNotification.getId());
            Log.d("TvNotificationHandler", "Notification removed");
            Listener listener = this.mUpdateListener;
            if (listener != null) {
                listener.notificationsUpdated(this.mNotifications);
            }
        }
    }

    @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
    public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
        onNotificationRemoved(statusBarNotification, rankingMap);
    }
}
