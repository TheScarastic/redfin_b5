package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.os.RemoteException;
import android.service.notification.NotificationListenerService;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifCollectionLogger.kt */
/* loaded from: classes.dex */
public final class NotifCollectionLogger {
    private final LogBuffer buffer;

    public NotifCollectionLogger(LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logNotifPosted(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifPosted$2 notifCollectionLogger$logNotifPosted$2 = NotifCollectionLogger$logNotifPosted$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifPosted$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifGroupPosted(String str, int i) {
        Intrinsics.checkNotNullParameter(str, "groupKey");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifGroupPosted$2 notifCollectionLogger$logNotifGroupPosted$2 = NotifCollectionLogger$logNotifGroupPosted$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifGroupPosted$2);
            obtain.setStr1(str);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifUpdated(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifUpdated$2 notifCollectionLogger$logNotifUpdated$2 = NotifCollectionLogger$logNotifUpdated$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifUpdated$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifRemoved(String str, int i) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifRemoved$2 notifCollectionLogger$logNotifRemoved$2 = NotifCollectionLogger$logNotifRemoved$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifRemoved$2);
            obtain.setStr1(str);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifReleased(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifReleased$2 notifCollectionLogger$logNotifReleased$2 = NotifCollectionLogger$logNotifReleased$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifReleased$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifDismissed(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifDismissed$2 notifCollectionLogger$logNotifDismissed$2 = NotifCollectionLogger$logNotifDismissed$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifDismissed$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }

    public final void logChildDismissed(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotifCollectionLogger$logChildDismissed$2 notifCollectionLogger$logChildDismissed$2 = NotifCollectionLogger$logChildDismissed$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logChildDismissed$2);
            obtain.setStr1(notificationEntry.getKey());
            logBuffer.push(obtain);
        }
    }

    public final void logDismissAll(int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logDismissAll$2 notifCollectionLogger$logDismissAll$2 = NotifCollectionLogger$logDismissAll$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logDismissAll$2);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logDismissOnAlreadyCanceledEntry(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotifCollectionLogger$logDismissOnAlreadyCanceledEntry$2 notifCollectionLogger$logDismissOnAlreadyCanceledEntry$2 = NotifCollectionLogger$logDismissOnAlreadyCanceledEntry$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logDismissOnAlreadyCanceledEntry$2);
            obtain.setStr1(notificationEntry.getKey());
            logBuffer.push(obtain);
        }
    }

    public final void logNotifDismissedIntercepted(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifDismissedIntercepted$2 notifCollectionLogger$logNotifDismissedIntercepted$2 = NotifCollectionLogger$logNotifDismissedIntercepted$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifDismissedIntercepted$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }

    public final void logNotifClearAllDismissalIntercepted(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logNotifClearAllDismissalIntercepted$2 notifCollectionLogger$logNotifClearAllDismissalIntercepted$2 = NotifCollectionLogger$logNotifClearAllDismissalIntercepted$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNotifClearAllDismissalIntercepted$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }

    public final void logNoNotificationToRemoveWithKey(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.ERROR;
        NotifCollectionLogger$logNoNotificationToRemoveWithKey$2 notifCollectionLogger$logNoNotificationToRemoveWithKey$2 = NotifCollectionLogger$logNoNotificationToRemoveWithKey$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logNoNotificationToRemoveWithKey$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }

    public final void logRankingMissing(String str, NotificationListenerService.RankingMap rankingMap) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(rankingMap, "rankingMap");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.WARNING;
        NotifCollectionLogger$logRankingMissing$2 notifCollectionLogger$logRankingMissing$2 = NotifCollectionLogger$logRankingMissing$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logRankingMissing$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
        LogBuffer logBuffer2 = this.buffer;
        LogLevel logLevel2 = LogLevel.DEBUG;
        NotifCollectionLogger$logRankingMissing$4 notifCollectionLogger$logRankingMissing$4 = NotifCollectionLogger$logRankingMissing$4.INSTANCE;
        if (!logBuffer2.getFrozen()) {
            logBuffer2.push(logBuffer2.obtain("NotifCollection", logLevel2, notifCollectionLogger$logRankingMissing$4));
        }
        String[] orderedKeys = rankingMap.getOrderedKeys();
        Intrinsics.checkNotNullExpressionValue(orderedKeys, "rankingMap.orderedKeys");
        int i = 0;
        int length = orderedKeys.length;
        while (i < length) {
            String str2 = orderedKeys[i];
            i++;
            LogBuffer logBuffer3 = this.buffer;
            LogLevel logLevel3 = LogLevel.DEBUG;
            NotifCollectionLogger$logRankingMissing$6 notifCollectionLogger$logRankingMissing$6 = NotifCollectionLogger$logRankingMissing$6.INSTANCE;
            if (!logBuffer3.getFrozen()) {
                LogMessageImpl obtain2 = logBuffer3.obtain("NotifCollection", logLevel3, notifCollectionLogger$logRankingMissing$6);
                obtain2.setStr1(str2);
                logBuffer3.push(obtain2);
            }
        }
    }

    public final void logRemoteExceptionOnNotificationClear(String str, RemoteException remoteException) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(remoteException, "e");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.WTF;
        NotifCollectionLogger$logRemoteExceptionOnNotificationClear$2 notifCollectionLogger$logRemoteExceptionOnNotificationClear$2 = NotifCollectionLogger$logRemoteExceptionOnNotificationClear$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logRemoteExceptionOnNotificationClear$2);
            obtain.setStr1(str);
            obtain.setStr2(remoteException.toString());
            logBuffer.push(obtain);
        }
    }

    public final void logRemoteExceptionOnClearAllNotifications(RemoteException remoteException) {
        Intrinsics.checkNotNullParameter(remoteException, "e");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.WTF;
        NotifCollectionLogger$logRemoteExceptionOnClearAllNotifications$2 notifCollectionLogger$logRemoteExceptionOnClearAllNotifications$2 = NotifCollectionLogger$logRemoteExceptionOnClearAllNotifications$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logRemoteExceptionOnClearAllNotifications$2);
            obtain.setStr1(remoteException.toString());
            logBuffer.push(obtain);
        }
    }

    public final void logLifetimeExtended(String str, NotifLifetimeExtender notifLifetimeExtender) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(notifLifetimeExtender, "extender");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logLifetimeExtended$2 notifCollectionLogger$logLifetimeExtended$2 = NotifCollectionLogger$logLifetimeExtended$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logLifetimeExtended$2);
            obtain.setStr1(str);
            obtain.setStr2(notifLifetimeExtender.getName());
            logBuffer.push(obtain);
        }
    }

    public final void logLifetimeExtensionEnded(String str, NotifLifetimeExtender notifLifetimeExtender, int i) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(notifLifetimeExtender, "extender");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        NotifCollectionLogger$logLifetimeExtensionEnded$2 notifCollectionLogger$logLifetimeExtensionEnded$2 = NotifCollectionLogger$logLifetimeExtensionEnded$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifCollection", logLevel, notifCollectionLogger$logLifetimeExtensionEnded$2);
            obtain.setStr1(str);
            obtain.setStr2(notifLifetimeExtender.getName());
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }
}
