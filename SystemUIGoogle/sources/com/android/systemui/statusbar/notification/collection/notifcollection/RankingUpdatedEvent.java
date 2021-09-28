package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.service.notification.NotificationListenerService;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public final class RankingUpdatedEvent extends NotifEvent {
    private final NotificationListenerService.RankingMap rankingMap;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof RankingUpdatedEvent) && Intrinsics.areEqual(this.rankingMap, ((RankingUpdatedEvent) obj).rankingMap);
    }

    public int hashCode() {
        return this.rankingMap.hashCode();
    }

    public String toString() {
        return "RankingUpdatedEvent(rankingMap=" + this.rankingMap + ')';
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public RankingUpdatedEvent(NotificationListenerService.RankingMap rankingMap) {
        super(null);
        Intrinsics.checkNotNullParameter(rankingMap, "rankingMap");
        this.rankingMap = rankingMap;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent
    public void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        Intrinsics.checkNotNullParameter(notifCollectionListener, "listener");
        notifCollectionListener.onRankingUpdate(this.rankingMap);
    }
}
