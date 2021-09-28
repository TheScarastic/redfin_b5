package com.android.systemui.assist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
/* loaded from: classes.dex */
public class AssistOrbContainer extends FrameLayout {
    private boolean mAnimatingOut;
    private View mNavbarScrim;
    private AssistOrbView mOrb;
    private View mScrim;

    public AssistOrbContainer(Context context) {
        this(context, null);
    }

    public AssistOrbContainer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AssistOrbContainer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mScrim = findViewById(R$id.assist_orb_scrim);
        this.mNavbarScrim = findViewById(R$id.assist_orb_navbar_scrim);
        this.mOrb = (AssistOrbView) findViewById(R$id.assist_orb);
    }

    public void show(boolean z, boolean z2, final Runnable runnable) {
        if (z) {
            if (getVisibility() != 0) {
                setVisibility(0);
                if (z2) {
                    startEnterAnimation(runnable);
                    return;
                }
                reset();
                if (runnable != null) {
                    runnable.run();
                }
            }
        } else if (z2) {
            startExitAnimation(new Runnable() { // from class: com.android.systemui.assist.AssistOrbContainer.1
                @Override // java.lang.Runnable
                public void run() {
                    AssistOrbContainer.this.mAnimatingOut = false;
                    AssistOrbContainer.this.setVisibility(8);
                    Runnable runnable2 = runnable;
                    if (runnable2 != null) {
                        runnable2.run();
                    }
                }
            });
        } else {
            setVisibility(8);
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    private void reset() {
        this.mAnimatingOut = false;
        this.mOrb.reset();
        this.mScrim.setAlpha(1.0f);
        this.mNavbarScrim.setAlpha(1.0f);
    }

    private void startEnterAnimation(final Runnable runnable) {
        if (!this.mAnimatingOut) {
            this.mOrb.startEnterAnimation();
            this.mScrim.setAlpha(0.0f);
            this.mNavbarScrim.setAlpha(0.0f);
            post(new Runnable() { // from class: com.android.systemui.assist.AssistOrbContainer.2
                @Override // java.lang.Runnable
                public void run() {
                    ViewPropertyAnimator startDelay = AssistOrbContainer.this.mScrim.animate().alpha(1.0f).setDuration(300).setStartDelay(0);
                    Interpolator interpolator = Interpolators.LINEAR_OUT_SLOW_IN;
                    startDelay.setInterpolator(interpolator);
                    AssistOrbContainer.this.mNavbarScrim.animate().alpha(1.0f).setDuration(300).setStartDelay(0).setInterpolator(interpolator).withEndAction(runnable);
                }
            });
        }
    }

    private void startExitAnimation(Runnable runnable) {
        if (!this.mAnimatingOut) {
            this.mAnimatingOut = true;
            this.mOrb.startExitAnimation(150);
            ViewPropertyAnimator startDelay = this.mScrim.animate().alpha(0.0f).setDuration(250).setStartDelay(150);
            Interpolator interpolator = Interpolators.FAST_OUT_SLOW_IN;
            startDelay.setInterpolator(interpolator);
            this.mNavbarScrim.animate().alpha(0.0f).setDuration(250).setStartDelay(150).setInterpolator(interpolator).withEndAction(runnable);
        } else if (runnable != null) {
            runnable.run();
        }
    }

    public boolean isShowing() {
        return getVisibility() == 0 && !this.mAnimatingOut;
    }

    public AssistOrbView getOrb() {
        return this.mOrb;
    }
}
