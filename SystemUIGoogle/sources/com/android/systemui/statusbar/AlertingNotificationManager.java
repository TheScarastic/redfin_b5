package com.android.systemui.statusbar;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Iterator;
import java.util.stream.Stream;
/* loaded from: classes.dex */
public abstract class AlertingNotificationManager implements NotificationLifetimeExtender {
    protected int mAutoDismissNotificationDecay;
    protected int mMinimumDisplayTime;
    protected NotificationLifetimeExtender.NotificationSafeToRemoveCallback mNotificationLifetimeFinishedCallback;
    protected final Clock mClock = new Clock();
    protected final ArrayMap<String, AlertEntry> mAlertEntries = new ArrayMap<>();
    protected final ArraySet<NotificationEntry> mExtendedLifetimeAlertEntries = new ArraySet<>();
    @VisibleForTesting
    public Handler mHandler = new Handler(Looper.getMainLooper());

    protected abstract void onAlertEntryAdded(AlertEntry alertEntry);

    protected abstract void onAlertEntryRemoved(AlertEntry alertEntry);

    public void showNotification(NotificationEntry notificationEntry) {
        if (Log.isLoggable("AlertNotifManager", 2)) {
            Log.v("AlertNotifManager", "showNotification");
        }
        addAlertEntry(notificationEntry);
        updateNotification(notificationEntry.getKey(), true);
        notificationEntry.setInterruption();
    }

    public boolean removeNotification(String str, boolean z) {
        if (Log.isLoggable("AlertNotifManager", 2)) {
            Log.v("AlertNotifManager", "removeNotification");
        }
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        if (alertEntry == null) {
            return true;
        }
        if (z || canRemoveImmediately(str)) {
            removeAlertEntry(str);
            return true;
        }
        alertEntry.removeAsSoonAsPossible();
        return false;
    }

    public void updateNotification(String str, boolean z) {
        if (Log.isLoggable("AlertNotifManager", 2)) {
            Log.v("AlertNotifManager", "updateNotification");
        }
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        if (alertEntry != null) {
            alertEntry.mEntry.sendAccessibilityEvent(2048);
            if (z) {
                alertEntry.updateEntry(true);
            }
        }
    }

    public void releaseAllImmediately() {
        if (Log.isLoggable("AlertNotifManager", 2)) {
            Log.v("AlertNotifManager", "releaseAllImmediately");
        }
        Iterator it = new ArraySet(this.mAlertEntries.keySet()).iterator();
        while (it.hasNext()) {
            removeAlertEntry((String) it.next());
        }
    }

    /* JADX DEBUG: Type inference failed for r1v4. Raw type applied. Possible types: java.util.stream.Stream<R>, java.util.stream.Stream<com.android.systemui.statusbar.notification.collection.NotificationEntry> */
    public Stream<NotificationEntry> getAllEntries() {
        return this.mAlertEntries.values().stream().map(AlertingNotificationManager$$ExternalSyntheticLambda0.INSTANCE);
    }

    public boolean hasNotifications() {
        return !this.mAlertEntries.isEmpty();
    }

    public boolean isAlerting(String str) {
        return this.mAlertEntries.containsKey(str);
    }

    protected final void addAlertEntry(NotificationEntry notificationEntry) {
        AlertEntry createAlertEntry = createAlertEntry();
        createAlertEntry.setEntry(notificationEntry);
        this.mAlertEntries.put(notificationEntry.getKey(), createAlertEntry);
        onAlertEntryAdded(createAlertEntry);
        notificationEntry.sendAccessibilityEvent(2048);
        notificationEntry.setIsAlerting(true);
    }

    public final void removeAlertEntry(String str) {
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        if (alertEntry != null) {
            NotificationEntry notificationEntry = alertEntry.mEntry;
            if (notificationEntry == null || !notificationEntry.isExpandAnimationRunning()) {
                this.mAlertEntries.remove(str);
                onAlertEntryRemoved(alertEntry);
                notificationEntry.sendAccessibilityEvent(2048);
                alertEntry.reset();
                if (this.mExtendedLifetimeAlertEntries.contains(notificationEntry)) {
                    NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback = this.mNotificationLifetimeFinishedCallback;
                    if (notificationSafeToRemoveCallback != null) {
                        notificationSafeToRemoveCallback.onSafeToRemove(str);
                    }
                    this.mExtendedLifetimeAlertEntries.remove(notificationEntry);
                }
            }
        }
    }

    protected AlertEntry createAlertEntry() {
        return new AlertEntry();
    }

    public boolean canRemoveImmediately(String str) {
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        return alertEntry == null || alertEntry.wasShownLongEnough() || alertEntry.mEntry.isRowDismissed();
    }

    @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
    public void setCallback(NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback) {
        this.mNotificationLifetimeFinishedCallback = notificationSafeToRemoveCallback;
    }

    @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
    public boolean shouldExtendLifetime(NotificationEntry notificationEntry) {
        return !canRemoveImmediately(notificationEntry.getKey());
    }

    @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
    public void setShouldManageLifetime(NotificationEntry notificationEntry, boolean z) {
        if (z) {
            this.mExtendedLifetimeAlertEntries.add(notificationEntry);
            this.mAlertEntries.get(notificationEntry.getKey()).removeAsSoonAsPossible();
            return;
        }
        this.mExtendedLifetimeAlertEntries.remove(notificationEntry);
    }

    /* loaded from: classes.dex */
    public class AlertEntry implements Comparable<AlertEntry> {
        public long mEarliestRemovaltime;
        public NotificationEntry mEntry;
        public long mPostTime;
        protected Runnable mRemoveAlertRunnable;

        public boolean isSticky() {
            return false;
        }

        public AlertEntry() {
            AlertingNotificationManager.this = r1;
        }

        public /* synthetic */ void lambda$setEntry$0(NotificationEntry notificationEntry) {
            AlertingNotificationManager.this.removeAlertEntry(notificationEntry.getKey());
        }

        public void setEntry(NotificationEntry notificationEntry) {
            setEntry(notificationEntry, new AlertingNotificationManager$AlertEntry$$ExternalSyntheticLambda0(this, notificationEntry));
        }

        public void setEntry(NotificationEntry notificationEntry, Runnable runnable) {
            this.mEntry = notificationEntry;
            this.mRemoveAlertRunnable = runnable;
            this.mPostTime = calculatePostTime();
            updateEntry(true);
        }

        public void updateEntry(boolean z) {
            if (Log.isLoggable("AlertNotifManager", 2)) {
                Log.v("AlertNotifManager", "updateEntry");
            }
            long currentTimeMillis = AlertingNotificationManager.this.mClock.currentTimeMillis();
            this.mEarliestRemovaltime = ((long) AlertingNotificationManager.this.mMinimumDisplayTime) + currentTimeMillis;
            if (z) {
                this.mPostTime = Math.max(this.mPostTime, currentTimeMillis);
            }
            removeAutoRemovalCallbacks();
            if (!isSticky()) {
                AlertingNotificationManager.this.mHandler.postDelayed(this.mRemoveAlertRunnable, Math.max(calculateFinishTime() - currentTimeMillis, (long) AlertingNotificationManager.this.mMinimumDisplayTime));
            }
        }

        public boolean wasShownLongEnough() {
            return this.mEarliestRemovaltime < AlertingNotificationManager.this.mClock.currentTimeMillis();
        }

        public int compareTo(AlertEntry alertEntry) {
            long j = this.mPostTime;
            long j2 = alertEntry.mPostTime;
            if (j < j2) {
                return 1;
            }
            if (j == j2) {
                return this.mEntry.getKey().compareTo(alertEntry.mEntry.getKey());
            }
            return -1;
        }

        public void reset() {
            this.mEntry = null;
            removeAutoRemovalCallbacks();
            this.mRemoveAlertRunnable = null;
        }

        public void removeAutoRemovalCallbacks() {
            Runnable runnable = this.mRemoveAlertRunnable;
            if (runnable != null) {
                AlertingNotificationManager.this.mHandler.removeCallbacks(runnable);
            }
        }

        public void removeAsSoonAsPossible() {
            if (this.mRemoveAlertRunnable != null) {
                removeAutoRemovalCallbacks();
                AlertingNotificationManager alertingNotificationManager = AlertingNotificationManager.this;
                alertingNotificationManager.mHandler.postDelayed(this.mRemoveAlertRunnable, this.mEarliestRemovaltime - alertingNotificationManager.mClock.currentTimeMillis());
            }
        }

        public long calculatePostTime() {
            return AlertingNotificationManager.this.mClock.currentTimeMillis();
        }

        protected long calculateFinishTime() {
            return this.mPostTime + ((long) AlertingNotificationManager.this.mAutoDismissNotificationDecay);
        }
    }

    /* loaded from: classes.dex */
    public static final class Clock {
        protected Clock() {
        }

        public long currentTimeMillis() {
            return SystemClock.elapsedRealtime();
        }
    }
}
