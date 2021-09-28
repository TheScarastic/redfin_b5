package com.google.android.systemui.reversecharging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.settingslib.Utils;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.google.android.systemui.ambientmusic.AmbientIndicationContainer;
import com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle;
import dagger.Lazy;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public class ReverseChargingViewController extends BroadcastReceiver implements LifecycleOwner, BatteryController.BatteryStateChangeCallback {
    private static final boolean DEBUG = Log.isLoggable("ReverseChargingViewCtrl", 3);
    private AmbientIndicationContainer mAmbientIndicationContainer;
    private final BatteryController mBatteryController;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private String mContentDescription;
    private final Context mContext;
    private final KeyguardIndicationControllerGoogle mKeyguardIndicationController;
    private int mLevel;
    private final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);
    private final Executor mMainExecutor;
    private String mName;
    private boolean mProvidingBattery;
    private boolean mReverse;
    private String mReverseCharging;
    private String mSlotReverseCharging;
    private final StatusBarIconController mStatusBarIconController;
    private final Lazy<StatusBar> mStatusBarLazy;

    public ReverseChargingViewController(Context context, BatteryController batteryController, Lazy<StatusBar> lazy, StatusBarIconController statusBarIconController, BroadcastDispatcher broadcastDispatcher, Executor executor, KeyguardIndicationControllerGoogle keyguardIndicationControllerGoogle) {
        this.mBatteryController = batteryController;
        this.mStatusBarIconController = statusBarIconController;
        this.mStatusBarLazy = lazy;
        this.mContext = context;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mMainExecutor = executor;
        this.mKeyguardIndicationController = keyguardIndicationControllerGoogle;
        loadStrings();
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.LOCALE_CHANGED")) {
            loadStrings();
            if (DEBUG) {
                Log.d("ReverseChargingViewCtrl", "onReceive(): ACTION_LOCALE_CHANGED this=" + this);
            }
            postOnMainThreadToUpdate();
        }
    }

    @Override // androidx.lifecycle.LifecycleOwner
    public Lifecycle getLifecycle() {
        return this.mLifecycle;
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
        this.mReverse = this.mBatteryController.isReverseOn();
        if (DEBUG) {
            Log.d("ReverseChargingViewCtrl", "onBatteryLevelChanged(): rtx=" + (this.mReverse ? 1 : 0) + " level=" + this.mLevel + " name=" + this.mName + " this=" + this);
        }
        postOnMainThreadToUpdate();
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onReverseChanged(boolean z, int i, String str) {
        this.mReverse = z;
        this.mLevel = i;
        this.mName = str;
        this.mProvidingBattery = z && i >= 0;
        if (DEBUG) {
            Log.d("ReverseChargingViewCtrl", "onReverseChanged(): rtx=" + (z ? 1 : 0) + " level=" + i + " name=" + str + " this=" + this);
        }
        postOnMainThreadToUpdate();
    }

    public void initialize() {
        this.mBatteryController.observe((Lifecycle) this.mLifecycle, (LifecycleRegistry) this);
        this.mLifecycle.markState(Lifecycle.State.RESUMED);
        this.mAmbientIndicationContainer = (AmbientIndicationContainer) this.mStatusBarLazy.get().getNotificationShadeWindowView().findViewById(R$id.ambient_indication_container);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        this.mBroadcastDispatcher.registerReceiver(this, intentFilter);
    }

    private void postOnMainThreadToUpdate() {
        this.mMainExecutor.execute(new Runnable() { // from class: com.google.android.systemui.reversecharging.ReverseChargingViewController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ReverseChargingViewController.$r8$lambda$xqAseX9lWbYGUJxuKYaygyCflug(ReverseChargingViewController.this);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$postOnMainThreadToUpdate$0() {
        updateMessage();
        updateReverseChargingIcon();
    }

    private void updateMessage() {
        if (this.mAmbientIndicationContainer == null) {
            return;
        }
        if (this.mReverse || !this.mBatteryController.isWirelessCharging() || TextUtils.isEmpty(this.mName)) {
            String str = "";
            this.mKeyguardIndicationController.setReverseChargingMessage(this.mProvidingBattery ? this.mReverseCharging : str);
            this.mAmbientIndicationContainer.setReverseChargingMessage(this.mProvidingBattery ? this.mReverseCharging : str);
            if (DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("updateMessage(): rtx=");
                sb.append(this.mReverse ? 1 : 0);
                sb.append(" rtxString=");
                if (this.mProvidingBattery) {
                    str = this.mReverseCharging;
                }
                sb.append(str);
                Log.d("ReverseChargingViewCtrl", sb.toString());
                return;
            }
            return;
        }
        String string = this.mContext.getResources().getString(R$string.reverse_charging_device_providing_charge_text, this.mName, Utils.formatPercentage(this.mLevel));
        if (DEBUG) {
            Log.d("ReverseChargingViewCtrl", "updateMessage(): rtx=" + (this.mReverse ? 1 : 0) + " wlcString=" + string);
        }
        this.mKeyguardIndicationController.setReverseChargingMessage(string);
        this.mAmbientIndicationContainer.setWirelessChargingMessage(string);
    }

    private void updateReverseChargingIcon() {
        this.mStatusBarIconController.setIcon(this.mSlotReverseCharging, R$drawable.ic_qs_reverse_charging, this.mContentDescription);
        this.mStatusBarIconController.setIconVisibility(this.mSlotReverseCharging, this.mProvidingBattery);
    }

    private void loadStrings() {
        this.mReverseCharging = this.mContext.getString(R$string.charging_reverse_text);
        this.mSlotReverseCharging = this.mContext.getString(R$string.status_bar_google_reverse_charging);
        this.mContentDescription = this.mContext.getString(R$string.reverse_charging_on_notification_title);
    }
}
