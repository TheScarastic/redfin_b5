package com.android.systemui.statusbar.notification.init;

import android.service.notification.StatusBarNotification;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wm.shell.bubbles.Bubbles;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Optional;
/* compiled from: NotificationsController.kt */
/* loaded from: classes.dex */
public interface NotificationsController {
    void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z);

    int getActiveNotificationsCount();

    void initialize(StatusBar statusBar, Optional<Bubbles> optional, NotificationPresenter notificationPresenter, NotificationListContainer notificationListContainer, NotificationActivityStarter notificationActivityStarter, NotificationRowBinderImpl.BindRowCallback bindRowCallback);

    void requestNotificationUpdate(String str);

    void resetUserExpandedStates();

    void setNotificationSnoozed(StatusBarNotification statusBarNotification, NotificationSwipeActionHelper.SnoozeOption snoozeOption);
}
