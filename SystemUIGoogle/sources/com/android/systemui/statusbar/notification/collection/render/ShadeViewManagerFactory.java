package com.android.systemui.statusbar.notification.collection.render;

import android.content.Context;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ShadeViewManager.kt */
/* loaded from: classes.dex */
public final class ShadeViewManagerFactory {
    private final Context context;
    private final ShadeViewDifferLogger logger;
    private final NotificationIconAreaController notificationIconAreaController;
    private final NotifViewBarn viewBarn;

    public ShadeViewManagerFactory(Context context, ShadeViewDifferLogger shadeViewDifferLogger, NotifViewBarn notifViewBarn, NotificationIconAreaController notificationIconAreaController) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(shadeViewDifferLogger, "logger");
        Intrinsics.checkNotNullParameter(notifViewBarn, "viewBarn");
        Intrinsics.checkNotNullParameter(notificationIconAreaController, "notificationIconAreaController");
        this.context = context;
        this.logger = shadeViewDifferLogger;
        this.viewBarn = notifViewBarn;
        this.notificationIconAreaController = notificationIconAreaController;
    }

    public final ShadeViewManager create(NotificationListContainer notificationListContainer) {
        Intrinsics.checkNotNullParameter(notificationListContainer, "listContainer");
        return new ShadeViewManager(this.context, notificationListContainer, this.logger, this.viewBarn, this.notificationIconAreaController);
    }
}
