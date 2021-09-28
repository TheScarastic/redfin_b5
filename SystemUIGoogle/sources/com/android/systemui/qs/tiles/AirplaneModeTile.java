package com.android.systemui.qs.tiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.sysprop.TelephonyProperties;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.GlobalSetting;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import dagger.Lazy;
/* loaded from: classes.dex */
public class AirplaneModeTile extends QSTileImpl<QSTile.BooleanState> {
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final Lazy<ConnectivityManager> mLazyConnectivityManager;
    private boolean mListening;
    private final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(17302815);
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.qs.tiles.AirplaneModeTile.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
                AirplaneModeTile.this.refreshState();
            }
        }
    };
    private final GlobalSetting mSetting = new GlobalSetting(this.mContext, this.mHandler, "airplane_mode_on") { // from class: com.android.systemui.qs.tiles.AirplaneModeTile.1
        @Override // com.android.systemui.qs.GlobalSetting
        protected void handleValueChanged(int i) {
            AirplaneModeTile.this.handleRefreshState(Integer.valueOf(i));
        }
    };

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return 112;
    }

    public AirplaneModeTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BroadcastDispatcher broadcastDispatcher, Lazy<ConnectivityManager> lazy) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mLazyConnectivityManager = lazy;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleClick(View view) {
        boolean z = ((QSTile.BooleanState) this.mState).value;
        MetricsLogger.action(this.mContext, getMetricsCategory(), !z);
        if (z || !((Boolean) TelephonyProperties.in_ecm_mode().orElse(Boolean.FALSE)).booleanValue()) {
            setEnabled(!z);
        } else {
            this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("android.telephony.action.SHOW_NOTICE_ECM_BLOCK_OTHERS"), 0);
        }
    }

    private void setEnabled(boolean z) {
        this.mLazyConnectivityManager.get().setAirplaneMode(z);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        return new Intent("android.settings.AIRPLANE_MODE_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.airplane_mode);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        checkIfRestrictionEnforcedByAdminOnly(booleanState, "no_airplane_mode");
        int i = 1;
        boolean z = (obj instanceof Integer ? ((Integer) obj).intValue() : this.mSetting.getValue()) != 0;
        booleanState.value = z;
        booleanState.label = this.mContext.getString(R$string.airplane_mode);
        booleanState.icon = this.mIcon;
        if (booleanState.slash == null) {
            booleanState.slash = new QSTile.SlashState();
        }
        booleanState.slash.isSlashed = !z;
        if (z) {
            i = 2;
        }
        booleanState.state = i;
        booleanState.contentDescription = booleanState.label;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected String composeChangeAnnouncement() {
        if (((QSTile.BooleanState) this.mState).value) {
            return this.mContext.getString(R$string.accessibility_quick_settings_airplane_changed_on);
        }
        return this.mContext.getString(R$string.accessibility_quick_settings_airplane_changed_off);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (this.mListening != z) {
            this.mListening = z;
            if (z) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
                this.mBroadcastDispatcher.registerReceiver(this.mReceiver, intentFilter);
            } else {
                this.mBroadcastDispatcher.unregisterReceiver(this.mReceiver);
            }
            this.mSetting.setListening(z);
        }
    }
}
