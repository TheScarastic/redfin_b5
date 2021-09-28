package com.android.systemui.statusbar;

import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationInteractionTracker.kt */
/* loaded from: classes.dex */
public final class NotificationInteractionTracker implements NotifCollectionListener, NotificationInteractionListener {
    private final NotificationClickNotifier clicker;
    private final NotificationEntryManager entryManager;
    private final Map<String, Boolean> interactions = new LinkedHashMap();

    public NotificationInteractionTracker(NotificationClickNotifier notificationClickNotifier, NotificationEntryManager notificationEntryManager) {
        Intrinsics.checkNotNullParameter(notificationClickNotifier, "clicker");
        Intrinsics.checkNotNullParameter(notificationEntryManager, "entryManager");
        this.clicker = notificationClickNotifier;
        this.entryManager = notificationEntryManager;
        notificationClickNotifier.addNotificationInteractionListener(this);
        notificationEntryManager.addCollectionListener(this);
    }

    public final boolean hasUserInteractedWith(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        Boolean bool = this.interactions.get(str);
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
    public void onEntryAdded(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Map<String, Boolean> map = this.interactions;
        String key = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "entry.key");
        map.put(key, Boolean.FALSE);
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
    public void onEntryCleanUp(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        this.interactions.remove(notificationEntry.getKey());
    }

    @Override // com.android.systemui.statusbar.NotificationInteractionListener
    public void onNotificationInteraction(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.interactions.put(str, Boolean.TRUE);
    }
}
