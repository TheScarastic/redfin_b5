package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.app.admin.DeviceAdminInfo;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.VpnManager;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.net.LegacyVpnInfo;
import com.android.internal.net.VpnConfig;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.policy.SecurityController;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executor;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class SecurityControllerImpl extends CurrentUserTracker implements SecurityController {
    private static final boolean DEBUG = Log.isLoggable("SecurityController", 3);
    private static final NetworkRequest REQUEST = new NetworkRequest.Builder().clearCapabilities().build();
    private final Executor mBgExecutor;
    private final BroadcastReceiver mBroadcastReceiver;
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private int mCurrentUserId;
    private final DevicePolicyManager mDevicePolicyManager;
    private final ConnectivityManager.NetworkCallback mNetworkCallback;
    private final PackageManager mPackageManager;
    private final UserManager mUserManager;
    private final VpnManager mVpnManager;
    private int mVpnUserId;
    @GuardedBy({"mCallbacks"})
    private final ArrayList<SecurityController.SecurityControllerCallback> mCallbacks = new ArrayList<>();
    private SparseArray<VpnConfig> mCurrentVpns = new SparseArray<>();
    private ArrayMap<Integer, Boolean> mHasCACerts = new ArrayMap<>();

    public SecurityControllerImpl(Context context, Handler handler, BroadcastDispatcher broadcastDispatcher, Executor executor) {
        super(broadcastDispatcher);
        AnonymousClass1 r0 = new ConnectivityManager.NetworkCallback() { // from class: com.android.systemui.statusbar.policy.SecurityControllerImpl.1
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                if (SecurityControllerImpl.DEBUG) {
                    Log.d("SecurityController", "onAvailable " + network.getNetId());
                }
                SecurityControllerImpl.this.updateState();
                SecurityControllerImpl.this.fireCallbacks();
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                if (SecurityControllerImpl.DEBUG) {
                    Log.d("SecurityController", "onLost " + network.getNetId());
                }
                SecurityControllerImpl.this.updateState();
                SecurityControllerImpl.this.fireCallbacks();
            }
        };
        this.mNetworkCallback = r0;
        AnonymousClass2 r1 = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.policy.SecurityControllerImpl.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                int intExtra;
                if ("android.security.action.TRUST_STORE_CHANGED".equals(intent.getAction())) {
                    SecurityControllerImpl.this.refreshCACerts(getSendingUserId());
                } else if ("android.intent.action.USER_UNLOCKED".equals(intent.getAction()) && (intExtra = intent.getIntExtra("android.intent.extra.user_handle", -10000)) != -10000) {
                    SecurityControllerImpl.this.refreshCACerts(intExtra);
                }
            }
        };
        this.mBroadcastReceiver = r1;
        this.mContext = context;
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        this.mConnectivityManager = connectivityManager;
        this.mVpnManager = (VpnManager) context.getSystemService(VpnManager.class);
        this.mPackageManager = context.getPackageManager();
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mBgExecutor = executor;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.security.action.TRUST_STORE_CHANGED");
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        broadcastDispatcher.registerReceiverWithHandler(r1, intentFilter, handler, UserHandle.ALL);
        connectivityManager.registerNetworkCallback(REQUEST, r0);
        onUserSwitched(ActivityManager.getCurrentUser());
        startTracking();
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("SecurityController state:");
        printWriter.print("  mCurrentVpns={");
        for (int i = 0; i < this.mCurrentVpns.size(); i++) {
            if (i > 0) {
                printWriter.print(", ");
            }
            printWriter.print(this.mCurrentVpns.keyAt(i));
            printWriter.print('=');
            printWriter.print(this.mCurrentVpns.valueAt(i).user);
        }
        printWriter.println("}");
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean isDeviceManaged() {
        return this.mDevicePolicyManager.isDeviceManaged();
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public CharSequence getDeviceOwnerOrganizationName() {
        return this.mDevicePolicyManager.getDeviceOwnerOrganizationName();
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public CharSequence getWorkProfileOrganizationName() {
        int workProfileUserId = getWorkProfileUserId(this.mCurrentUserId);
        if (workProfileUserId == -10000) {
            return null;
        }
        return this.mDevicePolicyManager.getOrganizationNameForUser(workProfileUserId);
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public String getPrimaryVpnName() {
        VpnConfig vpnConfig = this.mCurrentVpns.get(this.mVpnUserId);
        if (vpnConfig != null) {
            return getNameForVpnConfig(vpnConfig, new UserHandle(this.mVpnUserId));
        }
        return null;
    }

    private int getWorkProfileUserId(int i) {
        for (UserInfo userInfo : this.mUserManager.getProfiles(i)) {
            if (userInfo.isManagedProfile()) {
                return userInfo.id;
            }
        }
        return -10000;
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean hasWorkProfile() {
        return getWorkProfileUserId(this.mCurrentUserId) != -10000;
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean isWorkProfileOn() {
        UserHandle of = UserHandle.of(getWorkProfileUserId(this.mCurrentUserId));
        return of != null && !this.mUserManager.isQuietModeEnabled(of);
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean isProfileOwnerOfOrganizationOwnedDevice() {
        return this.mDevicePolicyManager.isOrganizationOwnedDeviceWithManagedProfile();
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public String getWorkProfileVpnName() {
        VpnConfig vpnConfig;
        int workProfileUserId = getWorkProfileUserId(this.mVpnUserId);
        if (workProfileUserId == -10000 || (vpnConfig = this.mCurrentVpns.get(workProfileUserId)) == null) {
            return null;
        }
        return getNameForVpnConfig(vpnConfig, UserHandle.of(workProfileUserId));
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public ComponentName getDeviceOwnerComponentOnAnyUser() {
        return this.mDevicePolicyManager.getDeviceOwnerComponentOnAnyUser();
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public int getDeviceOwnerType(ComponentName componentName) {
        return this.mDevicePolicyManager.getDeviceOwnerType(componentName);
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean isNetworkLoggingEnabled() {
        return this.mDevicePolicyManager.isNetworkLoggingEnabled(null);
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean isVpnEnabled() {
        for (int i : this.mUserManager.getProfileIdsWithDisabled(this.mVpnUserId)) {
            if (this.mCurrentVpns.get(i) != null) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean isVpnBranded() {
        String packageNameForVpnConfig;
        VpnConfig vpnConfig = this.mCurrentVpns.get(this.mVpnUserId);
        if (vpnConfig == null || (packageNameForVpnConfig = getPackageNameForVpnConfig(vpnConfig)) == null) {
            return false;
        }
        return isVpnPackageBranded(packageNameForVpnConfig);
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean hasCACertInCurrentUser() {
        Boolean bool = this.mHasCACerts.get(Integer.valueOf(this.mCurrentUserId));
        return bool != null && bool.booleanValue();
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean hasCACertInWorkProfile() {
        Boolean bool;
        int workProfileUserId = getWorkProfileUserId(this.mCurrentUserId);
        if (workProfileUserId == -10000 || (bool = this.mHasCACerts.get(Integer.valueOf(workProfileUserId))) == null || !bool.booleanValue()) {
            return false;
        }
        return true;
    }

    public void removeCallback(SecurityController.SecurityControllerCallback securityControllerCallback) {
        synchronized (this.mCallbacks) {
            if (securityControllerCallback != null) {
                if (DEBUG) {
                    Log.d("SecurityController", "removeCallback " + securityControllerCallback);
                }
                this.mCallbacks.remove(securityControllerCallback);
            }
        }
    }

    public void addCallback(SecurityController.SecurityControllerCallback securityControllerCallback) {
        synchronized (this.mCallbacks) {
            if (securityControllerCallback != null) {
                if (!this.mCallbacks.contains(securityControllerCallback)) {
                    if (DEBUG) {
                        Log.d("SecurityController", "addCallback " + securityControllerCallback);
                    }
                    this.mCallbacks.add(securityControllerCallback);
                }
            }
        }
    }

    @Override // com.android.systemui.settings.CurrentUserTracker
    public void onUserSwitched(int i) {
        this.mCurrentUserId = i;
        UserInfo userInfo = this.mUserManager.getUserInfo(i);
        if (userInfo.isRestricted()) {
            this.mVpnUserId = userInfo.restrictedProfileParentId;
        } else {
            this.mVpnUserId = this.mCurrentUserId;
        }
        fireCallbacks();
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean isParentalControlsEnabled() {
        return getProfileOwnerOrDeviceOwnerSupervisionComponent() != null;
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public DeviceAdminInfo getDeviceAdminInfo() {
        return getDeviceAdminInfo(getProfileOwnerOrDeviceOwnerComponent());
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public Drawable getIcon(DeviceAdminInfo deviceAdminInfo) {
        if (deviceAdminInfo == null) {
            return null;
        }
        return deviceAdminInfo.loadIcon(this.mPackageManager);
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public CharSequence getLabel(DeviceAdminInfo deviceAdminInfo) {
        if (deviceAdminInfo == null) {
            return null;
        }
        return deviceAdminInfo.loadLabel(this.mPackageManager);
    }

    private ComponentName getProfileOwnerOrDeviceOwnerSupervisionComponent() {
        return this.mDevicePolicyManager.getProfileOwnerOrDeviceOwnerSupervisionComponent(new UserHandle(this.mCurrentUserId));
    }

    private ComponentName getProfileOwnerOrDeviceOwnerComponent() {
        return getProfileOwnerOrDeviceOwnerSupervisionComponent();
    }

    private DeviceAdminInfo getDeviceAdminInfo(ComponentName componentName) {
        try {
            ResolveInfo resolveInfo = new ResolveInfo();
            resolveInfo.activityInfo = this.mPackageManager.getReceiverInfo(componentName, 128);
            return new DeviceAdminInfo(this.mContext, resolveInfo);
        } catch (PackageManager.NameNotFoundException | IOException | XmlPullParserException unused) {
            return null;
        }
    }

    public void refreshCACerts(int i) {
        this.mBgExecutor.execute(new Runnable(i) { // from class: com.android.systemui.statusbar.policy.SecurityControllerImpl$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SecurityControllerImpl.m376$r8$lambda$whQIoqHgO7e7SUSvapEPRdLZ7Q(SecurityControllerImpl.this, this.f$1);
            }
        });
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x00a2  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void lambda$refreshCACerts$0(int r8) {
        /*
            r7 = this;
            java.lang.String r0 = "Refreshing CA Certs "
            java.lang.String r1 = "SecurityController"
            r2 = 0
            android.content.Context r3 = r7.mContext     // Catch: RemoteException | InterruptedException | AssertionError -> 0x0063, all -> 0x0061
            android.os.UserHandle r4 = android.os.UserHandle.of(r8)     // Catch: RemoteException | InterruptedException | AssertionError -> 0x0063, all -> 0x0061
            android.security.KeyChain$KeyChainConnection r3 = android.security.KeyChain.bindAsUser(r3, r4)     // Catch: RemoteException | InterruptedException | AssertionError -> 0x0063, all -> 0x0061
            android.security.IKeyChainService r4 = r3.getService()     // Catch: all -> 0x0055
            android.content.pm.StringParceledListSlice r4 = r4.getUserCaAliases()     // Catch: all -> 0x0055
            java.util.List r4 = r4.getList()     // Catch: all -> 0x0055
            boolean r4 = r4.isEmpty()     // Catch: all -> 0x0055
            if (r4 != 0) goto L_0x0023
            r4 = 1
            goto L_0x0024
        L_0x0023:
            r4 = 0
        L_0x0024:
            android.util.Pair r5 = new android.util.Pair     // Catch: all -> 0x0055
            java.lang.Integer r6 = java.lang.Integer.valueOf(r8)     // Catch: all -> 0x0055
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r4)     // Catch: all -> 0x0055
            r5.<init>(r6, r4)     // Catch: all -> 0x0055
            r3.close()     // Catch: RemoteException | InterruptedException | AssertionError -> 0x0053, all -> 0x009c
            boolean r8 = com.android.systemui.statusbar.policy.SecurityControllerImpl.DEBUG
            if (r8 == 0) goto L_0x004a
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r0)
            r8.append(r5)
            java.lang.String r8 = r8.toString()
            android.util.Log.d(r1, r8)
        L_0x004a:
            java.lang.Object r8 = r5.second
            if (r8 == 0) goto L_0x009b
            android.util.ArrayMap<java.lang.Integer, java.lang.Boolean> r0 = r7.mHasCACerts
            java.lang.Object r1 = r5.first
            goto L_0x0091
        L_0x0053:
            r3 = move-exception
            goto L_0x0065
        L_0x0055:
            r4 = move-exception
            if (r3 == 0) goto L_0x0060
            r3.close()     // Catch: all -> 0x005c
            goto L_0x0060
        L_0x005c:
            r3 = move-exception
            r4.addSuppressed(r3)     // Catch: RemoteException | InterruptedException | AssertionError -> 0x0063, RemoteException | InterruptedException | AssertionError -> 0x0063, RemoteException | InterruptedException | AssertionError -> 0x0063, all -> 0x0061
        L_0x0060:
            throw r4     // Catch: RemoteException | InterruptedException | AssertionError -> 0x0063, RemoteException | InterruptedException | AssertionError -> 0x0063, RemoteException | InterruptedException | AssertionError -> 0x0063, all -> 0x0061
        L_0x0061:
            r8 = move-exception
            goto L_0x009e
        L_0x0063:
            r3 = move-exception
            r5 = r2
        L_0x0065:
            java.lang.String r4 = "failed to get CA certs"
            android.util.Log.i(r1, r4, r3)     // Catch: all -> 0x009c
            android.util.Pair r3 = new android.util.Pair     // Catch: all -> 0x009c
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch: all -> 0x009c
            r3.<init>(r8, r2)     // Catch: all -> 0x009c
            boolean r8 = com.android.systemui.statusbar.policy.SecurityControllerImpl.DEBUG
            if (r8 == 0) goto L_0x0089
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r0)
            r8.append(r3)
            java.lang.String r8 = r8.toString()
            android.util.Log.d(r1, r8)
        L_0x0089:
            java.lang.Object r8 = r3.second
            if (r8 == 0) goto L_0x009b
            android.util.ArrayMap<java.lang.Integer, java.lang.Boolean> r0 = r7.mHasCACerts
            java.lang.Object r1 = r3.first
        L_0x0091:
            java.lang.Integer r1 = (java.lang.Integer) r1
            java.lang.Boolean r8 = (java.lang.Boolean) r8
            r0.put(r1, r8)
            r7.fireCallbacks()
        L_0x009b:
            return
        L_0x009c:
            r8 = move-exception
            r2 = r5
        L_0x009e:
            boolean r3 = com.android.systemui.statusbar.policy.SecurityControllerImpl.DEBUG
            if (r3 == 0) goto L_0x00b4
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            r3.append(r2)
            java.lang.String r0 = r3.toString()
            android.util.Log.d(r1, r0)
        L_0x00b4:
            if (r2 == 0) goto L_0x00c8
            java.lang.Object r0 = r2.second
            if (r0 == 0) goto L_0x00c8
            android.util.ArrayMap<java.lang.Integer, java.lang.Boolean> r1 = r7.mHasCACerts
            java.lang.Object r2 = r2.first
            java.lang.Integer r2 = (java.lang.Integer) r2
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            r1.put(r2, r0)
            r7.fireCallbacks()
        L_0x00c8:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.SecurityControllerImpl.lambda$refreshCACerts$0(int):void");
    }

    private String getNameForVpnConfig(VpnConfig vpnConfig, UserHandle userHandle) {
        if (vpnConfig.legacy) {
            return this.mContext.getString(R$string.legacy_vpn_name);
        }
        String str = vpnConfig.user;
        try {
            Context context = this.mContext;
            return VpnConfig.getVpnLabel(context.createPackageContextAsUser(context.getPackageName(), 0, userHandle), str).toString();
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("SecurityController", "Package " + str + " is not present", e);
            return null;
        }
    }

    public void fireCallbacks() {
        synchronized (this.mCallbacks) {
            Iterator<SecurityController.SecurityControllerCallback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onStateChanged();
            }
        }
    }

    public void updateState() {
        LegacyVpnInfo legacyVpnInfo;
        SparseArray<VpnConfig> sparseArray = new SparseArray<>();
        for (UserInfo userInfo : this.mUserManager.getUsers()) {
            VpnConfig vpnConfig = this.mVpnManager.getVpnConfig(userInfo.id);
            if (vpnConfig != null && (!vpnConfig.legacy || ((legacyVpnInfo = this.mVpnManager.getLegacyVpnInfo(userInfo.id)) != null && legacyVpnInfo.state == 3))) {
                sparseArray.put(userInfo.id, vpnConfig);
            }
        }
        this.mCurrentVpns = sparseArray;
    }

    private String getPackageNameForVpnConfig(VpnConfig vpnConfig) {
        if (vpnConfig.legacy) {
            return null;
        }
        return vpnConfig.user;
    }

    private boolean isVpnPackageBranded(String str) {
        try {
            ApplicationInfo applicationInfo = this.mPackageManager.getApplicationInfo(str, 128);
            if (!(applicationInfo == null || applicationInfo.metaData == null || !applicationInfo.isSystemApp())) {
                return applicationInfo.metaData.getBoolean("com.android.systemui.IS_BRANDED", false);
            }
        } catch (PackageManager.NameNotFoundException unused) {
        }
        return false;
    }
}
