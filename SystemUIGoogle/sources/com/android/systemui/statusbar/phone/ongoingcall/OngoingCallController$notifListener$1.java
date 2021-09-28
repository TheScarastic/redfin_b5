package com.android.systemui.statusbar.phone.ongoingcall;

import android.app.PendingIntent;
import android.content.Intent;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: OngoingCallController.kt */
/* loaded from: classes.dex */
public final class OngoingCallController$notifListener$1 implements NotifCollectionListener {
    final /* synthetic */ OngoingCallController this$0;

    /* access modifiers changed from: package-private */
    public OngoingCallController$notifListener$1(OngoingCallController ongoingCallController) {
        this.this$0 = ongoingCallController;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
    public void onEntryAdded(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        onEntryUpdated(notificationEntry);
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
    public void onEntryUpdated(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intent intent = null;
        if (this.this$0.callNotificationInfo != null || !(OngoingCallControllerKt.isCallNotification(notificationEntry))) {
            String key = notificationEntry.getSbn().getKey();
            OngoingCallController.CallNotificationInfo callNotificationInfo = this.this$0.callNotificationInfo;
            if (!Intrinsics.areEqual(key, callNotificationInfo == null ? null : callNotificationInfo.getKey())) {
                return;
            }
        }
        String key2 = notificationEntry.getSbn().getKey();
        Intrinsics.checkNotNullExpressionValue(key2, "entry.sbn.key");
        long j = notificationEntry.getSbn().getNotification().when;
        PendingIntent pendingIntent = notificationEntry.getSbn().getNotification().contentIntent;
        if (pendingIntent != null) {
            intent = pendingIntent.getIntent();
        }
        OngoingCallController.CallNotificationInfo callNotificationInfo2 = new OngoingCallController.CallNotificationInfo(key2, j, intent, notificationEntry.getSbn().getUid(), notificationEntry.getSbn().getNotification().extras.getInt("android.callType", -1) == 2);
        if (!Intrinsics.areEqual(callNotificationInfo2, this.this$0.callNotificationInfo)) {
            this.this$0.callNotificationInfo = callNotificationInfo2;
            if (callNotificationInfo2.isOngoing()) {
                this.this$0.updateChip();
            } else {
                this.this$0.removeChip();
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
    public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        String key = notificationEntry.getSbn().getKey();
        OngoingCallController.CallNotificationInfo callNotificationInfo = this.this$0.callNotificationInfo;
        if (Intrinsics.areEqual(key, callNotificationInfo == null ? null : callNotificationInfo.getKey())) {
            this.this$0.removeChip();
        }
    }
}
