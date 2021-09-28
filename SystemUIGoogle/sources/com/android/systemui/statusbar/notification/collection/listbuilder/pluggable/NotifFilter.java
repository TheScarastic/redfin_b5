package com.android.systemui.statusbar.notification.collection.listbuilder.pluggable;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public abstract class NotifFilter extends Pluggable<NotifFilter> {
    public abstract boolean shouldFilterOut(NotificationEntry notificationEntry, long j);

    /* access modifiers changed from: protected */
    public NotifFilter(String str) {
        super(str);
    }
}
