package com.google.android.systemui.assist.uihints.edgelights;

import android.animation.ValueAnimator;
import android.util.MathUtils;
import com.android.systemui.assist.ui.EdgeLight;
/* loaded from: classes2.dex */
public final class EdgeLightUpdateListener implements ValueAnimator.AnimatorUpdateListener {
    private EdgeLight[] mFinalLights;
    private EdgeLight[] mInitialLights;
    private EdgeLight[] mLights;
    private EdgeLightsView mView;

    public EdgeLightUpdateListener(EdgeLight[] edgeLightArr, EdgeLight[] edgeLightArr2, EdgeLight[] edgeLightArr3, EdgeLightsView edgeLightsView) {
        if (edgeLightArr.length == edgeLightArr2.length && edgeLightArr3.length == edgeLightArr2.length) {
            this.mFinalLights = edgeLightArr2;
            this.mInitialLights = edgeLightArr;
            this.mLights = edgeLightArr3;
            this.mView = edgeLightsView;
            return;
        }
        throw new IllegalArgumentException("Lights arrays must be the same length");
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        int i = 0;
        while (true) {
            EdgeLight[] edgeLightArr = this.mLights;
            if (i < edgeLightArr.length) {
                this.mLights[i].setLength(MathUtils.lerp(this.mInitialLights[i].getLength(), this.mFinalLights[i].getLength(), animatedFraction));
                this.mLights[i].setStart(MathUtils.lerp(this.mInitialLights[i].getStart(), this.mFinalLights[i].getStart(), animatedFraction));
                i++;
            } else {
                this.mView.setAssistLights(edgeLightArr);
                return;
            }
        }
    }
}
