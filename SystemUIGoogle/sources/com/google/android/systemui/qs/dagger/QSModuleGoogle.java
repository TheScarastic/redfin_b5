package com.google.android.systemui.qs.dagger;

import android.content.Context;
import android.hardware.display.NightDisplayListener;
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
import com.google.android.systemui.statusbar.phone.AutoTileManagerGoogle;
/* loaded from: classes2.dex */
public interface QSModuleGoogle {
    static AutoTileManager provideAutoTileManager(Context context, AutoAddTracker.Builder builder, QSTileHost qSTileHost, Handler handler, SecureSettings secureSettings, HotspotController hotspotController, DataSaverController dataSaverController, ManagedProfileController managedProfileController, NightDisplayListener nightDisplayListener, CastController castController, BatteryController batteryController, ReduceBrightColorsController reduceBrightColorsController, DeviceControlsController deviceControlsController, WalletController walletController, boolean z) {
        AutoTileManagerGoogle autoTileManagerGoogle = new AutoTileManagerGoogle(context, builder, qSTileHost, handler, secureSettings, hotspotController, dataSaverController, managedProfileController, nightDisplayListener, castController, batteryController, reduceBrightColorsController, deviceControlsController, walletController, z);
        autoTileManagerGoogle.init();
        return autoTileManagerGoogle;
    }
}
