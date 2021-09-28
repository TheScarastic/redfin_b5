package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.Lifecycle;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$drawable;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
/* loaded from: classes.dex */
public class ReduceBrightColorsTile extends QSTileImpl<QSTile.BooleanState> implements ReduceBrightColorsController.Listener {
    private final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(R$drawable.ic_reduce_bright_colors);
    private final boolean mIsAvailable;
    private final ReduceBrightColorsController mReduceBrightColorsController;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return 0;
    }

    public ReduceBrightColorsTile(boolean z, ReduceBrightColorsController reduceBrightColorsController, QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mReduceBrightColorsController = reduceBrightColorsController;
        reduceBrightColorsController.observe(getLifecycle(), (Lifecycle) this);
        this.mIsAvailable = z;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public boolean isAvailable() {
        return this.mIsAvailable;
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleDestroy() {
        super.handleDestroy();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        return new Intent("android.settings.REDUCE_BRIGHT_COLORS_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        this.mReduceBrightColorsController.setReduceBrightColorsActivated(!((QSTile.BooleanState) this.mState).value);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return this.mContext.getString(17041245);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean isReduceBrightColorsActivated = this.mReduceBrightColorsController.isReduceBrightColorsActivated();
        booleanState.value = isReduceBrightColorsActivated;
        booleanState.state = isReduceBrightColorsActivated ? 2 : 1;
        booleanState.label = this.mContext.getString(17041245);
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        booleanState.contentDescription = booleanState.label;
        booleanState.icon = this.mIcon;
    }

    @Override // com.android.systemui.qs.ReduceBrightColorsController.Listener
    public void onActivated(boolean z) {
        refreshState();
    }
}
