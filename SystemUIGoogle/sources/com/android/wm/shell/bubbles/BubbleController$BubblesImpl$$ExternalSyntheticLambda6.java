package com.android.wm.shell.bubbles;

import android.service.notification.NotificationListenerService;
import com.android.wm.shell.bubbles.BubbleController;
import java.util.HashMap;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ NotificationListenerService.RankingMap f$1;
    public final /* synthetic */ HashMap f$2;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda6(BubbleController.BubblesImpl bubblesImpl, NotificationListenerService.RankingMap rankingMap, HashMap hashMap) {
        this.f$0 = bubblesImpl;
        this.f$1 = rankingMap;
        this.f$2 = hashMap;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onRankingUpdated$18(this.f$1, this.f$2);
    }
}
