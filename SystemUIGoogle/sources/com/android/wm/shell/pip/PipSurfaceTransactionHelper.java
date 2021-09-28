package com.android.wm.shell.pip;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceControl;
import com.android.wm.shell.R;
/* loaded from: classes2.dex */
public class PipSurfaceTransactionHelper {
    private int mCornerRadius;
    private final Matrix mTmpTransform = new Matrix();
    private final float[] mTmpFloat9 = new float[9];
    private final RectF mTmpSourceRectF = new RectF();
    private final RectF mTmpDestinationRectF = new RectF();
    private final Rect mTmpDestinationRect = new Rect();

    /* loaded from: classes2.dex */
    public interface SurfaceControlTransactionFactory {
        SurfaceControl.Transaction getTransaction();
    }

    public void onDensityOrFontScaleChanged(Context context) {
        this.mCornerRadius = context.getResources().getDimensionPixelSize(R.dimen.pip_corner_radius);
    }

    public PipSurfaceTransactionHelper alpha(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f) {
        transaction.setAlpha(surfaceControl, f);
        return this;
    }

    public PipSurfaceTransactionHelper crop(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect) {
        transaction.setWindowCrop(surfaceControl, rect.width(), rect.height()).setPosition(surfaceControl, (float) rect.left, (float) rect.top);
        return this;
    }

    public PipSurfaceTransactionHelper scale(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2) {
        return scale(transaction, surfaceControl, rect, rect2, 0.0f);
    }

    public PipSurfaceTransactionHelper scale(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, float f) {
        this.mTmpSourceRectF.set(rect);
        this.mTmpSourceRectF.offsetTo(0.0f, 0.0f);
        this.mTmpDestinationRectF.set(rect2);
        this.mTmpTransform.setRectToRect(this.mTmpSourceRectF, this.mTmpDestinationRectF, Matrix.ScaleToFit.FILL);
        this.mTmpTransform.postRotate(f, this.mTmpDestinationRectF.centerX(), this.mTmpDestinationRectF.centerY());
        transaction.setMatrix(surfaceControl, this.mTmpTransform, this.mTmpFloat9);
        return this;
    }

    public PipSurfaceTransactionHelper scaleAndCrop(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, Rect rect3) {
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
        float f3 = ((float) rect2.top) - (((float) rect3.top) * f2);
        this.mTmpTransform.setScale(f2, f2);
        transaction.setMatrix(surfaceControl, this.mTmpTransform, this.mTmpFloat9).setWindowCrop(surfaceControl, this.mTmpDestinationRect).setPosition(surfaceControl, ((float) rect2.left) - (((float) rect3.left) * f2), f3);
        return this;
    }

    public PipSurfaceTransactionHelper rotateAndScaleWithCrop(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, Rect rect3, float f, float f2, float f3, boolean z, boolean z2) {
        float f4;
        float f5;
        int i;
        this.mTmpDestinationRect.set(rect);
        this.mTmpDestinationRect.inset(rect3);
        int width = this.mTmpDestinationRect.width();
        int height = this.mTmpDestinationRect.height();
        int width2 = rect2.width();
        int height2 = rect2.height();
        float f6 = width <= height ? ((float) width2) / ((float) width) : ((float) height2) / ((float) height);
        Rect rect4 = this.mTmpDestinationRect;
        rect4.set(0, 0, width2, height2);
        rect4.scale(1.0f / f6);
        rect4.offset(rect3.left, rect3.top);
        if (z) {
            f5 = f2 - (((float) rect3.left) * f6);
            i = rect3.top;
        } else if (z2) {
            f5 = f2 - (((float) rect3.top) * f6);
            f4 = f3 + (((float) rect3.left) * f6);
            this.mTmpTransform.setScale(f6, f6);
            this.mTmpTransform.postRotate(f);
            this.mTmpTransform.postTranslate(f5, f4);
            transaction.setMatrix(surfaceControl, this.mTmpTransform, this.mTmpFloat9).setWindowCrop(surfaceControl, rect4);
            return this;
        } else {
            f5 = f2 + (((float) rect3.top) * f6);
            i = rect3.left;
        }
        f4 = f3 - (((float) i) * f6);
        this.mTmpTransform.setScale(f6, f6);
        this.mTmpTransform.postRotate(f);
        this.mTmpTransform.postTranslate(f5, f4);
        transaction.setMatrix(surfaceControl, this.mTmpTransform, this.mTmpFloat9).setWindowCrop(surfaceControl, rect4);
        return this;
    }

    public PipSurfaceTransactionHelper resetScale(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect) {
        transaction.setMatrix(surfaceControl, Matrix.IDENTITY_MATRIX, this.mTmpFloat9).setPosition(surfaceControl, (float) rect.left, (float) rect.top);
        return this;
    }

    public PipSurfaceTransactionHelper round(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, boolean z) {
        transaction.setCornerRadius(surfaceControl, z ? (float) this.mCornerRadius : 0.0f);
        return this;
    }

    public PipSurfaceTransactionHelper round(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2) {
        transaction.setCornerRadius(surfaceControl, ((float) this.mCornerRadius) * ((float) (Math.hypot((double) rect.width(), (double) rect.height()) / Math.hypot((double) rect2.width(), (double) rect2.height()))));
        return this;
    }
}
