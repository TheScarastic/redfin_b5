package com.android.systemui.qs.tiles;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.NextAlarmController;
import java.util.Locale;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AlarmTile.kt */
/* loaded from: classes.dex */
public final class AlarmTile extends QSTileImpl<QSTile.State> {
    private final NextAlarmController.NextAlarmChangeCallback callback;
    private AlarmManager.AlarmClockInfo lastAlarmInfo;
    private final UserTracker userTracker;
    private final QSTile.Icon icon = QSTileImpl.ResourceIcon.get(R$drawable.ic_alarm);
    private final Intent defaultIntent = new Intent("android.intent.action.SET_ALARM");

    public static /* synthetic */ void getDefaultIntent$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        return null;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return 0;
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public AlarmTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, UserTracker userTracker, NextAlarmController nextAlarmController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        Intrinsics.checkNotNullParameter(qSHost, "host");
        Intrinsics.checkNotNullParameter(looper, "backgroundLooper");
        Intrinsics.checkNotNullParameter(handler, "mainHandler");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(metricsLogger, "metricsLogger");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(qSLogger, "qsLogger");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        Intrinsics.checkNotNullParameter(nextAlarmController, "nextAlarmController");
        this.userTracker = userTracker;
        AlarmTile$callback$1 alarmTile$callback$1 = new NextAlarmController.NextAlarmChangeCallback(this) { // from class: com.android.systemui.qs.tiles.AlarmTile$callback$1
            final /* synthetic */ AlarmTile this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.statusbar.policy.NextAlarmController.NextAlarmChangeCallback
            public final void onNextAlarmChanged(AlarmManager.AlarmClockInfo alarmClockInfo) {
                this.this$0.lastAlarmInfo = alarmClockInfo;
                this.this$0.refreshState();
            }
        };
        this.callback = alarmTile$callback$1;
        nextAlarmController.observe((LifecycleOwner) this, (AlarmTile) alarmTile$callback$1);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public QSTile.State newTileState() {
        QSTile.State state = new QSTile.State();
        state.handlesLongClick = false;
        return state;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        PendingIntent pendingIntent = null;
        ActivityLaunchAnimator.Controller fromView = view == null ? null : ActivityLaunchAnimator.Controller.Companion.fromView(view, 32);
        AlarmManager.AlarmClockInfo alarmClockInfo = this.lastAlarmInfo;
        if (alarmClockInfo != null) {
            pendingIntent = alarmClockInfo.getShowIntent();
        }
        if (pendingIntent != null) {
            this.mActivityStarter.postStartActivityDismissingKeyguard(pendingIntent, fromView);
        } else {
            this.mActivityStarter.postStartActivityDismissingKeyguard(this.defaultIntent, 0, fromView);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleUpdateState(QSTile.State state, Object obj) {
        Unit unit;
        Intrinsics.checkNotNullParameter(state, "state");
        state.icon = this.icon;
        state.label = getTileLabel();
        AlarmManager.AlarmClockInfo alarmClockInfo = this.lastAlarmInfo;
        if (alarmClockInfo == null) {
            unit = null;
        } else {
            state.secondaryLabel = formatNextAlarm(alarmClockInfo);
            state.state = 2;
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            state.secondaryLabel = this.mContext.getString(R$string.qs_alarm_tile_no_alarm);
            state.state = 1;
        }
        state.contentDescription = TextUtils.concat(state.label, ", ", state.secondaryLabel);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        String string = this.mContext.getString(R$string.status_bar_alarm);
        Intrinsics.checkNotNullExpressionValue(string, "mContext.getString(R.string.status_bar_alarm)");
        return string;
    }

    private final String formatNextAlarm(AlarmManager.AlarmClockInfo alarmClockInfo) {
        return DateFormat.format(DateFormat.getBestDateTimePattern(Locale.getDefault(), use24HourFormat() ? "EHm" : "Ehma"), alarmClockInfo.getTriggerTime()).toString();
    }

    private final boolean use24HourFormat() {
        return DateFormat.is24HourFormat(this.mContext, this.userTracker.getUserId());
    }
}
