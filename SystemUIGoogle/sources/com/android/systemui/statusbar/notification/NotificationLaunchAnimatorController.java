package com.android.systemui.statusbar.notification;

import android.view.View;
import android.view.ViewGroup;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import com.android.systemui.statusbar.policy.HeadsUpUtil;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationLaunchAnimatorController.kt */
/* loaded from: classes.dex */
public final class NotificationLaunchAnimatorController implements ActivityLaunchAnimator.Controller {
    public static final Companion Companion = new Companion(null);
    private final HeadsUpManagerPhone headsUpManager;
    private final ExpandableNotificationRow notification;
    private final NotificationEntry notificationEntry;
    private final String notificationKey;
    private final NotificationListContainer notificationListContainer;
    private final NotificationShadeWindowViewController notificationShadeWindowViewController;

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void setLaunchContainer(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "ignored");
    }

    public NotificationLaunchAnimatorController(NotificationShadeWindowViewController notificationShadeWindowViewController, NotificationListContainer notificationListContainer, HeadsUpManagerPhone headsUpManagerPhone, ExpandableNotificationRow expandableNotificationRow) {
        Intrinsics.checkNotNullParameter(notificationShadeWindowViewController, "notificationShadeWindowViewController");
        Intrinsics.checkNotNullParameter(notificationListContainer, "notificationListContainer");
        Intrinsics.checkNotNullParameter(headsUpManagerPhone, "headsUpManager");
        Intrinsics.checkNotNullParameter(expandableNotificationRow, "notification");
        this.notificationShadeWindowViewController = notificationShadeWindowViewController;
        this.notificationListContainer = notificationListContainer;
        this.headsUpManager = headsUpManagerPhone;
        this.notification = expandableNotificationRow;
        NotificationEntry entry = expandableNotificationRow.getEntry();
        Intrinsics.checkNotNullExpressionValue(entry, "notification.entry");
        this.notificationEntry = entry;
        this.notificationKey = entry.getSbn().getKey();
    }

    /* compiled from: NotificationLaunchAnimatorController.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public ViewGroup getLaunchContainer() {
        View rootView = this.notification.getRootView();
        Objects.requireNonNull(rootView, "null cannot be cast to non-null type android.view.ViewGroup");
        return (ViewGroup) rootView;
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public ActivityLaunchAnimator.State createAnimatorState() {
        float f;
        int max = Math.max(0, this.notification.getActualHeight() - this.notification.getClipBottomAmount());
        int[] locationOnScreen = this.notification.getLocationOnScreen();
        int topClippingStartLocation = this.notificationListContainer.getTopClippingStartLocation();
        int max2 = Math.max(topClippingStartLocation - locationOnScreen[1], 0);
        int i = locationOnScreen[1] + max2;
        if (max2 > 0) {
            f = 0.0f;
        } else {
            f = this.notification.getCurrentBackgroundRadiusTop();
        }
        ExpandAnimationParameters expandAnimationParameters = new ExpandAnimationParameters(i, locationOnScreen[1] + max, locationOnScreen[0], locationOnScreen[0] + this.notification.getWidth(), f, this.notification.getCurrentBackgroundRadiusBottom());
        expandAnimationParameters.setStartTranslationZ(this.notification.getTranslationZ());
        expandAnimationParameters.setStartNotificationTop(this.notification.getTranslationY());
        expandAnimationParameters.setStartRoundedTopClipping(max2);
        expandAnimationParameters.setStartClipTopAmount(this.notification.getClipTopAmount());
        if (this.notification.isChildInGroup()) {
            expandAnimationParameters.setStartNotificationTop(expandAnimationParameters.getStartNotificationTop() + this.notification.getNotificationParent().getTranslationY());
            expandAnimationParameters.setParentStartRoundedTopClipping(Math.max(topClippingStartLocation - this.notification.getNotificationParent().getLocationOnScreen()[1], 0));
            int clipTopAmount = this.notification.getNotificationParent().getClipTopAmount();
            expandAnimationParameters.setParentStartClipTopAmount(clipTopAmount);
            if (clipTopAmount != 0) {
                float translationY = ((float) clipTopAmount) - this.notification.getTranslationY();
                if (translationY > 0.0f) {
                    expandAnimationParameters.setStartClipTopAmount((int) Math.ceil((double) translationY));
                }
            }
        }
        return expandAnimationParameters;
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onIntentStarted(boolean z) {
        this.notificationShadeWindowViewController.setExpandAnimationRunning(z);
        this.notificationEntry.setExpandAnimationRunning(z);
        if (!z) {
            removeHun(true);
        }
    }

    private final void removeHun(boolean z) {
        if (this.headsUpManager.isAlerting(this.notificationKey)) {
            HeadsUpUtil.setNeedsHeadsUpDisappearAnimationAfterClick(this.notification, z);
            this.headsUpManager.removeNotification(this.notificationKey, true, z);
        }
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationCancelled() {
        this.notificationShadeWindowViewController.setExpandAnimationRunning(false);
        this.notificationEntry.setExpandAnimationRunning(false);
        removeHun(true);
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationStart(boolean z) {
        this.notification.setExpandAnimationRunning(true);
        this.notificationListContainer.setExpandingNotification(this.notification);
        InteractionJankMonitor.getInstance().begin(this.notification, 16);
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationEnd(boolean z) {
        InteractionJankMonitor.getInstance().end(16);
        this.notification.setExpandAnimationRunning(false);
        this.notificationShadeWindowViewController.setExpandAnimationRunning(false);
        this.notificationEntry.setExpandAnimationRunning(false);
        this.notificationListContainer.setExpandingNotification(null);
        applyParams(null);
        removeHun(false);
    }

    private final void applyParams(ExpandAnimationParameters expandAnimationParameters) {
        this.notification.applyExpandAnimationParams(expandAnimationParameters);
        this.notificationListContainer.applyExpandAnimationParams(expandAnimationParameters);
    }

    @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
    public void onLaunchAnimationProgress(ActivityLaunchAnimator.State state, float f, float f2) {
        Intrinsics.checkNotNullParameter(state, "state");
        ExpandAnimationParameters expandAnimationParameters = (ExpandAnimationParameters) state;
        expandAnimationParameters.setProgress(f);
        expandAnimationParameters.setLinearProgress(f2);
        applyParams(expandAnimationParameters);
    }
}
