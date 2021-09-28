package com.google.android.apps.miphone.aiai.matchmaker.overview.ui;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public final class HighlightView extends FrameLayout {
    @Nullable
    private HighlightAccessibilityDelegate accessibilityDelegate;
    private float backgroundCornerRadius;
    private final Paint backgroundPaint;
    @Nullable
    private Path currentSelectionPath;
    private final float highlightCornerRadius;
    private final Paint highlightPaint;
    private float highlightProgress;
    private final ArrayList<RectF> highlights;
    private final List<TapListener> listeners;
    private float pathChangeProgress;
    @Nullable
    private Path previousSelectionPath;
    private final Paint selectionPaint;
    private final Rect tmpRect;
    private final RectF tmpRectF;
    private static final FloatProperty<HighlightView> HIGHLIGHT_PROGRESS = new FloatProperty<HighlightView>("highlightProgress") { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.HighlightView.1
        public void setValue(HighlightView highlightView, float f) {
            highlightView.setHighlightProgress(f);
        }

        public Float get(HighlightView highlightView) {
            return Float.valueOf(highlightView.getHighlightProgress());
        }
    };
    private static final FloatProperty<HighlightView> PATH_CHANGE_PROGRESS = new FloatProperty<HighlightView>("pathChangeProgress") { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.HighlightView.2
        public void setValue(HighlightView highlightView, float f) {
            highlightView.setPathChangeProgress(f);
        }

        public Float get(HighlightView highlightView) {
            return Float.valueOf(highlightView.getPathChangeProgress());
        }
    };
    private static final Interpolator HIGHLIGHT_INTERPOLATOR = new PathInterpolator(0.71f, 0.0f, 0.13f, 1.0f);

    /* loaded from: classes2.dex */
    public interface HighlightAccessibilityDelegate {
        boolean dispatchHoverEvent(MotionEvent motionEvent);

        boolean dispatchKeyEvent(KeyEvent keyEvent);

        void onFocusChanged(boolean z, int i, Rect rect);
    }

    /* loaded from: classes2.dex */
    public interface TapListener {
        void onTap(float f, float f2);
    }

    public HighlightView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public HighlightView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public HighlightView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.highlights = new ArrayList<>();
        this.listeners = new ArrayList();
        this.tmpRectF = new RectF();
        this.tmpRect = new Rect();
        this.highlightProgress = 0.0f;
        this.pathChangeProgress = 0.0f;
        Paint paint = new Paint();
        this.backgroundPaint = paint;
        paint.setStyle(Paint.Style.FILL);
        int i3 = Build.VERSION.SDK_INT;
        if (i3 >= 29) {
            paint.setColor(context.getColor(R$color.default_gleam_background_color));
        }
        float dimensionPixelSize = (float) context.getResources().getDimensionPixelSize(R$dimen.selection_padding);
        this.highlightCornerRadius = dimensionPixelSize;
        Paint paint2 = new Paint();
        this.selectionPaint = paint2;
        Paint paint3 = new Paint();
        this.highlightPaint = paint3;
        if (i3 >= 29) {
            paint3.setColor(context.getColor(R$color.default_gleam_highlight_color));
            paint2.setBlendMode(BlendMode.PLUS);
        }
        paint2.setColor(paint3.getColor());
        paint2.setStyle(Paint.Style.FILL_AND_STROKE);
        paint2.setStrokeJoin(Paint.Join.ROUND);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        paint2.setStrokeWidth(dimensionPixelSize * 2.0f);
        setWillNotDraw(false);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getHeight() != 0) {
            getDrawingRect(this.tmpRect);
            this.tmpRectF.set(this.tmpRect);
            RectF rectF = this.tmpRectF;
            float f = this.backgroundCornerRadius;
            canvas.drawRoundRect(rectF, f, f, this.backgroundPaint);
            float f2 = this.highlightProgress * 1.1f;
            for (int i = 0; i < this.highlights.size(); i++) {
                this.highlightPaint.setAlpha(Math.round(saturate((f2 - (this.highlights.get(i).top / ((float) getHeight()))) * 10.0f) * 255.0f));
                float f3 = this.highlightCornerRadius;
                canvas.drawRoundRect(this.highlights.get(i), f3, f3, this.highlightPaint);
            }
            if (this.previousSelectionPath != null) {
                this.selectionPaint.setAlpha((int) ((1.0f - this.pathChangeProgress) * 255.0f));
                canvas.drawPath(this.previousSelectionPath, this.selectionPaint);
            }
            if (this.currentSelectionPath != null) {
                this.selectionPaint.setAlpha((int) (this.pathChangeProgress * 255.0f));
                canvas.drawPath(this.currentSelectionPath, this.selectionPaint);
            }
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setOnTouchListener(new View.OnTouchListener() { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.HighlightView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return HighlightView.this.lambda$onAttachedToWindow$0$HighlightView(view, motionEvent);
            }
        });
        setAlpha(0.0f);
        animate().alpha(0.2f);
    }

    public /* synthetic */ boolean lambda$onAttachedToWindow$0$HighlightView(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() != 0) {
            return false;
        }
        performClick();
        handleTap(motionEvent.getX(), motionEvent.getY());
        return true;
    }

    public float getHighlightProgress() {
        return this.highlightProgress;
    }

    public void setHighlightProgress(float f) {
        this.highlightProgress = f;
        invalidate();
    }

    /* access modifiers changed from: private */
    public float getPathChangeProgress() {
        return this.pathChangeProgress;
    }

    /* access modifiers changed from: private */
    public void setPathChangeProgress(float f) {
        this.pathChangeProgress = f;
        invalidate();
    }

    @Override // android.view.View, android.view.ViewGroup
    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        HighlightAccessibilityDelegate highlightAccessibilityDelegate = this.accessibilityDelegate;
        if (highlightAccessibilityDelegate == null) {
            return super.dispatchHoverEvent(motionEvent);
        }
        return highlightAccessibilityDelegate.dispatchHoverEvent(motionEvent) || super.dispatchHoverEvent(motionEvent);
    }

    @Override // android.view.View, android.view.ViewGroup
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        HighlightAccessibilityDelegate highlightAccessibilityDelegate = this.accessibilityDelegate;
        if (highlightAccessibilityDelegate == null) {
            return super.dispatchKeyEvent(keyEvent);
        }
        return highlightAccessibilityDelegate.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
    }

    @Override // android.view.View
    public void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
        HighlightAccessibilityDelegate highlightAccessibilityDelegate = this.accessibilityDelegate;
        if (highlightAccessibilityDelegate != null) {
            highlightAccessibilityDelegate.onFocusChanged(z, i, rect);
        }
    }

    private static float saturate(float f) {
        if (f < 0.0f) {
            return 0.0f;
        }
        return Math.min(1.0f, f);
    }

    private void handleTap(float f, float f2) {
        for (TapListener tapListener : this.listeners) {
            tapListener.onTap(f, f2);
        }
    }
}
