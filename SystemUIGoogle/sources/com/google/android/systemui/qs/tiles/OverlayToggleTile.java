package com.google.android.systemui.qs.tiles;

import android.content.Intent;
import android.content.om.OverlayInfo;
import android.content.om.OverlayManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.UserHandle;
import android.util.Slog;
import android.view.View;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsJVMKt;
/* compiled from: OverlayToggleTile.kt */
/* loaded from: classes2.dex */
public final class OverlayToggleTile extends QSTileImpl<QSTile.BooleanState> {
    public static final Companion Companion = new Companion(null);
    private final OverlayManager om;
    private CharSequence overlayLabel;
    private String overlayPackage;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        return null;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return -1;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return "Overlay";
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleLongClick(View view) {
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public OverlayToggleTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, OverlayManager overlayManager) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        Intrinsics.checkNotNullParameter(qSHost, "host");
        Intrinsics.checkNotNullParameter(looper, "backgroundLooper");
        Intrinsics.checkNotNullParameter(handler, "mainHandler");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(metricsLogger, "metricsLogger");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(qSLogger, "qsLogger");
        Intrinsics.checkNotNullParameter(overlayManager, "om");
        this.om = overlayManager;
    }

    /* compiled from: OverlayToggleTile.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public boolean isAvailable() {
        return Build.IS_DEBUGGABLE;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        QSTile.BooleanState state;
        String str = this.overlayPackage;
        if (str != null && (state = getState()) != null) {
            boolean z = state.state != 2;
            String str2 = this.TAG;
            Slog.v(str2, "Setting enable state of " + str + " to " + z);
            this.om.setEnabled(str, z, UserHandle.CURRENT);
            refreshState("Restarting...");
            Thread.sleep(250);
            Slog.v(this.TAG, "Restarting System UI to react to overlay changes");
            Process.killProcess(Process.myPid());
        }
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int i;
        Object obj2;
        String str;
        Intrinsics.checkNotNullParameter(booleanState, "state");
        PackageManager packageManager = this.mContext.getPackageManager();
        booleanState.state = 0;
        booleanState.label = "No overlay";
        List overlayInfosForTarget = this.om.getOverlayInfosForTarget("com.android.systemui", UserHandle.CURRENT);
        if (overlayInfosForTarget != null) {
            Iterator it = overlayInfosForTarget.iterator();
            while (true) {
                i = 2;
                obj2 = null;
                if (!it.hasNext()) {
                    break;
                }
                Object next = it.next();
                String str2 = ((OverlayInfo) next).packageName;
                Intrinsics.checkNotNullExpressionValue(str2, "it.packageName");
                if (StringsKt__StringsJVMKt.startsWith$default(str2, "com.google.", false, 2, null)) {
                    obj2 = next;
                    break;
                }
            }
            OverlayInfo overlayInfo = (OverlayInfo) obj2;
            if (overlayInfo != null) {
                if (!Intrinsics.areEqual(this.overlayPackage, overlayInfo.packageName)) {
                    String str3 = overlayInfo.packageName;
                    this.overlayPackage = str3;
                    this.overlayLabel = packageManager.getPackageInfo(str3, 0).applicationInfo.loadLabel(packageManager);
                }
                booleanState.value = overlayInfo.isEnabled();
                if (!overlayInfo.isEnabled()) {
                    i = 1;
                }
                booleanState.state = i;
                booleanState.icon = QSTileImpl.ResourceIcon.get(17303566);
                booleanState.label = this.overlayLabel;
                if (obj != null) {
                    str = String.valueOf(obj);
                } else {
                    str = overlayInfo.isEnabled() ? "Enabled" : "Disabled";
                }
                booleanState.secondaryLabel = str;
            }
        }
    }
}
