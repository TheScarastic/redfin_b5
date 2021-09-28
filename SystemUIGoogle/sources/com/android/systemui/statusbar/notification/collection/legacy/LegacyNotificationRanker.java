package com.android.systemui.statusbar.notification.collection.legacy;

import android.service.notification.NotificationListenerService;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Collection;
import java.util.List;
/* compiled from: LegacyNotificationRanker.kt */
/* loaded from: classes.dex */
public interface LegacyNotificationRanker {
    NotificationListenerService.RankingMap getRankingMap();

    boolean isNotificationForCurrentProfiles(NotificationEntry notificationEntry);

    List<NotificationEntry> updateRanking(NotificationListenerService.RankingMap rankingMap, Collection<NotificationEntry> collection, String str);
}
