package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/* loaded from: classes.dex */
public class UdfpsSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    boolean mAwaitingSurfaceToStartIllumination;
    private GhbmIlluminationListener mGhbmIlluminationListener;
    boolean mHasValidSurface;
    private final SurfaceHolder mHolder;
    private Runnable mOnIlluminatedRunnable;
    private final Paint mSensorPaint;

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface GhbmIlluminationListener {
        void enableGhbm(Surface surface, Runnable runnable);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public UdfpsSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setZOrderOnTop(true);
        SurfaceHolder holder = getHolder();
        this.mHolder = holder;
        holder.addCallback(this);
        holder.setFormat(1);
        Paint paint = new Paint(0);
        this.mSensorPaint = paint;
        paint.setAntiAlias(true);
        paint.setARGB(255, 255, 255, 255);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mHasValidSurface = true;
        if (this.mAwaitingSurfaceToStartIllumination) {
            doIlluminate(this.mOnIlluminatedRunnable);
            this.mOnIlluminatedRunnable = null;
            this.mAwaitingSurfaceToStartIllumination = false;
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.mHasValidSurface = false;
    }

    /* access modifiers changed from: package-private */
    public void setGhbmIlluminationListener(GhbmIlluminationListener ghbmIlluminationListener) {
        this.mGhbmIlluminationListener = ghbmIlluminationListener;
    }

    /* access modifiers changed from: package-private */
    public void startGhbmIllumination(Runnable runnable) {
        if (this.mGhbmIlluminationListener == null) {
            Log.e("UdfpsSurfaceView", "startIllumination | mGhbmIlluminationListener is null");
        } else if (this.mHasValidSurface) {
            doIlluminate(runnable);
        } else {
            this.mAwaitingSurfaceToStartIllumination = true;
            this.mOnIlluminatedRunnable = runnable;
        }
    }

    private void doIlluminate(Runnable runnable) {
        GhbmIlluminationListener ghbmIlluminationListener = this.mGhbmIlluminationListener;
        if (ghbmIlluminationListener == null) {
            Log.e("UdfpsSurfaceView", "doIlluminate | mGhbmIlluminationListener is null");
        } else {
            ghbmIlluminationListener.enableGhbm(this.mHolder.getSurface(), runnable);
        }
    }

    /* access modifiers changed from: package-private */
    public void drawIlluminationDot(RectF rectF) {
        if (!this.mHasValidSurface) {
            Log.e("UdfpsSurfaceView", "drawIlluminationDot | the surface is destroyed or was never created.");
            return;
        }
        Canvas canvas = null;
        try {
            canvas = this.mHolder.lockCanvas();
            canvas.drawOval(rectF, this.mSensorPaint);
            this.mHolder.unlockCanvasAndPost(canvas);
        } catch (Throwable th) {
            if (canvas != null) {
                this.mHolder.unlockCanvasAndPost(canvas);
            }
            throw th;
        }
    }
}
