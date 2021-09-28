package com.google.android.systemui.statusbar.phone;

import android.content.Context;
import android.hardware.display.NightDisplayListener;
import android.os.Build;
import android.os.Handler;
import com.android.systemui.qs.AutoAddTracker;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.ManagedProfileController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.WalletController;
import com.android.systemui.util.settings.SecureSettings;
/* loaded from: classes2.dex */
public class AutoTileManagerGoogle extends AutoTileManager {
    private final BatteryController mBatteryController;
    private final BatteryController.BatteryStateChangeCallback mBatteryControllerCallback = new BatteryController.BatteryStateChangeCallback() { // from class: com.google.android.systemui.statusbar.phone.AutoTileManagerGoogle.1
        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public void onReverseChanged(boolean z, int i, String str) {
            if (!((AutoTileManager) AutoTileManagerGoogle.this).mAutoTracker.isAdded("reverse") && z) {
                ((AutoTileManager) AutoTileManagerGoogle.this).mHost.addTile("reverse");
                ((AutoTileManager) AutoTileManagerGoogle.this).mAutoTracker.setTileAdded("reverse");
                ((AutoTileManager) AutoTileManagerGoogle.this).mHandler.post(new AutoTileManagerGoogle$1$$ExternalSyntheticLambda0(this));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onReverseChanged$0() {
            AutoTileManagerGoogle.this.mBatteryController.removeCallback(AutoTileManagerGoogle.this.mBatteryControllerCallback);
        }
    };

    public AutoTileManagerGoogle(Context context, AutoAddTracker.Builder builder, QSTileHost qSTileHost, Handler handler, SecureSettings secureSettings, HotspotController hotspotController, DataSaverController dataSaverController, ManagedProfileController managedProfileController, NightDisplayListener nightDisplayListener, CastController castController, BatteryController batteryController, ReduceBrightColorsController reduceBrightColorsController, DeviceControlsController deviceControlsController, WalletController walletController, boolean z) {
        super(context, builder, qSTileHost, handler, secureSettings, hotspotController, dataSaverController, managedProfileController, nightDisplayListener, castController, reduceBrightColorsController, deviceControlsController, walletController, z);
        this.mBatteryController = batteryController;
        if (!this.mAutoTracker.isAdded("ott") && Build.IS_DEBUGGABLE) {
            this.mAutoTracker.setTileAdded("ott");
            this.mHost.addTile("ott");
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.AutoTileManager
    public void startControllersAndSettingsListeners() {
        super.startControllersAndSettingsListeners();
        if (!this.mAutoTracker.isAdded("reverse")) {
            this.mBatteryController.addCallback(this.mBatteryControllerCallback);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.AutoTileManager
    public void stopListening() {
        super.stopListening();
        this.mBatteryController.removeCallback(this.mBatteryControllerCallback);
    }
}
