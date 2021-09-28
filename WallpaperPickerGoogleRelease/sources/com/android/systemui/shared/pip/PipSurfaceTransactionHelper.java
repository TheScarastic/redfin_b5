package com.android.systemui.shared.pip;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.Choreographer;
import android.view.SurfaceControl;
import android.window.PictureInPictureSurfaceTransaction;
/* loaded from: classes.dex */
public class PipSurfaceTransactionHelper {
    private final int mCornerRadius;
    private final Matrix mTmpTransform = new Matrix();
    private final float[] mTmpFloat9 = new float[9];
    private final RectF mTmpSourceRectF = new RectF();
    private final RectF mTmpDestinationRectF = new RectF();
    private final Rect mTmpDestinationRect = new Rect();

    public PipSurfaceTransactionHelper(int i) {
        this.mCornerRadius = i;
    }

    private float getScaledCornerRadius(Rect rect, Rect rect2) {
        return ((float) this.mCornerRadius) * ((float) (Math.hypot((double) rect.width(), (double) rect.height()) / Math.hypot((double) rect2.width(), (double) rect2.height())));
    }

    public static SurfaceControl.Transaction newSurfaceControlTransaction() {
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        transaction.setFrameTimelineVsync(Choreographer.getSfInstance().getVsyncId());
        return transaction;
    }

    public PictureInPictureSurfaceTransaction scale(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2) {
        float f = (float) rect2.left;
        float f2 = (float) rect2.top;
        this.mTmpSourceRectF.set(rect);
        this.mTmpDestinationRectF.set(rect2);
        this.mTmpDestinationRectF.offsetTo(0.0f, 0.0f);
        this.mTmpTransform.setRectToRect(this.mTmpSourceRectF, this.mTmpDestinationRectF, Matrix.ScaleToFit.FILL);
        float scaledCornerRadius = getScaledCornerRadius(rect, rect2);
        transaction.setMatrix(surfaceControl, this.mTmpTransform, this.mTmpFloat9).setPosition(surfaceControl, f, f2).setCornerRadius(surfaceControl, scaledCornerRadius);
        return new PictureInPictureSurfaceTransaction(f, f2, this.mTmpFloat9, 0.0f, scaledCornerRadius, rect);
    }

    public PictureInPictureSurfaceTransaction scaleAndCrop(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, Rect rect3) {
        int i;
        float f;
        this.mTmpSourceRectF.set(rect);
        this.mTmpDestinationRect.set(rect);
        this.mTmpDestinationRect.inset(rect3);
        if (rect.width() <= rect.height()) {
            f = (float) rect2.width();
            i = rect.width();
        } else {
            f = (float) rect2.height();
            i = rect.height();
        }
        float f2 = f / ((float) i);
        float f3 = ((float) rect2.left) - (((float) (rect3.left + rect.left)) * f2);
        float f4 = ((float) rect2.top) - (((float) (rect3.top + rect.top)) * f2);
        this.mTmpTransform.setScale(f2, f2);
        float scaledCornerRadius = getScaledCornerRadius(this.mTmpDestinationRect, rect2);
        transaction.setMatrix(surfaceControl, this.mTmpTransform, this.mTmpFloat9).setWindowCrop(surfaceControl, this.mTmpDestinationRect).setPosition(surfaceControl, f3, f4).setCornerRadius(surfaceControl, scaledCornerRadius);
        return new PictureInPictureSurfaceTransaction(f3, f4, this.mTmpFloat9, 0.0f, scaledCornerRadius, this.mTmpDestinationRect);
    }

    public PictureInPictureSurfaceTransaction scaleAndRotate(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, Rect rect3, float f, float f2, float f3) {
        float f4;
        int i;
        float f5;
        float f6;
        this.mTmpSourceRectF.set(rect);
        this.mTmpDestinationRect.set(rect);
        this.mTmpDestinationRect.inset(rect3);
        if (rect.width() <= rect.height()) {
            f4 = (float) rect2.width();
            i = rect.width();
        } else {
            f4 = (float) rect2.height();
            i = rect.height();
        }
        float f7 = f4 / ((float) i);
        this.mTmpTransform.setRotate(f, 0.0f, 0.0f);
        this.mTmpTransform.postScale(f7, f7);
        float scaledCornerRadius = getScaledCornerRadius(this.mTmpDestinationRect, rect2);
        if (f < 0.0f) {
            f6 = (((float) rect3.top) * f7) + f2;
            f5 = (((float) rect3.left) * f7) + f3;
        } else {
            f6 = f2 - (((float) rect3.top) * f7);
            f5 = f3 - (((float) rect3.left) * f7);
        }
        transaction.setMatrix(surfaceControl, this.mTmpTransform, this.mTmpFloat9).setWindowCrop(surfaceControl, this.mTmpDestinationRect).setPosition(surfaceControl, f6, f5).setCornerRadius(surfaceControl, scaledCornerRadius);
        return new PictureInPictureSurfaceTransaction(f6, f5, this.mTmpFloat9, f, scaledCornerRadius, this.mTmpDestinationRect);
    }

    public PictureInPictureSurfaceTransaction scale(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, float f, float f2, float f3) {
        this.mTmpSourceRectF.set(rect);
        this.mTmpDestinationRectF.set(rect2);
        this.mTmpDestinationRectF.offsetTo(0.0f, 0.0f);
        this.mTmpTransform.setRectToRect(this.mTmpSourceRectF, this.mTmpDestinationRectF, Matrix.ScaleToFit.FILL);
        this.mTmpTransform.postRotate(f, 0.0f, 0.0f);
        float scaledCornerRadius = getScaledCornerRadius(rect, rect2);
        transaction.setMatrix(surfaceControl, this.mTmpTransform, this.mTmpFloat9).setPosition(surfaceControl, f2, f3).setCornerRadius(surfaceControl, scaledCornerRadius);
        return new PictureInPictureSurfaceTransaction(f2, f3, this.mTmpFloat9, f, scaledCornerRadius, rect);
    }
}
