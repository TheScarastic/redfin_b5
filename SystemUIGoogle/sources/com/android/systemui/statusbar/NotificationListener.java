package com.android.systemui.statusbar;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.android.systemui.statusbar.phone.NotificationListenerWithPlugins;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@SuppressLint({"OverrideAbstract"})
/* loaded from: classes.dex */
public class NotificationListener extends NotificationListenerWithPlugins {
    private final Context mContext;
    private final Handler mMainHandler;
    private final NotificationManager mNotificationManager;
    private final List<NotificationHandler> mNotificationHandlers = new ArrayList();
    private final ArrayList<NotificationSettingsListener> mSettingsListeners = new ArrayList<>();

    /* loaded from: classes.dex */
    public interface NotificationHandler {
        default void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        }

        void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap);

        void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap);

        void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i);

        void onNotificationsInitialized();
    }

    /* loaded from: classes.dex */
    public interface NotificationSettingsListener {
        default void onStatusBarIconsBehaviorChanged(boolean z) {
        }
    }

    public NotificationListener(Context context, NotificationManager notificationManager, Handler handler) {
        this.mContext = context;
        this.mNotificationManager = notificationManager;
        this.mMainHandler = handler;
    }

    public void addNotificationHandler(NotificationHandler notificationHandler) {
        if (!this.mNotificationHandlers.contains(notificationHandler)) {
            this.mNotificationHandlers.add(notificationHandler);
            return;
        }
        throw new IllegalArgumentException("Listener is already added");
    }

    public void addNotificationSettingsListener(NotificationSettingsListener notificationSettingsListener) {
        this.mSettingsListeners.add(notificationSettingsListener);
    }

    @Override // android.service.notification.NotificationListenerService
    public void onListenerConnected() {
        onPluginConnected();
        StatusBarNotification[] activeNotifications = getActiveNotifications();
        if (activeNotifications == null) {
            Log.w("NotificationListener", "onListenerConnected unable to get active notifications.");
            return;
        }
        this.mMainHandler.post(new Runnable(activeNotifications, getCurrentRanking()) { // from class: com.android.systemui.statusbar.NotificationListener$$ExternalSyntheticLambda4
            public final /* synthetic */ StatusBarNotification[] f$1;
            public final /* synthetic */ NotificationListenerService.RankingMap f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                NotificationListener.this.lambda$onListenerConnected$0(this.f$1, this.f$2);
            }
        });
        onSilentStatusBarIconsVisibilityChanged(this.mNotificationManager.shouldHideSilentStatusBarIcons());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onListenerConnected$0(StatusBarNotification[] statusBarNotificationArr, NotificationListenerService.RankingMap rankingMap) {
        ArrayList arrayList = new ArrayList();
        for (StatusBarNotification statusBarNotification : statusBarNotificationArr) {
            arrayList.add(getRankingOrTemporaryStandIn(rankingMap, statusBarNotification.getKey()));
        }
        NotificationListenerService.RankingMap rankingMap2 = new NotificationListenerService.RankingMap((NotificationListenerService.Ranking[]) arrayList.toArray(new NotificationListenerService.Ranking[0]));
        for (StatusBarNotification statusBarNotification2 : statusBarNotificationArr) {
            for (NotificationHandler notificationHandler : this.mNotificationHandlers) {
                notificationHandler.onNotificationPosted(statusBarNotification2, rankingMap2);
            }
        }
        for (NotificationHandler notificationHandler2 : this.mNotificationHandlers) {
            notificationHandler2.onNotificationsInitialized();
        }
    }

    @Override // android.service.notification.NotificationListenerService
    public void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        if (statusBarNotification != null && !onPluginNotificationPosted(statusBarNotification, rankingMap)) {
            this.mMainHandler.post(new Runnable(statusBarNotification, rankingMap) { // from class: com.android.systemui.statusbar.NotificationListener$$ExternalSyntheticLambda1
                public final /* synthetic */ StatusBarNotification f$1;
                public final /* synthetic */ NotificationListenerService.RankingMap f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    NotificationListener.this.lambda$onNotificationPosted$1(this.f$1, this.f$2);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onNotificationPosted$1(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        RemoteInputController.processForRemoteInput(statusBarNotification.getNotification(), this.mContext);
        for (NotificationHandler notificationHandler : this.mNotificationHandlers) {
            notificationHandler.onNotificationPosted(statusBarNotification, rankingMap);
        }
    }

    @Override // android.service.notification.NotificationListenerService
    public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
        if (statusBarNotification != null && !onPluginNotificationRemoved(statusBarNotification, rankingMap)) {
            this.mMainHandler.post(new Runnable(statusBarNotification, rankingMap, i) { // from class: com.android.systemui.statusbar.NotificationListener$$ExternalSyntheticLambda2
                public final /* synthetic */ StatusBarNotification f$1;
                public final /* synthetic */ NotificationListenerService.RankingMap f$2;
                public final /* synthetic */ int f$3;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    NotificationListener.this.lambda$onNotificationRemoved$2(this.f$1, this.f$2, this.f$3);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onNotificationRemoved$2(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
        for (NotificationHandler notificationHandler : this.mNotificationHandlers) {
            notificationHandler.onNotificationRemoved(statusBarNotification, rankingMap, i);
        }
    }

    @Override // android.service.notification.NotificationListenerService
    public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        onNotificationRemoved(statusBarNotification, rankingMap, 0);
    }

    @Override // android.service.notification.NotificationListenerService
    public void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
        if (rankingMap != null) {
            this.mMainHandler.post(new Runnable(onPluginRankingUpdate(rankingMap)) { // from class: com.android.systemui.statusbar.NotificationListener$$ExternalSyntheticLambda0
                public final /* synthetic */ NotificationListenerService.RankingMap f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    NotificationListener.this.lambda$onNotificationRankingUpdate$3(this.f$1);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onNotificationRankingUpdate$3(NotificationListenerService.RankingMap rankingMap) {
        for (NotificationHandler notificationHandler : this.mNotificationHandlers) {
            notificationHandler.onNotificationRankingUpdate(rankingMap);
        }
    }

    @Override // android.service.notification.NotificationListenerService
    public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        if (!onPluginNotificationChannelModified(str, userHandle, notificationChannel, i)) {
            this.mMainHandler.post(new Runnable(str, userHandle, notificationChannel, i) { // from class: com.android.systemui.statusbar.NotificationListener$$ExternalSyntheticLambda3
                public final /* synthetic */ String f$1;
                public final /* synthetic */ UserHandle f$2;
                public final /* synthetic */ NotificationChannel f$3;
                public final /* synthetic */ int f$4;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                    this.f$4 = r5;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    NotificationListener.this.lambda$onNotificationChannelModified$4(this.f$1, this.f$2, this.f$3, this.f$4);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onNotificationChannelModified$4(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        for (NotificationHandler notificationHandler : this.mNotificationHandlers) {
            notificationHandler.onNotificationChannelModified(str, userHandle, notificationChannel, i);
        }
    }

    @Override // android.service.notification.NotificationListenerService
    public void onSilentStatusBarIconsVisibilityChanged(boolean z) {
        Iterator<NotificationSettingsListener> it = this.mSettingsListeners.iterator();
        while (it.hasNext()) {
            it.next().onStatusBarIconsBehaviorChanged(z);
        }
    }

    public void registerAsSystemService() {
        try {
            registerAsSystemService(this.mContext, new ComponentName(this.mContext.getPackageName(), NotificationListener.class.getCanonicalName()), -1);
        } catch (RemoteException e) {
            Log.e("NotificationListener", "Unable to register notification listener", e);
        }
    }

    private static NotificationListenerService.Ranking getRankingOrTemporaryStandIn(NotificationListenerService.RankingMap rankingMap, String str) {
        NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
        if (rankingMap.getRanking(str, ranking)) {
            return ranking;
        }
        ranking.populate(str, 0, false, 0, 0, 0, null, null, null, new ArrayList(), new ArrayList(), false, 0, false, 0, false, new ArrayList(), new ArrayList(), false, false, false, null, 0, false);
        return ranking;
    }
}
