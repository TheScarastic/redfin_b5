package com.android.keyguard;

import android.graphics.Rect;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.shared.system.smartspace.SmartspaceTransitionController;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import java.util.TimeZone;
/* loaded from: classes.dex */
public class KeyguardStatusViewController extends ViewController<KeyguardStatusView> {
    private static final AnimationProperties CLOCK_ANIMATION_PROPERTIES = new AnimationProperties().setDuration(360);
    private final ConfigurationController mConfigurationController;
    private final DozeParameters mDozeParameters;
    private final KeyguardClockSwitchController mKeyguardClockSwitchController;
    private final KeyguardSliceViewController mKeyguardSliceViewController;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUnlockAnimationController mKeyguardUnlockAnimationController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final KeyguardVisibilityHelper mKeyguardVisibilityHelper;
    private SmartspaceTransitionController mSmartspaceTransitionController;
    private final Rect mClipBounds = new Rect();
    private final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.keyguard.KeyguardStatusViewController.1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onLocaleListChanged() {
            KeyguardStatusViewController.this.refreshTime();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onDensityOrFontScaleChanged() {
            KeyguardStatusViewController.this.mKeyguardClockSwitchController.onDensityOrFontScaleChanged();
        }
    };
    private KeyguardUpdateMonitorCallback mInfoCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.KeyguardStatusViewController.2
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onLockScreenModeChanged(int i) {
            KeyguardStatusViewController.this.mKeyguardSliceViewController.updateLockScreenMode(i);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onTimeChanged() {
            KeyguardStatusViewController.this.refreshTime();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onTimeFormatChanged(String str) {
            KeyguardStatusViewController.this.mKeyguardClockSwitchController.refreshFormat();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onTimeZoneChanged(TimeZone timeZone) {
            KeyguardStatusViewController.this.mKeyguardClockSwitchController.updateTimeZone(timeZone);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onKeyguardVisibilityChanged(boolean z) {
            if (z) {
                KeyguardStatusViewController.this.refreshTime();
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserSwitchComplete(int i) {
            KeyguardStatusViewController.this.mKeyguardClockSwitchController.refreshFormat();
        }
    };
    private KeyguardStateController.Callback mKeyguardStateControllerCallback = new KeyguardStateController.Callback() { // from class: com.android.keyguard.KeyguardStatusViewController.3
        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public void onKeyguardShowingChanged() {
            if (KeyguardStatusViewController.this.mKeyguardStateController.isShowing()) {
                ((KeyguardStatusView) ((ViewController) KeyguardStatusViewController.this).mView).setChildrenAlphaExcludingClockView(1.0f);
            }
        }
    };

    public KeyguardStatusViewController(KeyguardStatusView keyguardStatusView, KeyguardSliceViewController keyguardSliceViewController, KeyguardClockSwitchController keyguardClockSwitchController, KeyguardStateController keyguardStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, ConfigurationController configurationController, DozeParameters dozeParameters, KeyguardUnlockAnimationController keyguardUnlockAnimationController, SmartspaceTransitionController smartspaceTransitionController, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        super(keyguardStatusView);
        this.mKeyguardSliceViewController = keyguardSliceViewController;
        this.mKeyguardClockSwitchController = keyguardClockSwitchController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mConfigurationController = configurationController;
        this.mDozeParameters = dozeParameters;
        this.mKeyguardStateController = keyguardStateController;
        this.mKeyguardVisibilityHelper = new KeyguardVisibilityHelper(this.mView, keyguardStateController, dozeParameters, unlockedScreenOffAnimationController, true);
        this.mKeyguardUnlockAnimationController = keyguardUnlockAnimationController;
        this.mSmartspaceTransitionController = smartspaceTransitionController;
    }

    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        this.mKeyguardClockSwitchController.init();
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
        this.mKeyguardUpdateMonitor.registerCallback(this.mInfoCallback);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mKeyguardStateController.addCallback(this.mKeyguardStateControllerCallback);
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
        this.mKeyguardUpdateMonitor.removeCallback(this.mInfoCallback);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mKeyguardStateController.removeCallback(this.mKeyguardStateControllerCallback);
    }

    public void dozeTimeTick() {
        refreshTime();
        this.mKeyguardSliceViewController.refresh();
    }

    public void setDarkAmount(float f) {
        ((KeyguardStatusView) this.mView).setDarkAmount(f);
    }

    public void setHasVisibleNotifications(boolean z) {
        this.mKeyguardClockSwitchController.setHasVisibleNotifications(z);
    }

    public boolean hasCustomClock() {
        return this.mKeyguardClockSwitchController.hasCustomClock();
    }

    public void setAlpha(float f) {
        if (this.mKeyguardVisibilityHelper.isVisibilityAnimating()) {
            return;
        }
        if (this.mKeyguardUnlockAnimationController.isUnlockingWithSmartSpaceTransition()) {
            ((KeyguardStatusView) this.mView).setChildrenAlphaExcludingClockView(f);
            this.mKeyguardClockSwitchController.setChildrenAlphaExcludingSmartspace(f);
        } else if (!this.mKeyguardVisibilityHelper.isVisibilityAnimating()) {
            ((KeyguardStatusView) this.mView).setAlpha(f);
            if (((KeyguardStatusView) this.mView).getChildrenAlphaExcludingSmartSpace() < 1.0f) {
                ((KeyguardStatusView) this.mView).setChildrenAlphaExcludingClockView(1.0f);
                this.mKeyguardClockSwitchController.setChildrenAlphaExcludingSmartspace(1.0f);
            }
        }
    }

    public void setPivotX(float f) {
        ((KeyguardStatusView) this.mView).setPivotX(f);
    }

    public void setPivotY(float f) {
        ((KeyguardStatusView) this.mView).setPivotY(f);
    }

    public float getClockTextSize() {
        return this.mKeyguardClockSwitchController.getClockTextSize();
    }

    public int getLockscreenHeight() {
        return ((KeyguardStatusView) this.mView).getHeight() - this.mKeyguardClockSwitchController.getNotificationIconAreaHeight();
    }

    public void setStatusAccessibilityImportance(int i) {
        ((KeyguardStatusView) this.mView).setImportantForAccessibility(i);
    }

    public void updatePosition(int i, int i2, float f, boolean z) {
        AnimationProperties animationProperties = CLOCK_ANIMATION_PROPERTIES;
        PropertyAnimator.setProperty((KeyguardStatusView) this.mView, AnimatableProperty.Y, (float) i2, animationProperties, z);
        this.mKeyguardClockSwitchController.updatePosition(i, f, animationProperties, z);
    }

    public void setKeyguardStatusViewVisibility(int i, boolean z, boolean z2, int i2) {
        this.mKeyguardVisibilityHelper.setViewVisibility(i, z, z2, i2);
    }

    /* access modifiers changed from: private */
    public void refreshTime() {
        this.mKeyguardClockSwitchController.refresh();
    }

    public void setClipBounds(Rect rect) {
        if (rect != null) {
            this.mClipBounds.set(rect.left, (int) (((float) rect.top) - ((KeyguardStatusView) this.mView).getY()), rect.right, (int) (((float) rect.bottom) - ((KeyguardStatusView) this.mView).getY()));
            ((KeyguardStatusView) this.mView).setClipBounds(this.mClipBounds);
            return;
        }
        ((KeyguardStatusView) this.mView).setClipBounds(null);
    }
}
