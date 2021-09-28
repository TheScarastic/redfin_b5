package com.google.android.systemui.columbus.actions;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.PendingIntent;
import android.app.SynchronousUserSwitchObserver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import com.google.android.systemui.columbus.ColumbusEvent;
import com.google.android.systemui.columbus.ColumbusSettings;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.gates.KeyguardVisibility;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LaunchApp.kt */
/* loaded from: classes2.dex */
public final class LaunchApp extends UserAction {
    public static final Companion Companion = new Companion(null);
    private final Handler bgHandler;
    private String currentApp;
    private String currentShortcut;
    private final LauncherApps launcherApps;
    private final Handler mainHandler;
    private final UiEventLogger uiEventLogger;
    private final UserManager userManager;
    private final LaunchApp$userSwitchCallback$1 userSwitchCallback;
    private final String tag = "Columbus/LaunchApp";
    private final LaunchApp$settingsListener$1 settingsListener = new ColumbusSettings.ColumbusSettingsChangeListener(this) { // from class: com.google.android.systemui.columbus.actions.LaunchApp$settingsListener$1
        final /* synthetic */ LaunchApp this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
        public void onAlertSilenceEnabledChange(boolean z) {
            ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onAlertSilenceEnabledChange(this, z);
        }

        @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
        public void onColumbusEnabledChange(boolean z) {
            ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onColumbusEnabledChange(this, z);
        }

        @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
        public void onLowSensitivityChange(boolean z) {
            ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onLowSensitivityChange(this, z);
        }

        @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
        public void onSelectedActionChange(String str) {
            ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onSelectedActionChange(this, str);
        }

        @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
        public void onUseApSensorChange(boolean z) {
            ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onUseApSensorChange(this, z);
        }

        @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
        public void onSelectedAppChange(String str) {
            Intrinsics.checkNotNullParameter(str, "selectedApp");
            this.this$0.currentApp = str;
            this.this$0.updateAvailable();
        }

        @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
        public void onSelectedAppShortcutChange(String str) {
            Intrinsics.checkNotNullParameter(str, "selectedShortcut");
            this.this$0.currentShortcut = str;
            this.this$0.updateAvailable();
        }
    };
    private final LaunchApp$broadcastReceiver$1 broadcastReceiver = new BroadcastReceiver(this) { // from class: com.google.android.systemui.columbus.actions.LaunchApp$broadcastReceiver$1
        final /* synthetic */ LaunchApp this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            this.this$0.updateAvailableAppsAndShortcutsAsync();
        }
    };
    private final LaunchApp$gateListener$1 gateListener = new Gate.Listener(this) { // from class: com.google.android.systemui.columbus.actions.LaunchApp$gateListener$1
        final /* synthetic */ LaunchApp this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // com.google.android.systemui.columbus.gates.Gate.Listener
        public void onGateChanged(Gate gate) {
            Intrinsics.checkNotNullParameter(gate, "gate");
            if (!gate.isBlocking()) {
                this.this$0.updateAvailableAppsAndShortcutsAsync();
            }
        }
    };
    private final Map<String, PendingIntent> availableApps = new LinkedHashMap();
    private final Map<String, ShortcutInfo> availableShortcuts = new LinkedHashMap();

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public LaunchApp(Context context, LauncherApps launcherApps, IActivityManager iActivityManager, UserManager userManager, ColumbusSettings columbusSettings, KeyguardVisibility keyguardVisibility, Handler handler, Handler handler2, UiEventLogger uiEventLogger) {
        super(context, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(launcherApps, "launcherApps");
        Intrinsics.checkNotNullParameter(iActivityManager, "activityManagerService");
        Intrinsics.checkNotNullParameter(userManager, "userManager");
        Intrinsics.checkNotNullParameter(columbusSettings, "columbusSettings");
        Intrinsics.checkNotNullParameter(keyguardVisibility, "keyguardVisibility");
        Intrinsics.checkNotNullParameter(handler, "mainHandler");
        Intrinsics.checkNotNullParameter(handler2, "bgHandler");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        this.launcherApps = launcherApps;
        this.userManager = userManager;
        this.mainHandler = handler;
        this.bgHandler = handler2;
        this.uiEventLogger = uiEventLogger;
        LaunchApp$userSwitchCallback$1 launchApp$userSwitchCallback$1 = new SynchronousUserSwitchObserver(this) { // from class: com.google.android.systemui.columbus.actions.LaunchApp$userSwitchCallback$1
            final /* synthetic */ LaunchApp this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            public void onUserSwitching(int i) throws RemoteException {
                this.this$0.updateAvailableAppsAndShortcutsAsync();
            }
        };
        this.userSwitchCallback = launchApp$userSwitchCallback$1;
        this.currentApp = "";
        this.currentShortcut = "";
        try {
            iActivityManager.registerUserSwitchObserver(launchApp$userSwitchCallback$1, "Columbus/LaunchApp");
        } catch (RemoteException e) {
            Log.e("Columbus/LaunchApp", "Failed to register user switch observer", e);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addDataScheme("package");
        context.registerReceiver(this.broadcastReceiver, intentFilter);
        updateAvailableAppsAndShortcutsAsync();
        columbusSettings.registerColumbusSettingsChangeListener(this.settingsListener);
        this.currentApp = columbusSettings.selectedApp();
        this.currentShortcut = columbusSettings.selectedAppShortcut();
        keyguardVisibility.registerListener(this.gateListener);
        updateAvailable();
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        if (usingShortcut()) {
            ShortcutInfo shortcutInfo = this.availableShortcuts.get(this.currentShortcut);
            if (shortcutInfo != null) {
                this.uiEventLogger.log(ColumbusEvent.COLUMBUS_INVOKED_LAUNCH_SHORTCUT);
                this.launcherApps.startShortcut(shortcutInfo, null, null);
                return;
            }
            return;
        }
        PendingIntent pendingIntent = this.availableApps.get(this.currentApp);
        if (pendingIntent != null) {
            this.uiEventLogger.log(ColumbusEvent.COLUMBUS_INVOKED_LAUNCH_APP);
            pendingIntent.send();
        }
    }

    /* access modifiers changed from: private */
    public final void updateAvailable() {
        if (usingShortcut()) {
            setAvailable(this.availableShortcuts.containsKey(this.currentShortcut));
            return;
        }
        boolean z = true;
        if (!(this.currentApp.length() > 0) || !this.availableApps.containsKey(this.currentApp)) {
            z = false;
        }
        setAvailable(z);
    }

    /* access modifiers changed from: private */
    public final void updateAvailableAppsAndShortcutsAsync() {
        this.bgHandler.post(new Runnable(this) { // from class: com.google.android.systemui.columbus.actions.LaunchApp$updateAvailableAppsAndShortcutsAsync$1
            final /* synthetic */ LaunchApp this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // java.lang.Runnable
            public final void run() {
                int currentUser = ActivityManager.getCurrentUser();
                if (this.this$0.userManager.isUserUnlocked(currentUser)) {
                    this.this$0.availableApps.clear();
                    this.this$0.availableShortcuts.clear();
                    List<LauncherActivityInfo> activityList = this.this$0.launcherApps.getActivityList(null, UserHandle.of(currentUser));
                    List list = this.this$0.getAllShortcutsForUser(currentUser);
                    for (LauncherActivityInfo launcherActivityInfo : activityList) {
                        try {
                            PendingIntent mainActivityLaunchIntent = this.this$0.launcherApps.getMainActivityLaunchIntent(launcherActivityInfo.getComponentName(), null, UserHandle.of(currentUser));
                            if (mainActivityLaunchIntent != null) {
                                Map map = this.this$0.availableApps;
                                String packageName = launcherActivityInfo.getComponentName().getPackageName();
                                Intrinsics.checkNotNullExpressionValue(packageName, "appInfo.componentName.packageName");
                                map.put(packageName, mainActivityLaunchIntent);
                                LaunchApp launchApp = this.this$0;
                                Intrinsics.checkNotNullExpressionValue(launcherActivityInfo, "appInfo");
                                launchApp.addShortcutsForApp(launcherActivityInfo, list);
                            }
                        } catch (RuntimeException unused) {
                        }
                    }
                    Handler handler = this.this$0.mainHandler;
                    final LaunchApp launchApp2 = this.this$0;
                    handler.post(new Runnable() { // from class: com.google.android.systemui.columbus.actions.LaunchApp$updateAvailableAppsAndShortcutsAsync$1.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            launchApp2.updateAvailable();
                        }
                    });
                    return;
                }
                Log.d("Columbus/LaunchApp", "Did not update apps and shortcuts, user " + currentUser + " not unlocked");
            }
        });
    }

    /* access modifiers changed from: private */
    public final List<ShortcutInfo> getAllShortcutsForUser(int i) {
        LauncherApps.ShortcutQuery shortcutQuery = new LauncherApps.ShortcutQuery();
        shortcutQuery.setQueryFlags(9);
        try {
            return this.launcherApps.getShortcuts(shortcutQuery, UserHandle.of(i));
        } catch (Exception e) {
            if ((e instanceof SecurityException) || (e instanceof IllegalStateException)) {
                Log.e("Columbus/LaunchApp", "Failed to query for shortcuts", e);
                return null;
            }
            throw e;
        }
    }

    private final boolean usingShortcut() {
        return (this.currentShortcut.length() > 0) && !Intrinsics.areEqual(this.currentShortcut, this.currentApp);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public String toString() {
        if (!usingShortcut()) {
            return Intrinsics.stringPlus("Launch ", this.currentApp);
        }
        return "Launch " + this.currentApp + " shortcut " + this.currentShortcut;
    }

    /* compiled from: LaunchApp.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* access modifiers changed from: private */
    public final void addShortcutsForApp(LauncherActivityInfo launcherActivityInfo, List<ShortcutInfo> list) {
        if (list != null) {
            ArrayList<ShortcutInfo> arrayList = new ArrayList();
            for (Object obj : list) {
                if (Intrinsics.areEqual(((ShortcutInfo) obj).getPackage(), launcherActivityInfo.getComponentName().getPackageName())) {
                    arrayList.add(obj);
                }
            }
            for (ShortcutInfo shortcutInfo : arrayList) {
                Map<String, ShortcutInfo> map = this.availableShortcuts;
                String id = shortcutInfo.getId();
                Intrinsics.checkNotNullExpressionValue(id, "it.id");
                map.put(id, shortcutInfo);
            }
        }
    }
}
