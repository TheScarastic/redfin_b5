package com.android.systemui.settings.brightness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import com.android.settingslib.RestrictedLockUtils;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.R$layout;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.brightness.BrightnessSliderView;
import com.android.systemui.settings.brightness.ToggleSlider;
import com.android.systemui.statusbar.policy.BrightnessMirrorController;
import com.android.systemui.util.ViewController;
/* loaded from: classes.dex */
public class BrightnessSlider extends ViewController<BrightnessSliderView> implements ToggleSlider {
    private final FalsingManager mFalsingManager;
    private ToggleSlider.Listener mListener;
    private ToggleSlider mMirror;
    private BrightnessMirrorController mMirrorController;
    private final Gefingerpoken mOnInterceptListener = new Gefingerpoken() { // from class: com.android.systemui.settings.brightness.BrightnessSlider.1
        @Override // com.android.systemui.Gefingerpoken
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // com.android.systemui.Gefingerpoken
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked != 1 && actionMasked != 3) {
                return false;
            }
            BrightnessSlider.this.mFalsingManager.isFalseTouch(10);
            return false;
        }
    };
    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() { // from class: com.android.systemui.settings.brightness.BrightnessSlider.2
        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (BrightnessSlider.this.mListener != null) {
                BrightnessSlider.this.mListener.onChanged(BrightnessSlider.this.mTracking, i, false);
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
            BrightnessSlider.this.mTracking = true;
            if (BrightnessSlider.this.mListener != null) {
                BrightnessSlider.this.mListener.onChanged(BrightnessSlider.this.mTracking, BrightnessSlider.this.getValue(), false);
            }
            if (BrightnessSlider.this.mMirrorController != null) {
                BrightnessSlider.this.mMirrorController.showMirror();
                BrightnessSlider.this.mMirrorController.setLocationAndSize(((ViewController) BrightnessSlider.this).mView);
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            BrightnessSlider.this.mTracking = false;
            if (BrightnessSlider.this.mListener != null) {
                BrightnessSlider.this.mListener.onChanged(BrightnessSlider.this.mTracking, BrightnessSlider.this.getValue(), true);
            }
            if (BrightnessSlider.this.mMirrorController != null) {
                BrightnessSlider.this.mMirrorController.hideMirror();
            }
        }
    };
    private boolean mTracking;

    BrightnessSlider(BrightnessSliderView brightnessSliderView, FalsingManager falsingManager) {
        super(brightnessSliderView);
        this.mFalsingManager = falsingManager;
    }

    public View getRootView() {
        return this.mView;
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
        ((BrightnessSliderView) this.mView).setOnSeekBarChangeListener(this.mSeekListener);
        ((BrightnessSliderView) this.mView).setOnInterceptListener(this.mOnInterceptListener);
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
        ((BrightnessSliderView) this.mView).setOnSeekBarChangeListener(null);
        ((BrightnessSliderView) this.mView).setOnDispatchTouchEventListener(null);
        ((BrightnessSliderView) this.mView).setOnInterceptListener(null);
    }

    @Override // com.android.systemui.settings.brightness.ToggleSlider
    public boolean mirrorTouchEvent(MotionEvent motionEvent) {
        if (this.mMirror != null) {
            return copyEventToMirror(motionEvent);
        }
        return ((BrightnessSliderView) this.mView).dispatchTouchEvent(motionEvent);
    }

    private boolean copyEventToMirror(MotionEvent motionEvent) {
        MotionEvent copy = motionEvent.copy();
        boolean mirrorTouchEvent = this.mMirror.mirrorTouchEvent(copy);
        copy.recycle();
        return mirrorTouchEvent;
    }

    @Override // com.android.systemui.settings.brightness.ToggleSlider
    public void setEnforcedAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        ((BrightnessSliderView) this.mView).setEnforcedAdmin(enforcedAdmin);
    }

    private void setMirror(ToggleSlider toggleSlider) {
        this.mMirror = toggleSlider;
        if (toggleSlider != null) {
            toggleSlider.setMax(((BrightnessSliderView) this.mView).getMax());
            this.mMirror.setValue(((BrightnessSliderView) this.mView).getValue());
            ((BrightnessSliderView) this.mView).setOnDispatchTouchEventListener(new BrightnessSliderView.DispatchTouchEventListener() { // from class: com.android.systemui.settings.brightness.BrightnessSlider$$ExternalSyntheticLambda0
                @Override // com.android.systemui.settings.brightness.BrightnessSliderView.DispatchTouchEventListener
                public final boolean onDispatchTouchEvent(MotionEvent motionEvent) {
                    return BrightnessSlider.this.mirrorTouchEvent(motionEvent);
                }
            });
            return;
        }
        ((BrightnessSliderView) this.mView).setOnDispatchTouchEventListener(null);
    }

    public void setMirrorControllerAndMirror(BrightnessMirrorController brightnessMirrorController) {
        this.mMirrorController = brightnessMirrorController;
        if (brightnessMirrorController != null) {
            setMirror(brightnessMirrorController.getToggleSlider());
        } else {
            ((BrightnessSliderView) this.mView).setOnDispatchTouchEventListener(null);
        }
    }

    @Override // com.android.systemui.settings.brightness.ToggleSlider
    public void setOnChangedListener(ToggleSlider.Listener listener) {
        this.mListener = listener;
    }

    @Override // com.android.systemui.settings.brightness.ToggleSlider
    public void setMax(int i) {
        ((BrightnessSliderView) this.mView).setMax(i);
        ToggleSlider toggleSlider = this.mMirror;
        if (toggleSlider != null) {
            toggleSlider.setMax(i);
        }
    }

    @Override // com.android.systemui.settings.brightness.ToggleSlider
    public void setValue(int i) {
        ((BrightnessSliderView) this.mView).setValue(i);
        ToggleSlider toggleSlider = this.mMirror;
        if (toggleSlider != null) {
            toggleSlider.setValue(i);
        }
    }

    @Override // com.android.systemui.settings.brightness.ToggleSlider
    public int getValue() {
        return ((BrightnessSliderView) this.mView).getValue();
    }

    /* loaded from: classes.dex */
    public static class Factory {
        private final FalsingManager mFalsingManager;

        public Factory(FalsingManager falsingManager) {
            this.mFalsingManager = falsingManager;
        }

        public BrightnessSlider create(Context context, ViewGroup viewGroup) {
            return new BrightnessSlider((BrightnessSliderView) LayoutInflater.from(context).inflate(getLayout(), viewGroup, false), this.mFalsingManager);
        }

        private int getLayout() {
            return R$layout.quick_settings_brightness_dialog;
        }
    }
}
