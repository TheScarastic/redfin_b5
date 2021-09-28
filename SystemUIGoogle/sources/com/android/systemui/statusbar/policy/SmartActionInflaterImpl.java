package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.android.systemui.R$dimen;
import com.android.systemui.R$layout;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
public final class SmartActionInflaterImpl implements SmartActionInflater {
    private final ActivityStarter activityStarter;
    private final SmartReplyConstants constants;
    private final HeadsUpManager headsUpManager;
    private final SmartReplyController smartReplyController;

    public SmartActionInflaterImpl(SmartReplyConstants smartReplyConstants, ActivityStarter activityStarter, SmartReplyController smartReplyController, HeadsUpManager headsUpManager) {
        Intrinsics.checkNotNullParameter(smartReplyConstants, "constants");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(smartReplyController, "smartReplyController");
        Intrinsics.checkNotNullParameter(headsUpManager, "headsUpManager");
        this.constants = smartReplyConstants;
        this.activityStarter = activityStarter;
        this.smartReplyController = smartReplyController;
        this.headsUpManager = headsUpManager;
    }

    @Override // com.android.systemui.statusbar.policy.SmartActionInflater
    public Button inflateActionButton(ViewGroup viewGroup, NotificationEntry notificationEntry, SmartReplyView.SmartActions smartActions, int i, Notification.Action action, boolean z, Context context) {
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(smartActions, "smartActions");
        Intrinsics.checkNotNullParameter(action, "action");
        Intrinsics.checkNotNullParameter(context, "packageContext");
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.smart_action_button, viewGroup, false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.widget.Button");
        Button button = (Button) inflate;
        button.setText(action.title);
        Drawable loadDrawable = action.getIcon().loadDrawable(context);
        int dimensionPixelSize = button.getContext().getResources().getDimensionPixelSize(R$dimen.smart_action_button_icon_size);
        loadDrawable.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
        button.setCompoundDrawables(loadDrawable, null, null, null);
        View.OnClickListener smartActionInflaterImpl$inflateActionButton$1$onClickListener$1 = new View.OnClickListener(this, notificationEntry, smartActions, i, action) { // from class: com.android.systemui.statusbar.policy.SmartActionInflaterImpl$inflateActionButton$1$onClickListener$1
            final /* synthetic */ Notification.Action $action;
            final /* synthetic */ int $actionIndex;
            final /* synthetic */ NotificationEntry $entry;
            final /* synthetic */ SmartReplyView.SmartActions $smartActions;
            final /* synthetic */ SmartActionInflaterImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$entry = r2;
                this.$smartActions = r3;
                this.$actionIndex = r4;
                this.$action = r5;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SmartActionInflaterImpl.access$onSmartActionClick(this.this$0, this.$entry, this.$smartActions, this.$actionIndex, this.$action);
            }
        };
        if (z) {
            smartActionInflaterImpl$inflateActionButton$1$onClickListener$1 = new DelayedOnClickListener(smartActionInflaterImpl$inflateActionButton$1$onClickListener$1, this.constants.getOnClickInitDelay());
        }
        button.setOnClickListener(smartActionInflaterImpl$inflateActionButton$1$onClickListener$1);
        ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type com.android.systemui.statusbar.policy.SmartReplyView.LayoutParams");
        ((SmartReplyView.LayoutParams) layoutParams).mButtonType = SmartReplyView.SmartButtonType.ACTION;
        return button;
    }

    public final void onSmartActionClick(NotificationEntry notificationEntry, SmartReplyView.SmartActions smartActions, int i, Notification.Action action) {
        if (!smartActions.fromAssistant || 11 != action.getSemanticAction()) {
            ActivityStarter activityStarter = this.activityStarter;
            PendingIntent pendingIntent = action.actionIntent;
            Intrinsics.checkNotNullExpressionValue(pendingIntent, "action.actionIntent");
            SmartReplyStateInflaterKt.startPendingIntentDismissingKeyguard(activityStarter, pendingIntent, notificationEntry.getRow(), new Function0<Unit>(this, notificationEntry, i, action, smartActions) { // from class: com.android.systemui.statusbar.policy.SmartActionInflaterImpl$onSmartActionClick$1
                final /* synthetic */ Notification.Action $action;
                final /* synthetic */ int $actionIndex;
                final /* synthetic */ NotificationEntry $entry;
                final /* synthetic */ SmartReplyView.SmartActions $smartActions;
                final /* synthetic */ SmartActionInflaterImpl this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$entry = r2;
                    this.$actionIndex = r3;
                    this.$action = r4;
                    this.$smartActions = r5;
                }

                @Override // kotlin.jvm.functions.Function0
                public final void invoke() {
                    this.this$0.smartReplyController.smartActionClicked(this.$entry, this.$actionIndex, this.$action, this.$smartActions.fromAssistant);
                }
            });
            return;
        }
        notificationEntry.getRow().doSmartActionClick(((int) notificationEntry.getRow().getX()) / 2, ((int) notificationEntry.getRow().getY()) / 2, 11);
        this.smartReplyController.smartActionClicked(notificationEntry, i, action, smartActions.fromAssistant);
    }
}
