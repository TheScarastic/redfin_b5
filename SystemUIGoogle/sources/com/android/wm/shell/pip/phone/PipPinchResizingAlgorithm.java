package com.android.wm.shell.pip.phone;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
/* loaded from: classes2.dex */
public class PipPinchResizingAlgorithm {
    private final PointF mTmpDownVector = new PointF();
    private final PointF mTmpLastVector = new PointF();
    private final PointF mTmpDownCentroid = new PointF();
    private final PointF mTmpLastCentroid = new PointF();

    private float overRotateInfluenceCurve(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2) + 1.0f;
    }

    public float calculateBoundsAndAngle(PointF pointF, PointF pointF2, PointF pointF3, PointF pointF4, Point point, Point point2, Rect rect, Rect rect2) {
        float hypot = (float) Math.hypot((double) (pointF4.x - pointF3.x), (double) (pointF4.y - pointF3.y));
        float minScale = getMinScale(rect, point);
        float maxScale = getMaxScale(rect, point2);
        float hypot2 = hypot / ((float) Math.hypot((double) (pointF2.x - pointF.x), (double) (pointF2.y - pointF.y)));
        float f = minScale - hypot2;
        float f2 = 0.0f;
        if (f <= 0.0f) {
            f = 0.0f;
        }
        float f3 = hypot2 - maxScale;
        if (f3 > 0.0f) {
            f2 = f3;
        }
        float max = Math.max(minScale - (f * 0.25f), Math.min(maxScale + (f2 * 0.25f), hypot2));
        rect2.set(rect);
        scaleRectAboutCenter(rect2, max);
        getCentroid(pointF, pointF2, this.mTmpDownCentroid);
        getCentroid(pointF3, pointF4, this.mTmpLastCentroid);
        PointF pointF5 = this.mTmpLastCentroid;
        float f4 = pointF5.x;
        PointF pointF6 = this.mTmpDownCentroid;
        rect2.offset((int) (f4 - pointF6.x), (int) (pointF5.y - pointF6.y));
        this.mTmpDownVector.set(pointF2.x - pointF.x, pointF2.y - pointF.y);
        this.mTmpLastVector.set(pointF4.x - pointF3.x, pointF4.y - pointF3.y);
        return constrainRotationAngle((float) Math.toDegrees((double) ((float) Math.atan2((double) cross(this.mTmpDownVector, this.mTmpLastVector), (double) dot(this.mTmpDownVector, this.mTmpLastVector)))));
    }

    private float getMinScale(Rect rect, Point point) {
        return Math.max(((float) point.x) / ((float) rect.width()), ((float) point.y) / ((float) rect.height()));
    }

    private float getMaxScale(Rect rect, Point point) {
        return Math.min(((float) point.x) / ((float) rect.width()), ((float) point.y) / ((float) rect.height()));
    }

    private float constrainRotationAngle(float f) {
        return Math.signum(f) * Math.max(0.0f, Math.abs(dampedRotate(f)) - 5.0f);
    }

    private float dampedRotate(float f) {
        if (Float.compare(f, 0.0f) == 0) {
            return 0.0f;
        }
        float f2 = f / 45.0f;
        float abs = (f2 / Math.abs(f2)) * overRotateInfluenceCurve(Math.abs(f2));
        if (Math.abs(abs) >= 1.0f) {
            abs /= Math.abs(abs);
        }
        return abs * 0.4f * 45.0f;
    }

    private void getCentroid(PointF pointF, PointF pointF2, PointF pointF3) {
        pointF3.set((pointF2.x + pointF.x) / 2.0f, (pointF2.y + pointF.y) / 2.0f);
    }

    private float dot(PointF pointF, PointF pointF2) {
        return (pointF.x * pointF2.x) + (pointF.y * pointF2.y);
    }

    private float cross(PointF pointF, PointF pointF2) {
        return (pointF.x * pointF2.y) - (pointF.y * pointF2.x);
    }

    private void scaleRectAboutCenter(Rect rect, float f) {
        if (f != 1.0f) {
            int centerX = rect.centerX();
            int centerY = rect.centerY();
            rect.offset(-centerX, -centerY);
            rect.scale(f);
            rect.offset(centerX, centerY);
        }
    }
}
