package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.Layout;
import android.util.SparseArray;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TextAnimator.kt */
/* loaded from: classes.dex */
public final class TextAnimator {
    private ValueAnimator animator;
    private final Function0<Unit> invalidateCallback;
    private TextInterpolator textInterpolator;
    private final SparseArray<Typeface> typefaceCache = new SparseArray<>();

    public TextAnimator(Layout layout, Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(layout, "layout");
        Intrinsics.checkNotNullParameter(function0, "invalidateCallback");
        this.invalidateCallback = function0;
        this.textInterpolator = new TextInterpolator(layout);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f);
        ofFloat.setDuration(300L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.keyguard.TextAnimator$animator$1$1
            final /* synthetic */ TextAnimator this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                TextInterpolator textInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core = this.this$0.getTextInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                textInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core.setProgress(((Float) animatedValue).floatValue());
                this.this$0.invalidateCallback.invoke();
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter(this) { // from class: com.android.keyguard.TextAnimator$animator$1$2
            final /* synthetic */ TextAnimator this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                this.this$0.getTextInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().rebase();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.this$0.getTextInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().rebase();
            }
        });
        Unit unit = Unit.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(ofFloat, "ofFloat(1f).apply {\n        duration = DEFAULT_ANIMATION_DURATION\n        addUpdateListener {\n            textInterpolator.progress = it.animatedValue as Float\n            invalidateCallback()\n        }\n        addListener(object : AnimatorListenerAdapter() {\n            override fun onAnimationEnd(animation: Animator?) {\n                textInterpolator.rebase()\n            }\n            override fun onAnimationCancel(animation: Animator?) = textInterpolator.rebase()\n        })\n    }");
        this.animator = ofFloat;
    }

    public final TextInterpolator getTextInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.textInterpolator;
    }

    public final ValueAnimator getAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.animator;
    }

    public final void updateLayout(Layout layout) {
        Intrinsics.checkNotNullParameter(layout, "layout");
        this.textInterpolator.setLayout(layout);
    }

    public final boolean isRunning() {
        return this.animator.isRunning();
    }

    public final void draw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "c");
        this.textInterpolator.draw(canvas);
    }

    public final void setTextStyle(int i, float f, Integer num, boolean z, long j, TimeInterpolator timeInterpolator, long j2, Runnable runnable) {
        if (z) {
            this.animator.cancel();
            this.textInterpolator.rebase();
        }
        if (f >= 0.0f) {
            this.textInterpolator.getTargetPaint().setTextSize(f);
        }
        if (i >= 0) {
            this.textInterpolator.getTargetPaint().setTypeface((Typeface) TextAnimatorKt.access$getOrElse(this.typefaceCache, i, new Function0<Typeface>(this, i) { // from class: com.android.keyguard.TextAnimator$setTextStyle$1
                final /* synthetic */ int $weight;
                final /* synthetic */ TextAnimator this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$weight = r2;
                }

                @Override // kotlin.jvm.functions.Function0
                public final Typeface invoke() {
                    this.this$0.getTextInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().getTargetPaint().setFontVariationSettings(Intrinsics.stringPlus("'wght' ", Integer.valueOf(this.$weight)));
                    return this.this$0.getTextInterpolator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().getTargetPaint().getTypeface();
                }
            }));
        }
        if (num != null) {
            this.textInterpolator.getTargetPaint().setColor(num.intValue());
        }
        this.textInterpolator.onTargetPaintModified();
        if (z) {
            this.animator.setStartDelay(j2);
            ValueAnimator valueAnimator = this.animator;
            if (j == -1) {
                j = 300;
            }
            valueAnimator.setDuration(j);
            if (timeInterpolator != null) {
                getAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().setInterpolator(timeInterpolator);
            }
            if (runnable != null) {
                this.animator.addListener(new AnimatorListenerAdapter(runnable, this) { // from class: com.android.keyguard.TextAnimator$setTextStyle$listener$1
                    final /* synthetic */ Runnable $onAnimationEnd;
                    final /* synthetic */ TextAnimator this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.$onAnimationEnd = r1;
                        this.this$0 = r2;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        this.$onAnimationEnd.run();
                        this.this$0.getAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().removeListener(this);
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                        this.this$0.getAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().removeListener(this);
                    }
                });
            }
            this.animator.start();
            return;
        }
        this.textInterpolator.setProgress(1.0f);
        this.textInterpolator.rebase();
    }
}
