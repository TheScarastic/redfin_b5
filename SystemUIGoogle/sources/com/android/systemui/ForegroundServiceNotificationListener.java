package com.android.systemui;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.ForegroundServiceController;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.util.time.SystemClock;
/* loaded from: classes.dex */
public class ForegroundServiceNotificationListener {
    private final Context mContext;
    private final NotificationEntryManager mEntryManager;
    private final ForegroundServiceController mForegroundServiceController;

    public ForegroundServiceNotificationListener(Context context, ForegroundServiceController foregroundServiceController, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, SystemClock systemClock) {
        this.mContext = context;
        this.mForegroundServiceController = foregroundServiceController;
        this.mEntryManager = notificationEntryManager;
        notificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() { // from class: com.android.systemui.ForegroundServiceNotificationListener.1
            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onPendingEntryAdded(NotificationEntry notificationEntry) {
                ForegroundServiceNotificationListener.this.addNotification(notificationEntry, notificationEntry.getImportance());
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onPreEntryUpdated(NotificationEntry notificationEntry) {
                ForegroundServiceNotificationListener.this.updateNotification(notificationEntry, notificationEntry.getImportance());
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
                ForegroundServiceNotificationListener.this.removeNotification(notificationEntry.getSbn());
            }
        });
        notifPipeline.addCollectionListener(new NotifCollectionListener() { // from class: com.android.systemui.ForegroundServiceNotificationListener.2
            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryAdded(NotificationEntry notificationEntry) {
                ForegroundServiceNotificationListener.this.addNotification(notificationEntry, notificationEntry.getImportance());
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryUpdated(NotificationEntry notificationEntry) {
                ForegroundServiceNotificationListener.this.updateNotification(notificationEntry, notificationEntry.getImportance());
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
                ForegroundServiceNotificationListener.this.removeNotification(notificationEntry.getSbn());
            }
        });
    }

    /* access modifiers changed from: private */
    public void addNotification(NotificationEntry notificationEntry, int i) {
        updateNotification(notificationEntry, i);
    }

    /* access modifiers changed from: private */
    public void removeNotification(final StatusBarNotification statusBarNotification) {
        this.mForegroundServiceController.updateUserState(statusBarNotification.getUserId(), new ForegroundServiceController.UserStateUpdateCallback() { // from class: com.android.systemui.ForegroundServiceNotificationListener.3
            @Override // com.android.systemui.ForegroundServiceController.UserStateUpdateCallback
            public boolean updateUserState(ForegroundServicesUserState foregroundServicesUserState) {
                if (!ForegroundServiceNotificationListener.this.mForegroundServiceController.isDisclosureNotification(statusBarNotification)) {
                    return foregroundServicesUserState.removeNotification(statusBarNotification.getPackageName(), statusBarNotification.getKey());
                }
                foregroundServicesUserState.setRunningServices(null, 0);
                return true;
            }
        }, false);
    }

    /* access modifiers changed from: private */
    public void updateNotification(NotificationEntry notificationEntry, int i) {
        StatusBarNotification sbn = notificationEntry.getSbn();
        this.mForegroundServiceController.updateUserState(sbn.getUserId(), new ForegroundServiceController.UserStateUpdateCallback(sbn, i) { // from class: com.android.systemui.ForegroundServiceNotificationListener$$ExternalSyntheticLambda0
            public final /* synthetic */ StatusBarNotification f$1;
            public final /* synthetic */ int f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // com.android.systemui.ForegroundServiceController.UserStateUpdateCallback
            public final boolean updateUserState(ForegroundServicesUserState foregroundServicesUserState) {
                return ForegroundServiceNotificationListener.this.lambda$updateNotification$0(this.f$1, this.f$2, foregroundServicesUserState);
            }
        }, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateNotification$0(StatusBarNotification statusBarNotification, int i, ForegroundServicesUserState foregroundServicesUserState) {
        if (this.mForegroundServiceController.isDisclosureNotification(statusBarNotification)) {
            Bundle bundle = statusBarNotification.getNotification().extras;
            if (bundle != null) {
                foregroundServicesUserState.setRunningServices(bundle.getStringArray("android.foregroundApps"), statusBarNotification.getNotification().when);
            }
        } else {
            foregroundServicesUserState.removeNotification(statusBarNotification.getPackageName(), statusBarNotification.getKey());
            if ((statusBarNotification.getNotification().flags & 64) != 0 && i > 1) {
                foregroundServicesUserState.addImportantNotification(statusBarNotification.getPackageName(), statusBarNotification.getKey());
            }
            if (Notification.Builder.recoverBuilder(this.mContext, statusBarNotification.getNotification()).usesStandardHeader()) {
                foregroundServicesUserState.addStandardLayoutNotification(statusBarNotification.getPackageName(), statusBarNotification.getKey());
            }
        }
        return true;
    }
}
