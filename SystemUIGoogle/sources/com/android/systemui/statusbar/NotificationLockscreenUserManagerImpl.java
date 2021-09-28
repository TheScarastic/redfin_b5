package com.android.systemui.statusbar;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class NotificationLockscreenUserManagerImpl implements Dumpable, NotificationLockscreenUserManager, StatusBarStateController.StateListener {
    private boolean mAllowLockscreenRemoteInput;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final NotificationClickNotifier mClickNotifier;
    protected final Context mContext;
    protected int mCurrentUserId;
    private final DevicePolicyManager mDevicePolicyManager;
    private final DeviceProvisionedController mDeviceProvisionedController;
    private NotificationEntryManager mEntryManager;
    protected KeyguardManager mKeyguardManager;
    private final KeyguardStateController mKeyguardStateController;
    private LockPatternUtils mLockPatternUtils;
    protected ContentObserver mLockscreenSettingsObserver;
    private final Handler mMainHandler;
    protected NotificationPresenter mPresenter;
    protected ContentObserver mSettingsObserver;
    private boolean mShowLockscreenNotifications;
    private final UserManager mUserManager;
    private final Object mLock = new Object();
    private final SparseBooleanArray mLockscreenPublicMode = new SparseBooleanArray();
    private final SparseBooleanArray mUsersWithSeperateWorkChallenge = new SparseBooleanArray();
    private final SparseBooleanArray mUsersAllowingPrivateNotifications = new SparseBooleanArray();
    private final SparseBooleanArray mUsersAllowingNotifications = new SparseBooleanArray();
    private final List<NotificationLockscreenUserManager.UserChangedListener> mListeners = new ArrayList();
    private int mState = 0;
    private List<NotificationLockscreenUserManager.KeyguardNotificationSuppressor> mKeyguardSuppressors = new ArrayList();
    protected final BroadcastReceiver mAllUsersReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED".equals(intent.getAction()) && NotificationLockscreenUserManagerImpl.this.isCurrentProfile(getSendingUserId())) {
                NotificationLockscreenUserManagerImpl.this.mUsersAllowingPrivateNotifications.clear();
                NotificationLockscreenUserManagerImpl.this.updateLockscreenNotificationSetting();
                NotificationLockscreenUserManagerImpl.this.getEntryManager().updateNotifications("ACTION_DEVICE_POLICY_MANAGER_STATE_CHANGED");
            }
        }
    };
    protected final BroadcastReceiver mBaseBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.2
        /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            char c;
            String action = intent.getAction();
            action.hashCode();
            int i = 0;
            switch (action.hashCode()) {
                case -1238404651:
                    if (action.equals("android.intent.action.MANAGED_PROFILE_UNAVAILABLE")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -864107122:
                    if (action.equals("android.intent.action.MANAGED_PROFILE_AVAILABLE")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case -598152660:
                    if (action.equals("com.android.systemui.statusbar.work_challenge_unlocked_notification_action")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 833559602:
                    if (action.equals("android.intent.action.USER_UNLOCKED")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 959232034:
                    if (action.equals("android.intent.action.USER_SWITCHED")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 1121780209:
                    if (action.equals("android.intent.action.USER_ADDED")) {
                        c = 5;
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
                case 1:
                case 5:
                    NotificationLockscreenUserManagerImpl.this.updateCurrentProfilesCache();
                    return;
                case 2:
                    IntentSender intentSender = (IntentSender) intent.getParcelableExtra("android.intent.extra.INTENT");
                    String stringExtra = intent.getStringExtra("android.intent.extra.INDEX");
                    if (intentSender != null) {
                        try {
                            NotificationLockscreenUserManagerImpl.this.mContext.startIntentSender(intentSender, null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException unused) {
                        }
                    }
                    if (stringExtra != null) {
                        NotificationEntry activeNotificationUnfiltered = NotificationLockscreenUserManagerImpl.this.getEntryManager().getActiveNotificationUnfiltered(stringExtra);
                        int activeNotificationsCount = NotificationLockscreenUserManagerImpl.this.getEntryManager().getActiveNotificationsCount();
                        if (activeNotificationUnfiltered != null) {
                            i = activeNotificationUnfiltered.getRanking().getRank();
                        }
                        NotificationLockscreenUserManagerImpl.this.mClickNotifier.onNotificationClick(stringExtra, NotificationVisibility.obtain(stringExtra, i, activeNotificationsCount, true, NotificationLogger.getNotificationLocation(activeNotificationUnfiltered)));
                        return;
                    }
                    return;
                case 3:
                    ((OverviewProxyService) Dependency.get(OverviewProxyService.class)).startConnectionToCurrentUser();
                    return;
                case 4:
                    NotificationLockscreenUserManagerImpl.this.mCurrentUserId = intent.getIntExtra("android.intent.extra.user_handle", -1);
                    NotificationLockscreenUserManagerImpl.this.updateCurrentProfilesCache();
                    Log.v("LockscreenUserManager", "userId " + NotificationLockscreenUserManagerImpl.this.mCurrentUserId + " is in the house");
                    NotificationLockscreenUserManagerImpl.this.updateLockscreenNotificationSetting();
                    NotificationLockscreenUserManagerImpl.this.updatePublicMode();
                    NotificationLockscreenUserManagerImpl.this.getEntryManager().reapplyFilterAndSort("user switched");
                    NotificationLockscreenUserManagerImpl notificationLockscreenUserManagerImpl = NotificationLockscreenUserManagerImpl.this;
                    notificationLockscreenUserManagerImpl.mPresenter.onUserSwitched(notificationLockscreenUserManagerImpl.mCurrentUserId);
                    for (NotificationLockscreenUserManager.UserChangedListener userChangedListener : NotificationLockscreenUserManagerImpl.this.mListeners) {
                        userChangedListener.onUserChanged(NotificationLockscreenUserManagerImpl.this.mCurrentUserId);
                    }
                    return;
                default:
                    return;
            }
        }
    };
    protected final SparseArray<UserInfo> mCurrentProfiles = new SparseArray<>();
    protected final SparseArray<UserInfo> mCurrentManagedProfiles = new SparseArray<>();

    public NotificationEntryManager getEntryManager() {
        if (this.mEntryManager == null) {
            this.mEntryManager = (NotificationEntryManager) Dependency.get(NotificationEntryManager.class);
        }
        return this.mEntryManager;
    }

    public NotificationLockscreenUserManagerImpl(Context context, BroadcastDispatcher broadcastDispatcher, DevicePolicyManager devicePolicyManager, UserManager userManager, NotificationClickNotifier notificationClickNotifier, KeyguardManager keyguardManager, StatusBarStateController statusBarStateController, Handler handler, DeviceProvisionedController deviceProvisionedController, KeyguardStateController keyguardStateController) {
        this.mCurrentUserId = 0;
        this.mContext = context;
        this.mMainHandler = handler;
        this.mDevicePolicyManager = devicePolicyManager;
        this.mUserManager = userManager;
        this.mCurrentUserId = ActivityManager.getCurrentUser();
        this.mClickNotifier = notificationClickNotifier;
        statusBarStateController.addCallback(this);
        this.mLockPatternUtils = new LockPatternUtils(context);
        this.mKeyguardManager = keyguardManager;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mKeyguardStateController = keyguardStateController;
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public void setUpWithPresenter(NotificationPresenter notificationPresenter) {
        this.mPresenter = notificationPresenter;
        this.mLockscreenSettingsObserver = new ContentObserver(this.mMainHandler) { // from class: com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.3
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                NotificationLockscreenUserManagerImpl.this.mUsersAllowingPrivateNotifications.clear();
                NotificationLockscreenUserManagerImpl.this.mUsersAllowingNotifications.clear();
                NotificationLockscreenUserManagerImpl.this.updateLockscreenNotificationSetting();
                NotificationLockscreenUserManagerImpl.this.getEntryManager().updateNotifications("LOCK_SCREEN_SHOW_NOTIFICATIONS, or LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS change");
            }
        };
        this.mSettingsObserver = new ContentObserver(this.mMainHandler) { // from class: com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl.4
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                NotificationLockscreenUserManagerImpl.this.updateLockscreenNotificationSetting();
                if (NotificationLockscreenUserManagerImpl.this.mDeviceProvisionedController.isDeviceProvisioned()) {
                    NotificationLockscreenUserManagerImpl.this.getEntryManager().updateNotifications("LOCK_SCREEN_ALLOW_REMOTE_INPUT or ZEN_MODE change");
                }
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("lock_screen_show_notifications"), false, this.mLockscreenSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("lock_screen_allow_private_notifications"), true, this.mLockscreenSettingsObserver, -1);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("zen_mode"), false, this.mSettingsObserver);
        this.mBroadcastDispatcher.registerReceiver(this.mAllUsersReceiver, new IntentFilter("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED"), null, UserHandle.ALL);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.intent.action.USER_ADDED");
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
        this.mBroadcastDispatcher.registerReceiver(this.mBaseBroadcastReceiver, intentFilter, null, UserHandle.ALL);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("com.android.systemui.statusbar.work_challenge_unlocked_notification_action");
        this.mContext.registerReceiver(this.mBaseBroadcastReceiver, intentFilter2, "com.android.systemui.permission.SELF", null);
        updateCurrentProfilesCache();
        this.mSettingsObserver.onChange(false);
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public boolean shouldShowLockscreenNotifications() {
        return this.mShowLockscreenNotifications;
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public boolean shouldAllowLockscreenRemoteInput() {
        return this.mAllowLockscreenRemoteInput;
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public boolean isCurrentProfile(int i) {
        boolean z;
        synchronized (this.mLock) {
            if (i != -1) {
                try {
                    if (this.mCurrentProfiles.get(i) == null) {
                        z = false;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            z = true;
        }
        return z;
    }

    private boolean shouldTemporarilyHideNotifications(int i) {
        if (i == -1) {
            i = this.mCurrentUserId;
        }
        return ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isUserInLockdown(i);
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public boolean shouldHideNotifications(int i) {
        int i2;
        return (isLockscreenPublicMode(i) && !userAllowsNotificationsInPublic(i)) || (i != (i2 = this.mCurrentUserId) && shouldHideNotifications(i2)) || shouldTemporarilyHideNotifications(i);
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public boolean shouldHideNotifications(String str) {
        if (getEntryManager() == null) {
            Log.wtf("LockscreenUserManager", "mEntryManager was null!", new Throwable());
            return true;
        }
        NotificationEntry activeNotificationUnfiltered = getEntryManager().getActiveNotificationUnfiltered(str);
        if (!isLockscreenPublicMode(this.mCurrentUserId) || activeNotificationUnfiltered == null || activeNotificationUnfiltered.getRanking().getLockscreenVisibilityOverride() != -1) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public boolean shouldShowOnKeyguard(NotificationEntry notificationEntry) {
        boolean z;
        if (getEntryManager() == null) {
            Log.wtf("LockscreenUserManager", "mEntryManager was null!", new Throwable());
            return false;
        }
        for (int i = 0; i < this.mKeyguardSuppressors.size(); i++) {
            if (this.mKeyguardSuppressors.get(i).shouldSuppressOnKeyguard(notificationEntry)) {
                return false;
            }
        }
        if (hideSilentNotificationsOnLockscreen()) {
            z = notificationEntry.getBucket() == 1 || (notificationEntry.getBucket() != 6 && notificationEntry.getImportance() >= 3);
        } else {
            z = !notificationEntry.getRanking().isAmbient();
        }
        if (!this.mShowLockscreenNotifications || !z) {
            return false;
        }
        return true;
    }

    private boolean hideSilentNotificationsOnLockscreen() {
        return ((Boolean) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                return NotificationLockscreenUserManagerImpl.m258$r8$lambda$7RpAJ9riZSs87uqgxWoXe0eqF0(NotificationLockscreenUserManagerImpl.this);
            }
        })).booleanValue();
    }

    public /* synthetic */ Boolean lambda$hideSilentNotificationsOnLockscreen$0() {
        boolean z = true;
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "lock_screen_show_silent_notifications", 1) != 0) {
            z = false;
        }
        return Boolean.valueOf(z);
    }

    private void setShowLockscreenNotifications(boolean z) {
        this.mShowLockscreenNotifications = z;
    }

    private void setLockscreenAllowRemoteInput(boolean z) {
        this.mAllowLockscreenRemoteInput = z;
    }

    protected void updateLockscreenNotificationSetting() {
        boolean z = true;
        boolean z2 = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "lock_screen_show_notifications", 1, this.mCurrentUserId) != 0;
        boolean z3 = (this.mDevicePolicyManager.getKeyguardDisabledFeatures(null, this.mCurrentUserId) & 4) == 0;
        if (!z2 || !z3) {
            z = false;
        }
        setShowLockscreenNotifications(z);
        setLockscreenAllowRemoteInput(false);
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public boolean userAllowsPrivateNotificationsInPublic(int i) {
        boolean z = true;
        if (i == -1) {
            return true;
        }
        if (this.mUsersAllowingPrivateNotifications.indexOfKey(i) >= 0) {
            return this.mUsersAllowingPrivateNotifications.get(i);
        }
        boolean z2 = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "lock_screen_allow_private_notifications", 0, i) != 0;
        boolean adminAllowsKeyguardFeature = adminAllowsKeyguardFeature(i, 8);
        if (!z2 || !adminAllowsKeyguardFeature) {
            z = false;
        }
        this.mUsersAllowingPrivateNotifications.append(i, z);
        return z;
    }

    public boolean allowsManagedPrivateNotificationsInPublic() {
        synchronized (this.mLock) {
            for (int size = this.mCurrentManagedProfiles.size() - 1; size >= 0; size--) {
                if (!userAllowsPrivateNotificationsInPublic(this.mCurrentManagedProfiles.valueAt(size).id)) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean adminAllowsKeyguardFeature(int i, int i2) {
        if (i == -1 || (this.mDevicePolicyManager.getKeyguardDisabledFeatures(null, i) & i2) == 0) {
            return true;
        }
        return false;
    }

    public void setLockscreenPublicMode(boolean z, int i) {
        this.mLockscreenPublicMode.put(i, z);
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public boolean isLockscreenPublicMode(int i) {
        if (i == -1) {
            return this.mLockscreenPublicMode.get(this.mCurrentUserId, false);
        }
        return this.mLockscreenPublicMode.get(i, false);
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public boolean needsSeparateWorkChallenge(int i) {
        return this.mUsersWithSeperateWorkChallenge.get(i, false);
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public boolean userAllowsNotificationsInPublic(int i) {
        boolean z = true;
        if (isCurrentProfile(i) && i != this.mCurrentUserId) {
            return true;
        }
        if (this.mUsersAllowingNotifications.indexOfKey(i) >= 0) {
            return this.mUsersAllowingNotifications.get(i);
        }
        boolean z2 = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "lock_screen_show_notifications", 0, i) != 0;
        boolean adminAllowsKeyguardFeature = adminAllowsKeyguardFeature(i, 4);
        boolean privateNotificationsAllowed = this.mKeyguardManager.getPrivateNotificationsAllowed();
        if (!z2 || !adminAllowsKeyguardFeature || !privateNotificationsAllowed) {
            z = false;
        }
        this.mUsersAllowingNotifications.append(i, z);
        return z;
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public boolean needsRedaction(NotificationEntry notificationEntry) {
        int userId = notificationEntry.getSbn().getUserId();
        boolean z = (!this.mCurrentManagedProfiles.contains(userId) && (userAllowsPrivateNotificationsInPublic(this.mCurrentUserId) ^ true)) || (userAllowsPrivateNotificationsInPublic(userId) ^ true);
        boolean z2 = notificationEntry.getSbn().getNotification().visibility == 0;
        if (packageHasVisibilityOverride(notificationEntry.getSbn().getKey())) {
            return true;
        }
        if (!z2 || !z) {
            return false;
        }
        return true;
    }

    private boolean packageHasVisibilityOverride(String str) {
        if (getEntryManager() == null) {
            Log.wtf("LockscreenUserManager", "mEntryManager was null!", new Throwable());
            return true;
        }
        NotificationEntry activeNotificationUnfiltered = getEntryManager().getActiveNotificationUnfiltered(str);
        if (activeNotificationUnfiltered == null || activeNotificationUnfiltered.getRanking().getLockscreenVisibilityOverride() != 0) {
            return false;
        }
        return true;
    }

    public void updateCurrentProfilesCache() {
        synchronized (this.mLock) {
            this.mCurrentProfiles.clear();
            this.mCurrentManagedProfiles.clear();
            UserManager userManager = this.mUserManager;
            if (userManager != null) {
                for (UserInfo userInfo : userManager.getProfiles(this.mCurrentUserId)) {
                    this.mCurrentProfiles.put(userInfo.id, userInfo);
                    if ("android.os.usertype.profile.MANAGED".equals(userInfo.userType)) {
                        this.mCurrentManagedProfiles.put(userInfo.id, userInfo);
                    }
                }
            }
        }
        this.mMainHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NotificationLockscreenUserManagerImpl.$r8$lambda$QLFYnJwiyrqiprtccccG441tHOU(NotificationLockscreenUserManagerImpl.this);
            }
        });
    }

    public /* synthetic */ void lambda$updateCurrentProfilesCache$1() {
        for (NotificationLockscreenUserManager.UserChangedListener userChangedListener : this.mListeners) {
            userChangedListener.onCurrentProfilesChanged(this.mCurrentProfiles);
        }
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public boolean isAnyProfilePublicMode() {
        synchronized (this.mLock) {
            for (int size = this.mCurrentProfiles.size() - 1; size >= 0; size--) {
                if (isLockscreenPublicMode(this.mCurrentProfiles.valueAt(size).id)) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isAnyManagedProfilePublicMode() {
        synchronized (this.mLock) {
            for (int size = this.mCurrentManagedProfiles.size() - 1; size >= 0; size--) {
                if (isLockscreenPublicMode(this.mCurrentManagedProfiles.valueAt(size).id)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public int getCurrentUserId() {
        return this.mCurrentUserId;
    }

    public SparseArray<UserInfo> getCurrentProfiles() {
        return this.mCurrentProfiles;
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onStateChanged(int i) {
        this.mState = i;
        updatePublicMode();
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public void updatePublicMode() {
        boolean z;
        boolean z2 = this.mState != 0 || this.mKeyguardStateController.isShowing();
        boolean z3 = z2 && this.mKeyguardStateController.isMethodSecure();
        SparseArray<UserInfo> currentProfiles = getCurrentProfiles();
        this.mUsersWithSeperateWorkChallenge.clear();
        for (int size = currentProfiles.size() - 1; size >= 0; size--) {
            int i = currentProfiles.valueAt(size).id;
            boolean booleanValue = ((Boolean) DejankUtils.whitelistIpcs(new Supplier(i) { // from class: com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl$$ExternalSyntheticLambda2
                public final /* synthetic */ int f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.util.function.Supplier
                public final Object get() {
                    return NotificationLockscreenUserManagerImpl.m259$r8$lambda$YK0GEYM1RhYeyCM009gvxwTfzs(NotificationLockscreenUserManagerImpl.this, this.f$1);
                }
            })).booleanValue();
            if (z3 || i == getCurrentUserId() || !booleanValue || !this.mLockPatternUtils.isSecure(i)) {
                z = z3;
            } else {
                z = z2 || this.mKeyguardManager.isDeviceLocked(i);
            }
            setLockscreenPublicMode(z, i);
            this.mUsersWithSeperateWorkChallenge.put(i, booleanValue);
        }
        getEntryManager().updateNotifications("NotificationLockscreenUserManager.updatePublicMode");
    }

    public /* synthetic */ Boolean lambda$updatePublicMode$2(int i) {
        return Boolean.valueOf(this.mLockPatternUtils.isSeparateProfileChallengeEnabled(i));
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public void addUserChangedListener(NotificationLockscreenUserManager.UserChangedListener userChangedListener) {
        this.mListeners.add(userChangedListener);
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public void addKeyguardNotificationSuppressor(NotificationLockscreenUserManager.KeyguardNotificationSuppressor keyguardNotificationSuppressor) {
        this.mKeyguardSuppressors.add(keyguardNotificationSuppressor);
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager
    public void removeUserChangedListener(NotificationLockscreenUserManager.UserChangedListener userChangedListener) {
        this.mListeners.remove(userChangedListener);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("NotificationLockscreenUserManager state:");
        printWriter.print("  mCurrentUserId=");
        printWriter.println(this.mCurrentUserId);
        printWriter.print("  mShowLockscreenNotifications=");
        printWriter.println(this.mShowLockscreenNotifications);
        printWriter.print("  mAllowLockscreenRemoteInput=");
        printWriter.println(this.mAllowLockscreenRemoteInput);
        printWriter.print("  mCurrentProfiles=");
        synchronized (this.mLock) {
            for (int size = this.mCurrentProfiles.size() - 1; size >= 0; size += -1) {
                printWriter.print("" + this.mCurrentProfiles.valueAt(size).id + " ");
            }
        }
        printWriter.print("  mCurrentManagedProfiles=");
        synchronized (this.mLock) {
            for (int size2 = this.mCurrentManagedProfiles.size() - 1; size2 >= 0; size2 += -1) {
                printWriter.print("" + this.mCurrentManagedProfiles.valueAt(size2).id + " ");
            }
        }
        printWriter.println();
    }
}
