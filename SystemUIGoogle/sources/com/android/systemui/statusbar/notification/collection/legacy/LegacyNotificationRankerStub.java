package com.android.systemui.statusbar.notification.collection.legacy;

import android.service.notification.NotificationListenerService;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
/* loaded from: classes.dex */
public class LegacyNotificationRankerStub implements LegacyNotificationRanker {
    private NotificationListenerService.RankingMap mRankingMap = new NotificationListenerService.RankingMap(new NotificationListenerService.Ranking[0]);
    private final Comparator<NotificationEntry> mEntryComparator = Comparator.comparingLong(LegacyNotificationRankerStub$$ExternalSyntheticLambda0.INSTANCE);

    @Override // com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker
    public boolean isNotificationForCurrentProfiles(NotificationEntry notificationEntry) {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker
    public List<NotificationEntry> updateRanking(NotificationListenerService.RankingMap rankingMap, Collection<NotificationEntry> collection, String str) {
        if (rankingMap != null) {
            this.mRankingMap = rankingMap;
        }
        ArrayList arrayList = new ArrayList(collection);
        arrayList.sort(this.mEntryComparator);
        return arrayList;
    }

    @Override // com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker
    public NotificationListenerService.RankingMap getRankingMap() {
        return this.mRankingMap;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ long lambda$new$0(NotificationEntry notificationEntry) {
        return notificationEntry.getSbn().getNotification().when;
    }
}
