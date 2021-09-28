package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public final class EntryRemovedEvent extends NotifEvent {
    private final NotificationEntry entry;
    private final int reason;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EntryRemovedEvent)) {
            return false;
        }
        EntryRemovedEvent entryRemovedEvent = (EntryRemovedEvent) obj;
        return Intrinsics.areEqual(this.entry, entryRemovedEvent.entry) && this.reason == entryRemovedEvent.reason;
    }

    public int hashCode() {
        return (this.entry.hashCode() * 31) + Integer.hashCode(this.reason);
    }

    public String toString() {
        return "EntryRemovedEvent(entry=" + this.entry + ", reason=" + this.reason + ')';
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public EntryRemovedEvent(NotificationEntry notificationEntry, int i) {
        super(null);
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        this.entry = notificationEntry;
        this.reason = i;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent
    public void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        Intrinsics.checkNotNullParameter(notifCollectionListener, "listener");
        notifCollectionListener.onEntryRemoved(this.entry, this.reason);
    }
}
