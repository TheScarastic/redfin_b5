package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public final class CleanUpEntryEvent extends NotifEvent {
    private final NotificationEntry entry;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof CleanUpEntryEvent) && Intrinsics.areEqual(this.entry, ((CleanUpEntryEvent) obj).entry);
    }

    public int hashCode() {
        return this.entry.hashCode();
    }

    public String toString() {
        return "CleanUpEntryEvent(entry=" + this.entry + ')';
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public CleanUpEntryEvent(NotificationEntry notificationEntry) {
        super(null);
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        this.entry = notificationEntry;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent
    public void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        Intrinsics.checkNotNullParameter(notifCollectionListener, "listener");
        notifCollectionListener.onEntryCleanUp(this.entry);
    }
}
