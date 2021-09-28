package com.android.systemui.statusbar.notification;

import android.graphics.drawable.AnimatedImageDrawable;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.util.List;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class AnimatedImageNotificationManager {
    private final HeadsUpManager headsUpManager;
    private boolean isStatusBarExpanded;
    private final NotificationEntryManager notificationEntryManager;
    private final StatusBarStateController statusBarStateController;

    public AnimatedImageNotificationManager(NotificationEntryManager notificationEntryManager, HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController) {
        Intrinsics.checkNotNullParameter(notificationEntryManager, "notificationEntryManager");
        Intrinsics.checkNotNullParameter(headsUpManager, "headsUpManager");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        this.notificationEntryManager = notificationEntryManager;
        this.headsUpManager = headsUpManager;
        this.statusBarStateController = statusBarStateController;
    }

    public final void bind() {
        this.headsUpManager.addListener(new OnHeadsUpChangedListener(this) { // from class: com.android.systemui.statusbar.notification.AnimatedImageNotificationManager$bind$1
            final /* synthetic */ AnimatedImageNotificationManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
            public void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
                Intrinsics.checkNotNullParameter(notificationEntry, "entry");
                ExpandableNotificationRow row = notificationEntry.getRow();
                if (row != null) {
                    AnimatedImageNotificationManager animatedImageNotificationManager = this.this$0;
                    animatedImageNotificationManager.updateAnimatedImageDrawables(row, z || (animatedImageNotificationManager.isStatusBarExpanded));
                }
            }
        });
        this.statusBarStateController.addCallback(new StatusBarStateController.StateListener(this) { // from class: com.android.systemui.statusbar.notification.AnimatedImageNotificationManager$bind$2
            final /* synthetic */ AnimatedImageNotificationManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onExpandedChanged(boolean z) {
                this.this$0.isStatusBarExpanded = z;
                List<NotificationEntry> activeNotificationsForCurrentUser = this.this$0.notificationEntryManager.getActiveNotificationsForCurrentUser();
                Intrinsics.checkNotNullExpressionValue(activeNotificationsForCurrentUser, "notificationEntryManager.activeNotificationsForCurrentUser");
                AnimatedImageNotificationManager animatedImageNotificationManager = this.this$0;
                for (NotificationEntry notificationEntry : activeNotificationsForCurrentUser) {
                    ExpandableNotificationRow row = notificationEntry.getRow();
                    if (row != null) {
                        animatedImageNotificationManager.updateAnimatedImageDrawables(row, z || row.isHeadsUp());
                    }
                }
            }
        });
        this.notificationEntryManager.addNotificationEntryListener(new NotificationEntryListener(this) { // from class: com.android.systemui.statusbar.notification.AnimatedImageNotificationManager$bind$3
            final /* synthetic */ AnimatedImageNotificationManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onEntryInflated(NotificationEntry notificationEntry) {
                Intrinsics.checkNotNullParameter(notificationEntry, "entry");
                ExpandableNotificationRow row = notificationEntry.getRow();
                if (row != null) {
                    AnimatedImageNotificationManager animatedImageNotificationManager = this.this$0;
                    animatedImageNotificationManager.updateAnimatedImageDrawables(row, (animatedImageNotificationManager.isStatusBarExpanded) || row.isHeadsUp());
                }
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onEntryReinflated(NotificationEntry notificationEntry) {
                Intrinsics.checkNotNullParameter(notificationEntry, "entry");
                onEntryInflated(notificationEntry);
            }
        });
    }

    /* access modifiers changed from: private */
    public final void updateAnimatedImageDrawables(ExpandableNotificationRow expandableNotificationRow, boolean z) {
        NotificationContentView[] layouts = expandableNotificationRow.getLayouts();
        Sequence sequence = layouts == null ? null : ArraysKt___ArraysKt.asSequence(layouts);
        if (sequence == null) {
            sequence = SequencesKt__SequencesKt.emptySequence();
        }
        for (AnimatedImageDrawable animatedImageDrawable : SequencesKt___SequencesKt.mapNotNull(SequencesKt___SequencesKt.flatMap(SequencesKt___SequencesKt.flatMap(SequencesKt___SequencesKt.flatMap(sequence, AnimatedImageNotificationManager$updateAnimatedImageDrawables$1.INSTANCE), AnimatedImageNotificationManager$updateAnimatedImageDrawables$2.INSTANCE), AnimatedImageNotificationManager$updateAnimatedImageDrawables$3.INSTANCE), AnimatedImageNotificationManager$updateAnimatedImageDrawables$4.INSTANCE)) {
            if (z) {
                animatedImageDrawable.start();
            } else {
                animatedImageDrawable.stop();
            }
        }
    }
}
