package com.google.android.systemui.qs.tiles;

import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IThermalEventListener;
import android.os.IThermalService;
import android.os.Looper;
import android.os.RemoteException;
import android.os.Temperature;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.Lifecycle;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.Prefs;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.BatteryController;
/* loaded from: classes2.dex */
public class ReverseChargingTile extends QSTileImpl<QSTile.BooleanState> implements BatteryController.BatteryStateChangeCallback {
    private static final boolean DEBUG = Log.isLoggable("ReverseChargingTile", 3);
    private final BatteryController mBatteryController;
    private int mBatteryLevel;
    private boolean mListening;
    private boolean mOverHeat;
    private boolean mPowerSave;
    private boolean mReverse;
    private final IThermalService mThermalService;
    private int mThresholdLevel;
    private final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(R$drawable.ic_qs_reverse_charging);
    private final IThermalEventListener mThermalEventListener = new IThermalEventListener.Stub() { // from class: com.google.android.systemui.qs.tiles.ReverseChargingTile.1
        public void notifyThrottling(Temperature temperature) {
            int status = temperature.getStatus();
            ReverseChargingTile.this.mOverHeat = status >= 5;
            if (ReverseChargingTile.DEBUG) {
                Log.d("ReverseChargingTile", "notifyThrottling(): status=" + status);
            }
        }
    };
    private final ContentObserver mSettingsObserver = new ContentObserver(this.mHandler) { // from class: com.google.android.systemui.qs.tiles.ReverseChargingTile.2
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            ReverseChargingTile.this.updateThresholdLevel();
        }
    };

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return 0;
    }

    public ReverseChargingTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BatteryController batteryController, IThermalService iThermalService) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mBatteryController = batteryController;
        batteryController.observe(getLifecycle(), (Lifecycle) this);
        this.mThermalService = iThermalService;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleDestroy() {
        super.handleDestroy();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (this.mListening != z) {
            this.mListening = z;
            if (z) {
                updateThresholdLevel();
                this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("advanced_battery_usage_amount"), false, this.mSettingsObserver);
                try {
                    this.mThermalService.registerThermalEventListenerWithType(this.mThermalEventListener, 3);
                } catch (RemoteException e) {
                    Log.e("ReverseChargingTile", "Could not register thermal event listener, exception: " + e);
                }
                this.mOverHeat = isOverHeat();
            } else {
                this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsObserver);
                try {
                    this.mThermalService.unregisterThermalEventListener(this.mThermalEventListener);
                } catch (RemoteException e2) {
                    Log.e("ReverseChargingTile", "Could not unregister thermal event listener, exception: " + e2);
                }
            }
            if (DEBUG) {
                Log.d("ReverseChargingTile", "handleSetListening(): rtx=" + (this.mReverse ? 1 : 0) + ",level=" + this.mBatteryLevel + ",threshold=" + this.mThresholdLevel + ",listening=" + z);
            }
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public boolean isAvailable() {
        return this.mBatteryController.isReverseSupported();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        Intent intent = new Intent("android.settings.REVERSE_CHARGING_SETTINGS");
        intent.setPackage("com.android.settings");
        return intent;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        if (getState().state != 0) {
            this.mReverse = !this.mReverse;
            if (DEBUG) {
                Log.d("ReverseChargingTile", "handleClick(): rtx=" + (this.mReverse ? 1 : 0) + ",this=" + this);
            }
            this.mBatteryController.setReverseState(this.mReverse);
            showBottomSheetIfNecessary();
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.reverse_charging_title);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        String str;
        boolean isWirelessCharging = this.mBatteryController.isWirelessCharging();
        int i = 1;
        int i2 = this.mBatteryLevel <= this.mThresholdLevel ? 1 : 0;
        boolean z = this.mOverHeat;
        booleanState.value = !z && !this.mPowerSave && !isWirelessCharging && i2 == 0 && this.mReverse;
        if (z || this.mPowerSave || isWirelessCharging || i2 != 0) {
            i = 0;
        } else if (this.mReverse) {
            i = 2;
        }
        booleanState.state = i;
        booleanState.icon = this.mIcon;
        CharSequence tileLabel = getTileLabel();
        booleanState.label = tileLabel;
        booleanState.contentDescription = tileLabel;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        if (this.mOverHeat) {
            str = this.mContext.getString(R$string.too_hot_label);
        } else if (this.mPowerSave) {
            str = this.mContext.getString(R$string.quick_settings_dark_mode_secondary_label_battery_saver);
        } else if (isWirelessCharging) {
            str = this.mContext.getString(R$string.wireless_charging_label);
        } else {
            str = i2 != 0 ? this.mContext.getString(R$string.low_battery_label) : null;
        }
        booleanState.secondaryLabel = str;
        if (DEBUG) {
            Log.d("ReverseChargingTile", "handleUpdateState(): ps=" + (this.mPowerSave ? 1 : 0) + ",wlc=" + (isWirelessCharging ? 1 : 0) + ",low=" + i2 + ",over=" + (this.mOverHeat ? 1 : 0) + ",rtx=" + (this.mReverse ? 1 : 0) + ",this=" + this);
        }
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
        this.mBatteryLevel = i;
        this.mReverse = this.mBatteryController.isReverseOn();
        if (DEBUG) {
            Log.d("ReverseChargingTile", "onBatteryLevelChanged(): rtx=" + (this.mReverse ? 1 : 0) + ",level=" + this.mBatteryLevel + ",threshold=" + this.mThresholdLevel);
        }
        refreshState(null);
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onPowerSaveChanged(boolean z) {
        this.mPowerSave = z;
        refreshState(null);
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onReverseChanged(boolean z, int i, String str) {
        if (DEBUG) {
            Log.d("ReverseChargingTile", "onReverseChanged(): rtx=" + (z ? 1 : 0) + ",level=" + i + ",name=" + str + ",this=" + this);
        }
        this.mReverse = z;
        refreshState(null);
    }

    private void showBottomSheetIfNecessary() {
        if (!Prefs.getBoolean(this.mHost.getUserContext(), "HasSeenReverseBottomSheet", false)) {
            Intent intent = new Intent("android.settings.REVERSE_CHARGING_BOTTOM_SHEET");
            intent.setPackage("com.android.settings");
            this.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
            Prefs.putBoolean(this.mHost.getUserContext(), "HasSeenReverseBottomSheet", true);
        }
    }

    /* access modifiers changed from: private */
    public void updateThresholdLevel() {
        this.mThresholdLevel = Settings.Global.getInt(this.mContext.getContentResolver(), "advanced_battery_usage_amount", 2) * 5;
        if (DEBUG) {
            Log.d("ReverseChargingTile", "updateThresholdLevel(): rtx=" + (this.mReverse ? 1 : 0) + ",level=" + this.mBatteryLevel + ",threshold=" + this.mThresholdLevel);
        }
    }

    private boolean isOverHeat() {
        try {
            Temperature[] currentTemperaturesWithType = this.mThermalService.getCurrentTemperaturesWithType(3);
            for (Temperature temperature : currentTemperaturesWithType) {
                if (temperature.getStatus() >= 5) {
                    Log.w("ReverseChargingTile", "isOverHeat(): current skin status = " + temperature.getStatus() + ", temperature = " + temperature.getValue());
                    return true;
                }
            }
        } catch (RemoteException e) {
            Log.w("ReverseChargingTile", "isOverHeat(): " + e);
        }
        return false;
    }
}
