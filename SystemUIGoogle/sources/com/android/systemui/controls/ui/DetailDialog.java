package com.android.systemui.controls.ui;

import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Insets;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageView;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$style;
import com.android.wm.shell.TaskView;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DetailDialog.kt */
/* loaded from: classes.dex */
public final class DetailDialog extends Dialog {
    public static final Companion Companion = new Companion(null);
    private final Context activityContext;
    private final ControlViewHolder cvh;
    private int detailTaskId;
    private final Intent intent;
    private final TaskView.Listener stateCallback;
    private final TaskView taskView;

    public final Context getActivityContext() {
        return this.activityContext;
    }

    public final TaskView getTaskView() {
        return this.taskView;
    }

    public final Intent getIntent() {
        return this.intent;
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public DetailDialog(Context context, TaskView taskView, Intent intent, ControlViewHolder controlViewHolder) {
        super(context == null ? controlViewHolder.getContext() : context, R$style.Theme_SystemUI_Dialog_Control_DetailPanel);
        Intrinsics.checkNotNullParameter(taskView, "taskView");
        Intrinsics.checkNotNullParameter(intent, "intent");
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        this.activityContext = context;
        this.taskView = taskView;
        this.intent = intent;
        this.cvh = controlViewHolder;
        this.detailTaskId = -1;
        DetailDialog$stateCallback$1 detailDialog$stateCallback$1 = new TaskView.Listener(this) { // from class: com.android.systemui.controls.ui.DetailDialog$stateCallback$1
            final /* synthetic */ DetailDialog this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.wm.shell.TaskView.Listener
            public void onInitialized() {
                ActivityOptions activityOptions;
                Intent intent2 = new Intent(this.this$0.getIntent());
                intent2.putExtra("controls.DISPLAY_IN_PANEL", true);
                intent2.addFlags(524288);
                intent2.addFlags(134217728);
                Context activityContext = this.this$0.getActivityContext();
                if (activityContext == null) {
                    activityOptions = null;
                } else {
                    activityOptions = ActivityOptions.makeCustomAnimation(activityContext, 0, 0);
                }
                if (activityOptions == null) {
                    activityOptions = ActivityOptions.makeBasic();
                }
                this.this$0.getTaskView().startActivity(PendingIntent.getActivity(this.this$0.getContext(), 0, intent2, 201326592), null, activityOptions, this.this$0.getTaskViewBounds());
            }

            @Override // com.android.wm.shell.TaskView.Listener
            public void onTaskRemovalStarted(int i) {
                this.this$0.setDetailTaskId(-1);
                this.this$0.dismiss();
            }

            @Override // com.android.wm.shell.TaskView.Listener
            public void onTaskCreated(int i, ComponentName componentName) {
                this.this$0.setDetailTaskId(i);
            }

            @Override // com.android.wm.shell.TaskView.Listener
            public void onReleased() {
                this.this$0.removeDetailTask();
            }

            @Override // com.android.wm.shell.TaskView.Listener
            public void onBackPressedOnTaskRoot(int i) {
                this.this$0.dismiss();
            }
        };
        this.stateCallback = detailDialog$stateCallback$1;
        if (context == null) {
            getWindow().setType(2020);
        }
        getWindow().addFlags(32);
        getWindow().addPrivateFlags(536870912);
        setContentView(R$layout.controls_detail_dialog);
        ((ViewGroup) requireViewById(R$id.controls_activity_view)).addView(getTaskView());
        ((ImageView) requireViewById(R$id.control_detail_close)).setOnClickListener(new View.OnClickListener(this) { // from class: com.android.systemui.controls.ui.DetailDialog$2$1
            final /* synthetic */ DetailDialog this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Intrinsics.checkNotNullParameter(view, "$noName_0");
                this.this$0.dismiss();
            }
        });
        ImageView imageView = (ImageView) requireViewById(R$id.control_detail_open_in_app);
        imageView.setOnClickListener(new View.OnClickListener(this, imageView) { // from class: com.android.systemui.controls.ui.DetailDialog$3$1
            final /* synthetic */ ImageView $this_apply;
            final /* synthetic */ DetailDialog this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$this_apply = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Intrinsics.checkNotNullParameter(view, "v");
                this.this$0.removeDetailTask();
                this.this$0.dismiss();
                this.$this_apply.getContext().sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
                view.getContext().startActivity(this.this$0.getIntent());
            }
        });
        getWindow().getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener(this) { // from class: com.android.systemui.controls.ui.DetailDialog.4
            final /* synthetic */ DetailDialog this$0;

            {
                this.this$0 = r1;
            }

            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                Intrinsics.checkNotNullParameter(view, "v");
                Intrinsics.checkNotNullParameter(windowInsets, "insets");
                TaskView taskView2 = this.this$0.getTaskView();
                taskView2.setPadding(taskView2.getPaddingLeft(), taskView2.getPaddingTop(), taskView2.getPaddingRight(), windowInsets.getInsets(WindowInsets.Type.systemBars()).bottom);
                int paddingLeft = view.getPaddingLeft();
                int paddingBottom = view.getPaddingBottom();
                view.setPadding(paddingLeft, windowInsets.getInsets(WindowInsets.Type.systemBars()).top, view.getPaddingRight(), paddingBottom);
                return WindowInsets.CONSUMED;
            }
        });
        if (ScreenDecorationsUtils.supportsRoundedCornersOnWindows(getContext().getResources())) {
            taskView.setCornerRadius((float) getContext().getResources().getDimensionPixelSize(R$dimen.controls_activity_view_corner_radius));
        }
        taskView.setListener(controlViewHolder.getUiExecutor(), detailDialog$stateCallback$1);
    }

    /* compiled from: DetailDialog.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final void setDetailTaskId(int i) {
        this.detailTaskId = i;
    }

    public final void removeDetailTask() {
        if (this.detailTaskId != -1) {
            ActivityTaskManager.getInstance().removeTask(this.detailTaskId);
            this.detailTaskId = -1;
        }
    }

    public final Rect getTaskViewBounds() {
        WindowMetrics currentWindowMetrics = ((WindowManager) getContext().getSystemService(WindowManager.class)).getCurrentWindowMetrics();
        Rect bounds = currentWindowMetrics.getBounds();
        Insets insetsIgnoringVisibility = currentWindowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
        return new Rect(bounds.left - insetsIgnoringVisibility.left, bounds.top + insetsIgnoringVisibility.top + getContext().getResources().getDimensionPixelSize(R$dimen.controls_detail_dialog_header_height), bounds.right - insetsIgnoringVisibility.right, bounds.bottom - insetsIgnoringVisibility.bottom);
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        if (isShowing()) {
            this.taskView.release();
            super.dismiss();
        }
    }
}
