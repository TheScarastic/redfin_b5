package com.android.systemui.statusbar.policy;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.policy.SmartReplyView;
import java.util.Objects;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
public final class SmartReplyInflaterImpl implements SmartReplyInflater {
    private final SmartReplyConstants constants;
    private final Context context;
    private final KeyguardDismissUtil keyguardDismissUtil;
    private final NotificationRemoteInputManager remoteInputManager;
    private final SmartReplyController smartReplyController;

    public SmartReplyInflaterImpl(SmartReplyConstants smartReplyConstants, KeyguardDismissUtil keyguardDismissUtil, NotificationRemoteInputManager notificationRemoteInputManager, SmartReplyController smartReplyController, Context context) {
        Intrinsics.checkNotNullParameter(smartReplyConstants, "constants");
        Intrinsics.checkNotNullParameter(keyguardDismissUtil, "keyguardDismissUtil");
        Intrinsics.checkNotNullParameter(notificationRemoteInputManager, "remoteInputManager");
        Intrinsics.checkNotNullParameter(smartReplyController, "smartReplyController");
        Intrinsics.checkNotNullParameter(context, "context");
        this.constants = smartReplyConstants;
        this.keyguardDismissUtil = keyguardDismissUtil;
        this.remoteInputManager = notificationRemoteInputManager;
        this.smartReplyController = smartReplyController;
        this.context = context;
    }

    @Override // com.android.systemui.statusbar.policy.SmartReplyInflater
    public Button inflateReplyButton(SmartReplyView smartReplyView, NotificationEntry notificationEntry, SmartReplyView.SmartReplies smartReplies, int i, CharSequence charSequence, boolean z) {
        Intrinsics.checkNotNullParameter(smartReplyView, "parent");
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(smartReplies, "smartReplies");
        Intrinsics.checkNotNullParameter(charSequence, "choice");
        View inflate = LayoutInflater.from(smartReplyView.getContext()).inflate(R$layout.smart_reply_button, (ViewGroup) smartReplyView, false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.widget.Button");
        Button button = (Button) inflate;
        button.setText(charSequence);
        View.OnClickListener smartReplyInflaterImpl$inflateReplyButton$1$onClickListener$1 = new View.OnClickListener(this, notificationEntry, smartReplies, i, smartReplyView, button, charSequence) { // from class: com.android.systemui.statusbar.policy.SmartReplyInflaterImpl$inflateReplyButton$1$onClickListener$1
            final /* synthetic */ CharSequence $choice;
            final /* synthetic */ NotificationEntry $entry;
            final /* synthetic */ SmartReplyView $parent;
            final /* synthetic */ int $replyIndex;
            final /* synthetic */ SmartReplyView.SmartReplies $smartReplies;
            final /* synthetic */ Button $this_apply;
            final /* synthetic */ SmartReplyInflaterImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$entry = r2;
                this.$smartReplies = r3;
                this.$replyIndex = r4;
                this.$parent = r5;
                this.$this_apply = r6;
                this.$choice = r7;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SmartReplyInflaterImpl.access$onSmartReplyClick(this.this$0, this.$entry, this.$smartReplies, this.$replyIndex, this.$parent, this.$this_apply, this.$choice);
            }
        };
        if (z) {
            smartReplyInflaterImpl$inflateReplyButton$1$onClickListener$1 = new DelayedOnClickListener(smartReplyInflaterImpl$inflateReplyButton$1$onClickListener$1, this.constants.getOnClickInitDelay());
        }
        button.setOnClickListener(smartReplyInflaterImpl$inflateReplyButton$1$onClickListener$1);
        button.setAccessibilityDelegate(new View.AccessibilityDelegate(smartReplyView) { // from class: com.android.systemui.statusbar.policy.SmartReplyInflaterImpl$inflateReplyButton$1$1
            final /* synthetic */ SmartReplyView $parent;

            /* access modifiers changed from: package-private */
            {
                this.$parent = r1;
            }

            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                Intrinsics.checkNotNullParameter(view, "host");
                Intrinsics.checkNotNullParameter(accessibilityNodeInfo, "info");
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, this.$parent.getResources().getString(R$string.accessibility_send_smart_reply)));
            }
        });
        ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type com.android.systemui.statusbar.policy.SmartReplyView.LayoutParams");
        ((SmartReplyView.LayoutParams) layoutParams).mButtonType = SmartReplyView.SmartButtonType.REPLY;
        return button;
    }

    public final void onSmartReplyClick(NotificationEntry notificationEntry, SmartReplyView.SmartReplies smartReplies, int i, SmartReplyView smartReplyView, Button button, CharSequence charSequence) {
        SmartReplyStateInflaterKt.executeWhenUnlocked(this.keyguardDismissUtil, !notificationEntry.isRowPinned(), new Function0<Boolean>(this, smartReplies, button, charSequence, i, notificationEntry, smartReplyView) { // from class: com.android.systemui.statusbar.policy.SmartReplyInflaterImpl$onSmartReplyClick$1
            final /* synthetic */ Button $button;
            final /* synthetic */ CharSequence $choice;
            final /* synthetic */ NotificationEntry $entry;
            final /* synthetic */ int $replyIndex;
            final /* synthetic */ SmartReplyView.SmartReplies $smartReplies;
            final /* synthetic */ SmartReplyView $smartReplyView;
            final /* synthetic */ SmartReplyInflaterImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$smartReplies = r2;
                this.$button = r3;
                this.$choice = r4;
                this.$replyIndex = r5;
                this.$entry = r6;
                this.$smartReplyView = r7;
            }

            /* Return type fixed from 'boolean' to match base method */
            @Override // kotlin.jvm.functions.Function0
            public final Boolean invoke() {
                if (SmartReplyInflaterImpl.access$getConstants$p(this.this$0).getEffectiveEditChoicesBeforeSending(this.$smartReplies.remoteInput.getEditChoicesBeforeSending())) {
                    NotificationRemoteInputManager access$getRemoteInputManager$p = SmartReplyInflaterImpl.access$getRemoteInputManager$p(this.this$0);
                    Button button2 = this.$button;
                    SmartReplyView.SmartReplies smartReplies2 = this.$smartReplies;
                    RemoteInput remoteInput = smartReplies2.remoteInput;
                    access$getRemoteInputManager$p.activateRemoteInput(button2, new RemoteInput[]{remoteInput}, remoteInput, smartReplies2.pendingIntent, new NotificationEntry.EditedSuggestionInfo(this.$choice, this.$replyIndex));
                } else {
                    SmartReplyInflaterImpl.access$getSmartReplyController$p(this.this$0).smartReplySent(this.$entry, this.$replyIndex, this.$button.getText(), NotificationLogger.getNotificationLocation(this.$entry).toMetricsEventEnum(), false);
                    this.$entry.setHasSentReply();
                    try {
                        this.$smartReplies.pendingIntent.send(SmartReplyInflaterImpl.access$getContext$p(this.this$0), 0, SmartReplyInflaterImpl.access$createRemoteInputIntent(this.this$0, this.$smartReplies, this.$choice));
                    } catch (PendingIntent.CanceledException e) {
                        Log.w("SmartReplyViewInflater", "Unable to send smart reply", e);
                    }
                    this.$smartReplyView.hideSmartSuggestions();
                }
                return null;
            }
        });
    }

    public final Intent createRemoteInputIntent(SmartReplyView.SmartReplies smartReplies, CharSequence charSequence) {
        Bundle bundle = new Bundle();
        bundle.putString(smartReplies.remoteInput.getResultKey(), charSequence.toString());
        Intent addFlags = new Intent().addFlags(268435456);
        RemoteInput.addResultsToIntent(new RemoteInput[]{smartReplies.remoteInput}, addFlags, bundle);
        RemoteInput.setResultsSource(addFlags, 1);
        Intrinsics.checkNotNullExpressionValue(addFlags, "intent");
        return addFlags;
    }
}
