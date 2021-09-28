package com.google.android.systemui.assist.uihints.edgelights.mode;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.MathUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
import com.android.systemui.R$color;
import com.android.systemui.assist.ui.EdgeLight;
import com.android.systemui.assist.ui.PerimeterPathGuide;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightUpdateListener;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import java.util.Random;
/* loaded from: classes2.dex */
public final class FulfillBottom implements EdgeLightsView.Mode {
    private EdgeLight mBlueLight;
    private EdgeLight mGreenLight;
    private final boolean mIsListening;
    private EdgeLight[] mLightsArray;
    private EdgeLight mRedLight;
    private final Resources mResources;
    private EdgeLight mYellowLight;
    private static final PathInterpolator CRADLE_INTERPOLATOR = new PathInterpolator(0.4f, 0.0f, 0.6f, 1.0f);
    private static final PathInterpolator EXIT_TO_CORNER_INTERPOLATOR = new PathInterpolator(0.1f, 0.0f, 0.5f, 1.0f);
    private static final LinearInterpolator EXIT_FADE_INTERPOLATOR = new LinearInterpolator();
    private final Random mRandom = new Random();
    private AnimatorSet mExitAnimations = new AnimatorSet();
    private AnimatorSet mCradleAnimations = new AnimatorSet();
    private EdgeLightsView mEdgeLightsView = null;
    private PerimeterPathGuide mGuide = null;
    private EdgeLightsView.Mode mNextMode = null;
    private boolean mSwingLeft = false;

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public int getSubType() {
        return 3;
    }

    public FulfillBottom(Context context, boolean z) {
        this.mResources = context.getResources();
        this.mIsListening = z;
    }

    public boolean isListening() {
        return this.mIsListening;
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public void onNewModeRequest(EdgeLightsView edgeLightsView, EdgeLightsView.Mode mode) {
        this.mNextMode = mode;
        if (this.mCradleAnimations.isRunning()) {
            this.mCradleAnimations.cancel();
        }
        Log.v("FulfillBottom", "got mode " + mode.getClass().getSimpleName());
        if (!(mode instanceof Gone)) {
            if (this.mExitAnimations.isRunning()) {
                this.mExitAnimations.cancel();
            }
            this.mEdgeLightsView.commitModeTransition(this.mNextMode);
        } else if (!this.mExitAnimations.isRunning()) {
            animateExit();
        }
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public void start(EdgeLightsView edgeLightsView, PerimeterPathGuide perimeterPathGuide, EdgeLightsView.Mode mode) {
        this.mEdgeLightsView = edgeLightsView;
        this.mGuide = perimeterPathGuide;
        edgeLightsView.setVisibility(0);
        EdgeLight[] assistLights = edgeLightsView.getAssistLights();
        if (((mode instanceof FullListening) || (mode instanceof FulfillBottom)) && assistLights.length == 4) {
            this.mBlueLight = assistLights[0];
            this.mRedLight = assistLights[1];
            this.mYellowLight = assistLights[2];
            this.mGreenLight = assistLights[3];
        } else {
            this.mBlueLight = new EdgeLight(this.mResources.getColor(R$color.edge_light_blue, null), 0.0f, 0.0f);
            this.mRedLight = new EdgeLight(this.mResources.getColor(R$color.edge_light_red, null), 0.0f, 0.0f);
            this.mYellowLight = new EdgeLight(this.mResources.getColor(R$color.edge_light_yellow, null), 0.0f, 0.0f);
            this.mGreenLight = new EdgeLight(this.mResources.getColor(R$color.edge_light_green, null), 0.0f, 0.0f);
        }
        this.mLightsArray = new EdgeLight[]{this.mBlueLight, this.mRedLight, this.mYellowLight, this.mGreenLight};
        this.mSwingLeft = mode instanceof FulfillBottom ? ((FulfillBottom) mode).swingingToLeft() : this.mRandom.nextBoolean();
        animateCradle();
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public void onConfigurationChanged() {
        if (this.mNextMode == null) {
            start(this.mEdgeLightsView, this.mGuide, this);
            return;
        }
        if (this.mExitAnimations.isRunning()) {
            this.mExitAnimations.cancel();
        }
        onNewModeRequest(this.mEdgeLightsView, this.mNextMode);
    }

    private void setRelativePoints(float f, float f2, float f3) {
        float regionWidth = this.mGuide.getRegionWidth(PerimeterPathGuide.Region.BOTTOM);
        float f4 = f * regionWidth;
        this.mBlueLight.setEndpoints(0.0f, f4);
        float f5 = f2 * regionWidth;
        this.mRedLight.setEndpoints(f4, f5);
        float f6 = f3 * regionWidth;
        this.mYellowLight.setEndpoints(f5, f6);
        this.mGreenLight.setEndpoints(f6, regionWidth);
        this.mEdgeLightsView.setAssistLights(this.mLightsArray);
    }

    private void animateCradle() {
        float regionWidth = this.mGuide.getRegionWidth(PerimeterPathGuide.Region.BOTTOM);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this.mBlueLight.getEnd() / regionWidth, this.mRedLight.getEnd() / regionWidth, this.mYellowLight.getEnd() / regionWidth) { // from class: com.google.android.systemui.assist.uihints.edgelights.mode.FulfillBottom$$ExternalSyntheticLambda2
            public final /* synthetic */ float f$1;
            public final /* synthetic */ float f$2;
            public final /* synthetic */ float f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                FulfillBottom.this.lambda$animateCradle$0(this.f$1, this.f$2, this.f$3, valueAnimator);
            }
        });
        ofFloat.setDuration(1000L);
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.systemui.assist.uihints.edgelights.mode.FulfillBottom$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                FulfillBottom.this.lambda$animateCradle$1(valueAnimator);
            }
        });
        ofFloat2.setDuration(1300L);
        ofFloat2.setInterpolator(CRADLE_INTERPOLATOR);
        ofFloat2.setRepeatMode(2);
        ofFloat2.setRepeatCount(-1);
        AnimatorSet animatorSet = new AnimatorSet();
        this.mCradleAnimations = animatorSet;
        animatorSet.playSequentially(ofFloat, ofFloat2);
        this.mCradleAnimations.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateCradle$0(float f, float f2, float f3, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        setRelativePoints(MathUtils.lerp(f, this.mSwingLeft ? 0.69f : 0.035f, animatedFraction), MathUtils.lerp(f2, this.mSwingLeft ? 0.87f : 0.13f, animatedFraction), MathUtils.lerp(f3, this.mSwingLeft ? 0.965f : 0.31f, animatedFraction));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateCradle$1(ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        if (!this.mSwingLeft) {
            animatedFraction = 1.0f - animatedFraction;
        }
        setRelativePoints(MathUtils.lerp(0.69f, 0.035f, animatedFraction), MathUtils.lerp(0.87f, 0.13f, animatedFraction), MathUtils.lerp(0.965f, 0.31f, animatedFraction));
    }

    private boolean swingingToLeft() {
        return this.mSwingLeft;
    }

    private void animateExit() {
        ValueAnimator createToCornersAnimator = createToCornersAnimator();
        ValueAnimator createFadeOutAnimator = createFadeOutAnimator();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(createToCornersAnimator);
        animatorSet.play(createFadeOutAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.uihints.edgelights.mode.FulfillBottom.1
            private boolean mCancelled = false;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                FulfillBottom.this.mEdgeLightsView.setVisibility(8);
                if (FulfillBottom.this.mNextMode != null && !this.mCancelled) {
                    FulfillBottom.this.mEdgeLightsView.commitModeTransition(FulfillBottom.this.mNextMode);
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                this.mCancelled = true;
            }
        });
        this.mExitAnimations = animatorSet;
        animatorSet.start();
    }

    private ValueAnimator createToCornersAnimator() {
        EdgeLight[] copy = EdgeLight.copy(this.mLightsArray);
        EdgeLight[] copy2 = EdgeLight.copy(this.mLightsArray);
        float regionWidth = this.mGuide.getRegionWidth(PerimeterPathGuide.Region.BOTTOM_LEFT) * 0.8f;
        float f = -1.0f * regionWidth;
        float regionWidth2 = this.mGuide.getRegionWidth(PerimeterPathGuide.Region.BOTTOM);
        copy2[0].setEndpoints(f, f);
        copy2[1].setEndpoints(f, f);
        float f2 = regionWidth2 + regionWidth;
        copy2[2].setEndpoints(f2, f2);
        copy2[3].setEndpoints(f2, f2);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setInterpolator(EXIT_TO_CORNER_INTERPOLATOR);
        ofFloat.setDuration(350L);
        ofFloat.addUpdateListener(new EdgeLightUpdateListener(copy, copy2, this.mLightsArray, this.mEdgeLightsView));
        return ofFloat;
    }

    private ValueAnimator createFadeOutAnimator() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
        ofFloat.setInterpolator(EXIT_FADE_INTERPOLATOR);
        ofFloat.setDuration(350L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.systemui.assist.uihints.edgelights.mode.FulfillBottom$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                FulfillBottom.this.lambda$createFadeOutAnimator$2(valueAnimator);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.uihints.edgelights.mode.FulfillBottom.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                FulfillBottom.this.mEdgeLightsView.setAssistLights(new EdgeLight[0]);
                FulfillBottom.this.mEdgeLightsView.setAlpha(1.0f);
            }
        });
        return ofFloat;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createFadeOutAnimator$2(ValueAnimator valueAnimator) {
        this.mEdgeLightsView.setAlpha(1.0f - valueAnimator.getAnimatedFraction());
    }
}
