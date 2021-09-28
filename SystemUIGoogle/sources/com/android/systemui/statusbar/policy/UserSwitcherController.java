package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IActivityTaskManager;
import android.app.IApplicationThread;
import android.app.ProfilerInfo;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.telephony.TelephonyCallback;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManagerGlobal;
import android.widget.BaseAdapter;
import androidx.appcompat.R$styleable;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.R$string;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.GuestResumeSessionReceiver;
import com.android.systemui.Prefs;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.SystemUISecondaryUserService;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.DetailAdapter;
import com.android.systemui.qs.QSUserSwitcherEvent;
import com.android.systemui.qs.tiles.UserDetailView;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.user.CreateUserActivity;
import com.android.systemui.util.settings.SecureSettings;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import javax.inject.Provider;
/* loaded from: classes.dex */
public class UserSwitcherController implements Dumpable {
    private final ActivityStarter mActivityStarter;
    private final IActivityTaskManager mActivityTaskManager;
    @VisibleForTesting
    Dialog mAddUserDialog;
    private boolean mAddUsersFromLockScreen;
    private final Executor mBgExecutor;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final KeyguardStateController.Callback mCallback;
    protected final Context mContext;
    private final DevicePolicyManager mDevicePolicyManager;
    private final DeviceProvisionedController mDeviceProvisionedController;
    @VisibleForTesting
    AlertDialog mExitGuestDialog;
    private FalsingManager mFalsingManager;
    private final AtomicBoolean mGuestCreationScheduled;
    private final AtomicBoolean mGuestIsResetting;
    @VisibleForTesting
    final GuestResumeSessionReceiver mGuestResumeSessionReceiver;
    private final boolean mGuestUserAutoCreated;
    protected final Handler mHandler;
    private final KeyguardStateController mKeyguardStateController;
    @VisibleForTesting
    boolean mPauseRefreshUsers;
    private Intent mSecondaryUserServiceIntent;
    private final ContentObserver mSettingsObserver;
    private boolean mSimpleUserSwitcher;
    private final TelephonyListenerManager mTelephonyListenerManager;
    private final UiEventLogger mUiEventLogger;
    public final DetailAdapter mUserDetailAdapter;
    protected final UserManager mUserManager;
    protected final UserTracker mUserTracker;
    private final ArrayList<WeakReference<BaseUserAdapter>> mAdapters = new ArrayList<>();
    private ArrayList<UserRecord> mUsers = new ArrayList<>();
    private int mLastNonGuestUser = 0;
    private boolean mResumeUserOnGuestLogout = true;
    private int mSecondaryUser = -10000;
    private SparseBooleanArray mForcePictureLoadForUserId = new SparseBooleanArray(2);
    private final TelephonyCallback.CallStateListener mPhoneStateListener = new TelephonyCallback.CallStateListener() { // from class: com.android.systemui.statusbar.policy.UserSwitcherController.3
        private int mCallState;

        public void onCallStateChanged(int i) {
            if (this.mCallState != i) {
                this.mCallState = i;
                UserSwitcherController.this.refreshUsers(-10000);
            }
        }
    };
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.policy.UserSwitcherController.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            boolean z = true;
            int i = -10000;
            if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
                AlertDialog alertDialog = UserSwitcherController.this.mExitGuestDialog;
                if (alertDialog != null && alertDialog.isShowing()) {
                    UserSwitcherController.this.mExitGuestDialog.cancel();
                    UserSwitcherController.this.mExitGuestDialog = null;
                }
                int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -1);
                UserInfo userInfo = UserSwitcherController.this.mUserManager.getUserInfo(intExtra);
                int size = UserSwitcherController.this.mUsers.size();
                int i2 = 0;
                while (i2 < size) {
                    UserRecord userRecord = (UserRecord) UserSwitcherController.this.mUsers.get(i2);
                    UserInfo userInfo2 = userRecord.info;
                    if (userInfo2 != null) {
                        boolean z2 = userInfo2.id == intExtra;
                        if (userRecord.isCurrent != z2) {
                            UserSwitcherController.this.mUsers.set(i2, userRecord.copyWithIsCurrent(z2));
                        }
                        if (z2 && !userRecord.isGuest) {
                            UserSwitcherController.this.mLastNonGuestUser = userRecord.info.id;
                        }
                        if ((userInfo == null || !userInfo.isAdmin()) && userRecord.isRestricted) {
                            UserSwitcherController.this.mUsers.remove(i2);
                            i2--;
                        }
                    }
                    i2++;
                }
                UserSwitcherController.this.notifyAdapters();
                if (UserSwitcherController.this.mSecondaryUser != -10000) {
                    context.stopServiceAsUser(UserSwitcherController.this.mSecondaryUserServiceIntent, UserHandle.of(UserSwitcherController.this.mSecondaryUser));
                    UserSwitcherController.this.mSecondaryUser = -10000;
                }
                if (!(userInfo == null || userInfo.id == 0)) {
                    context.startServiceAsUser(UserSwitcherController.this.mSecondaryUserServiceIntent, UserHandle.of(userInfo.id));
                    UserSwitcherController.this.mSecondaryUser = userInfo.id;
                }
                if (UserSwitcherController.this.mGuestUserAutoCreated) {
                    UserSwitcherController.this.guaranteeGuestPresent();
                }
            } else {
                if ("android.intent.action.USER_INFO_CHANGED".equals(intent.getAction())) {
                    i = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                } else if ("android.intent.action.USER_UNLOCKED".equals(intent.getAction()) && intent.getIntExtra("android.intent.extra.user_handle", -10000) != 0) {
                    return;
                }
                z = false;
            }
            UserSwitcherController.this.refreshUsers(i);
            if (z) {
                UserSwitcherController.this.mUnpauseRefreshUsers.run();
            }
        }
    };
    private final Runnable mUnpauseRefreshUsers = new Runnable() { // from class: com.android.systemui.statusbar.policy.UserSwitcherController.5
        @Override // java.lang.Runnable
        public void run() {
            UserSwitcherController.this.mHandler.removeCallbacks(this);
            UserSwitcherController userSwitcherController = UserSwitcherController.this;
            userSwitcherController.mPauseRefreshUsers = false;
            userSwitcherController.refreshUsers(-10000);
        }
    };
    private final DeviceProvisionedController.DeviceProvisionedListener mGuaranteeGuestPresentAfterProvisioned = new DeviceProvisionedController.DeviceProvisionedListener() { // from class: com.android.systemui.statusbar.policy.UserSwitcherController.7
        @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
        public void onDeviceProvisionedChanged() {
            if (UserSwitcherController.this.isDeviceAllowedToAddGuest()) {
                UserSwitcherController.this.mBgExecutor.execute(new UserSwitcherController$7$$ExternalSyntheticLambda0(this));
                UserSwitcherController.this.guaranteeGuestPresent();
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onDeviceProvisionedChanged$0() {
            UserSwitcherController.this.mDeviceProvisionedController.removeCallback(UserSwitcherController.this.mGuaranteeGuestPresentAfterProvisioned);
        }
    };

    public UserSwitcherController(Context context, UserManager userManager, UserTracker userTracker, KeyguardStateController keyguardStateController, DeviceProvisionedController deviceProvisionedController, DevicePolicyManager devicePolicyManager, Handler handler, ActivityStarter activityStarter, BroadcastDispatcher broadcastDispatcher, UiEventLogger uiEventLogger, FalsingManager falsingManager, TelephonyListenerManager telephonyListenerManager, IActivityTaskManager iActivityTaskManager, UserDetailAdapter userDetailAdapter, SecureSettings secureSettings, Executor executor) {
        AnonymousClass6 r10 = new KeyguardStateController.Callback() { // from class: com.android.systemui.statusbar.policy.UserSwitcherController.6
            @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
            public void onKeyguardShowingChanged() {
                if (!UserSwitcherController.this.mKeyguardStateController.isShowing()) {
                    UserSwitcherController userSwitcherController = UserSwitcherController.this;
                    userSwitcherController.mHandler.post(new UserSwitcherController$6$$ExternalSyntheticLambda0(userSwitcherController));
                    return;
                }
                UserSwitcherController.this.notifyAdapters();
            }
        };
        this.mCallback = r10;
        this.mContext = context;
        this.mUserTracker = userTracker;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mTelephonyListenerManager = telephonyListenerManager;
        this.mActivityTaskManager = iActivityTaskManager;
        this.mUiEventLogger = uiEventLogger;
        this.mFalsingManager = falsingManager;
        GuestResumeSessionReceiver guestResumeSessionReceiver = new GuestResumeSessionReceiver(this, userTracker, uiEventLogger, secureSettings);
        this.mGuestResumeSessionReceiver = guestResumeSessionReceiver;
        this.mUserDetailAdapter = userDetailAdapter;
        this.mBgExecutor = executor;
        if (!UserManager.isGuestUserEphemeral()) {
            guestResumeSessionReceiver.register(broadcastDispatcher);
        }
        this.mGuestUserAutoCreated = context.getResources().getBoolean(17891567);
        this.mGuestIsResetting = new AtomicBoolean();
        this.mGuestCreationScheduled = new AtomicBoolean();
        this.mKeyguardStateController = keyguardStateController;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mDevicePolicyManager = devicePolicyManager;
        this.mHandler = handler;
        this.mActivityStarter = activityStarter;
        this.mUserManager = userManager;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_ADDED");
        intentFilter.addAction("android.intent.action.USER_REMOVED");
        intentFilter.addAction("android.intent.action.USER_INFO_CHANGED");
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.intent.action.USER_STOPPED");
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        broadcastDispatcher.registerReceiver(this.mReceiver, intentFilter, null, UserHandle.SYSTEM);
        this.mSimpleUserSwitcher = shouldUseSimpleUserSwitcher();
        this.mSecondaryUserServiceIntent = new Intent(context, SystemUISecondaryUserService.class);
        context.registerReceiverAsUser(this.mReceiver, UserHandle.SYSTEM, new IntentFilter(), "com.android.systemui.permission.SELF", null);
        AnonymousClass1 r2 = new ContentObserver(handler) { // from class: com.android.systemui.statusbar.policy.UserSwitcherController.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                UserSwitcherController userSwitcherController = UserSwitcherController.this;
                userSwitcherController.mSimpleUserSwitcher = userSwitcherController.shouldUseSimpleUserSwitcher();
                UserSwitcherController userSwitcherController2 = UserSwitcherController.this;
                boolean z2 = false;
                if (Settings.Global.getInt(userSwitcherController2.mContext.getContentResolver(), "add_users_when_locked", 0) != 0) {
                    z2 = true;
                }
                userSwitcherController2.mAddUsersFromLockScreen = z2;
                UserSwitcherController.this.refreshUsers(-10000);
            }
        };
        this.mSettingsObserver = r2;
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("lockscreenSimpleUserSwitcher"), true, r2);
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("add_users_when_locked"), true, r2);
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("allow_user_switching_when_system_user_locked"), true, r2);
        r2.onChange(false);
        keyguardStateController.addCallback(r10);
        listenForCallState();
        refreshUsers(-10000);
    }

    /* access modifiers changed from: private */
    public void refreshUsers(int i) {
        UserInfo userInfo;
        if (i != -10000) {
            this.mForcePictureLoadForUserId.put(i, true);
        }
        if (!this.mPauseRefreshUsers) {
            boolean z = this.mForcePictureLoadForUserId.get(-1);
            SparseArray sparseArray = new SparseArray(this.mUsers.size());
            int size = this.mUsers.size();
            for (int i2 = 0; i2 < size; i2++) {
                UserRecord userRecord = this.mUsers.get(i2);
                if (!(userRecord == null || userRecord.picture == null || (userInfo = userRecord.info) == null || z || this.mForcePictureLoadForUserId.get(userInfo.id))) {
                    sparseArray.put(userRecord.info.id, userRecord.picture);
                }
            }
            this.mForcePictureLoadForUserId.clear();
            final boolean z2 = this.mAddUsersFromLockScreen;
            new AsyncTask<SparseArray<Bitmap>, Void, ArrayList<UserRecord>>() { // from class: com.android.systemui.statusbar.policy.UserSwitcherController.2
                /* access modifiers changed from: protected */
                public ArrayList<UserRecord> doInBackground(SparseArray<Bitmap>... sparseArrayArr) {
                    SparseArray<Bitmap> sparseArray2 = sparseArrayArr[0];
                    List<UserInfo> aliveUsers = UserSwitcherController.this.mUserManager.getAliveUsers();
                    UserRecord userRecord2 = null;
                    if (aliveUsers == null) {
                        return null;
                    }
                    ArrayList<UserRecord> arrayList = new ArrayList<>(aliveUsers.size());
                    int userId = UserSwitcherController.this.mUserTracker.getUserId();
                    UserSwitcherController userSwitcherController = UserSwitcherController.this;
                    boolean z3 = userSwitcherController.mUserManager.getUserSwitchability(UserHandle.of(userSwitcherController.mUserTracker.getUserId())) == 0;
                    UserInfo userInfo2 = null;
                    for (UserInfo userInfo3 : aliveUsers) {
                        boolean z4 = userId == userInfo3.id;
                        UserInfo userInfo4 = z4 ? userInfo3 : userInfo2;
                        boolean z5 = z3 || z4;
                        if (userInfo3.isEnabled()) {
                            if (userInfo3.isGuest()) {
                                userRecord2 = new UserRecord(userInfo3, null, true, z4, false, false, z3);
                            } else if (userInfo3.supportsSwitchToByUser()) {
                                Bitmap bitmap = sparseArray2.get(userInfo3.id);
                                if (bitmap == null && (bitmap = UserSwitcherController.this.mUserManager.getUserIcon(userInfo3.id)) != null) {
                                    int dimensionPixelSize = UserSwitcherController.this.mContext.getResources().getDimensionPixelSize(R$dimen.max_avatar_size);
                                    bitmap = Bitmap.createScaledBitmap(bitmap, dimensionPixelSize, dimensionPixelSize, true);
                                }
                                arrayList.add(new UserRecord(userInfo3, bitmap, false, z4, false, false, z5));
                            }
                        }
                        userInfo2 = userInfo4;
                    }
                    if (arrayList.size() > 1 || userRecord2 != null) {
                        Prefs.putBoolean(UserSwitcherController.this.mContext, "HasSeenMultiUser", true);
                    }
                    boolean z6 = !UserSwitcherController.this.mUserManager.hasBaseUserRestriction("no_add_user", UserHandle.SYSTEM);
                    boolean z7 = userInfo2 != null && (userInfo2.isAdmin() || userInfo2.id == 0) && z6;
                    boolean z8 = z6 && z2;
                    boolean z9 = (z7 || z8) && userRecord2 == null;
                    boolean z10 = (z7 || z8) && UserSwitcherController.this.mUserManager.canAddMoreUsers();
                    boolean z11 = !z2;
                    if (userRecord2 != null) {
                        arrayList.add(userRecord2);
                    } else if (UserSwitcherController.this.mGuestUserAutoCreated) {
                        UserRecord userRecord3 = new UserRecord(null, null, true, false, false, false, !UserSwitcherController.this.mGuestIsResetting.get() && z3);
                        UserSwitcherController.this.checkIfAddUserDisallowedByAdminOnly(userRecord3);
                        arrayList.add(userRecord3);
                    } else if (z9) {
                        UserRecord userRecord4 = new UserRecord(null, null, true, false, false, z11, z3);
                        UserSwitcherController.this.checkIfAddUserDisallowedByAdminOnly(userRecord4);
                        arrayList.add(userRecord4);
                    }
                    if (z10) {
                        UserRecord userRecord5 = new UserRecord(null, null, false, false, true, z11, z3);
                        UserSwitcherController.this.checkIfAddUserDisallowedByAdminOnly(userRecord5);
                        arrayList.add(userRecord5);
                    }
                    return arrayList;
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(ArrayList<UserRecord> arrayList) {
                    if (arrayList != null) {
                        UserSwitcherController.this.mUsers = arrayList;
                        UserSwitcherController.this.notifyAdapters();
                    }
                }
            }.execute(sparseArray);
        }
    }

    private void pauseRefreshUsers() {
        if (!this.mPauseRefreshUsers) {
            this.mHandler.postDelayed(this.mUnpauseRefreshUsers, 3000);
            this.mPauseRefreshUsers = true;
        }
    }

    /* access modifiers changed from: private */
    public void notifyAdapters() {
        for (int size = this.mAdapters.size() - 1; size >= 0; size--) {
            BaseUserAdapter baseUserAdapter = this.mAdapters.get(size).get();
            if (baseUserAdapter != null) {
                baseUserAdapter.notifyDataSetChanged();
            } else {
                this.mAdapters.remove(size);
            }
        }
    }

    public boolean isSimpleUserSwitcher() {
        return this.mSimpleUserSwitcher;
    }

    public boolean useFullscreenUserSwitcher() {
        int intValue = ((Integer) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.systemui.statusbar.policy.UserSwitcherController$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                return UserSwitcherController.this.lambda$useFullscreenUserSwitcher$0();
            }
        })).intValue();
        if (intValue != -1) {
            return intValue != 0;
        }
        return this.mContext.getResources().getBoolean(R$bool.config_enableFullscreenUserSwitcher);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Integer lambda$useFullscreenUserSwitcher$0() {
        return Integer.valueOf(Settings.System.getInt(this.mContext.getContentResolver(), "enable_fullscreen_user_switcher", -1));
    }

    @VisibleForTesting
    void onUserListItemClicked(UserRecord userRecord) {
        int i;
        UserInfo userInfo;
        if (userRecord.isGuest && userRecord.info == null) {
            i = createGuest();
            if (i != -10000) {
                this.mUiEventLogger.log(QSUserSwitcherEvent.QS_USER_GUEST_ADD);
            } else {
                return;
            }
        } else if (userRecord.isAddUser) {
            showAddUserDialog();
            return;
        } else {
            i = userRecord.info.id;
        }
        int userId = this.mUserTracker.getUserId();
        if (userId == i) {
            if (userRecord.isGuest) {
                showExitGuestDialog(i);
            }
        } else if (!UserManager.isGuestUserEphemeral() || (userInfo = this.mUserManager.getUserInfo(userId)) == null || !userInfo.isGuest()) {
            switchToUserId(i);
        } else {
            showExitGuestDialog(userId, userRecord.resolveId());
        }
    }

    protected void switchToUserId(int i) {
        try {
            pauseRefreshUsers();
            ActivityManager.getService().switchUser(i);
        } catch (RemoteException e) {
            Log.e("UserSwitcherController", "Couldn't switch user.", e);
        }
    }

    protected void showExitGuestDialog(int i) {
        int i2;
        UserInfo userInfo;
        showExitGuestDialog(i, (!this.mResumeUserOnGuestLogout || (i2 = this.mLastNonGuestUser) == 0 || (userInfo = this.mUserManager.getUserInfo(i2)) == null || !userInfo.isEnabled() || !userInfo.supportsSwitchToByUser()) ? 0 : userInfo.id);
    }

    protected void showExitGuestDialog(int i, int i2) {
        AlertDialog alertDialog = this.mExitGuestDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.mExitGuestDialog.cancel();
        }
        ExitGuestDialog exitGuestDialog = new ExitGuestDialog(this.mContext, i, i2);
        this.mExitGuestDialog = exitGuestDialog;
        exitGuestDialog.show();
    }

    public void showAddUserDialog() {
        Dialog dialog = this.mAddUserDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mAddUserDialog.cancel();
        }
        AddUserDialog addUserDialog = new AddUserDialog(this.mContext);
        this.mAddUserDialog = addUserDialog;
        addUserDialog.show();
    }

    private void listenForCallState() {
        this.mTelephonyListenerManager.addCallStateListener(this.mPhoneStateListener);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("UserSwitcherController state:");
        printWriter.println("  mLastNonGuestUser=" + this.mLastNonGuestUser);
        printWriter.print("  mUsers.size=");
        printWriter.println(this.mUsers.size());
        for (int i = 0; i < this.mUsers.size(); i++) {
            printWriter.print("    ");
            printWriter.println(this.mUsers.get(i).toString());
        }
        printWriter.println("mSimpleUserSwitcher=" + this.mSimpleUserSwitcher);
        printWriter.println("mGuestUserAutoCreated=" + this.mGuestUserAutoCreated);
    }

    public String getCurrentUserName() {
        UserRecord userRecord;
        UserInfo userInfo;
        if (this.mUsers.isEmpty() || (userRecord = this.mUsers.get(0)) == null || (userInfo = userRecord.info) == null) {
            return null;
        }
        if (userRecord.isGuest) {
            return this.mContext.getString(R$string.guest_nickname);
        }
        return userInfo.name;
    }

    public void onDensityOrFontScaleChanged() {
        refreshUsers(-1);
    }

    @VisibleForTesting
    public void addAdapter(WeakReference<BaseUserAdapter> weakReference) {
        this.mAdapters.add(weakReference);
    }

    @VisibleForTesting
    public ArrayList<UserRecord> getUsers() {
        return this.mUsers;
    }

    public void removeGuestUser(int i, int i2) {
        UserInfo userInfo = this.mUserTracker.getUserInfo();
        if (userInfo.id != i) {
            Log.w("UserSwitcherController", "User requesting to start a new session (" + i + ") is not current user (" + userInfo.id + ")");
        } else if (!userInfo.isGuest()) {
            Log.w("UserSwitcherController", "User requesting to start a new session (" + i + ") is not a guest");
        } else if (!this.mUserManager.markGuestForDeletion(userInfo.id)) {
            Log.w("UserSwitcherController", "Couldn't mark the guest for deletion for user " + i);
        } else {
            try {
                if (i2 == -10000) {
                    int createGuest = createGuest();
                    if (createGuest == -10000) {
                        Log.e("UserSwitcherController", "Could not create new guest, switching back to system user");
                        switchToUserId(0);
                        this.mUserManager.removeUser(userInfo.id);
                        WindowManagerGlobal.getWindowManagerService().lockNow((Bundle) null);
                        return;
                    }
                    switchToUserId(createGuest);
                    this.mUserManager.removeUser(userInfo.id);
                    return;
                }
                if (this.mGuestUserAutoCreated) {
                    this.mGuestIsResetting.set(true);
                }
                switchToUserId(i2);
                this.mUserManager.removeUser(userInfo.id);
            } catch (RemoteException unused) {
                Log.e("UserSwitcherController", "Couldn't remove guest because ActivityManager or WindowManager is dead");
            }
        }
    }

    private void scheduleGuestCreation() {
        if (this.mGuestCreationScheduled.compareAndSet(false, true)) {
            this.mBgExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.policy.UserSwitcherController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    UserSwitcherController.this.lambda$scheduleGuestCreation$1();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleGuestCreation$1() {
        int createGuest = createGuest();
        this.mGuestCreationScheduled.set(false);
        this.mGuestIsResetting.set(false);
        if (createGuest == -10000) {
            Log.w("UserSwitcherController", "Could not create new guest while exiting existing guest");
            refreshUsers(-10000);
        }
    }

    public void schedulePostBootGuestCreation() {
        if (isDeviceAllowedToAddGuest()) {
            guaranteeGuestPresent();
        } else {
            this.mDeviceProvisionedController.addCallback(this.mGuaranteeGuestPresentAfterProvisioned);
        }
    }

    /* access modifiers changed from: private */
    public boolean isDeviceAllowedToAddGuest() {
        return this.mDeviceProvisionedController.isDeviceProvisioned() && !this.mDevicePolicyManager.isDeviceManaged();
    }

    /* access modifiers changed from: private */
    public void guaranteeGuestPresent() {
        if (isDeviceAllowedToAddGuest() && this.mUserManager.findCurrentGuestUser() == null) {
            scheduleGuestCreation();
        }
    }

    public int createGuest() {
        try {
            UserManager userManager = this.mUserManager;
            Context context = this.mContext;
            UserInfo createGuest = userManager.createGuest(context, context.getString(R$string.guest_nickname));
            if (createGuest != null) {
                return createGuest.id;
            }
            Log.e("UserSwitcherController", "Couldn't create guest, most likely because there already exists one");
            return -10000;
        } catch (UserManager.UserOperationException e) {
            Log.e("UserSwitcherController", "Couldn't create guest user", e);
            return -10000;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class BaseUserAdapter extends BaseAdapter {
        final UserSwitcherController mController;
        private final KeyguardStateController mKeyguardStateController;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return (long) i;
        }

        /* access modifiers changed from: protected */
        public BaseUserAdapter(UserSwitcherController userSwitcherController) {
            this.mController = userSwitcherController;
            this.mKeyguardStateController = userSwitcherController.mKeyguardStateController;
            userSwitcherController.addAdapter(new WeakReference<>(this));
        }

        /* access modifiers changed from: protected */
        public ArrayList<UserRecord> getUsers() {
            return this.mController.getUsers();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return countUsers(true);
        }

        private int countUsers(boolean z) {
            boolean isShowing = this.mKeyguardStateController.isShowing();
            int size = getUsers().size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                if (!getUsers().get(i2).isGuest || z) {
                    if (getUsers().get(i2).isRestricted && isShowing) {
                        break;
                    }
                    i++;
                }
            }
            return i;
        }

        @Override // android.widget.Adapter
        public UserRecord getItem(int i) {
            return getUsers().get(i);
        }

        public void onUserListItemClicked(UserRecord userRecord) {
            this.mController.onUserListItemClicked(userRecord);
        }

        public String getName(Context context, UserRecord userRecord) {
            int i;
            int i2;
            if (userRecord.isGuest) {
                if (userRecord.isCurrent) {
                    if (this.mController.mGuestUserAutoCreated) {
                        i2 = R$string.guest_reset_guest;
                    } else {
                        i2 = R$string.guest_exit_guest;
                    }
                    return context.getString(i2);
                } else if (userRecord.info != null) {
                    return context.getString(R$string.guest_nickname);
                } else {
                    if (!this.mController.mGuestUserAutoCreated) {
                        return context.getString(R$string.guest_new_guest);
                    }
                    if (this.mController.mGuestIsResetting.get()) {
                        i = R$string.guest_resetting;
                    } else {
                        i = R$string.guest_nickname;
                    }
                    return context.getString(i);
                }
            } else if (userRecord.isAddUser) {
                return context.getString(com.android.systemui.R$string.user_add_user);
            } else {
                return userRecord.info.name;
            }
        }

        /* access modifiers changed from: protected */
        public static ColorFilter getDisabledUserAvatarColorFilter() {
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0.0f);
            return new ColorMatrixColorFilter(colorMatrix);
        }

        /* access modifiers changed from: protected */
        public static Drawable getIconDrawable(Context context, UserRecord userRecord) {
            int i;
            if (userRecord.isAddUser) {
                i = R$drawable.ic_add_circle;
            } else if (userRecord.isGuest) {
                i = R$drawable.ic_avatar_guest_user;
            } else {
                i = R$drawable.ic_avatar_user;
            }
            return context.getDrawable(i);
        }

        public void refresh() {
            this.mController.refreshUsers(-10000);
        }
    }

    /* access modifiers changed from: private */
    public void checkIfAddUserDisallowedByAdminOnly(UserRecord userRecord) {
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, "no_add_user", this.mUserTracker.getUserId());
        if (checkIfRestrictionEnforced == null || RestrictedLockUtilsInternal.hasBaseUserRestriction(this.mContext, "no_add_user", this.mUserTracker.getUserId())) {
            userRecord.isDisabledByAdmin = false;
            userRecord.enforcedAdmin = null;
            return;
        }
        userRecord.isDisabledByAdmin = true;
        userRecord.enforcedAdmin = checkIfRestrictionEnforced;
    }

    /* access modifiers changed from: private */
    public boolean shouldUseSimpleUserSwitcher() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "lockscreenSimpleUserSwitcher", this.mContext.getResources().getBoolean(17891555) ? 1 : 0) != 0;
    }

    public void startActivity(Intent intent) {
        this.mActivityStarter.startActivity(intent, true);
    }

    /* loaded from: classes.dex */
    public static final class UserRecord {
        public RestrictedLockUtils.EnforcedAdmin enforcedAdmin;
        public final UserInfo info;
        public final boolean isAddUser;
        public final boolean isCurrent;
        public boolean isDisabledByAdmin;
        public final boolean isGuest;
        public final boolean isRestricted;
        public boolean isSwitchToEnabled;
        public final Bitmap picture;

        public UserRecord(UserInfo userInfo, Bitmap bitmap, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
            this.info = userInfo;
            this.picture = bitmap;
            this.isGuest = z;
            this.isCurrent = z2;
            this.isAddUser = z3;
            this.isRestricted = z4;
            this.isSwitchToEnabled = z5;
        }

        public UserRecord copyWithIsCurrent(boolean z) {
            return new UserRecord(this.info, this.picture, this.isGuest, z, this.isAddUser, this.isRestricted, this.isSwitchToEnabled);
        }

        public int resolveId() {
            UserInfo userInfo;
            if (this.isGuest || (userInfo = this.info) == null) {
                return -10000;
            }
            return userInfo.id;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("UserRecord(");
            if (this.info != null) {
                sb.append("name=\"");
                sb.append(this.info.name);
                sb.append("\" id=");
                sb.append(this.info.id);
            } else if (this.isGuest) {
                sb.append("<add guest placeholder>");
            } else if (this.isAddUser) {
                sb.append("<add user placeholder>");
            }
            if (this.isGuest) {
                sb.append(" <isGuest>");
            }
            if (this.isAddUser) {
                sb.append(" <isAddUser>");
            }
            if (this.isCurrent) {
                sb.append(" <isCurrent>");
            }
            if (this.picture != null) {
                sb.append(" <hasPicture>");
            }
            if (this.isRestricted) {
                sb.append(" <isRestricted>");
            }
            if (this.isDisabledByAdmin) {
                sb.append(" <isDisabledByAdmin>");
                sb.append(" enforcedAdmin=");
                sb.append(this.enforcedAdmin);
            }
            if (this.isSwitchToEnabled) {
                sb.append(" <isSwitchToEnabled>");
            }
            sb.append(')');
            return sb.toString();
        }
    }

    /* loaded from: classes.dex */
    public static class UserDetailAdapter implements DetailAdapter {
        private final Intent USER_SETTINGS_INTENT = new Intent("android.settings.USER_SETTINGS");
        private final Context mContext;
        private final Provider<UserDetailView.Adapter> mUserDetailViewAdapterProvider;

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public int getMetricsCategory() {
            return R$styleable.AppCompatTheme_windowMinWidthMinor;
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public Boolean getToggleState() {
            return null;
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public void setToggleState(boolean z) {
        }

        /* access modifiers changed from: package-private */
        public UserDetailAdapter(Context context, Provider<UserDetailView.Adapter> provider) {
            this.mContext = context;
            this.mUserDetailViewAdapterProvider = provider;
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public CharSequence getTitle() {
            return this.mContext.getString(com.android.systemui.R$string.quick_settings_user_title);
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public View createDetailView(Context context, View view, ViewGroup viewGroup) {
            UserDetailView userDetailView;
            if (!(view instanceof UserDetailView)) {
                userDetailView = UserDetailView.inflate(context, viewGroup, false);
                userDetailView.setAdapter(this.mUserDetailViewAdapterProvider.get());
            } else {
                userDetailView = (UserDetailView) view;
            }
            userDetailView.refreshAdapter();
            return userDetailView;
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public Intent getSettingsIntent() {
            return this.USER_SETTINGS_INTENT;
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public int getSettingsText() {
            return com.android.systemui.R$string.quick_settings_more_user_settings;
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public UiEventLogger.UiEventEnum openDetailEvent() {
            return QSUserSwitcherEvent.QS_USER_DETAIL_OPEN;
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public UiEventLogger.UiEventEnum closeDetailEvent() {
            return QSUserSwitcherEvent.QS_USER_DETAIL_CLOSE;
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public UiEventLogger.UiEventEnum moreSettingsEvent() {
            return QSUserSwitcherEvent.QS_USER_MORE_SETTINGS;
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class ExitGuestDialog extends SystemUIDialog implements DialogInterface.OnClickListener {
        private final int mGuestId;
        private final int mTargetId;

        public ExitGuestDialog(Context context, int i, int i2) {
            super(context);
            int i3;
            int i4;
            if (UserSwitcherController.this.mGuestUserAutoCreated) {
                i3 = R$string.guest_reset_guest_dialog_title;
            } else {
                i3 = com.android.systemui.R$string.guest_exit_guest_dialog_title;
            }
            setTitle(i3);
            setMessage(context.getString(com.android.systemui.R$string.guest_exit_guest_dialog_message));
            setButton(-2, context.getString(17039360), this);
            if (UserSwitcherController.this.mGuestUserAutoCreated) {
                i4 = R$string.guest_reset_guest_confirm_button;
            } else {
                i4 = com.android.systemui.R$string.guest_exit_guest_dialog_remove;
            }
            setButton(-1, context.getString(i4), this);
            SystemUIDialog.setWindowOnTop(this);
            setCanceledOnTouchOutside(false);
            this.mGuestId = i;
            this.mTargetId = i2;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (!UserSwitcherController.this.mFalsingManager.isFalseTap(i == -2 ? 0 : 3)) {
                if (i == -2) {
                    cancel();
                    return;
                }
                UserSwitcherController.this.mUiEventLogger.log(QSUserSwitcherEvent.QS_USER_GUEST_REMOVE);
                dismiss();
                UserSwitcherController.this.removeGuestUser(this.mGuestId, this.mTargetId);
            }
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class AddUserDialog extends SystemUIDialog implements DialogInterface.OnClickListener {
        public AddUserDialog(Context context) {
            super(context);
            setTitle(com.android.systemui.R$string.user_add_user_title);
            setMessage(context.getString(com.android.systemui.R$string.user_add_user_message_short));
            setButton(-2, context.getString(17039360), this);
            setButton(-1, context.getString(17039370), this);
            SystemUIDialog.setWindowOnTop(this);
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (!UserSwitcherController.this.mFalsingManager.isFalseTap(i == -2 ? 0 : 2)) {
                if (i == -2) {
                    cancel();
                    return;
                }
                dismiss();
                if (!ActivityManager.isUserAMonkey()) {
                    Intent createIntentForStart = CreateUserActivity.createIntentForStart(getContext());
                    if (UserSwitcherController.this.mKeyguardStateController.isUnlocked() || UserSwitcherController.this.mKeyguardStateController.canDismissLockScreen()) {
                        UserSwitcherController.this.mActivityStarter.startActivity(createIntentForStart, true);
                        return;
                    }
                    try {
                        UserSwitcherController.this.mActivityTaskManager.startActivity((IApplicationThread) null, UserSwitcherController.this.mContext.getBasePackageName(), UserSwitcherController.this.mContext.getAttributionTag(), createIntentForStart, createIntentForStart.resolveTypeIfNeeded(UserSwitcherController.this.mContext.getContentResolver()), (IBinder) null, (String) null, 0, 0, (ProfilerInfo) null, (Bundle) null);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Log.e("UserSwitcherController", "Couldn't start create user activity", e);
                    }
                }
            }
        }
    }
}
