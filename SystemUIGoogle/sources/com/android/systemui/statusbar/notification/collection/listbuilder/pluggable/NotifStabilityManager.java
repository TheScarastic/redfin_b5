package com.android.systemui.statusbar.notification.collection.listbuilder.pluggable;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public abstract class NotifStabilityManager extends Pluggable<NotifStabilityManager> {
    public abstract boolean isGroupChangeAllowed(NotificationEntry notificationEntry);

    public abstract boolean isSectionChangeAllowed(NotificationEntry notificationEntry);

    public abstract void onBeginRun();

    /* access modifiers changed from: protected */
    public NotifStabilityManager(String str) {
        super(str);
    }
}
