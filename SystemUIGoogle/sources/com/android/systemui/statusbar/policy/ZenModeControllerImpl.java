package com.android.systemui.statusbar.policy;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.service.notification.ZenModeConfig;
import android.text.format.DateFormat;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.qs.GlobalSetting;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.util.Utils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class ZenModeControllerImpl extends CurrentUserTracker implements ZenModeController, Dumpable {
    private static final boolean DEBUG = Log.isLoggable("ZenModeController", 3);
    private final AlarmManager mAlarmManager;
    private ZenModeConfig mConfig;
    private final GlobalSetting mConfigSetting;
    private NotificationManager.Policy mConsolidatedNotificationPolicy;
    private final Context mContext;
    private final GlobalSetting mModeSetting;
    private final NotificationManager mNoMan;
    private boolean mRegistered;
    private final SetupObserver mSetupObserver;
    private int mUserId;
    private final UserManager mUserManager;
    private int mZenMode;
    private long mZenUpdateTime;
    private final ArrayList<ZenModeController.Callback> mCallbacks = new ArrayList<>();
    private final Object mCallbacksLock = new Object();
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.policy.ZenModeControllerImpl.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.app.action.NEXT_ALARM_CLOCK_CHANGED".equals(intent.getAction())) {
                ZenModeControllerImpl.this.fireNextAlarmChanged();
            }
            if ("android.os.action.ACTION_EFFECTS_SUPPRESSOR_CHANGED".equals(intent.getAction())) {
                ZenModeControllerImpl.this.fireEffectsSuppressorChanged();
            }
        }
    };

    public ZenModeControllerImpl(Context context, Handler handler, BroadcastDispatcher broadcastDispatcher) {
        super(broadcastDispatcher);
        this.mContext = context;
        AnonymousClass1 r5 = new GlobalSetting(context, handler, "zen_mode") { // from class: com.android.systemui.statusbar.policy.ZenModeControllerImpl.1
            @Override // com.android.systemui.qs.GlobalSetting
            protected void handleValueChanged(int i) {
                ZenModeControllerImpl.this.updateZenMode(i);
                ZenModeControllerImpl.this.fireZenChanged(i);
            }
        };
        this.mModeSetting = r5;
        AnonymousClass2 r0 = new GlobalSetting(context, handler, "zen_mode_config_etag") { // from class: com.android.systemui.statusbar.policy.ZenModeControllerImpl.2
            @Override // com.android.systemui.qs.GlobalSetting
            protected void handleValueChanged(int i) {
                ZenModeControllerImpl.this.updateZenModeConfig();
            }
        };
        this.mConfigSetting = r0;
        this.mNoMan = (NotificationManager) context.getSystemService("notification");
        r5.setListening(true);
        updateZenMode(r5.getValue());
        r0.setListening(true);
        updateZenModeConfig();
        updateConsolidatedNotificationPolicy();
        this.mAlarmManager = (AlarmManager) context.getSystemService("alarm");
        SetupObserver setupObserver = new SetupObserver(handler);
        this.mSetupObserver = setupObserver;
        setupObserver.register();
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        startTracking();
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController
    public boolean isVolumeRestricted() {
        return this.mUserManager.hasUserRestriction("no_adjust_volume", new UserHandle(this.mUserId));
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController
    public boolean areNotificationsHiddenInShade() {
        if (this.mZenMode == 0 || (this.mConsolidatedNotificationPolicy.suppressedVisualEffects & 256) == 0) {
            return false;
        }
        return true;
    }

    public void addCallback(ZenModeController.Callback callback) {
        synchronized (this.mCallbacksLock) {
            this.mCallbacks.add(callback);
        }
    }

    public void removeCallback(ZenModeController.Callback callback) {
        synchronized (this.mCallbacksLock) {
            this.mCallbacks.remove(callback);
        }
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController
    public int getZen() {
        return this.mZenMode;
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController
    public void setZen(int i, Uri uri, String str) {
        this.mNoMan.setZenMode(i, uri, str);
    }

    public boolean isZenAvailable() {
        return this.mSetupObserver.isDeviceProvisioned() && this.mSetupObserver.isUserSetup();
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController
    public ZenModeConfig.ZenRule getManualRule() {
        ZenModeConfig zenModeConfig = this.mConfig;
        if (zenModeConfig == null) {
            return null;
        }
        return zenModeConfig.manualRule;
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController
    public ZenModeConfig getConfig() {
        return this.mConfig;
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController
    public NotificationManager.Policy getConsolidatedPolicy() {
        return this.mConsolidatedNotificationPolicy;
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController
    public long getNextAlarm() {
        AlarmManager.AlarmClockInfo nextAlarmClock = this.mAlarmManager.getNextAlarmClock(this.mUserId);
        if (nextAlarmClock != null) {
            return nextAlarmClock.getTriggerTime();
        }
        return 0;
    }

    @Override // com.android.systemui.settings.CurrentUserTracker
    public void onUserSwitched(int i) {
        this.mUserId = i;
        if (this.mRegistered) {
            this.mContext.unregisterReceiver(this.mReceiver);
        }
        IntentFilter intentFilter = new IntentFilter("android.app.action.NEXT_ALARM_CLOCK_CHANGED");
        intentFilter.addAction("android.os.action.ACTION_EFFECTS_SUPPRESSOR_CHANGED");
        this.mContext.registerReceiverAsUser(this.mReceiver, new UserHandle(this.mUserId), intentFilter, null, null);
        this.mRegistered = true;
        this.mSetupObserver.register();
    }

    /* access modifiers changed from: private */
    public void fireNextAlarmChanged() {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, ZenModeControllerImpl$$ExternalSyntheticLambda6.INSTANCE);
        }
    }

    /* access modifiers changed from: private */
    public void fireEffectsSuppressorChanged() {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, ZenModeControllerImpl$$ExternalSyntheticLambda5.INSTANCE);
        }
    }

    /* access modifiers changed from: private */
    public void fireZenChanged(int i) {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new Consumer(i) { // from class: com.android.systemui.statusbar.policy.ZenModeControllerImpl$$ExternalSyntheticLambda0
                public final /* synthetic */ int f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ZenModeControllerImpl.$r8$lambda$lX1vyT0eEeBQWh5qXgCH2FzjcYs(this.f$0, (ZenModeController.Callback) obj);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void fireZenAvailableChanged(boolean z) {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new Consumer(z) { // from class: com.android.systemui.statusbar.policy.ZenModeControllerImpl$$ExternalSyntheticLambda4
                public final /* synthetic */ boolean f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ZenModeControllerImpl.$r8$lambda$IKMACfoM1WucNG9GgDEG0xfVoOM(this.f$0, (ZenModeController.Callback) obj);
                }
            });
        }
    }

    private void fireManualRuleChanged(ZenModeConfig.ZenRule zenRule) {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new Consumer(zenRule) { // from class: com.android.systemui.statusbar.policy.ZenModeControllerImpl$$ExternalSyntheticLambda2
                public final /* synthetic */ ZenModeConfig.ZenRule f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ZenModeControllerImpl.m382$r8$lambda$PBhm2cO8Qvwhi01X7Z489FB0Ms(this.f$0, (ZenModeController.Callback) obj);
                }
            });
        }
    }

    private void fireConsolidatedPolicyChanged(NotificationManager.Policy policy) {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new Consumer(policy) { // from class: com.android.systemui.statusbar.policy.ZenModeControllerImpl$$ExternalSyntheticLambda1
                public final /* synthetic */ NotificationManager.Policy f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ZenModeControllerImpl.$r8$lambda$kC2b1sOy4JEEKPMdltnWZ8Stbyo(this.f$0, (ZenModeController.Callback) obj);
                }
            });
        }
    }

    @VisibleForTesting
    protected void fireConfigChanged(ZenModeConfig zenModeConfig) {
        synchronized (this.mCallbacksLock) {
            Utils.safeForeach(this.mCallbacks, new Consumer(zenModeConfig) { // from class: com.android.systemui.statusbar.policy.ZenModeControllerImpl$$ExternalSyntheticLambda3
                public final /* synthetic */ ZenModeConfig f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ZenModeControllerImpl.$r8$lambda$UYqFTPP5XejjhfEinRX46v5Yyvo(this.f$0, (ZenModeController.Callback) obj);
                }
            });
        }
    }

    @VisibleForTesting
    protected void updateZenMode(int i) {
        this.mZenMode = i;
        this.mZenUpdateTime = System.currentTimeMillis();
    }

    @VisibleForTesting
    protected void updateConsolidatedNotificationPolicy() {
        NotificationManager.Policy consolidatedNotificationPolicy = this.mNoMan.getConsolidatedNotificationPolicy();
        if (!Objects.equals(consolidatedNotificationPolicy, this.mConsolidatedNotificationPolicy)) {
            this.mConsolidatedNotificationPolicy = consolidatedNotificationPolicy;
            fireConsolidatedPolicyChanged(consolidatedNotificationPolicy);
        }
    }

    @VisibleForTesting
    protected void updateZenModeConfig() {
        ZenModeConfig zenModeConfig = this.mNoMan.getZenModeConfig();
        if (!Objects.equals(zenModeConfig, this.mConfig)) {
            ZenModeConfig zenModeConfig2 = this.mConfig;
            ZenModeConfig.ZenRule zenRule = null;
            ZenModeConfig.ZenRule zenRule2 = zenModeConfig2 != null ? zenModeConfig2.manualRule : null;
            this.mConfig = zenModeConfig;
            this.mZenUpdateTime = System.currentTimeMillis();
            fireConfigChanged(zenModeConfig);
            if (zenModeConfig != null) {
                zenRule = zenModeConfig.manualRule;
            }
            if (!Objects.equals(zenRule2, zenRule)) {
                fireManualRuleChanged(zenRule);
            }
            NotificationManager.Policy consolidatedNotificationPolicy = this.mNoMan.getConsolidatedNotificationPolicy();
            if (!Objects.equals(consolidatedNotificationPolicy, this.mConsolidatedNotificationPolicy)) {
                this.mConsolidatedNotificationPolicy = consolidatedNotificationPolicy;
                fireConsolidatedPolicyChanged(consolidatedNotificationPolicy);
            }
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("ZenModeControllerImpl:");
        printWriter.println("  mZenMode=" + this.mZenMode);
        printWriter.println("  mConfig=" + this.mConfig);
        printWriter.println("  mConsolidatedNotificationPolicy=" + this.mConsolidatedNotificationPolicy);
        printWriter.println("  mZenUpdateTime=" + ((Object) DateFormat.format("MM-dd HH:mm:ss", this.mZenUpdateTime)));
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class SetupObserver extends ContentObserver {
        private boolean mRegistered;
        private final ContentResolver mResolver;

        public SetupObserver(Handler handler) {
            super(handler);
            this.mResolver = ZenModeControllerImpl.this.mContext.getContentResolver();
        }

        public boolean isUserSetup() {
            return Settings.Secure.getIntForUser(this.mResolver, "user_setup_complete", 0, ZenModeControllerImpl.this.mUserId) != 0;
        }

        public boolean isDeviceProvisioned() {
            return Settings.Global.getInt(this.mResolver, "device_provisioned", 0) != 0;
        }

        public void register() {
            if (this.mRegistered) {
                this.mResolver.unregisterContentObserver(this);
            }
            this.mResolver.registerContentObserver(Settings.Global.getUriFor("device_provisioned"), false, this);
            this.mResolver.registerContentObserver(Settings.Secure.getUriFor("user_setup_complete"), false, this, ZenModeControllerImpl.this.mUserId);
            this.mRegistered = true;
            ZenModeControllerImpl zenModeControllerImpl = ZenModeControllerImpl.this;
            zenModeControllerImpl.fireZenAvailableChanged(zenModeControllerImpl.isZenAvailable());
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            if (Settings.Global.getUriFor("device_provisioned").equals(uri) || Settings.Secure.getUriFor("user_setup_complete").equals(uri)) {
                ZenModeControllerImpl zenModeControllerImpl = ZenModeControllerImpl.this;
                zenModeControllerImpl.fireZenAvailableChanged(zenModeControllerImpl.isZenAvailable());
            }
        }
    }
}
