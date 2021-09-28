package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public final class BindEntryEvent extends NotifEvent {
    private final NotificationEntry entry;
    private final StatusBarNotification sbn;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BindEntryEvent)) {
            return false;
        }
        BindEntryEvent bindEntryEvent = (BindEntryEvent) obj;
        return Intrinsics.areEqual(this.entry, bindEntryEvent.entry) && Intrinsics.areEqual(this.sbn, bindEntryEvent.sbn);
    }

    public int hashCode() {
        return (this.entry.hashCode() * 31) + this.sbn.hashCode();
    }

    public String toString() {
        return "BindEntryEvent(entry=" + this.entry + ", sbn=" + this.sbn + ')';
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public BindEntryEvent(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
        super(null);
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        this.entry = notificationEntry;
        this.sbn = statusBarNotification;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent
    public void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        Intrinsics.checkNotNullParameter(notifCollectionListener, "listener");
        notifCollectionListener.onEntryBind(this.entry, this.sbn);
    }
}
