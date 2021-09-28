package com.android.systemui.statusbar.notification;

import android.app.Notification;
import android.os.SystemClock;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import com.android.systemui.DejankUtils;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wm.shell.bubbles.Bubbles;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class NotificationClicker implements View.OnClickListener {
    private final Optional<Bubbles> mBubblesOptional;
    private final NotificationClickerLogger mLogger;
    private final NotificationActivityStarter mNotificationActivityStarter;
    private final Optional<StatusBar> mStatusBarOptional;

    private NotificationClicker(NotificationClickerLogger notificationClickerLogger, Optional<StatusBar> optional, Optional<Bubbles> optional2, NotificationActivityStarter notificationActivityStarter) {
        this.mLogger = notificationClickerLogger;
        this.mStatusBarOptional = optional;
        this.mBubblesOptional = optional2;
        this.mNotificationActivityStarter = notificationActivityStarter;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (!(view instanceof ExpandableNotificationRow)) {
            Log.e("NotificationClicker", "NotificationClicker called on a view that is not a notification row.");
            return;
        }
        this.mStatusBarOptional.ifPresent(new Consumer(view) { // from class: com.android.systemui.statusbar.notification.NotificationClicker$$ExternalSyntheticLambda1
            public final /* synthetic */ View f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NotificationClicker.lambda$onClick$0(this.f$0, (StatusBar) obj);
            }
        });
        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
        NotificationEntry entry = expandableNotificationRow.getEntry();
        this.mLogger.logOnClick(entry);
        if (isMenuVisible(expandableNotificationRow)) {
            this.mLogger.logMenuVisible(entry);
            expandableNotificationRow.animateResetTranslation();
        } else if (expandableNotificationRow.isChildInGroup() && isMenuVisible(expandableNotificationRow.getNotificationParent())) {
            this.mLogger.logParentMenuVisible(entry);
            expandableNotificationRow.getNotificationParent().animateResetTranslation();
        } else if (expandableNotificationRow.isSummaryWithChildren() && expandableNotificationRow.areChildrenExpanded()) {
            this.mLogger.logChildrenExpanded(entry);
        } else if (expandableNotificationRow.areGutsExposed()) {
            this.mLogger.logGutsExposed(entry);
        } else {
            expandableNotificationRow.setJustClicked(true);
            DejankUtils.postAfterTraversal(new Runnable() { // from class: com.android.systemui.statusbar.notification.NotificationClicker$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ExpandableNotificationRow.this.setJustClicked(false);
                }
            });
            if (!expandableNotificationRow.getEntry().isBubble() && this.mBubblesOptional.isPresent()) {
                this.mBubblesOptional.get().collapseStack();
            }
            this.mNotificationActivityStarter.onNotificationClicked(entry.getSbn(), expandableNotificationRow);
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onClick$0(View view, StatusBar statusBar) {
        statusBar.wakeUpIfDozing(SystemClock.uptimeMillis(), view, "NOTIFICATION_CLICK");
    }

    private boolean isMenuVisible(ExpandableNotificationRow expandableNotificationRow) {
        return expandableNotificationRow.getProvider() != null && expandableNotificationRow.getProvider().isMenuVisible();
    }

    public void register(ExpandableNotificationRow expandableNotificationRow, StatusBarNotification statusBarNotification) {
        Notification notification = statusBarNotification.getNotification();
        if (notification.contentIntent == null && notification.fullScreenIntent == null && !expandableNotificationRow.getEntry().isBubble()) {
            expandableNotificationRow.setOnClickListener(null);
        } else {
            expandableNotificationRow.setOnClickListener(this);
        }
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private final NotificationClickerLogger mLogger;

        public Builder(NotificationClickerLogger notificationClickerLogger) {
            this.mLogger = notificationClickerLogger;
        }

        public NotificationClicker build(Optional<StatusBar> optional, Optional<Bubbles> optional2, NotificationActivityStarter notificationActivityStarter) {
            return new NotificationClicker(this.mLogger, optional, optional2, notificationActivityStarter);
        }
    }
}
