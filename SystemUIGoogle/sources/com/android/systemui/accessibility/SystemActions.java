package com.android.systemui.accessibility;

import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.Icon;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Log;
import android.view.IWindowManager;
import android.view.KeyEvent;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.accessibility.dialog.AccessibilityButtonChooserActivity;
import com.android.internal.util.ScreenshotHelper;
import com.android.systemui.SystemUI;
import com.android.systemui.recents.Recents;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarWindowCallback;
import com.android.systemui.util.Assert;
import dagger.Lazy;
import java.util.Locale;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class SystemActions extends SystemUI {
    private boolean mDismissNotificationShadeActionRegistered;
    private final NotificationShadeWindowController mNotificationShadeController;
    private final Recents mRecents;
    private final Lazy<StatusBar> mStatusBar;
    private final SystemActionsBroadcastReceiver mReceiver = new SystemActionsBroadcastReceiver();
    private Locale mLocale = this.mContext.getResources().getConfiguration().getLocales().get(0);
    private final AccessibilityManager mA11yManager = (AccessibilityManager) this.mContext.getSystemService("accessibility");
    private final StatusBarWindowCallback mNotificationShadeCallback = new StatusBarWindowCallback() { // from class: com.android.systemui.accessibility.SystemActions$$ExternalSyntheticLambda0
        @Override // com.android.systemui.statusbar.phone.StatusBarWindowCallback
        public final void onStateChanged(boolean z, boolean z2, boolean z3) {
            SystemActions.m50$r8$lambda$utnwEEQvm8Mv9_tRtcZGSPwEMQ(SystemActions.this, z, z2, z3);
        }
    };

    public SystemActions(Context context, NotificationShadeWindowController notificationShadeWindowController, Lazy<StatusBar> lazy, Recents recents) {
        super(context);
        this.mRecents = recents;
        this.mNotificationShadeController = notificationShadeWindowController;
        this.mStatusBar = lazy;
    }

    public /* synthetic */ void lambda$new$0(boolean z, boolean z2, boolean z3) {
        registerOrUnregisterDismissNotificationShadeAction();
    }

    @Override // com.android.systemui.SystemUI
    public void start() {
        this.mNotificationShadeController.registerCallback(this.mNotificationShadeCallback);
        Context context = this.mContext;
        SystemActionsBroadcastReceiver systemActionsBroadcastReceiver = this.mReceiver;
        context.registerReceiverForAllUsers(systemActionsBroadcastReceiver, systemActionsBroadcastReceiver.createIntentFilter(), "com.android.systemui.permission.SELF", null);
        registerActions();
    }

    @Override // com.android.systemui.SystemUI
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Locale locale = this.mContext.getResources().getConfiguration().getLocales().get(0);
        if (!locale.equals(this.mLocale)) {
            this.mLocale = locale;
            registerActions();
        }
    }

    private void registerActions() {
        RemoteAction createRemoteAction = createRemoteAction(17039597, "SYSTEM_ACTION_BACK");
        RemoteAction createRemoteAction2 = createRemoteAction(17039600, "SYSTEM_ACTION_HOME");
        RemoteAction createRemoteAction3 = createRemoteAction(17039607, "SYSTEM_ACTION_RECENTS");
        RemoteAction createRemoteAction4 = createRemoteAction(17039602, "SYSTEM_ACTION_NOTIFICATIONS");
        RemoteAction createRemoteAction5 = createRemoteAction(17039606, "SYSTEM_ACTION_QUICK_SETTINGS");
        RemoteAction createRemoteAction6 = createRemoteAction(17039605, "SYSTEM_ACTION_POWER_DIALOG");
        RemoteAction createRemoteAction7 = createRemoteAction(17039601, "SYSTEM_ACTION_LOCK_SCREEN");
        RemoteAction createRemoteAction8 = createRemoteAction(17039608, "SYSTEM_ACTION_TAKE_SCREENSHOT");
        RemoteAction createRemoteAction9 = createRemoteAction(17039599, "SYSTEM_ACTION_ACCESSIBILITY_SHORTCUT");
        this.mA11yManager.registerSystemAction(createRemoteAction, 1);
        this.mA11yManager.registerSystemAction(createRemoteAction2, 2);
        this.mA11yManager.registerSystemAction(createRemoteAction3, 3);
        this.mA11yManager.registerSystemAction(createRemoteAction4, 4);
        this.mA11yManager.registerSystemAction(createRemoteAction5, 5);
        this.mA11yManager.registerSystemAction(createRemoteAction6, 6);
        this.mA11yManager.registerSystemAction(createRemoteAction7, 8);
        this.mA11yManager.registerSystemAction(createRemoteAction8, 9);
        this.mA11yManager.registerSystemAction(createRemoteAction9, 13);
        registerOrUnregisterDismissNotificationShadeAction();
    }

    private void registerOrUnregisterDismissNotificationShadeAction() {
        Assert.isMainThread();
        StatusBar statusBar = this.mStatusBar.get();
        if (!statusBar.isPanelExpanded() || statusBar.isKeyguardShowing()) {
            if (this.mDismissNotificationShadeActionRegistered) {
                this.mA11yManager.unregisterSystemAction(15);
                this.mDismissNotificationShadeActionRegistered = false;
            }
        } else if (!this.mDismissNotificationShadeActionRegistered) {
            this.mA11yManager.registerSystemAction(createRemoteAction(17039598, "SYSTEM_ACTION_ACCESSIBILITY_DISMISS_NOTIFICATION_SHADE"), 15);
            this.mDismissNotificationShadeActionRegistered = true;
        }
    }

    public void register(int i) {
        String str;
        int i2;
        switch (i) {
            case 1:
                i2 = 17039597;
                str = "SYSTEM_ACTION_BACK";
                break;
            case 2:
                i2 = 17039600;
                str = "SYSTEM_ACTION_HOME";
                break;
            case 3:
                i2 = 17039607;
                str = "SYSTEM_ACTION_RECENTS";
                break;
            case 4:
                i2 = 17039602;
                str = "SYSTEM_ACTION_NOTIFICATIONS";
                break;
            case 5:
                i2 = 17039606;
                str = "SYSTEM_ACTION_QUICK_SETTINGS";
                break;
            case 6:
                i2 = 17039605;
                str = "SYSTEM_ACTION_POWER_DIALOG";
                break;
            case 7:
            case 10:
            case 14:
            default:
                return;
            case 8:
                i2 = 17039601;
                str = "SYSTEM_ACTION_LOCK_SCREEN";
                break;
            case 9:
                i2 = 17039608;
                str = "SYSTEM_ACTION_TAKE_SCREENSHOT";
                break;
            case 11:
                i2 = 17039604;
                str = "SYSTEM_ACTION_ACCESSIBILITY_BUTTON";
                break;
            case 12:
                i2 = 17039603;
                str = "SYSTEM_ACTION_ACCESSIBILITY_BUTTON_MENU";
                break;
            case 13:
                i2 = 17039599;
                str = "SYSTEM_ACTION_ACCESSIBILITY_SHORTCUT";
                break;
            case 15:
                i2 = 17039598;
                str = "SYSTEM_ACTION_ACCESSIBILITY_DISMISS_NOTIFICATION_SHADE";
                break;
        }
        this.mA11yManager.registerSystemAction(createRemoteAction(i2, str), i);
    }

    private RemoteAction createRemoteAction(int i, String str) {
        return new RemoteAction(Icon.createWithResource(this.mContext, 17301684), this.mContext.getString(i), this.mContext.getString(i), this.mReceiver.createPendingIntent(this.mContext, str));
    }

    public void unregister(int i) {
        this.mA11yManager.unregisterSystemAction(i);
    }

    public void handleBack() {
        sendDownAndUpKeyEvents(4);
    }

    public void handleHome() {
        sendDownAndUpKeyEvents(3);
    }

    private void sendDownAndUpKeyEvents(int i) {
        long uptimeMillis = SystemClock.uptimeMillis();
        sendKeyEventIdentityCleared(i, 0, uptimeMillis, uptimeMillis);
        sendKeyEventIdentityCleared(i, 1, uptimeMillis, SystemClock.uptimeMillis());
    }

    private void sendKeyEventIdentityCleared(int i, int i2, long j, long j2) {
        KeyEvent obtain = KeyEvent.obtain(j, j2, i2, i, 0, 0, -1, 0, 8, 257, null);
        InputManager.getInstance().injectInputEvent(obtain, 0);
        obtain.recycle();
    }

    public void handleRecents() {
        this.mRecents.toggleRecentApps();
    }

    public void handleNotifications() {
        this.mStatusBar.get().animateExpandNotificationsPanel();
    }

    public void handleQuickSettings() {
        this.mStatusBar.get().animateExpandSettingsPanel(null);
    }

    public void handlePowerDialog() {
        try {
            WindowManagerGlobal.getWindowManagerService().showGlobalActions();
        } catch (RemoteException unused) {
            Log.e("SystemActions", "failed to display power dialog.");
        }
    }

    public void handleLockScreen() {
        IWindowManager windowManagerService = WindowManagerGlobal.getWindowManagerService();
        ((PowerManager) this.mContext.getSystemService(PowerManager.class)).goToSleep(SystemClock.uptimeMillis(), 7, 0);
        try {
            windowManagerService.lockNow((Bundle) null);
        } catch (RemoteException unused) {
            Log.e("SystemActions", "failed to lock screen.");
        }
    }

    public void handleTakeScreenshot() {
        new ScreenshotHelper(this.mContext).takeScreenshot(1, true, true, 4, new Handler(Looper.getMainLooper()), (Consumer) null);
    }

    public void handleAccessibilityButton() {
        AccessibilityManager.getInstance(this.mContext).notifyAccessibilityButtonClicked(0);
    }

    public void handleAccessibilityButtonChooser() {
        Intent intent = new Intent("com.android.internal.intent.action.CHOOSE_ACCESSIBILITY_BUTTON");
        intent.addFlags(268468224);
        intent.setClassName("android", AccessibilityButtonChooserActivity.class.getName());
        this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
    }

    public void handleAccessibilityShortcut() {
        this.mA11yManager.performAccessibilityShortcut();
    }

    public void handleAccessibilityDismissNotificationShade() {
        this.mStatusBar.get().animateCollapsePanels(0, false);
    }

    /* loaded from: classes.dex */
    public class SystemActionsBroadcastReceiver extends BroadcastReceiver {
        private SystemActionsBroadcastReceiver() {
            SystemActions.this = r1;
        }

        public PendingIntent createPendingIntent(Context context, String str) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1103811776:
                    if (str.equals("SYSTEM_ACTION_BACK")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1103619272:
                    if (str.equals("SYSTEM_ACTION_HOME")) {
                        c = 1;
                        break;
                    }
                    break;
                case -720484549:
                    if (str.equals("SYSTEM_ACTION_POWER_DIALOG")) {
                        c = 2;
                        break;
                    }
                    break;
                case -535129457:
                    if (str.equals("SYSTEM_ACTION_NOTIFICATIONS")) {
                        c = 3;
                        break;
                    }
                    break;
                case -181386672:
                    if (str.equals("SYSTEM_ACTION_ACCESSIBILITY_SHORTCUT")) {
                        c = 4;
                        break;
                    }
                    break;
                case -153384569:
                    if (str.equals("SYSTEM_ACTION_LOCK_SCREEN")) {
                        c = 5;
                        break;
                    }
                    break;
                case 42571871:
                    if (str.equals("SYSTEM_ACTION_RECENTS")) {
                        c = 6;
                        break;
                    }
                    break;
                case 526987266:
                    if (str.equals("SYSTEM_ACTION_ACCESSIBILITY_BUTTON_MENU")) {
                        c = 7;
                        break;
                    }
                    break;
                case 1245940668:
                    if (str.equals("SYSTEM_ACTION_ACCESSIBILITY_BUTTON")) {
                        c = '\b';
                        break;
                    }
                    break;
                case 1579999269:
                    if (str.equals("SYSTEM_ACTION_TAKE_SCREENSHOT")) {
                        c = '\t';
                        break;
                    }
                    break;
                case 1668921710:
                    if (str.equals("SYSTEM_ACTION_QUICK_SETTINGS")) {
                        c = '\n';
                        break;
                    }
                    break;
                case 1894867256:
                    if (str.equals("SYSTEM_ACTION_ACCESSIBILITY_DISMISS_NOTIFICATION_SHADE")) {
                        c = 11;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case '\b':
                case '\t':
                case '\n':
                case 11:
                    Intent intent = new Intent(str);
                    intent.setPackage(context.getPackageName());
                    return PendingIntent.getBroadcast(context, 0, intent, 67108864);
                default:
                    return null;
            }
        }

        public IntentFilter createIntentFilter() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("SYSTEM_ACTION_BACK");
            intentFilter.addAction("SYSTEM_ACTION_HOME");
            intentFilter.addAction("SYSTEM_ACTION_RECENTS");
            intentFilter.addAction("SYSTEM_ACTION_NOTIFICATIONS");
            intentFilter.addAction("SYSTEM_ACTION_QUICK_SETTINGS");
            intentFilter.addAction("SYSTEM_ACTION_POWER_DIALOG");
            intentFilter.addAction("SYSTEM_ACTION_LOCK_SCREEN");
            intentFilter.addAction("SYSTEM_ACTION_TAKE_SCREENSHOT");
            intentFilter.addAction("SYSTEM_ACTION_ACCESSIBILITY_BUTTON");
            intentFilter.addAction("SYSTEM_ACTION_ACCESSIBILITY_BUTTON_MENU");
            intentFilter.addAction("SYSTEM_ACTION_ACCESSIBILITY_SHORTCUT");
            intentFilter.addAction("SYSTEM_ACTION_ACCESSIBILITY_DISMISS_NOTIFICATION_SHADE");
            return intentFilter;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action.hashCode();
            char c = 65535;
            switch (action.hashCode()) {
                case -1103811776:
                    if (action.equals("SYSTEM_ACTION_BACK")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1103619272:
                    if (action.equals("SYSTEM_ACTION_HOME")) {
                        c = 1;
                        break;
                    }
                    break;
                case -720484549:
                    if (action.equals("SYSTEM_ACTION_POWER_DIALOG")) {
                        c = 2;
                        break;
                    }
                    break;
                case -535129457:
                    if (action.equals("SYSTEM_ACTION_NOTIFICATIONS")) {
                        c = 3;
                        break;
                    }
                    break;
                case -181386672:
                    if (action.equals("SYSTEM_ACTION_ACCESSIBILITY_SHORTCUT")) {
                        c = 4;
                        break;
                    }
                    break;
                case -153384569:
                    if (action.equals("SYSTEM_ACTION_LOCK_SCREEN")) {
                        c = 5;
                        break;
                    }
                    break;
                case 42571871:
                    if (action.equals("SYSTEM_ACTION_RECENTS")) {
                        c = 6;
                        break;
                    }
                    break;
                case 526987266:
                    if (action.equals("SYSTEM_ACTION_ACCESSIBILITY_BUTTON_MENU")) {
                        c = 7;
                        break;
                    }
                    break;
                case 1245940668:
                    if (action.equals("SYSTEM_ACTION_ACCESSIBILITY_BUTTON")) {
                        c = '\b';
                        break;
                    }
                    break;
                case 1579999269:
                    if (action.equals("SYSTEM_ACTION_TAKE_SCREENSHOT")) {
                        c = '\t';
                        break;
                    }
                    break;
                case 1668921710:
                    if (action.equals("SYSTEM_ACTION_QUICK_SETTINGS")) {
                        c = '\n';
                        break;
                    }
                    break;
                case 1894867256:
                    if (action.equals("SYSTEM_ACTION_ACCESSIBILITY_DISMISS_NOTIFICATION_SHADE")) {
                        c = 11;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    SystemActions.this.handleBack();
                    return;
                case 1:
                    SystemActions.this.handleHome();
                    return;
                case 2:
                    SystemActions.this.handlePowerDialog();
                    return;
                case 3:
                    SystemActions.this.handleNotifications();
                    return;
                case 4:
                    SystemActions.this.handleAccessibilityShortcut();
                    return;
                case 5:
                    SystemActions.this.handleLockScreen();
                    return;
                case 6:
                    SystemActions.this.handleRecents();
                    return;
                case 7:
                    SystemActions.this.handleAccessibilityButtonChooser();
                    return;
                case '\b':
                    SystemActions.this.handleAccessibilityButton();
                    return;
                case '\t':
                    SystemActions.this.handleTakeScreenshot();
                    return;
                case '\n':
                    SystemActions.this.handleQuickSettings();
                    return;
                case 11:
                    SystemActions.this.handleAccessibilityDismissNotificationShade();
                    return;
                default:
                    return;
            }
        }
    }
}
