package com.android.systemui.people.widget;

import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.app.backup.BackupManager;
import android.app.job.JobScheduler;
import android.app.people.ConversationChannel;
import android.app.people.IPeopleManager;
import android.app.people.PeopleManager;
import android.app.people.PeopleSpaceTile;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.PreferenceManager;
import android.service.notification.ConversationChannelWrapper;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.people.NotificationHelper;
import com.android.systemui.people.PeopleSpaceUtils;
import com.android.systemui.people.PeopleTileViewHelper;
import com.android.systemui.people.SharedPreferencesHelper;
import com.android.systemui.people.widget.PeopleBackupHelper;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.wm.shell.bubbles.Bubbles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/* loaded from: classes.dex */
public class PeopleSpaceWidgetManager {
    @GuardedBy({"mLock"})
    public static Map<PeopleTileKey, TileConversationListener> mListeners = new HashMap();
    @GuardedBy({"mLock"})
    public static Map<Integer, PeopleSpaceTile> mTiles = new HashMap();
    private AppWidgetManager mAppWidgetManager;
    private BackupManager mBackupManager;
    protected final BroadcastReceiver mBaseBroadcastReceiver;
    private Executor mBgExecutor;
    private BroadcastDispatcher mBroadcastDispatcher;
    private Optional<Bubbles> mBubblesOptional;
    private final Context mContext;
    private INotificationManager mINotificationManager;
    private IPeopleManager mIPeopleManager;
    private LauncherApps mLauncherApps;
    private final NotificationListener.NotificationHandler mListener;
    private final Object mLock;
    private PeopleSpaceWidgetManager mManager;
    private NotificationEntryManager mNotificationEntryManager;
    @GuardedBy({"mLock"})
    private Map<String, Set<String>> mNotificationKeyToWidgetIdsMatchedByUri;
    private NotificationManager mNotificationManager;
    private PackageManager mPackageManager;
    private PeopleManager mPeopleManager;
    private boolean mRegisteredReceivers;
    private SharedPreferences mSharedPrefs;
    public UiEventLogger mUiEventLogger;
    private UserManager mUserManager;

    public PeopleSpaceWidgetManager(Context context, LauncherApps launcherApps, NotificationEntryManager notificationEntryManager, PackageManager packageManager, Optional<Bubbles> optional, UserManager userManager, NotificationManager notificationManager, BroadcastDispatcher broadcastDispatcher, Executor executor) {
        this.mLock = new Object();
        this.mUiEventLogger = new UiEventLoggerImpl();
        this.mNotificationKeyToWidgetIdsMatchedByUri = new HashMap();
        this.mListener = new NotificationListener.NotificationHandler() { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager.1
            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
            }

            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public void onNotificationsInitialized() {
            }

            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
                PeopleSpaceWidgetManager.this.updateWidgetsWithNotificationChanged(statusBarNotification, PeopleSpaceUtils.NotificationAction.POSTED);
            }

            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
                PeopleSpaceWidgetManager.this.updateWidgetsWithNotificationChanged(statusBarNotification, PeopleSpaceUtils.NotificationAction.REMOVED);
            }

            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
                if (notificationChannel.isConversation()) {
                    PeopleSpaceWidgetManager peopleSpaceWidgetManager = PeopleSpaceWidgetManager.this;
                    peopleSpaceWidgetManager.updateWidgets(peopleSpaceWidgetManager.mAppWidgetManager.getAppWidgetIds(new ComponentName(PeopleSpaceWidgetManager.this.mContext, PeopleSpaceWidgetProvider.class)));
                }
            }
        };
        this.mBaseBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager.2
            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onReceive$0(Intent intent) {
                PeopleSpaceWidgetManager.this.updateWidgetsFromBroadcastInBackground(intent.getAction());
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                PeopleSpaceWidgetManager.this.mBgExecutor.execute(new PeopleSpaceWidgetManager$2$$ExternalSyntheticLambda0(this, intent));
            }
        };
        this.mContext = context;
        this.mAppWidgetManager = AppWidgetManager.getInstance(context);
        this.mIPeopleManager = IPeopleManager.Stub.asInterface(ServiceManager.getService("people"));
        this.mLauncherApps = launcherApps;
        this.mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.mPeopleManager = (PeopleManager) context.getSystemService(PeopleManager.class);
        this.mNotificationEntryManager = notificationEntryManager;
        this.mPackageManager = packageManager;
        this.mINotificationManager = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
        this.mBubblesOptional = optional;
        this.mUserManager = userManager;
        this.mBackupManager = new BackupManager(context);
        this.mNotificationManager = notificationManager;
        this.mManager = this;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mBgExecutor = executor;
    }

    public void init() {
        synchronized (this.mLock) {
            if (!this.mRegisteredReceivers) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.app.action.INTERRUPTION_FILTER_CHANGED");
                intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
                intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
                intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
                intentFilter.addAction("android.intent.action.PACKAGES_SUSPENDED");
                intentFilter.addAction("android.intent.action.PACKAGES_UNSUSPENDED");
                intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
                intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
                intentFilter.addAction("android.intent.action.USER_UNLOCKED");
                this.mBroadcastDispatcher.registerReceiver(this.mBaseBroadcastReceiver, intentFilter, null, UserHandle.ALL);
                IntentFilter intentFilter2 = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
                intentFilter2.addAction("android.intent.action.PACKAGE_ADDED");
                intentFilter2.addDataScheme("package");
                this.mContext.registerReceiver(this.mBaseBroadcastReceiver, intentFilter2);
                IntentFilter intentFilter3 = new IntentFilter("android.intent.action.BOOT_COMPLETED");
                intentFilter3.setPriority(1000);
                this.mContext.registerReceiver(this.mBaseBroadcastReceiver, intentFilter3);
                this.mRegisteredReceivers = true;
            }
        }
    }

    /* loaded from: classes.dex */
    public class TileConversationListener implements PeopleManager.ConversationListener {
        public TileConversationListener() {
        }

        public void onConversationUpdate(ConversationChannel conversationChannel) {
            PeopleSpaceWidgetManager.this.mBgExecutor.execute(new PeopleSpaceWidgetManager$TileConversationListener$$ExternalSyntheticLambda0(this, conversationChannel));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onConversationUpdate$0(ConversationChannel conversationChannel) {
            PeopleSpaceWidgetManager.this.updateWidgetsWithConversationChanged(conversationChannel);
        }
    }

    @VisibleForTesting
    PeopleSpaceWidgetManager(Context context, AppWidgetManager appWidgetManager, IPeopleManager iPeopleManager, PeopleManager peopleManager, LauncherApps launcherApps, NotificationEntryManager notificationEntryManager, PackageManager packageManager, Optional<Bubbles> optional, UserManager userManager, BackupManager backupManager, INotificationManager iNotificationManager, NotificationManager notificationManager, Executor executor) {
        this.mLock = new Object();
        this.mUiEventLogger = new UiEventLoggerImpl();
        this.mNotificationKeyToWidgetIdsMatchedByUri = new HashMap();
        this.mListener = new NotificationListener.NotificationHandler() { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager.1
            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
            }

            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public void onNotificationsInitialized() {
            }

            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
                PeopleSpaceWidgetManager.this.updateWidgetsWithNotificationChanged(statusBarNotification, PeopleSpaceUtils.NotificationAction.POSTED);
            }

            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
                PeopleSpaceWidgetManager.this.updateWidgetsWithNotificationChanged(statusBarNotification, PeopleSpaceUtils.NotificationAction.REMOVED);
            }

            @Override // com.android.systemui.statusbar.NotificationListener.NotificationHandler
            public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
                if (notificationChannel.isConversation()) {
                    PeopleSpaceWidgetManager peopleSpaceWidgetManager = PeopleSpaceWidgetManager.this;
                    peopleSpaceWidgetManager.updateWidgets(peopleSpaceWidgetManager.mAppWidgetManager.getAppWidgetIds(new ComponentName(PeopleSpaceWidgetManager.this.mContext, PeopleSpaceWidgetProvider.class)));
                }
            }
        };
        this.mBaseBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager.2
            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onReceive$0(Intent intent) {
                PeopleSpaceWidgetManager.this.updateWidgetsFromBroadcastInBackground(intent.getAction());
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                PeopleSpaceWidgetManager.this.mBgExecutor.execute(new PeopleSpaceWidgetManager$2$$ExternalSyntheticLambda0(this, intent));
            }
        };
        this.mContext = context;
        this.mAppWidgetManager = appWidgetManager;
        this.mIPeopleManager = iPeopleManager;
        this.mPeopleManager = peopleManager;
        this.mLauncherApps = launcherApps;
        this.mNotificationEntryManager = notificationEntryManager;
        this.mPackageManager = packageManager;
        this.mBubblesOptional = optional;
        this.mUserManager = userManager;
        this.mBackupManager = backupManager;
        this.mINotificationManager = iNotificationManager;
        this.mNotificationManager = notificationManager;
        this.mManager = this;
        this.mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.mBgExecutor = executor;
    }

    public void updateWidgets(int[] iArr) {
        this.mBgExecutor.execute(new Runnable(iArr) { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager$$ExternalSyntheticLambda2
            public final /* synthetic */ int[] f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                PeopleSpaceWidgetManager.this.lambda$updateWidgets$0(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    /* renamed from: updateWidgetsInBackground */
    public void lambda$updateWidgets$0(int[] iArr) {
        try {
            if (iArr.length != 0) {
                synchronized (this.mLock) {
                    updateSingleConversationWidgets(iArr);
                }
            }
        } catch (Exception e) {
            Log.e("PeopleSpaceWidgetMgr", "Exception: " + e);
        }
    }

    public void updateSingleConversationWidgets(int[] iArr) {
        HashMap hashMap = new HashMap();
        for (int i : iArr) {
            PeopleSpaceTile tileForExistingWidget = getTileForExistingWidget(i);
            if (tileForExistingWidget == null) {
                Log.e("PeopleSpaceWidgetMgr", "Matching conversation not found for shortcut ID");
            }
            lambda$addNewWidget$5(i, tileForExistingWidget);
            hashMap.put(Integer.valueOf(i), tileForExistingWidget);
            if (tileForExistingWidget != null) {
                registerConversationListenerIfNeeded(i, new PeopleTileKey(tileForExistingWidget));
            }
        }
        PeopleSpaceUtils.getDataFromContactsOnBackgroundThread(this.mContext, this.mManager, hashMap, iArr);
    }

    private void updateAppWidgetViews(int i, PeopleSpaceTile peopleSpaceTile, Bundle bundle) {
        PeopleTileKey keyFromStorageByWidgetId = getKeyFromStorageByWidgetId(i);
        if (!PeopleTileKey.isValid(keyFromStorageByWidgetId)) {
            Log.e("PeopleSpaceWidgetMgr", "Cannot update invalid widget");
            return;
        }
        this.mAppWidgetManager.updateAppWidget(i, PeopleTileViewHelper.createRemoteViews(this.mContext, peopleSpaceTile, i, bundle, keyFromStorageByWidgetId));
    }

    public void updateAppWidgetOptionsAndViewOptional(int i, Optional<PeopleSpaceTile> optional) {
        if (optional.isPresent()) {
            lambda$addNewWidget$5(i, optional.get());
        }
    }

    /* renamed from: updateAppWidgetOptionsAndView */
    public void lambda$addNewWidget$5(int i, PeopleSpaceTile peopleSpaceTile) {
        synchronized (mTiles) {
            mTiles.put(Integer.valueOf(i), peopleSpaceTile);
        }
        updateAppWidgetViews(i, peopleSpaceTile, this.mAppWidgetManager.getAppWidgetOptions(i));
    }

    public PeopleSpaceTile getTileForExistingWidget(int i) {
        try {
            return getTileForExistingWidgetThrowing(i);
        } catch (Exception e) {
            Log.e("PeopleSpaceWidgetMgr", "Failed to retrieve conversation for tile: " + e);
            return null;
        }
    }

    private PeopleSpaceTile getTileForExistingWidgetThrowing(int i) throws PackageManager.NameNotFoundException {
        PeopleSpaceTile peopleSpaceTile;
        synchronized (mTiles) {
            peopleSpaceTile = mTiles.get(Integer.valueOf(i));
        }
        if (peopleSpaceTile != null) {
            return peopleSpaceTile;
        }
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(String.valueOf(i), 0);
        return getTileFromPersistentStorage(new PeopleTileKey(sharedPreferences.getString("shortcut_id", ""), sharedPreferences.getInt("user_id", -1), sharedPreferences.getString("package_name", "")), i, true);
    }

    public PeopleSpaceTile getTileFromPersistentStorage(PeopleTileKey peopleTileKey, int i, boolean z) throws PackageManager.NameNotFoundException {
        if (!PeopleTileKey.isValid(peopleTileKey)) {
            Log.e("PeopleSpaceWidgetMgr", "PeopleTileKey invalid: " + peopleTileKey.toString());
            return null;
        }
        IPeopleManager iPeopleManager = this.mIPeopleManager;
        if (iPeopleManager == null || this.mLauncherApps == null) {
            Log.d("PeopleSpaceWidgetMgr", "System services are null");
            return null;
        }
        try {
            ConversationChannel conversation = iPeopleManager.getConversation(peopleTileKey.getPackageName(), peopleTileKey.getUserId(), peopleTileKey.getShortcutId());
            if (conversation == null) {
                return null;
            }
            PeopleSpaceTile.Builder builder = new PeopleSpaceTile.Builder(conversation, this.mLauncherApps);
            String string = this.mSharedPrefs.getString(String.valueOf(i), null);
            if (z && string != null && builder.build().getContactUri() == null) {
                builder.setContactUri(Uri.parse(string));
            }
            return getTileWithCurrentState(builder.build(), "android.intent.action.BOOT_COMPLETED");
        } catch (RemoteException e) {
            Log.e("PeopleSpaceWidgetMgr", "Could not retrieve data: " + e);
            return null;
        }
    }

    public void updateWidgetsWithNotificationChanged(StatusBarNotification statusBarNotification, PeopleSpaceUtils.NotificationAction notificationAction) {
        this.mBgExecutor.execute(new Runnable(statusBarNotification, notificationAction) { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager$$ExternalSyntheticLambda1
            public final /* synthetic */ StatusBarNotification f$1;
            public final /* synthetic */ PeopleSpaceUtils.NotificationAction f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                PeopleSpaceWidgetManager.this.lambda$updateWidgetsWithNotificationChanged$1(this.f$1, this.f$2);
            }
        });
    }

    /* access modifiers changed from: private */
    /* renamed from: updateWidgetsWithNotificationChangedInBackground */
    public void lambda$updateWidgetsWithNotificationChanged$1(StatusBarNotification statusBarNotification, PeopleSpaceUtils.NotificationAction notificationAction) {
        try {
            PeopleTileKey peopleTileKey = new PeopleTileKey(statusBarNotification.getShortcutId(), statusBarNotification.getUser().getIdentifier(), statusBarNotification.getPackageName());
            if (!PeopleTileKey.isValid(peopleTileKey)) {
                Log.d("PeopleSpaceWidgetMgr", "Sbn doesn't contain valid PeopleTileKey: " + peopleTileKey.toString());
            } else if (this.mAppWidgetManager.getAppWidgetIds(new ComponentName(this.mContext, PeopleSpaceWidgetProvider.class)).length == 0) {
                Log.d("PeopleSpaceWidgetMgr", "No app widget ids returned");
            } else {
                synchronized (this.mLock) {
                    Set<String> matchingKeyWidgetIds = getMatchingKeyWidgetIds(peopleTileKey);
                    matchingKeyWidgetIds.addAll(getMatchingUriWidgetIds(statusBarNotification, notificationAction));
                    updateWidgetIdsBasedOnNotifications(matchingKeyWidgetIds);
                }
            }
        } catch (Exception e) {
            Log.e("PeopleSpaceWidgetMgr", "Throwing exception: " + e);
        }
    }

    private void updateWidgetIdsBasedOnNotifications(Set<String> set) {
        if (!set.isEmpty()) {
            try {
                ((Map) set.stream().map(PeopleSpaceWidgetManager$$ExternalSyntheticLambda9.INSTANCE).collect(Collectors.toMap(Function.identity(), new Function(getGroupedConversationNotifications()) { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager$$ExternalSyntheticLambda4
                    public final /* synthetic */ Map f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return PeopleSpaceWidgetManager.this.lambda$updateWidgetIdsBasedOnNotifications$2(this.f$1, (Integer) obj);
                    }
                }))).forEach(new BiConsumer() { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager$$ExternalSyntheticLambda3
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        PeopleSpaceWidgetManager.this.lambda$updateWidgetIdsBasedOnNotifications$3((Integer) obj, (Optional) obj2);
                    }
                });
            } catch (Exception e) {
                Log.e("PeopleSpaceWidgetMgr", "Exception updating widgets: " + e);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Optional lambda$updateWidgetIdsBasedOnNotifications$2(Map map, Integer num) {
        return getAugmentedTileForExistingWidget(num.intValue(), map);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateWidgetIdsBasedOnNotifications$3(Integer num, Optional optional) {
        updateAppWidgetOptionsAndViewOptional(num.intValue(), optional);
    }

    public PeopleSpaceTile augmentTileFromNotificationEntryManager(PeopleSpaceTile peopleSpaceTile, Optional<Integer> optional) {
        return augmentTileFromNotifications(peopleSpaceTile, new PeopleTileKey(peopleSpaceTile), peopleSpaceTile.getContactUri() != null ? peopleSpaceTile.getContactUri().toString() : null, getGroupedConversationNotifications(), optional);
    }

    public Map<PeopleTileKey, Set<NotificationEntry>> getGroupedConversationNotifications() {
        ArrayList arrayList = new ArrayList(this.mNotificationEntryManager.getVisibleNotifications());
        for (NotificationEntry notificationEntry : this.mNotificationEntryManager.getPendingNotificationsIterator()) {
            arrayList.add(notificationEntry);
        }
        return (Map) arrayList.stream().filter(new Predicate() { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return PeopleSpaceWidgetManager.this.lambda$getGroupedConversationNotifications$4((NotificationEntry) obj);
            }
        }).collect(Collectors.groupingBy(PeopleSpaceWidgetManager$$ExternalSyntheticLambda10.INSTANCE, Collectors.mapping(Function.identity(), Collectors.toSet())));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getGroupedConversationNotifications$4(NotificationEntry notificationEntry) {
        return NotificationHelper.isValid(notificationEntry) && NotificationHelper.isMissedCallOrHasContent(notificationEntry) && !NotificationHelper.shouldFilterOut(this.mBubblesOptional, notificationEntry);
    }

    public PeopleSpaceTile augmentTileFromNotifications(PeopleSpaceTile peopleSpaceTile, PeopleTileKey peopleTileKey, String str, Map<PeopleTileKey, Set<NotificationEntry>> map, Optional<Integer> optional) {
        boolean z = this.mPackageManager.checkPermission("android.permission.READ_CONTACTS", peopleSpaceTile.getPackageName()) == 0;
        List arrayList = new ArrayList();
        if (z) {
            arrayList = PeopleSpaceUtils.getNotificationsByUri(this.mPackageManager, str, map);
            arrayList.isEmpty();
        }
        Set<NotificationEntry> set = map.get(peopleTileKey);
        if (set == null) {
            set = new HashSet<>();
        }
        if (set.isEmpty() && arrayList.isEmpty()) {
            return PeopleSpaceUtils.removeNotificationFields(peopleSpaceTile);
        }
        set.addAll(arrayList);
        return PeopleSpaceUtils.augmentTileFromNotification(this.mContext, peopleSpaceTile, peopleTileKey, NotificationHelper.getHighestPriorityNotification(set), PeopleSpaceUtils.getMessagesCount(set), optional, this.mBackupManager);
    }

    public Optional<PeopleSpaceTile> getAugmentedTileForExistingWidget(int i, Map<PeopleTileKey, Set<NotificationEntry>> map) {
        PeopleSpaceTile tileForExistingWidget = getTileForExistingWidget(i);
        if (tileForExistingWidget == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(augmentTileFromNotifications(tileForExistingWidget, new PeopleTileKey(tileForExistingWidget), this.mSharedPrefs.getString(String.valueOf(i), null), map, Optional.of(Integer.valueOf(i))));
    }

    public Set<String> getMatchingKeyWidgetIds(PeopleTileKey peopleTileKey) {
        if (!PeopleTileKey.isValid(peopleTileKey)) {
            return new HashSet();
        }
        return new HashSet(this.mSharedPrefs.getStringSet(peopleTileKey.toString(), new HashSet()));
    }

    private Set<String> getMatchingUriWidgetIds(StatusBarNotification statusBarNotification, PeopleSpaceUtils.NotificationAction notificationAction) {
        if (notificationAction.equals(PeopleSpaceUtils.NotificationAction.POSTED)) {
            Set<String> fetchMatchingUriWidgetIds = fetchMatchingUriWidgetIds(statusBarNotification);
            if (fetchMatchingUriWidgetIds != null && !fetchMatchingUriWidgetIds.isEmpty()) {
                this.mNotificationKeyToWidgetIdsMatchedByUri.put(statusBarNotification.getKey(), fetchMatchingUriWidgetIds);
                return fetchMatchingUriWidgetIds;
            }
        } else {
            Set<String> remove = this.mNotificationKeyToWidgetIdsMatchedByUri.remove(statusBarNotification.getKey());
            if (remove != null && !remove.isEmpty()) {
                return remove;
            }
        }
        return new HashSet();
    }

    private Set<String> fetchMatchingUriWidgetIds(StatusBarNotification statusBarNotification) {
        String contactUri;
        if (!NotificationHelper.shouldMatchNotificationByUri(statusBarNotification) || (contactUri = NotificationHelper.getContactUri(statusBarNotification)) == null) {
            return null;
        }
        HashSet hashSet = new HashSet(this.mSharedPrefs.getStringSet(contactUri, new HashSet()));
        if (hashSet.isEmpty()) {
            return null;
        }
        return hashSet;
    }

    public void updateWidgetsWithConversationChanged(ConversationChannel conversationChannel) {
        ShortcutInfo shortcutInfo = conversationChannel.getShortcutInfo();
        synchronized (this.mLock) {
            for (String str : getMatchingKeyWidgetIds(new PeopleTileKey(shortcutInfo.getId(), shortcutInfo.getUserId(), shortcutInfo.getPackage()))) {
                updateStorageAndViewWithConversationData(conversationChannel, Integer.parseInt(str));
            }
        }
    }

    private void updateStorageAndViewWithConversationData(ConversationChannel conversationChannel, int i) {
        PeopleSpaceTile tileForExistingWidget = getTileForExistingWidget(i);
        if (tileForExistingWidget != null) {
            PeopleSpaceTile.Builder builder = tileForExistingWidget.toBuilder();
            ShortcutInfo shortcutInfo = conversationChannel.getShortcutInfo();
            Uri uri = null;
            if (shortcutInfo.getPersons() != null && shortcutInfo.getPersons().length > 0) {
                Person person = shortcutInfo.getPersons()[0];
                if (person.getUri() != null) {
                    uri = Uri.parse(person.getUri());
                }
            }
            CharSequence label = shortcutInfo.getLabel();
            if (label != null) {
                builder.setUserName(label);
            }
            Icon convertDrawableToIcon = PeopleSpaceTile.convertDrawableToIcon(this.mLauncherApps.getShortcutIconDrawable(shortcutInfo, 0));
            if (convertDrawableToIcon != null) {
                builder.setUserIcon(convertDrawableToIcon);
            }
            NotificationChannel notificationChannel = conversationChannel.getNotificationChannel();
            if (notificationChannel != null) {
                builder.setIsImportantConversation(notificationChannel.isImportantConversation());
            }
            builder.setContactUri(uri).setStatuses(conversationChannel.getStatuses()).setLastInteractionTimestamp(conversationChannel.getLastEventTimestamp());
            lambda$addNewWidget$5(i, builder.build());
        }
    }

    public void attach(NotificationListener notificationListener) {
        notificationListener.addNotificationHandler(this.mListener);
    }

    public void onAppWidgetOptionsChanged(int i, Bundle bundle) {
        PeopleTileKey peopleTileKeyFromBundle = AppWidgetOptionsHelper.getPeopleTileKeyFromBundle(bundle);
        if (PeopleTileKey.isValid(peopleTileKeyFromBundle)) {
            AppWidgetOptionsHelper.removePeopleTileKey(this.mAppWidgetManager, i);
            addNewWidget(i, peopleTileKeyFromBundle);
        }
        updateWidgets(new int[]{i});
    }

    public void addNewWidget(int i, PeopleTileKey peopleTileKey) {
        PeopleTileKey keyFromStorageByWidgetId;
        try {
            PeopleSpaceTile tileFromPersistentStorage = getTileFromPersistentStorage(peopleTileKey, i, false);
            if (tileFromPersistentStorage != null) {
                PeopleSpaceTile augmentTileFromNotificationEntryManager = augmentTileFromNotificationEntryManager(tileFromPersistentStorage, Optional.of(Integer.valueOf(i)));
                synchronized (this.mLock) {
                    keyFromStorageByWidgetId = getKeyFromStorageByWidgetId(i);
                }
                if (PeopleTileKey.isValid(keyFromStorageByWidgetId)) {
                    deleteWidgets(new int[]{i});
                } else {
                    this.mUiEventLogger.log(PeopleSpaceUtils.PeopleSpaceWidgetEvent.PEOPLE_SPACE_WIDGET_ADDED);
                }
                synchronized (this.mLock) {
                    PeopleSpaceUtils.setSharedPreferencesStorageForTile(this.mContext, peopleTileKey, i, augmentTileFromNotificationEntryManager.getContactUri(), this.mBackupManager);
                }
                registerConversationListenerIfNeeded(i, peopleTileKey);
                try {
                    this.mLauncherApps.cacheShortcuts(augmentTileFromNotificationEntryManager.getPackageName(), Collections.singletonList(augmentTileFromNotificationEntryManager.getId()), augmentTileFromNotificationEntryManager.getUserHandle(), 2);
                } catch (Exception e) {
                    Log.w("PeopleSpaceWidgetMgr", "Exception caching shortcut:" + e);
                }
                this.mBgExecutor.execute(new Runnable(i, augmentTileFromNotificationEntryManager) { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager$$ExternalSyntheticLambda0
                    public final /* synthetic */ int f$1;
                    public final /* synthetic */ PeopleSpaceTile f$2;

                    {
                        this.f$1 = r2;
                        this.f$2 = r3;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        PeopleSpaceWidgetManager.this.lambda$addNewWidget$5(this.f$1, this.f$2);
                    }
                });
            }
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("PeopleSpaceWidgetMgr", "Cannot add widget since app was uninstalled");
        }
    }

    public void registerConversationListenerIfNeeded(int i, PeopleTileKey peopleTileKey) {
        if (PeopleTileKey.isValid(peopleTileKey)) {
            TileConversationListener tileConversationListener = new TileConversationListener();
            synchronized (mListeners) {
                if (!mListeners.containsKey(peopleTileKey)) {
                    mListeners.put(peopleTileKey, tileConversationListener);
                    this.mPeopleManager.registerConversationListener(peopleTileKey.getPackageName(), peopleTileKey.getUserId(), peopleTileKey.getShortcutId(), tileConversationListener, this.mContext.getMainExecutor());
                }
            }
        }
    }

    private PeopleTileKey getKeyFromStorageByWidgetId(int i) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(String.valueOf(i), 0);
        return new PeopleTileKey(sharedPreferences.getString("shortcut_id", ""), sharedPreferences.getInt("user_id", -1), sharedPreferences.getString("package_name", ""));
    }

    public void deleteWidgets(int[] iArr) {
        PeopleTileKey peopleTileKey;
        HashSet hashSet;
        String string;
        for (int i : iArr) {
            this.mUiEventLogger.log(PeopleSpaceUtils.PeopleSpaceWidgetEvent.PEOPLE_SPACE_WIDGET_DELETED);
            synchronized (this.mLock) {
                SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(String.valueOf(i), 0);
                peopleTileKey = new PeopleTileKey(sharedPreferences.getString("shortcut_id", null), sharedPreferences.getInt("user_id", -1), sharedPreferences.getString("package_name", null));
                if (PeopleTileKey.isValid(peopleTileKey)) {
                    hashSet = new HashSet(this.mSharedPrefs.getStringSet(peopleTileKey.toString(), new HashSet()));
                    string = this.mSharedPrefs.getString(String.valueOf(i), null);
                } else {
                    return;
                }
            }
            synchronized (this.mLock) {
                PeopleSpaceUtils.removeSharedPreferencesStorageForTile(this.mContext, peopleTileKey, i, string);
            }
            if (hashSet.contains(String.valueOf(i)) && hashSet.size() == 1) {
                unregisterConversationListener(peopleTileKey, i);
                uncacheConversationShortcut(peopleTileKey);
            }
        }
    }

    private void unregisterConversationListener(PeopleTileKey peopleTileKey, int i) {
        synchronized (mListeners) {
            TileConversationListener tileConversationListener = mListeners.get(peopleTileKey);
            if (tileConversationListener != null) {
                mListeners.remove(peopleTileKey);
                this.mPeopleManager.unregisterConversationListener(tileConversationListener);
            }
        }
    }

    private void uncacheConversationShortcut(PeopleTileKey peopleTileKey) {
        try {
            this.mLauncherApps.uncacheShortcuts(peopleTileKey.getPackageName(), Collections.singletonList(peopleTileKey.getShortcutId()), UserHandle.of(peopleTileKey.getUserId()), 2);
        } catch (Exception e) {
            Log.d("PeopleSpaceWidgetMgr", "Exception uncaching shortcut:" + e);
        }
    }

    public boolean requestPinAppWidget(ShortcutInfo shortcutInfo, Bundle bundle) {
        RemoteViews preview = getPreview(shortcutInfo.getId(), shortcutInfo.getUserHandle(), shortcutInfo.getPackage(), bundle);
        if (preview == null) {
            Log.w("PeopleSpaceWidgetMgr", "Skipping pinning widget: no tile for shortcutId: " + shortcutInfo.getId());
            return false;
        }
        Bundle bundle2 = new Bundle();
        bundle2.putParcelable("appWidgetPreview", preview);
        PendingIntent pendingIntent = PeopleSpaceWidgetPinnedReceiver.getPendingIntent(this.mContext, shortcutInfo);
        return this.mAppWidgetManager.requestPinAppWidget(new ComponentName(this.mContext, PeopleSpaceWidgetProvider.class), bundle2, pendingIntent);
    }

    public List<PeopleSpaceTile> getPriorityTiles() throws Exception {
        return PeopleSpaceUtils.getSortedTiles(this.mIPeopleManager, this.mLauncherApps, this.mUserManager, this.mINotificationManager.getConversations(true).getList().stream().filter(PeopleSpaceWidgetManager$$ExternalSyntheticLambda12.INSTANCE).map(PeopleSpaceWidgetManager$$ExternalSyntheticLambda8.INSTANCE));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getPriorityTiles$6(ConversationChannelWrapper conversationChannelWrapper) {
        return conversationChannelWrapper.getNotificationChannel() != null && conversationChannelWrapper.getNotificationChannel().isImportantConversation();
    }

    public List<PeopleSpaceTile> getRecentTiles() throws Exception {
        return PeopleSpaceUtils.getSortedTiles(this.mIPeopleManager, this.mLauncherApps, this.mUserManager, Stream.concat(this.mINotificationManager.getConversations(false).getList().stream().filter(PeopleSpaceWidgetManager$$ExternalSyntheticLambda13.INSTANCE).map(PeopleSpaceWidgetManager$$ExternalSyntheticLambda7.INSTANCE), this.mIPeopleManager.getRecentConversations().getList().stream().map(PeopleSpaceWidgetManager$$ExternalSyntheticLambda6.INSTANCE)));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getRecentTiles$8(ConversationChannelWrapper conversationChannelWrapper) {
        return conversationChannelWrapper.getNotificationChannel() == null || !conversationChannelWrapper.getNotificationChannel().isImportantConversation();
    }

    public RemoteViews getPreview(String str, UserHandle userHandle, String str2, Bundle bundle) {
        try {
            PeopleSpaceTile tile = PeopleSpaceUtils.getTile(this.mIPeopleManager.getConversation(str2, userHandle.getIdentifier(), str), this.mLauncherApps);
            if (tile == null) {
                return null;
            }
            PeopleSpaceTile augmentTileFromNotificationEntryManager = augmentTileFromNotificationEntryManager(tile, Optional.empty());
            return PeopleTileViewHelper.createRemoteViews(this.mContext, augmentTileFromNotificationEntryManager, 0, bundle, new PeopleTileKey(augmentTileFromNotificationEntryManager));
        } catch (Exception e) {
            Log.w("PeopleSpaceWidgetMgr", "Exception getting tiles: " + e);
            return null;
        }
    }

    @VisibleForTesting
    void updateWidgetsFromBroadcastInBackground(String str) {
        int[] appWidgetIds = this.mAppWidgetManager.getAppWidgetIds(new ComponentName(this.mContext, PeopleSpaceWidgetProvider.class));
        if (appWidgetIds != null) {
            for (int i : appWidgetIds) {
                try {
                    synchronized (this.mLock) {
                        PeopleSpaceTile tileForExistingWidgetThrowing = getTileForExistingWidgetThrowing(i);
                        if (tileForExistingWidgetThrowing == null) {
                            Log.e("PeopleSpaceWidgetMgr", "Matching conversation not found for shortcut ID");
                        } else {
                            lambda$addNewWidget$5(i, getTileWithCurrentState(tileForExistingWidgetThrowing, str));
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e("PeopleSpaceWidgetMgr", "Package no longer found for tile: " + e);
                    JobScheduler jobScheduler = (JobScheduler) this.mContext.getSystemService(JobScheduler.class);
                    if (jobScheduler == null || jobScheduler.getPendingJob(74823873) == null) {
                        synchronized (this.mLock) {
                            lambda$addNewWidget$5(i, null);
                            deleteWidgets(new int[]{i});
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    private PeopleSpaceTile getTileWithCurrentState(PeopleSpaceTile peopleSpaceTile, String str) throws PackageManager.NameNotFoundException {
        char c;
        PeopleSpaceTile.Builder builder = peopleSpaceTile.toBuilder();
        switch (str.hashCode()) {
            case -1238404651:
                if (str.equals("android.intent.action.MANAGED_PROFILE_UNAVAILABLE")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -1001645458:
                if (str.equals("android.intent.action.PACKAGES_SUSPENDED")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -864107122:
                if (str.equals("android.intent.action.MANAGED_PROFILE_AVAILABLE")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -19011148:
                if (str.equals("android.intent.action.LOCALE_CHANGED")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 798292259:
                if (str.equals("android.intent.action.BOOT_COMPLETED")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 833559602:
                if (str.equals("android.intent.action.USER_UNLOCKED")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 1290767157:
                if (str.equals("android.intent.action.PACKAGES_UNSUSPENDED")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 2106958107:
                if (str.equals("android.app.action.INTERRUPTION_FILTER_CHANGED")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                builder.setNotificationPolicyState(getNotificationPolicyState());
                break;
            case 1:
            case 2:
                builder.setIsPackageSuspended(getPackageSuspended(peopleSpaceTile));
                break;
            case 3:
            case 4:
            case 5:
                builder.setIsUserQuieted(getUserQuieted(peopleSpaceTile));
                break;
            case 6:
                break;
            default:
                builder.setIsUserQuieted(getUserQuieted(peopleSpaceTile)).setIsPackageSuspended(getPackageSuspended(peopleSpaceTile)).setNotificationPolicyState(getNotificationPolicyState());
                break;
        }
        return builder.build();
    }

    private boolean getPackageSuspended(PeopleSpaceTile peopleSpaceTile) throws PackageManager.NameNotFoundException {
        boolean z = !TextUtils.isEmpty(peopleSpaceTile.getPackageName()) && this.mPackageManager.isPackageSuspended(peopleSpaceTile.getPackageName());
        this.mPackageManager.getApplicationInfoAsUser(peopleSpaceTile.getPackageName(), 128, PeopleSpaceUtils.getUserId(peopleSpaceTile));
        return z;
    }

    private boolean getUserQuieted(PeopleSpaceTile peopleSpaceTile) {
        return peopleSpaceTile.getUserHandle() != null && this.mUserManager.isQuietModeEnabled(peopleSpaceTile.getUserHandle());
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0032  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0041  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getNotificationPolicyState() {
        /*
            r4 = this;
            android.app.NotificationManager r0 = r4.mNotificationManager
            android.app.NotificationManager$Policy r0 = r0.getNotificationPolicy()
            int r1 = r0.suppressedVisualEffects
            boolean r1 = android.app.NotificationManager.Policy.areAllVisualEffectsSuppressed(r1)
            r2 = 1
            if (r1 != 0) goto L_0x0010
            return r2
        L_0x0010:
            android.app.NotificationManager r4 = r4.mNotificationManager
            int r4 = r4.getCurrentInterruptionFilter()
            if (r4 == r2) goto L_0x0045
            r1 = 2
            if (r4 == r1) goto L_0x001c
            goto L_0x0044
        L_0x001c:
            boolean r4 = r0.allowConversations()
            if (r4 == 0) goto L_0x002b
            int r4 = r0.priorityConversationSenders
            if (r4 != r2) goto L_0x0027
            return r2
        L_0x0027:
            if (r4 != r1) goto L_0x002b
            r4 = 4
            goto L_0x002c
        L_0x002b:
            r4 = 0
        L_0x002c:
            boolean r3 = r0.allowMessages()
            if (r3 == 0) goto L_0x0041
            int r0 = r0.allowMessagesFrom()
            if (r0 == r2) goto L_0x003e
            if (r0 == r1) goto L_0x003b
            return r2
        L_0x003b:
            r4 = r4 | 8
            return r4
        L_0x003e:
            r4 = r4 | 16
            return r4
        L_0x0041:
            if (r4 == 0) goto L_0x0044
            return r4
        L_0x0044:
            return r1
        L_0x0045:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleSpaceWidgetManager.getNotificationPolicyState():int");
    }

    public void remapWidgets(int[] iArr, int[] iArr2) {
        HashMap hashMap = new HashMap();
        for (int i = 0; i < iArr.length; i++) {
            hashMap.put(String.valueOf(iArr[i]), String.valueOf(iArr2[i]));
        }
        remapWidgetFiles(hashMap);
        remapSharedFile(hashMap);
        remapFollowupFile(hashMap);
        int[] appWidgetIds = this.mAppWidgetManager.getAppWidgetIds(new ComponentName(this.mContext, PeopleSpaceWidgetProvider.class));
        Bundle bundle = new Bundle();
        bundle.putBoolean("appWidgetRestoreCompleted", true);
        for (int i2 : appWidgetIds) {
            this.mAppWidgetManager.updateAppWidgetOptions(i2, bundle);
        }
        updateWidgets(appWidgetIds);
    }

    public void remapWidgetFiles(Map<String, String> map) {
        HashMap hashMap = new HashMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String valueOf = String.valueOf(entry.getKey());
            String valueOf2 = String.valueOf(entry.getValue());
            if (!valueOf.equals(valueOf2)) {
                SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(valueOf, 0);
                PeopleTileKey peopleTileKey = SharedPreferencesHelper.getPeopleTileKey(sharedPreferences);
                if (PeopleTileKey.isValid(peopleTileKey)) {
                    hashMap.put(valueOf2, peopleTileKey);
                    SharedPreferencesHelper.clear(sharedPreferences);
                }
            }
        }
        for (Map.Entry entry2 : hashMap.entrySet()) {
            SharedPreferencesHelper.setPeopleTileKey(this.mContext.getSharedPreferences((String) entry2.getKey(), 0), (PeopleTileKey) entry2.getValue());
        }
    }

    public void remapSharedFile(Map<String, String> map) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        for (Map.Entry<String, ?> entry : defaultSharedPreferences.getAll().entrySet()) {
            String key = entry.getKey();
            int i = AnonymousClass3.$SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType[PeopleBackupHelper.getEntryType(entry).ordinal()];
            if (i == 1) {
                String str = map.get(key);
                if (TextUtils.isEmpty(str)) {
                    Log.w("PeopleSpaceWidgetMgr", "Key is widget id without matching new id, skipping: " + key);
                } else {
                    try {
                        edit.putString(str, (String) entry.getValue());
                    } catch (Exception unused) {
                        Log.e("PeopleSpaceWidgetMgr", "Malformed entry value: " + entry.getValue());
                    }
                    edit.remove(key);
                }
            } else if (i == 2 || i == 3) {
                try {
                    edit.putStringSet(key, getNewWidgets((Set) entry.getValue(), map));
                } catch (Exception unused2) {
                    Log.e("PeopleSpaceWidgetMgr", "Malformed entry value: " + entry.getValue());
                    edit.remove(key);
                }
            } else if (i == 4) {
                Log.e("PeopleSpaceWidgetMgr", "Key not identified:" + key);
            }
        }
        edit.apply();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.people.widget.PeopleSpaceWidgetManager$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType;

        static {
            int[] iArr = new int[PeopleBackupHelper.SharedFileEntryType.values().length];
            $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType = iArr;
            try {
                iArr[PeopleBackupHelper.SharedFileEntryType.WIDGET_ID.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType[PeopleBackupHelper.SharedFileEntryType.PEOPLE_TILE_KEY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType[PeopleBackupHelper.SharedFileEntryType.CONTACT_URI.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType[PeopleBackupHelper.SharedFileEntryType.UNKNOWN.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public void remapFollowupFile(Map<String, String> map) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("shared_follow_up", 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            String key = entry.getKey();
            try {
                edit.putStringSet(key, getNewWidgets((Set) entry.getValue(), map));
            } catch (Exception unused) {
                Log.e("PeopleSpaceWidgetMgr", "Malformed entry value: " + entry.getValue());
                edit.remove(key);
            }
        }
        edit.apply();
    }

    private Set<String> getNewWidgets(Set<String> set, Map<String, String> map) {
        Stream<String> stream = set.stream();
        Objects.requireNonNull(map);
        return (Set) stream.map(new Function(map) { // from class: com.android.systemui.people.widget.PeopleSpaceWidgetManager$$ExternalSyntheticLambda5
            public final /* synthetic */ Map f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return (String) this.f$0.get((String) obj);
            }
        }).filter(PeopleSpaceWidgetManager$$ExternalSyntheticLambda14.INSTANCE).collect(Collectors.toSet());
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getNewWidgets$11(String str) {
        return !TextUtils.isEmpty(str);
    }
}
