package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.ScaleXY;
import java.util.List;
/* loaded from: classes.dex */
public class ScaleKeyframeAnimation extends KeyframeAnimation<ScaleXY> {
    private final ScaleXY scaleXY = new ScaleXY();

    public ScaleKeyframeAnimation(List<Keyframe<ScaleXY>> list) {
        super(list);
    }

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation
    public ScaleXY getValue(Keyframe<ScaleXY> keyframe, float f) {
        ScaleXY scaleXY;
        ScaleXY scaleXY2;
        ScaleXY scaleXY3 = keyframe.startValue;
        if (scaleXY3 == null || (scaleXY = keyframe.endValue) == null) {
            throw new IllegalStateException("Missing values for keyframe.");
        }
        ScaleXY scaleXY4 = scaleXY3;
        ScaleXY scaleXY5 = scaleXY;
        LottieValueCallback<A> lottieValueCallback = this.valueCallback;
        if (lottieValueCallback != 0 && (scaleXY2 = (ScaleXY) lottieValueCallback.getValueInternal(keyframe.startFrame, keyframe.endFrame.floatValue(), scaleXY4, scaleXY5, f, getLinearCurrentKeyframeProgress(), getProgress())) != null) {
            return scaleXY2;
        }
        this.scaleXY.set(MiscUtils.lerp(scaleXY4.getScaleX(), scaleXY5.getScaleX(), f), MiscUtils.lerp(scaleXY4.getScaleY(), scaleXY5.getScaleY(), f));
        return this.scaleXY;
    }
}
