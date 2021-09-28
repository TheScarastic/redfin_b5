package com.android.systemui.statusbar.notification.collection;

import android.app.Notification;
import android.service.notification.StatusBarNotification;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationRankingManager.kt */
/* loaded from: classes.dex */
public final class NotificationRankingManagerKt {
    /* access modifiers changed from: private */
    public static final boolean isSystemMax(NotificationEntry notificationEntry) {
        if (notificationEntry.getImportance() >= 4) {
            StatusBarNotification sbn = notificationEntry.getSbn();
            Intrinsics.checkNotNullExpressionValue(sbn, "sbn");
            if (isSystemNotification(sbn)) {
                return true;
            }
        }
        return false;
    }

    private static final boolean isSystemNotification(StatusBarNotification statusBarNotification) {
        return Intrinsics.areEqual("android", statusBarNotification.getPackageName()) || Intrinsics.areEqual("com.android.systemui", statusBarNotification.getPackageName());
    }

    /* access modifiers changed from: private */
    public static final boolean isImportantCall(NotificationEntry notificationEntry) {
        return notificationEntry.getSbn().getNotification().isStyle(Notification.CallStyle.class) && notificationEntry.getImportance() > 1;
    }

    /* access modifiers changed from: private */
    public static final boolean isColorizedForegroundService(NotificationEntry notificationEntry) {
        Notification notification = notificationEntry.getSbn().getNotification();
        return notification.isForegroundService() && notification.isColorized() && notificationEntry.getImportance() > 1;
    }
}
