package com.android.systemui.statusbar.notification.row;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.NotificationChannel;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.R$id;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ChannelEditorListView.kt */
/* loaded from: classes.dex */
public final class ChannelRow extends LinearLayout {
    private NotificationChannel channel;
    private TextView channelDescription;
    private TextView channelName;
    public ChannelEditorDialogController controller;
    private boolean gentle;
    private final int highlightColor = Utils.getColorAttrDefaultColor(getContext(), 16843820);

    /* renamed from: switch */
    private Switch f1switch;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelRow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "c");
        Intrinsics.checkNotNullParameter(attributeSet, "attrs");
    }

    public final ChannelEditorDialogController getController() {
        ChannelEditorDialogController channelEditorDialogController = this.controller;
        if (channelEditorDialogController != null) {
            return channelEditorDialogController;
        }
        Intrinsics.throwUninitializedPropertyAccessException("controller");
        throw null;
    }

    public final void setController(ChannelEditorDialogController channelEditorDialogController) {
        Intrinsics.checkNotNullParameter(channelEditorDialogController, "<set-?>");
        this.controller = channelEditorDialogController;
    }

    public final NotificationChannel getChannel() {
        return this.channel;
    }

    public final void setChannel(NotificationChannel notificationChannel) {
        this.channel = notificationChannel;
        updateImportance();
        updateViews();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        View findViewById = findViewById(R$id.channel_name);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(R.id.channel_name)");
        this.channelName = (TextView) findViewById;
        View findViewById2 = findViewById(R$id.channel_description);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(R.id.channel_description)");
        this.channelDescription = (TextView) findViewById2;
        View findViewById3 = findViewById(R$id.toggle);
        Intrinsics.checkNotNullExpressionValue(findViewById3, "findViewById(R.id.toggle)");
        Switch r0 = (Switch) findViewById3;
        this.f1switch = r0;
        if (r0 != null) {
            r0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this) { // from class: com.android.systemui.statusbar.notification.row.ChannelRow$onFinishInflate$1
                final /* synthetic */ ChannelRow this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    NotificationChannel channel = this.this$0.getChannel();
                    if (channel != null) {
                        this.this$0.getController().proposeEditForChannel(channel, z ? channel.getImportance() : 0);
                    }
                }
            });
            setOnClickListener(new View.OnClickListener(this) { // from class: com.android.systemui.statusbar.notification.row.ChannelRow$onFinishInflate$2
                final /* synthetic */ ChannelRow this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    Switch access$getSwitch$p = ChannelRow.access$getSwitch$p(this.this$0);
                    if (access$getSwitch$p != null) {
                        access$getSwitch$p.toggle();
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("switch");
                        throw null;
                    }
                }
            });
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("switch");
        throw null;
    }

    public final void playHighlight() {
        ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), 0, Integer.valueOf(this.highlightColor));
        ofObject.setDuration(200L);
        ofObject.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.systemui.statusbar.notification.row.ChannelRow$playHighlight$1
            final /* synthetic */ ChannelRow this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ChannelRow channelRow = this.this$0;
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Int");
                channelRow.setBackgroundColor(((Integer) animatedValue).intValue());
            }
        });
        ofObject.setRepeatMode(2);
        ofObject.setRepeatCount(5);
        ofObject.start();
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0060  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x006b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void updateViews() {
        /*
            r6 = this;
            android.app.NotificationChannel r0 = r6.channel
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            android.widget.TextView r1 = r6.channelName
            r2 = 0
            if (r1 == 0) goto L_0x007a
            java.lang.CharSequence r3 = r0.getName()
            if (r3 != 0) goto L_0x0012
            java.lang.String r3 = ""
        L_0x0012:
            r1.setText(r3)
            java.lang.String r1 = r0.getGroup()
            java.lang.String r3 = "channelDescription"
            if (r1 != 0) goto L_0x001e
            goto L_0x002d
        L_0x001e:
            android.widget.TextView r4 = r6.channelDescription
            if (r4 == 0) goto L_0x0076
            com.android.systemui.statusbar.notification.row.ChannelEditorDialogController r5 = r6.getController()
            java.lang.CharSequence r1 = r5.groupNameForId(r1)
            r4.setText(r1)
        L_0x002d:
            java.lang.String r1 = r0.getGroup()
            r4 = 0
            if (r1 == 0) goto L_0x0053
            android.widget.TextView r1 = r6.channelDescription
            if (r1 == 0) goto L_0x004f
            java.lang.CharSequence r1 = r1.getText()
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 == 0) goto L_0x0043
            goto L_0x0053
        L_0x0043:
            android.widget.TextView r1 = r6.channelDescription
            if (r1 == 0) goto L_0x004b
            r1.setVisibility(r4)
            goto L_0x005c
        L_0x004b:
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r3)
            throw r2
        L_0x004f:
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r3)
            throw r2
        L_0x0053:
            android.widget.TextView r1 = r6.channelDescription
            if (r1 == 0) goto L_0x0072
            r3 = 8
            r1.setVisibility(r3)
        L_0x005c:
            android.widget.Switch r6 = r6.f1switch
            if (r6 == 0) goto L_0x006b
            int r0 = r0.getImportance()
            if (r0 == 0) goto L_0x0067
            r4 = 1
        L_0x0067:
            r6.setChecked(r4)
            return
        L_0x006b:
            java.lang.String r6 = "switch"
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r6)
            throw r2
        L_0x0072:
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r3)
            throw r2
        L_0x0076:
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r3)
            throw r2
        L_0x007a:
            java.lang.String r6 = "channelName"
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r6)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.ChannelRow.updateViews():void");
    }

    private final void updateImportance() {
        NotificationChannel notificationChannel = this.channel;
        boolean z = false;
        int importance = notificationChannel == null ? 0 : notificationChannel.getImportance();
        if (importance != -1000 && importance < 3) {
            z = true;
        }
        this.gentle = z;
    }
}
