package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
/* loaded from: classes.dex */
public final /* synthetic */ class HeadsUpCoordinator$1$$ExternalSyntheticLambda1 implements NotifBindPipeline.BindCallback {
    public final /* synthetic */ HeadsUpCoordinator f$0;

    public /* synthetic */ HeadsUpCoordinator$1$$ExternalSyntheticLambda1(HeadsUpCoordinator headsUpCoordinator) {
        this.f$0 = headsUpCoordinator;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotifBindPipeline.BindCallback
    public final void onBindFinished(NotificationEntry notificationEntry) {
        HeadsUpCoordinator.access$400(this.f$0, notificationEntry);
    }
}
