package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.Lifecycle;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.DetailAdapter;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
/* loaded from: classes.dex */
public abstract class SensorPrivacyToggleTile extends QSTileImpl<QSTile.BooleanState> implements IndividualSensorPrivacyController.Callback {
    private final KeyguardStateController mKeyguard;
    protected IndividualSensorPrivacyController mSensorPrivacyController;

    public abstract int getIconRes(boolean z);

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return 0;
    }

    public abstract String getRestriction();

    public abstract int getSensorId();

    /* access modifiers changed from: protected */
    public SensorPrivacyToggleTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, IndividualSensorPrivacyController individualSensorPrivacyController, KeyguardStateController keyguardStateController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mSensorPrivacyController = individualSensorPrivacyController;
        this.mKeyguard = keyguardStateController;
        individualSensorPrivacyController.observe(getLifecycle(), (Lifecycle) this);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        if (!this.mKeyguard.isMethodSecure() || !this.mKeyguard.isShowing()) {
            this.mSensorPrivacyController.setSensorBlocked(1, getSensorId(), !this.mSensorPrivacyController.isSensorBlocked(getSensorId()));
        } else {
            this.mActivityStarter.postQSRunnableDismissingKeyguard(new Runnable() { // from class: com.android.systemui.qs.tiles.SensorPrivacyToggleTile$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SensorPrivacyToggleTile.this.lambda$handleClick$0();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleClick$0() {
        this.mSensorPrivacyController.setSensorBlocked(1, getSensorId(), !this.mSensorPrivacyController.isSensorBlocked(getSensorId()));
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean z;
        if (obj == null) {
            z = this.mSensorPrivacyController.isSensorBlocked(getSensorId());
        } else {
            z = ((Boolean) obj).booleanValue();
        }
        checkIfRestrictionEnforcedByAdminOnly(booleanState, getRestriction());
        booleanState.icon = QSTileImpl.ResourceIcon.get(getIconRes(z));
        booleanState.state = z ? 1 : 2;
        booleanState.value = !z;
        booleanState.label = getTileLabel();
        if (z) {
            booleanState.secondaryLabel = this.mContext.getString(R$string.quick_settings_camera_mic_blocked);
        } else {
            booleanState.secondaryLabel = this.mContext.getString(R$string.quick_settings_camera_mic_available);
        }
        booleanState.contentDescription = booleanState.label;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        return new Intent("android.settings.PRIVACY_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public DetailAdapter getDetailAdapter() {
        return super.getDetailAdapter();
    }

    @Override // com.android.systemui.statusbar.policy.IndividualSensorPrivacyController.Callback
    public void onSensorBlockedChanged(int i, boolean z) {
        if (i == getSensorId()) {
            refreshState(Boolean.valueOf(z));
        }
    }
}
