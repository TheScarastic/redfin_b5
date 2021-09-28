package com.android.systemui.statusbar.notification;

import android.animation.ObjectAnimator;
import android.view.animation.Interpolator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.PanelExpansionListener;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationWakeUpCoordinator.kt */
/* loaded from: classes.dex */
public final class NotificationWakeUpCoordinator implements OnHeadsUpChangedListener, StatusBarStateController.StateListener, PanelExpansionListener {
    private final KeyguardBypassController bypassController;
    private boolean collapsedEnoughToHide;
    private final DozeParameters dozeParameters;
    private boolean fullyAwake;
    private float mDozeAmount;
    private final HeadsUpManager mHeadsUpManager;
    private float mLinearDozeAmount;
    private float mLinearVisibilityAmount;
    private float mNotificationVisibleAmount;
    private boolean mNotificationsVisible;
    private boolean mNotificationsVisibleForExpansion;
    private NotificationStackScrollLayoutController mStackScrollerController;
    private float mVisibilityAmount;
    private ObjectAnimator mVisibilityAnimator;
    private boolean notificationsFullyHidden;
    private boolean pulseExpanding;
    private boolean pulsing;
    private final StatusBarStateController statusBarStateController;
    private final UnlockedScreenOffAnimationController unlockedScreenOffAnimationController;
    private boolean wakingUp;
    private boolean willWakeUp;
    private final NotificationWakeUpCoordinator$mNotificationVisibility$1 mNotificationVisibility = new NotificationWakeUpCoordinator$mNotificationVisibility$1();
    private Interpolator mVisibilityInterpolator = Interpolators.FAST_OUT_SLOW_IN_REVERSE;
    private final Set<NotificationEntry> mEntrySetToClearWhenFinished = new LinkedHashSet();
    private final ArrayList<WakeUpListener> wakeUpListeners = new ArrayList<>();
    private int state = 1;

    /* compiled from: NotificationWakeUpCoordinator.kt */
    /* loaded from: classes.dex */
    public interface WakeUpListener {
        default void onFullyHiddenChanged(boolean z) {
        }

        default void onPulseExpansionChanged(boolean z) {
        }
    }

    public NotificationWakeUpCoordinator(HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController, KeyguardBypassController keyguardBypassController, DozeParameters dozeParameters, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        Intrinsics.checkNotNullParameter(headsUpManager, "mHeadsUpManager");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(keyguardBypassController, "bypassController");
        Intrinsics.checkNotNullParameter(dozeParameters, "dozeParameters");
        Intrinsics.checkNotNullParameter(unlockedScreenOffAnimationController, "unlockedScreenOffAnimationController");
        this.mHeadsUpManager = headsUpManager;
        this.statusBarStateController = statusBarStateController;
        this.bypassController = keyguardBypassController;
        this.dozeParameters = dozeParameters;
        this.unlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
        headsUpManager.addListener(this);
        statusBarStateController.addCallback(this);
        addListener(new WakeUpListener(this) { // from class: com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.1
            final /* synthetic */ NotificationWakeUpCoordinator this$0;

            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.WakeUpListener
            public void onFullyHiddenChanged(boolean z) {
                if (z && this.this$0.mNotificationsVisibleForExpansion) {
                    this.this$0.setNotificationsVisibleForExpansion(false, false, false);
                }
            }
        });
    }

    public final void setFullyAwake(boolean z) {
        this.fullyAwake = z;
    }

    public final void setWakingUp(boolean z) {
        this.wakingUp = z;
        setWillWakeUp(false);
        if (z) {
            if (this.mNotificationsVisible && !this.mNotificationsVisibleForExpansion && !this.bypassController.getBypassEnabled()) {
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
                if (notificationStackScrollLayoutController != null) {
                    notificationStackScrollLayoutController.wakeUpFromPulse();
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("mStackScrollerController");
                    throw null;
                }
            }
            if (this.bypassController.getBypassEnabled() && !this.mNotificationsVisible) {
                updateNotificationVisibility(shouldAnimateVisibility(), false);
            }
        }
    }

    public final void setWillWakeUp(boolean z) {
        if (z) {
            if (this.mDozeAmount == 0.0f) {
                return;
            }
        }
        this.willWakeUp = z;
    }

    public final void setPulsing(boolean z) {
        this.pulsing = z;
        if (z) {
            updateNotificationVisibility(shouldAnimateVisibility(), false);
        }
    }

    public final boolean getNotificationsFullyHidden() {
        return this.notificationsFullyHidden;
    }

    private final void setNotificationsFullyHidden(boolean z) {
        if (this.notificationsFullyHidden != z) {
            this.notificationsFullyHidden = z;
            Iterator<WakeUpListener> it = this.wakeUpListeners.iterator();
            while (it.hasNext()) {
                it.next().onFullyHiddenChanged(z);
            }
        }
    }

    public final boolean getCanShowPulsingHuns() {
        boolean z = this.pulsing;
        if (!this.bypassController.getBypassEnabled()) {
            return z;
        }
        boolean z2 = z || ((this.wakingUp || this.willWakeUp || this.fullyAwake) && this.statusBarStateController.getState() == 1);
        if (this.collapsedEnoughToHide) {
            return false;
        }
        return z2;
    }

    public final void setStackScroller(NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        Intrinsics.checkNotNullParameter(notificationStackScrollLayoutController, "stackScrollerController");
        this.mStackScrollerController = notificationStackScrollLayoutController;
        this.pulseExpanding = notificationStackScrollLayoutController.isPulseExpanding();
        notificationStackScrollLayoutController.setOnPulseHeightChangedListener(new Runnable(this) { // from class: com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator$setStackScroller$1
            final /* synthetic */ NotificationWakeUpCoordinator this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // java.lang.Runnable
            public final void run() {
                boolean isPulseExpanding = this.this$0.isPulseExpanding();
                boolean z = isPulseExpanding != this.this$0.pulseExpanding;
                this.this$0.pulseExpanding = isPulseExpanding;
                Iterator it = this.this$0.wakeUpListeners.iterator();
                while (it.hasNext()) {
                    ((NotificationWakeUpCoordinator.WakeUpListener) it.next()).onPulseExpansionChanged(z);
                }
            }
        });
    }

    public final boolean isPulseExpanding() {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
        if (notificationStackScrollLayoutController != null) {
            return notificationStackScrollLayoutController.isPulseExpanding();
        }
        Intrinsics.throwUninitializedPropertyAccessException("mStackScrollerController");
        throw null;
    }

    public final void setNotificationsVisibleForExpansion(boolean z, boolean z2, boolean z3) {
        this.mNotificationsVisibleForExpansion = z;
        updateNotificationVisibility(z2, z3);
        if (!z && this.mNotificationsVisible) {
            this.mHeadsUpManager.releaseAllImmediately();
        }
    }

    public final void addListener(WakeUpListener wakeUpListener) {
        Intrinsics.checkNotNullParameter(wakeUpListener, "listener");
        this.wakeUpListeners.add(wakeUpListener);
    }

    public final void removeListener(WakeUpListener wakeUpListener) {
        Intrinsics.checkNotNullParameter(wakeUpListener, "listener");
        this.wakeUpListeners.remove(wakeUpListener);
    }

    private final void updateNotificationVisibility(boolean z, boolean z2) {
        boolean z3 = false;
        boolean z4 = (this.mNotificationsVisibleForExpansion || this.mHeadsUpManager.hasNotifications()) && getCanShowPulsingHuns();
        if (!z4 && this.mNotificationsVisible && (this.wakingUp || this.willWakeUp)) {
            if (this.mDozeAmount == 0.0f) {
                z3 = true;
            }
            if (!z3) {
                return;
            }
        }
        setNotificationsVisible(z4, z, z2);
    }

    private final void setNotificationsVisible(boolean z, boolean z2, boolean z3) {
        if (this.mNotificationsVisible != z) {
            this.mNotificationsVisible = z;
            ObjectAnimator objectAnimator = this.mVisibilityAnimator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            if (z2) {
                notifyAnimationStart(z);
                startVisibilityAnimation(z3);
                return;
            }
            setVisibilityAmount(z ? 1.0f : 0.0f);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0037, code lost:
        if ((r4 == 1.0f) != false) goto L_0x0039;
     */
    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDozeAmountChanged(float r6, float r7) {
        /*
            r5 = this;
            boolean r0 = r5.overrideDozeAmountIfAnimatingScreenOff(r6)
            if (r0 == 0) goto L_0x0007
            return
        L_0x0007:
            boolean r0 = r5.overrideDozeAmountIfBypass()
            if (r0 == 0) goto L_0x000e
            return
        L_0x000e:
            r0 = 1065353216(0x3f800000, float:1.0)
            int r1 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x0018
            r1 = r2
            goto L_0x0019
        L_0x0018:
            r1 = r3
        L_0x0019:
            if (r1 != 0) goto L_0x0042
            r1 = 0
            int r4 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1))
            if (r4 != 0) goto L_0x0022
            r4 = r2
            goto L_0x0023
        L_0x0022:
            r4 = r3
        L_0x0023:
            if (r4 != 0) goto L_0x0042
            float r4 = r5.mLinearDozeAmount
            int r1 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1))
            if (r1 != 0) goto L_0x002d
            r1 = r2
            goto L_0x002e
        L_0x002d:
            r1 = r3
        L_0x002e:
            if (r1 != 0) goto L_0x0039
            int r1 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r1 != 0) goto L_0x0036
            r1 = r2
            goto L_0x0037
        L_0x0036:
            r1 = r3
        L_0x0037:
            if (r1 == 0) goto L_0x0042
        L_0x0039:
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r0 != 0) goto L_0x003e
            goto L_0x003f
        L_0x003e:
            r2 = r3
        L_0x003f:
            r5.notifyAnimationStart(r2)
        L_0x0042:
            r5.setDozeAmount(r6, r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.onDozeAmountChanged(float, float):void");
    }

    public final void setDozeAmount(float f, float f2) {
        boolean z = true;
        boolean z2 = !(f == this.mLinearDozeAmount);
        this.mLinearDozeAmount = f;
        this.mDozeAmount = f2;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
        if (notificationStackScrollLayoutController != null) {
            notificationStackScrollLayoutController.setDozeAmount(f2);
            updateHideAmount();
            if (z2) {
                if (f != 0.0f) {
                    z = false;
                }
                if (z) {
                    setNotificationsVisible(false, false, false);
                    setNotificationsVisibleForExpansion(false, false, false);
                    return;
                }
                return;
            }
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mStackScrollerController");
        throw null;
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onStateChanged(int i) {
        if (this.dozeParameters.shouldControlUnlockedScreenOff() && this.unlockedScreenOffAnimationController.isScreenOffAnimationPlaying() && this.state == 1 && i == 0) {
            setDozeAmount(0.0f, 0.0f);
        }
        if (!overrideDozeAmountIfAnimatingScreenOff(this.mLinearDozeAmount) && !overrideDozeAmountIfBypass()) {
            if (this.bypassController.getBypassEnabled() && i == 1 && this.state == 2 && (!this.statusBarStateController.isDozing() || shouldAnimateVisibility())) {
                setNotificationsVisible(true, false, false);
                setNotificationsVisible(false, true, false);
            }
            this.state = i;
        }
    }

    @Override // com.android.systemui.statusbar.phone.PanelExpansionListener
    public void onPanelExpansionChanged(float f, boolean z) {
        boolean z2 = f <= 0.9f;
        if (z2 != this.collapsedEnoughToHide) {
            boolean canShowPulsingHuns = getCanShowPulsingHuns();
            this.collapsedEnoughToHide = z2;
            if (canShowPulsingHuns && !getCanShowPulsingHuns()) {
                updateNotificationVisibility(true, true);
                this.mHeadsUpManager.releaseAllImmediately();
            }
        }
    }

    private final boolean overrideDozeAmountIfBypass() {
        if (!this.bypassController.getBypassEnabled()) {
            return false;
        }
        float f = 1.0f;
        if (this.statusBarStateController.getState() == 0 || this.statusBarStateController.getState() == 2) {
            f = 0.0f;
        }
        setDozeAmount(f, f);
        return true;
    }

    private final boolean overrideDozeAmountIfAnimatingScreenOff(float f) {
        if (!this.unlockedScreenOffAnimationController.isScreenOffAnimationPlaying()) {
            return false;
        }
        setDozeAmount(1.0f, 1.0f);
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0017, code lost:
        if ((r0 == 1.0f) != false) goto L_0x0019;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void startVisibilityAnimation(boolean r7) {
        /*
            r6 = this;
            float r0 = r6.mNotificationVisibleAmount
            r1 = 0
            int r2 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            r3 = 1
            r4 = 0
            if (r2 != 0) goto L_0x000b
            r2 = r3
            goto L_0x000c
        L_0x000b:
            r2 = r4
        L_0x000c:
            r5 = 1065353216(0x3f800000, float:1.0)
            if (r2 != 0) goto L_0x0019
            int r0 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r0 != 0) goto L_0x0016
            r0 = r3
            goto L_0x0017
        L_0x0016:
            r0 = r4
        L_0x0017:
            if (r0 == 0) goto L_0x0024
        L_0x0019:
            boolean r0 = r6.mNotificationsVisible
            if (r0 == 0) goto L_0x0020
            android.view.animation.Interpolator r0 = com.android.systemui.animation.Interpolators.TOUCH_RESPONSE
            goto L_0x0022
        L_0x0020:
            android.view.animation.Interpolator r0 = com.android.systemui.animation.Interpolators.FAST_OUT_SLOW_IN_REVERSE
        L_0x0022:
            r6.mVisibilityInterpolator = r0
        L_0x0024:
            boolean r0 = r6.mNotificationsVisible
            if (r0 == 0) goto L_0x0029
            r1 = r5
        L_0x0029:
            com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator$mNotificationVisibility$1 r0 = r6.mNotificationVisibility
            float[] r2 = new float[r3]
            r2[r4] = r1
            android.animation.ObjectAnimator r0 = android.animation.ObjectAnimator.ofFloat(r6, r0, r2)
            android.view.animation.Interpolator r1 = com.android.systemui.animation.Interpolators.LINEAR
            r0.setInterpolator(r1)
            r1 = 500(0x1f4, double:2.47E-321)
            if (r7 == 0) goto L_0x0041
            float r7 = (float) r1
            r1 = 1069547520(0x3fc00000, float:1.5)
            float r7 = r7 / r1
            long r1 = (long) r7
        L_0x0041:
            r0.setDuration(r1)
            r0.start()
            r6.mVisibilityAnimator = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.startVisibilityAnimation(boolean):void");
    }

    /* access modifiers changed from: private */
    public final void setVisibilityAmount(float f) {
        this.mLinearVisibilityAmount = f;
        this.mVisibilityAmount = this.mVisibilityInterpolator.getInterpolation(f);
        handleAnimationFinished();
        updateHideAmount();
    }

    private final void handleAnimationFinished() {
        boolean z = true;
        if (!(this.mLinearDozeAmount == 0.0f)) {
            if (this.mLinearVisibilityAmount != 0.0f) {
                z = false;
            }
            if (!z) {
                return;
            }
        }
        for (NotificationEntry notificationEntry : this.mEntrySetToClearWhenFinished) {
            notificationEntry.setHeadsUpAnimatingAway(false);
        }
        this.mEntrySetToClearWhenFinished.clear();
    }

    private final void updateHideAmount() {
        float min = Math.min(1.0f - this.mLinearVisibilityAmount, this.mLinearDozeAmount);
        float min2 = Math.min(1.0f - this.mVisibilityAmount, this.mDozeAmount);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
        if (notificationStackScrollLayoutController != null) {
            notificationStackScrollLayoutController.setHideAmount(min, min2);
            setNotificationsFullyHidden(min == 1.0f);
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mStackScrollerController");
        throw null;
    }

    private final void notifyAnimationStart(boolean z) {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
        if (notificationStackScrollLayoutController != null) {
            notificationStackScrollLayoutController.notifyHideAnimationStart(!z);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("mStackScrollerController");
            throw null;
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onDozingChanged(boolean z) {
        if (z) {
            setNotificationsVisible(false, false, false);
        }
    }

    @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
    public void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        boolean shouldAnimateVisibility = shouldAnimateVisibility();
        if (!z) {
            if (!(this.mLinearDozeAmount == 0.0f)) {
                if (!(this.mLinearVisibilityAmount == 0.0f)) {
                    if (notificationEntry.isRowDismissed()) {
                        shouldAnimateVisibility = false;
                    } else if (!this.wakingUp && !this.willWakeUp) {
                        notificationEntry.setHeadsUpAnimatingAway(true);
                        this.mEntrySetToClearWhenFinished.add(notificationEntry);
                    }
                }
            }
        } else if (this.mEntrySetToClearWhenFinished.contains(notificationEntry)) {
            this.mEntrySetToClearWhenFinished.remove(notificationEntry);
            notificationEntry.setHeadsUpAnimatingAway(false);
        }
        updateNotificationVisibility(shouldAnimateVisibility, false);
    }

    private final boolean shouldAnimateVisibility() {
        return this.dozeParameters.getAlwaysOn() && !this.dozeParameters.getDisplayNeedsBlanking();
    }
}
