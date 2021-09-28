package com.android.systemui.controls.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.service.controls.Control;
import android.service.controls.actions.BooleanAction;
import android.service.controls.actions.CommandAction;
import android.service.controls.actions.FloatAction;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.controls.ui.ControlActionCoordinatorImpl;
import com.android.systemui.globalactions.GlobalActionsComponent;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.wm.shell.TaskView;
import com.android.wm.shell.TaskViewFactory;
import dagger.Lazy;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlActionCoordinatorImpl.kt */
/* loaded from: classes.dex */
public final class ControlActionCoordinatorImpl implements ControlActionCoordinator {
    public static final Companion Companion = new Companion(null);
    private Set<String> actionsInProgress = new LinkedHashSet();
    public Context activityContext;
    private final ActivityStarter activityStarter;
    private final DelayableExecutor bgExecutor;
    private final BroadcastDispatcher broadcastDispatcher;
    private final Context context;
    private final ControlsMetricsLogger controlsMetricsLogger;
    private Dialog dialog;
    private final GlobalActionsComponent globalActionsComponent;
    private final KeyguardStateController keyguardStateController;
    private final Lazy<ControlsUiController> lazyUiController;
    private Action pendingAction;
    private final Optional<TaskViewFactory> taskViewFactory;
    private final DelayableExecutor uiExecutor;
    private final Vibrator vibrator;

    public ControlActionCoordinatorImpl(Context context, DelayableExecutor delayableExecutor, DelayableExecutor delayableExecutor2, ActivityStarter activityStarter, KeyguardStateController keyguardStateController, GlobalActionsComponent globalActionsComponent, Optional<TaskViewFactory> optional, BroadcastDispatcher broadcastDispatcher, Lazy<ControlsUiController> lazy, ControlsMetricsLogger controlsMetricsLogger) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(delayableExecutor, "bgExecutor");
        Intrinsics.checkNotNullParameter(delayableExecutor2, "uiExecutor");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(globalActionsComponent, "globalActionsComponent");
        Intrinsics.checkNotNullParameter(optional, "taskViewFactory");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(lazy, "lazyUiController");
        Intrinsics.checkNotNullParameter(controlsMetricsLogger, "controlsMetricsLogger");
        this.context = context;
        this.bgExecutor = delayableExecutor;
        this.uiExecutor = delayableExecutor2;
        this.activityStarter = activityStarter;
        this.keyguardStateController = keyguardStateController;
        this.globalActionsComponent = globalActionsComponent;
        this.taskViewFactory = optional;
        this.broadcastDispatcher = broadcastDispatcher;
        this.lazyUiController = lazy;
        this.controlsMetricsLogger = controlsMetricsLogger;
        Object systemService = context.getSystemService("vibrator");
        Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.os.Vibrator");
        this.vibrator = (Vibrator) systemService;
    }

    private final boolean isLocked() {
        return !this.keyguardStateController.isUnlocked();
    }

    public Context getActivityContext() {
        Context context = this.activityContext;
        if (context != null) {
            return context;
        }
        Intrinsics.throwUninitializedPropertyAccessException("activityContext");
        throw null;
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void setActivityContext(Context context) {
        Intrinsics.checkNotNullParameter(context, "<set-?>");
        this.activityContext = context;
    }

    /* compiled from: ControlActionCoordinatorImpl.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void closeDialogs() {
        Dialog dialog = this.dialog;
        if (dialog != null) {
            dialog.dismiss();
        }
        this.dialog = null;
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void toggle(ControlViewHolder controlViewHolder, String str, boolean z) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        Intrinsics.checkNotNullParameter(str, "templateId");
        this.controlsMetricsLogger.touch(controlViewHolder, isLocked());
        bouncerOrRun(createAction(controlViewHolder.getCws().getCi().getControlId(), new Function0<Unit>(controlViewHolder, str, z) { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$toggle$1
            final /* synthetic */ ControlViewHolder $cvh;
            final /* synthetic */ boolean $isChecked;
            final /* synthetic */ String $templateId;

            /* access modifiers changed from: package-private */
            {
                this.$cvh = r1;
                this.$templateId = r2;
                this.$isChecked = r3;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                this.$cvh.getLayout().performHapticFeedback(6);
                this.$cvh.action(new BooleanAction(this.$templateId, !this.$isChecked));
            }
        }, true));
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void touch(ControlViewHolder controlViewHolder, String str, Control control) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        Intrinsics.checkNotNullParameter(str, "templateId");
        Intrinsics.checkNotNullParameter(control, "control");
        this.controlsMetricsLogger.touch(controlViewHolder, isLocked());
        bouncerOrRun(createAction(controlViewHolder.getCws().getCi().getControlId(), new Function0<Unit>(controlViewHolder, this, control, str) { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$touch$1
            final /* synthetic */ Control $control;
            final /* synthetic */ ControlViewHolder $cvh;
            final /* synthetic */ String $templateId;
            final /* synthetic */ ControlActionCoordinatorImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.$cvh = r1;
                this.this$0 = r2;
                this.$control = r3;
                this.$templateId = r4;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                this.$cvh.getLayout().performHapticFeedback(6);
                if (this.$cvh.usePanel()) {
                    ControlActionCoordinatorImpl controlActionCoordinatorImpl = this.this$0;
                    ControlViewHolder controlViewHolder2 = this.$cvh;
                    Intent intent = this.$control.getAppIntent().getIntent();
                    Intrinsics.checkNotNullExpressionValue(intent, "control.getAppIntent().getIntent()");
                    controlActionCoordinatorImpl.showDetail(controlViewHolder2, intent);
                    return;
                }
                this.$cvh.action(new CommandAction(this.$templateId));
            }
        }, controlViewHolder.usePanel()));
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void drag(boolean z) {
        if (z) {
            vibrate(Vibrations.INSTANCE.getRangeEdgeEffect());
        } else {
            vibrate(Vibrations.INSTANCE.getRangeMiddleEffect());
        }
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void setValue(ControlViewHolder controlViewHolder, String str, float f) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        Intrinsics.checkNotNullParameter(str, "templateId");
        this.controlsMetricsLogger.drag(controlViewHolder, isLocked());
        bouncerOrRun(createAction(controlViewHolder.getCws().getCi().getControlId(), new Function0<Unit>(controlViewHolder, str, f) { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$setValue$1
            final /* synthetic */ ControlViewHolder $cvh;
            final /* synthetic */ float $newValue;
            final /* synthetic */ String $templateId;

            /* access modifiers changed from: package-private */
            {
                this.$cvh = r1;
                this.$templateId = r2;
                this.$newValue = r3;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                this.$cvh.action(new FloatAction(this.$templateId, this.$newValue));
            }
        }, false));
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void longPress(ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        this.controlsMetricsLogger.longPress(controlViewHolder, isLocked());
        bouncerOrRun(createAction(controlViewHolder.getCws().getCi().getControlId(), new Function0<Unit>(controlViewHolder, this) { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$longPress$1
            final /* synthetic */ ControlViewHolder $cvh;
            final /* synthetic */ ControlActionCoordinatorImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.$cvh = r1;
                this.this$0 = r2;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                Control control = this.$cvh.getCws().getControl();
                if (control != null) {
                    ControlViewHolder controlViewHolder2 = this.$cvh;
                    ControlActionCoordinatorImpl controlActionCoordinatorImpl = this.this$0;
                    controlViewHolder2.getLayout().performHapticFeedback(0);
                    Intent intent = control.getAppIntent().getIntent();
                    Intrinsics.checkNotNullExpressionValue(intent, "it.getAppIntent().getIntent()");
                    ControlActionCoordinatorImpl.access$showDetail(controlActionCoordinatorImpl, controlViewHolder2, intent);
                }
            }
        }, false));
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void runPendingAction(String str) {
        Intrinsics.checkNotNullParameter(str, "controlId");
        if (!isLocked()) {
            Action action = this.pendingAction;
            if (Intrinsics.areEqual(action == null ? null : action.getControlId(), str)) {
                Action action2 = this.pendingAction;
                if (action2 != null) {
                    action2.invoke();
                }
                this.pendingAction = null;
            }
        }
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void enableActionOnTouch(String str) {
        Intrinsics.checkNotNullParameter(str, "controlId");
        this.actionsInProgress.remove(str);
    }

    public final boolean shouldRunAction(String str) {
        if (!this.actionsInProgress.add(str)) {
            return false;
        }
        this.uiExecutor.executeDelayed(new Runnable(this, str) { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$shouldRunAction$1
            final /* synthetic */ String $controlId;
            final /* synthetic */ ControlActionCoordinatorImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$controlId = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.actionsInProgress.remove(this.$controlId);
            }
        }, 3000);
        return true;
    }

    @VisibleForTesting
    public final void bouncerOrRun(Action action) {
        Intrinsics.checkNotNullParameter(action, "action");
        if (this.keyguardStateController.isShowing()) {
            if (isLocked()) {
                this.context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
                this.pendingAction = action;
            }
            this.activityStarter.dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction(action) { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$bouncerOrRun$1
                final /* synthetic */ ControlActionCoordinatorImpl.Action $action;

                /* access modifiers changed from: package-private */
                {
                    this.$action = r1;
                }

                @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                public final boolean onDismiss() {
                    Log.d("ControlsUiController", "Device unlocked, invoking controls action");
                    this.$action.invoke();
                    return true;
                }
            }, new Runnable(this) { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$bouncerOrRun$2
                final /* synthetic */ ControlActionCoordinatorImpl this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    ControlActionCoordinatorImpl.access$setPendingAction$p(this.this$0, null);
                }
            }, true);
            return;
        }
        action.invoke();
    }

    private final void vibrate(VibrationEffect vibrationEffect) {
        this.bgExecutor.execute(new Runnable(this, vibrationEffect) { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$vibrate$1
            final /* synthetic */ VibrationEffect $effect;
            final /* synthetic */ ControlActionCoordinatorImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$effect = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.vibrator.vibrate(this.$effect);
            }
        });
    }

    public final void showDetail(ControlViewHolder controlViewHolder, Intent intent) {
        this.bgExecutor.execute(new Runnable(this, intent, controlViewHolder) { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showDetail$1
            final /* synthetic */ ControlViewHolder $cvh;
            final /* synthetic */ Intent $intent;
            final /* synthetic */ ControlActionCoordinatorImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$intent = r2;
                this.$cvh = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                final List<ResolveInfo> queryIntentActivities = this.this$0.context.getPackageManager().queryIntentActivities(this.$intent, 65536);
                Intrinsics.checkNotNullExpressionValue(queryIntentActivities, "context.packageManager.queryIntentActivities(\n                intent,\n                PackageManager.MATCH_DEFAULT_ONLY\n            )");
                DelayableExecutor delayableExecutor = this.this$0.uiExecutor;
                final ControlActionCoordinatorImpl controlActionCoordinatorImpl = this.this$0;
                final ControlViewHolder controlViewHolder2 = this.$cvh;
                final Intent intent2 = this.$intent;
                delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showDetail$1.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        if (!(!queryIntentActivities.isEmpty()) || !controlActionCoordinatorImpl.taskViewFactory.isPresent()) {
                            controlViewHolder2.setErrorStatus();
                            return;
                        }
                        Context context = controlActionCoordinatorImpl.context;
                        DelayableExecutor delayableExecutor2 = controlActionCoordinatorImpl.uiExecutor;
                        final ControlActionCoordinatorImpl controlActionCoordinatorImpl2 = controlActionCoordinatorImpl;
                        final Intent intent3 = intent2;
                        final ControlViewHolder controlViewHolder3 = controlViewHolder2;
                        ((TaskViewFactory) controlActionCoordinatorImpl.taskViewFactory.get()).create(context, delayableExecutor2, new Consumer<TaskView>() { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl.showDetail.1.1.1
                            public final void accept(TaskView taskView) {
                                ControlActionCoordinatorImpl controlActionCoordinatorImpl3 = controlActionCoordinatorImpl2;
                                Context activityContext = controlActionCoordinatorImpl2.getActivityContext();
                                Intrinsics.checkNotNullExpressionValue(taskView, "it");
                                DetailDialog detailDialog = new DetailDialog(activityContext, taskView, intent3, controlViewHolder3);
                                detailDialog.setOnDismissListener(new ControlActionCoordinatorImpl$showDetail$1$1$1$1$1(controlActionCoordinatorImpl2));
                                detailDialog.show();
                                Unit unit = Unit.INSTANCE;
                                controlActionCoordinatorImpl3.dialog = detailDialog;
                            }
                        });
                    }
                });
            }
        });
    }

    @VisibleForTesting
    public final Action createAction(String str, Function0<Unit> function0, boolean z) {
        Intrinsics.checkNotNullParameter(str, "controlId");
        Intrinsics.checkNotNullParameter(function0, "f");
        return new Action(this, str, function0, z);
    }

    /* compiled from: ControlActionCoordinatorImpl.kt */
    /* loaded from: classes.dex */
    public final class Action {
        private final boolean blockable;
        private final String controlId;
        private final Function0<Unit> f;
        final /* synthetic */ ControlActionCoordinatorImpl this$0;

        public Action(ControlActionCoordinatorImpl controlActionCoordinatorImpl, String str, Function0<Unit> function0, boolean z) {
            Intrinsics.checkNotNullParameter(controlActionCoordinatorImpl, "this$0");
            Intrinsics.checkNotNullParameter(str, "controlId");
            Intrinsics.checkNotNullParameter(function0, "f");
            this.this$0 = controlActionCoordinatorImpl;
            this.controlId = str;
            this.f = function0;
            this.blockable = z;
        }

        public final String getControlId() {
            return this.controlId;
        }

        public final void invoke() {
            if (!this.blockable || this.this$0.shouldRunAction(this.controlId)) {
                this.f.invoke();
            }
        }
    }
}
