package com.android.systemui.statusbar.notification.collection.notifcollection;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public final class RankingAppliedEvent extends NotifEvent {
    public RankingAppliedEvent() {
        super(null);
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent
    public void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        Intrinsics.checkNotNullParameter(notifCollectionListener, "listener");
        notifCollectionListener.onRankingApplied();
    }
}
