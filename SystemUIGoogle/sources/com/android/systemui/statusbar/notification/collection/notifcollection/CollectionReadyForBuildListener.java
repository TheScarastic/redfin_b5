package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Collection;
/* loaded from: classes.dex */
public interface CollectionReadyForBuildListener {
    void onBuildList(Collection<NotificationEntry> collection);
}
