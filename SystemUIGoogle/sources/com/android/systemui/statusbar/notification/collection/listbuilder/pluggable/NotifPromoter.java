package com.android.systemui.statusbar.notification.collection.listbuilder.pluggable;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public abstract class NotifPromoter extends Pluggable<NotifPromoter> {
    public abstract boolean shouldPromoteToTopLevel(NotificationEntry notificationEntry);

    /* access modifiers changed from: protected */
    public NotifPromoter(String str) {
        super(str);
    }
}
