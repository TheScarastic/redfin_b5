package com.google.android.systemui.qs.tiles;

import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tiles.BatterySaverTile;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.settings.SecureSettings;
/* loaded from: classes2.dex */
public class BatterySaverTileGoogle extends BatterySaverTile {
    private boolean mExtreme;

    public BatterySaverTileGoogle(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BatteryController batteryController, SecureSettings secureSettings) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger, batteryController, secureSettings);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.qs.tiles.BatterySaverTile
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        super.handleUpdateState(booleanState, obj);
        if (booleanState.state != 2 || !this.mExtreme) {
            booleanState.secondaryLabel = "";
        } else {
            booleanState.secondaryLabel = this.mContext.getString(R$string.extreme_battery_saver_text);
        }
        booleanState.stateDescription = booleanState.secondaryLabel;
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onExtremeBatterySaverChanged(boolean z) {
        if (this.mExtreme != z) {
            this.mExtreme = z;
            refreshState();
        }
    }
}
