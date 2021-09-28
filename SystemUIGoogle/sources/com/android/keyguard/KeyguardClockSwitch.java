package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.ClockPlugin;
import java.util.TimeZone;
/* loaded from: classes.dex */
public class KeyguardClockSwitch extends RelativeLayout {
    private FrameLayout mClockFrame;
    private ClockPlugin mClockPlugin;
    private int mClockSwitchYAmount;
    private AnimatableClockView mClockView;
    private int[] mColorPalette;
    private float mDarkAmount;
    private View mKeyguardStatusArea;
    private FrameLayout mLargeClockFrame;
    private AnimatableClockView mLargeClockView;
    private int mSmartspaceTopOffset;
    private View mSmartspaceView;
    private boolean mSupportsDarkText;
    private Boolean mHasVisibleNotifications = null;
    private AnimatorSet mClockInAnim = null;
    private AnimatorSet mClockOutAnim = null;
    private ObjectAnimator mSmartspaceAnim = null;

    public KeyguardClockSwitch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onDensityOrFontScaleChanged() {
        this.mLargeClockView.setTextSize(0, (float) ((RelativeLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.large_clock_text_size));
        this.mClockView.setTextSize(0, (float) ((RelativeLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.clock_text_size));
        this.mClockSwitchYAmount = ((RelativeLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.keyguard_clock_switch_y_shift);
        this.mSmartspaceTopOffset = ((RelativeLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.keyguard_smartspace_top_offset);
    }

    public boolean hasCustomClock() {
        return this.mClockPlugin != null;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mClockFrame = (FrameLayout) findViewById(R$id.lockscreen_clock_view);
        this.mClockView = (AnimatableClockView) findViewById(R$id.animatable_clock_view);
        this.mLargeClockFrame = (FrameLayout) findViewById(R$id.lockscreen_clock_view_large);
        this.mLargeClockView = (AnimatableClockView) findViewById(R$id.animatable_clock_view_large);
        this.mKeyguardStatusArea = findViewById(R$id.keyguard_status_area);
        onDensityOrFontScaleChanged();
    }

    /* access modifiers changed from: package-private */
    public void setClockPlugin(ClockPlugin clockPlugin, int i) {
        FrameLayout frameLayout;
        FrameLayout frameLayout2;
        ClockPlugin clockPlugin2 = this.mClockPlugin;
        if (clockPlugin2 != null) {
            View view = clockPlugin2.getView();
            if (view != null && view.getParent() == (frameLayout2 = this.mClockFrame)) {
                frameLayout2.removeView(view);
            }
            View bigClockView = this.mClockPlugin.getBigClockView();
            if (bigClockView != null && bigClockView.getParent() == (frameLayout = this.mLargeClockFrame)) {
                frameLayout.removeView(bigClockView);
            }
            this.mClockPlugin.onDestroyView();
            this.mClockPlugin = null;
        }
        if (clockPlugin == null) {
            this.mClockView.setVisibility(0);
            this.mLargeClockView.setVisibility(0);
            return;
        }
        View view2 = clockPlugin.getView();
        if (view2 != null) {
            this.mClockFrame.addView(view2, -1, new ViewGroup.LayoutParams(-1, -2));
            this.mClockView.setVisibility(8);
        }
        View bigClockView2 = clockPlugin.getBigClockView();
        if (bigClockView2 != null) {
            this.mLargeClockFrame.addView(bigClockView2);
            this.mLargeClockView.setVisibility(8);
        }
        this.mClockPlugin = clockPlugin;
        clockPlugin.setStyle(getPaint().getStyle());
        this.mClockPlugin.setTextColor(getCurrentTextColor());
        this.mClockPlugin.setDarkAmount(this.mDarkAmount);
        int[] iArr = this.mColorPalette;
        if (iArr != null) {
            this.mClockPlugin.setColorPalette(this.mSupportsDarkText, iArr);
        }
    }

    public void setTextColor(int i) {
        ClockPlugin clockPlugin = this.mClockPlugin;
        if (clockPlugin != null) {
            clockPlugin.setTextColor(i);
        }
    }

    private void animateClockChange(boolean z) {
        FrameLayout frameLayout;
        float f;
        FrameLayout frameLayout2;
        AnimatorSet animatorSet = this.mClockInAnim;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        AnimatorSet animatorSet2 = this.mClockOutAnim;
        if (animatorSet2 != null) {
            animatorSet2.cancel();
        }
        ObjectAnimator objectAnimator = this.mSmartspaceAnim;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        int i = -1;
        if (z) {
            frameLayout = this.mClockFrame;
            frameLayout2 = this.mLargeClockFrame;
            if (indexOfChild(frameLayout2) == -1) {
                addView(frameLayout2);
            }
            if (this.mSmartspaceView == null) {
                f = 0.0f;
            } else {
                f = (float) ((this.mClockFrame.getTop() - this.mSmartspaceView.getTop()) + this.mSmartspaceTopOffset);
            }
        } else {
            frameLayout2 = this.mClockFrame;
            frameLayout = this.mLargeClockFrame;
            removeView(frameLayout);
            f = 0.0f;
            i = 1;
        }
        AnimatorSet animatorSet3 = new AnimatorSet();
        this.mClockOutAnim = animatorSet3;
        animatorSet3.setDuration(150L);
        this.mClockOutAnim.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
        this.mClockOutAnim.playTogether(ObjectAnimator.ofFloat(frameLayout, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(frameLayout, View.TRANSLATION_Y, 0.0f, (float) ((-this.mClockSwitchYAmount) * i)));
        this.mClockOutAnim.addListener(new AnimatorListenerAdapter() { // from class: com.android.keyguard.KeyguardClockSwitch.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                KeyguardClockSwitch.this.mClockOutAnim = null;
            }
        });
        frameLayout2.setAlpha(0.0f);
        frameLayout2.setVisibility(0);
        AnimatorSet animatorSet4 = new AnimatorSet();
        this.mClockInAnim = animatorSet4;
        animatorSet4.setDuration(200L);
        this.mClockInAnim.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        this.mClockInAnim.playTogether(ObjectAnimator.ofFloat(frameLayout2, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(frameLayout2, View.TRANSLATION_Y, (float) (i * this.mClockSwitchYAmount), 0.0f));
        this.mClockInAnim.setStartDelay(75);
        this.mClockInAnim.addListener(new AnimatorListenerAdapter() { // from class: com.android.keyguard.KeyguardClockSwitch.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                KeyguardClockSwitch.this.mClockInAnim = null;
            }
        });
        this.mClockInAnim.start();
        this.mClockOutAnim.start();
        View view = this.mSmartspaceView;
        if (view != null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, f);
            this.mSmartspaceAnim = ofFloat;
            ofFloat.setDuration(350L);
            this.mSmartspaceAnim.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            this.mSmartspaceAnim.addListener(new AnimatorListenerAdapter() { // from class: com.android.keyguard.KeyguardClockSwitch.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    KeyguardClockSwitch.this.mSmartspaceAnim = null;
                }
            });
            this.mSmartspaceAnim.start();
        }
    }

    public void setDarkAmount(float f) {
        this.mDarkAmount = f;
        ClockPlugin clockPlugin = this.mClockPlugin;
        if (clockPlugin != null) {
            clockPlugin.setDarkAmount(f);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean willSwitchToLargeClock(boolean z) {
        Boolean bool = this.mHasVisibleNotifications;
        if (bool != null && z == bool.booleanValue()) {
            return false;
        }
        boolean z2 = !z;
        animateClockChange(z2);
        this.mHasVisibleNotifications = Boolean.valueOf(z);
        return z2;
    }

    public Paint getPaint() {
        return this.mClockView.getPaint();
    }

    public int getCurrentTextColor() {
        return this.mClockView.getCurrentTextColor();
    }

    public float getTextSize() {
        return this.mClockView.getTextSize();
    }

    public void refresh() {
        ClockPlugin clockPlugin = this.mClockPlugin;
        if (clockPlugin != null) {
            clockPlugin.onTimeTick();
        }
    }

    public void onTimeZoneChanged(TimeZone timeZone) {
        ClockPlugin clockPlugin = this.mClockPlugin;
        if (clockPlugin != null) {
            clockPlugin.onTimeZoneChanged(timeZone);
        }
    }

    /* access modifiers changed from: package-private */
    public void setSmartspaceView(View view) {
        this.mSmartspaceView = view;
    }

    /* access modifiers changed from: package-private */
    public void updateColors(ColorExtractor.GradientColors gradientColors) {
        this.mSupportsDarkText = gradientColors.supportsDarkText();
        int[] colorPalette = gradientColors.getColorPalette();
        this.mColorPalette = colorPalette;
        ClockPlugin clockPlugin = this.mClockPlugin;
        if (clockPlugin != null) {
            clockPlugin.setColorPalette(this.mSupportsDarkText, colorPalette);
        }
    }
}
