package com.android.systemui.accessibility;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.RemoteException;
import android.util.Log;
import android.view.accessibility.IRemoteMagnificationAnimationCallback;
import android.view.animation.AccelerateInterpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$integer;
import java.io.PrintWriter;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class WindowMagnificationAnimationController implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private static final boolean DEBUG = Log.isLoggable("WindowMagnificationAnimationController", 3);
    private IRemoteMagnificationAnimationCallback mAnimationCallback;
    private final Context mContext;
    private final WindowMagnificationController mController;
    private boolean mEndAnimationCanceled;
    private final AnimationSpec mEndSpec;
    private final AnimationSpec mStartSpec;
    private int mState;
    private final ValueAnimator mValueAnimator;

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationRepeat(Animator animator) {
    }

    /* access modifiers changed from: package-private */
    public WindowMagnificationAnimationController(Context context, WindowMagnificationController windowMagnificationController) {
        this(context, windowMagnificationController, newValueAnimator(context.getResources()));
    }

    @VisibleForTesting
    WindowMagnificationAnimationController(Context context, WindowMagnificationController windowMagnificationController, ValueAnimator valueAnimator) {
        this.mStartSpec = new AnimationSpec();
        this.mEndSpec = new AnimationSpec();
        this.mEndAnimationCanceled = false;
        this.mState = 0;
        this.mContext = context;
        this.mController = windowMagnificationController;
        this.mValueAnimator = valueAnimator;
        valueAnimator.addUpdateListener(this);
        valueAnimator.addListener(this);
    }

    /* access modifiers changed from: package-private */
    public void enableWindowMagnification(float f, float f2, float f3, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        sendAnimationCallback(false);
        this.mAnimationCallback = iRemoteMagnificationAnimationCallback;
        setupEnableAnimationSpecs(f, f2, f3);
        if (this.mEndSpec.equals(this.mStartSpec)) {
            int i = this.mState;
            if (i == 0) {
                this.mController.enableWindowMagnification(f, f2, f3);
            } else if (i == 3 || i == 2) {
                this.mValueAnimator.cancel();
            }
            sendAnimationCallback(true);
            setState(1);
            return;
        }
        int i2 = this.mState;
        if (i2 == 2) {
            this.mValueAnimator.reverse();
        } else {
            if (i2 == 3) {
                this.mValueAnimator.cancel();
            }
            this.mValueAnimator.start();
        }
        setState(3);
    }

    private void setupEnableAnimationSpecs(float f, float f2, float f3) {
        float scale = this.mController.getScale();
        float centerX = this.mController.getCenterX();
        float centerY = this.mController.getCenterY();
        if (this.mState == 0) {
            this.mStartSpec.set(1.0f, f2, f3);
            AnimationSpec animationSpec = this.mEndSpec;
            if (Float.isNaN(f)) {
                f = (float) this.mContext.getResources().getInteger(R$integer.magnification_default_scale);
            }
            animationSpec.set(f, f2, f3);
        } else {
            this.mStartSpec.set(scale, centerX, centerY);
            AnimationSpec animationSpec2 = this.mEndSpec;
            if (Float.isNaN(f)) {
                f = scale;
            }
            if (Float.isNaN(f2)) {
                f2 = centerX;
            }
            if (Float.isNaN(f3)) {
                f3 = centerY;
            }
            animationSpec2.set(f, f2, f3);
        }
        if (DEBUG) {
            Log.d("WindowMagnificationAnimationController", "SetupEnableAnimationSpecs : mStartSpec = " + this.mStartSpec + ", endSpec = " + this.mEndSpec);
        }
    }

    /* access modifiers changed from: package-private */
    public void setScale(float f) {
        if (!this.mValueAnimator.isRunning()) {
            this.mController.setScale(f);
        }
    }

    /* access modifiers changed from: package-private */
    public void deleteWindowMagnification(IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        sendAnimationCallback(false);
        this.mAnimationCallback = iRemoteMagnificationAnimationCallback;
        int i = this.mState;
        if (i != 0 && i != 2) {
            this.mStartSpec.set(1.0f, Float.NaN, Float.NaN);
            this.mEndSpec.set(this.mController.getScale(), Float.NaN, Float.NaN);
            this.mValueAnimator.reverse();
            setState(2);
        } else if (i == 0) {
            sendAnimationCallback(true);
        }
    }

    /* access modifiers changed from: package-private */
    public void moveWindowMagnifier(float f, float f2) {
        if (!this.mValueAnimator.isRunning()) {
            this.mController.moveWindowMagnifier(f, f2);
        }
    }

    /* access modifiers changed from: package-private */
    public void onConfigurationChanged(int i) {
        this.mController.onConfigurationChanged(i);
    }

    private void setState(int i) {
        if (DEBUG) {
            Log.d("WindowMagnificationAnimationController", "setState from " + this.mState + " to " + i);
        }
        this.mState = i;
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator animator) {
        this.mEndAnimationCanceled = false;
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator, boolean z) {
        if (!this.mEndAnimationCanceled) {
            if (z) {
                this.mController.deleteWindowMagnification();
                setState(0);
            } else {
                setState(1);
            }
            sendAnimationCallback(true);
        }
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animator) {
        this.mEndAnimationCanceled = true;
    }

    private void sendAnimationCallback(boolean z) {
        IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback = this.mAnimationCallback;
        if (iRemoteMagnificationAnimationCallback != null) {
            try {
                iRemoteMagnificationAnimationCallback.onResult(z);
                if (DEBUG) {
                    Log.d("WindowMagnificationAnimationController", "sendAnimationCallback success = " + z);
                }
            } catch (RemoteException e) {
                Log.w("WindowMagnificationAnimationController", "sendAnimationCallback failed : " + e);
            }
            this.mAnimationCallback = null;
        }
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        this.mController.enableWindowMagnification(this.mStartSpec.mScale + ((this.mEndSpec.mScale - this.mStartSpec.mScale) * animatedFraction), this.mStartSpec.mCenterX + ((this.mEndSpec.mCenterX - this.mStartSpec.mCenterX) * animatedFraction), this.mStartSpec.mCenterY + ((this.mEndSpec.mCenterY - this.mStartSpec.mCenterY) * animatedFraction));
    }

    public void updateSysUiStateFlag() {
        this.mController.updateSysUIStateFlag();
    }

    /* access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        this.mController.dump(printWriter);
    }

    private static ValueAnimator newValueAnimator(Resources resources) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration((long) resources.getInteger(17694722));
        valueAnimator.setInterpolator(new AccelerateInterpolator(2.5f));
        valueAnimator.setFloatValues(0.0f, 1.0f);
        return valueAnimator;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class AnimationSpec {
        private float mCenterX;
        private float mCenterY;
        private float mScale;

        private AnimationSpec() {
            this.mScale = Float.NaN;
            this.mCenterX = Float.NaN;
            this.mCenterY = Float.NaN;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || AnimationSpec.class != obj.getClass()) {
                return false;
            }
            AnimationSpec animationSpec = (AnimationSpec) obj;
            return this.mScale == animationSpec.mScale && this.mCenterX == animationSpec.mCenterX && this.mCenterY == animationSpec.mCenterY;
        }

        public int hashCode() {
            float f = this.mScale;
            int i = 0;
            int floatToIntBits = (f != 0.0f ? Float.floatToIntBits(f) : 0) * 31;
            float f2 = this.mCenterX;
            int floatToIntBits2 = (floatToIntBits + (f2 != 0.0f ? Float.floatToIntBits(f2) : 0)) * 31;
            float f3 = this.mCenterY;
            if (f3 != 0.0f) {
                i = Float.floatToIntBits(f3);
            }
            return floatToIntBits2 + i;
        }

        void set(float f, float f2, float f3) {
            this.mScale = f;
            this.mCenterX = f2;
            this.mCenterY = f3;
        }

        public String toString() {
            return "AnimationSpec{mScale=" + this.mScale + ", mCenterX=" + this.mCenterX + ", mCenterY=" + this.mCenterY + '}';
        }
    }
}
