package com.google.android.systemui.assist.uihints;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;
import android.util.MathUtils;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsListener;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import com.google.android.systemui.assist.uihints.edgelights.mode.FullListening;
import com.google.android.systemui.assist.uihints.input.TouchInsideRegion;
import java.util.Optional;
/* loaded from: classes2.dex */
public class ScrimController implements TranscriptionController.TranscriptionSpaceListener, NgaMessageHandler.CardInfoListener, EdgeLightsListener, TouchInsideRegion {
    private static final LinearInterpolator ALPHA_INTERPOLATOR = new LinearInterpolator();
    private final LightnessProvider mLightnessProvider;
    private float mMedianLightness;
    private final OverlappedElementController mOverlappedElement;
    private final ViewGroup mParent;
    private final View mScrimView;
    private VisibilityListener mVisibilityListener;
    private ValueAnimator mAlphaAnimator = new ValueAnimator();
    private float mInvocationProgress = 0.0f;
    private boolean mTranscriptionVisible = false;
    private boolean mCardVisible = false;
    private boolean mHaveAccurateLightness = false;
    private boolean mInFullListening = false;
    private boolean mCardTransitionAnimated = false;
    private boolean mCardForcesScrimGone = false;
    private boolean mIsDozing = false;

    public ScrimController(ViewGroup viewGroup, OverlappedElementController overlappedElementController, LightnessProvider lightnessProvider, TouchInsideHandler touchInsideHandler) {
        this.mParent = viewGroup;
        View findViewById = viewGroup.findViewById(R$id.scrim);
        this.mScrimView = findViewById;
        findViewById.setBackgroundTintBlendMode(BlendMode.SRC_IN);
        this.mLightnessProvider = lightnessProvider;
        findViewById.setOnClickListener(touchInsideHandler);
        findViewById.setOnTouchListener(touchInsideHandler);
        this.mOverlappedElement = overlappedElementController;
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.CardInfoListener
    public void onCardInfo(boolean z, int i, boolean z2, boolean z3) {
        this.mCardVisible = z;
        this.mCardTransitionAnimated = z2;
        this.mCardForcesScrimGone = z3;
        refresh();
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsListener
    public void onModeStarted(EdgeLightsView.Mode mode) {
        this.mInFullListening = mode instanceof FullListening;
        refresh();
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceListener
    public void onStateChanged(TranscriptionController.State state, TranscriptionController.State state2) {
        boolean z = state2 != TranscriptionController.State.NONE;
        if (this.mTranscriptionVisible != z) {
            this.mTranscriptionVisible = z;
            refresh();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isVisible() {
        return this.mScrimView.getVisibility() == 0;
    }

    /* access modifiers changed from: package-private */
    public void setVisibilityListener(VisibilityListener visibilityListener) {
        this.mVisibilityListener = visibilityListener;
    }

    @Override // com.google.android.systemui.assist.uihints.input.TouchInsideRegion
    public Optional<Region> getTouchInsideRegion() {
        if (!isVisible()) {
            return Optional.empty();
        }
        Rect rect = new Rect();
        this.mScrimView.getHitRect(rect);
        rect.top = rect.bottom - this.mScrimView.getResources().getDimensionPixelSize(R$dimen.scrim_touchable_height);
        return Optional.of(new Region(rect));
    }

    /* access modifiers changed from: package-private */
    public SurfaceControl getSurfaceControllerHandle() {
        if (this.mScrimView.getViewRootImpl() == null) {
            return null;
        }
        return this.mScrimView.getViewRootImpl().getSurfaceControl();
    }

    /* access modifiers changed from: package-private */
    public void setInvocationProgress(float f) {
        float constrain = MathUtils.constrain(f, 0.0f, 1.0f);
        if (this.mInvocationProgress != constrain) {
            this.mInvocationProgress = constrain;
            refresh();
        }
    }

    /* access modifiers changed from: package-private */
    public void setIsDozing(boolean z) {
        this.mIsDozing = z;
        refresh();
    }

    /* access modifiers changed from: package-private */
    public void setHasMedianLightness(float f) {
        this.mHaveAccurateLightness = true;
        this.mMedianLightness = f;
        refresh();
    }

    /* access modifiers changed from: package-private */
    public void onLightnessInvalidated() {
        this.mHaveAccurateLightness = false;
        refresh();
    }

    private void refresh() {
        if (!this.mHaveAccurateLightness || this.mIsDozing) {
            setRelativeAlpha(0.0f, false);
            return;
        }
        boolean z = this.mCardVisible;
        if (z && this.mCardForcesScrimGone) {
            setRelativeAlpha(0.0f, this.mCardTransitionAnimated);
        } else if (this.mInFullListening || this.mTranscriptionVisible) {
            if (!z || isVisible()) {
                setRelativeAlpha(1.0f, false);
            }
        } else if (z) {
            setRelativeAlpha(0.0f, this.mCardTransitionAnimated);
        } else {
            float f = this.mInvocationProgress;
            if (f > 0.0f) {
                setRelativeAlpha(Math.min(1.0f, f), false);
            } else {
                setRelativeAlpha(0.0f, true);
            }
        }
    }

    private void setRelativeAlpha(float f, boolean z) {
        if (!this.mHaveAccurateLightness && f > 0.0f) {
            return;
        }
        if (f < 0.0f || f > 1.0f) {
            Log.e("ScrimController", "Got unexpected alpha: " + f + ", ignoring");
            return;
        }
        if (this.mAlphaAnimator.isRunning()) {
            this.mAlphaAnimator.cancel();
        }
        if (f > 0.0f) {
            if (this.mScrimView.getVisibility() != 0) {
                this.mScrimView.setBackgroundTintList(ColorStateList.valueOf(this.mMedianLightness <= 0.4f ? -16777216 : -1));
                setVisibility(0);
            }
            if (z) {
                ValueAnimator createRelativeAlphaAnimator = createRelativeAlphaAnimator(f);
                this.mAlphaAnimator = createRelativeAlphaAnimator;
                createRelativeAlphaAnimator.start();
                return;
            }
            setAlpha(f);
        } else if (z) {
            ValueAnimator createRelativeAlphaAnimator2 = createRelativeAlphaAnimator(f);
            this.mAlphaAnimator = createRelativeAlphaAnimator2;
            createRelativeAlphaAnimator2.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.uihints.ScrimController.1
                private boolean mCancelled = false;

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    super.onAnimationCancel(animator);
                    this.mCancelled = true;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (!this.mCancelled) {
                        ScrimController.this.setVisibility(8);
                    }
                }
            });
            this.mAlphaAnimator.start();
        } else {
            setAlpha(0.0f);
            setVisibility(8);
        }
    }

    /* access modifiers changed from: private */
    public void setVisibility(int i) {
        if (i != this.mScrimView.getVisibility()) {
            this.mScrimView.setVisibility(i);
            VisibilityListener visibilityListener = this.mVisibilityListener;
            if (visibilityListener != null) {
                visibilityListener.onVisibilityChanged(i);
            }
            this.mLightnessProvider.setMuted(i == 0);
            View view = this.mScrimView;
            view.setBackground(i == 0 ? view.getContext().getDrawable(R$drawable.scrim_strip) : null);
            if (i != 0) {
                this.mOverlappedElement.setAlpha(1.0f);
                refresh();
            }
        }
    }

    private void setAlpha(float f) {
        this.mScrimView.setAlpha(f);
        this.mOverlappedElement.setAlpha(1.0f - f);
    }

    private ValueAnimator createRelativeAlphaAnimator(float f) {
        ValueAnimator duration = ValueAnimator.ofFloat(this.mScrimView.getAlpha(), f).setDuration((long) (Math.abs(f - this.mScrimView.getAlpha()) * 300.0f));
        duration.setInterpolator(ALPHA_INTERPOLATOR);
        duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.systemui.assist.uihints.ScrimController$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScrimController.this.lambda$createRelativeAlphaAnimator$0(valueAnimator);
            }
        });
        return duration;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createRelativeAlphaAnimator$0(ValueAnimator valueAnimator) {
        setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }
}
