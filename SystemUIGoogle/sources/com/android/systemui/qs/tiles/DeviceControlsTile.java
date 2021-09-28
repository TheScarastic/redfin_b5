package com.android.systemui.qs.tiles;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DeviceControlsTile.kt */
/* loaded from: classes.dex */
public final class DeviceControlsTile extends QSTileImpl<QSTile.State> {
    private final ControlsComponent controlsComponent;
    private final KeyguardStateController keyguardStateController;
    private AtomicBoolean hasControlsApps = new AtomicBoolean(false);
    private final QSTile.Icon icon = QSTileImpl.ResourceIcon.get(R$drawable.controls_icon);
    private final DeviceControlsTile$listingCallback$1 listingCallback = new DeviceControlsTile$listingCallback$1(this);

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        return null;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return 0;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleLongClick(View view) {
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public DeviceControlsTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, ControlsComponent controlsComponent, KeyguardStateController keyguardStateController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        Intrinsics.checkNotNullParameter(qSHost, "host");
        Intrinsics.checkNotNullParameter(looper, "backgroundLooper");
        Intrinsics.checkNotNullParameter(handler, "mainHandler");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(metricsLogger, "metricsLogger");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(qSLogger, "qsLogger");
        Intrinsics.checkNotNullParameter(controlsComponent, "controlsComponent");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        this.controlsComponent = controlsComponent;
        this.keyguardStateController = keyguardStateController;
        controlsComponent.getControlsListingController().ifPresent(new Consumer<ControlsListingController>(this) { // from class: com.android.systemui.qs.tiles.DeviceControlsTile.1
            final /* synthetic */ DeviceControlsTile this$0;

            {
                this.this$0 = r1;
            }

            public final void accept(ControlsListingController controlsListingController) {
                Intrinsics.checkNotNullParameter(controlsListingController, "it");
                DeviceControlsTile deviceControlsTile = this.this$0;
                controlsListingController.observe((LifecycleOwner) deviceControlsTile, (DeviceControlsTile) deviceControlsTile.listingCallback);
            }
        });
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public boolean isAvailable() {
        return this.controlsComponent.getControlsController().isPresent();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public QSTile.State newTileState() {
        QSTile.State state = new QSTile.State();
        state.state = 0;
        state.handlesLongClick = false;
        return state;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        ActivityLaunchAnimator.Controller controller;
        if (getState().state != 0) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(this.mContext, ControlsActivity.class));
            intent.addFlags(335544320);
            intent.putExtra("extra_animate", true);
            if (view == null) {
                controller = null;
            } else {
                controller = ActivityLaunchAnimator.Controller.Companion.fromView(view, 32);
            }
            this.mUiHandler.post(new Runnable(this, intent, controller) { // from class: com.android.systemui.qs.tiles.DeviceControlsTile$handleClick$1
                final /* synthetic */ ActivityLaunchAnimator.Controller $animationController;
                final /* synthetic */ Intent $intent;
                final /* synthetic */ DeviceControlsTile this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$intent = r2;
                    this.$animationController = r3;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    if (DeviceControlsTile.access$getKeyguardStateController$p(this.this$0).isUnlocked()) {
                        DeviceControlsTile.access$getMActivityStarter$p(this.this$0).startActivity(this.$intent, true, this.$animationController);
                    } else if (this.this$0.getState().state == 2) {
                        DeviceControlsTile.access$getMHost$p(this.this$0).collapsePanels();
                        DeviceControlsTile.access$getMContext$p(this.this$0).startActivity(this.$intent);
                    } else {
                        DeviceControlsTile.access$getMActivityStarter$p(this.this$0).postStartActivityDismissingKeyguard(this.$intent, 0, this.$animationController);
                    }
                }
            });
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleUpdateState(QSTile.State state, Object obj) {
        Intrinsics.checkNotNullParameter(state, "state");
        CharSequence tileLabel = getTileLabel();
        state.label = tileLabel;
        state.contentDescription = tileLabel;
        state.icon = this.icon;
        if (!this.controlsComponent.isEnabled() || !this.hasControlsApps.get()) {
            state.state = 0;
            return;
        }
        if (this.controlsComponent.getVisibility() == ControlsComponent.Visibility.AVAILABLE) {
            state.state = 2;
            state.secondaryLabel = this.controlsComponent.getControlsController().get().getPreferredStructure().getStructure();
        } else {
            state.state = 1;
            state.secondaryLabel = this.mContext.getText(R$string.controls_tile_locked);
        }
        state.stateDescription = state.secondaryLabel;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        CharSequence text = this.mContext.getText(R$string.quick_controls_title);
        Intrinsics.checkNotNullExpressionValue(text, "mContext.getText(R.string.quick_controls_title)");
        return text;
    }
}
