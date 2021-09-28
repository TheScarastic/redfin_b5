package com.google.android.material.circularreveal;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import com.google.android.material.circularreveal.CircularRevealWidget;
import com.google.android.material.math.MathUtils;
/* loaded from: classes2.dex */
public class CircularRevealHelper {
    public static final int STRATEGY;
    private boolean buildingCircularRevealCache;
    private final Delegate delegate;
    private boolean hasCircularRevealCache;
    private Drawable overlayDrawable;
    private CircularRevealWidget.RevealInfo revealInfo;
    private final Paint scrimPaint;
    private final View view;
    private final Path revealPath = new Path();
    private final Paint revealPaint = new Paint(7);

    /* loaded from: classes2.dex */
    public interface Delegate {
        void actualDraw(Canvas canvas);

        boolean actualIsOpaque();
    }

    static {
        int i = Build.VERSION.SDK_INT;
        if (i >= 21) {
            STRATEGY = 2;
        } else if (i >= 18) {
            STRATEGY = 1;
        } else {
            STRATEGY = 0;
        }
    }

    public CircularRevealHelper(Delegate delegate) {
        this.delegate = delegate;
        View view = (View) delegate;
        this.view = view;
        view.setWillNotDraw(false);
        Paint paint = new Paint(1);
        this.scrimPaint = paint;
        paint.setColor(0);
    }

    public void buildCircularRevealCache() {
        if (STRATEGY == 0) {
            this.buildingCircularRevealCache = true;
            this.hasCircularRevealCache = false;
            this.view.buildDrawingCache();
            Bitmap drawingCache = this.view.getDrawingCache();
            if (!(drawingCache != null || this.view.getWidth() == 0 || this.view.getHeight() == 0)) {
                drawingCache = Bitmap.createBitmap(this.view.getWidth(), this.view.getHeight(), Bitmap.Config.ARGB_8888);
                this.view.draw(new Canvas(drawingCache));
            }
            if (drawingCache != null) {
                Paint paint = this.revealPaint;
                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                paint.setShader(new BitmapShader(drawingCache, tileMode, tileMode));
            }
            this.buildingCircularRevealCache = false;
            this.hasCircularRevealCache = true;
        }
    }

    public void destroyCircularRevealCache() {
        if (STRATEGY == 0) {
            this.hasCircularRevealCache = false;
            this.view.destroyDrawingCache();
            this.revealPaint.setShader(null);
            this.view.invalidate();
        }
    }

    public void setRevealInfo(CircularRevealWidget.RevealInfo revealInfo) {
        if (revealInfo == null) {
            this.revealInfo = null;
        } else {
            CircularRevealWidget.RevealInfo revealInfo2 = this.revealInfo;
            if (revealInfo2 == null) {
                this.revealInfo = new CircularRevealWidget.RevealInfo(revealInfo);
            } else {
                revealInfo2.set(revealInfo);
            }
            if (MathUtils.geq(revealInfo.radius, getDistanceToFurthestCorner(revealInfo), 1.0E-4f)) {
                this.revealInfo.radius = Float.MAX_VALUE;
            }
        }
        invalidateRevealInfo();
    }

    public CircularRevealWidget.RevealInfo getRevealInfo() {
        CircularRevealWidget.RevealInfo revealInfo = this.revealInfo;
        if (revealInfo == null) {
            return null;
        }
        CircularRevealWidget.RevealInfo revealInfo2 = new CircularRevealWidget.RevealInfo(revealInfo);
        if (revealInfo2.isInvalid()) {
            revealInfo2.radius = getDistanceToFurthestCorner(revealInfo2);
        }
        return revealInfo2;
    }

    public void setCircularRevealScrimColor(int i) {
        this.scrimPaint.setColor(i);
        this.view.invalidate();
    }

    public int getCircularRevealScrimColor() {
        return this.scrimPaint.getColor();
    }

    public void setCircularRevealOverlayDrawable(Drawable drawable) {
        this.overlayDrawable = drawable;
        this.view.invalidate();
    }

    private void invalidateRevealInfo() {
        if (STRATEGY == 1) {
            this.revealPath.rewind();
            CircularRevealWidget.RevealInfo revealInfo = this.revealInfo;
            if (revealInfo != null) {
                this.revealPath.addCircle(revealInfo.centerX, revealInfo.centerY, revealInfo.radius, Path.Direction.CW);
            }
        }
        this.view.invalidate();
    }

    private float getDistanceToFurthestCorner(CircularRevealWidget.RevealInfo revealInfo) {
        return MathUtils.distanceToFurthestCorner(revealInfo.centerX, revealInfo.centerY, 0.0f, 0.0f, (float) this.view.getWidth(), (float) this.view.getHeight());
    }

    public void draw(Canvas canvas) {
        if (shouldDrawCircularReveal()) {
            int i = STRATEGY;
            if (i == 0) {
                CircularRevealWidget.RevealInfo revealInfo = this.revealInfo;
                canvas.drawCircle(revealInfo.centerX, revealInfo.centerY, revealInfo.radius, this.revealPaint);
                if (shouldDrawScrim()) {
                    CircularRevealWidget.RevealInfo revealInfo2 = this.revealInfo;
                    canvas.drawCircle(revealInfo2.centerX, revealInfo2.centerY, revealInfo2.radius, this.scrimPaint);
                }
            } else if (i == 1) {
                int save = canvas.save();
                canvas.clipPath(this.revealPath);
                this.delegate.actualDraw(canvas);
                if (shouldDrawScrim()) {
                    canvas.drawRect(0.0f, 0.0f, (float) this.view.getWidth(), (float) this.view.getHeight(), this.scrimPaint);
                }
                canvas.restoreToCount(save);
            } else if (i == 2) {
                this.delegate.actualDraw(canvas);
                if (shouldDrawScrim()) {
                    canvas.drawRect(0.0f, 0.0f, (float) this.view.getWidth(), (float) this.view.getHeight(), this.scrimPaint);
                }
            } else {
                throw new IllegalStateException("Unsupported strategy " + i);
            }
        } else {
            this.delegate.actualDraw(canvas);
            if (shouldDrawScrim()) {
                canvas.drawRect(0.0f, 0.0f, (float) this.view.getWidth(), (float) this.view.getHeight(), this.scrimPaint);
            }
        }
        drawOverlayDrawable(canvas);
    }

    private void drawOverlayDrawable(Canvas canvas) {
        if (shouldDrawOverlayDrawable()) {
            Rect bounds = this.overlayDrawable.getBounds();
            float width = this.revealInfo.centerX - (((float) bounds.width()) / 2.0f);
            float height = this.revealInfo.centerY - (((float) bounds.height()) / 2.0f);
            canvas.translate(width, height);
            this.overlayDrawable.draw(canvas);
            canvas.translate(-width, -height);
        }
    }

    public boolean isOpaque() {
        return this.delegate.actualIsOpaque() && !shouldDrawCircularReveal();
    }

    private boolean shouldDrawCircularReveal() {
        CircularRevealWidget.RevealInfo revealInfo = this.revealInfo;
        boolean z = revealInfo == null || revealInfo.isInvalid();
        if (STRATEGY == 0) {
            return !z && this.hasCircularRevealCache;
        }
        return !z;
    }

    private boolean shouldDrawScrim() {
        return !this.buildingCircularRevealCache && Color.alpha(this.scrimPaint.getColor()) != 0;
    }

    private boolean shouldDrawOverlayDrawable() {
        return (this.buildingCircularRevealCache || this.overlayDrawable == null || this.revealInfo == null) ? false : true;
    }
}
