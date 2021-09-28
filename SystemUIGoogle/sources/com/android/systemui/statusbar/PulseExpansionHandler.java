package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.PowerManager;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PulseExpansionHandler.kt */
/* loaded from: classes.dex */
public final class PulseExpansionHandler implements Gefingerpoken {
    public static final Companion Companion = new Companion(null);
    private static final int SPRING_BACK_ANIMATION_LENGTH_MS = 375;
    private boolean bouncerShowing;
    private final KeyguardBypassController bypassController;
    private final ConfigurationController configurationController;
    private final FalsingCollector falsingCollector;
    private final FalsingManager falsingManager;
    private final HeadsUpManagerPhone headsUpManager;
    private boolean isExpanding;
    private boolean isWakingToShadeLocked;
    private boolean leavingLockscreen;
    private final LockscreenShadeTransitionController lockscreenShadeTransitionController;
    private boolean mDraggedFarEnough;
    private float mInitialTouchX;
    private float mInitialTouchY;
    private final PowerManager mPowerManager;
    private boolean mPulsing;
    private ExpandableView mStartingChild;
    private final int[] mTemp2 = new int[2];
    private int minDragDistance;
    private Runnable pulseExpandAbortListener;
    private boolean qsExpanded;
    private final NotificationRoundnessManager roundnessManager;
    private NotificationStackScrollLayoutController stackScrollerController;
    private final StatusBarStateController statusBarStateController;
    private float touchSlop;
    private VelocityTracker velocityTracker;
    private final NotificationWakeUpCoordinator wakeUpCoordinator;

    public PulseExpansionHandler(final Context context, NotificationWakeUpCoordinator notificationWakeUpCoordinator, KeyguardBypassController keyguardBypassController, HeadsUpManagerPhone headsUpManagerPhone, NotificationRoundnessManager notificationRoundnessManager, ConfigurationController configurationController, StatusBarStateController statusBarStateController, FalsingManager falsingManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, FalsingCollector falsingCollector) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(notificationWakeUpCoordinator, "wakeUpCoordinator");
        Intrinsics.checkNotNullParameter(keyguardBypassController, "bypassController");
        Intrinsics.checkNotNullParameter(headsUpManagerPhone, "headsUpManager");
        Intrinsics.checkNotNullParameter(notificationRoundnessManager, "roundnessManager");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(lockscreenShadeTransitionController, "lockscreenShadeTransitionController");
        Intrinsics.checkNotNullParameter(falsingCollector, "falsingCollector");
        this.wakeUpCoordinator = notificationWakeUpCoordinator;
        this.bypassController = keyguardBypassController;
        this.headsUpManager = headsUpManagerPhone;
        this.roundnessManager = notificationRoundnessManager;
        this.configurationController = configurationController;
        this.statusBarStateController = statusBarStateController;
        this.falsingManager = falsingManager;
        this.lockscreenShadeTransitionController = lockscreenShadeTransitionController;
        this.falsingCollector = falsingCollector;
        initResources(context);
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) { // from class: com.android.systemui.statusbar.PulseExpansionHandler.1
            final /* synthetic */ PulseExpansionHandler this$0;

            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onConfigChanged(Configuration configuration) {
                this.this$0.initResources(context);
            }
        });
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
    }

    /* compiled from: PulseExpansionHandler.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final boolean isExpanding() {
        return this.isExpanding;
    }

    private final void setExpanding(boolean z) {
        boolean z2 = this.isExpanding != z;
        this.isExpanding = z;
        this.bypassController.setPulseExpanding(z);
        if (z2) {
            if (z) {
                NotificationEntry topEntry = this.headsUpManager.getTopEntry();
                if (topEntry != null) {
                    this.roundnessManager.setTrackingHeadsUp(topEntry.getRow());
                }
                this.lockscreenShadeTransitionController.onPulseExpansionStarted();
            } else {
                this.roundnessManager.setTrackingHeadsUp(null);
                if (!this.leavingLockscreen) {
                    this.bypassController.maybePerformPendingUnlock();
                    Runnable runnable = this.pulseExpandAbortListener;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            }
            this.headsUpManager.unpinAll(true);
        }
    }

    public final boolean isWakingToShadeLocked() {
        return this.isWakingToShadeLocked;
    }

    private final boolean isFalseTouch() {
        return this.falsingManager.isFalseTouch(2);
    }

    public final void setQsExpanded(boolean z) {
        this.qsExpanded = z;
    }

    public final void setPulseExpandAbortListener(Runnable runnable) {
        this.pulseExpandAbortListener = runnable;
    }

    public final void setBouncerShowing(boolean z) {
        this.bouncerShowing = z;
    }

    public final void initResources(Context context) {
        this.minDragDistance = context.getResources().getDimensionPixelSize(R$dimen.keyguard_drag_down_min_distance);
        this.touchSlop = (float) ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override // com.android.systemui.Gefingerpoken
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, "event");
        return canHandleMotionEvent() && startExpansion(motionEvent);
    }

    private final boolean canHandleMotionEvent() {
        return this.wakeUpCoordinator.getCanShowPulsingHuns() && !this.qsExpanded && !this.bouncerShowing;
    }

    private final boolean startExpansion(MotionEvent motionEvent) {
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        VelocityTracker velocityTracker = this.velocityTracker;
        Intrinsics.checkNotNull(velocityTracker);
        velocityTracker.addMovement(motionEvent);
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mDraggedFarEnough = false;
            setExpanding(false);
            this.leavingLockscreen = false;
            this.mStartingChild = null;
            this.mInitialTouchY = y;
            this.mInitialTouchX = x;
        } else if (actionMasked == 1) {
            recycleVelocityTracker();
            setExpanding(false);
        } else if (actionMasked == 2) {
            float f = y - this.mInitialTouchY;
            if (f > this.touchSlop && f > Math.abs(x - this.mInitialTouchX)) {
                this.falsingCollector.onStartExpandingFromPulse();
                setExpanding(true);
                captureStartingChild(this.mInitialTouchX, this.mInitialTouchY);
                this.mInitialTouchY = y;
                this.mInitialTouchX = x;
                return true;
            }
        } else if (actionMasked == 3) {
            recycleVelocityTracker();
            setExpanding(false);
        }
        return false;
    }

    private final void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
        }
        this.velocityTracker = null;
    }

    @Override // com.android.systemui.Gefingerpoken
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, "event");
        boolean z = false;
        boolean z2 = (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) && this.isExpanding;
        if (!canHandleMotionEvent() && !z2) {
            return false;
        }
        if (this.velocityTracker == null || !this.isExpanding || motionEvent.getActionMasked() == 0) {
            return startExpansion(motionEvent);
        }
        VelocityTracker velocityTracker = this.velocityTracker;
        Intrinsics.checkNotNull(velocityTracker);
        velocityTracker.addMovement(motionEvent);
        float y = motionEvent.getY() - this.mInitialTouchY;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1) {
            VelocityTracker velocityTracker2 = this.velocityTracker;
            Intrinsics.checkNotNull(velocityTracker2);
            velocityTracker2.computeCurrentVelocity(1000);
            if (y > 0.0f) {
                VelocityTracker velocityTracker3 = this.velocityTracker;
                Intrinsics.checkNotNull(velocityTracker3);
                if (velocityTracker3.getYVelocity() > -1000.0f && this.statusBarStateController.getState() != 0) {
                    z = true;
                }
            }
            if (this.falsingManager.isUnlockingDisabled() || isFalseTouch() || !z) {
                cancelExpansion();
            } else {
                finishExpansion();
            }
            recycleVelocityTracker();
        } else if (actionMasked == 2) {
            updateExpansionHeight(y);
        } else if (actionMasked == 3) {
            cancelExpansion();
            recycleVelocityTracker();
        }
        return this.isExpanding;
    }

    private final void finishExpansion() {
        ExpandableView expandableView = this.mStartingChild;
        if (expandableView != null) {
            Intrinsics.checkNotNull(expandableView);
            setUserLocked(expandableView, false);
            this.mStartingChild = null;
        }
        if (this.statusBarStateController.isDozing()) {
            this.isWakingToShadeLocked = true;
            this.wakeUpCoordinator.setWillWakeUp(true);
            PowerManager powerManager = this.mPowerManager;
            Intrinsics.checkNotNull(powerManager);
            powerManager.wakeUp(SystemClock.uptimeMillis(), 4, "com.android.systemui:PULSEDRAG");
        }
        this.lockscreenShadeTransitionController.goToLockedShade(expandableView, false);
        this.lockscreenShadeTransitionController.finishPulseAnimation(false);
        this.leavingLockscreen = true;
        setExpanding(false);
        ExpandableView expandableView2 = this.mStartingChild;
        if (expandableView2 instanceof ExpandableNotificationRow) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView2;
            Intrinsics.checkNotNull(expandableNotificationRow);
            expandableNotificationRow.onExpandedByGesture(true);
        }
    }

    private final void updateExpansionHeight(float f) {
        float max = Math.max(f, 0.0f);
        ExpandableView expandableView = this.mStartingChild;
        if (expandableView != null) {
            Intrinsics.checkNotNull(expandableView);
            expandableView.setActualHeight(Math.min((int) (((float) expandableView.getCollapsedHeight()) + max), expandableView.getMaxContentHeight()));
        } else {
            this.wakeUpCoordinator.setNotificationsVisibleForExpansion(f > ((float) this.lockscreenShadeTransitionController.getDistanceUntilShowingPulsingNotifications()), true, true);
        }
        this.lockscreenShadeTransitionController.setPulseHeight(max, false);
    }

    private final void captureStartingChild(float f, float f2) {
        if (this.mStartingChild == null && !this.bypassController.getBypassEnabled()) {
            ExpandableView findView = findView(f, f2);
            this.mStartingChild = findView;
            if (findView != null) {
                Intrinsics.checkNotNull(findView);
                setUserLocked(findView, true);
            }
        }
    }

    private final void reset(ExpandableView expandableView) {
        if (expandableView.getActualHeight() == expandableView.getCollapsedHeight()) {
            setUserLocked(expandableView, false);
            return;
        }
        ObjectAnimator ofInt = ObjectAnimator.ofInt(expandableView, "actualHeight", expandableView.getActualHeight(), expandableView.getCollapsedHeight());
        ofInt.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofInt.setDuration((long) SPRING_BACK_ANIMATION_LENGTH_MS);
        ofInt.addListener(new AnimatorListenerAdapter(this, expandableView) { // from class: com.android.systemui.statusbar.PulseExpansionHandler$reset$1
            final /* synthetic */ ExpandableView $child;
            final /* synthetic */ PulseExpansionHandler this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$child = r2;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                Intrinsics.checkNotNullParameter(animator, "animation");
                PulseExpansionHandler.access$setUserLocked(this.this$0, this.$child, false);
            }
        });
        ofInt.start();
    }

    public final void setUserLocked(ExpandableView expandableView, boolean z) {
        if (expandableView instanceof ExpandableNotificationRow) {
            ((ExpandableNotificationRow) expandableView).setUserLocked(z);
        }
    }

    private final void cancelExpansion() {
        setExpanding(false);
        this.falsingCollector.onExpansionFromPulseStopped();
        ExpandableView expandableView = this.mStartingChild;
        if (expandableView != null) {
            Intrinsics.checkNotNull(expandableView);
            reset(expandableView);
            this.mStartingChild = null;
        }
        this.lockscreenShadeTransitionController.finishPulseAnimation(true);
        this.wakeUpCoordinator.setNotificationsVisibleForExpansion(false, true, false);
    }

    private final ExpandableView findView(float f, float f2) {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.stackScrollerController;
        if (notificationStackScrollLayoutController != null) {
            notificationStackScrollLayoutController.getLocationOnScreen(this.mTemp2);
            int[] iArr = this.mTemp2;
            float f3 = f + ((float) iArr[0]);
            float f4 = f2 + ((float) iArr[1]);
            NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.stackScrollerController;
            if (notificationStackScrollLayoutController2 != null) {
                ExpandableView childAtRawPosition = notificationStackScrollLayoutController2.getChildAtRawPosition(f3, f4);
                if (childAtRawPosition == null || !childAtRawPosition.isContentExpandable()) {
                    return null;
                }
                return childAtRawPosition;
            }
            Intrinsics.throwUninitializedPropertyAccessException("stackScrollerController");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("stackScrollerController");
        throw null;
    }

    public final void setUp(NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        Intrinsics.checkNotNullParameter(notificationStackScrollLayoutController, "stackScrollerController");
        this.stackScrollerController = notificationStackScrollLayoutController;
    }

    public final void setPulsing(boolean z) {
        this.mPulsing = z;
    }

    public final void onStartedWakingUp() {
        this.isWakingToShadeLocked = false;
    }
}
