package com.android.systemui.statusbar.notification.init;

import android.service.notification.StatusBarNotification;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wm.shell.bubbles.Bubbles;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Optional;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationsControllerStub.kt */
/* loaded from: classes.dex */
public final class NotificationsControllerStub implements NotificationsController {
    private final NotificationListener notificationListener;

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public int getActiveNotificationsCount() {
        return 0;
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public void requestNotificationUpdate(String str) {
        Intrinsics.checkNotNullParameter(str, "reason");
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public void resetUserExpandedStates() {
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public void setNotificationSnoozed(StatusBarNotification statusBarNotification, NotificationSwipeActionHelper.SnoozeOption snoozeOption) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        Intrinsics.checkNotNullParameter(snoozeOption, "snoozeOption");
    }

    public NotificationsControllerStub(NotificationListener notificationListener) {
        Intrinsics.checkNotNullParameter(notificationListener, "notificationListener");
        this.notificationListener = notificationListener;
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public void initialize(StatusBar statusBar, Optional<Bubbles> optional, NotificationPresenter notificationPresenter, NotificationListContainer notificationListContainer, NotificationActivityStarter notificationActivityStarter, NotificationRowBinderImpl.BindRowCallback bindRowCallback) {
        Intrinsics.checkNotNullParameter(statusBar, "statusBar");
        Intrinsics.checkNotNullParameter(optional, "bubblesOptional");
        Intrinsics.checkNotNullParameter(notificationPresenter, "presenter");
        Intrinsics.checkNotNullParameter(notificationListContainer, "listContainer");
        Intrinsics.checkNotNullParameter(notificationActivityStarter, "notificationActivityStarter");
        Intrinsics.checkNotNullParameter(bindRowCallback, "bindRowCallback");
        this.notificationListener.registerAsSystemService();
    }

    @Override // com.android.systemui.statusbar.notification.init.NotificationsController
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println();
        printWriter.println("Notification handling disabled");
        printWriter.println();
    }
}
