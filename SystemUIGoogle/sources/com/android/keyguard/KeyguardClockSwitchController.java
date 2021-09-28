package com.android.keyguard;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.keyguard.clock.ClockManager;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.plugins.ClockPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.system.smartspace.SmartspaceTransitionController;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.NotificationIconContainer;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.ViewController;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
/* loaded from: classes.dex */
public class KeyguardClockSwitchController extends ViewController<KeyguardClockSwitch> {
    private final BatteryController mBatteryController;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final KeyguardBypassController mBypassController;
    private FrameLayout mClockFrame;
    private final ClockManager mClockManager;
    private AnimatableClockController mClockViewController;
    private final SysuiColorExtractor mColorExtractor;
    private final KeyguardSliceViewController mKeyguardSliceViewController;
    private final KeyguardUnlockAnimationController mKeyguardUnlockAnimationController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private FrameLayout mLargeClockFrame;
    private AnimatableClockController mLargeClockViewController;
    private final NotificationIconAreaController mNotificationIconAreaController;
    private final LockscreenSmartspaceController mSmartspaceController;
    private SmartspaceTransitionController mSmartspaceTransitionController;
    private View mSmartspaceView;
    private final StatusBarStateController mStatusBarStateController;
    private final ColorExtractor.OnColorsChangedListener mColorsListener = new ColorExtractor.OnColorsChangedListener() { // from class: com.android.keyguard.KeyguardClockSwitchController$$ExternalSyntheticLambda0
        public final void onColorsChanged(ColorExtractor colorExtractor, int i) {
            KeyguardClockSwitchController.this.lambda$new$0(colorExtractor, i);
        }
    };
    private final ClockManager.ClockChangedListener mClockChangedListener = new ClockManager.ClockChangedListener() { // from class: com.android.keyguard.KeyguardClockSwitchController$$ExternalSyntheticLambda1
        @Override // com.android.keyguard.clock.ClockManager.ClockChangedListener
        public final void onClockChanged(ClockPlugin clockPlugin) {
            KeyguardClockSwitchController.this.setClockPlugin(clockPlugin);
        }
    };
    private boolean mOnlyClock = false;

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ColorExtractor colorExtractor, int i) {
        if ((i & 2) != 0) {
            ((KeyguardClockSwitch) this.mView).updateColors(getGradientColors());
        }
    }

    public KeyguardClockSwitchController(KeyguardClockSwitch keyguardClockSwitch, StatusBarStateController statusBarStateController, SysuiColorExtractor sysuiColorExtractor, ClockManager clockManager, KeyguardSliceViewController keyguardSliceViewController, NotificationIconAreaController notificationIconAreaController, BroadcastDispatcher broadcastDispatcher, BatteryController batteryController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardBypassController keyguardBypassController, LockscreenSmartspaceController lockscreenSmartspaceController, KeyguardUnlockAnimationController keyguardUnlockAnimationController, SmartspaceTransitionController smartspaceTransitionController) {
        super(keyguardClockSwitch);
        this.mStatusBarStateController = statusBarStateController;
        this.mColorExtractor = sysuiColorExtractor;
        this.mClockManager = clockManager;
        this.mKeyguardSliceViewController = keyguardSliceViewController;
        this.mNotificationIconAreaController = notificationIconAreaController;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mBatteryController = batteryController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mBypassController = keyguardBypassController;
        this.mSmartspaceController = lockscreenSmartspaceController;
        this.mKeyguardUnlockAnimationController = keyguardUnlockAnimationController;
        this.mSmartspaceTransitionController = smartspaceTransitionController;
    }

    public void setOnlyClock(boolean z) {
        this.mOnlyClock = z;
    }

    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        this.mKeyguardSliceViewController.init();
        this.mClockFrame = (FrameLayout) ((KeyguardClockSwitch) this.mView).findViewById(R$id.lockscreen_clock_view);
        this.mLargeClockFrame = (FrameLayout) ((KeyguardClockSwitch) this.mView).findViewById(R$id.lockscreen_clock_view_large);
        AnimatableClockController animatableClockController = new AnimatableClockController((AnimatableClockView) ((KeyguardClockSwitch) this.mView).findViewById(R$id.animatable_clock_view), this.mStatusBarStateController, this.mBroadcastDispatcher, this.mBatteryController, this.mKeyguardUpdateMonitor, this.mBypassController);
        this.mClockViewController = animatableClockController;
        animatableClockController.init();
        AnimatableClockController animatableClockController2 = new AnimatableClockController((AnimatableClockView) ((KeyguardClockSwitch) this.mView).findViewById(R$id.animatable_clock_view_large), this.mStatusBarStateController, this.mBroadcastDispatcher, this.mBatteryController, this.mKeyguardUpdateMonitor, this.mBypassController);
        this.mLargeClockViewController = animatableClockController2;
        animatableClockController2.init();
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
        this.mClockManager.addOnClockChangedListener(this.mClockChangedListener);
        this.mColorExtractor.addOnColorsChangedListener(this.mColorsListener);
        ((KeyguardClockSwitch) this.mView).updateColors(getGradientColors());
        if (this.mOnlyClock) {
            ((KeyguardClockSwitch) this.mView).findViewById(R$id.keyguard_status_area).setVisibility(8);
            ((KeyguardClockSwitch) this.mView).findViewById(R$id.left_aligned_notification_icon_container).setVisibility(8);
            return;
        }
        updateAodIcons();
        if (this.mSmartspaceController.isEnabled()) {
            this.mSmartspaceView = this.mSmartspaceController.buildAndConnectView((ViewGroup) this.mView);
            View findViewById = ((KeyguardClockSwitch) this.mView).findViewById(R$id.keyguard_status_area);
            int indexOfChild = ((KeyguardClockSwitch) this.mView).indexOfChild(findViewById);
            findViewById.setVisibility(8);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
            layoutParams.addRule(3, R$id.lockscreen_clock_view);
            ((KeyguardClockSwitch) this.mView).addView(this.mSmartspaceView, indexOfChild, layoutParams);
            this.mSmartspaceView.setPaddingRelative(getContext().getResources().getDimensionPixelSize(R$dimen.below_clock_padding_start), 0, getContext().getResources().getDimensionPixelSize(R$dimen.below_clock_padding_end), 0);
            updateClockLayout();
            View findViewById2 = ((KeyguardClockSwitch) this.mView).findViewById(R$id.left_aligned_notification_icon_container);
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) findViewById2.getLayoutParams();
            layoutParams2.addRule(3, this.mSmartspaceView.getId());
            findViewById2.setLayoutParams(layoutParams2);
            ((KeyguardClockSwitch) this.mView).setSmartspaceView(this.mSmartspaceView);
            this.mSmartspaceTransitionController.setLockscreenSmartspace(this.mSmartspaceView);
        }
    }

    /* access modifiers changed from: package-private */
    public int getNotificationIconAreaHeight() {
        return this.mNotificationIconAreaController.getHeight();
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
        this.mClockManager.removeOnClockChangedListener(this.mClockChangedListener);
        this.mColorExtractor.removeOnColorsChangedListener(this.mColorsListener);
        ((KeyguardClockSwitch) this.mView).setClockPlugin(null, this.mStatusBarStateController.getState());
        this.mSmartspaceController.disconnect();
        View view = this.mSmartspaceView;
        if (view != null) {
            ((KeyguardClockSwitch) this.mView).removeView(view);
            this.mSmartspaceView = null;
        }
    }

    public void onDensityOrFontScaleChanged() {
        ((KeyguardClockSwitch) this.mView).onDensityOrFontScaleChanged();
        updateClockLayout();
    }

    private void updateClockLayout() {
        if (this.mSmartspaceController.isEnabled()) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.topMargin = getContext().getResources().getDimensionPixelSize(R$dimen.keyguard_large_clock_top_margin);
            this.mLargeClockFrame.setLayoutParams(layoutParams);
        }
    }

    public void setHasVisibleNotifications(boolean z) {
        if (((KeyguardClockSwitch) this.mView).willSwitchToLargeClock(z)) {
            this.mLargeClockViewController.animateAppear();
        }
    }

    public boolean hasCustomClock() {
        return ((KeyguardClockSwitch) this.mView).hasCustomClock();
    }

    public float getClockTextSize() {
        return ((KeyguardClockSwitch) this.mView).getTextSize();
    }

    /* access modifiers changed from: package-private */
    public void refresh() {
        AnimatableClockController animatableClockController = this.mClockViewController;
        if (animatableClockController != null) {
            animatableClockController.refreshTime();
            this.mLargeClockViewController.refreshTime();
        }
        LockscreenSmartspaceController lockscreenSmartspaceController = this.mSmartspaceController;
        if (lockscreenSmartspaceController != null) {
            lockscreenSmartspaceController.requestSmartspaceUpdate();
        }
        ((KeyguardClockSwitch) this.mView).refresh();
    }

    /* access modifiers changed from: package-private */
    public void updatePosition(int i, float f, AnimationProperties animationProperties, boolean z) {
        if (getCurrentLayoutDirection() == 1) {
            i = -i;
        }
        FrameLayout frameLayout = this.mClockFrame;
        AnimatableProperty animatableProperty = AnimatableProperty.TRANSLATION_X;
        float f2 = (float) i;
        PropertyAnimator.setProperty(frameLayout, animatableProperty, f2, animationProperties, z);
        PropertyAnimator.setProperty(this.mLargeClockFrame, AnimatableProperty.SCALE_X, f, animationProperties, z);
        PropertyAnimator.setProperty(this.mLargeClockFrame, AnimatableProperty.SCALE_Y, f, animationProperties, z);
        View view = this.mSmartspaceView;
        if (view != null) {
            PropertyAnimator.setProperty(view, animatableProperty, f2, animationProperties, z);
            if (this.mKeyguardUnlockAnimationController.isUnlockingWithSmartSpaceTransition()) {
                this.mKeyguardUnlockAnimationController.updateLockscreenSmartSpacePosition();
            }
        }
        this.mKeyguardSliceViewController.updatePosition(i, animationProperties, z);
        this.mNotificationIconAreaController.updatePosition(i, animationProperties, z);
    }

    public void setChildrenAlphaExcludingSmartspace(float f) {
        HashSet hashSet = new HashSet();
        View view = this.mSmartspaceView;
        if (view != null) {
            hashSet.add(view);
        }
        setChildrenAlphaExcluding(f, hashSet);
    }

    public void setChildrenAlphaExcluding(float f, Set<View> set) {
        for (int i = 0; i < ((KeyguardClockSwitch) this.mView).getChildCount(); i++) {
            View childAt = ((KeyguardClockSwitch) this.mView).getChildAt(i);
            if (!set.contains(childAt)) {
                childAt.setAlpha(f);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateTimeZone(TimeZone timeZone) {
        ((KeyguardClockSwitch) this.mView).onTimeZoneChanged(timeZone);
        AnimatableClockController animatableClockController = this.mClockViewController;
        if (animatableClockController != null) {
            animatableClockController.onTimeZoneChanged(timeZone);
            this.mLargeClockViewController.onTimeZoneChanged(timeZone);
        }
    }

    /* access modifiers changed from: package-private */
    public void refreshFormat() {
        AnimatableClockController animatableClockController = this.mClockViewController;
        if (animatableClockController != null) {
            animatableClockController.refreshFormat();
            this.mLargeClockViewController.refreshFormat();
        }
    }

    private void updateAodIcons() {
        this.mNotificationIconAreaController.setupAodIcons((NotificationIconContainer) ((KeyguardClockSwitch) this.mView).findViewById(R$id.left_aligned_notification_icon_container));
    }

    /* access modifiers changed from: private */
    public void setClockPlugin(ClockPlugin clockPlugin) {
        ((KeyguardClockSwitch) this.mView).setClockPlugin(clockPlugin, this.mStatusBarStateController.getState());
    }

    private ColorExtractor.GradientColors getGradientColors() {
        return this.mColorExtractor.getColors(2);
    }

    private int getCurrentLayoutDirection() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault());
    }
}
