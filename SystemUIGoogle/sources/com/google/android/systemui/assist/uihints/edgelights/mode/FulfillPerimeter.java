package com.google.android.systemui.assist.uihints.edgelights.mode;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.animation.PathInterpolator;
import com.android.systemui.R$color;
import com.android.systemui.assist.ui.EdgeLight;
import com.android.systemui.assist.ui.PerimeterPathGuide;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
/* loaded from: classes2.dex */
public final class FulfillPerimeter implements EdgeLightsView.Mode {
    private static final PathInterpolator FULFILL_PERIMETER_INTERPOLATOR = new PathInterpolator(0.2f, 0.0f, 0.2f, 1.0f);
    private final EdgeLight mBlueLight;
    private boolean mDisappearing = false;
    private final EdgeLight mGreenLight;
    private final EdgeLight[] mLights;
    private EdgeLightsView.Mode mNextMode;
    private final EdgeLight mRedLight;
    private final EdgeLight mYellowLight;

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public int getSubType() {
        return 4;
    }

    public FulfillPerimeter(Context context) {
        EdgeLight edgeLight = new EdgeLight(context.getResources().getColor(R$color.edge_light_blue, null), 0.0f, 0.0f);
        this.mBlueLight = edgeLight;
        EdgeLight edgeLight2 = new EdgeLight(context.getResources().getColor(R$color.edge_light_red, null), 0.0f, 0.0f);
        this.mRedLight = edgeLight2;
        EdgeLight edgeLight3 = new EdgeLight(context.getResources().getColor(R$color.edge_light_yellow, null), 0.0f, 0.0f);
        this.mYellowLight = edgeLight3;
        EdgeLight edgeLight4 = new EdgeLight(context.getResources().getColor(R$color.edge_light_green, null), 0.0f, 0.0f);
        this.mGreenLight = edgeLight4;
        this.mLights = new EdgeLight[]{edgeLight, edgeLight2, edgeLight4, edgeLight3};
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public void onNewModeRequest(EdgeLightsView edgeLightsView, EdgeLightsView.Mode mode) {
        this.mNextMode = mode;
    }

    @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView.Mode
    public void start(final EdgeLightsView edgeLightsView, PerimeterPathGuide perimeterPathGuide, EdgeLightsView.Mode mode) {
        PerimeterPathGuide perimeterPathGuide2 = perimeterPathGuide;
        boolean z = false;
        edgeLightsView.setVisibility(0);
        final AnimatorSet animatorSet = new AnimatorSet();
        EdgeLight[] edgeLightArr = this.mLights;
        int length = edgeLightArr.length;
        int i = 0;
        while (i < length) {
            EdgeLight edgeLight = edgeLightArr[i];
            boolean z2 = (edgeLight == this.mBlueLight || edgeLight == this.mRedLight) ? true : z;
            boolean z3 = (edgeLight == this.mRedLight || edgeLight == this.mYellowLight) ? true : z;
            PerimeterPathGuide.Region region = PerimeterPathGuide.Region.BOTTOM;
            float regionCenter = perimeterPathGuide2.getRegionCenter(region);
            float makeClockwise = (z2 ? PerimeterPathGuide.makeClockwise(perimeterPathGuide2.getRegionCenter(PerimeterPathGuide.Region.TOP)) : regionCenter) - regionCenter;
            float regionCenter2 = perimeterPathGuide2.getRegionCenter(PerimeterPathGuide.Region.TOP) - perimeterPathGuide2.getRegionCenter(region);
            float f = regionCenter2 - 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.setStartDelay(z3 ? 100 : 0);
            ofFloat.setDuration(433L);
            PathInterpolator pathInterpolator = FULFILL_PERIMETER_INTERPOLATOR;
            ofFloat.setInterpolator(pathInterpolator);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(edgeLight, makeClockwise, regionCenter, f, 0.0f, edgeLightsView) { // from class: com.google.android.systemui.assist.uihints.edgelights.mode.FulfillPerimeter$$ExternalSyntheticLambda0
                public final /* synthetic */ EdgeLight f$1;
                public final /* synthetic */ float f$2;
                public final /* synthetic */ float f$3;
                public final /* synthetic */ float f$4;
                public final /* synthetic */ float f$5;
                public final /* synthetic */ EdgeLightsView f$6;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                    this.f$4 = r5;
                    this.f$5 = r6;
                    this.f$6 = r7;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    FulfillPerimeter.this.lambda$start$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, valueAnimator);
                }
            });
            if (!z3) {
                animatorSet.play(ofFloat);
            } else {
                float interpolation = ofFloat.getInterpolator().getInterpolation(100.0f / ((float) ofFloat.getDuration())) * regionCenter2;
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat2.setStartDelay(ofFloat.getStartDelay() + 100);
                ofFloat2.setDuration(733L);
                ofFloat2.setInterpolator(pathInterpolator);
                ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(edgeLight, interpolation, perimeterPathGuide, edgeLightsView) { // from class: com.google.android.systemui.assist.uihints.edgelights.mode.FulfillPerimeter$$ExternalSyntheticLambda1
                    public final /* synthetic */ EdgeLight f$1;
                    public final /* synthetic */ float f$2;
                    public final /* synthetic */ PerimeterPathGuide f$3;
                    public final /* synthetic */ EdgeLightsView f$4;

                    {
                        this.f$1 = r2;
                        this.f$2 = r3;
                        this.f$3 = r4;
                        this.f$4 = r5;
                    }

                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        FulfillPerimeter.this.lambda$start$1(this.f$1, this.f$2, this.f$3, this.f$4, valueAnimator);
                    }
                });
                animatorSet.play(ofFloat);
                animatorSet.play(ofFloat2);
            }
            i++;
            perimeterPathGuide2 = perimeterPathGuide;
            z = false;
        }
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.uihints.edgelights.mode.FulfillPerimeter.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                if (FulfillPerimeter.this.mNextMode == null) {
                    FulfillPerimeter.this.mDisappearing = false;
                    animatorSet.start();
                } else if (FulfillPerimeter.this.mNextMode != null) {
                    new Handler().postDelayed(new FulfillPerimeter$1$$ExternalSyntheticLambda0(this, edgeLightsView), 500);
                }
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onAnimationEnd$0(EdgeLightsView edgeLightsView2) {
                edgeLightsView2.commitModeTransition(FulfillPerimeter.this.mNextMode);
            }
        });
        animatorSet.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$start$0(EdgeLight edgeLight, float f, float f2, float f3, float f4, EdgeLightsView edgeLightsView, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        edgeLight.setStart((f * animatedFraction) + f2);
        if (!this.mDisappearing) {
            edgeLight.setLength((f3 * animatedFraction) + f4);
        }
        edgeLightsView.setAssistLights(this.mLights);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$start$1(EdgeLight edgeLight, float f, PerimeterPathGuide perimeterPathGuide, EdgeLightsView edgeLightsView, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        if (animatedFraction != 0.0f) {
            this.mDisappearing = true;
            EdgeLight edgeLight2 = this.mRedLight;
            if (edgeLight == edgeLight2) {
                edgeLight2.setLength(Math.max(((0.0f - f) * animatedFraction) + f, 0.0f));
                EdgeLight edgeLight3 = this.mBlueLight;
                edgeLight3.setLength(Math.abs(edgeLight3.getStart()) - Math.abs(this.mRedLight.getStart()));
            } else {
                EdgeLight edgeLight4 = this.mYellowLight;
                if (edgeLight == edgeLight4) {
                    PerimeterPathGuide.Region region = PerimeterPathGuide.Region.BOTTOM;
                    edgeLight4.setStart((perimeterPathGuide.getRegionCenter(region) * 2.0f) - (this.mRedLight.getStart() + this.mRedLight.getLength()));
                    this.mYellowLight.setLength(this.mRedLight.getLength());
                    this.mGreenLight.setStart((perimeterPathGuide.getRegionCenter(region) * 2.0f) - (this.mBlueLight.getStart() + this.mBlueLight.getLength()));
                    this.mGreenLight.setLength(this.mBlueLight.getLength());
                }
            }
            edgeLightsView.setAssistLights(this.mLights);
        }
    }
}
