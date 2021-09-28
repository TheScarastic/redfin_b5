package com.android.systemui.statusbar.notification;

import android.os.RemoteException;
import android.os.SystemClock;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.Dumpable;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationRemoveInterceptor;
import com.android.systemui.statusbar.NotificationUiAdjustment;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinder;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRankerStub;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.NotificationRowContentBinder;
import com.android.systemui.util.Assert;
import com.android.systemui.util.leak.LeakDetector;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public class NotificationEntryManager implements CommonNotifCollection, Dumpable, VisualStabilityManager.Callback {
    private static final boolean DEBUG = Log.isLoggable("NotificationEntryMgr", 3);
    private final Set<NotificationEntry> mAllNotifications;
    private final FeatureFlags mFeatureFlags;
    private final ForegroundServiceDismissalFeatureController mFgsFeatureController;
    private final NotificationGroupManagerLegacy mGroupManager;
    private NotificationListenerService.RankingMap mLatestRankingMap;
    private final LeakDetector mLeakDetector;
    private final NotificationEntryManagerLogger mLogger;
    private final Lazy<NotificationRowBinder> mNotificationRowBinderLazy;
    private NotificationPresenter mPresenter;
    private final Set<NotificationEntry> mReadOnlyAllNotifications;
    private final List<NotificationEntry> mReadOnlyNotifications;
    private final Lazy<NotificationRemoteInputManager> mRemoteInputManagerLazy;
    @VisibleForTesting
    protected final ArrayList<NotificationEntry> mSortedAndFiltered;
    private final IStatusBarService mStatusBarService;
    @VisibleForTesting
    protected final HashMap<String, NotificationEntry> mPendingNotifications = new HashMap<>();
    private final ArrayMap<String, NotificationEntry> mActiveNotifications = new ArrayMap<>();
    private final Map<NotificationEntry, NotificationLifetimeExtender> mRetainedNotifications = new ArrayMap();
    private final List<NotifCollectionListener> mNotifCollectionListeners = new ArrayList();
    private LegacyNotificationRanker mRanker = new LegacyNotificationRankerStub();
    @VisibleForTesting
    final ArrayList<NotificationLifetimeExtender> mNotificationLifetimeExtenders = new ArrayList<>();
    private final List<NotificationEntryListener> mNotificationEntryListeners = new ArrayList();
    private final List<NotificationRemoveInterceptor> mRemoveInterceptors = new ArrayList();
    private final NotificationRowContentBinder.InflationCallback mInflationCallback = new NotificationRowContentBinder.InflationCallback() { // from class: com.android.systemui.statusbar.notification.NotificationEntryManager.1
        @Override // com.android.systemui.statusbar.notification.row.NotificationRowContentBinder.InflationCallback
        public void handleInflationException(NotificationEntry notificationEntry, Exception exc) {
            NotificationEntryManager.this.handleInflationException(notificationEntry.getSbn(), exc);
        }

        @Override // com.android.systemui.statusbar.notification.row.NotificationRowContentBinder.InflationCallback
        public void onAsyncInflationFinished(NotificationEntry notificationEntry) {
            NotificationEntryManager.this.mPendingNotifications.remove(notificationEntry.getKey());
            if (!notificationEntry.isRowRemoved()) {
                boolean z = NotificationEntryManager.this.getActiveNotificationUnfiltered(notificationEntry.getKey()) == null;
                NotificationEntryManager.this.mLogger.logNotifInflated(notificationEntry.getKey(), z);
                if (z) {
                    for (NotificationEntryListener notificationEntryListener : NotificationEntryManager.this.mNotificationEntryListeners) {
                        notificationEntryListener.onEntryInflated(notificationEntry);
                    }
                    NotificationEntryManager.this.addActiveNotification(notificationEntry);
                    NotificationEntryManager.this.updateNotifications("onAsyncInflationFinished");
                    for (NotificationEntryListener notificationEntryListener2 : NotificationEntryManager.this.mNotificationEntryListeners) {
                        notificationEntryListener2.onNotificationAdded(notificationEntry);
                    }
                    return;
                }
                for (NotificationEntryListener notificationEntryListener3 : NotificationEntryManager.this.mNotificationEntryListeners) {
                    notificationEntryListener3.onEntryReinflated(notificationEntry);
                }
            }
        }
    };
    private final NotificationListener.NotificationHandler mNotifListener = new NotificationListener.NotificationHandler() { // from class: com.android.systemui.statusbar.notification.NotificationEntryManager.2
        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public void onNotificationsInitialized() {
        }

        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
            if (NotificationEntryManager.this.mActiveNotifications.containsKey(statusBarNotification.getKey())) {
                NotificationEntryManager.this.updateNotification(statusBarNotification, rankingMap);
            } else {
                NotificationEntryManager.this.addNotification(statusBarNotification, rankingMap);
            }
        }

        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
            NotificationEntryManager.this.removeNotification(statusBarNotification.getKey(), rankingMap, i);
        }

        @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
        public void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
            NotificationEntryManager.this.updateNotificationRanking(rankingMap);
        }
    };

    /* loaded from: classes.dex */
    public interface KeyguardEnvironment {
        boolean isDeviceProvisioned();

        boolean isNotificationForCurrentProfiles(StatusBarNotification statusBarNotification);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("NotificationEntryManager state:");
        printWriter.println("  mAllNotifications=");
        if (this.mAllNotifications.size() == 0) {
            printWriter.println("null");
        } else {
            int i = 0;
            for (NotificationEntry notificationEntry : this.mAllNotifications) {
                dumpEntry(printWriter, "  ", i, notificationEntry);
                i++;
            }
        }
        printWriter.print("  mPendingNotifications=");
        if (this.mPendingNotifications.size() == 0) {
            printWriter.println("null");
        } else {
            for (NotificationEntry notificationEntry2 : this.mPendingNotifications.values()) {
                printWriter.println(notificationEntry2.getSbn());
            }
        }
        printWriter.println("  Remove interceptors registered:");
        Iterator<NotificationRemoveInterceptor> it = this.mRemoveInterceptors.iterator();
        while (it.hasNext()) {
            printWriter.println("    " + it.next().getClass().getSimpleName());
        }
        printWriter.println("  Lifetime extenders registered:");
        Iterator<NotificationLifetimeExtender> it2 = this.mNotificationLifetimeExtenders.iterator();
        while (it2.hasNext()) {
            printWriter.println("    " + it2.next().getClass().getSimpleName());
        }
        printWriter.println("  Lifetime-extended notifications:");
        if (this.mRetainedNotifications.isEmpty()) {
            printWriter.println("    None");
            return;
        }
        for (Map.Entry<NotificationEntry, NotificationLifetimeExtender> entry : this.mRetainedNotifications.entrySet()) {
            printWriter.println("    " + entry.getKey().getSbn() + " retained by " + entry.getValue().getClass().getName());
        }
    }

    public NotificationEntryManager(NotificationEntryManagerLogger notificationEntryManagerLogger, NotificationGroupManagerLegacy notificationGroupManagerLegacy, FeatureFlags featureFlags, Lazy<NotificationRowBinder> lazy, Lazy<NotificationRemoteInputManager> lazy2, LeakDetector leakDetector, ForegroundServiceDismissalFeatureController foregroundServiceDismissalFeatureController, IStatusBarService iStatusBarService) {
        ArraySet arraySet = new ArraySet();
        this.mAllNotifications = arraySet;
        this.mReadOnlyAllNotifications = Collections.unmodifiableSet(arraySet);
        ArrayList<NotificationEntry> arrayList = new ArrayList<>();
        this.mSortedAndFiltered = arrayList;
        this.mReadOnlyNotifications = Collections.unmodifiableList(arrayList);
        this.mLogger = notificationEntryManagerLogger;
        this.mGroupManager = notificationGroupManagerLegacy;
        this.mFeatureFlags = featureFlags;
        this.mNotificationRowBinderLazy = lazy;
        this.mRemoteInputManagerLazy = lazy2;
        this.mLeakDetector = leakDetector;
        this.mFgsFeatureController = foregroundServiceDismissalFeatureController;
        this.mStatusBarService = iStatusBarService;
    }

    public void attach(NotificationListener notificationListener) {
        notificationListener.addNotificationHandler(this.mNotifListener);
    }

    public void setRanker(LegacyNotificationRanker legacyNotificationRanker) {
        this.mRanker = legacyNotificationRanker;
    }

    public void addNotificationEntryListener(NotificationEntryListener notificationEntryListener) {
        this.mNotificationEntryListeners.add(notificationEntryListener);
    }

    public void removeNotificationEntryListener(NotificationEntryListener notificationEntryListener) {
        this.mNotificationEntryListeners.remove(notificationEntryListener);
    }

    public void addNotificationRemoveInterceptor(NotificationRemoveInterceptor notificationRemoveInterceptor) {
        this.mRemoveInterceptors.add(notificationRemoveInterceptor);
    }

    public void setUpWithPresenter(NotificationPresenter notificationPresenter) {
        this.mPresenter = notificationPresenter;
    }

    public void addNotificationLifetimeExtenders(List<NotificationLifetimeExtender> list) {
        for (NotificationLifetimeExtender notificationLifetimeExtender : list) {
            addNotificationLifetimeExtender(notificationLifetimeExtender);
        }
    }

    public void addNotificationLifetimeExtender(NotificationLifetimeExtender notificationLifetimeExtender) {
        this.mNotificationLifetimeExtenders.add(notificationLifetimeExtender);
        notificationLifetimeExtender.setCallback(new NotificationLifetimeExtender.NotificationSafeToRemoveCallback() { // from class: com.android.systemui.statusbar.notification.NotificationEntryManager$$ExternalSyntheticLambda0
            @Override // com.android.systemui.statusbar.NotificationLifetimeExtender.NotificationSafeToRemoveCallback
            public final void onSafeToRemove(String str) {
                NotificationEntryManager.this.lambda$addNotificationLifetimeExtender$0(str);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addNotificationLifetimeExtender$0(String str) {
        removeNotification(str, this.mLatestRankingMap, 0);
    }

    @Override // com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager.Callback
    public void onChangeAllowed() {
        updateNotifications("reordering is now allowed");
    }

    public void performRemoveNotification(StatusBarNotification statusBarNotification, DismissedByUserStats dismissedByUserStats, int i) {
        removeNotificationInternal(statusBarNotification.getKey(), null, dismissedByUserStats.notificationVisibility, false, dismissedByUserStats, i);
    }

    private NotificationVisibility obtainVisibility(String str) {
        NotificationEntry notificationEntry = this.mActiveNotifications.get(str);
        return NotificationVisibility.obtain(str, notificationEntry != null ? notificationEntry.getRanking().getRank() : 0, this.mActiveNotifications.size(), true, NotificationLogger.getNotificationLocation(getActiveNotificationUnfiltered(str)));
    }

    private void abortExistingInflation(String str, String str2) {
        if (this.mPendingNotifications.containsKey(str)) {
            NotificationEntry notificationEntry = this.mPendingNotifications.get(str);
            notificationEntry.abortTask();
            this.mPendingNotifications.remove(str);
            for (NotifCollectionListener notifCollectionListener : this.mNotifCollectionListeners) {
                notifCollectionListener.onEntryCleanUp(notificationEntry);
            }
            this.mLogger.logInflationAborted(str, "pending", str2);
        }
        NotificationEntry activeNotificationUnfiltered = getActiveNotificationUnfiltered(str);
        if (activeNotificationUnfiltered != null) {
            activeNotificationUnfiltered.abortTask();
            this.mLogger.logInflationAborted(str, "active", str2);
        }
    }

    /* access modifiers changed from: private */
    public void handleInflationException(StatusBarNotification statusBarNotification, Exception exc) {
        removeNotificationInternal(statusBarNotification.getKey(), null, null, true, null, 4);
        for (NotificationEntryListener notificationEntryListener : this.mNotificationEntryListeners) {
            notificationEntryListener.onInflationError(statusBarNotification, exc);
        }
    }

    /* access modifiers changed from: private */
    public void addActiveNotification(NotificationEntry notificationEntry) {
        Assert.isMainThread();
        this.mActiveNotifications.put(notificationEntry.getKey(), notificationEntry);
        this.mGroupManager.onEntryAdded(notificationEntry);
        updateRankingAndSort(this.mRanker.getRankingMap(), "addEntryInternalInternal");
    }

    @VisibleForTesting
    public void addActiveNotificationForTest(NotificationEntry notificationEntry) {
        this.mActiveNotifications.put(notificationEntry.getKey(), notificationEntry);
        this.mGroupManager.onEntryAdded(notificationEntry);
        reapplyFilterAndSort("addVisibleNotification");
    }

    @VisibleForTesting
    protected void removeNotification(String str, NotificationListenerService.RankingMap rankingMap, int i) {
        removeNotificationInternal(str, rankingMap, obtainVisibility(str), false, null, i);
    }

    private void removeNotificationInternal(String str, NotificationListenerService.RankingMap rankingMap, NotificationVisibility notificationVisibility, boolean z, DismissedByUserStats dismissedByUserStats, int i) {
        boolean z2;
        NotificationEntry activeNotificationUnfiltered = getActiveNotificationUnfiltered(str);
        for (NotificationRemoveInterceptor notificationRemoveInterceptor : this.mRemoveInterceptors) {
            if (notificationRemoveInterceptor.onNotificationRemoveRequested(str, activeNotificationUnfiltered, i)) {
                this.mLogger.logRemovalIntercepted(str);
                return;
            }
        }
        boolean z3 = true;
        boolean z4 = false;
        if (activeNotificationUnfiltered == null) {
            NotificationEntry notificationEntry = this.mPendingNotifications.get(str);
            if (notificationEntry != null) {
                Iterator<NotificationLifetimeExtender> it = this.mNotificationLifetimeExtenders.iterator();
                while (it.hasNext()) {
                    NotificationLifetimeExtender next = it.next();
                    if (next.shouldExtendLifetimeForPendingNotification(notificationEntry)) {
                        extendLifetime(notificationEntry, next);
                        this.mLogger.logLifetimeExtended(str, next.getClass().getName(), "pending");
                        z4 = true;
                    }
                }
                if (!z4) {
                    abortExistingInflation(str, "removeNotification");
                    this.mAllNotifications.remove(notificationEntry);
                    this.mLeakDetector.trackGarbage(notificationEntry);
                    return;
                }
                return;
            }
            return;
        }
        boolean isRowDismissed = activeNotificationUnfiltered.isRowDismissed();
        if (!z && !isRowDismissed) {
            Iterator<NotificationLifetimeExtender> it2 = this.mNotificationLifetimeExtenders.iterator();
            while (it2.hasNext()) {
                NotificationLifetimeExtender next2 = it2.next();
                if (next2.shouldExtendLifetime(activeNotificationUnfiltered)) {
                    this.mLatestRankingMap = rankingMap;
                    extendLifetime(activeNotificationUnfiltered, next2);
                    this.mLogger.logLifetimeExtended(str, next2.getClass().getName(), "active");
                    z2 = true;
                    break;
                }
            }
        }
        z2 = false;
        if (!z2) {
            abortExistingInflation(str, "removeNotification");
            this.mAllNotifications.remove(activeNotificationUnfiltered);
            cancelLifetimeExtension(activeNotificationUnfiltered);
            if (activeNotificationUnfiltered.rowExists()) {
                activeNotificationUnfiltered.removeRow();
            }
            handleGroupSummaryRemoved(str);
            removeVisibleNotification(str);
            updateNotifications("removeNotificationInternal");
            if (dismissedByUserStats == null) {
                z3 = false;
            }
            this.mLogger.logNotifRemoved(activeNotificationUnfiltered.getKey(), z3);
            if (z3 && notificationVisibility != null) {
                sendNotificationRemovalToServer(activeNotificationUnfiltered.getSbn(), dismissedByUserStats);
            }
            for (NotificationEntryListener notificationEntryListener : this.mNotificationEntryListeners) {
                notificationEntryListener.onEntryRemoved(activeNotificationUnfiltered, notificationVisibility, z3, i);
            }
            for (NotifCollectionListener notifCollectionListener : this.mNotifCollectionListeners) {
                notifCollectionListener.onEntryRemoved(activeNotificationUnfiltered, 0);
            }
            for (NotifCollectionListener notifCollectionListener2 : this.mNotifCollectionListeners) {
                notifCollectionListener2.onEntryCleanUp(activeNotificationUnfiltered);
            }
            this.mLeakDetector.trackGarbage(activeNotificationUnfiltered);
        }
    }

    private void sendNotificationRemovalToServer(StatusBarNotification statusBarNotification, DismissedByUserStats dismissedByUserStats) {
        try {
            this.mStatusBarService.onNotificationClear(statusBarNotification.getPackageName(), statusBarNotification.getUser().getIdentifier(), statusBarNotification.getKey(), dismissedByUserStats.dismissalSurface, dismissedByUserStats.dismissalSentiment, dismissedByUserStats.notificationVisibility);
        } catch (RemoteException unused) {
        }
    }

    private void handleGroupSummaryRemoved(String str) {
        List<NotificationEntry> attachedNotifChildren;
        NotificationEntry activeNotificationUnfiltered = getActiveNotificationUnfiltered(str);
        if (activeNotificationUnfiltered != null && activeNotificationUnfiltered.rowExists() && activeNotificationUnfiltered.isSummaryWithChildren()) {
            if ((activeNotificationUnfiltered.getSbn().getOverrideGroupKey() == null || activeNotificationUnfiltered.isRowDismissed()) && (attachedNotifChildren = activeNotificationUnfiltered.getAttachedNotifChildren()) != null) {
                for (int i = 0; i < attachedNotifChildren.size(); i++) {
                    NotificationEntry notificationEntry = attachedNotifChildren.get(i);
                    boolean z = (activeNotificationUnfiltered.getSbn().getNotification().flags & 64) != 0;
                    boolean z2 = this.mRemoteInputManagerLazy.get().shouldKeepForRemoteInputHistory(notificationEntry) || this.mRemoteInputManagerLazy.get().shouldKeepForSmartReplyHistory(notificationEntry);
                    if (!z && !z2) {
                        notificationEntry.setKeepInParent(true);
                        notificationEntry.removeRow();
                    }
                }
            }
        }
    }

    private void addNotificationInternal(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) throws InflationException {
        String key = statusBarNotification.getKey();
        if (DEBUG) {
            Log.d("NotificationEntryMgr", "addNotification key=" + key);
        }
        updateRankingAndSort(rankingMap, "addNotificationInternal");
        NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
        rankingMap.getRanking(key, ranking);
        NotificationEntry notificationEntry = this.mPendingNotifications.get(key);
        if (notificationEntry != null) {
            notificationEntry.setSbn(statusBarNotification);
            notificationEntry.setRanking(ranking);
        } else {
            notificationEntry = new NotificationEntry(statusBarNotification, ranking, this.mFgsFeatureController.isForegroundServiceDismissalEnabled(), SystemClock.uptimeMillis());
            this.mAllNotifications.add(notificationEntry);
            this.mLeakDetector.trackInstance(notificationEntry);
            for (NotifCollectionListener notifCollectionListener : this.mNotifCollectionListeners) {
                notifCollectionListener.onEntryInit(notificationEntry);
            }
        }
        for (NotifCollectionListener notifCollectionListener2 : this.mNotifCollectionListeners) {
            notifCollectionListener2.onEntryBind(notificationEntry, statusBarNotification);
        }
        if (!this.mFeatureFlags.isNewNotifPipelineRenderingEnabled()) {
            this.mNotificationRowBinderLazy.get().inflateViews(notificationEntry, this.mInflationCallback);
        }
        this.mPendingNotifications.put(key, notificationEntry);
        this.mLogger.logNotifAdded(notificationEntry.getKey());
        for (NotificationEntryListener notificationEntryListener : this.mNotificationEntryListeners) {
            notificationEntryListener.onPendingEntryAdded(notificationEntry);
        }
        for (NotifCollectionListener notifCollectionListener3 : this.mNotifCollectionListeners) {
            notifCollectionListener3.onEntryAdded(notificationEntry);
        }
        for (NotifCollectionListener notifCollectionListener4 : this.mNotifCollectionListeners) {
            notifCollectionListener4.onRankingApplied();
        }
    }

    public void addNotification(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        try {
            addNotificationInternal(statusBarNotification, rankingMap);
        } catch (InflationException e) {
            handleInflationException(statusBarNotification, e);
        }
    }

    private void updateNotificationInternal(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) throws InflationException {
        if (DEBUG) {
            Log.d("NotificationEntryMgr", "updateNotification(" + statusBarNotification + ")");
        }
        String key = statusBarNotification.getKey();
        abortExistingInflation(key, "updateNotification");
        NotificationEntry activeNotificationUnfiltered = getActiveNotificationUnfiltered(key);
        if (activeNotificationUnfiltered != null) {
            cancelLifetimeExtension(activeNotificationUnfiltered);
            updateRankingAndSort(rankingMap, "updateNotificationInternal");
            StatusBarNotification sbn = activeNotificationUnfiltered.getSbn();
            activeNotificationUnfiltered.setSbn(statusBarNotification);
            for (NotifCollectionListener notifCollectionListener : this.mNotifCollectionListeners) {
                notifCollectionListener.onEntryBind(activeNotificationUnfiltered, statusBarNotification);
            }
            this.mGroupManager.onEntryUpdated(activeNotificationUnfiltered, sbn);
            this.mLogger.logNotifUpdated(activeNotificationUnfiltered.getKey());
            for (NotificationEntryListener notificationEntryListener : this.mNotificationEntryListeners) {
                notificationEntryListener.onPreEntryUpdated(activeNotificationUnfiltered);
            }
            for (NotifCollectionListener notifCollectionListener2 : this.mNotifCollectionListeners) {
                notifCollectionListener2.onEntryUpdated(activeNotificationUnfiltered);
            }
            if (!this.mFeatureFlags.isNewNotifPipelineRenderingEnabled()) {
                this.mNotificationRowBinderLazy.get().inflateViews(activeNotificationUnfiltered, this.mInflationCallback);
            }
            updateNotifications("updateNotificationInternal");
            for (NotificationEntryListener notificationEntryListener2 : this.mNotificationEntryListeners) {
                notificationEntryListener2.onPostEntryUpdated(activeNotificationUnfiltered);
            }
            for (NotifCollectionListener notifCollectionListener3 : this.mNotifCollectionListeners) {
                notifCollectionListener3.onRankingApplied();
            }
        }
    }

    public void updateNotification(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        try {
            updateNotificationInternal(statusBarNotification, rankingMap);
        } catch (InflationException e) {
            handleInflationException(statusBarNotification, e);
        }
    }

    public void updateNotifications(String str) {
        reapplyFilterAndSort(str);
        if (this.mPresenter != null && !this.mFeatureFlags.isNewNotifPipelineRenderingEnabled()) {
            this.mPresenter.updateNotificationViews(str);
        }
    }

    public void updateNotificationRanking(NotificationListenerService.RankingMap rankingMap) {
        ArrayList<NotificationEntry> arrayList = new ArrayList();
        arrayList.addAll(getVisibleNotifications());
        arrayList.addAll(this.mPendingNotifications.values());
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        for (NotificationEntry notificationEntry : arrayList) {
            arrayMap.put(notificationEntry.getKey(), NotificationUiAdjustment.extractFromNotificationEntry(notificationEntry));
            arrayMap2.put(notificationEntry.getKey(), Integer.valueOf(notificationEntry.getImportance()));
        }
        updateRankingAndSort(rankingMap, "updateNotificationRanking");
        updateRankingOfPendingNotifications(rankingMap);
        for (NotificationEntry notificationEntry2 : arrayList) {
            this.mNotificationRowBinderLazy.get().onNotificationRankingUpdated(notificationEntry2, (Integer) arrayMap2.get(notificationEntry2.getKey()), (NotificationUiAdjustment) arrayMap.get(notificationEntry2.getKey()), NotificationUiAdjustment.extractFromNotificationEntry(notificationEntry2), this.mInflationCallback);
        }
        updateNotifications("updateNotificationRanking");
        for (NotificationEntryListener notificationEntryListener : this.mNotificationEntryListeners) {
            notificationEntryListener.onNotificationRankingUpdated(rankingMap);
        }
        for (NotifCollectionListener notifCollectionListener : this.mNotifCollectionListeners) {
            notifCollectionListener.onRankingUpdate(rankingMap);
        }
        for (NotifCollectionListener notifCollectionListener2 : this.mNotifCollectionListeners) {
            notifCollectionListener2.onRankingApplied();
        }
    }

    private void updateRankingOfPendingNotifications(NotificationListenerService.RankingMap rankingMap) {
        if (rankingMap != null) {
            for (NotificationEntry notificationEntry : this.mPendingNotifications.values()) {
                NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
                if (rankingMap.getRanking(notificationEntry.getKey(), ranking)) {
                    notificationEntry.setRanking(ranking);
                }
            }
        }
    }

    public Iterable<NotificationEntry> getPendingNotificationsIterator() {
        return this.mPendingNotifications.values();
    }

    public NotificationEntry getActiveNotificationUnfiltered(String str) {
        return this.mActiveNotifications.get(str);
    }

    public NotificationEntry getPendingOrActiveNotif(String str) {
        if (this.mPendingNotifications.containsKey(str)) {
            return this.mPendingNotifications.get(str);
        }
        return this.mActiveNotifications.get(str);
    }

    private void extendLifetime(NotificationEntry notificationEntry, NotificationLifetimeExtender notificationLifetimeExtender) {
        NotificationLifetimeExtender notificationLifetimeExtender2 = this.mRetainedNotifications.get(notificationEntry);
        if (!(notificationLifetimeExtender2 == null || notificationLifetimeExtender2 == notificationLifetimeExtender)) {
            notificationLifetimeExtender2.setShouldManageLifetime(notificationEntry, false);
        }
        this.mRetainedNotifications.put(notificationEntry, notificationLifetimeExtender);
        notificationLifetimeExtender.setShouldManageLifetime(notificationEntry, true);
    }

    private void cancelLifetimeExtension(NotificationEntry notificationEntry) {
        NotificationLifetimeExtender remove = this.mRetainedNotifications.remove(notificationEntry);
        if (remove != null) {
            remove.setShouldManageLifetime(notificationEntry, false);
        }
    }

    private void removeVisibleNotification(String str) {
        Assert.isMainThread();
        NotificationEntry remove = this.mActiveNotifications.remove(str);
        if (remove != null) {
            this.mGroupManager.onEntryRemoved(remove);
        }
    }

    public List<NotificationEntry> getActiveNotificationsForCurrentUser() {
        Assert.isMainThread();
        ArrayList arrayList = new ArrayList();
        int size = this.mActiveNotifications.size();
        for (int i = 0; i < size; i++) {
            NotificationEntry valueAt = this.mActiveNotifications.valueAt(i);
            if (this.mRanker.isNotificationForCurrentProfiles(valueAt)) {
                arrayList.add(valueAt);
            }
        }
        return arrayList;
    }

    public void reapplyFilterAndSort(String str) {
        updateRankingAndSort(this.mRanker.getRankingMap(), str);
    }

    private void updateRankingAndSort(NotificationListenerService.RankingMap rankingMap, String str) {
        this.mSortedAndFiltered.clear();
        this.mSortedAndFiltered.addAll(this.mRanker.updateRanking(rankingMap, this.mActiveNotifications.values(), str));
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println("NotificationEntryManager");
        int size = this.mSortedAndFiltered.size();
        printWriter.print(str);
        printWriter.println("active notifications: " + size);
        int i = 0;
        while (i < size) {
            dumpEntry(printWriter, str, i, this.mSortedAndFiltered.get(i));
            i++;
        }
        synchronized (this.mActiveNotifications) {
            int size2 = this.mActiveNotifications.size();
            printWriter.print(str);
            printWriter.println("inactive notifications: " + (size2 - i));
            int i2 = 0;
            for (int i3 = 0; i3 < size2; i3++) {
                NotificationEntry valueAt = this.mActiveNotifications.valueAt(i3);
                if (!this.mSortedAndFiltered.contains(valueAt)) {
                    dumpEntry(printWriter, str, i2, valueAt);
                    i2++;
                }
            }
        }
    }

    private void dumpEntry(PrintWriter printWriter, String str, int i, NotificationEntry notificationEntry) {
        printWriter.print(str);
        printWriter.println("  [" + i + "] key=" + notificationEntry.getKey() + " icon=" + notificationEntry.getIcons().getStatusBarIcon());
        StatusBarNotification sbn = notificationEntry.getSbn();
        printWriter.print(str);
        printWriter.println("      pkg=" + sbn.getPackageName() + " id=" + sbn.getId() + " importance=" + notificationEntry.getRanking().getImportance());
        printWriter.print(str);
        StringBuilder sb = new StringBuilder();
        sb.append("      notification=");
        sb.append(sbn.getNotification());
        printWriter.println(sb.toString());
    }

    public List<NotificationEntry> getVisibleNotifications() {
        return this.mReadOnlyNotifications;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection
    public Collection<NotificationEntry> getAllNotifs() {
        return this.mReadOnlyAllNotifications;
    }

    public int getActiveNotificationsCount() {
        return this.mReadOnlyNotifications.size();
    }

    public boolean hasActiveNotifications() {
        return this.mReadOnlyNotifications.size() != 0;
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection
    public void addCollectionListener(NotifCollectionListener notifCollectionListener) {
        this.mNotifCollectionListeners.add(notifCollectionListener);
    }
}
