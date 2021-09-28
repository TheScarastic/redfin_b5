package com.android.systemui.qs.tiles;

import android.os.Handler;
import android.os.Looper;
import android.provider.DeviceConfig;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.DejankUtils;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
/* loaded from: classes.dex */
public class CameraToggleTile extends SensorPrivacyToggleTile {
    @Override // com.android.systemui.qs.tiles.SensorPrivacyToggleTile
    public int getIconRes(boolean z) {
        return z ? 17302353 : 17302352;
    }

    @Override // com.android.systemui.qs.tiles.SensorPrivacyToggleTile
    public String getRestriction() {
        return "disallow_camera_toggle";
    }

    @Override // com.android.systemui.qs.tiles.SensorPrivacyToggleTile
    public int getSensorId() {
        return 2;
    }

    public CameraToggleTile(QSHost qSHost, Looper looper, Handler handler, MetricsLogger metricsLogger, FalsingManager falsingManager, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, IndividualSensorPrivacyController individualSensorPrivacyController, KeyguardStateController keyguardStateController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger, individualSensorPrivacyController, keyguardStateController);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public boolean isAvailable() {
        return this.mSensorPrivacyController.supportsSensorToggle(2) && ((Boolean) DejankUtils.whitelistIpcs(CameraToggleTile$$ExternalSyntheticLambda0.INSTANCE)).booleanValue();
    }

    public static /* synthetic */ Boolean lambda$isAvailable$0() {
        return Boolean.valueOf(DeviceConfig.getBoolean("privacy", "camera_toggle_enabled", true));
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.quick_settings_camera_label);
    }
}
