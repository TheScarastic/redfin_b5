package com.google.android.systemui.statusbar;

import android.app.IActivityManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IBatteryStats;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.fuelgauge.BatteryStatus;
import com.android.systemui.R$anim;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.KeyguardIndication;
import com.android.systemui.keyguard.KeyguardIndicationRotateTextViewController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.DateFormatUtil;
import com.android.systemui.util.wakelock.WakeLock;
import com.google.android.systemui.adaptivecharging.AdaptiveChargingManager;
import com.google.android.systemui.ambientmusic.AmbientIndicationContainer;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
/* loaded from: classes2.dex */
public class KeyguardIndicationControllerGoogle extends KeyguardIndicationController {
    private boolean mAdaptiveChargingActive;
    private boolean mAdaptiveChargingEnabledInSettings;
    @VisibleForTesting
    protected AdaptiveChargingManager mAdaptiveChargingManager;
    private final IBatteryStats mBatteryInfo;
    private int mBatteryLevel;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final Context mContext;
    private final DateFormatUtil mDateFormatUtil;
    private final DeviceConfigProxy mDeviceConfig;
    private long mEstimatedChargeCompletion;
    private boolean mInited;
    private boolean mIsCharging;
    private StatusBar mStatusBar;
    private final TunerService mTunerService;
    private KeyguardUpdateMonitorCallback mUpdateMonitorCallback;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.google.android.systemui.adaptivecharging.ADAPTIVE_CHARGING_DEADLINE_SET")) {
                KeyguardIndicationControllerGoogle.this.triggerAdaptiveChargingStatusUpdate();
            }
        }
    };
    @VisibleForTesting
    protected AdaptiveChargingManager.AdaptiveChargingStatusReceiver mAdaptiveChargingStatusReceiver = new AdaptiveChargingManager.AdaptiveChargingStatusReceiver() { // from class: com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle.2
        @Override // com.google.android.systemui.adaptivecharging.AdaptiveChargingManager.AdaptiveChargingStatusReceiver
        public void onDestroyInterface() {
        }

        @Override // com.google.android.systemui.adaptivecharging.AdaptiveChargingManager.AdaptiveChargingStatusReceiver
        public void onReceiveStatus(String str, int i) {
            boolean z = KeyguardIndicationControllerGoogle.this.mAdaptiveChargingActive;
            KeyguardIndicationControllerGoogle.this.mAdaptiveChargingActive = AdaptiveChargingManager.isStageActiveOrEnabled(str) && i > 0;
            long j = KeyguardIndicationControllerGoogle.this.mEstimatedChargeCompletion;
            KeyguardIndicationControllerGoogle keyguardIndicationControllerGoogle = KeyguardIndicationControllerGoogle.this;
            long currentTimeMillis = System.currentTimeMillis();
            TimeUnit timeUnit = TimeUnit.SECONDS;
            keyguardIndicationControllerGoogle.mEstimatedChargeCompletion = currentTimeMillis + timeUnit.toMillis((long) (i + 29));
            long abs = Math.abs(KeyguardIndicationControllerGoogle.this.mEstimatedChargeCompletion - j);
            if (z != KeyguardIndicationControllerGoogle.this.mAdaptiveChargingActive || (KeyguardIndicationControllerGoogle.this.mAdaptiveChargingActive && abs > timeUnit.toMillis(30))) {
                KeyguardIndicationControllerGoogle.this.updateIndication(true);
            }
        }
    };

    public KeyguardIndicationControllerGoogle(Context context, WakeLock.Builder builder, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, DockManager dockManager, BroadcastDispatcher broadcastDispatcher, DevicePolicyManager devicePolicyManager, IBatteryStats iBatteryStats, UserManager userManager, TunerService tunerService, DeviceConfigProxy deviceConfigProxy, DelayableExecutor delayableExecutor, FalsingManager falsingManager, LockPatternUtils lockPatternUtils, IActivityManager iActivityManager, KeyguardBypassController keyguardBypassController) {
        super(context, builder, keyguardStateController, statusBarStateController, keyguardUpdateMonitor, dockManager, broadcastDispatcher, devicePolicyManager, iBatteryStats, userManager, delayableExecutor, falsingManager, lockPatternUtils, iActivityManager, keyguardBypassController);
        this.mContext = context;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mTunerService = tunerService;
        this.mDeviceConfig = deviceConfigProxy;
        this.mAdaptiveChargingManager = new AdaptiveChargingManager(context);
        this.mBatteryInfo = iBatteryStats;
        this.mDateFormatUtil = new DateFormatUtil(context);
    }

    @Override // com.android.systemui.statusbar.KeyguardIndicationController
    public void init() {
        super.init();
        if (!this.mInited) {
            this.mInited = true;
            this.mTunerService.addTunable(new TunerService.Tunable() { // from class: com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle$$ExternalSyntheticLambda2
                @Override // com.android.systemui.tuner.TunerService.Tunable
                public final void onTuningChanged(String str, String str2) {
                    KeyguardIndicationControllerGoogle.this.lambda$init$0(str, str2);
                }
            }, "adaptive_charging_enabled");
            this.mDeviceConfig.addOnPropertiesChangedListener("adaptive_charging", this.mContext.getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle$$ExternalSyntheticLambda0
                public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                    KeyguardIndicationControllerGoogle.this.lambda$init$1(properties);
                }
            });
            triggerAdaptiveChargingStatusUpdate();
            this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, new IntentFilter("com.google.android.systemui.adaptivecharging.ADAPTIVE_CHARGING_DEADLINE_SET"), null, UserHandle.ALL);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$0(String str, String str2) {
        refreshAdaptiveChargingEnabled();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$1(DeviceConfig.Properties properties) {
        if (properties.getKeyset().contains("adaptive_charging_enabled")) {
            triggerAdaptiveChargingStatusUpdate();
        }
    }

    private void refreshAdaptiveChargingEnabled() {
        this.mAdaptiveChargingEnabledInSettings = this.mAdaptiveChargingManager.isAvailable() && this.mAdaptiveChargingManager.isEnabled();
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.KeyguardIndicationController
    public String computePowerIndication() {
        if (!this.mIsCharging || !this.mAdaptiveChargingEnabledInSettings || !this.mAdaptiveChargingActive) {
            return super.computePowerIndication();
        }
        return this.mContext.getResources().getString(R$string.adaptive_charging_time_estimate, NumberFormat.getPercentInstance().format((double) (((float) this.mBatteryLevel) / 100.0f)), DateFormat.format(DateFormat.getBestDateTimePattern(Locale.getDefault(), this.mDateFormatUtil.is24HourFormat() ? "Hm" : "hma"), this.mEstimatedChargeCompletion).toString());
    }

    @Override // com.android.systemui.statusbar.KeyguardIndicationController
    protected KeyguardUpdateMonitorCallback getKeyguardCallback() {
        if (this.mUpdateMonitorCallback == null) {
            this.mUpdateMonitorCallback = new GoogleKeyguardCallback();
        }
        return this.mUpdateMonitorCallback;
    }

    public void setReverseChargingMessage(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            this.mRotateTextViewController.hideIndication(10);
        } else {
            this.mRotateTextViewController.updateIndication(10, new KeyguardIndication.Builder().setMessage(charSequence).setIcon(this.mContext.getDrawable(R$anim.reverse_charging_animation)).setTextColor(this.mInitialTextColorState).build(), false);
        }
    }

    public void setAmbientMusic(CharSequence charSequence, PendingIntent pendingIntent, int i, boolean z) {
        KeyguardIndicationControllerGoogle$$ExternalSyntheticLambda1 keyguardIndicationControllerGoogle$$ExternalSyntheticLambda1;
        KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController = this.mRotateTextViewController;
        KeyguardIndication.Builder textColor = new KeyguardIndication.Builder().setMessage(charSequence).setIcon(AmbientIndicationContainer.getNowPlayingIcon(i, this.mContext)).setTextColor(this.mInitialTextColorState);
        if (pendingIntent == null) {
            keyguardIndicationControllerGoogle$$ExternalSyntheticLambda1 = null;
        } else {
            keyguardIndicationControllerGoogle$$ExternalSyntheticLambda1 = new View.OnClickListener(z, pendingIntent) { // from class: com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle$$ExternalSyntheticLambda1
                public final /* synthetic */ boolean f$1;
                public final /* synthetic */ PendingIntent f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    KeyguardIndicationControllerGoogle.this.lambda$setAmbientMusic$2(this.f$1, this.f$2, view);
                }
            };
        }
        keyguardIndicationRotateTextViewController.updateIndication(9, textColor.setClickListener(keyguardIndicationControllerGoogle$$ExternalSyntheticLambda1).build(), charSequence == null);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setAmbientMusic$2(boolean z, PendingIntent pendingIntent, View view) {
        StatusBar statusBar;
        if (z || (statusBar = this.mStatusBar) == null) {
            sendBroadcastWithoutDismissingKeyguard(pendingIntent);
        } else {
            statusBar.startPendingIntentDismissingKeyguard(pendingIntent);
        }
    }

    public void setStatusBar(StatusBar statusBar) {
        this.mStatusBar = statusBar;
    }

    private void sendBroadcastWithoutDismissingKeyguard(PendingIntent pendingIntent) {
        if (!pendingIntent.isActivity()) {
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                Log.w("KgrdIndicationCtrlGoogle", "Sending intent failed: " + e);
            }
        }
    }

    public void hideAmbientMusic() {
        this.mRotateTextViewController.hideIndication(9);
    }

    /* access modifiers changed from: private */
    public void triggerAdaptiveChargingStatusUpdate() {
        refreshAdaptiveChargingEnabled();
        if (this.mAdaptiveChargingEnabledInSettings) {
            this.mAdaptiveChargingManager.queryStatus(this.mAdaptiveChargingStatusReceiver);
        } else {
            this.mAdaptiveChargingActive = false;
        }
    }

    /* loaded from: classes2.dex */
    protected class GoogleKeyguardCallback extends KeyguardIndicationController.BaseKeyguardCallback {
        protected GoogleKeyguardCallback() {
            super();
        }

        @Override // com.android.systemui.statusbar.KeyguardIndicationController.BaseKeyguardCallback, com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onRefreshBatteryInfo(BatteryStatus batteryStatus) {
            KeyguardIndicationControllerGoogle.this.mIsCharging = batteryStatus.status == 2;
            KeyguardIndicationControllerGoogle.this.mBatteryLevel = batteryStatus.level;
            super.onRefreshBatteryInfo(batteryStatus);
            if (KeyguardIndicationControllerGoogle.this.mIsCharging) {
                KeyguardIndicationControllerGoogle.this.triggerAdaptiveChargingStatusUpdate();
            } else {
                KeyguardIndicationControllerGoogle.this.mAdaptiveChargingActive = false;
            }
        }
    }
}
