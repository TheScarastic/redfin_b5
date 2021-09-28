package com.airbnb.lottie.animation.keyframe;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;
/* loaded from: classes.dex */
public class PathKeyframeAnimation extends KeyframeAnimation<PointF> {
    private PathKeyframe pathMeasureKeyframe;
    private final PointF point = new PointF();
    private final float[] pos = new float[2];
    private PathMeasure pathMeasure = new PathMeasure();

    public PathKeyframeAnimation(List<? extends Keyframe<PointF>> list) {
        super(list);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r5v0, resolved type: T */
    /* JADX DEBUG: Multi-variable search result rejected for r6v0, resolved type: T */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation
    public PointF getValue(Keyframe<PointF> keyframe, float f) {
        PointF pointF;
        PathKeyframe pathKeyframe = (PathKeyframe) keyframe;
        Path path = pathKeyframe.getPath();
        if (path == null) {
            return keyframe.startValue;
        }
        LottieValueCallback<A> lottieValueCallback = this.valueCallback;
        if (lottieValueCallback != 0 && (pointF = (PointF) lottieValueCallback.getValueInternal(pathKeyframe.startFrame, pathKeyframe.endFrame.floatValue(), pathKeyframe.startValue, pathKeyframe.endValue, getLinearCurrentKeyframeProgress(), f, getProgress())) != null) {
            return pointF;
        }
        if (this.pathMeasureKeyframe != pathKeyframe) {
            this.pathMeasure.setPath(path, false);
            this.pathMeasureKeyframe = pathKeyframe;
        }
        PathMeasure pathMeasure = this.pathMeasure;
        pathMeasure.getPosTan(f * pathMeasure.getLength(), this.pos, null);
        PointF pointF2 = this.point;
        float[] fArr = this.pos;
        pointF2.set(fArr[0], fArr[1]);
        return this.point;
    }
}
