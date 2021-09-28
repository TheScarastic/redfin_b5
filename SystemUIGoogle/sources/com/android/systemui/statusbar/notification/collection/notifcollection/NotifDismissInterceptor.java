package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public interface NotifDismissInterceptor {

    /* loaded from: classes.dex */
    public interface OnEndDismissInterception {
        void onEndDismissInterception(NotifDismissInterceptor notifDismissInterceptor, NotificationEntry notificationEntry, DismissedByUserStats dismissedByUserStats);
    }

    void cancelDismissInterception(NotificationEntry notificationEntry);

    String getName();

    void setCallback(OnEndDismissInterception onEndDismissInterception);

    boolean shouldInterceptDismissal(NotificationEntry notificationEntry);
}
