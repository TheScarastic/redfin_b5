package com.android.systemui.statusbar.charging;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ChargingRippleView.kt */
/* loaded from: classes.dex */
public final class ChargingRippleView extends View {
    private float radius;
    private boolean rippleInProgress;
    private final Paint ripplePaint;
    private final RippleShader rippleShader;
    private final int defaultColor = -1;
    private PointF origin = new PointF();
    private long duration = 1750;

    public final void startRipple() {
        startRipple$default(this, null, 1, null);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r0v0, resolved type: android.graphics.Paint */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v1, types: [com.android.systemui.statusbar.charging.RippleShader, android.graphics.Shader] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ChargingRippleView(android.content.Context r4, android.util.AttributeSet r5) {
        /*
            r3 = this;
            r3.<init>(r4, r5)
            com.android.systemui.statusbar.charging.RippleShader r4 = new com.android.systemui.statusbar.charging.RippleShader
            r4.<init>()
            r3.rippleShader = r4
            r5 = -1
            r3.defaultColor = r5
            android.graphics.Paint r0 = new android.graphics.Paint
            r0.<init>()
            r3.ripplePaint = r0
            android.graphics.PointF r1 = new android.graphics.PointF
            r1.<init>()
            r3.origin = r1
            r1 = 1750(0x6d6, double:8.646E-321)
            r3.duration = r1
            r4.setColor(r5)
            r5 = 0
            r4.setProgress(r5)
            r5 = 1050253722(0x3e99999a, float:0.3)
            r4.setSparkleStrength(r5)
            r0.setShader(r4)
            r4 = 8
            r3.setVisibility(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.charging.ChargingRippleView.<init>(android.content.Context, android.util.AttributeSet):void");
    }

    public final boolean getRippleInProgress() {
        return this.rippleInProgress;
    }

    public final void setRippleInProgress(boolean z) {
        this.rippleInProgress = z;
    }

    public final void setRadius(float f) {
        this.rippleShader.setRadius(f);
        this.radius = f;
    }

    public final void setOrigin(PointF pointF) {
        Intrinsics.checkNotNullParameter(pointF, "value");
        this.rippleShader.setOrigin(pointF);
        this.origin = pointF;
    }

    public final void setDuration(long j) {
        this.duration = j;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        this.rippleShader.setPixelDensity(getResources().getDisplayMetrics().density);
        super.onConfigurationChanged(configuration);
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        this.rippleShader.setPixelDensity(getResources().getDisplayMetrics().density);
        super.onAttachedToWindow();
    }

    public static /* synthetic */ void startRipple$default(ChargingRippleView chargingRippleView, Runnable runnable, int i, Object obj) {
        if ((i & 1) != 0) {
            runnable = null;
        }
        chargingRippleView.startRipple(runnable);
    }

    public final void startRipple(Runnable runnable) {
        if (!this.rippleInProgress) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.setDuration(this.duration);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.systemui.statusbar.charging.ChargingRippleView$startRipple$1
                final /* synthetic */ ChargingRippleView this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    long currentPlayTime = valueAnimator.getCurrentPlayTime();
                    Object animatedValue = valueAnimator.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                    float floatValue = ((Float) animatedValue).floatValue();
                    this.this$0.rippleShader.setProgress(floatValue);
                    this.this$0.rippleShader.setDistortionStrength(((float) 1) - floatValue);
                    this.this$0.rippleShader.setTime((float) currentPlayTime);
                    this.this$0.invalidate();
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter(this, runnable) { // from class: com.android.systemui.statusbar.charging.ChargingRippleView$startRipple$2
                final /* synthetic */ Runnable $onAnimationEnd;
                final /* synthetic */ ChargingRippleView this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$onAnimationEnd = r2;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    this.this$0.setRippleInProgress(false);
                    this.this$0.setVisibility(8);
                    Runnable runnable2 = this.$onAnimationEnd;
                    if (runnable2 != null) {
                        runnable2.run();
                    }
                }
            });
            ofFloat.start();
            setVisibility(0);
            this.rippleInProgress = true;
        }
    }

    public final void setColor(int i) {
        this.rippleShader.setColor(i);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float f = (float) 1;
        float progress = (f - (((f - this.rippleShader.getProgress()) * (f - this.rippleShader.getProgress())) * (f - this.rippleShader.getProgress()))) * this.radius * 1.5f;
        if (canvas != null) {
            PointF pointF = this.origin;
            canvas.drawCircle(pointF.x, pointF.y, progress, this.ripplePaint);
        }
    }
}
