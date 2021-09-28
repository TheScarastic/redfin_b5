package com.android.systemui.navigationbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.om.IOverlayManager;
import android.content.pm.PackageManager;
import android.content.res.ApkAssets;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class NavigationModeController implements Dumpable {
    private static final String TAG = "NavigationModeController";
    private final Context mContext;
    private Context mCurrentUserContext;
    private final DeviceProvisionedController.DeviceProvisionedListener mDeviceProvisionedCallback;
    private final Executor mUiBgExecutor;
    private ArrayList<ModeChangedListener> mListeners = new ArrayList<>();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.navigationbar.NavigationModeController.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Log.d(NavigationModeController.TAG, "ACTION_OVERLAY_CHANGED");
            NavigationModeController.this.updateCurrentInteractionMode(true);
        }
    };
    private final IOverlayManager mOverlayManager = IOverlayManager.Stub.asInterface(ServiceManager.getService("overlay"));

    /* loaded from: classes.dex */
    public interface ModeChangedListener {
        void onNavigationModeChanged(int i);
    }

    public NavigationModeController(Context context, DeviceProvisionedController deviceProvisionedController, ConfigurationController configurationController, Executor executor) {
        AnonymousClass1 r0 = new DeviceProvisionedController.DeviceProvisionedListener() { // from class: com.android.systemui.navigationbar.NavigationModeController.1
            @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
            public void onUserSwitched() {
                String str = NavigationModeController.TAG;
                Log.d(str, "onUserSwitched: " + ActivityManagerWrapper.getInstance().getCurrentUserId());
                NavigationModeController.this.updateCurrentInteractionMode(true);
            }
        };
        this.mDeviceProvisionedCallback = r0;
        this.mContext = context;
        this.mCurrentUserContext = context;
        this.mUiBgExecutor = executor;
        deviceProvisionedController.addCallback(r0);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.OVERLAY_CHANGED");
        intentFilter.addDataScheme("package");
        intentFilter.addDataSchemeSpecificPart("android", 0);
        context.registerReceiverAsUser(this.mReceiver, UserHandle.ALL, intentFilter, null, null);
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.navigationbar.NavigationModeController.3
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onOverlayChanged() {
                Log.d(NavigationModeController.TAG, "onOverlayChanged");
                NavigationModeController.this.updateCurrentInteractionMode(true);
            }
        });
        updateCurrentInteractionMode(false);
    }

    public void updateCurrentInteractionMode(boolean z) {
        Context currentUserContext = getCurrentUserContext();
        this.mCurrentUserContext = currentUserContext;
        int currentInteractionMode = getCurrentInteractionMode(currentUserContext);
        this.mUiBgExecutor.execute(new Runnable(currentInteractionMode) { // from class: com.android.systemui.navigationbar.NavigationModeController$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                NavigationModeController.this.lambda$updateCurrentInteractionMode$0(this.f$1);
            }
        });
        String str = TAG;
        Log.d(str, "updateCurrentInteractionMode: mode=" + currentInteractionMode);
        dumpAssetPaths(this.mCurrentUserContext);
        if (z) {
            for (int i = 0; i < this.mListeners.size(); i++) {
                this.mListeners.get(i).onNavigationModeChanged(currentInteractionMode);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateCurrentInteractionMode$0(int i) {
        Settings.Secure.putString(this.mCurrentUserContext.getContentResolver(), "navigation_mode", String.valueOf(i));
    }

    public int addListener(ModeChangedListener modeChangedListener) {
        this.mListeners.add(modeChangedListener);
        return getCurrentInteractionMode(this.mCurrentUserContext);
    }

    public void removeListener(ModeChangedListener modeChangedListener) {
        this.mListeners.remove(modeChangedListener);
    }

    private int getCurrentInteractionMode(Context context) {
        int integer = context.getResources().getInteger(17694862);
        String str = TAG;
        Log.d(str, "getCurrentInteractionMode: mode=" + integer + " contextUser=" + context.getUserId());
        return integer;
    }

    public Context getCurrentUserContext() {
        int currentUserId = ActivityManagerWrapper.getInstance().getCurrentUserId();
        String str = TAG;
        Log.d(str, "getCurrentUserContext: contextUser=" + this.mContext.getUserId() + " currentUser=" + currentUserId);
        if (this.mContext.getUserId() == currentUserId) {
            return this.mContext;
        }
        try {
            Context context = this.mContext;
            return context.createPackageContextAsUser(context.getPackageName(), 0, UserHandle.of(currentUserId));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to create package context", e);
            return null;
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        printWriter.println("NavigationModeController:");
        printWriter.println("  mode=" + getCurrentInteractionMode(this.mCurrentUserContext));
        try {
            str = String.join(", ", this.mOverlayManager.getDefaultOverlayPackages());
        } catch (RemoteException unused) {
            str = "failed_to_fetch";
        }
        printWriter.println("  defaultOverlays=" + str);
        dumpAssetPaths(this.mCurrentUserContext);
    }

    private void dumpAssetPaths(Context context) {
        String str = TAG;
        Log.d(str, "  contextUser=" + this.mCurrentUserContext.getUserId());
        Log.d(str, "  assetPaths=");
        ApkAssets[] apkAssets = context.getResources().getAssets().getApkAssets();
        for (ApkAssets apkAssets2 : apkAssets) {
            Log.d(TAG, "    " + apkAssets2.getDebugName());
        }
    }
}
