package com.android.wm.shell.onehanded;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.view.ContextThemeWrapper;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.R;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.onehanded.OneHandedAnimationController;
import com.android.wm.shell.onehanded.OneHandedState;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public class OneHandedTutorialHandler implements OneHandedTransitionCallback, OneHandedState.OnStateChangedListener, OneHandedAnimationCallback {
    private int mAlphaAnimationDurationMs;
    private ValueAnimator mAlphaAnimator;
    private float mAlphaTransitionStart;
    private Context mContext;
    private int mCurrentState;
    private Rect mDisplayBounds;
    private ViewGroup mTargetViewContainer;
    private int mTutorialAreaHeight;
    private final float mTutorialHeightRatio;
    private View mTutorialView;
    private final WindowManager mWindowManager;

    public OneHandedTutorialHandler(Context context, OneHandedSettingsUtil oneHandedSettingsUtil, WindowManager windowManager) {
        this.mContext = context;
        this.mWindowManager = windowManager;
        this.mTutorialHeightRatio = oneHandedSettingsUtil.getTranslationFraction(context);
        this.mAlphaAnimationDurationMs = oneHandedSettingsUtil.getTransitionDuration(context);
    }

    @Override // com.android.wm.shell.onehanded.OneHandedAnimationCallback
    public void onOneHandedAnimationCancel(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
        ValueAnimator valueAnimator = this.mAlphaAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    @Override // com.android.wm.shell.onehanded.OneHandedAnimationCallback
    public void onAnimationUpdate(SurfaceControl.Transaction transaction, float f, float f2) {
        if (isAttached()) {
            if (f2 < this.mAlphaTransitionStart) {
                checkTransitionEnd();
                return;
            }
            ValueAnimator valueAnimator = this.mAlphaAnimator;
            if (valueAnimator != null && !valueAnimator.isStarted() && !this.mAlphaAnimator.isRunning()) {
                this.mAlphaAnimator.start();
            }
        }
    }

    @Override // com.android.wm.shell.onehanded.OneHandedState.OnStateChangedListener
    public void onStateChanged(int i) {
        this.mCurrentState = i;
        if (i != 0) {
            if (i == 1) {
                createViewAndAttachToWindow(this.mContext);
                updateThemeColor();
                setupAlphaTransition(true);
                return;
            } else if (i == 2) {
                checkTransitionEnd();
                setupAlphaTransition(false);
                return;
            } else if (i != 3) {
                return;
            }
        }
        checkTransitionEnd();
        removeTutorialFromWindowManager();
    }

    public void onDisplayChanged(DisplayLayout displayLayout) {
        if (displayLayout.height() > displayLayout.width()) {
            this.mDisplayBounds = new Rect(0, 0, displayLayout.width(), displayLayout.height());
        } else {
            this.mDisplayBounds = new Rect(0, 0, displayLayout.height(), displayLayout.width());
        }
        int round = Math.round(((float) this.mDisplayBounds.height()) * this.mTutorialHeightRatio);
        this.mTutorialAreaHeight = round;
        this.mAlphaTransitionStart = ((float) round) * 0.6f;
    }

    @VisibleForTesting
    void createViewAndAttachToWindow(Context context) {
        if (!isAttached()) {
            this.mTutorialView = LayoutInflater.from(context).inflate(R.layout.one_handed_tutorial, (ViewGroup) null);
            FrameLayout frameLayout = new FrameLayout(context);
            this.mTargetViewContainer = frameLayout;
            frameLayout.setClipChildren(false);
            this.mTargetViewContainer.setAlpha(this.mCurrentState == 2 ? 1.0f : 0.0f);
            this.mTargetViewContainer.addView(this.mTutorialView);
            this.mTargetViewContainer.setLayerType(2, null);
            attachTargetToWindow();
        }
    }

    private void attachTargetToWindow() {
        try {
            this.mWindowManager.addView(this.mTargetViewContainer, getTutorialTargetLayoutParams());
        } catch (IllegalStateException unused) {
            this.mWindowManager.updateViewLayout(this.mTargetViewContainer, getTutorialTargetLayoutParams());
        }
    }

    @VisibleForTesting
    void removeTutorialFromWindowManager() {
        if (isAttached()) {
            this.mTargetViewContainer.setLayerType(0, null);
            this.mWindowManager.removeViewImmediate(this.mTargetViewContainer);
            this.mTargetViewContainer = null;
        }
    }

    private WindowManager.LayoutParams getTutorialTargetLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(this.mDisplayBounds.width(), this.mTutorialAreaHeight, 0, 0, 2024, 264, -3);
        layoutParams.gravity = 51;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.privateFlags |= 16;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.setTitle("one-handed-tutorial-overlay");
        return layoutParams;
    }

    @VisibleForTesting
    boolean isAttached() {
        ViewGroup viewGroup = this.mTargetViewContainer;
        return viewGroup != null && viewGroup.isAttachedToWindow();
    }

    public void onConfigurationChanged() {
        removeTutorialFromWindowManager();
        int i = this.mCurrentState;
        if (i == 1 || i == 2) {
            createViewAndAttachToWindow(this.mContext);
            updateThemeColor();
            checkTransitionEnd();
        }
    }

    private void updateThemeColor() {
        if (this.mTutorialView != null) {
            TypedArray obtainStyledAttributes = new ContextThemeWrapper(this.mTutorialView.getContext(), 16974563).obtainStyledAttributes(new int[]{16842806, 16842808});
            int color = obtainStyledAttributes.getColor(0, 0);
            int color2 = obtainStyledAttributes.getColor(1, 0);
            obtainStyledAttributes.recycle();
            ((ImageView) this.mTutorialView.findViewById(R.id.one_handed_tutorial_image)).setImageTintList(ColorStateList.valueOf(color));
            ((TextView) this.mTutorialView.findViewById(R.id.one_handed_tutorial_title)).setTextColor(color);
            ((TextView) this.mTutorialView.findViewById(R.id.one_handed_tutorial_description)).setTextColor(color2);
        }
    }

    private void setupAlphaTransition(boolean z) {
        float f = 0.0f;
        float f2 = z ? 0.0f : 1.0f;
        if (z) {
            f = 1.0f;
        }
        int round = z ? this.mAlphaAnimationDurationMs : Math.round(((float) this.mAlphaAnimationDurationMs) * (1.0f - this.mTutorialHeightRatio));
        ValueAnimator ofFloat = ValueAnimator.ofFloat(f2, f);
        this.mAlphaAnimator = ofFloat;
        ofFloat.setInterpolator(new LinearInterpolator());
        this.mAlphaAnimator.setDuration((long) round);
        this.mAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.wm.shell.onehanded.OneHandedTutorialHandler$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                OneHandedTutorialHandler.$r8$lambda$jhhcX4xE3FrAi4hp28D349ARzJs(OneHandedTutorialHandler.this, valueAnimator);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupAlphaTransition$0(ValueAnimator valueAnimator) {
        this.mTargetViewContainer.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    private void checkTransitionEnd() {
        ValueAnimator valueAnimator = this.mAlphaAnimator;
        if (valueAnimator == null) {
            return;
        }
        if (valueAnimator.isRunning() || this.mAlphaAnimator.isStarted()) {
            this.mAlphaAnimator.end();
            this.mAlphaAnimator.removeAllUpdateListeners();
            this.mAlphaAnimator = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        printWriter.println("OneHandedTutorialHandler");
        printWriter.print("  isAttached=");
        printWriter.println(isAttached());
        printWriter.print("  mCurrentState=");
        printWriter.println(this.mCurrentState);
        printWriter.print("  mDisplayBounds=");
        printWriter.println(this.mDisplayBounds);
        printWriter.print("  mTutorialAreaHeight=");
        printWriter.println(this.mTutorialAreaHeight);
        printWriter.print("  mAlphaTransitionStart=");
        printWriter.println(this.mAlphaTransitionStart);
        printWriter.print("  mAlphaAnimationDurationMs=");
        printWriter.println(this.mAlphaAnimationDurationMs);
    }
}
