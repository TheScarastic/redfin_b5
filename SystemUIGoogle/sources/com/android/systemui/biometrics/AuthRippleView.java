package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;
import android.view.animation.PathInterpolator;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.charging.RippleShader;
import java.util.Objects;
import kotlin.comparisons.ComparisonsKt___ComparisonsJvmKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AuthRippleView.kt */
/* loaded from: classes.dex */
public final class AuthRippleView extends View {
    private PointF origin = new PointF();
    private float radius;
    private boolean rippleInProgress;
    private final Paint ripplePaint;
    private final RippleShader rippleShader;

    /* JADX DEBUG: Multi-variable search result rejected for r3v1, resolved type: android.graphics.Paint */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.statusbar.charging.RippleShader, android.graphics.Shader] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public AuthRippleView(android.content.Context r2, android.util.AttributeSet r3) {
        /*
            r1 = this;
            r1.<init>(r2, r3)
            com.android.systemui.statusbar.charging.RippleShader r2 = new com.android.systemui.statusbar.charging.RippleShader
            r2.<init>()
            r1.rippleShader = r2
            android.graphics.Paint r3 = new android.graphics.Paint
            r3.<init>()
            r1.ripplePaint = r3
            android.graphics.PointF r0 = new android.graphics.PointF
            r0.<init>()
            r1.origin = r0
            r0 = -1
            r2.setColor(r0)
            r0 = 0
            r2.setProgress(r0)
            r0 = 1053609165(0x3ecccccd, float:0.4)
            r2.setSparkleStrength(r0)
            r3.setShader(r2)
            r2 = 8
            r1.setVisibility(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.AuthRippleView.<init>(android.content.Context, android.util.AttributeSet):void");
    }

    private final void setRadius(float f) {
        this.rippleShader.setRadius(f);
        this.radius = f;
    }

    private final void setOrigin(PointF pointF) {
        this.rippleShader.setOrigin(pointF);
        this.origin = pointF;
    }

    public final void setSensorLocation(PointF pointF) {
        Intrinsics.checkNotNullParameter(pointF, "location");
        setOrigin(pointF);
        setRadius(ComparisonsKt___ComparisonsJvmKt.maxOf(pointF.x, pointF.y, ((float) getWidth()) - pointF.x, ((float) getHeight()) - pointF.y));
    }

    public final void startRipple(Runnable runnable, LightRevealScrim lightRevealScrim) {
        if (!this.rippleInProgress) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.setInterpolator(new PathInterpolator(0.4f, 0.0f, 0.0f, 1.0f));
            ofFloat.setDuration(1533L);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, lightRevealScrim) { // from class: com.android.systemui.biometrics.AuthRippleView$startRipple$rippleAnimator$1$1
                final /* synthetic */ LightRevealScrim $lightReveal;
                final /* synthetic */ AuthRippleView this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$lightReveal = r2;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    long currentPlayTime = valueAnimator.getCurrentPlayTime();
                    RippleShader rippleShader = this.this$0.rippleShader;
                    Object animatedValue = valueAnimator.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                    rippleShader.setProgress(((Float) animatedValue).floatValue());
                    this.this$0.rippleShader.setTime((float) currentPlayTime);
                    LightRevealScrim lightRevealScrim2 = this.$lightReveal;
                    if (lightRevealScrim2 != null) {
                        Object animatedValue2 = valueAnimator.getAnimatedValue();
                        Objects.requireNonNull(animatedValue2, "null cannot be cast to non-null type kotlin.Float");
                        lightRevealScrim2.setRevealAmount(((Float) animatedValue2).floatValue());
                    }
                    this.this$0.invalidate();
                }
            });
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat2.setInterpolator(ofFloat.getInterpolator());
            ofFloat2.setStartDelay(10);
            ofFloat2.setDuration(ofFloat.getDuration());
            ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(lightRevealScrim) { // from class: com.android.systemui.biometrics.AuthRippleView$startRipple$revealAnimator$1$1
                final /* synthetic */ LightRevealScrim $lightReveal;

                /* access modifiers changed from: package-private */
                {
                    this.$lightReveal = r1;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    LightRevealScrim lightRevealScrim2 = this.$lightReveal;
                    if (lightRevealScrim2 != null) {
                        Object animatedValue = valueAnimator.getAnimatedValue();
                        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                        lightRevealScrim2.setRevealAmount(((Float) animatedValue).floatValue());
                    }
                }
            });
            ValueAnimator ofInt = ValueAnimator.ofInt(0, 127);
            ofInt.setDuration(167L);
            ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.systemui.biometrics.AuthRippleView$startRipple$alphaInAnimator$1$1
                final /* synthetic */ AuthRippleView this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    RippleShader rippleShader = this.this$0.rippleShader;
                    int color = this.this$0.rippleShader.getColor();
                    Object animatedValue = valueAnimator.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Int");
                    rippleShader.setColor(ColorUtils.setAlphaComponent(color, ((Integer) animatedValue).intValue()));
                    this.this$0.invalidate();
                }
            });
            ValueAnimator ofInt2 = ValueAnimator.ofInt(127, 0);
            ofInt2.setStartDelay(417);
            ofInt2.setDuration(1116L);
            ofInt2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.systemui.biometrics.AuthRippleView$startRipple$alphaOutAnimator$1$1
                final /* synthetic */ AuthRippleView this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    RippleShader rippleShader = this.this$0.rippleShader;
                    int color = this.this$0.rippleShader.getColor();
                    Object animatedValue = valueAnimator.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Int");
                    rippleShader.setColor(ColorUtils.setAlphaComponent(color, ((Integer) animatedValue).intValue()));
                    this.this$0.invalidate();
                }
            });
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ofFloat, ofFloat2, ofInt, ofInt2);
            animatorSet.addListener(new AnimatorListenerAdapter(this, runnable) { // from class: com.android.systemui.biometrics.AuthRippleView$startRipple$animatorSet$1$1
                final /* synthetic */ Runnable $onAnimationEnd;
                final /* synthetic */ AuthRippleView this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$onAnimationEnd = r2;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    this.this$0.rippleInProgress = true;
                    this.this$0.setVisibility(0);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    Runnable runnable2 = this.$onAnimationEnd;
                    if (runnable2 != null) {
                        runnable2.run();
                    }
                    this.this$0.rippleInProgress = false;
                    this.this$0.setVisibility(8);
                }
            });
            animatorSet.start();
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
