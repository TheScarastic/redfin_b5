package com.android.systemui.wmshell;

import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.ZenModeConfig;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.model.SysUiState;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.scrim.ScrimView;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoveInterceptor;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.NotificationChannelHelper;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.wm.shell.bubbles.Bubble;
import com.android.wm.shell.bubbles.BubbleEntry;
import com.android.wm.shell.bubbles.Bubbles;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
/* loaded from: classes2.dex */
public class BubblesManager implements Dumpable {
    private final IStatusBarService mBarService;
    private ScrimView mBubbleScrim;
    private final Bubbles mBubbles;
    private final List<NotifCallback> mCallbacks = new ArrayList();
    private final Context mContext;
    private final NotifPipeline mNotifPipeline;
    private final NotificationEntryManager mNotificationEntryManager;
    private final NotificationGroupManagerLegacy mNotificationGroupManager;
    private final NotificationInterruptStateProvider mNotificationInterruptStateProvider;
    private final INotificationManager mNotificationManager;
    private final NotificationShadeWindowController mNotificationShadeWindowController;
    private final ShadeController mShadeController;
    private final Executor mSysuiMainExecutor;
    private final Bubbles.SysuiProxy mSysuiProxy;

    /* loaded from: classes2.dex */
    public interface NotifCallback {
        void invalidateNotifications(String str);

        void maybeCancelSummary(NotificationEntry notificationEntry);

        void removeNotification(NotificationEntry notificationEntry, DismissedByUserStats dismissedByUserStats, int i);
    }

    public static BubblesManager create(Context context, Optional<Bubbles> optional, NotificationShadeWindowController notificationShadeWindowController, StatusBarStateController statusBarStateController, ShadeController shadeController, ConfigurationController configurationController, IStatusBarService iStatusBarService, INotificationManager iNotificationManager, NotificationInterruptStateProvider notificationInterruptStateProvider, ZenModeController zenModeController, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, SysUiState sysUiState, FeatureFlags featureFlags, DumpManager dumpManager, Executor executor) {
        if (optional.isPresent()) {
            return new BubblesManager(context, optional.get(), notificationShadeWindowController, statusBarStateController, shadeController, configurationController, iStatusBarService, iNotificationManager, notificationInterruptStateProvider, zenModeController, notificationLockscreenUserManager, notificationGroupManagerLegacy, notificationEntryManager, notifPipeline, sysUiState, featureFlags, dumpManager, executor);
        }
        return null;
    }

    @VisibleForTesting
    BubblesManager(Context context, final Bubbles bubbles, NotificationShadeWindowController notificationShadeWindowController, StatusBarStateController statusBarStateController, ShadeController shadeController, ConfigurationController configurationController, IStatusBarService iStatusBarService, INotificationManager iNotificationManager, NotificationInterruptStateProvider notificationInterruptStateProvider, ZenModeController zenModeController, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, final SysUiState sysUiState, FeatureFlags featureFlags, DumpManager dumpManager, final Executor executor) {
        this.mContext = context;
        this.mBubbles = bubbles;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mShadeController = shadeController;
        this.mNotificationManager = iNotificationManager;
        this.mNotificationInterruptStateProvider = notificationInterruptStateProvider;
        this.mNotificationGroupManager = notificationGroupManagerLegacy;
        this.mNotificationEntryManager = notificationEntryManager;
        this.mNotifPipeline = notifPipeline;
        this.mSysuiMainExecutor = executor;
        this.mBarService = iStatusBarService == null ? IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar")) : iStatusBarService;
        ScrimView scrimView = new ScrimView(context);
        this.mBubbleScrim = scrimView;
        scrimView.setImportantForAccessibility(2);
        bubbles.setBubbleScrim(this.mBubbleScrim, new BiConsumer() { // from class: com.android.systemui.wmshell.BubblesManager$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BubblesManager.this.lambda$new$0((Executor) obj, (Looper) obj2);
            }
        });
        if (featureFlags.isNewNotifPipelineRenderingEnabled()) {
            setupNotifPipeline();
        } else {
            setupNEM();
        }
        dumpManager.registerDumpable("Bubbles", this);
        statusBarStateController.addCallback(new StatusBarStateController.StateListener() { // from class: com.android.systemui.wmshell.BubblesManager.1
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onStateChanged(int i) {
                bubbles.onStatusBarStateChanged(i == 0);
            }
        });
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.wmshell.BubblesManager.2
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onConfigChanged(Configuration configuration) {
                BubblesManager.this.mBubbles.onConfigChanged(configuration);
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onUiModeChanged() {
                BubblesManager.this.mBubbles.updateForThemeChanges();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onThemeChanged() {
                BubblesManager.this.mBubbles.updateForThemeChanges();
            }
        });
        zenModeController.addCallback(new ZenModeController.Callback() { // from class: com.android.systemui.wmshell.BubblesManager.3
            @Override // com.android.systemui.statusbar.policy.ZenModeController.Callback
            public void onZenChanged(int i) {
                BubblesManager.this.mBubbles.onZenStateChanged();
            }

            @Override // com.android.systemui.statusbar.policy.ZenModeController.Callback
            public void onConfigChanged(ZenModeConfig zenModeConfig) {
                BubblesManager.this.mBubbles.onZenStateChanged();
            }
        });
        notificationLockscreenUserManager.addUserChangedListener(new NotificationLockscreenUserManager.UserChangedListener() { // from class: com.android.systemui.wmshell.BubblesManager.4
            @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager.UserChangedListener
            public void onUserChanged(int i) {
                BubblesManager.this.mBubbles.onUserChanged(i);
            }

            @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager.UserChangedListener
            public void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray) {
                BubblesManager.this.mBubbles.onCurrentProfilesChanged(sparseArray);
            }
        });
        AnonymousClass5 r1 = new Bubbles.SysuiProxy() { // from class: com.android.systemui.wmshell.BubblesManager.5
            @Override // com.android.wm.shell.bubbles.Bubbles.SysuiProxy
            public void isNotificationShadeExpand(Consumer<Boolean> consumer) {
                executor.execute(new BubblesManager$5$$ExternalSyntheticLambda11(this, consumer));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$isNotificationShadeExpand$0(Consumer consumer) {
                consumer.accept(Boolean.valueOf(BubblesManager.this.mNotificationShadeWindowController.getPanelExpanded()));
            }

            @Override // com.android.wm.shell.bubbles.Bubbles.SysuiProxy
            public void getPendingOrActiveEntry(String str, Consumer<BubbleEntry> consumer) {
                executor.execute(new BubblesManager$5$$ExternalSyntheticLambda10(this, str, consumer));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$getPendingOrActiveEntry$1(String str, Consumer consumer) {
                BubbleEntry bubbleEntry;
                NotificationEntry pendingOrActiveNotif = BubblesManager.this.mNotificationEntryManager.getPendingOrActiveNotif(str);
                if (pendingOrActiveNotif == null) {
                    bubbleEntry = null;
                } else {
                    bubbleEntry = BubblesManager.notifToBubbleEntry(pendingOrActiveNotif);
                }
                consumer.accept(bubbleEntry);
            }

            @Override // com.android.wm.shell.bubbles.Bubbles.SysuiProxy
            public void getShouldRestoredEntries(ArraySet<String> arraySet, Consumer<List<BubbleEntry>> consumer) {
                executor.execute(new BubblesManager$5$$ExternalSyntheticLambda0(this, arraySet, consumer));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$getShouldRestoredEntries$2(ArraySet arraySet, Consumer consumer) {
                ArrayList arrayList = new ArrayList();
                List<NotificationEntry> activeNotificationsForCurrentUser = BubblesManager.this.mNotificationEntryManager.getActiveNotificationsForCurrentUser();
                for (int i = 0; i < activeNotificationsForCurrentUser.size(); i++) {
                    NotificationEntry notificationEntry = activeNotificationsForCurrentUser.get(i);
                    if (arraySet.contains(notificationEntry.getKey()) && BubblesManager.this.mNotificationInterruptStateProvider.shouldBubbleUp(notificationEntry) && notificationEntry.isBubble()) {
                        arrayList.add(BubblesManager.notifToBubbleEntry(notificationEntry));
                    }
                }
                consumer.accept(arrayList);
            }

            @Override // com.android.wm.shell.bubbles.Bubbles.SysuiProxy
            public void setNotificationInterruption(String str) {
                executor.execute(new BubblesManager$5$$ExternalSyntheticLambda6(this, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$setNotificationInterruption$3(String str) {
                NotificationEntry pendingOrActiveNotif = BubblesManager.this.mNotificationEntryManager.getPendingOrActiveNotif(str);
                if (pendingOrActiveNotif != null && pendingOrActiveNotif.getImportance() >= 4) {
                    pendingOrActiveNotif.setInterruption();
                }
            }

            @Override // com.android.wm.shell.bubbles.Bubbles.SysuiProxy
            public void requestNotificationShadeTopUi(boolean z, String str) {
                executor.execute(new BubblesManager$5$$ExternalSyntheticLambda12(this, z, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$requestNotificationShadeTopUi$4(boolean z, String str) {
                BubblesManager.this.mNotificationShadeWindowController.setRequestTopUi(z, str);
            }

            @Override // com.android.wm.shell.bubbles.Bubbles.SysuiProxy
            public void notifyRemoveNotification(String str, int i) {
                executor.execute(new BubblesManager$5$$ExternalSyntheticLambda9(this, str, i));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$notifyRemoveNotification$5(String str, int i) {
                NotificationEntry pendingOrActiveNotif = BubblesManager.this.mNotificationEntryManager.getPendingOrActiveNotif(str);
                if (pendingOrActiveNotif != null) {
                    for (NotifCallback notifCallback : BubblesManager.this.mCallbacks) {
                        notifCallback.removeNotification(pendingOrActiveNotif, BubblesManager.this.getDismissedByUserStats(pendingOrActiveNotif, true), i);
                    }
                }
            }

            @Override // com.android.wm.shell.bubbles.Bubbles.SysuiProxy
            public void notifyInvalidateNotifications(String str) {
                executor.execute(new BubblesManager$5$$ExternalSyntheticLambda4(this, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$notifyInvalidateNotifications$6(String str) {
                for (NotifCallback notifCallback : BubblesManager.this.mCallbacks) {
                    notifCallback.invalidateNotifications(str);
                }
            }

            @Override // com.android.wm.shell.bubbles.Bubbles.SysuiProxy
            public void notifyMaybeCancelSummary(String str) {
                executor.execute(new BubblesManager$5$$ExternalSyntheticLambda8(this, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$notifyMaybeCancelSummary$7(String str) {
                NotificationEntry pendingOrActiveNotif = BubblesManager.this.mNotificationEntryManager.getPendingOrActiveNotif(str);
                if (pendingOrActiveNotif != null) {
                    for (NotifCallback notifCallback : BubblesManager.this.mCallbacks) {
                        notifCallback.maybeCancelSummary(pendingOrActiveNotif);
                    }
                }
            }

            @Override // com.android.wm.shell.bubbles.Bubbles.SysuiProxy
            public void removeNotificationEntry(String str) {
                executor.execute(new BubblesManager$5$$ExternalSyntheticLambda3(this, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$removeNotificationEntry$8(String str) {
                NotificationEntry pendingOrActiveNotif = BubblesManager.this.mNotificationEntryManager.getPendingOrActiveNotif(str);
                if (pendingOrActiveNotif != null) {
                    BubblesManager.this.mNotificationGroupManager.onEntryRemoved(pendingOrActiveNotif);
                }
            }

            @Override // com.android.wm.shell.bubbles.Bubbles.SysuiProxy
            public void updateNotificationBubbleButton(String str) {
                executor.execute(new BubblesManager$5$$ExternalSyntheticLambda7(this, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$updateNotificationBubbleButton$9(String str) {
                NotificationEntry pendingOrActiveNotif = BubblesManager.this.mNotificationEntryManager.getPendingOrActiveNotif(str);
                if (pendingOrActiveNotif != null && pendingOrActiveNotif.getRow() != null) {
                    pendingOrActiveNotif.getRow().updateBubbleButton();
                }
            }

            @Override // com.android.wm.shell.bubbles.Bubbles.SysuiProxy
            public void updateNotificationSuppression(String str) {
                executor.execute(new BubblesManager$5$$ExternalSyntheticLambda5(this, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$updateNotificationSuppression$10(String str) {
                NotificationEntry pendingOrActiveNotif = BubblesManager.this.mNotificationEntryManager.getPendingOrActiveNotif(str);
                if (pendingOrActiveNotif != null) {
                    BubblesManager.this.mNotificationGroupManager.updateSuppression(pendingOrActiveNotif);
                }
            }

            @Override // com.android.wm.shell.bubbles.Bubbles.SysuiProxy
            public void onStackExpandChanged(boolean z) {
                executor.execute(new BubblesManager$5$$ExternalSyntheticLambda1(this, sysUiState, z));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onStackExpandChanged$11(SysUiState sysUiState2, boolean z) {
                sysUiState2.setFlag(16384, z).commitUpdate(BubblesManager.this.mContext.getDisplayId());
            }

            @Override // com.android.wm.shell.bubbles.Bubbles.SysuiProxy
            public void onUnbubbleConversation(String str) {
                executor.execute(new BubblesManager$5$$ExternalSyntheticLambda2(this, str));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onUnbubbleConversation$12(String str) {
                NotificationEntry pendingOrActiveNotif = BubblesManager.this.mNotificationEntryManager.getPendingOrActiveNotif(str);
                if (pendingOrActiveNotif != null) {
                    BubblesManager.this.onUserChangedBubble(pendingOrActiveNotif, false);
                }
            }
        };
        this.mSysuiProxy = r1;
        bubbles.setSysuiProxy(r1);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Executor executor, Looper looper) {
        this.mBubbleScrim.setExecutor(executor, looper);
    }

    private void setupNEM() {
        this.mNotificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() { // from class: com.android.systemui.wmshell.BubblesManager.6
            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onPendingEntryAdded(NotificationEntry notificationEntry) {
                BubblesManager.this.onEntryAdded(notificationEntry);
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onPreEntryUpdated(NotificationEntry notificationEntry) {
                BubblesManager.this.onEntryUpdated(notificationEntry);
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
                BubblesManager.this.onEntryRemoved(notificationEntry);
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onNotificationRankingUpdated(NotificationListenerService.RankingMap rankingMap) {
                BubblesManager.this.onRankingUpdate(rankingMap);
            }
        });
        this.mNotificationEntryManager.addNotificationRemoveInterceptor(new NotificationRemoveInterceptor() { // from class: com.android.systemui.wmshell.BubblesManager$$ExternalSyntheticLambda0
            @Override // com.android.systemui.statusbar.NotificationRemoveInterceptor
            public final boolean onNotificationRemoveRequested(String str, NotificationEntry notificationEntry, int i) {
                return BubblesManager.this.lambda$setupNEM$1(str, notificationEntry, i);
            }
        });
        this.mNotificationGroupManager.registerGroupChangeListener(new NotificationGroupManagerLegacy.OnGroupChangeListener() { // from class: com.android.systemui.wmshell.BubblesManager.7
            @Override // com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy.OnGroupChangeListener
            public void onGroupSuppressionChanged(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, boolean z) {
                NotificationEntry notificationEntry = notificationGroup.summary;
                String groupKey = notificationEntry != null ? notificationEntry.getSbn().getGroupKey() : null;
                if (!z && groupKey != null) {
                    BubblesManager.this.mBubbles.removeSuppressedSummaryIfNecessary(groupKey, null, null);
                }
            }
        });
        addNotifCallback(new NotifCallback() { // from class: com.android.systemui.wmshell.BubblesManager.8
            @Override // com.android.systemui.wmshell.BubblesManager.NotifCallback
            public void removeNotification(NotificationEntry notificationEntry, DismissedByUserStats dismissedByUserStats, int i) {
                BubblesManager.this.mNotificationEntryManager.performRemoveNotification(notificationEntry.getSbn(), dismissedByUserStats, i);
            }

            @Override // com.android.systemui.wmshell.BubblesManager.NotifCallback
            public void invalidateNotifications(String str) {
                BubblesManager.this.mNotificationEntryManager.updateNotifications(str);
            }

            @Override // com.android.systemui.wmshell.BubblesManager.NotifCallback
            public void maybeCancelSummary(NotificationEntry notificationEntry) {
                BubblesManager.this.mBubbles.removeSuppressedSummaryIfNecessary(notificationEntry.getSbn().getGroupKey(), new BubblesManager$8$$ExternalSyntheticLambda0(this), BubblesManager.this.mSysuiMainExecutor);
                NotificationEntry logicalGroupSummary = BubblesManager.this.mNotificationGroupManager.getLogicalGroupSummary(notificationEntry);
                if (logicalGroupSummary != null) {
                    ArrayList<NotificationEntry> logicalChildren = BubblesManager.this.mNotificationGroupManager.getLogicalChildren(logicalGroupSummary.getSbn());
                    if (logicalGroupSummary.getKey().equals(notificationEntry.getKey())) {
                        return;
                    }
                    if (logicalChildren == null || logicalChildren.isEmpty()) {
                        BubblesManager.this.mNotificationEntryManager.performRemoveNotification(logicalGroupSummary.getSbn(), BubblesManager.this.getDismissedByUserStats(logicalGroupSummary, false), 0);
                    }
                }
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$maybeCancelSummary$0(String str) {
                NotificationEntry activeNotificationUnfiltered = BubblesManager.this.mNotificationEntryManager.getActiveNotificationUnfiltered(str);
                if (activeNotificationUnfiltered != null) {
                    BubblesManager.this.mNotificationEntryManager.performRemoveNotification(activeNotificationUnfiltered.getSbn(), BubblesManager.this.getDismissedByUserStats(activeNotificationUnfiltered, false), 0);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setupNEM$1(String str, NotificationEntry notificationEntry, int i) {
        boolean z = true;
        boolean z2 = i == 3;
        boolean z3 = i == 2 || i == 1;
        boolean z4 = i == 8 || i == 9;
        boolean z5 = i == 12;
        if ((notificationEntry == null || !notificationEntry.isRowDismissed() || z4) && !z2 && !z3 && !z5) {
            z = false;
        }
        if (z) {
            return handleDismissalInterception(notificationEntry);
        }
        return false;
    }

    private void setupNotifPipeline() {
        this.mNotifPipeline.addCollectionListener(new NotifCollectionListener() { // from class: com.android.systemui.wmshell.BubblesManager.9
            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryAdded(NotificationEntry notificationEntry) {
                BubblesManager.this.onEntryAdded(notificationEntry);
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryUpdated(NotificationEntry notificationEntry) {
                BubblesManager.this.onEntryUpdated(notificationEntry);
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
                BubblesManager.this.onEntryRemoved(notificationEntry);
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
                BubblesManager.this.onRankingUpdate(rankingMap);
            }
        });
    }

    void onEntryAdded(NotificationEntry notificationEntry) {
        if (this.mNotificationInterruptStateProvider.shouldBubbleUp(notificationEntry) && notificationEntry.isBubble()) {
            this.mBubbles.onEntryAdded(notifToBubbleEntry(notificationEntry));
        }
    }

    void onEntryUpdated(NotificationEntry notificationEntry) {
        this.mBubbles.onEntryUpdated(notifToBubbleEntry(notificationEntry), this.mNotificationInterruptStateProvider.shouldBubbleUp(notificationEntry));
    }

    void onEntryRemoved(NotificationEntry notificationEntry) {
        this.mBubbles.onEntryRemoved(notifToBubbleEntry(notificationEntry));
    }

    void onRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
        String[] orderedKeys = rankingMap.getOrderedKeys();
        HashMap<String, Pair<BubbleEntry, Boolean>> hashMap = new HashMap<>();
        for (String str : orderedKeys) {
            NotificationEntry pendingOrActiveNotif = this.mNotificationEntryManager.getPendingOrActiveNotif(str);
            hashMap.put(str, new Pair<>(pendingOrActiveNotif != null ? notifToBubbleEntry(pendingOrActiveNotif) : null, Boolean.valueOf(pendingOrActiveNotif != null ? this.mNotificationInterruptStateProvider.shouldBubbleUp(pendingOrActiveNotif) : false)));
        }
        this.mBubbles.onRankingUpdated(rankingMap, hashMap);
    }

    /* access modifiers changed from: private */
    public DismissedByUserStats getDismissedByUserStats(NotificationEntry notificationEntry, boolean z) {
        return new DismissedByUserStats(3, 1, NotificationVisibility.obtain(notificationEntry.getKey(), notificationEntry.getRanking().getRank(), this.mNotificationEntryManager.getActiveNotificationsCount(), z, NotificationLogger.getNotificationLocation(notificationEntry)));
    }

    public ScrimView getScrimForBubble() {
        return this.mBubbleScrim;
    }

    public boolean handleDismissalInterception(NotificationEntry notificationEntry) {
        if (notificationEntry == null) {
            return false;
        }
        List<NotificationEntry> attachedNotifChildren = notificationEntry.getAttachedNotifChildren();
        ArrayList arrayList = null;
        if (attachedNotifChildren != null) {
            arrayList = new ArrayList();
            for (int i = 0; i < attachedNotifChildren.size(); i++) {
                arrayList.add(notifToBubbleEntry(attachedNotifChildren.get(i)));
            }
        }
        return this.mBubbles.handleDismissalInterception(notifToBubbleEntry(notificationEntry), arrayList, new IntConsumer(attachedNotifChildren, notificationEntry) { // from class: com.android.systemui.wmshell.BubblesManager$$ExternalSyntheticLambda2
            public final /* synthetic */ List f$1;
            public final /* synthetic */ NotificationEntry f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.IntConsumer
            public final void accept(int i2) {
                BubblesManager.this.lambda$handleDismissalInterception$2(this.f$1, this.f$2, i2);
            }
        }, this.mSysuiMainExecutor);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleDismissalInterception$2(List list, NotificationEntry notificationEntry, int i) {
        if (i >= 0) {
            for (NotifCallback notifCallback : this.mCallbacks) {
                notifCallback.removeNotification((NotificationEntry) list.get(i), getDismissedByUserStats((NotificationEntry) list.get(i), true), 12);
            }
            return;
        }
        this.mNotificationGroupManager.onEntryRemoved(notificationEntry);
    }

    public void expandStackAndSelectBubble(NotificationEntry notificationEntry) {
        this.mBubbles.expandStackAndSelectBubble(notifToBubbleEntry(notificationEntry));
    }

    public void expandStackAndSelectBubble(Bubble bubble) {
        this.mBubbles.expandStackAndSelectBubble(bubble);
    }

    public Bubble getBubbleWithShortcutId(String str) {
        return this.mBubbles.getBubbleWithShortcutId(str);
    }

    public void addNotifCallback(NotifCallback notifCallback) {
        this.mCallbacks.add(notifCallback);
    }

    public void onUserSetImportantConversation(NotificationEntry notificationEntry) {
        if (notificationEntry.getBubbleMetadata() != null) {
            try {
                this.mBarService.onNotificationBubbleChanged(notificationEntry.getKey(), true, 2);
            } catch (RemoteException e) {
                Log.e("Bubbles", e.getMessage());
            }
            this.mShadeController.collapsePanel(true);
            if (notificationEntry.getRow() != null) {
                notificationEntry.getRow().updateBubbleButton();
            }
        }
    }

    public void onUserChangedBubble(NotificationEntry notificationEntry, boolean z) {
        NotificationChannel channel = notificationEntry.getChannel();
        String packageName = notificationEntry.getSbn().getPackageName();
        int uid = notificationEntry.getSbn().getUid();
        if (channel != null && packageName != null) {
            try {
                this.mBarService.onNotificationBubbleChanged(notificationEntry.getKey(), z, 3);
            } catch (RemoteException unused) {
            }
            NotificationChannel createConversationChannelIfNeeded = NotificationChannelHelper.createConversationChannelIfNeeded(this.mContext, this.mNotificationManager, notificationEntry, channel);
            createConversationChannelIfNeeded.setAllowBubbles(z);
            try {
                int bubblePreferenceForPackage = this.mNotificationManager.getBubblePreferenceForPackage(packageName, uid);
                if (z && bubblePreferenceForPackage == 0) {
                    this.mNotificationManager.setBubblesAllowed(packageName, uid, 2);
                }
                this.mNotificationManager.updateNotificationChannelForPackage(packageName, uid, createConversationChannelIfNeeded);
            } catch (RemoteException e) {
                Log.e("Bubbles", e.getMessage());
            }
            if (z) {
                this.mShadeController.collapsePanel(true);
                if (notificationEntry.getRow() != null) {
                    notificationEntry.getRow().updateBubbleButton();
                }
            }
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.mBubbles.dump(fileDescriptor, printWriter, strArr);
    }

    public static boolean areBubblesEnabled(Context context, UserHandle userHandle) {
        return userHandle.getIdentifier() < 0 ? Settings.Secure.getInt(context.getContentResolver(), "notification_bubbles", 0) == 1 : Settings.Secure.getIntForUser(context.getContentResolver(), "notification_bubbles", 0, userHandle.getIdentifier()) == 1;
    }

    static BubbleEntry notifToBubbleEntry(NotificationEntry notificationEntry) {
        return new BubbleEntry(notificationEntry.getSbn(), notificationEntry.getRanking(), notificationEntry.isClearable(), notificationEntry.shouldSuppressNotificationDot(), notificationEntry.shouldSuppressNotificationList(), notificationEntry.shouldSuppressPeek());
    }
}
