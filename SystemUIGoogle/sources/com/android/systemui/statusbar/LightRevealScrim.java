package com.android.systemui.statusbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LightRevealScrim.kt */
/* loaded from: classes.dex */
public final class LightRevealScrim extends View {
    private final Paint gradientPaint;
    public Consumer<Float> revealAmountListener;
    private float revealGradientEndColorAlpha;
    private float revealGradientHeight;
    private float revealGradientWidth;
    private float revealAmount = 1.0f;
    private LightRevealEffect revealEffect = LiftReveal.INSTANCE;
    private PointF revealGradientCenter = new PointF();
    private int revealGradientEndColor = -16777216;
    private final Matrix shaderGradientMatrix = new Matrix();

    public LightRevealScrim(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        paint.setShader(new RadialGradient(0.0f, 0.0f, 1.0f, new int[]{0, -1}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        Unit unit = Unit.INSTANCE;
        this.gradientPaint = paint;
        this.revealEffect.setRevealAmountOnScrim(this.revealAmount, this);
        setPaintColorFilter();
        invalidate();
    }

    public final Consumer<Float> getRevealAmountListener() {
        Consumer<Float> consumer = this.revealAmountListener;
        if (consumer != null) {
            return consumer;
        }
        Intrinsics.throwUninitializedPropertyAccessException("revealAmountListener");
        throw null;
    }

    public final void setRevealAmountListener(Consumer<Float> consumer) {
        Intrinsics.checkNotNullParameter(consumer, "<set-?>");
        this.revealAmountListener = consumer;
    }

    public final float getRevealAmount() {
        return this.revealAmount;
    }

    public final void setRevealAmount(float f) {
        if (!(this.revealAmount == f)) {
            this.revealAmount = f;
            this.revealEffect.setRevealAmountOnScrim(f, this);
            getRevealAmountListener().accept(Float.valueOf(f));
            invalidate();
        }
    }

    public final LightRevealEffect getRevealEffect() {
        return this.revealEffect;
    }

    public final void setRevealEffect(LightRevealEffect lightRevealEffect) {
        Intrinsics.checkNotNullParameter(lightRevealEffect, "value");
        if (!Intrinsics.areEqual(this.revealEffect, lightRevealEffect)) {
            this.revealEffect = lightRevealEffect;
            lightRevealEffect.setRevealAmountOnScrim(this.revealAmount, this);
            invalidate();
        }
    }

    public final PointF getRevealGradientCenter() {
        return this.revealGradientCenter;
    }

    public final float getRevealGradientWidth() {
        return this.revealGradientWidth;
    }

    public final float getRevealGradientHeight() {
        return this.revealGradientHeight;
    }

    public final void setRevealGradientEndColorAlpha(float f) {
        if (!(this.revealGradientEndColorAlpha == f)) {
            this.revealGradientEndColorAlpha = f;
            setPaintColorFilter();
        }
    }

    public final void setRevealGradientBounds(float f, float f2, float f3, float f4) {
        float f5 = f3 - f;
        this.revealGradientWidth = f5;
        float f6 = f4 - f2;
        this.revealGradientHeight = f6;
        PointF pointF = this.revealGradientCenter;
        pointF.x = f + (f5 / 2.0f);
        pointF.y = f2 + (f6 / 2.0f);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (canvas != null && this.revealGradientWidth > 0.0f && this.revealGradientHeight > 0.0f) {
            Matrix matrix = this.shaderGradientMatrix;
            matrix.setScale(getRevealGradientWidth(), getRevealGradientHeight(), 0.0f, 0.0f);
            matrix.postTranslate(getRevealGradientCenter().x, getRevealGradientCenter().y);
            this.gradientPaint.getShader().setLocalMatrix(matrix);
            canvas.drawRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), this.gradientPaint);
        } else if (this.revealAmount < 1.0f && canvas != null) {
            canvas.drawColor(this.revealGradientEndColor);
        }
    }

    private final void setPaintColorFilter() {
        this.gradientPaint.setColorFilter(new PorterDuffColorFilter(Color.argb((int) (this.revealGradientEndColorAlpha * ((float) 255)), Color.red(this.revealGradientEndColor), Color.green(this.revealGradientEndColor), Color.blue(this.revealGradientEndColor)), PorterDuff.Mode.MULTIPLY));
    }
}
