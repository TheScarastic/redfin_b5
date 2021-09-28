package com.android.systemui.statusbar.notification.icon;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: IconManager.kt */
/* loaded from: classes.dex */
public final class IconManager$entryListener$1 implements NotifCollectionListener {
    final /* synthetic */ IconManager this$0;

    /* access modifiers changed from: package-private */
    public IconManager$entryListener$1(IconManager iconManager) {
        this.this$0 = iconManager;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
    public void onEntryInit(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        notificationEntry.addOnSensitivityChangedListener(this.this$0.sensitivityListener);
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
    public void onEntryCleanUp(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        notificationEntry.removeOnSensitivityChangedListener(this.this$0.sensitivityListener);
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
    public void onRankingApplied() {
        for (NotificationEntry notificationEntry : this.this$0.notifCollection.getAllNotifs()) {
            IconManager iconManager = this.this$0;
            Intrinsics.checkNotNullExpressionValue(notificationEntry, "entry");
            boolean z = iconManager.isImportantConversation(notificationEntry);
            if (notificationEntry.getIcons().getAreIconsAvailable() && z != notificationEntry.getIcons().isImportantConversation()) {
                this.this$0.updateIconsSafe(notificationEntry);
            }
            notificationEntry.getIcons().setImportantConversation(z);
        }
    }
}
