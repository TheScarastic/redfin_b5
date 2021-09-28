package com.android.keyguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.NumberFormat;
import com.android.settingslib.Utils;
import com.android.systemui.R$attr;
import com.android.systemui.R$dimen;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.ViewController;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
/* loaded from: classes.dex */
public class AnimatableClockController extends ViewController<AnimatableClockView> {
    private final BatteryController mBatteryController;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final NumberFormat mBurmeseNf;
    private final String mBurmeseNumerals;
    private final KeyguardBypassController mBypassController;
    private float mDozeAmount;
    private boolean mIsCharging;
    private boolean mIsDozing;
    boolean mKeyguardShowing;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private Locale mLocale;
    private int mLockScreenColor;
    private final StatusBarStateController mStatusBarStateController;
    private final int mDozingColor = -1;
    private final BatteryController.BatteryStateChangeCallback mBatteryCallback = new BatteryController.BatteryStateChangeCallback() { // from class: com.android.keyguard.AnimatableClockController.1
        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
            AnimatableClockController animatableClockController = AnimatableClockController.this;
            if (animatableClockController.mKeyguardShowing && !animatableClockController.mIsCharging && z2) {
                StatusBarStateController statusBarStateController = AnimatableClockController.this.mStatusBarStateController;
                Objects.requireNonNull(statusBarStateController);
                ((AnimatableClockView) ((ViewController) AnimatableClockController.this).mView).animateCharge(new AnimatableClockController$1$$ExternalSyntheticLambda0(statusBarStateController));
            }
            AnimatableClockController.this.mIsCharging = z2;
        }
    };
    private final BroadcastReceiver mLocaleBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.keyguard.AnimatableClockController.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            AnimatableClockController.this.updateLocale();
        }
    };
    private final StatusBarStateController.StateListener mStatusBarStatePersistentListener = new StatusBarStateController.StateListener() { // from class: com.android.keyguard.AnimatableClockController.3
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onDozeAmountChanged(float f, float f2) {
            boolean z = false;
            boolean z2 = (AnimatableClockController.this.mDozeAmount == 0.0f && f == 1.0f) || (AnimatableClockController.this.mDozeAmount == 1.0f && f == 0.0f);
            if (f > AnimatableClockController.this.mDozeAmount) {
                z = true;
            }
            AnimatableClockController.this.mDozeAmount = f;
            if (AnimatableClockController.this.mIsDozing != z) {
                AnimatableClockController.this.mIsDozing = z;
                ((AnimatableClockView) ((ViewController) AnimatableClockController.this).mView).animateDoze(AnimatableClockController.this.mIsDozing, !z2);
            }
        }
    };
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.AnimatableClockController.4
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onKeyguardVisibilityChanged(boolean z) {
            AnimatableClockController animatableClockController = AnimatableClockController.this;
            animatableClockController.mKeyguardShowing = z;
            if (!z) {
                animatableClockController.reset();
            }
        }
    };
    private final float mBurmeseLineSpacing = getContext().getResources().getFloat(R$dimen.keyguard_clock_line_spacing_scale_burmese);
    private final float mDefaultLineSpacing = getContext().getResources().getFloat(R$dimen.keyguard_clock_line_spacing_scale);

    public AnimatableClockController(AnimatableClockView animatableClockView, StatusBarStateController statusBarStateController, BroadcastDispatcher broadcastDispatcher, BatteryController batteryController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardBypassController keyguardBypassController) {
        super(animatableClockView);
        NumberFormat instance = NumberFormat.getInstance(Locale.forLanguageTag("my"));
        this.mBurmeseNf = instance;
        this.mStatusBarStateController = statusBarStateController;
        this.mIsDozing = statusBarStateController.isDozing();
        this.mDozeAmount = statusBarStateController.getDozeAmount();
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mBypassController = keyguardBypassController;
        this.mBatteryController = batteryController;
        this.mBurmeseNumerals = instance.format(1234567890L);
    }

    /* access modifiers changed from: private */
    public void reset() {
        ((AnimatableClockView) this.mView).animateDoze(this.mIsDozing, false);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        this.mIsDozing = this.mStatusBarStateController.isDozing();
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
        updateLocale();
        this.mBroadcastDispatcher.registerReceiver(this.mLocaleBroadcastReceiver, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
        this.mDozeAmount = this.mStatusBarStateController.getDozeAmount();
        this.mBatteryController.addCallback(this.mBatteryCallback);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        this.mStatusBarStateController.removeCallback(this.mStatusBarStatePersistentListener);
        this.mStatusBarStateController.addCallback(this.mStatusBarStatePersistentListener);
        refreshTime();
        initColors();
        ((AnimatableClockView) this.mView).animateDoze(this.mIsDozing, false);
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
        this.mBroadcastDispatcher.unregisterReceiver(this.mLocaleBroadcastReceiver);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
        this.mBatteryController.removeCallback(this.mBatteryCallback);
        if (!((AnimatableClockView) this.mView).isAttachedToWindow()) {
            this.mStatusBarStateController.removeCallback(this.mStatusBarStatePersistentListener);
        }
    }

    public void animateAppear() {
        if (!this.mIsDozing) {
            ((AnimatableClockView) this.mView).animateAppearOnLockscreen();
        }
    }

    public void refreshTime() {
        ((AnimatableClockView) this.mView).refreshTime();
    }

    public void onTimeZoneChanged(TimeZone timeZone) {
        ((AnimatableClockView) this.mView).onTimeZoneChanged(timeZone);
    }

    public void refreshFormat() {
        ((AnimatableClockView) this.mView).refreshFormat();
    }

    /* access modifiers changed from: private */
    public void updateLocale() {
        Locale locale = Locale.getDefault();
        if (!Objects.equals(locale, this.mLocale)) {
            this.mLocale = locale;
            if (NumberFormat.getInstance(locale).format(1234567890L).equals(this.mBurmeseNumerals)) {
                ((AnimatableClockView) this.mView).setLineSpacingScale(this.mBurmeseLineSpacing);
            } else {
                ((AnimatableClockView) this.mView).setLineSpacingScale(this.mDefaultLineSpacing);
            }
            ((AnimatableClockView) this.mView).refreshFormat();
        }
    }

    private void initColors() {
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(getContext(), R$attr.wallpaperTextColorAccent);
        this.mLockScreenColor = colorAttrDefaultColor;
        ((AnimatableClockView) this.mView).setColors(-1, colorAttrDefaultColor);
        ((AnimatableClockView) this.mView).animateDoze(this.mIsDozing, false);
    }
}
